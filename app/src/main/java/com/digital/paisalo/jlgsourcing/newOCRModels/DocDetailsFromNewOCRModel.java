package com.digital.paisalo.jlgsourcing.newOCRModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocDetailsFromNewOCRModel {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private DocData data;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DocData getData() {
        return data;
    }

    public void setData(DocData data) {
        this.data = data;
    }
}
