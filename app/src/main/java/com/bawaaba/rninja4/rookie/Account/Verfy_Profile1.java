package com.bawaaba.rninja4.rookie.Account;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bawaaba.rninja4.rookie.R;

public class Verfy_Profile1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfy__profile1);
        getSupportActionBar().hide();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(Verfy_Profile1.this, Profile_settings.class);
//        startActivity(intent);
//        finish();
//    }
}
