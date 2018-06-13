package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PortfolioVideo extends AppCompatActivity  implements View.OnClickListener{
    private static final String TAG = PortfolioVideo.class.getSimpleName();

    private RecyclerView mPrortfolio_video;
    private AdapterVideo mAdapter;
    private GridLayoutManager mLayoutManager;

    private SQLiteHandler db;
    private SessionManager session;
    private AppCompatTextView tveditVideos;
    private String user_id = "";
    private AppCompatTextView Video_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_video);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        List<PortfolioVideoData> data=new ArrayList<>();
        tveditVideos=(AppCompatTextView)findViewById(R.id.tveditVideos);
        tveditVideos.setOnClickListener(this);

        Video_count=(AppCompatTextView)findViewById(R.id.video_count);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        Intent from_profile = getIntent();
        String user_id = from_profile.getStringExtra("user_id");

        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");

        if(session.isLoggedIn() && (db_id.equals(user_id) || (user_id==null))) {
            tveditVideos.setVisibility(View.VISIBLE);
        }else{
            tveditVideos.setVisibility(View.GONE);
        }

        Intent from_Profile = getIntent();
        final String portfolio_video = from_Profile.getStringExtra("portfolio_video");
        Log.e(TAG, "videoCheck2: "+portfolio_video);

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(portfolio_video);
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
                tv.setText(jsonObject.getString("portfolio_video"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("portfolio_image", String.valueOf(count));
        if ((count == 0)&&session.isLoggedIn() && (db_id.equals(user_id) || (user_id == null))) {
            Video_count.setVisibility(View.VISIBLE);
        }
        else {
            Video_count.setVisibility(View.GONE);
        }

        PortfolioVideo.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent to_adpter_video = new Intent(PortfolioVideo.this,AdapterVideo.class);
                to_adpter_video.putExtra("portfolio_video",portfolio_video);
            }
        });


        try {
            JSONArray video_data = new JSONArray(portfolio_video);

            for (int i = 0; i < video_data.length(); i++){
                JSONObject object = video_data.getJSONObject(i);

                PortfolioVideoData videoData = new PortfolioVideoData();

                videoData.Video_link = object.getString("link");
                videoData.video_thumbnail = object.getString("thumbnail");

                Log.e("thumbnail",object.getString("thumbnail"));
                data.add(videoData);
            }

            mPrortfolio_video=(RecyclerView)findViewById(R.id.port_videos);
            // mPrortfolio_video.addItemDecoration(new SimpleDividerItemDecoration(this));
            mAdapter = new AdapterVideo(PortfolioVideo.this,data);
            mPrortfolio_video.setLayoutManager( mLayoutManager);
            GridLayoutManager mLayoutManager=new GridLayoutManager(this,4);
            mPrortfolio_video.setLayoutManager( mLayoutManager);
            mPrortfolio_video.setAdapter(mAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tveditVideos:
                Intent intent = new Intent(PortfolioVideo.this, PortfolioVideoEditActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }
    @Override
    public void onBackPressed() {
        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");
        Intent from_profile = getIntent();
        String user_id = from_profile.getStringExtra("user_id");
        if (session.isLoggedIn() && db_id.equals(user_id)) {
            Intent intent = new Intent(PortfolioVideo.this, com.bawaaba.rninja4.rookie.MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
