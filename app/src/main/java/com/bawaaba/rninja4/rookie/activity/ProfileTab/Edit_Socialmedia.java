package com.bawaaba.rninja4.rookie.activity.ProfileTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bawaaba.rninja4.rookie.App.AppController;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.bawaaba.rninja4.rookie.App.AppConfig.URL_ADD_SOCIAL_MEDIA;

public class Edit_Socialmedia extends BaseActivity implements View.OnClickListener {

    private static final String TAG = Edit_Socialmedia.class.getSimpleName();
    private EditText monogram;
    private EditText twitter;
    private EditText Vimeo;
    private EditText Website;
    private EditText Youtube;
    private EditText Linkedin;
    private EditText Instagram;
    private EditText Googleplus;
    private EditText Github;
    private EditText Behance;
    private EditText Stack;
    private EditText Bitbucket;
    private EditText Soundcloud;
    private EditText Dribble;
    private EditText Pinterest;
    private EditText Facebook;
    private EditText Otherlink;
    private AppCompatTextView tvSave;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__socialmedia);
        getSupportActionBar().hide();

        monogram = (EditText) findViewById(R.id.edit_mono);
        twitter = (EditText) findViewById(R.id.edit_twit);
        Vimeo = (EditText) findViewById(R.id.edit_vimeo);
        Website = (EditText) findViewById(R.id.edit_website);
        Youtube = (EditText) findViewById(R.id.edit_youtube);
        Linkedin = (EditText) findViewById(R.id.edit_linkedin);
        Instagram = (EditText) findViewById(R.id.edit_instagram);
        Googleplus = (EditText) findViewById(R.id.edit_google_plus);
        Github = (EditText) findViewById(R.id.edit_github);
        Behance = (EditText) findViewById(R.id.edit_behance);
        Stack = (EditText) findViewById(R.id.edit_stack);
        Bitbucket = (EditText) findViewById(R.id.edit_bitbucket);
        Soundcloud = (EditText) findViewById(R.id.edit_sounclod);
        Dribble = (EditText) findViewById(R.id.edit_dribble);
        Pinterest = (EditText) findViewById(R.id.edit_pineterest);
        Facebook = (EditText) findViewById(R.id.edit_facebook);
        Otherlink = (EditText) findViewById(R.id.edit_otherlink);

        Intent from_Profile = getIntent();
        final String socialmedia = from_Profile.getStringExtra("socialmedia");

        try {
            JSONObject socialmedia_data = new JSONObject(socialmedia);

            String mono_text = socialmedia_data.getString("500Px");
            Log.e("textcheck","textcheck");
            if (!mono_text.equals("null"))
            {
                monogram.setText(mono_text);
            }


            String behance_text = socialmedia_data.getString("behance");

            if (!behance_text.equals("null"))
            {
                Behance.setText(behance_text);
            }


            String facebook_text = socialmedia_data.getString("facebook");
            if (!facebook_text.equals("null"))
            {
                Facebook.setText(facebook_text);
            }


            String dribbble_text = socialmedia_data.getString("dribbble");
            if (!dribbble_text.equals("null"))
            {
                Dribble.setText(dribbble_text);
            }

            String github_text = socialmedia_data.getString("github");
            if (!github_text.equals("null"))
            {
                Github.setText(github_text);
            }


            String bitbucket_text = socialmedia_data.getString("bitbucket");
            if (!bitbucket_text.equals("null"))
            {
                Bitbucket.setText(bitbucket_text);
            }


            String googleplus_text = socialmedia_data.getString("googleplus");
            if (!googleplus_text.equals("null"))
            {
                Googleplus.setText(googleplus_text);
            }



            String instagram_text = socialmedia_data.getString("instagram");
            if (!instagram_text.equals("null"))
            {
                Instagram.setText(instagram_text);
            }


            String linkedin_text = socialmedia_data.getString("linkedin");
            if (!linkedin_text.equals("null"))
            {
                Linkedin.setText(linkedin_text);
            }


            String pinterest_text = socialmedia_data.getString("pinterest");
            if (!pinterest_text.equals("null"))
            {
                Pinterest.setText(pinterest_text);
            }

            String soundcloud_text = socialmedia_data.getString("soundcloud");
            if (!soundcloud_text.equals("null"))
            {
                Soundcloud.setText(soundcloud_text);
            }


            String stack_text = socialmedia_data.getString("stack");
            if (!stack_text.equals("null"))
            {
                Stack.setText(stack_text);
            }

            String twitter_text = socialmedia_data.getString("twitter");
            if (!twitter_text.equals("null"))
            {
                twitter.setText(twitter_text);
            }

            String vimeo_text = socialmedia_data.getString("vimeo");
            if (!vimeo_text.equals("null"))
            {
                Vimeo.setText(vimeo_text);
            }

            String youtube_text = socialmedia_data.getString("youtube");
            if (!youtube_text.equals("null"))
            {
                Youtube.setText(youtube_text);
            }



            String otherurl_text = socialmedia_data.getString("otherurl");
            if (!otherurl_text.equals("null"))
            {
                Otherlink.setText(otherurl_text);
            }

            String website_text = socialmedia_data.getString("website");
            if (!website_text.equals("null"))
            {
                Website.setText(website_text);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        initViews();
        setOnclick();


    }

    private void setOnclick() {
        tvSave.setOnClickListener(this);
    }

    private void initViews() {
        tvSave = (AppCompatTextView) findViewById(R.id.tvSave);

    }

    @Override
    public void onClick(View v) {

        final String mono = monogram.getText().toString().trim();
        final String twit = twitter.getText().toString().trim();
        final String vimeo = Vimeo.getText().toString().trim();
        final String website = Website.getText().toString().trim();
        final String youtube = Youtube.getText().toString().trim();
        final String linkedin = Linkedin.getText().toString().trim();
        final String instagram = Instagram.getText().toString().trim();
        final String googleplus = Googleplus.getText().toString().trim();
        final String github = Github.getText().toString().trim();
        final String behance = Behance.getText().toString().trim();
        final String stack = Stack.getText().toString().trim();
        final String bitbucket = Bitbucket.getText().toString().trim();
        final String souncloud = Soundcloud.getText().toString().trim();
        final String dribble = Dribble.getText().toString().trim();
        final String pinterest = Pinterest.getText().toString().trim();
        final String facebook = Facebook.getText().toString().trim();
        final String otherlink = Otherlink.getText().toString().trim();


        Intent i1 = getIntent();
            String user_id = i1.getStringExtra("user_id");
            edit_socialmedia(user_id, mono, twit, vimeo, website, youtube, linkedin, instagram, googleplus, github, behance, stack, bitbucket, souncloud, dribble, pinterest, facebook, otherlink);


    }

    private void edit_socialmedia(final String user_id, final String mono, final String twit, final String vimeo, final String website, final String youtube, final String linkedin, final String instagram, final String googleplus, final String github, final String behance, final String stack, final String bitbucket, final String souncloud, final String dribble, final String pinterest, final String facebook, final String otherlink) {

        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        final String token = user.get("token");

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_ADD_SOCIAL_MEDIA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Log.e("EDIT JSON: ", "updated");
                        Toast.makeText(getApplicationContext(),
                                "Your social media details have been updated successfully", Toast.LENGTH_LONG).show();

                        Intent to_profile = new Intent(Edit_Socialmedia.this, ProfileView.class);
                        startActivity(to_profile);
                        finish();

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "new", Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", user_id);

                params.put("500px", (mono.isEmpty())?"no":mono);

                params.put("behance", (behance.isEmpty())?"no":behance);

                params.put("facebook", (facebook.isEmpty())?"no":facebook);

                params.put("dribbble", (dribble.isEmpty())?"no":dribble);

                params.put("github", (github.isEmpty())?"no":github);

                params.put("bitbucket", (bitbucket.isEmpty())?"no":bitbucket);

                params.put("googleplus", (googleplus.isEmpty())?"no":googleplus);

                params.put("instagram", (instagram.isEmpty())?"no":instagram);

                params.put("linkedin", (linkedin.isEmpty())?"no":linkedin);

                params.put("pinterest", (pinterest.isEmpty())?"no":pinterest);

                params.put("soundcloud", (souncloud.isEmpty())?"no":souncloud);

                params.put("stackoverflow", (stack.isEmpty())?"no":stack);

                params.put("twitter", (twit.isEmpty())?"no":twit);

                params.put("vimeo", (vimeo.isEmpty())?"no":vimeo);

                params.put("youtube", (youtube.isEmpty())?"no":youtube);

                params.put("website", (website.isEmpty())?"no":website);

                params.put("otherurl", (otherlink.isEmpty())?"no":otherlink);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Client-Service", "app-client");
                headers.put("Auth-Key", "123321");
                headers.put("Token", token);
                headers.put("User-Id", user_id);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}

