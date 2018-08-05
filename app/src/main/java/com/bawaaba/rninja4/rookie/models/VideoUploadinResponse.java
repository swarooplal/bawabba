
package com.bawaaba.rninja4.rookie.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoUploadinResponse {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("portfolio_video")
    @Expose
    private List<PortfolioVideo> portfolioVideo = null;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PortfolioVideo> getPortfolioVideo() {
        return portfolioVideo;
    }

    public void setPortfolioVideo(List<PortfolioVideo> portfolioVideo) {
        this.portfolioVideo = portfolioVideo;
    }



}
