package com.bawaaba.rninja4.rookie.model.searchResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by rninja4 on 11/14/17.
 */

public class User {

    @SerializedName("registration")
    @Expose
    private String registration;
    @SerializedName("current_name")
    @Expose
    private String currentName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("experience")
    @Expose
    private String experience;
    @SerializedName("profile_img")
    @Expose
    private String profileImg;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("review_count")
    @Expose
    private String reviewCount;
    @SerializedName("skills")
    @Expose
    private List<String> skills = null;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("verify")
    @Expose
    private String verify;

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}


