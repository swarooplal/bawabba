package com.bawaaba.rninja4.rookie.activity.ProfileTab;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.AddOtherUrl;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOtherUrlActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llOtherUrlLayout;
    private AppCompatButton btnAddServices;
    private ArrayList<AppCompatEditText> listTittle = new ArrayList<>();
    private ArrayList<AppCompatEditText> listUrl = new ArrayList<>();
    private ArrayList<AppCompatEditText> listDes = new ArrayList<>();
    private AppCompatTextView tvSave;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_other_url);

        getSupportActionBar().hide();
        initViews();
        // llOtherUrlLayout.removeAllViews();
    }

    private void initViews() {
        llOtherUrlLayout = (LinearLayout) findViewById(R.id.llOtherUrlLayout);
        btnAddServices = (AppCompatButton) findViewById(R.id.btnAddServices);
        tvSave = (AppCompatTextView) findViewById(R.id.tvSave);
        addServices();
        btnAddServices.setOnClickListener(this);
        tvSave.setOnClickListener(this);

    }

    private void addServices() {
        final View view = AddOtherUrlActivity.this.getLayoutInflater().inflate(R.layout.layout_add_other_url, null);
        final AppCompatEditText etTittle = (AppCompatEditText) view.findViewById(R.id.etTittle);
        final AppCompatEditText etUrl = (AppCompatEditText) view.findViewById(R.id.etUrl);
        final AppCompatEditText etDescription = (AppCompatEditText) view.findViewById(R.id.etDescription);
        final ImageView ivDelete=(ImageView)view.findViewById(R.id.ivDelete);
        ivDelete.setTag(count);
        listTittle.add(etTittle);
        listUrl.add(etUrl);
        listDes.add(etDescription);
        llOtherUrlLayout.addView(view);
        count = count + 1;
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listTittle.remove(etTittle);
                listUrl.remove(etUrl);
                listDes.remove(etDescription);
                llOtherUrlLayout.removeView(view);
            }
        });

        // llOtherUrlLayout.addView(view);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnAddServices:
                addServices();
                break;
            case R.id.tvSave:
                if (validFields())
                    apiCallToSaveData();
                break;
            default:
                break;
        }

    }

    private boolean validFields() {
        boolean status = true;
        for (int i = 0; i < listTittle.size(); i++) {
            if (TextUtils.isEmpty(listTittle.get(i).getText().toString().trim())) {
                status = false;
                listTittle.get(i).setError("");
            }
            if (TextUtils.isEmpty(listDes.get(i).getText().toString().trim())) {
                status = false;
                listDes.get(i).setError("");
            }
            if (TextUtils.isEmpty(listUrl.get(i).getText().toString().trim())) {
                status = false;
                listUrl.get(i).setError("");
            }
        }
        return status;
    }

    private void apiCallToSaveData() {

        HashMap<String, String> postvalue = new HashMap<>();
        ArrayList<AddOtherUrl> addOtherUrls = new ArrayList<>();
        for (int i = 0; i < listDes.size(); i++) {
            AddOtherUrl otherUrl = new AddOtherUrl();
            otherUrl.setDescription(listDes.get(i).getText().toString().trim());
            otherUrl.setTittle(listTittle.get(i).getText().toString().trim());
            otherUrl.setUrl(listUrl.get(i).getText().toString().trim());
            addOtherUrls.add(otherUrl);
        }
        try {
            String response = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileResponse();
            final Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);
            for (int i = 0; i < profileresponse.getUserData().getPortfolioLink().size(); i++) {
                AddOtherUrl otherUrl = new AddOtherUrl();
                otherUrl.setDescription(profileresponse.getUserData().getPortfolioLink().get(i).getDescription());
                otherUrl.setTittle(profileresponse.getUserData().getPortfolioLink().get(i).getTitle());
                otherUrl.setUrl(profileresponse.getUserData().getPortfolioLink().get(i).getLinks());
                addOtherUrls.add(otherUrl);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

        for (int i = 0; i < addOtherUrls.size(); i++) {
            postvalue.put("title[" + i + "]", addOtherUrls.get(i).getTittle());
            postvalue.put("description[" + i + "]", addOtherUrls.get(i).getDescription());
            postvalue.put("link[" + i + "]", addOtherUrls.get(i).getUrl());
        }
        final Dialog dialog = ObjectFactory.getInstance().getUtils(AddOtherUrlActivity.this).showLoadingDialog(AddOtherUrlActivity.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(AddOtherUrlActivity.this).getApiService().addotherUrl("app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                postvalue
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            System.out.println("AddServiceActivity.onResponse" + responseString);
                            if (!jsonObject.getBoolean("error")) {
                                Toast.makeText(AddOtherUrlActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();

                            } else {
//                                Toast.makeText(AddOtherUrlActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                                Toast.makeText(AddOtherUrlActivity.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(AddOtherUrlActivity.this, "failed to load..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
