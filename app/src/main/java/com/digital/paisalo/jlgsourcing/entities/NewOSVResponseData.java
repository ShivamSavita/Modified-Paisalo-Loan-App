package com.digital.paisalo.jlgsourcing.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewOSVResponseData {

    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("dob")
    @Expose
    private Object dob;
    @SerializedName("pan_No")
    @Expose
    private String panNo;
    @SerializedName("adharId")
    @Expose
    private Object adharId;
    @SerializedName("voterId")
    @Expose
    private Object voterId;
    @SerializedName("dL_No")
    @Expose
    private Object dLNo;
    @SerializedName("isOSV")
    @Expose
    private Boolean isOSV;
    @SerializedName("isIdMatched")
    @Expose
    private Boolean isIdMatched;
    @SerializedName("osvName")
    @Expose
    private Object osvName;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error_reason")
    @Expose
    private Object errorReason;
    @SerializedName("error_code")
    @Expose
    private Object errorCode;
    @SerializedName("error_message")
    @Expose
    private Object errorMessage;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("reasonPhase")
    @Expose
    private Object reasonPhase;
    @SerializedName("isSuccessStatusCode")
    @Expose
    private Boolean isSuccessStatusCode;
    @SerializedName("responseContent")
    @Expose
    private Object responseContent;

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getDob() {
        return dob;
    }

    public void setDob(Object dob) {
        this.dob = dob;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public Object getAdharId() {
        return adharId;
    }

    public void setAdharId(Object adharId) {
        this.adharId = adharId;
    }

    public Object getVoterId() {
        return voterId;
    }

    public void setVoterId(Object voterId) {
        this.voterId = voterId;
    }

    public Object getdLNo() {
        return dLNo;
    }

    public void setdLNo(Object dLNo) {
        this.dLNo = dLNo;
    }

    public Boolean getIsOSV() {
        return isOSV;
    }

    public void setIsOSV(Boolean isOSV) {
        this.isOSV = isOSV;
    }

    public Boolean getIsIdMatched() {
        return isIdMatched;
    }

    public void setIsIdMatched(Boolean isIdMatched) {
        this.isIdMatched = isIdMatched;
    }

    public Object getOsvName() {
        return osvName;
    }

    public void setOsvName(Object osvName) {
        this.osvName = osvName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Object getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(Object errorReason) {
        this.errorReason = errorReason;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Object getReasonPhase() {
        return reasonPhase;
    }

    public void setReasonPhase(Object reasonPhase) {
        this.reasonPhase = reasonPhase;
    }

    public Boolean getIsSuccessStatusCode() {
        return isSuccessStatusCode;
    }

    public void setIsSuccessStatusCode(Boolean isSuccessStatusCode) {
        this.isSuccessStatusCode = isSuccessStatusCode;
    }

    public Object getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(Object responseContent) {
        this.responseContent = responseContent;
    }

}
