package com.bawaaba.rninja4.rookie.Account;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.MainActivity;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Change_email extends BaseActivity implements View.OnClickListener {

    private String TAG = Change_email.class.getSimpleName();

    private EditText newEmail;
    private AppCompatTextView tvSave;
    private SQLiteHandler db;
    private SessionManager session;
    private Button Ok;
    private Button cancel;
    private AppCompatEditText Password;
    private Button Confirm;
    private Button btnClear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        newEmail = (EditText) findViewById(R.id.new_email);
        tvSave = (AppCompatTextView) findViewById(R.id.tvSave);
        btnClear = (Button)findViewById(R.id.btn_clear);

        newEmail.addTextChangedListener(textWatcher());
        btnClear.setOnClickListener(onClickListener());

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actiontitle_layout9);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getSupportActionBar().hide();
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        final String user_id = user.get("uid");
        final String email = user.get("email");
        final String token = user.get("token");
        final String password = user.get("password");
        Log.e("password",password);
        newEmail.setText(email);
        newEmail.setSelection(newEmail.getText().length());

        initViews();
        setOnclick();
    }


    private View.OnClickListener onClickListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                newEmail.setText(""); //clear edittext
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

                if (!newEmail.getText().toString().equals("")) { //if edittext include text
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

        final String new_email = newEmail.getText().toString().trim();

        if(!new_email.isEmpty()&&!isValidEmail(new_email)){
            newEmail.setBackgroundResource(R.drawable.red_alert_round);
            Toast.makeText(getApplicationContext(),
                    "Please enter valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!new_email.isEmpty()) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(Change_email.this);
            final View mView = getLayoutInflater().inflate(R.layout.dialogue_email, null);
            Password=(AppCompatEditText)mView.findViewById(R.id.tvTitle);
            Ok=(Button)mView.findViewById(R.id.ok);
            cancel=(Button)mView.findViewById(R.id.cancel);

            Ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db = new SQLiteHandler(getApplicationContext());
                    HashMap<String, String> user = db.getUserDetails();
                    final String current_password = user.get("password");
                    String password=Password.getText().toString().trim();
                    if (!password.isEmpty()&&(password.matches(current_password))) {
                        changeEmail(new_email);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter the current password!", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });

            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            dialog.setCancelable(false);


            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  dialog.setCancelable(true);
                    dialog.dismiss();
                }
            });


        } else{
         newEmail.setBackgroundResource(R.drawable.red_alert_round);
            Toast.makeText(getApplicationContext(),
                    "Please enter email address", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    private void changeEmail(final String new_email) {
        final Dialog dialog = ObjectFactory.getInstance().getUtils(Change_email.this).showLoadingDialog(Change_email.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(Change_email.this).getApiService().changeEmail(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getEmail(),
                new_email
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

                                    final iOSDialog iOSDialog = new iOSDialog(Change_email.this);
                                    iOSDialog.setTitle( "Success");
                                    iOSDialog.setSubtitle("A verification email has been sent to your new email address.");
                                    iOSDialog.setPositiveLabel("Ok");
                                    iOSDialog.setBoldPositiveLabel(true);

                                    iOSDialog.setPositiveListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            db.deleteUsers();
                                                session.setLogin(false);
                                                Intent intent = new Intent(Change_email.this, MainActivity.class); // for logout
                                                startActivity(intent);
                                                finish();
                                               iOSDialog.dismiss();
                                        }
                                    });
                                    iOSDialog.show();

                                }else{
                                    Toast.makeText(Change_email.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Change_email.this, "Network Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Change_email.this, Account_settings.class);
        startActivity(intent);
        finish();
    }
}