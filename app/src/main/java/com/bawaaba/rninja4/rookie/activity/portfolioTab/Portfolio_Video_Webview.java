package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bawaaba.rninja4.rookie.R;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class Portfolio_Video_Webview extends AppCompatActivity {
    WebView webView;
    JZVideoPlayerStandard jzVideoPlayerStandard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio__video__webview);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        jzVideoPlayerStandard   = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
        Intent from_adapter_video = this.getIntent();
        final String link = from_adapter_video.getExtras().getString("link");

        jzVideoPlayerStandard.setUp(link,
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,"");



        Log.e("video intent check",link);

                if(link.contains("youtube")){

                    webView = (WebView) findViewById(R.id.webView);

                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    String frameVideo = "<iframe  width=\"500\" height=\"500\" src='" + link + "?rel=0&autoplay=1' type=\'text/html\' frameborder=\'0\' allowfullscreen=\'allowfullscreen\'></iframe>";

                    //String frameVideo = "<iframe class=\"youtube-player\" type=\"text/html\" width=\"500\" height=\"500\" src='" + link + "?rel=0&autoplay=1' type=\'text/html\' frameborder=\'0\' allowfullscreen=\'allowfullscreen\'></iframe>";

                    webView.loadData(frameVideo, "text/html", "utf-8");
                    webView.loadUrl(frameVideo);
                    webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                    webView.setWebViewClient(new WebViewClient());
                    webView.setBackgroundColor(Color.parseColor("#000000"));
////                    WebSettings w = webView.getSettings();
//
                    webView.setWebViewClient(new WebViewClient()
                    {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url)
                        {
                            view.loadUrl(url);
                            return true;
                        }
                    });

                   // WebSettings webSettings = webView.getSettings();
//                    webSettings.setJavaScriptEnabled(true);
//                    webSettings.setBuiltInZoomControls(true);
//                    webSettings.setAllowContentAccess(true);
//                    webSettings.setEnableSmoothTransition(true);
//                    webSettings.setLoadsImagesAutomatically(true);
//                    webSettings.setLoadWithOverviewMode(true);
//                    webSettings.setSupportZoom(false);
//                    webSettings.setUseWideViewPort(true);
//                    webSettings.setAppCacheEnabled(true);
//                    webSettings.setSupportMultipleWindows(true);

//                    webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//                    webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//                    webView.getSettings().setAppCacheEnabled(true);
//                   // webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//                    webSettings.setDomStorageEnabled(true);
//                    //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//                    webSettings.setUseWideViewPort(true);

                }else if(link.contains("vimeo")){
                    webView = (WebView) findViewById(R.id.webView);
                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);

//                    webView.setWebChromeClient(new WebChromeClient());
//                    reactContext.addLifecycleEventListener(webView);
//                    mWebViewConfig.configWebView(webView);

                    String frameVideo ="<iframe src='" + link +" 'width='500' height='500' frameborder='0' webkitallowfullscreen  allowfullscreen></iframe>";
                    webView.loadData(frameVideo, "text/html", "utf-8");
                    webView.setWebViewClient(new WebViewClient());
                    webView.setBackgroundColor(Color.parseColor("#000000"));
                }else{

                    webView = (WebView) findViewById(R.id.webView);
                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);

                    String frameVideo="<video width=\"100%\" controls=\"controls\" autoplay=\"autoplay\"><source src='"+link+"' type='video/mp4'></video>";
                    webView.loadData(frameVideo, "text/html", "utf-8");
                    webView.setWebViewClient(new WebViewClient());
                    webView.setBackgroundColor(Color.parseColor("#000000"));
                }

            }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
           }
