package com.bawaaba.rninja4.rookie.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.Edit_Audio_Data;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.Image;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.PortfolioVideoData;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rninja4 on 11/1/17.
 */

public class AppPreference implements IConsts{

    public static final String LOGIN_TOKEN = "LOGIN_TOKEN";
    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PROFILE_IMG= "USER_PROFILE_IMG";
    public static final String USER_SHORT_IMG = "SHORT_PROFILE_IMG";
    public static final String USER_LOGGEDIN = "USER_LOGGEDIN";
    public static final String CURRENT_ACTIVITY= "CURRENT_ACTIVITY";
    public static final String CHAT_NEW_MESSAGE_ARRIVED= "CHAT_NEW_MESSAGE_ARRIVED";
    public static final String UNREAD_MSG= "UNREAD_MSG";
    private SharedPreferences mSharedPreferences;
    private ArrayList<Image> images = new ArrayList<>();
    private int imagesCurrentPosition = 0;
    private List<PortfolioVideoData> videosData = new ArrayList<>();
    private List<Edit_Audio_Data> audioData = new ArrayList<>();
    private String profileImg = "";
    private String profileResponse = "";
    private String searchResult = "";
    private String reviewResult = "";
    private String review = "";
    private String description = "";
    private String reviewImage = "";
    private String inboxResponse = "";
    public AppPreference(Context context) {
        super();
        if (context != null) {
            mSharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        }
    }

    public void updateContext(Context context) {
        if (context != null) {
            mSharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        }
    }

    public void saveLoginToken(String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LOGIN_TOKEN, value);
        editor.apply();
    }

    public String getLoginToken() {
        return mSharedPreferences.getString(LOGIN_TOKEN, "");
    }

    public void saveUserId(String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_ID, value);
        editor.apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(USER_ID, "");
    }

    public QBUser getCurrentUser(){
        String login = getUserId();
        QBUser qbUser = new QBUser(login, QB_PASSWORD);
        return qbUser;
    }

    public void setImagesArray(ArrayList<Image> images) {
        this.images = images;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public int getImagesCurrentPosition() {
        return imagesCurrentPosition;
    }

    public void setImagesCurrentPosition(int position) {
        this.imagesCurrentPosition = position;
    }

    public void setVideos(List<PortfolioVideoData> data) {
        this.videosData = data;
    }

    public List<PortfolioVideoData> getVideosData() {
        return videosData;
    }

    public List<Edit_Audio_Data> getAudioData() {
        return this.audioData;
    }

    public void setAudioData(List<Edit_Audio_Data> data) {
        this.audioData = data;
    }

    public String getProfileResponse() {
        return this.profileResponse;
    }

    public void setProfileResponse(String response) {
        this.profileResponse = response;
    }

    public String getInboxResponse() {
        return this.inboxResponse;
    }

    public void setInboxResponse(String response) {
        this.inboxResponse = response;
    }

    public void saveUserName(String name) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_NAME, name);
        editor.apply();
    }

    public String getUserName() {
        return mSharedPreferences.getString(USER_NAME, "");
    }

    public String getSearchResult() {
        return this.searchResult;
    }

    public void setSearchResult(String response) {
        this.searchResult = response;
    }

//    public String getReview() {
//        return this.review;
//    }
//    public void setReview(String response) {
//        this.review = response;
//    }

    public String getReviewImage() {
        return this.reviewImage;
    }
    public void setReviewImage(String response) {
        this.reviewImage = response;
    }

    public String get() {
        return this.review;
    }

    public String getReviewResult() {
        return this.reviewResult;
    }
    public void setReviewResult(String response) {
        this.reviewResult = response;
    }

    public boolean isLoggedIn() {
        return mSharedPreferences.getBoolean(USER_LOGGEDIN, false);
    }

    public void saveLogin(boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(USER_LOGGEDIN, value);
        editor.apply();
    }

    public boolean isNewMessageArrived() {
        return mSharedPreferences.getBoolean(CHAT_NEW_MESSAGE_ARRIVED, false);
    }

    public void saveNewMessageArrived(boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(CHAT_NEW_MESSAGE_ARRIVED, value);
        editor.apply();
    }

    public int getUnreadMessage() {
        return mSharedPreferences.getInt(UNREAD_MSG, 0);
    }

    public void saveUnreadMessage(int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(UNREAD_MSG, value);
        editor.apply();
    }

    public String getCurrentActivity() {
        return mSharedPreferences.getString(CURRENT_ACTIVITY, "");
    }

    public void saveCurrentActivity(String name) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CURRENT_ACTIVITY, name);
        editor.apply();
    }

    public String getEmail() {
        return mSharedPreferences.getString(USER_EMAIL, "");
    }

    public void setEmail(String name) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_EMAIL, name);
        editor.apply();
    }

    public String getProfileImg() {
        return mSharedPreferences.getString(USER_PROFILE_IMG, "");
    }

    public void saveProfileImg(String img) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_PROFILE_IMG, img);
        editor.apply();
    }

    public String getShortProfileImg() {
        return mSharedPreferences.getString(USER_SHORT_IMG, "");
    }

    public void saveShortProfileImg(String img) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_SHORT_IMG, img);
        editor.apply();
    }



    public String getDescription(){
        return this.description;

    }

    public void setDescription(String trim){
        this.description=trim;
    }

    public void clearAll(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
