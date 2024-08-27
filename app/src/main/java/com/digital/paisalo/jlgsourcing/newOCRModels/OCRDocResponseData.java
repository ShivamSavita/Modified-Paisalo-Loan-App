package com.digital.paisalo.jlgsourcing.newOCRModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OCRDocResponseData {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private OCRDocModelData data;

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

    public OCRDocModelData getData() {
        return data;
    }

    public void setData(OCRDocModelData data) {
        this.data = data;
    }

}
