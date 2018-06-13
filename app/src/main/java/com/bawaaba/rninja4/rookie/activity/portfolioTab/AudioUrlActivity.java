package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioUrlActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llUrl;
    private AppCompatButton tvAddMore;
    private ArrayList<AppCompatEditText> urlEditTexts = new ArrayList<>();
    private ArrayList<AppCompatEditText> urletAddTittle = new ArrayList<>();
    private AppCompatTextView tvSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_url);
        getSupportActionBar().hide();
        initViews();
    }
    private void initViews() {
        llUrl = (LinearLayout) findViewById(R.id.llUrl);
        tvAddMore = (AppCompatButton) findViewById(R.id.tvAddMore);
        tvSave = (AppCompatTextView) findViewById(R.id.tvSave);
        addUrls();
        tvAddMore.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvAddMore:
                addUrls();
                break;
            case R.id.tvSave:
                saveApi();
                break;
            default:
                break;
        }
    }
    private void saveApi() {
        boolean status = true;
        for (int i = 0; i < urlEditTexts.size(); i++) {
            if (TextUtils.isEmpty(urlEditTexts.get(i).getText().toString().trim())) {
               // urlEditTexts.get(i).setError("");
                urlEditTexts.get(i).setBackgroundResource(R.drawable.red_alert_round);
                Toast.makeText(AudioUrlActivity.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                status = false;
            }
        }
        for (int i = 0; i < urletAddTittle.size(); i++) {
            if (TextUtils.isEmpty(urletAddTittle.get(i).getText().toString().trim())) {
              //  urletAddTittle.get(i).setError("");
                urletAddTittle.get(i).setBackgroundResource(R.drawable.red_alert_round);
                Toast.makeText(AudioUrlActivity.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                status = false;
            }
        }
        if (status) {
            HashMap<String, String> postvalue = new HashMap<>();
            HashMap<String, String> post = new HashMap<>();
            for (int i = 0; i < urlEditTexts.size(); i++) {
                postvalue.put("url[" + i + "]", urlEditTexts.get(i).getText().toString().trim());
                System.out.println("VideoAddActivity.addUrlsApi " + urlEditTexts.get(i).getText().toString().trim());
            }
            for (int i = 0; i < urletAddTittle.size(); i++) {
                post.put("title[" + i + "]", urletAddTittle.get(i).getText().toString().trim());
                System.out.println("VideoAddActivity.addUrlsApi " + urletAddTittle.get(i).getText().toString().trim());
            }
            RequestBody userId =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), String.valueOf(ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId()));
            RequestBody uploadType =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), "url");

            RequestBody table_name =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), "portfolio_vid");
            final Dialog dialog = ObjectFactory.getInstance().getUtils(AudioUrlActivity.this).showLoadingDialog(AudioUrlActivity.this);
            dialog.show();
            Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(AudioUrlActivity.this).getApiService().uploadAudioUrls(
                    "app-client",
                    "123321",
                    ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                    ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                    ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                    "url",
                    postvalue,
                    post,
                    "portfolio_aud"
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
                                    Toast.makeText(AudioUrlActivity.this, "Successfully added.", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(AudioUrlActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AudioUrlActivity.this, "failed to load..", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void addUrls() {
        final View view = AudioUrlActivity.this.getLayoutInflater().inflate(R.layout.layout_add_audio_url, null);
        final AppCompatEditText etAddUrl = (AppCompatEditText) view.findViewById(R.id.etAddUrl);
        final AppCompatEditText etAddTittle = (AppCompatEditText) view.findViewById(R.id.etAddTittle);
        ImageView ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        urlEditTexts.add(etAddUrl);
        urletAddTittle.add(etAddTittle);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                urlEditTexts.remove(etAddTittle);
                urlEditTexts.remove(etAddUrl);
                llUrl.removeView(view);
            }
        });
        llUrl.addView(view);
    }
}

