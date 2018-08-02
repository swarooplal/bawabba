package com.bawaaba.rninja4.rookie.dashboard_new;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ActivitySearchSetSkills;
import com.bawaaba.rninja4.rookie.activity.SearchActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private Button Search;
    private EditText InputSearch;
    private EditText InputSkill;
    private AppCompatTextView InputLocation;
    private TextView SearchResult;
    private AppCompatTextView tvClear;
    private ImageView image;
    private AutoCompleteTextView style;
    private MultiAutoCompleteTextView simpleMultiAutoCompleteTextView;
    private LinearLayout linearlayout;
    private View viewLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_search, container, false);
        initViews(v);
        setListeners();
        init();
        return v;
    }

    private void initViews(View v) {
        Search = (Button) v.findViewById(R.id.butsearch);
        InputSearch = (EditText) v.findViewById(R.id.textsearch);
        linearlayout = (LinearLayout) v.findViewById(R.id.llTop);
        viewLocation =  v.findViewById(R.id.place_linear);
        InputLocation = (AppCompatTextView) v.findViewById(R.id.locations);
        simpleMultiAutoCompleteTextView = (MultiAutoCompleteTextView) v.findViewById(R.id.textskill);
        tvClear = (AppCompatTextView) v.findViewById(R.id.tvClear);
    }

    private void setListeners() {
        tvClear.setOnClickListener(this);
        simpleMultiAutoCompleteTextView.setOnClickListener(this);


        linearlayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                Utilities.hideKeyBoard(getActivity());
                return false;
            }
        });

        Search.setOnClickListener(this);
        viewLocation.setOnClickListener(this);
    }

    private void init() {
    }



    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(getActivity());
            getActivity().startActivityFromFragment(this,intent, 1);
        } catch (Exception e) {
            Log.e("eee","",e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("search", "re=" + requestCode);
        try {
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
            } else if (requestCode == 2) {
                if (data == null) {
                    simpleMultiAutoCompleteTextView.setText("");

                    return;
                }
                Bundle MBuddle = data.getExtras();
                String MMessage = MBuddle.getString("result");
                if (MMessage != null) {
                    simpleMultiAutoCompleteTextView.setText(MMessage);
                } else {
                    simpleMultiAutoCompleteTextView.setText("");

                }

            }
        } catch (Exception ignored){}

    }

    private void clearSearch() {

        InputSearch.setText("");
        InputLocation.setText("");
        simpleMultiAutoCompleteTextView.setText("");
    }

    @Override
    public void onClick(View view) {
        try {
           Utilities.hideKeyBoard(getActivity());
            switch (view.getId()) {
                case R.id.textskill:
                    Intent i = new Intent(getContext(), ActivitySearchSetSkills.class);
                    getActivity().startActivityFromFragment(this, i, 2);
                    break;
                case R.id.butsearch:
                    String keyword = InputSearch.getText().toString().trim();
                    // String skills = InputSkill.getText().toString().trim();
                    String skills = simpleMultiAutoCompleteTextView.getText().toString().trim();
                    String location = InputLocation.getText().toString().trim();
                    if (keyword.isEmpty() && skills.isEmpty() && location.isEmpty()) {
                        Toast.makeText(getContext(), "Please provide any search condition", Toast.LENGTH_LONG).show();
                    } else {
                        ((BaseBottomHelperActivity) getActivity()).searchUser(keyword, skills, location);
                    }
                    break;
                case R.id.tvClear: clearSearch();
                    break;
                case R.id.place_linear:findPlace(view);
                break;

            }
        } catch (Exception ignored) {
        }

    }
}
