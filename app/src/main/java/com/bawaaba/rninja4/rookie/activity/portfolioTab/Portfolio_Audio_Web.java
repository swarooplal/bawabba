package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.bawaaba.rninja4.rookie.R;

public class Portfolio_Audio_Web extends AppCompatActivity {

    WebView Audio_view ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio__audio__web);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent from_adapter_audio = this.getIntent();
        final String audio_link = from_adapter_audio.getExtras().getString("audio_link");




                        if(audio_link.contains("soundcloud")){

                            Audio_view = (WebView) findViewById(R.id.audio_view);
                            String html = "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:2)\" href=\"hdpi.css\" /></head> <body style=\"background:black;margin:1 1 1 1; padding:1 1 1 1;\"> <iframe id=\"sc-widget " +
                                    "\" width=\"100%\" height=\"650\"" + // Set Appropriate Width and Height that you want for SoundCloud Player
                                    " src=\"" + audio_link   // Set Embedded url
                                    + "\" frameborder=\"yes\" scrolling=\"yes\"></iframe>" +
                                    "<script src=\"https://w.soundcloud.com/player/api.js\" type=\"text/javascript\"></script> </body> </html> ";

                            Audio_view.setVisibility(View.VISIBLE);
                            Audio_view.getSettings().setJavaScriptEnabled(true);
                            Audio_view.getSettings().setLoadWithOverviewMode(true);
                            Audio_view.getSettings().setUseWideViewPort(true);
                            Audio_view.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
                            Log.e("Audiocheck",audio_link);

                        }else{
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(audio_link), "audio/mp3");
                            Portfolio_Audio_Web.this.startActivity(intent);
                        }
                    }
}

