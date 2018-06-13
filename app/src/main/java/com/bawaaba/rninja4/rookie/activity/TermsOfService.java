package com.bawaaba.rninja4.rookie.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.bawaaba.rninja4.rookie.R;

public class TermsOfService extends AppCompatActivity {
   private WebView Terms_web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_service);
        getSupportActionBar().hide();

        Terms_web=(WebView)findViewById(R.id.terms_web);
        Terms_web.loadUrl("https://www.bawabba.com/mobiletermsofservice");
    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(TermsOfService.this, LoginActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
