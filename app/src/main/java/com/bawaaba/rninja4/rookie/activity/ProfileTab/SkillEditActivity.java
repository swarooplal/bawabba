package com.bawaaba.rninja4.rookie.activity.ProfileTab;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.adapters.RegisterSkillsRecyclerviewAdapter;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.dashboard_new.ProfileViewFragment;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.RegisterSkillsResponse;
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

public class SkillEditActivity extends BaseActivity implements View.OnClickListener {
    List<String> selectedItems = new ArrayList<>();
    private String skills;
    private String user_id;
    public String category_selection;
    private RecyclerView rvskills;
    private AppCompatTextView tvSave;
    private AppCompatEditText tvSearch;
    String old_value;

    private DrawerLayout mDrawer;
    private Toolbar toolbar2;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_skill_edit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        nvDrawer.setItemIconTintList(null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        skills = bundle.getString("skills");
        user_id = bundle.getString("user_id");
        category_selection = bundle.getString("category");
        old_value = category_selection;
        Log.d("catoger>>>1", category_selection);
        Log.d("catoger_old>>>1", old_value);
        initViews();
        searchLanguages();
        Log.e("skillcheckuseredit", String.valueOf(skills));
        try {
            JSONArray skill_data = new JSONArray(skills);
            Log.e("skilldatachecking", String.valueOf(skill_data));
            for (int i = 0; i < skill_data.length(); i++) {
                final String skill = skill_data.getString(i);
//                textSkills.append(skill + "\t \t \t");
                selectedItems.add(skill);
            }
            for (int i = 0; i < selectedItems.size(); i++) {
                System.out.println("SkillEditActivity.onCreate " + selectedItems.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setupDrawerContent(nvDrawer);
        ObjectFactory.getInstance().getNetworkManager(SkillEditActivity.this).setCheckedItems(selectedItems);
        apiCall(category_selection);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawer.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawer.closeDrawer(Gravity.RIGHT);
                } else {
                    mDrawer.openDrawer(Gravity.RIGHT);
                }
            }
        });
    }

    private void searchLanguages() {
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            String category = "0";

            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {
                menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        apiCall(String.valueOf(category));
                        mDrawer.closeDrawer(Gravity.RIGHT);
                        //selectedItems.clear();
                        Log.e("positionof category", String.valueOf(category));
                        return true;
                    }
                });

                switch (menuItem.getItemId()) {
                    case R.id.nav_menu1:
                        category = "0";
                        category_selection = category;
                        Log.d("catoger>>>2", category + "");
                        Log.d("catoger_old>>>1", old_value);
                        break;
                    case R.id.nav_menu2:
                        category = "1";
                        category_selection = category;
                        Log.d("catoger>>>2", category_selection);
                        Log.d("catoger_old>>>1", old_value);
                        break;
                    case R.id.nav_menu3:
                        category = "2";
                        category_selection = category;
                        Log.d("catoger>>>2", category + "");
                        Log.d("catoger_old>>>1", old_value);
                        break;
                    case R.id.nav_menu4:
                        Log.d("catoger>>>2", category + "");
                        category = "3";
                        category_selection = category;
                        break;
                    case R.id.nav_menu5:
                        category = "4";
                        category_selection = category;
                        break;
                    case R.id.nav_menu6:
                        category = "5";
                        category_selection = category;
                        break;
                    case R.id.nav_menu7:
                        category = "6";
                        category_selection = category;
                        break;
                    case R.id.nav_menu8:
                        category = "7";
                        category_selection = category;
                        break;
                    case R.id.nav_menu9:
                        category = "8";
                        category_selection = category;
                        break;
                    case R.id.nav_menu10:
                        category = "9";
                        category_selection = category;
                        break;
                    case R.id.nav_menu11:
                        category = "10";
                        category_selection = category;
                        break;
                    case R.id.nav_menu12:
                        category = "11";
                        category_selection = category;
                        break;
                    case R.id.nav_menu13:
                        category = "12";
                        category_selection = category;
                        break;
                    case R.id.nav_menu14:
                        category = "13";
                        category_selection = category;
                        break;
                    case R.id.nav_menu15:
                        category = "14";
                        category_selection = category;
                        break;
                    case R.id.nav_menu16:
                        category = "15";
                        category_selection = category;
                        break;
                    case R.id.nav_menu17:
                        category = "16";
                        category_selection = category;
                        break;
                    case R.id.nav_menu18:
                        category = "17";
                        category_selection = category;
                        break;
                    case R.id.nav_menu19:
                        category = "18";
                        category_selection = category;
                        break;
                    case R.id.nav_menu20:
                        category = "19";
                        category_selection = category;
                        break;

                }
                return true;
            }
        });
    }

    private void initViews() {
        tvSearch = (AppCompatEditText) findViewById(R.id.tvSearchHere);
        rvskills = (RecyclerView) findViewById(R.id.rvskills);
        tvSave = (AppCompatTextView) findViewById(R.id.tvSave);

        tvSave.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SkillEditActivity.this, LinearLayoutManager.VERTICAL, false);
        rvskills.setLayoutManager(layoutManager);

        RegisterSkillsRecyclerviewAdapter registerSkillsRecyclerviewAdapter = new RegisterSkillsRecyclerviewAdapter(getApplicationContext(), new ArrayList<Skill>());
        rvskills.setAdapter(registerSkillsRecyclerviewAdapter);
    }

    private void apiCall(String category) {

        // swar0op tweaked the code
        if (category_selection.equals(old_value)) {
            Toast.makeText(getApplicationContext(), "Category Not changed", Toast.LENGTH_LONG).show();
        } else {
            selectedItems.clear();
            ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).setCheckedItems(selectedItems);
        }

        Log.d("catoger>>>2", category);
        // String url = "api/user/get_all_skills?category=" + category;
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(SkillEditActivity.this).getApiService().getCategorySkillsinInside(category);
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
//                                if(skillsResponse.getSkills().size()>11){
//                                    Toast.makeText(SkillEditActivity.this, "Maximum 10 skills to select", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }

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
                Toast.makeText(SkillEditActivity.this, "failed to load..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter(final List<Skill> subcategory) {
        String response = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileResponse();
        final Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);
        if (old_value.equals(category_selection))
        {
            if (profileresponse.getUserData().getSkills().size() > 0) {
                List<String> checkedItems = new ArrayList<>();
                for (int i = 0; i < profileresponse.getUserData().getSkills().size(); i++) {
                    checkedItems.add(String.valueOf(profileresponse.getUserData().getSkills().get(i)));
                }
                ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).setCheckedItems(checkedItems);
            }
        }


        RegisterSkillsRecyclerviewAdapter registerSkillsRecyclerviewAdapter = new RegisterSkillsRecyclerviewAdapter(getApplicationContext(), subcategory);
        rvskills.setAdapter(registerSkillsRecyclerviewAdapter);

        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i0, int i1, int i2) {
                String st = charSequence.toString();
                List<Skill> skillsearch = new ArrayList<>();

                for (int i = 0; i < subcategory.size(); i++) {
                    if (subcategory.get(i).getName().toLowerCase().contains(st.toString().toLowerCase())) {
                        skillsearch.add(subcategory.get(i));
                    }
                }
                RegisterSkillsRecyclerviewAdapter registerSkillsRecyclerviewAdapter = new RegisterSkillsRecyclerviewAdapter(getApplicationContext(), skillsearch);
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
        Log.e("checkeditemslist", String.valueOf(ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).getCheckedItems()));
        Log.e("checkeditemslistsize", String.valueOf(ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).getCheckedItems().size()));
        if ((ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).getCheckedItems().size() > 10)) {
            Toast.makeText(SkillEditActivity.this, "Maximum 10 skills are allowed.", Toast.LENGTH_SHORT).show();
            return;
        }
        String items = "";
        for (int i = 0; i < ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).getCheckedItems().size(); i++) {
            if (i == 0) {
                items = ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).getCheckedItems().get(i);
                Log.e("checkeditemscheck", items);
            } else {
                items = items + "," + ObjectFactory.getInstance().getNetworkManager(getApplicationContext()).getCheckedItems().get(i);
            }
        }
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(SkillEditActivity.this).getApiService().updateSkill("app-client", "123321", user_id,
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(), user_id, items, category_selection
        );
        Log.e("user_id", user_id);
        Log.e("items", items);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            if (jsonObject != null) {
                                if (!jsonObject.getBoolean("error")) {
                                    Toast.makeText(SkillEditActivity.this, "Your skills have been updated successfully", Toast.LENGTH_SHORT).show();
                                    AppPreference appPreference = ObjectFactory.getInstance().getAppPreference(getApplicationContext());
                                    BaseBottomHelperActivity.start(getApplicationContext(), ProfileViewFragment.class.getName(), appPreference.getUserId(), appPreference.getUserName());
                                   /* Intent intent = new Intent(SkillEditActivity.this, ProfileView.class);
                                    startActivity(intent);*/
                                    finish();
                                } else {
                                    Toast.makeText(SkillEditActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SkillEditActivity.this, "failed to load..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

