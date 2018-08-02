package com.bawaaba.rninja4.rookie.activity.ProfileTab;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.JSONParser;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import co.lujun.androidtagview.ColorFactory;
import co.lujun.androidtagview.TagContainerLayout;

public class SkillTab extends AppCompatActivity implements View.OnClickListener {
    JSONParser jsonParser = new JSONParser();
    ArrayList<HashMap<String, String>> SkillList;
    private String TAG = SkillTab.class.getSimpleName();
    private TextView textSkills;
    private TextView editSkills;
    private SQLiteHandler db;
    private SessionManager session;
    private String skills = "";
    private String user_id = "";
    private String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_tab);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //  textSkills = (TextView) findViewById(R.id.tab_skill);
        TagContainerLayout mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tagcontainerLayout);
        editSkills = (TextView) findViewById(R.id.edit_skill);
        editSkills.setOnClickListener(this);
//      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        SkillList = new ArrayList<HashMap<String, String>>();
        // new SkillDetails().execute();

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        // editSkills.setTypeface(null, Typeface.NORMAL);
        editSkills.setTypeface(null, Typeface.BOLD);

        mTagContainerLayout.setTheme(ColorFactory.NONE);


        Intent i1 = getIntent();
        user_id = i1.getStringExtra("user_id");
        category = i1.getStringExtra("category");

        HashMap<String, String> user = db.getUserDetails();
        String db_id = user.get("uid"); // value from db when logged in

        if (session.isLoggedIn() && (db_id.equals(user_id) || (user_id == null))) {
            editSkills.setVisibility(View.VISIBLE);
        } else {
            editSkills.setVisibility(View.GONE);

        }
        Intent from_Profile = getIntent();
        skills = from_Profile.getStringExtra("skills");
        Log.d("skils>>>>>>",skills);



        ArrayList<String> list = new ArrayList<String>();

        try {
            JSONArray skill_data = new JSONArray(skills);

            for (int i = 0; i < skill_data.length(); i++) {
                String subcategory_name = skill_data.getString(i);
                Log.e("skillcheck",subcategory_name);

                if (subcategory_name != null) {
                    list.add(String.valueOf(subcategory_name));

                    Log.e("skillcheck2", String.valueOf(list));

                    mTagContainerLayout.setTags(list);

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_skill:
                loadEditSkillActivity();
                break;
            default:
                break;
        }
    }

    private void loadEditSkillActivity() {
        Intent skillIntent = new Intent(SkillTab.this, SkillEditActivity.class);
        skillIntent.putExtra("skills", skills);
        skillIntent.putExtra("user_id", user_id);
        skillIntent.putExtra("category", category);
        startActivity(skillIntent);
    }
}