package com.bawaaba.rninja4.rookie.dashboard.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ActivitySearchSetSkills;
import com.bawaaba.rninja4.rookie.activity.SearchResult;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.searchResult.SearchResultResponse;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by rninja4 on 7/16/18.
 */

public class FragmentSearch extends Fragment {


    List<String> responseList = new ArrayList<String>();
    @BindView(R.id.butsearch)
    Button buttonSearchUsers;
    @BindView(R.id.textsearch)
    EditText InputSearch;
    @BindView(R.id.locations)
    AppCompatTextView InputLocation;
    @BindView(R.id.tvClear)
    AppCompatTextView tvClear;
    @BindView(R.id.textskill)
    MultiAutoCompleteTextView simpleMultiAutoCompleteTextView;
    @BindView(R.id.llTop)
    LinearLayout  linearlayout;
    @BindView(R.id.place_linear)
    LinearLayout  locationselection;

    View view;
    public FragmentSearch() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search
                , container, false);
        ButterKnife.bind(this, rootView);
        linearlayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {

               // hideSoftKeyboard(getActivity());
                return false;
            }
        });
        return rootView;
    }
// buuton click for search users
    @NonNull
    @OnClick(R.id.butsearch)
    public void serachUsers()
    {

        String keyword = InputSearch.getText().toString().trim();
        String skills = simpleMultiAutoCompleteTextView.getText().toString().trim();
        String location = InputLocation.getText().toString().trim();
        if (keyword.isEmpty() && skills.isEmpty() && location.isEmpty()) {
            Toast.makeText(getContext(), "Please provide any search condition", Toast.LENGTH_LONG).show();
        } else {
            searchUser(keyword, skills, location);
        }
    }



// seraching the bawabba freelancer users

    private void searchUser(final String keyword, final String skills, final String location) {

        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(getContext()).getApiService().serachUser("app-client",
                "123321", keyword, skills, location);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jObj = new JSONObject(responseString);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {
                            SearchResultResponse resultResponse = new Gson().fromJson(responseString, SearchResultResponse.class);
                            ObjectFactory.getInstance().getAppPreference(getContext()).setSearchResult(responseString);
                            JSONArray user = jObj.getJSONArray("user");

                            Intent to_searchresult = new Intent(getContext(), SearchResult.class);
                            to_searchresult.putExtra("search_result", user.toString());
                            startActivity(to_searchresult);

                        } else {
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
    @OnClick(R.id.tvClear)
    public void clearSearch()
    {
        clear_search();
    }

    @OnClick(R.id.textskill)
    public void searchSkills()
    {


     //    need to change the fragment
        Intent i = new Intent(getContext(), ActivitySearchSetSkills.class);
        startActivityForResult(i, 2);
    }

    @OnClick(R.id.place_linear)
    public void locationChoose()
    {
       findPlace(view);
    }



    // method to clear all the serach fields

    private void clear_search() {

        InputSearch.setText("");
        InputLocation.setText("");
        simpleMultiAutoCompleteTextView.setText("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
                InputLocation.setText(place.getAddress());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }else if (requestCode==2){
            if (data==null){
                simpleMultiAutoCompleteTextView.setText("");

                return;
            }
            Bundle MBuddle = data.getExtras();
            String MMessage = MBuddle .getString("result");
            if (MMessage!=null){
                simpleMultiAutoCompleteTextView.setText(MMessage);
            }else{
                simpleMultiAutoCompleteTextView.setText("");

            }



        }
    }
//  google places
    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(getActivity());
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }
  // hiding the keu board when the user touch outside
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}
