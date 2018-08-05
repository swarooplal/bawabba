package com.bawaaba.rninja4.rookie.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;

import java.util.HashMap;

public class Account_settings extends AppCompatActivity {
    private static final String TAG = Account_settings.class.getSimpleName();

    private TextView emailText;
    private TextView passwordText;
    private TextView account_email;
    private TextView account_current_password;
    private Button account_save;
    private SQLiteHandler db;
    private RelativeLayout change_email;
    private RelativeLayout change_password;
    private ArrayAdapter<String> accountAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actiontitle_layout6);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getSupportActionBar().hide();
        change_email=(RelativeLayout)findViewById(R.id.change_email);
        change_password=(RelativeLayout)findViewById(R.id.change_password);

        emailText=(TextView)findViewById(R.id.email_text);
        passwordText=(TextView)findViewById(R.id.password_text);
        account_email = (TextView) findViewById(R.id.email_edit);
        account_current_password = (TextView) findViewById(R.id.password_edit);
//        account_new_password = (EditText) findViewById(R.id.account_new_password);
//        account_retype_password = (EditText) findViewById(R.id.account_retype_password);
//        account_save = (Button) findViewById(R.id.account_save);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");

        account_email.setText(email);
        change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_change_email = new Intent(Account_settings.this,Change_email.class);
                startActivity(to_change_email);
                finish();
            }
        });
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_change_password = new Intent(Account_settings.this,Change_password.class);
                startActivity(to_change_password);
                finish();
            }
        });

    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(Account_settings.this, Profile_settings.class);
//        startActivity(intent);
//        finish();
//    }

}