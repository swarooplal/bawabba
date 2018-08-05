package com.bawaaba.rninja4.rookie.Account;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.dashboard_new.ProfileViewFragment;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.bawaaba.rninja4.rookie.utils.AppPreference;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class GeneralSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton rbHire;
    private RadioButton rbWork;
    private AppCompatTextView tvDescription;
    private AppCompatButton btnSubmit;
    private AppCompatEditText etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_settings);

        getSupportActionBar().hide();
        String response = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileResponse();
        final Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);
        initViews();
        btnClicks();
        etDescription.setText(profileresponse.getUserData().getDescription());

        if (profileresponse.getUserData().getRole().equalsIgnoreCase("freelancer")) {
            rbWork.performClick();
        } else {
            rbHire.performClick();
        }

    }

    private void btnClicks() {
        rbHire.setOnClickListener(this);
        rbWork.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }
    private void initViews() {
        rbHire = (RadioButton) findViewById(R.id.rbHire);
        rbWork = (RadioButton) findViewById(R.id.rbWork);
        etDescription = (AppCompatEditText) findViewById(R.id.etDescription);
        btnSubmit = (AppCompatButton) findViewById(R.id.btnSubmit);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSubmit:

                if (TextUtils.isEmpty(etDescription.getText().toString().trim())){
                    etDescription.setBackgroundResource(R.drawable.red_alert_round);
                    Toast.makeText(GeneralSettingsActivity.this, "Please enter your profile description", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    etDescription.setBackgroundResource(R.drawable.rectangular_edit2);
                }

                if (!TextUtils.isEmpty(etDescription.getText().toString().trim()) && etDescription.length() < 50) {
                    etDescription.setBackgroundResource(R.drawable.red_alert);
                    Toast.makeText(GeneralSettingsActivity.this, "Description should be minimum of 50 characters.", Toast.LENGTH_SHORT).show();

                    return;
                }

                if (btnSubmit.getText().toString().trim().matches("Next") && !TextUtils.isEmpty(etDescription.getText().toString().trim())) {
                    String response = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileResponse();
                    final Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);

                    String category;
                    category= String.valueOf(profileresponse.getUserData().getCategory());
                    String skills;
                    skills= String.valueOf(profileresponse.getUserData().getSkills());
                    Log.e("skillcheckuser",skills);

                    ObjectFactory.getInstance().getAppPreference(getApplicationContext()).setDescription(etDescription.getText().toString().trim());
                    Intent to_work = new Intent(this, SwitchToWorkActivity.class);
                    to_work.putExtra("category",category);
                  //  to_work.putExtra("skills",skills);
                    to_work.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(to_work);
                    finishAffinity();
                } else {
                    apiCallToSubmit();
                }
                break;
            case R.id.rbHire:
                rbHire.setChecked(true);
                rbWork.setChecked(false);
                btnSubmit.setText("Save");
                etDescription.setVisibility(View.GONE);
                break;
            case R.id.rbWork:
                rbHire.setChecked(false);
                rbWork.setChecked(true);
                btnSubmit.setText("Next");
                etDescription.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
    private void apiCallToSubmit() {
        final Dialog dialog = ObjectFactory.getInstance().getUtils(GeneralSettingsActivity.this).showLoadingDialog(GeneralSettingsActivity.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(GeneralSettingsActivity.this).getApiService().changeType(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                "1", "", "", ""
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            System.out.println("PortfolioVideoEditActivity.onResponse " + responseString);
                            if (jsonObject != null) {
                                if (!jsonObject.getBoolean("error")) {
                                    Toast.makeText(GeneralSettingsActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    AppPreference appPreference=ObjectFactory.getInstance().getAppPreference(getApplicationContext());
                                    BaseBottomHelperActivity.start(getApplicationContext(), ProfileViewFragment.class.getName(),appPreference.getUserId(),appPreference.getUserName());
                                    /*Intent intent = new Intent(GeneralSettingsActivity.this, ProfileView.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);*/
                                    finishAffinity();
                                } else {
                                    Toast.makeText(GeneralSettingsActivity.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
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
                dialog.dismiss();
                Toast.makeText(GeneralSettingsActivity.this, "failed..", Toast.LENGTH_SHORT).show();
            }
        });
    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(GeneralSettingsActivity.this, Profile_settings.class);
//        startActivity(intent);
//        finish();
//    }

}

