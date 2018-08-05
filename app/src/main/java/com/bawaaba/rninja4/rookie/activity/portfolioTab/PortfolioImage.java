package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.GaleryAdapter;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PortfolioImage extends AppCompatActivity {

    private String TAG = PortfolioImage.class.getSimpleName();
    private ProgressDialog pDialog;
    private ArrayList<Image> images;
    private GaleryAdapter mAdapter;
    private RecyclerView recyclerView;
    private LinearLayout linear_edit;

    private SQLiteHandler db;
    private SessionManager session;
    private String user_id = "";
    private AppCompatTextView Image_count;

    private boolean mHasSaveInstanceState;
    private AppCompatTextView tvEditPhotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_image);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tvEditPhotos = (AppCompatTextView) findViewById(R.id.tvEditPhotos);
        linear_edit = (LinearLayout) findViewById(R.id.llEditPhoto);
        Image_count = (AppCompatTextView) findViewById(R.id.image_count);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        Intent from_profile = getIntent();
        String user_id = from_profile.getStringExtra("user_id");

        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");
        if (session.isLoggedIn() && (db_id.equals(user_id) || (user_id == null))) {
            linear_edit.setVisibility(View.VISIBLE);
        } else {
            linear_edit.setVisibility(View.GONE);
        }
        Log.e(TAG, "Imagecheck2: ");
        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new GaleryAdapter(getApplicationContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GaleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GaleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).setImagesArray(images);
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).setImagesCurrentPosition(position);
                Intent intent = new Intent(PortfolioImage.this, ImageSlidingActivity.class);
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        Intent from_Profile = getIntent();
        String portfolio_image = from_Profile.getStringExtra("portfolio_image");
        Log.e(TAG, "Imagecheck2: " + portfolio_image);

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(portfolio_image);
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
                tv.setText(jsonObject.getString("portfolio_image"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("portfolio_image", String.valueOf(count));
        if ((count == 0) && session.isLoggedIn() && (db_id.equals(user_id) || (user_id == null))) {
            Image_count.setVisibility(View.VISIBLE);
        } else {
            Image_count.setVisibility(View.GONE);
        }

        try {
            JSONArray portfolio_image_data = new JSONArray(portfolio_image);
            for (int i = 0; i < portfolio_image_data.length(); i++) {
                JSONObject object = portfolio_image_data.getJSONObject(i);
                Image image = new Image();
                image.setName(object.getString("title"));
                String small_image = object.getString("thumbnail");
                String medium_image = object.getString("image");
                image.setMedium(small_image);
                image.setLarge(medium_image);
                images.add(image);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        tvEditPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).setImagesArray(images);
                Intent intent = new Intent(PortfolioImage.this, ImageEditActivity.class);
               startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
            super.onBackPressed();
        }
    }