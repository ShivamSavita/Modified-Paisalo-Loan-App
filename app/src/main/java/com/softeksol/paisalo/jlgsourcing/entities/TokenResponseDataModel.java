package com.softeksol.paisalo.jlgsourcing.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenResponseDataModel {

    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("account_exists")
    @Expose
    private Boolean accountExists;
    @SerializedName("upi_id")
    @Expose
    private Object upiId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("remarks")
    @Expose
    private Object remarks;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Boolean getAccountExists() {
        return accountExists;
    }

    public void setAccountExists(Boolean accountExists) {
        this.accountExists = accountExists;
    }

    public Object getUpiId() {
        return upiId;
    }

    public void setUpiId(Object upiId) {
        this.upiId = upiId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

}
