package com.bawaaba.rninja4.rookie.BioTab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.dashboard_new.ProfileViewFragment;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.currencies.CurrenciesResponse;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.bawaaba.rninja4.rookie.utils.AppPreference;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddServices extends BaseActivity implements View.OnClickListener {
    List<String> currency = new ArrayList<>();
    List<AppCompatEditText> finalTittle = new ArrayList<>();
    List<AppCompatEditText> finalAmount = new ArrayList<>();
    List<AppCompatEditText> finalDescription = new ArrayList<>();
    List<AppCompatSpinner> finalSpinner = new ArrayList<>();
    int count = 0;
    private AppCompatTextView tvSave;
    private LinearLayout llServiceLayout;
    private AppCompatButton btnAddServices;
    private LinearLayout spinnerLayout;
    private LinearLayout droplayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);

        getSupportActionBar().hide();
        initViews();
        setOnclick();
        apiCalltoLoadCurrency();

        //  llServiceLayout.removeAllViews();
    }
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (getCurrentFocus() != null) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    private void setOldData() {
        String response = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileResponse();
        final Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);
        for (int i =0; i < profileresponse.getUserData().getServices().size(); i++) {
            final View view1 = AddServices.this.getLayoutInflater().inflate(R.layout.layout_add_services, null);
            final AppCompatEditText etTittle = (AppCompatEditText) view1.findViewById(R.id.etTittle);
            final AppCompatEditText etAmount = (AppCompatEditText) view1.findViewById(R.id.etAmount);
            final AppCompatEditText etDescription = (AppCompatEditText) view1.findViewById(R.id.etDescription);
            final ImageView ivDelete = (ImageView) view1.findViewById(R.id.ivDelete);
            final ImageView dropdown = (ImageView) view1.findViewById(R.id.drop_id);
            final AppCompatSpinner spinnerCurrany = (AppCompatSpinner) view1.findViewById(R.id.spinnerCurrany);
            final LinearLayout droplayout = (LinearLayout) view1.findViewById(R.id.drop_layout);




            ivDelete.setTag(count);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, currency);//setting the country_array to spinner
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // .getBackground().setColorFilter(ContextCompat.getColor(this,R.color.white), PorterDuff.Mode.SRC_ATOP);
            spinnerCurrany.setAdapter(adapter);

            spinnerCurrany.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ((TextView) spinnerCurrany.getSelectedView()).setTextColor(Color.WHITE);
                }
            });
            etTittle.setText(profileresponse.getUserData().getServices().get(i).getTitle());
            etAmount.setText(profileresponse.getUserData().getServices().get(i).getPrice());
            etDescription.setText(profileresponse.getUserData().getServices().get(i).getDescription());

            for (int j = 0; j < currency.size(); j++) {
                if (currency.get(j).matches(profileresponse.getUserData().getServices().get(i).getCurrency())) {
                    spinnerCurrany.setSelection(j);
                    //Log.e("value of j", String.valueOf(currency.size()));
                }

            }
            spinnerCurrany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    if(selectedItem.matches("Ask for Price")){
                      etAmount.setVisibility(View.GONE);
                     droplayout.getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;


                     // finalAmount.remove(etAmount);
                    }else{
                        etAmount.setVisibility(View.VISIBLE);
                        droplayout.getLayoutParams().width=200;

                    }
                    Log.e("selectedspinner",selectedItem);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

//if you want to set any action you can do in this listener
            finalTittle.add(etTittle);
            finalAmount.add(etAmount);
            finalDescription.add(etDescription);
            finalSpinner.add(spinnerCurrany);

            count = count + 1;
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalSpinner.remove(spinnerCurrany);
                    finalAmount.remove(etAmount);
                    finalTittle.remove(etTittle);
                    finalDescription.remove(etDescription);
                    llServiceLayout.removeView(view1);
                }
            });
            llServiceLayout.addView(view1);
        }
    }
    private void setOnclick() {
        btnAddServices.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }
    private void addServices() {
        final View view = AddServices.this.getLayoutInflater().inflate(R.layout.layout_add_services, null);
        final AppCompatEditText etTittle = (AppCompatEditText) view.findViewById(R.id.etTittle);
        final AppCompatEditText etAmount = (AppCompatEditText) view.findViewById(R.id.etAmount);
        final AppCompatEditText etDescription = (AppCompatEditText) view.findViewById(R.id.etDescription);
        final AppCompatSpinner spinnerCurrany = (AppCompatSpinner) view.findViewById(R.id.spinnerCurrany);
        final ImageView dropdown = (ImageView) view.findViewById(R.id.drop_id);
        final LinearLayout spinner_layout = (LinearLayout) view.findViewById(R.id.RelativeLayout1);
        final LinearLayout droplayout = (LinearLayout) view.findViewById(R.id.drop_layout);

        final ImageView ivDelete=(ImageView)view.findViewById(R.id.ivDelete);
        String compareValue = "AED";
        ivDelete.setTag(count);

        ArrayAdapter<String >adapter = new ArrayAdapter<String> (getApplicationContext(), R.layout.spinner_style,currency );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrany.setAdapter(adapter);

        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spinnerCurrany.setSelection(spinnerPosition);
        }


        spinnerCurrany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.matches("Ask for Price")){
                    etAmount.setVisibility(View.GONE);
                    droplayout.getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;

                }else{
                    etAmount.setVisibility(View.VISIBLE);
                    droplayout.getLayoutParams().width=200;

                }
                Log.e("selectedspinner",selectedItem);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
         finalTittle.add(etTittle);
         finalAmount.add(etAmount);
         finalDescription.add(etDescription);
         finalSpinner.add(spinnerCurrany);
         count = count + 1;
         ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalSpinner.remove(spinnerCurrany);
                finalAmount.remove(etAmount);
                finalTittle.remove(etTittle);
                finalDescription.remove(etDescription);
                llServiceLayout.removeView(view);
            }
        });
        llServiceLayout.addView(view);
    }

    private void apiCalltoLoadCurrency() {
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(AddServices.this).getApiService().getCurrency("app-client",
                "123321");
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            currency = new ArrayList<String>();
                            Log.e("currencycheck", String.valueOf(currency));
                            CurrenciesResponse currenciesResponse = new Gson().fromJson(responseString, CurrenciesResponse.class);
                            if (!currenciesResponse.getError()) {
                                for (int i = 0; i < currenciesResponse.getCurrencies().size(); i++) {
                                    if(currenciesResponse.getCurrencies().get(i).getCode().matches("ASK")){
                                        currency.add("Ask for Price");
                                    }else{
                                        currency.add(currenciesResponse.getCurrencies().get(i).getCode());
                                       // currency.add(currenciesResponse.getCurrencies().get(i).getName());
                                    }

                                  //  currency.add(currenciesResponse.getCurrencies().get(i).getName());
                                }
                               //addServices();
                                setOldData();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddServices.this, "failed to load..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {

        tvSave = (AppCompatTextView) findViewById(R.id.tvSave);
        llServiceLayout = (LinearLayout) findViewById(R.id.llServiceLayout);
        btnAddServices = (AppCompatButton) findViewById(R.id.btnAddServices);
        spinnerLayout=(LinearLayout)findViewById(R.id.RelativeLayout1);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddServices:
                addServices();
                break;
            case R.id.tvSave:
             if (validFields()){
                 saveData();
             }

                break;
            default:
                break;
        }
    }
    private boolean validFields() {
        boolean status = true;
        Log.e("count", String.valueOf(finalDescription.size()));

        for (int i = 0; i < finalDescription.size(); i++) {
            if (TextUtils.isEmpty(finalSpinner.get(i).getSelectedItem().toString())) {
                status = false;
            }
            if(!finalSpinner.get(i).getSelectedItem().equals("Ask for Price")){
                if (TextUtils.isEmpty(finalAmount.get(i).getText().toString().trim())) {
                    status = false;
                   // spinnerLayout.setBackgroundResource(R.drawable.red_alert_round);
                finalAmount.get(i).setBackgroundResource(R.drawable.red_alert_round);
                    Toast.makeText(AddServices.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                }
            }
            Log.e("selectescuurency", String.valueOf(finalSpinner.get(i)));

             if (TextUtils.isEmpty(finalDescription.get(i).getText().toString().trim())) {
                 status = false;
                 // finalDescription.get(i).setError("");
                 finalDescription.get(i).setBackgroundResource(R.drawable.red_alert_round);
                 Toast.makeText(AddServices.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
             }else{
                 finalDescription.get(i).setBackgroundResource(R.drawable.grey_box);

            } if (TextUtils.isEmpty(finalTittle.get(i).getText().toString().trim())) {
                status = false;
                //finalTittle.get(i).setError("");
                finalTittle.get(i).setBackgroundResource(R.drawable.red_alert_round);
                Toast.makeText(AddServices.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            }else {
                finalTittle.get(i).setBackgroundResource(R.drawable.grey_box);
            }
        }
        return status;
    }

    private void saveData() {

        List<String> tittle = new ArrayList<>();
        List<String> description = new ArrayList<>();
        List<String> amount = new ArrayList<>();
        List<String> spiner = new ArrayList<>();

        for (int i = 0; i < finalSpinner.size(); i++) {
//            System.out.println("AddServiceActivity.saveData " + finalSpinner.get(i).getSelectedItem().toString());
//            System.out.println("AddServiceActivity.saveData " + finalAmount.get(i).getText().toString().trim());
//            System.out.println("AddServiceActivity.saveData " + finalDescription.get(i).getText().toString().trim());
//            System.out.println("AddServiceActivity.saveData " + finalTittle.get(i).getText().toString().trim());
            tittle.add(finalTittle.get(i).getText().toString().trim());
            description.add(finalDescription.get(i).getText().toString().trim());
            amount.add(finalAmount.get(i).getText().toString().trim());
            spiner.add(finalSpinner.get(i).getSelectedItem().toString());
        }
        JSONArray tittleJson = new JSONArray(tittle);
        JSONArray currencyJson = new JSONArray(spiner);
        JSONArray amountJson = new JSONArray(amount);
        JSONArray descriptionJson = new JSONArray(description);

        HashMap<String, String> postvalue = new HashMap<>();
        for (int i = 0; i < finalSpinner.size(); i++) {
            postvalue.put("title[" + i + "]", finalTittle.get(i).getText().toString().trim());
            postvalue.put("currency[" + i + "]", (finalSpinner.get(i).getSelectedItem().toString().matches("Ask for Price")?"ASK":finalSpinner.get(i).getSelectedItem().toString()));
            postvalue.put("price[" + i + "]",finalSpinner.get(i).getSelectedItem().toString().matches("Ask for Price")?"0": finalAmount.get(i).getText().toString().trim());
            postvalue.put("description[" + i + "]", finalDescription.get(i).getText().toString().trim());
        }
        System.out.println("AddServiceActivity.saveData" + descriptionJson.toString());



        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(AddServices.this).getApiService().addServices("app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                postvalue
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            System.out.println("AddServiceActivity.onResponse" + responseString);
                            if (!jsonObject.getBoolean("error")) {
                                Toast.makeText(AddServices.this, "Your services have been updated successfully", Toast.LENGTH_SHORT).show();
                                AppPreference appPreference=ObjectFactory.getInstance().getAppPreference(getApplicationContext());
                                BaseBottomHelperActivity.start(getApplicationContext(), ProfileViewFragment.class.getName(),appPreference.getUserId(),appPreference.getUserName());
                                /*Intent intent = new Intent(AddServices.this, ProfileView.class);
                                startActivity(intent);*/
                                finish();
                            } else {
                                Toast.makeText(AddServices.this, "Some error occurred", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddServices.this, "failed to load..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}