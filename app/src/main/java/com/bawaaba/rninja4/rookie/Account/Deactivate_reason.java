package com.bawaaba.rninja4.rookie.Account;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.LoginActivity;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Deactivate_reason extends BaseActivity implements View.OnClickListener {

    private static final String TAG = Deactivate_reason.class.getSimpleName();

    private TextView Deactivation_reason;
    private RadioGroup Reason;
    private RadioButton first_reason;
    private RadioButton second_reason;
    private RadioButton third_reason;
    private RadioButton other_reason;
    private RadioButton radioButton;
    private EditText other_reasons;
    private AppCompatTextView deactivate;
    private SQLiteHandler db;
    private SessionManager session;
    private String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate_reason);
        getSupportActionBar().hide();

        other_reasons = (EditText) findViewById(R.id.other_reason);
        Reason = (RadioGroup) findViewById(R.id.reason);
        first_reason = (RadioButton) findViewById(R.id.use);
        second_reason = (RadioButton) findViewById(R.id.safe);
        third_reason = (RadioButton) findViewById(R.id.expect);
        other_reason = (RadioButton) findViewById(R.id.other);
        other_reasons.setVisibility(View.GONE);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        Reason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {


                if (checkedId == R.id.other) {
                    other_reasons.setVisibility(View.VISIBLE);
                } else {
                    other_reasons.setVisibility(View.GONE);
                }
            }
        });

        initViews();
        setOnclick();

    }

    private void setOnclick() {
        deactivate.setOnClickListener(this);
    }

    private void initViews() {
        deactivate = (AppCompatTextView) findViewById(R.id.deactivate);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

        int selectedId = Reason.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        String radio_value = (String) radioButton.getText();

        final String reason = (radio_value.equals("Other") ? other_reasons.getText().toString().trim() : radio_value);
        deactivation();
    }

    private void deactivation() {

        String radio_value = (String) radioButton.getText();
        final String reason = (radio_value.equals("Other") ? other_reasons.getText().toString().trim() : radio_value);

        if(reason.length()==0){
            Toast.makeText(this, "Please enter your reason for deactivation", Toast.LENGTH_SHORT).show();
            return;
        }


        final Dialog dialog = ObjectFactory.getInstance().getUtils(Deactivate_reason.this).showLoadingDialog(Deactivate_reason.this);
        dialog.show();

        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(Deactivate_reason.this).getApiService().deactivateaccount(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),

                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(), reason);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.body()!= null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            boolean error = jsonObject.getBoolean("error");
                            if (!error) {

                                Log.e("working", String.valueOf(jsonObject));

                                Toast.makeText(Deactivate_reason.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                db.deleteUsers();
                                session.setLogin(false);
                                Intent intent = new Intent(Deactivate_reason.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                Log.e("working", "forget");
                                Toast.makeText(Deactivate_reason.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Deactivate_reason.this, "Network Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Deactivate_reason.this, Deactivate_Profile.class);
        startActivity(intent);
    }
}
