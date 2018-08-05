package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.AddOtherUrlActivity;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.EditUrlActivity;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OtherFile extends AppCompatActivity {
    TextView mTitle;
    TextView mDesc;
    TextView mLink;
    private RecyclerView mPrortfolio_otherfile;
    private Adapterother_file mAdapter;
    private LinearLayoutManager mLayoutManager;
    private String TAG = OtherFile.class.getSimpleName();
    private TextView Edit_url;
    private TextView Add_url;
    private SQLiteHandler db;
    private SessionManager session;
    private AppCompatTextView link_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_file);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        List<OtherFile_data> data = new ArrayList<>();

//        Edit_url=(TextView)findViewById(R.id.edit_otherurl);
        Add_url = (TextView) findViewById(R.id.add_url);
        Edit_url = (TextView) findViewById(R.id.edit_url);
        link_count=(AppCompatTextView)findViewById(R.id.link_count);

        Add_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_addurl = new Intent(OtherFile.this, AddOtherUrlActivity.class);
                startActivity(to_addurl);
            }
        });

        Edit_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_addurl = new Intent(OtherFile.this, EditUrlActivity.class);
                startActivity(to_addurl);
            }
        });
//
//        Edit_url.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent to_editurl = new Intent(OtherFile.this,Edit_otherurl.class);
//                startActivity(to_editurl);
//            }
//        });

        //  new OtherPortfolio().execute();

        Intent from_Profile = getIntent();
        final String portfolio_link = from_Profile.getStringExtra("portfolio_link");
        Log.e(TAG, "linkCheck: " + portfolio_link);

        OtherFile.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent to_adpter_audio = new Intent(OtherFile.this, Adapterother_file.class);
                to_adpter_audio.putExtra("portfolio_link", portfolio_link);
            }
        });

        try {
            JSONArray link_data = new JSONArray(portfolio_link);
            for (int i = 0; i < link_data.length(); i++) {
                JSONObject object = link_data.getJSONObject(i);
                OtherFile_data otherData = new OtherFile_data();
                otherData.other_link = object.getString("links");
                otherData.other_title = object.getString("title");
                otherData.other_description = object.getString("description");
                data.add(otherData);
            }
            db = new SQLiteHandler(getApplicationContext());
            session = new SessionManager(getApplicationContext());
            Intent from_profile = getIntent();
            String user_id = from_profile.getStringExtra("user_id");

            HashMap<String, String> user = db.getUserDetails();
            final String db_id = user.get("uid");

            if(session.isLoggedIn() && (db_id.equals(user_id) || (user_id==null))) {
                Edit_url.setVisibility(View.VISIBLE);

            }else{
                Edit_url.setVisibility(View.GONE);

            }
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(portfolio_link);
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
                    tv.setText(jsonObject.getString("portfolio_link"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.e("portfolio_link", String.valueOf(count));
            if ((count == 0)&&session.isLoggedIn() && (db_id.equals(user_id) || (user_id == null))) {
                link_count.setVisibility(View.VISIBLE);
            }
            else {
                link_count.setVisibility(View.GONE);
            }

            mPrortfolio_otherfile = (RecyclerView) findViewById(R.id.port_other_url);
            mAdapter = new Adapterother_file(OtherFile.this, data);
            mPrortfolio_otherfile.setLayoutManager(mLayoutManager);
            mPrortfolio_otherfile.setAdapter(mAdapter);
            mPrortfolio_otherfile.setLayoutManager(new LinearLayoutManager(OtherFile.this));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");
        Intent from_profile = getIntent();
        String user_id = from_profile.getStringExtra("user_id");
        if (session.isLoggedIn() && db_id.equals(user_id)) {
            BaseBottomHelperActivity.start(getApplicationContext(),null,null,null);
            /*Intent intent = new Intent(OtherFile.this, com.bawaaba.rninja4.rookie.MainActivity.class);
            startActivity(intent);*/
            finish();
        } else {
            super.onBackPressed();
        }
    }
}

