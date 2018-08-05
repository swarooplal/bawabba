package com.bawaaba.rninja4.rookie.BioTab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.JSONParser;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class Aboutme extends AppCompatActivity {

    private String TAG = Aboutme.class.getSimpleName();

    private ProgressDialog pDialog;

    private TextView textaboutme;

    private TextView editaboutme;

    private SQLiteHandler db;
    private SessionManager session;


    JSONParser jsonParser = new JSONParser();
    ArrayList<HashMap<String, String>> AboutList;
    JSONArray inbox = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutme);

        editaboutme = (TextView) findViewById(R.id.edit_about);
        textaboutme = (TextView) findViewById(R.id.aboutme);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        Intent from_profile = getIntent();
        String user_id = from_profile.getStringExtra("user_id");

        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid"); // value from db when logged in


        if (session.isLoggedIn() && (db_id.equals(user_id) || (user_id == null))) {
            editaboutme.setVisibility(View.VISIBLE);
        } else {
            editaboutme.setVisibility(View.GONE);
        }

        editaboutme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_editabout = new Intent(Aboutme.this, EditAbout.class);
                to_editabout.putExtra("user_id", db_id);
                startActivity(to_editabout);
                finish();
            }
        });

        Intent from_Profile = getIntent();
        final String aboutme = from_Profile.getStringExtra("aboutme");

        textaboutme =(TextView)findViewById(R.id.aboutme);
        textaboutme.setText(aboutme);

    }
}