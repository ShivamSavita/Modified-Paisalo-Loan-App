package com.softeksol.paisalo.jlgsourcing.entities.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OCRData {

    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("f_Name")
    @Expose
    private Object fName;
    @SerializedName("dob")
    @Expose
    private Object dob;
    @SerializedName("pan_No")
    @Expose
    private Object panNo;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("adharId")
    @Expose
    private String adharId;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("address2")
    @Expose
    private String address2;
    @SerializedName("address3")
    @Expose
    private String address3;
    @SerializedName("voterId")
    @Expose
    private Object voterId;
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

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getfName() {
        return fName;
    }

    public void setfName(Object fName) {
        this.fName = fName;
    }

    public Object getDob() {
        return dob;
    }

    public void setDob(Object dob) {
        this.dob = dob;
    }

    public Object getPanNo() {
        return panNo;
    }

    public void setPanNo(Object panNo) {
        this.panNo = panNo;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
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

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public Object getVoterId() {
        return voterId;
    }

    public void setVoterId(Object voterId) {
        this.voterId = voterId;
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

}