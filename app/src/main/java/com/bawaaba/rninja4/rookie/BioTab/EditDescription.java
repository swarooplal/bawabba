package com.bawaaba.rninja4.rookie.BioTab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.bawaaba.rninja4.rookie.utils.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.bawaaba.rninja4.rookie.App.AppConfig.URL_EDIT_DESCRIPTION;

public class EditDescription extends BaseActivity implements View.OnClickListener  {

    private static final String TAG = EditAbout.class.getSimpleName();
    private EditText edit_description;
    //  private Button save;
    private SQLiteHandler db;
    private AppCompatTextView tvSave;

    JSONParser jsonParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_description);
        //initViews();
        //setOnclick();
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actiontitle_layout);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();

        initViews();
        setOnclick();

        edit_description = (EditText) findViewById(R.id.descriptionBox);
        // save = (Button) findViewById(R.id.but_desc);

        Intent from_Profile = getIntent();
        String description = from_Profile.getStringExtra("description");
        edit_description.setText(description);

        edit_description.addTextChangedListener(textWatcher());

    }

    private TextWatcher textWatcher() {
        return new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!edit_description.getText().toString().equals("")) { //if edittext include text
                    edit_description.setBackgroundResource(R.drawable.grey_box);

                } else { //not include text
                    edit_description.setBackgroundResource(R.drawable.red_alert_round);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void setOnclick() {
        tvSave.setOnClickListener(this);
    }

    private void initViews() {
        tvSave = (AppCompatTextView) findViewById(R.id.tvSave);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

        String descrption = edit_description.getText().toString().trim();

        if(descrption!=null && descrption.length()<50){
            edit_description.setBackgroundResource(R.drawable.red_alert_round);
            Toast.makeText(getApplicationContext(),
                    "Description should have atleast 50 characters!", Toast.LENGTH_LONG).show();
            return ;
        }

        if (!descrption.isEmpty()) {

            Intent i1 = getIntent();
            String user_id = i1.getStringExtra("user_id");
            editdescUser(user_id, descrption);

        } else {
            Toast.makeText(getApplicationContext(),
                    "Please enter your details!", Toast.LENGTH_SHORT)
                    .show();

        }
    }

    private void editdescUser(final String user_id, final String descrption) {
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        final String token = user.get("token");//token value after the user logedin

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_EDIT_DESCRIPTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Log.e("EDIT JSON: ", "updated");
                        Toast.makeText(getApplicationContext(),
                                "Your description has been updated successfully", Toast.LENGTH_LONG).show();

                        Intent to_profile = new Intent(EditDescription.this, ProfileView.class);
                        startActivity(to_profile);
                        finish();
                    }else{
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

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
                params.put("description", descrption);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Client-Service", "app-client");
                headers.put("Auth-Key","123321");
                headers.put("Token",token);
                headers.put("User-Id",user_id);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}







