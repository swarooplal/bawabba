package com.bawaaba.rninja4.rookie.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ContactActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ContactActivity.class.getSimpleName();
    private EditText Name;
    private EditText Email;
    private EditText ContactNum;
    private EditText Message;
    private Button SendMessage;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().hide();
        Name = (EditText) findViewById(R.id.name);
        Email = (EditText) findViewById(R.id.email);
        ContactNum = (EditText) findViewById(R.id.conatact_num);
        Message = (EditText) findViewById(R.id.message);

        initViews();
        setOnclick();


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
        SendMessage.setOnClickListener(this);
    }

    private void initViews() {

        SendMessage = (Button) findViewById(R.id.send_message);
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }


    @Override
    public void onClick(View v) {
        contactactivity();
    }

    private void contactactivity() {
        String name = Name.getText().toString().trim();
        if (name.matches("")) {
            Name.setBackgroundResource(R.drawable.red_alert);
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = Email.getText().toString().trim();
        if (email.matches("")) {

            Email.setBackgroundResource(R.drawable.red_alert);
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!email.isEmpty()&&!isValidEmail(email)){
            Email.setBackgroundResource(R.drawable.red_alert);
            Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        String phone = ContactNum.getText().toString().trim();
//
//        if (phone != null && phone.length() < 13) {
//            ContactNum.setBackgroundResource(R.drawable.red_alert);
//            Toast.makeText(ContactActivity.this, "Minimum 13 digits are required for contact number with country code", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if ( phone.length()==0) {
//            ContactNum.setBackgroundResource(R.drawable.red_alert);
//            Toast.makeText(ContactActivity.this, "Please enter your valid phone number", Toast.LENGTH_SHORT).show();
//            return;
//        }


        String message = Message.getText().toString().trim();


        if (message.matches("")) {
            Toast.makeText(this, "Please enter your message", Toast.LENGTH_SHORT).show();
            return;
        }

        if(message.length()<10) {
            Message.setBackgroundResource(R.drawable.red_alert);
            Toast.makeText(this, "Minimum 10 characters required for message.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(message.length()>1500) {
            Message.setBackgroundResource(R.drawable.red_alert);
            Toast.makeText(this, "Message should not exceed more than 1500 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent from_Profile = getIntent();
        final String profile_id = from_Profile.getStringExtra("profile_id");
        Log.e("profileid",profile_id);


        final Dialog dialog = ObjectFactory.getInstance().getUtils(ContactActivity.this).showLoadingDialog(ContactActivity.this);
        dialog.show();

        if(phone.isEmpty()){
            phone="no";
        }

        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(ContactActivity.this).getApiService().contactform(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                profile_id,
                name,
                email,
                phone,
                message);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
               // dialog.dismiss();
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            boolean error = jsonObject.getBoolean("error");
                            Log.e("errormessgesss", String.valueOf(error));
                            Log.e("errormessgesss", String.valueOf(jsonObject.getString("message")));
                            if (!error) {
                                Toast.makeText(getApplicationContext(),
                                        "Your message has been sent successfully", Toast.LENGTH_LONG).show();
                                /*Intent to_profile = new Intent(ContactActivity.this, ProfileView.class);
                                to_profile.putExtra("reg_id",profile_id);
                                startActivity(to_profile);*/
                                finish();
                            } else {
                                Toast.makeText(ContactActivity.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ContactActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(ContactActivity.this, ProfileView.class);
//        startActivity(intent);
//        finish();
    }
}

