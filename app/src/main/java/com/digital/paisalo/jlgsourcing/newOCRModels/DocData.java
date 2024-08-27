package com.digital.paisalo.jlgsourcing.newOCRModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocData {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("pan_No")
    @Expose
    private String panNo;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("adharId")
    @Expose
    private String adharId;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("stateName")
    @Expose
    private String stateName;
    @SerializedName("cityName")
    @Expose
    private String cityName;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("voterfatherName")
    @Expose
    private String voterfatherName;
    @SerializedName("voterHusbandName")
    @Expose
    private String voterHusbandName;
    @SerializedName("guardianName")
    @Expose
    private String guardianName;
    @SerializedName("relation")
    @Expose
    private String relation;
    @SerializedName("voterId")
    @Expose
    private String voterId;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAdharId() {
        return adharId;
    }

    public void setAdharId(String adharId) {
        this.adharId = adharId;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getVoterfatherName() {
        return voterfatherName;
    }

    public void setVoterfatherName(String voterfatherName) {
        this.voterfatherName = voterfatherName;
    }

    public String getVoterHusbandName() {
        return voterHusbandName;
    }

    public void setVoterHusbandName(String voterHusbandName) {
        this.voterHusbandName = voterHusbandName;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
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
