package com.bawaaba.rninja4.rookie.model.searchResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rninja4 on 11/14/17.
 */

public class SearchResultResponse {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("user")
    @Expose
    private List<User> user = null;
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
