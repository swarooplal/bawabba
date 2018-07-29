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
import android.widget.ImageView;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.JSONParser;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.AdapterReviewResult;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.GaleryAdapter;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.ReviewResultData;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.activity.SimpleDividerItemDecoration;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.dashboard_new.ProfileViewFragment;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.utils.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PortfolioReview extends AppCompatActivity {

    private static final String TAG = PortfolioReview.class.getSimpleName();

    private RecyclerView mProfiledetails;
    private AdapterReviewResult mAdapter;
    private GaleryAdapter mAdapter_galery;

    private LinearLayoutManager mLayoutManager;
    private ArrayList<Image> images;

    private ImageView profile1;
    private AppCompatTextView review_count;
    private SQLiteHandler db;
    private SessionManager session;

    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_review);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        List<ReviewResultData> data = new ArrayList<>();

        profile1=(ImageView)findViewById(R.id.img_profile);
        review_count=(AppCompatTextView) findViewById(R.id.review_count);


        Intent from_Profile = getIntent();
        final String review = from_Profile.getStringExtra("review");
        Log.e(TAG, "reviewcheck2: " + review);

        PortfolioReview.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent to_adpter_audio = new Intent(PortfolioReview.this,AdapterReviewResult.class);
                to_adpter_audio.putExtra("review",review);
            }
        });


        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        Intent from_profile = getIntent();
        String user_id = from_profile.getStringExtra("user_id");

        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");

        images = new ArrayList<>();
        mAdapter_galery = new GaleryAdapter(getApplicationContext(), images);

//        mProfiledetails.addOnItemTouchListener(new GaleryAdapter.RecyclerTouchListener(getApplicationContext(), mProfiledetails, new GaleryAdapter.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).setImagesArray(images);
//                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).setImagesCurrentPosition(position);
//                Intent intent = new Intent(PortfolioReview.this, ImageSlidingActivity.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//
//        }));

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(review);
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
                tv.setText(jsonObject.getString("review"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("review", String.valueOf(count));
        if ((count == 0)) {
            review_count.setVisibility(View.VISIBLE);
        }
        else {
            review_count.setVisibility(View.GONE);
        }

        try{
            JSONArray review_data = new JSONArray(review);

            for (int i = 0; i < review_data.length(); i++){

                JSONObject object = review_data.getJSONObject(i);

                ReviewResultData reviewData = new ReviewResultData();
                reviewData.user_id = object.getString("user_id");
                reviewData.profileImage = object.getString("profile_img");
                reviewData.profileName = object.getString("current_name");
                reviewData.reviewdetails = object.getString("review");
                // String count= String.valueOf(object.getString("review").length());
                reviewData.ratingdetails= object.getString("rating");
                reviewData.reviewdate= object.getString("date");
                reviewData.reviewImage= object.getString("review_image");
                reviewData.review_count= object.getString("rating");
                reviewData.verify_value= object.getString("verify");

                data.add(reviewData);

            }
            Log.e("checkreevie", String.valueOf(review_data));
            mProfiledetails = (RecyclerView) findViewById(R.id.profile_details);
            mProfiledetails.addItemDecoration(new SimpleDividerItemDecoration(this));
            mProfiledetails.setLayoutManager( mLayoutManager);
            mAdapter = new AdapterReviewResult(PortfolioReview.this, data);
            mProfiledetails.setAdapter(mAdapter);
            mProfiledetails.setLayoutManager(new LinearLayoutManager(PortfolioReview.this));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        AppPreference appPreference= ObjectFactory.getInstance().getAppPreference(getApplicationContext());
        BaseBottomHelperActivity.start(getApplicationContext(), ProfileViewFragment.class.getName(),appPreference.getUserId(),appPreference.getUserName());
       /* Intent intent = new Intent(PortfolioReview.this, ProfileView.class);
        startActivity(intent);*/
        finish();
    }
}
