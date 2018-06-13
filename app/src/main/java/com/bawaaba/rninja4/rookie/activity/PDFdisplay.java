package com.bawaaba.rninja4.rookie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PDFdisplay extends AppCompatActivity {
    private String TAG =PDFdisplay.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent from_adapter_pdf = getIntent();
        String document = from_adapter_pdf.getStringExtra("documents");
        Log.e(TAG, "docCheck3: "+document );



        WebView webView=new WebView(PDFdisplay.this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

         webView.setWebViewClient(new Callback());
         webView.loadUrl("http://docs.google.com/gview?embedded=true&url="+document);

        setContentView(webView);
    }
    private class Callback extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(
                 WebView view, String url) {
            return(false);
        }

    }

}
