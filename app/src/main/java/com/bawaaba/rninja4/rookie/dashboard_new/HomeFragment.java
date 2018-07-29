package com.bawaaba.rninja4.rookie.dashboard_new;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.Data;
import com.bawaaba.rninja4.rookie.MainActivity;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.SearchResult;
import com.bawaaba.rninja4.rookie.activity.Subcategory;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.ItemClickListener_grid;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.searchResult.SearchResultResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_dashboard_home,container,false);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        tvSearchHere = (AppCompatEditText) v.findViewById(R.id.tvSearchHere);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        setListeners();

        return v;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        tvSearchHere.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEND) {
                    Utilities.hideKeyBoard(getActivity());
                    ((BaseBottomHelperActivity)getActivity()).searchUser(tvSearchHere.getText().toString().trim(), "", "");
                    return true;
                }
                return false;
            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utilities.hideKeyBoard(getActivity());
                return false;
            }
        });

        homeAdapter=new HomeAdapter(Data.getdata(), new ItemClickListener_grid() {
            @Override
            public void onItemClick(int pos) {
                ((BaseBottomHelperActivity)getActivity()).changeFragment(SubcategoryFragment.newInstance(pos),false);
//                Intent to_subcategory = new Intent(getContext(), Subcategory.class);
//                to_subcategory.putExtra("id", pos);
//                startActivity(to_subcategory);
            }
        });
        recyclerView.setAdapter(homeAdapter);

    }






    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private AppCompatEditText tvSearchHere;
    private HomeAdapter homeAdapter;


}
