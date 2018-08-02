package com.bawaaba.rninja4.rookie.dashboard_new;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.AdapterSearchResult;
import com.bawaaba.rninja4.rookie.activity.SearchResult;
import com.bawaaba.rninja4.rookie.activity.SimpleDividerItemDecoration;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.searchResult.SearchResultResponse;
import com.bawaaba.rninja4.rookie.model.searchResult.User;
import com.google.gson.Gson;

import java.util.List;

public class SearchResultFragment extends Fragment implements View.OnClickListener {

    public static SearchResultFragment newInstance(String searchResult) {

        Bundle args = new Bundle();
        args.putString("searchResult",searchResult);
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_search_result,container,false);
        initViews(v);
        init();
        return v;
    }

    private void initViews(View v) {
        ratings = (Button) v.findViewById(R.id.ratings);
        ratings = (Button) v.findViewById(R.id.ratings);
        tvReviews = (AppCompatTextView) v.findViewById(R.id.tvReviews);
        tvRatings = (AppCompatTextView) v.findViewById(R.id.tvRatings);
        mProfiledetails = (RecyclerView) v.findViewById(R.id.profile_details);
        mProfiledetails.setLayoutManager(new LinearLayoutManager(getContext()));
        mProfiledetails.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        tvReviews.setOnClickListener(this);
        tvRatings.setOnClickListener(this);
        ratings.setOnClickListener(this);


    }

    private void init() {
        try {
            String response = ObjectFactory.getInstance().getAppPreference(getContext()).getSearchResult();
            SearchResultResponse resultResponseOriginal = new Gson().fromJson(response, SearchResultResponse.class);
            SearchResultResponse resultResponse = new Gson().fromJson(response, SearchResultResponse.class);
            adapter=new SearchResultAdapter(resultResponseOriginal.getUser());
            mProfiledetails.setAdapter(adapter);

//            mAdapter = new AdapterSearchResult(SearchResult.this, resultResponseOriginal.getUser());

//            mProfiledetails.setAdapter(mAdapter);

            for (int i = 0; i < resultResponse.getUser().size(); i++) {
                if (TextUtils.isEmpty(resultResponse.getUser().get(i).getRating())) {
                    resultResponse.getUser().get(i).setRating("0");
                }
            }
            userOriginal = resultResponse.getUser();


        } catch (Exception e) {
        }
    }

    private void setSelection(boolean isReviewSelected) {
        tvReviews.setBackground(getResources().getDrawable(R.drawable.search_result_box));
        tvRatings.setBackground(getResources().getDrawable(R.drawable.search_result_box2));
        tvReviews.setTextColor(getResources().getColor(R.color.white));
        tvRatings.setTextColor(getResources().getColor(R.color.portfolioText));
    }



    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.ratings:
                adapter.setList(userOriginal);
                break;
                default:setSelection(view.getId()==R.id.tvReviews);

            }
        } catch (Exception ignored){}

    }

    private AppCompatTextView tvReviews;
    private AppCompatTextView tvRatings;
    private Button ratings;
    private Button review;
    private RecyclerView mProfiledetails;

    private List<User> userOriginal;
    private SearchResultAdapter adapter;


}
