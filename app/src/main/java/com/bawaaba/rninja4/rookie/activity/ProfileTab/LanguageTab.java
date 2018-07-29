package com.bawaaba.rninja4.rookie.activity.ProfileTab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.JSONParser;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.lujun.androidtagview.ColorFactory;
import co.lujun.androidtagview.TagContainerLayout;

public class LanguageTab extends AppCompatActivity {

    private String TAG = LanguageTab.class.getSimpleName();
    private ProgressDialog pDialog;

    private AppCompatTextView LanguageName;
    private AppCompatTextView language_count;
    private TextView editLanguage;


    private SQLiteHandler db;
    private SessionManager session;
    private String languages_string;


    JSONParser jsonParser = new JSONParser();

    ArrayList<HashMap<String, String>> LanguageList;
    JSONArray language = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_tab);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        LanguageList = new ArrayList<HashMap<String, String>>();
        // LanguageName = (AppCompatTextView) findViewById(R.id.tab_lang);
        TagContainerLayout mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tagcontainerLayout);
        Log.e("LanguageCheck: ", "Arjun1");

        editLanguage = (TextView) findViewById(R.id.edit_lang);
        editLanguage.setTypeface(null, Typeface.BOLD);


        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        Intent i1 = getIntent();
        final String user_id = i1.getStringExtra("user_id");
        languages_string = i1.getStringExtra("language");

        mTagContainerLayout.setTheme(ColorFactory.NONE);
        mTagContainerLayout.setTagBackgroundColor(Color.TRANSPARENT);

        HashMap<String, String> user = db.getUserDetails();
        String db_id = user.get("uid"); // value from db when logged in

        if (session.isLoggedIn() && (db_id.equals(user_id) || (user_id == null))) {
            editLanguage.setVisibility(View.VISIBLE);
        } else {
            editLanguage.setVisibility(View.GONE);
        }
        editLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent to_editlanguages = new Intent(LanguageTab.this, Edit_Languages.class);
                to_editlanguages.putExtra("user_id", user_id);
                to_editlanguages.putExtra("language", languages_string);
                startActivity(to_editlanguages);
            }
        });

        Intent from_Profile = getIntent();
        String language = from_Profile.getStringExtra("language");
        Log.e(TAG, "LangCheck: " + language);

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(language);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int count = jsonArray.length();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tv = new TextView(getApplicationContext());

            try {
                tv.setText(jsonObject.getString("language"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("language", String.valueOf(count));

        language_count = (AppCompatTextView) findViewById(R.id.lang_count);

        if (count == 0) {
            Log.e("sdgfjdf", "sarg");
            language_count.setVisibility(View.VISIBLE);
            mTagContainerLayout.setVisibility(View.GONE);

        } else {
            language_count.setVisibility(View.GONE);
            mTagContainerLayout.setVisibility(View.VISIBLE);

            Log.e("sdgfjdf", "fkugdsfu");
        }
        language_count.setTypeface(null, Typeface.NORMAL);
        ArrayList<String> list = new ArrayList<String>();

        try {

            JSONArray language_data = new JSONArray(language);
            for (int i = 0; i < language_data.length(); i++) {
                JSONObject object = language_data.getJSONObject(i);
                String name = object.getString("name");

                if (name != null) {
                    list.add(String.valueOf(name));

                    Log.e("skillcheck2", String.valueOf(list));

                    mTagContainerLayout.setTags(list);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");
        Intent from_profile = getIntent();
        String user_id = from_profile.getStringExtra("user_id");
        if (session.isLoggedIn() && db_id.equals(user_id)) {
            BaseBottomHelperActivity.start(LanguageTab.this,null,null,null);
            /*Intent intent = new Intent(LanguageTab.this, com.bawaaba.rninja4.rookie.MainActivity.class);
            startActivity(intent);*/
            finish();
        } else {
            super.onBackPressed();
        }
    }
}

