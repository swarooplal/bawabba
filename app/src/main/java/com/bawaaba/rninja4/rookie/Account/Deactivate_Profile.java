package com.bawaaba.rninja4.rookie.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.R;

public class Deactivate_Profile extends AppCompatActivity {

    private ImageView app_logo;
    private TextView Deactivation_strings;
    private Button deactivation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate__profile);
        getSupportActionBar().hide();

        deactivation=(Button)findViewById(R.id.de_activate);
        deactivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_deactivate_reason = new Intent(Deactivate_Profile.this, Deactivate_reason.class);
                startActivity(to_deactivate_reason);
                finish();
            }
        });
    }
//        @Override
//    public void onBackPressed() {
//        super.onBackPressed();
////        Intent intent = new Intent(Deactivate_Profile.this, Profile_settings.class);
////        startActivity(intent);
//
//
//    }

}
