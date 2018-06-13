package com.bawaaba.rninja4.rookie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rninja4 on 11/1/17.
 */

public class RegisterSkillsResponse {


    @SerializedName("skills")
    @Expose
    private List<Skill> skills = null;

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

}
