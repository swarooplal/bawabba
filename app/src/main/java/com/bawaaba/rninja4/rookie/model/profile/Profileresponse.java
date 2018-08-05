package com.bawaaba.rninja4.rookie.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rninja4 on 11/1/17.
 */

public class Profileresponse {


    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("UserData")
    @Expose
    private UserData userData;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
