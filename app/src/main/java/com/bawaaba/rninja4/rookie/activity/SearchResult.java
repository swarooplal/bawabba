package com.bawaaba.rninja4.rookie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.bawaaba.rninja4.rookie.MainActivity;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.dashboard_new.ChatFragment;
import com.bawaaba.rninja4.rookie.dashboard_new.ProfileViewFragment;
import com.bawaaba.rninja4.rookie.dashboard_new.SearchFragment;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.searchResult.SearchResultResponse;
import com.bawaaba.rninja4.rookie.model.searchResult.User;
import com.bawaaba.rninja4.rookie.utils.AppPreference;
import com.bawaaba.rninja4.rookie.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SearchResult extends AppCompatActivity implements View.OnClickListener {
        private static final String TAG = SearchResult.class.getSimpleName();

        ArrayList<User> users = new ArrayList<>();
        SearchResultResponse resultResponse = new SearchResultResponse();
        List<User> userOriginal = new ArrayList<>();
        List<User> userRatingBasedSorted = new ArrayList<>();
        List<User> userReviewBasedSorted = new ArrayList<>();
        private RecyclerView mProfiledetails;
        private AdapterSearchResult mAdapter;
        private Button ratings;
        private Button review;
        private ImageView profile1;
        private LinearLayoutManager mLayoutManager;
        private String response = "";
        private AppCompatTextView tvReviews;
        private AppCompatTextView tvRatings;
        private TextBadgeItem textBadgeItem;
        private SQLiteHandler db;
        private SessionManager session;

    public void isMessageArrived() {
        try {
            boolean isMessageArrived = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).isNewMessageArrived();
            if (isMessageArrived) {
                //showUnreadMessages();
                int total = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUnreadMessage();
                if (total > 0) {
                    textBadgeItem.setText("" + total);
                    textBadgeItem.show(false);
                } else {
                    hideText();
                }
            } else {
                hideText();
            }
        } catch (Exception e) {
        }
    }

    private void hideText(){
        textBadgeItem.setText("");
        textBadgeItem.hide();
    }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search_result);

            db = new SQLiteHandler(getApplicationContext());
            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = db.getUserDetails();
            final String db_id = user.get("uid");

//            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//            getSupportActionBar().setCustomView(R.layout.actiontitle_layout25);
//            ActionBar actionBar = getSupportActionBar();
            getSupportActionBar().hide();

            intiViews();
            onClickListners();

          //  actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
            profile1 = (ImageView) findViewById(R.id.img_profile);

            ratings = (Button) findViewById(R.id.ratings);
            review = (Button) findViewById(R.id.review);

            List<SearchResultData> data = new ArrayList<>();


            BottomNavigationBar bottomNavigationView = (BottomNavigationBar)
                    findViewById(R.id.bottom_bar);

            textBadgeItem = Utils.getTextBadge();
            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveCurrentActivity("SearchResult");
            isMessageArrived();
            isMessageArrived();
            bottomNavigationView
                    .addItem(new BottomNavigationItem(R.drawable.ic_home1, "Home").setActiveColorResource(R.color.bottomnavigation))
                    .addItem(new BottomNavigationItem(R.drawable.ic_search1, "Search").setActiveColorResource(R.color.bottomnavigation))
                    .addItem(new BottomNavigationItem(R.drawable.ic_inbox1, "Inbox").setBadgeItem(textBadgeItem).setActiveColorResource(R.color.bottomnavigation))
                    .addItem(new BottomNavigationItem(R.drawable.ic_profile, "Profile").setActiveColorResource(R.color.bottomnavigation))
                    .setFirstSelectedPosition(1)
                    .initialise();
            isMessageArrived();
            bottomNavigationView.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
                @Override
                public void onTabSelected(int position) {
                    switch (position) {
                        case 0:
                            BaseBottomHelperActivity.start(getApplicationContext(),null,null,null);
                            /*Intent to_main = new Intent(SearchResult.this, MainActivity.class);
                            startActivity(to_main);*/
//                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_left);
                            finish();
                            break;
                        case 1:
                            BaseBottomHelperActivity.start(getApplicationContext(), SearchFragment.class.getName(),null,null);
                           /* Intent to_search = new Intent(SearchResult.this, SearchActivity.class);
                            startActivity(to_search);*/
//                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_left);
                            finish();
                            break;
                        case 2:
                            if (session.isLoggedIn() && db_id != null) {
                                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveNewMessageArrived(false);
                                BaseBottomHelperActivity.start(getApplicationContext(), ChatFragment.class.getName(),null,null);
                                /*Intent to_inbox = new Intent(SearchResult.this, com.bawaaba.rninja4.rookie.activity.ChatFunction.ChatActivity.class);
                                startActivity(to_inbox);*/
                                finish();
                            } else {
                                Intent to_login = new Intent(SearchResult.this, LoginActivity.class);
                                startActivity(to_login);
                                finish();
                            }
//                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_left);
                            finish();
                            break;
                        case 3:
                            AppPreference appPreference=ObjectFactory.getInstance().getAppPreference(getApplicationContext());
                            BaseBottomHelperActivity.start(getApplicationContext(), ProfileViewFragment.class.getName(),appPreference.getUserId(),appPreference.getUserName());
                            /*Intent to_profile = new Intent(SearchResult.this, ProfileView.class);
                            startActivity(to_profile);*/
//                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_left);

                            finish();
                            break;
                        default:
                            break;

                    }
                }

                @Override
                public void onTabUnselected(int position) {

                }

                @Override
                public void onTabReselected(int position) {

                }
            });




            Intent from_searchactivity = getIntent();
            final String search_result = from_searchactivity.getStringExtra("search_result");

            Log.e(TAG, "searching....: " + search_result);


            SearchResult.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent toadapter_search = new Intent(SearchResult.this, AdapterSearchResult.class);
                    toadapter_search.putExtra("search_result", search_result);
                }
            });

            try {
                response = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getSearchResult();
                SearchResultResponse resultResponseOriginal = new Gson().fromJson(response, SearchResultResponse.class);
                resultResponse = new Gson().fromJson(response, SearchResultResponse.class);
                mProfiledetails = (RecyclerView) findViewById(R.id.profile_details);
                mAdapter = new AdapterSearchResult(SearchResult.this, resultResponseOriginal.getUser());
                mProfiledetails.setLayoutManager(mLayoutManager);
                mProfiledetails.addItemDecoration(new SimpleDividerItemDecoration(this));
                mProfiledetails.setAdapter(mAdapter);
                mProfiledetails.setLayoutManager(new LinearLayoutManager(SearchResult.this));

                for (int i = 0; i < resultResponse.getUser().size(); i++) {
                    if (TextUtils.isEmpty(resultResponse.getUser().get(i).getRating())) {
                        resultResponse.getUser().get(i).setRating("0");
                    }
                }
                userOriginal = resultResponse.getUser();


            } catch (Exception e) {
                e.printStackTrace();
            }
            ratings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mAdapter = new AdapterSearchResult(SearchResult.this, userOriginal);
                    mProfiledetails.setLayoutManager(mLayoutManager);
                    mProfiledetails.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            });

        }

//            for (int i = 0; i < search_data.length(); i++) {
//                JSONObject object = search_data.getJSONObject(i);
//                SearchResultData Profile_data = new SearchResultData();
//                Profile_data.profileImage = object.getString("profile_img");
//                Profile_data.profileName = object.getString("current_name");
//                Profile_data.biography = object.getString("experience");
//                Profile_data.skillName = object.getString("skills");
//                Profile_data.reg_id = object.getString("registration");
//                Profile_data.Profile_rating = object.getString("rating");
//                Profile_data.rating_count = object.getString("rating");
//                Profile_data.review_count = object.getString("review_count");
//                data.add(Profile_data);
//            }

        private void onClickListners() {
            tvReviews.setOnClickListener(this);
            tvRatings.setOnClickListener(this);
        }

        private void intiViews() {
            tvReviews = (AppCompatTextView) findViewById(R.id.tvReviews);
            tvRatings = (AppCompatTextView) findViewById(R.id.tvRatings);
        }

        @Override
        public void onClick(View v) {

            int id = v.getId();
            switch (id) {

                case R.id.tvReviews:

                    tvReviews.setBackground(getResources().getDrawable(R.drawable.search_result_box));
                    tvRatings.setBackground(getResources().getDrawable(R.drawable.search_result_box2));
                    tvReviews.setTextColor(getResources().getColor(R.color.white));
                    tvRatings.setTextColor(getResources().getColor(R.color.portfolioText));
                    break;
                case R.id.tvRatings:
                    tvRatings.setBackground(getResources().getDrawable(R.drawable.search_result_box2));
                    tvReviews.setBackground(getResources().getDrawable(R.drawable.search_result_box));
                    tvRatings.setTextColor(getResources().getColor(R.color.portfolioText));
                    tvReviews.setTextColor(getResources().getColor(R.color.white));

                    break;
                default:
                    break;
            }
        }

    }
