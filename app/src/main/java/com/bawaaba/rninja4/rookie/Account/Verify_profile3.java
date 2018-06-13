package com.bawaaba.rninja4.rookie.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bawaaba.rninja4.rookie.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Verify_profile3 extends AppCompatActivity {

    private CircleImageView user_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_profile3);
        getSupportActionBar().hide();


        user_image = (CircleImageView) findViewById(R.id.user_image);

        Intent from_prof_settings = getIntent();
        String profile_image = from_prof_settings.getStringExtra("profile_img");

        Glide.with(getApplicationContext()).load(profile_image).into(user_image);

    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(Verify_profile3.this, Profile_settings.class);
//        startActivity(intent);
//        finish();
//    }
}
