package com.bawaaba.rninja4.rookie.activity.ProfileTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.activity.adapters.LanguagesListAdapter;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.dashboard_new.ProfileViewFragment;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.Skill;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.bawaaba.rninja4.rookie.utils.AppPreference;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;
import com.google.gson.Gson;

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

public class Edit_Languages extends BaseActivity implements View.OnClickListener {

    List<Skill> skillsNew = new ArrayList<>();
    private RecyclerView rvskills;
    private AppCompatTextView tvSave;
    private AppCompatEditText tvSearch;
    private String user_id;
    private String langauges_string;
    private List<String> selectedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_languages);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        Bundle bundle = getIntent().getExtras();
        user_id = bundle.getString("user_id");
        langauges_string = bundle.getString("language");

        initViews();
        apiCall();
        searchLanguages();
    }

    private void searchLanguages() {
    }

    private void initViews() {
        rvskills = (RecyclerView) findViewById(R.id.rvskills);
        tvSave = (AppCompatTextView) findViewById(R.id.tvSave);
        tvSearch = (AppCompatEditText) findViewById(R.id.tvSearchHere);

        tvSave.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Edit_Languages.this, LinearLayoutManager.VERTICAL, false);
        rvskills.setLayoutManager(layoutManager);

        LanguagesListAdapter registerSkillsRecyclerviewAdapter = new LanguagesListAdapter(getApplicationContext(), new ArrayList<Skill>());
        rvskills.setAdapter(registerSkillsRecyclerviewAdapter);
    }


    private void apiCall() {
        String url = "assets/languages.json";
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(Edit_Languages.this).getApiService().getCategorySkills(url);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            skillsNew = new ArrayList<Skill>();
                            try {
                                JSONArray languages = new JSONArray(responseString);
                                for (int i = 0; i < languages.length(); i++) {
                                    JSONObject c = languages.getJSONObject(i);
                                    String name = c.getString("name");
                                    String id = c.getString("id");
                                    Skill skill = new Skill();
                                    skill.setId(id);
                                    skill.setName(name);
                                    skillsNew.add(skill);
                                }
                                setAdapter(skillsNew);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Edit_Languages.this, "failed to load..", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void setAdapter(final List<Skill> skills) {
        String response = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileResponse();
        final Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);
        if (profileresponse.getUserData().getLanguage().size() > 0) {
            List<String> checkedItems = new ArrayList<>();
            for (int i = 0; i < profileresponse.getUserData().getLanguage().size(); i++) {
                checkedItems.add(String.valueOf(profileresponse.getUserData().getLanguage().get(i).getId()));
            }
          ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).setCheckedLangauges(checkedItems);

            // Log.e("language number",ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).setCheckedLangauges(checkedItems);
//            if(ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).setCheckedLangauges(checkedItems)>10){
//                Toast.makeText(Edit_Languages.this, "Maximum 10 languages are allowed.", Toast.LENGTH_SHORT).show();
//                return;
//            }
        }


        LanguagesListAdapter registerSkillsRecyclerviewAdapter = new LanguagesListAdapter(getApplicationContext(), skills);
        rvskills.setAdapter(registerSkillsRecyclerviewAdapter);

        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i0, int i1, int i2) {
                String st = charSequence.toString();
                List<Skill> skillsearch = new ArrayList<>();
                for (int i = 0; i < skills.size(); i++) {
                    if (skills.get(i).getName().toLowerCase().contains(st.toString().toLowerCase())) {
                        skillsearch.add(skills.get(i));
                    }
                }
                LanguagesListAdapter registerSkillsRecyclerviewAdapter = new LanguagesListAdapter(getApplicationContext(), skillsearch);
                rvskills.setAdapter(registerSkillsRecyclerviewAdapter);
                registerSkillsRecyclerviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSave:
                apiCallToSaveData();
                break;
            default:
                break;
        }
    }

    private void apiCallToSaveData() {
        System.out.println("SkillEditActivity.apiCallToSaveData");
        ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).getCheckedItems();


        String items = "";
        for (int i = 0; i < ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).getCheckedLangauges().size(); i++) {
            if (i == 0) {
                items = ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).getCheckedLangauges().get(i);
            } else {
                items = items + " ," + ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).getCheckedLangauges().get(i);
            }
        }
        String language_number= String.valueOf(ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).getCheckedLangauges().size());
        Log.e("languagenumber",language_number);

        String delete_all="";
        if(language_number.equals("0")){
            delete_all="yes";
        }else{
            delete_all="no";
        }
Log.e("deleteall",delete_all);
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(Edit_Languages.this).getApiService().updateLangauge("app-client", "123321", user_id,
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(), user_id, items,delete_all
        );

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            Log.e("responseldhhjsdc", String.valueOf(responseString));
                            Log.e("responselang", String.valueOf(jsonObject));

                            if (jsonObject != null) {
                                if (!jsonObject.getBoolean("error")) {
                                    Toast.makeText(Edit_Languages.this, "Your languages have been updated successfully", Toast.LENGTH_SHORT).show();
                                    AppPreference appPreference=ObjectFactory.getInstance().getAppPreference(getApplicationContext());
                                    BaseBottomHelperActivity.start(getApplicationContext(), ProfileViewFragment.class.getName(),appPreference.getUserId(),appPreference.getUserName());
                                    /*Intent intent = new Intent(Edit_Languages.this, ProfileView.class);
                                    startActivity(intent);*/
                                    finish();
                                } else {
                                    Toast.makeText(Edit_Languages.this, "Some error occurred", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Edit_Languages.this, "failed to load..", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
