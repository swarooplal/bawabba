package com.bawaaba.rninja4.rookie.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.JSONParser;
import com.bawaaba.rninja4.rookie.MainActivity;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.adapters.RegisterSkillsRecyclerviewAdapter;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.RegisterSkillsResponse;
import com.bawaaba.rninja4.rookie.model.Skill;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Register_Skills extends BaseActivity implements View.OnClickListener {

    JSONParser jsonParser = new JSONParser();
    ArrayList RegisterskillsList;
    private ProgressDialog pDialog;
    private String role;
    private String fullname;
    private String email;
    private String password;
    private String DateofBirth;
    private String location;
    private String contactNumber;
    private String description;
    private String gender;
    private String category;
    private String profile_image;
    private RecyclerView rvskills;
    private AppCompatTextView tvComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerskill_list);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//         getSupportActionBar().setCustomView(R.layout.actiontitle_layout26);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        category = bundle.getString("category");
        role = bundle.getString("role");
        fullname = bundle.getString("fullname");
        email = bundle.getString("email");
        password = bundle.getString("password");
        DateofBirth = bundle.getString("dob");
        contactNumber = bundle.getString("phone");
        location = bundle.getString("location");
        description = bundle.getString("description");
        gender = bundle.getString("gender");
        profile_image=bundle.getString("profile_img");
        rvskills = (RecyclerView) findViewById(R.id.rvskills);
        tvComplete = (AppCompatTextView) findViewById(R.id.tvComplete);
        tvComplete.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Register_Skills.this, LinearLayoutManager.VERTICAL, false);
        rvskills.setLayoutManager(layoutManager);
        RegisterskillsList = new ArrayList<>();
        apiCall();
    }

    private void apiCall() {
        String url = "api/user/get_all_skills?category=" + category;
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(Register_Skills.this).getApiService().getCategorySkills(url);
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
                                    Log.e("skillresponse", String.valueOf(skillsResponse));
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
                Toast.makeText(Register_Skills.this, "failed to load..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAdapter(List<Skill> subcategory) {
        RegisterSkillsRecyclerviewAdapter registerSkillsRecyclerviewAdapter = new RegisterSkillsRecyclerviewAdapter(getApplicationContext(), subcategory);
        rvskills.setAdapter(registerSkillsRecyclerviewAdapter);
        Log.e("Registers", String.valueOf(rvskills));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvComplete:
                apiCallTOSignUp();
                break;
            default:
                break;
        }
    }


    private void apiCallTOSignUp() {
        String skills = "";
        for (int i = 0; i < ObjectFactory.getInstance().getNetworkManager(Register_Skills.this).getCheckedItems().size(); i++) {
            if (i == 0) {
                skills = ObjectFactory.getInstance().getNetworkManager(Register_Skills.this).getCheckedItems().get(i);
            } else
                skills = skills + " ," + ObjectFactory.getInstance().getNetworkManager(Register_Skills.this).getCheckedItems().get(i);
        }
        if(skills.length()==0){
            Toast.makeText(this, "Please select your skills", Toast.LENGTH_SHORT).show();
            return;
        }

//        if((ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).getCheckedItems().size()>10)){
//            Toast.makeText(Register_Skills.this, "Maximum 10 skills are allowed.", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if(profile_image.isEmpty()){
            profile_image="no";
        }
        final Dialog dialog = ObjectFactory.getInstance().getUtils(Register_Skills.this).showLoadingDialog(Register_Skills.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(Register_Skills.this).getApiService().signUp("app-client", "123321",email,fullname,location,skills,
                description,category,gender,password,DateofBirth,contactNumber,role,profile_image);


        final String finalSkills = skills;
        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        Log.e("category",category);
                        Log.e("role",role);
                        Log.e("fullname",fullname);
                        Log.e("email",email);
                        Log.e("password",password);
                        Log.e("dob",DateofBirth);
                        Log.e("location",location);
                        Log.e("description",description);
                        Log.e("gender",gender);
                        Log.e("phone",contactNumber);
                        // Log.e("profile_img",profile_image);

                        Log.e("skills", finalSkills);

                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            if (jsonObject != null) {
                                if (!jsonObject.getBoolean("error")) {

                                    Log.e("regerror","responseString");
                                    Toast.makeText(Register_Skills.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    BaseBottomHelperActivity.start(getApplicationContext(),null,null,null);
                                    /*Intent intent = new Intent(Register_Skills.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);*/
                                    finishAffinity();
                                } else {
                                    Toast.makeText(Register_Skills.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Register_Skills.this, "failed to load..", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private class registerSkill extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... args) {

            Intent from_registercategory = getIntent();
            final int position = from_registercategory.getIntExtra("id", 0);
            String Client_service = "app-client";
            String Auth_key = "123321";
            String token = "";
            String user_id = "";
            String Register_skill_URL = "http://demo.rookieninja.com/api/user/category?category=" + position;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jsonParser.makeHttpRequest(Register_skill_URL, "GET",
                    params, Client_service, Auth_key, token, user_id);
            Log.e("registerskill JSON: ", json.toString());

            try {
                JSONArray subcategory = json.getJSONArray("subcategory");
                // looping through All messages
                for (int i = 0; i < subcategory.length(); i++) {
                    //     final String skill = skills.getString(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ObjectFactory.getInstance().getNetworkManager(Register_Skills.this).getCheckedItems().clear();
    }
 }
