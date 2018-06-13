package com.bawaaba.rninja4.rookie.model.currencies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rninja4 on 11/1/17.
 */

public class CurrenciesResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("currencies")
    @Expose
    private List<Currency> currencies = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }
}
