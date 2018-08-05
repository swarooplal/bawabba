package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.JSONParser;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.PDFdisplay;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.DocEditActivity;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.LanguageTab;
import com.bawaaba.rninja4.rookie.activity.adapters.FileListRecyclerviewAdapter;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PortfolioFile extends BaseActivity {

    JSONParser jsonParser = new JSONParser();
    ArrayList<String> FileList;
    ImageView Pdfimage;
    TextView PdfTitle;
    private String TAG = LanguageTab.class.getSimpleName();
    private RecyclerView rvFiles;
    private AppCompatTextView tvEditFiles;
    private AdapterPdfFile mAdapter;
    private SQLiteHandler db;
    private SessionManager session;
    private AppCompatTextView file_count;

    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_file);

        file_count=(AppCompatTextView)findViewById(R.id.file_count);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        List<PdfData> data=new ArrayList<>();
        Intent from_Profile = getIntent();
        final String portfolio_doc = from_Profile.getStringExtra("portfolio_doc");
        Log.e(TAG, "docCheck: "+portfolio_doc );


        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        Intent from_profile = getIntent();
        String user_id = from_profile.getStringExtra("user_id");

        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(portfolio_doc);
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
                tv.setText(jsonObject.getString("portfolio_doc"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("portfolio_doc", String.valueOf(count));
        if ((count == 0)&&session.isLoggedIn() && (db_id.equals(user_id) || (user_id == null))) {
            file_count.setVisibility(View.VISIBLE);
        }
        else {
            file_count.setVisibility(View.GONE);
        }


        PortfolioFile.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent to_adpter_audio = new Intent(PortfolioFile.this,AdapterPdfFile.class);
                to_adpter_audio.putExtra("portfolio_video",portfolio_doc);
            }
        });


        try {
            JSONArray document_data=new JSONArray(portfolio_doc);

            for (int i = 0; i < document_data .length(); i++) {
                JSONObject object = document_data.getJSONObject(i);
                PdfData fileData = new PdfData();
                fileData.PDF_Link = object.getString("documents");
                fileData.PDF_Thumbnail= object.getString("thumbnail");
                fileData.PDF_Title= object.getString("title");
                data.add(fileData);
            }

            tvEditFiles = (AppCompatTextView) findViewById(R.id.tvEditFiles);



            if(session.isLoggedIn() && (db_id.equals(user_id) || (user_id==null))) {
                tvEditFiles.setVisibility(View.VISIBLE);
            }else{
                tvEditFiles.setVisibility(View.GONE);

            }
            rvFiles=(RecyclerView)findViewById(R.id.rvFiles);
            mAdapter = new AdapterPdfFile(PortfolioFile.this,data);
            rvFiles.setLayoutManager( mLayoutManager);
            rvFiles.setAdapter(mAdapter);
            rvFiles.setLayoutManager(new LinearLayoutManager(PortfolioFile.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void initViews() {

        rvFiles = (RecyclerView) findViewById(R.id.rvFiles);
        tvEditFiles = (AppCompatTextView) findViewById(R.id.tvEditFiles);
        tvEditFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PortfolioFile.this, DocEditActivity.class);
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvFiles.setLayoutManager(layoutManager);
        String response = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileResponse();
        final Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);
        if (profileresponse.getUserData().getPortfolioDoc().size() > 0) {
            FileListRecyclerviewAdapter adapter = new FileListRecyclerviewAdapter(getApplicationContext(), profileresponse.getUserData().getPortfolioDoc());
            adapter.setOnClickListener(new FileListRecyclerviewAdapter.AdvertisementsRecyclerViewClickListener() {
                @Override
                public void onClicked(int position, View v) {
                    Intent to_pdfdisplay = new Intent(PortfolioFile.this, PDFdisplay.class);
                    to_pdfdisplay.putExtra("documents", profileresponse.getUserData().getPortfolioDoc().get(position).getDocuments());
                    startActivity(to_pdfdisplay);
                }
            });
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
            /*Intent intent = new Intent(PortfolioFile.this, com.bawaaba.rninja4.rookie.MainActivity.class);
            startActivity(intent);*/
            finish();
        } else {
            super.onBackPressed();
        }
    }
}

