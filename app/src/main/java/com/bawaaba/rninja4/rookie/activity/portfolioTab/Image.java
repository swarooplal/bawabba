package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import java.io.Serializable;


public class Image implements Serializable {

    private String name;
    private String  medium;
    private String  large;


    public Image() {
    }

    public Image(String name,String medium,  String large) {
        this.name = name;

        this.medium = medium;
        this.large = large;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }



    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }


}