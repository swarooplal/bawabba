package com.bawaaba.rninja4.rookie.Account;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.activity.adapters.RegisterSkillsRecyclerviewAdapter;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.RegisterSkillsResponse;
import com.bawaaba.rninja4.rookie.model.Skill;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubcategoryListActivity extends AppCompatActivity implements View.OnClickListener {

    private String id;
    private RecyclerView rvskills;
    private AppCompatButton btnSubmit;
    List<String> selectedItems = new ArrayList<>();
    private String skills;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory_list);
        getSupportActionBar().hide();
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        skills = bundle.getString("skills");
        rvskills = (RecyclerView) findViewById(R.id.rvskills);
        btnSubmit = (AppCompatButton) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        apiCall();
        rvskills.setLayoutManager(layoutManager);
        ObjectFactory.getInstance().getNetworkManager(SubcategoryListActivity.this).setCheckedItems(selectedItems);
          Log.e("selected skillsonly", String.valueOf(selectedItems));
        //  selectedItems.clear();

    }
    private void apiCall() {
        String url = "api/user/get_all_skills?category=" + id;
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(SubcategoryListActivity.this).getApiService().getCategorySkills(url);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            if (jsonObject != null) {
                                RegisterSkillsResponse skillsResponse = new Gson().fromJson(responseString, RegisterSkillsResponse.class);
                                if (skillsResponse.getSkills().size() > 0) {
                                    setAdapter(skillsResponse.getSkills());
                                }
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
                Toast.makeText(SubcategoryListActivity.this, "failed to load..", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setAdapter(List<Skill> subcategory) {
        RegisterSkillsRecyclerviewAdapter registerSkillsRecyclerviewAdapter = new RegisterSkillsRecyclerviewAdapter(getApplicationContext(), subcategory);
        rvskills.setAdapter(registerSkillsRecyclerviewAdapter);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).getCheckedItems();
                List<String> items = ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).getCheckedItems();
//                if((ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).getCheckedItems().size()>10)){
//                    Toast.makeText(SubcategoryListActivity.this, "Maximum 10 skills are allowed.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if(items.size() > 0) {
                    String skills = "";
                    for (int i = 0; i < items.size(); i++) {
                        if (i == 0) {
                            skills = items.get(i);
                        } else {
                            skills = skills + "," + items.get(i);
                        }
                    }
                    final Dialog dialog = ObjectFactory.getInstance().getUtils(SubcategoryListActivity.this).showLoadingDialog(SubcategoryListActivity.this);
                    dialog.show();
                    Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(SubcategoryListActivity.this).getApiService().changeType(
                            "app-client",
                            "123321",
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(), ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                            "0",
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getDescription(),
                            id,
                            skills
                    );
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.body() != null) {
                                try {
                                    String responseString = new String(response.body().bytes());
                                    if (responseString != null) {
                                        JSONObject jsonObject = new JSONObject(responseString);
                                        System.out.println("PortfolioVideoEditActivity.onResponse " + responseString);
                                        if (jsonObject != null) {
                                            if (!jsonObject.getBoolean("error")) {
                                                Toast.makeText(SubcategoryListActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(SubcategoryListActivity.this, RegisterActivity.class);
//                                        startActivity(intent);
//                                        finish();
                                                Intent intent = new Intent(SubcategoryListActivity.this, ProfileView.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finishAffinity();
                                            }else {
                                                Toast.makeText(SubcategoryListActivity.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                                            }
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
                            Toast.makeText(SubcategoryListActivity.this, "failed..", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Please select your skills", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            default:
                break;
        }
    }
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
      //  Intent intent = new Intent(SubcategoryListActivity.this, SwitchToWorkActivity.class);
        super.onBackPressed();
        ObjectFactory.getInstance().getNetworkManager(SubcategoryListActivity.this).getCheckedItems().clear();
    }
}