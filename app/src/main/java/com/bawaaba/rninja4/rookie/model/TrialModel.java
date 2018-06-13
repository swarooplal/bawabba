package com.bawaaba.rninja4.rookie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rninja4 on 11/1/17.
 */

public class TrialModel {
    @SerializedName("title[]")
    @Expose
    private String tittle;

    @SerializedName("currency[]")
    @Expose
    private String currency;

    @SerializedName("price[]")
    @Expose
    private String price;

    @SerializedName("description[]")
    @Expose
    private String description;

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
