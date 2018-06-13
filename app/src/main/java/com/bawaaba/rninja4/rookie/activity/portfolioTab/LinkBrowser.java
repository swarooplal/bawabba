package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.bawaaba.rninja4.rookie.R;

public class LinkBrowser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_browser);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent from_adpter_url = getIntent();
        final String link = from_adpter_url.getStringExtra("links");
        Log.e("link_data", String.valueOf(link));
        WebView webView = (WebView) findViewById(R.id.webView3);
        //WebView webView = new WebView(LinkBrowser.this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://" + link);
    }
}




