package com.bawaaba.rninja4.rookie.BioTab;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bawaaba.rninja4.rookie.App.AppController;
import com.bawaaba.rninja4.rookie.JSONParser;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bawaaba.rninja4.rookie.App.AppConfig.URL_EDIT_ABOUT_DETAILS;

public class EditAbout extends AppCompatActivity {

    private static final String TAG = EditAbout.class.getSimpleName();

    private EditText Textbox;

    private Button save;

    private SQLiteHandler db;

    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_about);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actiontitle_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Textbox=(EditText)findViewById(R.id.aboutBox);
        save=(Button) findViewById(R.id.but_about);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String about_me = Textbox.getText().toString().trim();

                if (!about_me.isEmpty()) {

                    Intent from_profile = getIntent();
                    String user_id = from_profile.getStringExtra("user_id");
                    editaboutUser(user_id, about_me);

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_SHORT)
                            .show();

                }
            }
        });

                new Editabout_me().execute();


            }

    private void editaboutUser(final String user_id, final String about_me) {

        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        final String token = user.get("token");//token value after the user logedin

        Log.e("token value: ", token);
        Log.e("user_id: ", user_id);

        String tag_string_req = "req_register";

         StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_EDIT_ABOUT_DETAILS, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                try {
                    Log.e("EDIT JSON: ", response);
                    Toast.makeText(getApplicationContext(),
                            "updated", Toast.LENGTH_LONG).show();

                    Intent to_profile = new Intent(EditAbout.this,ProfileView.class);
                    startActivity(to_profile);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage()+"new", Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", user_id);
                params.put("aboutme", about_me);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Client-Service", "app-client");
                headers.put("Auth-Key","123321");
                headers.put("User-Id",user_id);
                headers.put("Token",token);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);



    }

    private class Editabout_me extends AsyncTask<String, String, String> {

                @Override
                protected String doInBackground(String... args) {

                    Intent i1 = getIntent();
                    String user_id = i1.getStringExtra("user_id");
                    String Client_service = "app-client";
                    String Auth_key = "123321";
                    String token = "";
                    Log.e("USERID1", user_id);
                    final String ABOUT_URL = "http://demo.rookieninja.com/api/user/biography?user_id=" + user_id;
                    List<NameValuePair> params = new ArrayList<NameValuePair>();

                    JSONObject about = jsonParser.makeHttpRequest(ABOUT_URL, "GET",
                            params,Client_service,Auth_key,token,user_id);
                    Log.e("AboutJSON: ", about.toString());

                    try {

//                final JSONArray abouttab = about.getJSONArray("biography");

                        final String abouttab = about.getString("biography");
                        Log.e("AboutJSON2: ", String.valueOf(abouttab));
                        EditAbout.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Textbox.setText(abouttab);

                            }
                        });


                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    return null;
                }

            }
        }
