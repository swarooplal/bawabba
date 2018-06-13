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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.Edit_Audio_Data;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PortfolioAudio extends AppCompatActivity {

    private RecyclerView mPrortfolio_audio;
    private AdapterAudio mAdapter;
    private LinearLayoutManager mLayoutManager;
    private TextView Edit_audio;
    private SQLiteHandler db;
    private SessionManager session;
    private String user_id = "";
    private AppCompatTextView audio_count;
    private LinearLayout audio_linear;
    private String TAG = PortfolioAudio.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_audio);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final List<Edit_Audio_Data> data = new ArrayList<>();

        audio_linear=(LinearLayout)findViewById(R.id.audio_linear);
        audio_count=(AppCompatTextView)findViewById(R.id.audio_count);
        Edit_audio = (TextView) findViewById(R.id.edit_audio);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        Intent from_profile = getIntent();
        String user_id = from_profile.getStringExtra("user_id");

        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");

        if(session.isLoggedIn() && (db_id.equals(user_id) || (user_id==null))) {
            Edit_audio.setVisibility(View.VISIBLE);
        }else{
            Edit_audio.setVisibility(View.GONE);

        }
        Intent from_Profile = getIntent();
        final String portfolio_audio = from_Profile.getStringExtra("portfolio_audio");
        Log.e(TAG, "videoCheck2: "+portfolio_audio);

        PortfolioAudio.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent to_adpter_audio = new Intent(PortfolioAudio.this,AdapterAudio.class);
                to_adpter_audio.putExtra("portfolio_audio",portfolio_audio);
            }
        });


        try {
            JSONArray audio_data = new JSONArray(portfolio_audio);

            for (int i = 0; i < audio_data.length(); i++) {
                JSONObject object = audio_data.getJSONObject(i);
                Edit_Audio_Data audioData = new Edit_Audio_Data();
                audioData.Audio_Link = object.getString("audio_link");
                audioData.Audio_Title = object.getString("title");
                data.add(audioData);
            }
            mPrortfolio_audio = (RecyclerView) findViewById(R.id.port_audios);
            mAdapter = new AdapterAudio(PortfolioAudio.this, data);
            mPrortfolio_audio.setLayoutManager(mLayoutManager);
            mPrortfolio_audio.setAdapter(mAdapter);
            mPrortfolio_audio.setLayoutManager(new LinearLayoutManager(PortfolioAudio.this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(portfolio_audio);
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
                tv.setText(jsonObject.getString("portfolio_audio"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("portfolio_audio", String.valueOf(count));
        if ((count == 0)&&session.isLoggedIn() && (db_id.equals(user_id) || (user_id == null))) {
            audio_count.setVisibility(View.VISIBLE);
        }
        else {
            audio_count.setVisibility(View.GONE);
        }





        Edit_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).setAudioData(data);
                Intent intent = new Intent(PortfolioAudio.this, AudioEditActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");
        Intent from_profile = getIntent();
        String user_id = from_profile.getStringExtra("user_id");
        if (session.isLoggedIn() && db_id.equals(user_id)) {
            Intent intent = new Intent(PortfolioAudio.this, com.bawaaba.rninja4.rookie.MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}