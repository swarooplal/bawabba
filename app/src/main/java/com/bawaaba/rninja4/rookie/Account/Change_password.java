package com.bawaaba.rninja4.rookie.Account;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;
import com.gdacciaro.iOSDialog.iOSDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Change_password extends BaseActivity implements View.OnClickListener {

    private String TAG = Change_password.class.getSimpleName();
    private EditText Currentpassword;
    private EditText Newpassword;
    private EditText Retypepassword;
    private AppCompatTextView tvSave;
    private SQLiteHandler db;
    private SessionManager session;
    private Button Ok;
    private Button btnClear;
    private Button btnClear1;
    private Button btnClear2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Currentpassword = (EditText) findViewById(R.id.current_password);
        Newpassword = (EditText) findViewById(R.id.new_password);
        Retypepassword = (EditText) findViewById(R.id.retype_password);
        btnClear = (Button)findViewById(R.id.btn_clear);
        btnClear1 = (Button)findViewById(R.id.btn_clear1);
        btnClear2 = (Button)findViewById(R.id.btn_clear2);


        Currentpassword.addTextChangedListener(textWatcher());
        btnClear.setOnClickListener(onClickListener());

        Newpassword.addTextChangedListener(textWatcher1());
        btnClear1.setOnClickListener(onClickListener1());

        Retypepassword.addTextChangedListener(textWatcher2());
        btnClear2.setOnClickListener(onClickListener2());

        getSupportActionBar().hide();
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        initViews();
        setOnclick();
    }

    private TextWatcher textWatcher2() {
        return new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!Retypepassword.getText().toString().equals("")) { //if edittext include text
                    btnClear2.setVisibility(View.VISIBLE);

                } else { //not include text
                    btnClear2.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

    }

    private View.OnClickListener onClickListener2() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Retypepassword.setText(""); //clear edittext

            }
        };

    }

    private TextWatcher textWatcher1() {
        return new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!Newpassword.getText().toString().equals("")) { //if edittext include text
                    btnClear1.setVisibility(View.VISIBLE);

                } else { //not include text
                    btnClear1.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private View.OnClickListener onClickListener1() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Newpassword.setText(""); //clear edittext

            }
        };
    }


    private View.OnClickListener onClickListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Currentpassword.setText(""); //clear edittext

            }
        };
    }

    private TextWatcher textWatcher() {
        return new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!Currentpassword.getText().toString().equals("")) { //if edittext include text
                    btnClear.setVisibility(View.VISIBLE);

                } else { //not include text
                    btnClear.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
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

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String password = user.get("password");
        String current_password = Currentpassword.getText().toString().trim();
        String new_password = Newpassword.getText().toString().trim();
        String retype_password = Retypepassword.getText().toString().trim();

        if (!current_password.isEmpty() && !new_password.isEmpty() && !retype_password.isEmpty()) {
            chanagePassword(current_password, new_password);
        } else {
            Currentpassword.setBackgroundResource(R.drawable.red_alert_round);
            Newpassword.setBackgroundResource(R.drawable.red_alert_round);
            Retypepassword.setBackgroundResource(R.drawable.red_alert_round);
            Toast.makeText(getApplicationContext(),
                    "Please enter your old password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!current_password.isEmpty()&&new_password.isEmpty()||retype_password.isEmpty()) {
            Newpassword.setBackgroundResource(R.drawable.red_alert);
            Toast.makeText(Change_password.this, "Please enter your new password.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (new_password != null && new_password.length() < 6) {
            Newpassword.setBackgroundResource(R.drawable.red_alert);
            Toast.makeText(Change_password.this, "New password should be of minimum 6 characters.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!retype_password.equals(new_password)) {
            //chanagePassword(current_password, new_password);
            Toast.makeText(getApplicationContext(),
                    "Password not matching.Please enter again", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void chanagePassword(String current_password, String new_password) {
        final Dialog dialog = ObjectFactory.getInstance().getUtils(Change_password.this).showLoadingDialog(Change_password.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(Change_password.this).getApiService().changePassword(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                new_password,
                current_password,
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getEmail()
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            final JSONObject jsonObject = new JSONObject(responseString);
                            if (jsonObject != null) {
                                System.out.println("ProfileView.onResponse " + responseString);
                                if (!jsonObject.getBoolean("error")) {

                                    final iOSDialog iOSDialog = new iOSDialog(Change_password.this);
                                    iOSDialog.setTitle( "Success");
                                    iOSDialog.setSubtitle("A verification email has been sent to your  email address.");
                                    iOSDialog.setPositiveLabel("Ok");
                                    iOSDialog.setBoldPositiveLabel(true);

                                    iOSDialog.setPositiveListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Intent intent = new Intent(Change_password.this,Account_settings.class ); // for logout
                                            startActivity(intent);
                                            finish();
                                            iOSDialog.dismiss();
                                        }
                                    });
                                    iOSDialog.show();

                                    Toast.makeText(Change_password.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Change_password.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                                }
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
                Toast.makeText(Change_password.this, "Network Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Change_password.this, Account_settings.class);
        startActivity(intent);
        finish();
    }
}
