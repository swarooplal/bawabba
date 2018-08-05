package com.bawaaba.rninja4.rookie.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rninja4 on 11/1/17.
 */

public class Socialmedia {

    @SerializedName("500Px")
    @Expose
    private Object _500Px;
    @SerializedName("behance")
    @Expose
    private Object behance;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("dribbble")
    @Expose
    private Object dribbble;
    @SerializedName("github")
    @Expose
    private Object github;
    @SerializedName("bitbucket")
    @Expose
    private Object bitbucket;
    @SerializedName("googleplus")
    @Expose
    private Object googleplus;
    @SerializedName("instagram")
    @Expose
    private String instagram;
    @SerializedName("linkedin")
    @Expose
    private String linkedin;
    @SerializedName("pinterest")
    @Expose
    private Object pinterest;
    @SerializedName("soundcloud")
    @Expose
    private Object soundcloud;
    @SerializedName("stack")
    @Expose
    private Object stack;
    @SerializedName("twitter")
    @Expose
    private String twitter;
    @SerializedName("vimeo")
    @Expose
    private Object vimeo;
    @SerializedName("youtube")
    @Expose
    private Object youtube;
    @SerializedName("otherurl")
    @Expose
    private Object otherurl;
    @SerializedName("website")
    @Expose
    private String website;

    public Object get500Px() {
        return _500Px;
    }

    public void set500Px(Object _500Px) {
        this._500Px = _500Px;
    }

    public Object getBehance() {
        return behance;
    }

    public void setBehance(Object behance) {
        this.behance = behance;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public Object getDribbble() {
        return dribbble;
    }

    public void setDribbble(Object dribbble) {
        this.dribbble = dribbble;
    }

    public Object getGithub() {
        return github;
    }

    public void setGithub(Object github) {
        this.github = github;
    }

    public Object getBitbucket() {
        return bitbucket;
    }

    public void setBitbucket(Object bitbucket) {
        this.bitbucket = bitbucket;
    }

    public Object getGoogleplus() {
        return googleplus;
    }

    public void setGoogleplus(Object googleplus) {
        this.googleplus = googleplus;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public Object getPinterest() {
        return pinterest;
    }

    public void setPinterest(Object pinterest) {
        this.pinterest = pinterest;
    }

    public Object getSoundcloud() {
        return soundcloud;
    }

    public void setSoundcloud(Object soundcloud) {
        this.soundcloud = soundcloud;
    }

    public Object getStack() {
        return stack;
    }

    public void setStack(Object stack) {
        this.stack = stack;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public Object getVimeo() {
        return vimeo;
    }

    public void setVimeo(Object vimeo) {
        this.vimeo = vimeo;
    }

    public Object getYoutube() {
        return youtube;
    }

    public void setYoutube(Object youtube) {
        this.youtube = youtube;
    }

    public Object getOtherurl() {
        return otherurl;
    }

    public void setOtherurl(Object otherurl) {
        this.otherurl = otherurl;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

}
