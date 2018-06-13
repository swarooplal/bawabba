package com.bawaaba.rninja4.rookie.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bawaaba.rninja4.rookie.R;

public class Internetconnection extends AppCompatActivity {
    private Button reload;
    private String TAG = Internetconnection.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internetconnection);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actiontitle_layout2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        reload = (Button) findViewById(R.id.refresh);

        Intent i3 = getIntent();
        final   String category_id = i3.getStringExtra("category_id");
        final String current_class = i3.getStringExtra("current_class");

        Log.e(TAG, "category value " + category_id);
        Log.e(TAG, "currentclass: " + current_class);

        reload.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
//                Context context = null;
//                ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkInfo netInfo = mgr.getActiveNetworkInfo();
//                if (netInfo != null) {
//                    if (netInfo.isConnected()) {
//                        // Internet Available
                        if(current_class.equals("Loginactivity"))
                        {
                            Log.e(TAG, "currentclass:Loginactivity ");
                            Intent i5=new Intent(Internetconnection.this,LoginActivity.class);
                            startActivity(i5);
                            finish();
                        }

                        if(current_class.equals("Subcategory"))
                        {

                            Log.e(TAG, "currentclass:Subcategory");
                            Intent i4=new Intent(Internetconnection.this,Subcategory.class);
                            //i4.putExtra("category_id", category_id);
                            startActivity(i4);
                            finish();
                        }
//                    }else {
//                        //No internet
//                        Toast.makeText(getApplicationContext(),
//                                "No Internet Connection!!",
//                                Toast.LENGTH_LONG)
//                                .show();
//                    }
//                } else {
//                    //No internet
//                    Toast.makeText(getApplicationContext(),
//                            "No Internet Connection!!!",
//                            Toast.LENGTH_LONG)
//                            .show();
//                }


            }


        });
    }
}