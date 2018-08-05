
package com.bawaaba.rninja4.rookie.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PortfolioVideo {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("title")
    @Expose
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
