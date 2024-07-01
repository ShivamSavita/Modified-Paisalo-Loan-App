package com.softeksol.paisalo.jlgsourcing.newOCRModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OCRDocModelData {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("pan_No")
    @Expose
    private String panNo;
    @SerializedName("adharId")
    @Expose
    private String adharId;
    @SerializedName("voterId")
    @Expose
    private String voterId;
    @SerializedName("dL_No")
    @Expose
    private String dLNo;
    @SerializedName("isOSV")
    @Expose
    private Boolean isOSV;
    @SerializedName("osvName")
    @Expose
    private String osvName;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error_reason")
    @Expose
    private String errorReason;
    @SerializedName("error_code")
    @Expose
    private String errorCode;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("reasonPhase")
    @Expose
    private String reasonPhase;
    @SerializedName("isSuccessStatusCode")
    @Expose
    private Boolean isSuccessStatusCode;
    @SerializedName("responseContent")
    @Expose
    private String responseContent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getAdharId() {
        return adharId;
    }

    public void setAdharId(String adharId) {
        this.adharId = adharId;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getdLNo() {
        return dLNo;
    }

    public void setdLNo(String dLNo) {
        this.dLNo = dLNo;
    }

    public Boolean getIsOSV() {
        return isOSV;
    }

    public void setIsOSV(Boolean isOSV) {
        this.isOSV = isOSV;
    }

    public String getOsvName() {
        return osvName;
    }

    public void setOsvName(String osvName) {
        this.osvName = osvName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getReasonPhase() {
        return reasonPhase;
    }

    public void setReasonPhase(String reasonPhase) {
        this.reasonPhase = reasonPhase;
    }

    public Boolean getIsSuccessStatusCode() {
        return isSuccessStatusCode;
    }

    public void setIsSuccessStatusCode(Boolean isSuccessStatusCode) {
        this.isSuccessStatusCode = isSuccessStatusCode;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }
}
