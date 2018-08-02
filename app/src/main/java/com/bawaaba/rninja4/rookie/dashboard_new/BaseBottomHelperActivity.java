package com.bawaaba.rninja4.rookie.dashboard_new;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.ChatActivity;
import com.bawaaba.rninja4.rookie.activity.LoginActivity;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.activity.SearchActivity;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.MessageEvent;
import com.bawaaba.rninja4.rookie.model.searchResult.SearchResultResponse;
import com.bawaaba.rninja4.rookie.utils.AppPreference;
import com.bawaaba.rninja4.rookie.utils.Utils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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

public class BaseBottomHelperActivity extends AppCompatActivity {


    public static void start(Context ctx, String className, String profileId, String name) {
        ctx.startActivity(new Intent(ctx.getApplicationContext(), BaseBottomHelperActivity.class)
                .putExtra(EXTRA_CLASS_NAME, className)
                .putExtra(EXTRA_PROFILE_NAME, name)
                .putExtra(EXTRA_PROFILE_ID, profileId).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_base_bottom_menu);
        progressDialog = Utils.showLoadingDialog(this);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
        }
        EventBus.getDefault().register(this);
        fetchUnreadNotification();

        bottomMenuHelper = new BaseBottomMenuHelper(this, 0);

        String className = getIntent().getStringExtra(EXTRA_CLASS_NAME);
        String profileName = getIntent().getStringExtra(EXTRA_PROFILE_NAME);
        String profileId = getIntent().getStringExtra(EXTRA_PROFILE_ID);
        if (SearchFragment.class.getName().equals(className)) {

            changeFragment(new SearchFragment(), false);
            bottomMenuHelper.changePosition(1);
        } else if (ChatFragment.class.getName().equals(className)) {

            changeFragment(new ChatFragment(), false);
            bottomMenuHelper.changePosition(2);
        } else if (ProfileViewFragment.class.getName().equals(className)) {
            AppPreference appPreference=ObjectFactory.getInstance().getAppPreference(getApplicationContext());
            boolean isOwnProfile=appPreference.getUserId()!=null && appPreference.getUserId().equals(profileId);
            changeFragment(ProfileViewFragment.newInstance(profileId, profileName), false);
            bottomMenuHelper.changePosition(isOwnProfile?3:0);
        } else {

            changeFragment(new HomeFragment(), false);
            bottomMenuHelper.changePosition(0);
        }


    }

    @Override
    public void onBackPressed() {
        if(isBackPressProcessing)
            return;
        isBackPressProcessing=true;
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            int bottomMenuCurrentPosition = bottomMenuHelper.getSelectedPosition();
            HashMap<String, String> user = new SQLiteHandler(getApplicationContext()).getUserDetails();
            String db_id = user.get("uid");
            boolean isLoggedIn = new SessionManager(getApplicationContext()).isLoggedIn() && db_id != null;
            while (backstackPositions.size() > 0) {
                int i = backstackPositions.get(backstackPositions.size() - 1);
                if (i == bottomMenuCurrentPosition || (!isLoggedIn && (i == 2 || i == 3))) {
                    backstackPositions.remove(backstackPositions.size() - 1);
                    continue;
                }
                int position = backstackPositions.get(backstackPositions.size() - 1);
                backstackPositions.remove(backstackPositions.size() - 1);
                switch (position) {
                    case 1:
                        changeFragment(new SearchFragment(), true, true);
                        bottomMenuHelper.changePosition(1);
                        isBackPressProcessing=false;
                        return;
                    case 2:
                        changeFragment(new ChatFragment(), true, true);
                        bottomMenuHelper.changePosition(2);
                        isBackPressProcessing=false;
                        return;
                    case 3:
                        AppPreference appPreference=ObjectFactory.getInstance().getAppPreference(getApplicationContext());
                        changeFragment(ProfileViewFragment.newInstance(appPreference.getUserId(),appPreference.getUserName()), true,true);
                        bottomMenuHelper.changePosition(3);
                        isBackPressProcessing=false;
                        return;
                    default:
                        changeFragment(new HomeFragment(), true, true);
                        bottomMenuHelper.changePosition(0);
                        isBackPressProcessing=false;
                        return;
                }
            }
            if (bottomMenuHelper.getSelectedPosition() != 0) {
                bottomMenuHelper.changePosition(0);
                changeFragment(new HomeFragment(), true);
                isBackPressProcessing=false;
            } else {
                finish();
            }
        } else {
            super.onBackPressed();
            isBackPressProcessing=false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bottomMenuHelper.unbind();
        EventBus.getDefault().unregister(this);
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Ac", "=" + requestCode);
        lastActivityResultData = data;
        lastActivityResultRequestCode = requestCode;
        lastActivityResultResultCode = resultCode;
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (lastActivityResultRequestCode > 0)
            bottomMenuHelper.onActivityResult(lastActivityResultRequestCode, lastActivityResultResultCode, lastActivityResultData);
        lastActivityResultData = null;
        lastActivityResultRequestCode = 0;
        lastActivityResultResultCode = 0;

    }

    private void fetchUnreadNotification() {

        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(getApplicationContext()).getApiService().notification("app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId());

        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = null;
                            Log.d("read_responseString", responseString.toString());
                            try {
                                jsonObject = new JSONObject(responseString);
                                String count = jsonObject.getString("count");
                                if (!count.equals("0")) {
                                    bottomMenuHelper.getTextBadgeItem().setText(count);
                                    bottomMenuHelper.getTextBadgeItem().show(false);
                                }
                            } catch (Exception e) {
                            }

                        }

                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }


    public void searchUser(final String keyword, final String skills, final String location) {

        try {
            showProgress("Fetching details", false);
            Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(getApplicationContext()).getApiService().serachUser("app-client",
                    "123321", keyword, skills, location);

            responseBodyCall.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if (response.body() != null) {
                        try {
                            String responseString = new String(response.body().bytes());
                            Log.e("Res", "=" + responseString);
                            JSONObject jObj = new JSONObject(responseString);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                SearchResultResponse resultResponse = new Gson().fromJson(responseString, SearchResultResponse.class);
                                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).setSearchResult(responseString);
                                JSONArray user = jObj.getJSONArray("user");

                                changeFragment(SearchResultFragment.newInstance(user.toString()), false);
//
//                                Intent to_searchresult = new Intent(getContext(), SearchResult.class);
//                                to_searchresult.putExtra("search_result", user.toString());
//                                startActivity(to_searchresult);

                            } else {
                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e("err", "=", e);
                            try {
                                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e1) {
                            }
                        }

                    }
                    dismissProgress();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
                    } catch (Exception e1) {
                    }
                    dismissProgress();
                }
            });
        } catch (Exception ignored) {
            Log.e("ex", "=", ignored);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (bottomMenuHelper != null)
            bottomMenuHelper.isMessageArrived();
    }

    /**
     * Clear all fragments from the memory
     */
    public void clearBackStack(int uptoKeep) {
        try {

            int upto = uptoKeep < 0 ? 0 : uptoKeep;
            FragmentManager manager = getSupportFragmentManager();
//            while (manager.getBackStackEntryCount() > upto) {
//                manager.popBackStackImmediate();
//            }
            while (upto < manager.getBackStackEntryCount()) {
                manager.popBackStackImmediate(manager.getBackStackEntryAt(upto).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } catch (Exception e) {
        }
    }




    /**
     * Please do not call {@link BaseBottomMenuHelper#changePosition(int)} before calling this  Otherwise backstack will not work properly
     * @param fragment
     * @param clearBackStack
     * @param isFromBackStack to prevent adding previous position to backstack
     */
    public void changeFragment(@NonNull Fragment fragment, boolean clearBackStack, boolean isFromBackStack) {
        Utilities.hideKeyBoard(this);
        int previousBottomMenuPosition = bottomMenuHelper.getSelectedPosition();
        if (clearBackStack) {
            clearBackStack(0);
        }
        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, fragment).addToBackStack(fragment.getClass().getName()).commit();

        } catch (Exception e) {
        }
        if (!isFromBackStack && previousBottomMenuPosition > -1 && (backstackPositions.isEmpty() || backstackPositions.get(backstackPositions.size() - 1) != previousBottomMenuPosition)) {
            if (backstackPositions.size() >= bottomMenuHelper.getTabCount()) {
                backstackPositions.remove(0);
            }
            backstackPositions.add(bottomMenuHelper.getSelectedPosition());
        }
    }

    public void changeFragment(@NonNull Fragment fragment, boolean clearBackStack) {
        changeFragment(fragment, clearBackStack, false);
    }

    public void showProgress(@NonNull String msg, boolean cancelable) {
        progressDialog.setCancelable(cancelable);
//        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void dismissProgress() {
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
        }
    }

    public void showSnackBar(@NonNull String msg, @ColorInt int textColor) {
        try {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(textColor);
            snackbar.show();
        } catch (Exception e) {
        }
    }

    public void setTabPosition(int position) {
        bottomMenuHelper.changePosition(position);
    }

    private BaseBottomMenuHelper bottomMenuHelper;
    private Dialog progressDialog;
    private Intent lastActivityResultData;
    private int lastActivityResultRequestCode;
    private int lastActivityResultResultCode;
    private boolean isBackPressProcessing;
    private List<Integer> backstackPositions = new ArrayList<>();
    private static final String EXTRA_CLASS_NAME = "extra_class_name";
    private static final String EXTRA_PROFILE_ID = "extra_profile_id";
    private static final String EXTRA_PROFILE_NAME = "extra_profile_name";
}
