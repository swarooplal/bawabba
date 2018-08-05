package com.bawaaba.rninja4.rookie.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.JSONParser;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ForgetPassword extends AppCompatActivity {

    private EditText forget_email;
    private Button reset_password;
    private TextView sign_up;

    private static final String TAG = ForgetPassword.class.getSimpleName();

    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        forget_email = (EditText) findViewById(R.id.email);
        reset_password = (Button) findViewById(R.id.btnLogin);
        sign_up = (TextView) findViewById(R.id.signup);
        getSupportActionBar().hide();

        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = forget_email.getText().toString().trim();


                if(email.length()==0){
                    forget_email.setBackgroundResource(R.drawable.red_alert);
                    Toast.makeText(getApplicationContext(),
                            "Please fill required fields", Toast.LENGTH_LONG)
                            .show();
                }

                if (!isValidEmail(email)) {
                    forget_email.setBackgroundResource(R.drawable.red_alert);
                    Toast.makeText(getApplicationContext(),
                            "Please enter your valid email address", Toast.LENGTH_LONG)
                            .show();
                }

                final Dialog dialog = ObjectFactory.getInstance().getUtils(ForgetPassword.this).showLoadingDialog(ForgetPassword.this);
                dialog.show();

                Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(ForgetPassword.this).getApiService().forgetpassword(
                        "app-client",
                        "123321",
                        email);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.body() != null) {
                            try {
                                String responseString = new String(response.body().bytes());
                                if (responseString != null) {
                                    JSONObject jsonObject = new JSONObject(responseString);
                                    boolean error = jsonObject.getBoolean("error");
                                    if (!error) {

                                        Log.e("working", String.valueOf(jsonObject));


                                        Toast.makeText(getApplicationContext(),
                                                "A password recovery mail has been sent to your given email address", Toast.LENGTH_LONG)
                                                .show();
                                        Intent intent = new Intent(ForgetPassword.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Log.e("working","forget");
                                        Toast.makeText(ForgetPassword.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ForgetPassword.this, "Network Error", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
            private boolean isValidEmail(String email) {
                String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(email);
                return matcher.matches();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ForgetPassword.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}












