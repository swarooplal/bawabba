package com.bawaaba.rninja4.rookie.BioTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.*;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Services extends AppCompatActivity implements View.OnClickListener {
    private String TAG = Services.class.getSimpleName();
    private SQLiteHandler db;
    private RecyclerView mProfiledetails;
    private AdapterServices mAdapter;
    private SessionManager session;
    private LinearLayoutManager mLayoutManager;
    private TextView add_services;
    private TextView No_service;
    private TextView tvShomore;
    private List<ServiceData> dataFinal = new ArrayList<>();
    List<ServiceData> data = new ArrayList<>();
    List<ServiceData> dataLimited = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        add_services = (TextView) findViewById(R.id.edit_service);
        tvShomore = (TextView) findViewById(R.id.tvShowmore);
        No_service=(TextView)findViewById(R.id.no_service);

        Intent from_profile = getIntent();
        String user_id = from_profile.getStringExtra("user_id");

        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid"); // value from db when logged in

        Intent from_Profile = getIntent();
        String services = from_Profile.getStringExtra("services");
        Log.e("services", services);


        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(services );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int count = jsonArray.length();
        for(int i = 0; i< jsonArray.length();i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tv = new TextView(getApplicationContext());

            try {
                tv.setText(jsonObject.getString("services"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("services", String.valueOf(count));

        if(count == 0){
            Log.e("sdgfjdf","sarg");
            No_service.setVisibility(View.VISIBLE);
            tvShomore.setVisibility(View.GONE);

        }
        else{
            No_service.setVisibility(View.GONE);
            tvShomore.setVisibility(View.VISIBLE);
        }

        if(session.isLoggedIn() && (db_id.equals(user_id) || (user_id==null))) {
            add_services.setVisibility(View.VISIBLE);
        }else{
            add_services.setVisibility(View.GONE);
        }

        try {
            JSONArray service_data = new JSONArray(services);
            System.out.println("Services.onCreate " + service_data.length());
            for (int i = 0; i < service_data.length(); i++) {
                JSONObject object = service_data.getJSONObject(i);
                ServiceData serviceData = new ServiceData();
                serviceData.Service_title = object.getString("title");
                serviceData.Service_price = object.getString("price");
                serviceData.Service_Currency = object.getString("currency");
                serviceData.Service_description = object.getString("description");
                data.add(serviceData);

            }



            if(data.size()<=2){
                tvShomore.setVisibility(View.GONE);
            }
            if (data.size() > 2) {
                for (int i = 0; i < 2; i++) {
                    dataLimited.add(data.get(i));
                }
                mProfiledetails = (RecyclerView) findViewById(R.id.services);
                mProfiledetails.setLayoutManager(mLayoutManager);
                mAdapter = new AdapterServices(Services.this, dataLimited);
                mProfiledetails.setAdapter(mAdapter);
                mProfiledetails.setLayoutManager(new LinearLayoutManager(Services.this));

            } else {
                mProfiledetails = (RecyclerView) findViewById(R.id.services);
                mProfiledetails.setLayoutManager(mLayoutManager);
                mAdapter = new AdapterServices(Services.this, data);
                mProfiledetails.setAdapter(mAdapter);
                mProfiledetails.setLayoutManager(new LinearLayoutManager(Services.this));
            }

            tvShomore.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 if (tvShomore.getText().toString().matches("Read more..")) {
                                                     mProfiledetails = (RecyclerView) findViewById(R.id.services);
                                                     mProfiledetails.setLayoutManager(mLayoutManager);
                                                     mAdapter = new AdapterServices(Services.this, data);
                                                     mProfiledetails.setAdapter(mAdapter);
                                                     mProfiledetails.setLayoutManager(new LinearLayoutManager(Services.this));
                                                     tvShomore.setText("..Read less");
                                                 } else {
                                                     mProfiledetails = (RecyclerView) findViewById(R.id.services);
                                                     mProfiledetails.setLayoutManager(mLayoutManager);
                                                     mAdapter = new AdapterServices(Services.this, dataLimited);
                                                     mProfiledetails.setAdapter(mAdapter);
                                                     mProfiledetails.setLayoutManager(new LinearLayoutManager(Services.this));
                                                     tvShomore.setText("Read more..");
                                                 }
                                             }
                                         }
            );


        } catch (JSONException e) {
            e.printStackTrace();
        }
        add_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Services.this, AddServices.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

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
        if(session.isLoggedIn()&&db_id.equals(user_id)) {
            BaseBottomHelperActivity.start(getApplicationContext(),null,null,null);
            /*Intent intent = new Intent(Services.this, com.bawaaba.rninja4.rookie.MainActivity.class);
            startActivity(intent);*/
            finish();
        }else{
            super.onBackPressed();
        }
    }
}

