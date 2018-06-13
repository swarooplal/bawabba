package com.bawaaba.rninja4.rookie.model.inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rninja4 on 11/1/17.
 */

public class InboxResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("emails")
    @Expose
    private List<Email> emails = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

}
