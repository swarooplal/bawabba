package com.bawaaba.rninja4.rookie.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserData {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("profile_img")
    @Expose
    private String profileImg;
    @SerializedName("profile_url")
    @Expose
    private String profileUrl;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("verify")
    @Expose
    private String verify;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("skills")
    @Expose
    private List<String> skills = null;
    @SerializedName("language")
    @Expose
    private List<Language> language = null;
    @SerializedName("services")
    @Expose
    private List<Service> services = null;

    @SerializedName("review_image")
    @Expose
    private String reviewImage;

    @SerializedName("socialmedia")
    @Expose
    private Socialmedia socialmedia;
    @SerializedName("portfolio_image")
    @Expose
    private List<PortfolioImage> portfolioImage = null;
    @SerializedName("portfolio_video")
    @Expose
    private List<PortfolioVideo> portfolioVideo = null;
    @SerializedName("portfolio_audio")
    @Expose
    private List<PortfolioAudio> portfolioAudio = null;
    @SerializedName("portfolio_doc")
    @Expose
    private List<PortfolioDoc> portfolioDoc = null;
    @SerializedName("portfolio_link")
    @Expose
    private List<PortfolioLink> portfolioLink = null;
    @SerializedName("review")
    @Expose
    private List<Review> review = null;
    @SerializedName("review_by_me")
    @Expose
    private List<ReviewByMe> reviewByMe = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<Language> getLanguage() {
        return language;
    }

    public void setLanguage(List<Language> language) {
        this.language = language;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public Socialmedia getSocialmedia() {
        return socialmedia;
    }

    public void setSocialmedia(Socialmedia socialmedia) {
        this.socialmedia = socialmedia;
    }

    public List<PortfolioImage> getPortfolioImage() {
        return portfolioImage;
    }

    public void setPortfolioImage(List<PortfolioImage> portfolioImage) {
        this.portfolioImage = portfolioImage;
    }

    public List<PortfolioVideo> getPortfolioVideo() {
        return portfolioVideo;
    }

    public void setPortfolioVideo(List<PortfolioVideo> portfolioVideo) {
        this.portfolioVideo = portfolioVideo;
    }

    public List<PortfolioAudio> getPortfolioAudio() {
        return portfolioAudio;
    }

    public void setPortfolioAudio(List<PortfolioAudio> portfolioAudio) {
        this.portfolioAudio = portfolioAudio;
    }

    public List<PortfolioDoc> getPortfolioDoc() {
        return portfolioDoc;
    }

    public void setPortfolioDoc(List<PortfolioDoc> portfolioDoc) {
        this.portfolioDoc = portfolioDoc;
    }

    public List<PortfolioLink> getPortfolioLink() {
        return portfolioLink;
    }

    public void setPortfolioLink(List<PortfolioLink> portfolioLink) {
        this.portfolioLink = portfolioLink;
    }


    public List<Review> getReview() {
        return review;
    }

    public String getReviewImage() {
        return reviewImage;
    }

    public void setRevieImage(String reviewImage) {
        this.reviewImage = reviewImage;
    }

    public void setReview(List<Review> review) {
        this.review = review;
    }

    public List<ReviewByMe> getReviewByMe() {
        return reviewByMe;
    }

    public void setReviewByMe(List<ReviewByMe> reviewByMe) {
        this.reviewByMe = reviewByMe;
    }


}
