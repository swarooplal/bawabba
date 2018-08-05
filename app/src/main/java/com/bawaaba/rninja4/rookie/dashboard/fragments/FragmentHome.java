package com.bawaaba.rninja4.rookie.dashboard.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.Data;
import com.bawaaba.rninja4.rookie.MycustomAdapter;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.SearchResult;
import com.bawaaba.rninja4.rookie.dashboard.recyclerview.RecyclerItemClickListener;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.searchResult.SearchResultResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;



public class FragmentHome extends Fragment {


    private SQLiteHandler db;
    private SessionManager session;
    MycustomAdapter adapter;
    @BindView(R.id.tvSearchHere)
    AppCompatEditText tvSearchHere;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerViewMainCatogery;


    public FragmentHome() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);
        tvSearchHere.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchUser(tvSearchHere.getText().toString().trim(), "", "");
                    return true;

                }
                return false;
            }
        });
        adapter = new MycustomAdapter(getContext(), Data.getdata());
        recyclerViewMainCatogery.setAdapter(adapter);
        GridLayoutManager mgridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerViewMainCatogery.setLayoutManager(mgridLayoutManager);


        //  EventBus.getDefault().register(this);
        recyclerViewMainCatogery.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("111", "1");

//                Fragment fragmentC = new FragmentSubcategory();
//                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                transaction.add(R.id.child_fragment, fragmentC );
//                transaction.addToBackStack(null);
//                transaction.commit();
//                ((DashboardActivity) getActivity()).showFragment(new FragmentSubcategory());
            }
        }));
        return view;
    }

    //  serach the bawabba freelancer users
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


}

