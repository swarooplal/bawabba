package com.bawaaba.rninja4.rookie.activity.ProfileTab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.JSONParser;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.adapters.GridSocialMediaAdapter;
import com.bawaaba.rninja4.rookie.custom_views.MyGridView;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.model.SocialMediaListing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SocialMedia extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    ArrayList<String> IconList;
    JSONArray inbox = null;
    private String TAG = SocialMedia.class.getSimpleName();
    private ProgressDialog pDialog;
    private ImageView textIcons;
    private GridView gvSocialMedia;
    private TextView edit_socialmedia;
    private SQLiteHandler db;
    private SessionManager session;
    private AppCompatTextView socialmedia_count;
    JSONArray socialmedia = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        gvSocialMedia =(MyGridView)findViewById(R.id.gvSocialMedia);
        edit_socialmedia=(TextView)findViewById(R.id.edit_social);
        IconList = new ArrayList<>();

        //  new socialicons().execute();

        db= new SQLiteHandler(getApplicationContext());
        session=new SessionManager(getApplicationContext());


        Intent from_Profile = getIntent();
        final String user_id = from_Profile.getStringExtra("user_id");
        final String socialmedia = from_Profile.getStringExtra("socialmedia");
        Log.e("socialmediavalue",socialmedia);

//        JSONArray jsonArray = new JSONArray();
//        try {
//            jsonArray = new JSONArray(socialmedia);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Log.e("sdgfjdf", String.valueOf(jsonArray));
//        int count = jsonArray.length();
//        for(int i = 0; i< jsonArray.length();i++) {
//            JSONObject jsonObject = null;
//            try {
//                jsonObject = jsonArray.getJSONObject(i);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            TextView tv = new TextView(getApplicationContext());
//
//            try {
//                tv.setText(jsonObject.getString("socialmedia"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        Log.e("socialmediacounttttt", String.valueOf(count));

        edit_socialmedia.setTypeface(null, Typeface.BOLD);
        HashMap<String, String> user = db.getUserDetails();
        String db_id = user.get("uid");

        if (session.isLoggedIn() && (db_id.equals(user_id) || (user_id == null))) {
            edit_socialmedia.setVisibility(View.VISIBLE);
        } else {
            edit_socialmedia.setVisibility(View.GONE);

        }

        edit_socialmedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent to_social_media=new Intent(SocialMedia.this,Edit_Socialmedia.class);
                to_social_media.putExtra("user_id",user_id);
                to_social_media.putExtra("socialmedia",socialmedia);
                startActivity(to_social_media);

            }
        });





        try {
            JSONObject socialmedia_data = new JSONObject(socialmedia);
            Log.e(TAG, "mediaCheck2: " + socialmedia);

//            for (int i = 0; i < socialmedia_data.length(); i++) {

            //JSONObject object = socialmedia_data.getJSONObject(String.valueOf(i));


            String five = socialmedia_data.getString("500Px");
            String behance = socialmedia_data.getString("behance");
            final String facebook = socialmedia_data.getString("facebook");
            String dribbble = socialmedia_data.getString("dribbble");
            String github = socialmedia_data.getString("github");
            String bitbucket = socialmedia_data.getString("bitbucket");
            String googleplus = socialmedia_data.getString("googleplus");
            String instagram = socialmedia_data.getString("instagram");
            String linkedin = socialmedia_data.getString("linkedin");
            String pinterest = socialmedia_data.getString("pinterest");
            String soundcloud = socialmedia_data.getString("soundcloud");
            String stack = socialmedia_data.getString("stack");
            String twitter = socialmedia_data.getString("twitter");
            String vimeo = socialmedia_data.getString("vimeo");
            String youtube = socialmedia_data.getString("youtube");
            String otherurl = socialmedia_data.getString("otherurl");
            String website = socialmedia_data.getString("website");

            socialmedia_count=(AppCompatTextView)findViewById(R.id.socialmedia_count);
            if(five.matches("null") && behance.matches("null")&&facebook.matches("null")&&dribbble.matches("null")&&github.matches("null")&&bitbucket.matches("null")&&googleplus.matches("null")&&instagram.matches("null")&&linkedin.matches("null")&&pinterest.matches("null")&&soundcloud.matches("null")&&stack.matches("null")&&twitter.matches("null")&&vimeo.matches("null")&&youtube.matches("null")
                    &&otherurl.matches("null")&&website.matches("null")){

                socialmedia_count.setVisibility(View.VISIBLE);
            }
            else{
                socialmedia_count.setVisibility(View.GONE);
            }
            socialmedia_count.setTypeface(null, Typeface.NORMAL);

            ArrayList<SocialMediaListing> social = new ArrayList<>();
            if (!five.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("five");
                listing.setUrl(five);
                listing.setSocialImage(R.drawable.five);
                social.add(listing);
            }

            if (!behance.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("behance");
                listing.setUrl(behance);
                listing.setSocialImage(R.drawable.ic_behance);
                social.add(listing);
            }
            if (!facebook.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("facebook");
                listing.setUrl(facebook);
                listing.setSocialImage(R.drawable.ic_facebook);
                social.add(listing);
            }
            if (!dribbble.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("dribbble");
                listing.setUrl(dribbble);
                listing.setSocialImage(R.drawable.ic_dribbble);
                social.add(listing);
            }
            if (!github.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("github");
                listing.setUrl(github);
                listing.setSocialImage(R.drawable.ic_github_logo);
                social.add(listing);
            }
            if (!bitbucket.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("bitbucket");
                listing.setUrl(bitbucket);
                listing.setSocialImage(R.drawable.ic_bitbucket_logo);
                social.add(listing);
            }
            if (!googleplus.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("googleplus");
                listing.setUrl(googleplus);
                listing.setSocialImage(R.drawable.ic_google_plus);
                social.add(listing);
            }
            if (!instagram.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("instagram");
                listing.setUrl(instagram);
                listing.setSocialImage(R.drawable.ic_instagram);
                social.add(listing);
            }
            if (!linkedin.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("linkedin");
                listing.setUrl(linkedin);
                listing.setSocialImage(R.drawable.ic_linkedin);
                social.add(listing);
            }
            if (!pinterest.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("pinterest");
                listing.setUrl(pinterest);
                listing.setSocialImage(R.drawable.ic_pinterest);
                social.add(listing);
            }
            if (!soundcloud.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("soundcloud");
                listing.setUrl(soundcloud);
                listing.setSocialImage(R.drawable.ic_soundcloud);
                social.add(listing);
            }
            if (!stack.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("stack");
                listing.setUrl(stack);
                listing.setSocialImage(R.drawable.ic_overflowing);
                social.add(listing);
            }
            if (!twitter.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("twitter");
                listing.setUrl(twitter);
                listing.setSocialImage(R.drawable.ic_twitter);
                social.add(listing);
            }
            if (!vimeo.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("vimeo");
                listing.setUrl(vimeo);
                listing.setSocialImage(R.drawable.ic_vimeo);
                social.add(listing);
            }
            if (!youtube.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("youtube");
                listing.setUrl(youtube);
                listing.setSocialImage(R.drawable.youtube);
                social.add(listing);
            }

            if (! otherurl.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("otherurl");
                listing.setUrl(otherurl);
                listing.setSocialImage(R.drawable.otherurl);
                social.add(listing);
            }

            if (!website.matches("null")) {
                SocialMediaListing listing = new SocialMediaListing();
                listing.setName("website");
                listing.setUrl(website);
                listing.setSocialImage(R.drawable.ic_world_wide_web);
                social.add(listing);
            }

            GridSocialMediaAdapter gridSocialMediaAdapter = new GridSocialMediaAdapter(SocialMedia.this, social);
            gvSocialMedia.setAdapter(gridSocialMediaAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");
        Intent from_profile = getIntent();
        String user_id = from_profile.getStringExtra("user_id");
        if (session.isLoggedIn() && db_id.equals(user_id)) {
            Intent intent = new Intent(SocialMedia.this, com.bawaaba.rninja4.rookie.MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}