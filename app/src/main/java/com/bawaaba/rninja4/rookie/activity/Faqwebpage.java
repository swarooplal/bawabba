package com.bawaaba.rninja4.rookie.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.bawaaba.rninja4.rookie.R;

public class Faqwebpage extends AppCompatActivity {

    private WebView Faq_web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqwebpage);

        getSupportActionBar().hide();

        Faq_web=(WebView)findViewById(R.id.faq_web);

        Faq_web.loadUrl("https://www.bawabba.com/mobilefaq");
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(Faqwebpage.this, LoginActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
