package com.bawaaba.rninja4.rookie.model;

/**
 * Created by rninja4 on 2/18/18.
 */

public class BlockModel {
    public String NameOfUser="";
    public String UserId="";
    public String Imagpath="";

    public String getImagpath() {
        return Imagpath;
    }

    public void setImagpath(String imagpath) {
        Imagpath = imagpath;
    }

    public String getNameOfUser() {
        return NameOfUser;
    }

    public void setNameOfUser(String nameOfUser) {
        NameOfUser = nameOfUser;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
