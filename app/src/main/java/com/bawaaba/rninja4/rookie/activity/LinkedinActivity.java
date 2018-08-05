package com.bawaaba.rninja4.rookie.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.R;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class LinkedinActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgProfile,imgLogin;
    TextView txtDetails;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkedin);

        InitilizeControls();
    }
    private void InitilizeControls() {

        imgLogin = (ImageView) findViewById(R.id.imgLogin);
        imgLogin.setOnClickListener(this);

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);

        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        txtDetails = (TextView) findViewById(R.id.txtDetails);

        //default

        imgLogin.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.GONE);
        imgProfile.setVisibility(View.GONE);
        txtDetails.setVisibility(View.GONE);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgLogin:
                handleLogin();
                break;
            case R.id.btnLogout:
                handleLogout();
                break;

        }
    }
    private void handleLogout(){

        LISessionManager.getInstance(getApplicationContext()).clearSession();
        imgLogin.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.GONE);
        imgProfile.setVisibility(View.GONE);
        txtDetails.setVisibility(View.GONE);
        Log.d("myApp", "no network");

    }
    private void handleLogin() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.

                imgLogin.setVisibility(View.GONE);
                btnLogout.setVisibility(View.VISIBLE);
                imgProfile.setVisibility(View.VISIBLE);
                txtDetails.setVisibility(View.VISIBLE);

                fetchPersonalInfo();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
              Log.e("Arjun",error.toString());
            }
        }, true);
    }
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE,Scope.R_EMAILADDRESS);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Add this line to your existing onActivityResult() method
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }
    private void fetchPersonalInfo(){
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,picture-url,email-address,picture-urls::(original))";

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                JSONObject jsonObject=apiResponse.getResponseDataAsJson();
                try {
                    String firstName=jsonObject.getString("firstName");
                    String lastName=jsonObject.getString("lastName");
                    String pictureUrl=jsonObject.getString("pictureUrl");
                    String emailAddress=jsonObject.getString("emailAddress");
                    Log.d("Arjunlink", String.valueOf(jsonObject));
                    Picasso.with(getApplicationContext()).load(pictureUrl).into(imgProfile);
                    StringBuilder sb=new StringBuilder();
                    sb.append("FirstName: "+firstName);
                    sb.append("\n\n");
                    sb.append("LastName: "+lastName);
                    sb.append("\n\n");
                    sb.append("Email: "+emailAddress);
                    sb.append("\n\n");
                    txtDetails.setText(sb);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!

                Log.e("ARJUN",liApiError.getMessage());
            }
        });
    }
}
