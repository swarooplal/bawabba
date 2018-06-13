package com.bawaaba.rninja4.rookie.BioTab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Description extends AppCompatActivity {

    private String TAG = Description.class.getSimpleName();
    private ProgressDialog pDialog;

    private TextView textdescription;
    private TextView editdescription;
    private SQLiteHandler db;
    private SessionManager session;
    private TextView show, hide;

    List<Description> data = new ArrayList<>();
    List<Description> dataLimited = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        editdescription = (TextView) findViewById(R.id.edit_desc);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        Intent from_profile = getIntent();
        String user_id = from_profile.getStringExtra("user_id");

        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid"); // value from db when logged in

        if (session.isLoggedIn() && (db_id.equals(user_id) || (user_id == null))) {
            editdescription.setVisibility(View.VISIBLE);
        } else {
            editdescription.setVisibility(View.GONE);
        }

        Intent from_Profile = getIntent();
        final String description = from_Profile.getStringExtra("description");
        textdescription = (TextView) findViewById(R.id.description);
        textdescription.setText(description);

//        ViewTreeObserver vto = textdescription.getViewTreeObserver();
//
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                ViewTreeObserver obs = textdescription.getViewTreeObserver();
//                obs.removeGlobalOnLayoutListener(this);
//
//                if(textdescription.getLineCount() > 5){
//                    Log.d("","Line["+textdescription.getLineCount()+"]"+textdescription.getText());
//                    int lineEndIndex = textdescription.getLayout().getLineEnd(4);
//                    String text = textdescription.getText().subSequence(0, lineEndIndex-5)+"...";
//                    textdescription.setText(text);
//
//                }
//            }
//        });

        editdescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent to_editdescription = new Intent(Description.this, EditDescription.class);
                to_editdescription.putExtra("user_id", db_id);
                to_editdescription.putExtra("description", description);
                startActivity(to_editdescription);
            }
        });

        show = (TextView) findViewById(R.id.show);
        show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("Show button");
                show.setVisibility(View.INVISIBLE);
                hide.setVisibility(View.VISIBLE);
               textdescription.setMaxLines(Integer.MAX_VALUE);
            }
        });
        hide = (TextView) findViewById(R.id.hide);
        hide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("Hide button");
                hide.setVisibility(View.INVISIBLE);
                show.setVisibility(View.VISIBLE);
              textdescription.setMaxLines(5);
            }
        });
//       int lines=textdescription.length();
//        Log.e("lines", String.valueOf(lines));

        textdescription.post(new Runnable() {

            @Override
            public void run() {

                Log.e("Line count: ", textdescription.getLineCount()+"");

                if(textdescription.getLineCount()<6) {

                    hide.setVisibility(View.INVISIBLE);
                    show.setVisibility(View.INVISIBLE);
                }
            }
        });

    }
}

