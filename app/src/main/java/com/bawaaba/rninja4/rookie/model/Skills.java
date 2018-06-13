package com.bawaaba.rninja4.rookie.model;

/**
 * Created by Asus on 18-02-2018.
 */

public class Skills {
    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }


    public boolean isChk() {
        return isChk;
    }

    public void setChk(boolean chk) {
        isChk = chk;
    }

    private boolean isChk=false;

    private String skills;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
