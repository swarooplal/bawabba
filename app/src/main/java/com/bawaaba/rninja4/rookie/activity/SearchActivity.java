package com.bawaaba.rninja4.rookie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.bawaaba.rninja4.rookie.App.AppConfig;
import com.bawaaba.rninja4.rookie.App.AppController;
import com.bawaaba.rninja4.rookie.MainActivity;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.MessageEvent;
import com.bawaaba.rninja4.rookie.model.searchResult.SearchResultResponse;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;
import com.bawaaba.rninja4.rookie.utils.Utils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = SearchActivity.class.getSimpleName();
    private static Context context;
    //private ImageView ProfileImage  ;
    List<String> responseList = new ArrayList<String>();
    private Button Search;
    private EditText InputSearch;
    private EditText InputSkill;
    private AppCompatTextView InputLocation;
    //private SQLiteHandler db;
    private TextView SearchResult;
    private AppCompatTextView tvClear;
    private ImageView image;
    private AutoCompleteTextView style;
    private MultiAutoCompleteTextView simpleMultiAutoCompleteTextView;
    private TextBadgeItem textBadgeItem;
    private SQLiteHandler db;
    private SessionManager session;
    private LinearLayout linearlayout;


    public void isMessageArrived() {
        try {
            boolean isMessageArrived = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).isNewMessageArrived();
            if (isMessageArrived) {
                //showUnreadMessages();
                int total = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUnreadMessage();
                if (total > 0) {
                    textBadgeItem.setText("" + total);
                    textBadgeItem.show(false);
                } else {
                    hideText();
                }
            } else {
                hideText();
            }
        } catch (Exception e) {
        }
    }

    private void hideText(){
        textBadgeItem.setText("");
        textBadgeItem.hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        initViews();
        setOnclick();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");

       // getSupportActionBar().setCustomView(R.layout.actiontitle_layout4);
        ActionBar actionBar = getSupportActionBar();
      //  actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));

        BottomNavigationBar bottomNavigationView = (BottomNavigationBar)
                findViewById(R.id.bottom_bar);

        textBadgeItem = Utils.getTextBadge();
        ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveCurrentActivity("SearchActivity");
        isMessageArrived();
        bottomNavigationView
                .addItem(new BottomNavigationItem(R.drawable.ic_home1, "Home").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_search1, "Search").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_inbox1, "Inbox").setBadgeItem(textBadgeItem).setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_profile, "Profile").setActiveColorResource(R.color.bottomnavigation))
                .setFirstSelectedPosition(1)
                .initialise();
        isMessageArrived();
        bottomNavigationView.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        Intent to_main = new Intent(SearchActivity.this, MainActivity.class);
                        startActivity(to_main);
//                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_left);
                        finish();
                        break;
                    case 1:
                        Intent to_search = new Intent(SearchActivity.this, SearchActivity.class);
                        startActivity(to_search);
//                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_left);
                        finish();
                        break;
                    case 2:
                        if (session.isLoggedIn() && db_id != null) {
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveNewMessageArrived(false);
                            Intent to_inbox = new Intent(SearchActivity.this, com.bawaaba.rninja4.rookie.activity.ChatFunction.ChatActivity.class);
                            startActivity(to_inbox);
                            finish();
                        } else {
                            Intent to_login = new Intent(SearchActivity.this, LoginActivity.class);
                            startActivity(to_login);
                            finish();
                        }
//                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_left);
                        finish();
                        break;
                    case 3:
                        if (session.isLoggedIn() && db_id != null) {
                            Intent to_profile = new Intent(SearchActivity.this, ProfileView.class);
                            startActivity(to_profile);
                            finish();
                        }else{
                            Intent to_login = new Intent(SearchActivity.this, LoginActivity.class);
                            startActivity(to_login);
                            finish();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }
            @Override
            public void onTabReselected(int position) {
            }
        });

//
//        final ImageButton home = (ImageButton) findViewById(R.id.home);
//        final ImageButton reg = (ImageButton) findViewById(R.id.register);
        Search = (Button) findViewById(R.id.butsearch);
        InputSearch = (EditText) findViewById(R.id.textsearch);
        linearlayout=(LinearLayout)findViewById(R.id.llTop);


       // String[] androidVersionNames = {"Accountancy", "Algebra Classes", "Biology Classes", "Makeup Classes", "SQL Analyst", "Beautician", "Makeup Artist", "Babysitter", "Market Research", "Costume Designer", "2D Design", "Digital Print", "Web Designer", "Computer Engineer", "Dance Performer", "DJ", "Android Developer"};

        //InputSkill = (EditText) findViewById(R.id.textskill);
        InputLocation = (AppCompatTextView) findViewById(R.id.locations);
        simpleMultiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.textskill);
      //  multiAutocompleteUpdate();
       // new HttpGetTask().execute();

        simpleMultiAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchActivity.this, ActivitySearchSetSkills.class);
                startActivityForResult(i, 2);
            }
        });

        linearlayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String keyword = InputSearch.getText().toString().trim();
                // String skills = InputSkill.getText().toString().trim();
                String skills = simpleMultiAutoCompleteTextView.getText().toString().trim();
                String location = InputLocation.getText().toString().trim();
                searchUser(keyword, skills, location);

            }
        });
        EventBus.getDefault().register(this);

    }

    private void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        isMessageArrived();
    }


    private void multiAutocompleteUpdate() {

        ArrayAdapter<String> versionNames = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, responseList);
        simpleMultiAutoCompleteTextView.setAdapter(versionNames);
        simpleMultiAutoCompleteTextView.setThreshold(1);
        simpleMultiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

    }
    private void setOnclick() {
        tvClear.setOnClickListener(this);
    }

    private void initViews() {
        tvClear =(AppCompatTextView)findViewById(R.id.tvClear);
    }

    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
                InputLocation.setText(place.getAddress());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }else if (requestCode==2){
            if (data==null){
                simpleMultiAutoCompleteTextView.setText("");

                return;
            }
            Bundle MBuddle = data.getExtras();
            String MMessage = MBuddle .getString("result");
            if (MMessage!=null){
                simpleMultiAutoCompleteTextView.setText(MMessage);
            }else{
                simpleMultiAutoCompleteTextView.setText("");

            }



        }
    }

    private void searchUser(final String keyword, final String skills, final String location) {

        if (keyword.isEmpty() && skills.isEmpty() && location.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please provide any search condition", Toast.LENGTH_LONG).show();
        } else {
            String tag_string_req = "req_search";
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_SEARCH, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "Search Responses: " + response.toString());

                    try {

                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {
                            SearchResultResponse resultResponse = new Gson().fromJson(response, SearchResultResponse.class);
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).setSearchResult(response);
                            JSONArray user = jObj.getJSONArray("user");

                            Intent to_searchresult = new Intent(SearchActivity.this, SearchResult.class);
                            to_searchresult.putExtra("search_result", user.toString());
                            startActivity(to_searchresult);

                        } else {
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
//
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "No profiles are available!", Toast.LENGTH_LONG).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to search url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("keyword", keyword);
                    params.put("skills", skills);
                    params.put("location", location);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvClear:
                clear_search();
                break;

            default:
                break;

        }

    }

    private void clear_search() {

        InputSearch.setText("");
        InputLocation.setText("");
        simpleMultiAutoCompleteTextView.setText("");
    }
    private class HttpGetTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String url = "http://test378.bawabba.com/assets/skills.json";
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONArray json = new JSONArray(jsonStr);


                     Log.e("ResponseCity", jsonStr);
                    for (int i = 0; i < json.length(); i++) {
                        final JSONObject e = json.getJSONObject(i);
                        String skill = e.getString("name");
                        responseList.add(skill);

                        Log.e("ResponseCity", String.valueOf(responseList));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}