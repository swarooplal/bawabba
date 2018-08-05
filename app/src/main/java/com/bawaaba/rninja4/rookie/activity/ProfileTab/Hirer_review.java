package com.bawaaba.rninja4.rookie.activity.ProfileTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.SimpleDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Hirer_review extends AppCompatActivity {

    private static final String TAG = Hirer_review.class.getSimpleName();
    private RecyclerView mProfiledetails;
    private AdapterReview_by_me_result mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hirer_review);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        List<ReviwbymeData> data = new ArrayList<>();

        Intent from_Profile = getIntent();
        final String review_by_me = from_Profile.getStringExtra("review_by_me");
        Log.e(TAG, "reviewcheck2: " + review_by_me);

        try {
            JSONArray review_by_me_data = new JSONArray(review_by_me);

            for (int i = 0; i < review_by_me_data.length(); i++) {

                JSONObject object = review_by_me_data.getJSONObject(i);

                ReviwbymeData reviewbymeData = new ReviwbymeData();

                reviewbymeData.profileImage = object.getString("profile_img");
                reviewbymeData.profileName = object.getString("current_name");
                reviewbymeData.reviewdetails = object.getString("review_by_me");
                // String count= String.valueOf(object.getString("review").length());
                reviewbymeData.ratingdetails = object.getString("rating");
                reviewbymeData.reviewdate = object.getString("date");
                reviewbymeData.reviewImage = object.getString("review_image");
                reviewbymeData.review_count = object.getString("rating");
                data.add(reviewbymeData);
            }

            Log.e("checkreevie", String.valueOf(review_by_me_data));
            mProfiledetails = (RecyclerView) findViewById(R.id.profile_details);
            mProfiledetails.addItemDecoration(new SimpleDividerItemDecoration(this));
            mProfiledetails.setLayoutManager( mLayoutManager);
            mAdapter = new AdapterReview_by_me_result(Hirer_review.this,data);
            mProfiledetails.setAdapter(mAdapter);
            mProfiledetails.setLayoutManager(new LinearLayoutManager(Hirer_review.this));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
