package com.softeksol.paisalo.jlgsourcing.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataEMI implements Serializable {


    @SerializedName("FiCode")
    @Expose
    private String fiCode;
    @SerializedName("FiCreator")
    @Expose
    private String fiCreator;
    @SerializedName("LoanAmount")
    @Expose
    private long loanAmount;
    @SerializedName("SevenPerPF")
    @Expose
    private long sevenPerPF;
    @SerializedName("Gst18Per")
    @Expose
    private long gst18Per;
    @SerializedName("InsuranceAmt")
    @Expose
    private long insuranceAmt;
    @SerializedName("ProcessingFeeToPay")
    @Expose
    private long processingFeeToPay;


    public String getFiCode() {
        return fiCode;
    }

    public void setFiCode(String fiCode) {
        this.fiCode = fiCode;
    }

    public String getFiCreator() {
        return fiCreator;
    }

    public void setFiCreator(String fiCreator) {
        this.fiCreator = fiCreator;
    }

    public long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public long getSevenPerPF() {
        return sevenPerPF;
    }

    public void setSevenPerPF(long sevenPerPF) {
        this.sevenPerPF = sevenPerPF;
    }

    public long getGst18Per() {
        return gst18Per;
    }

    public void setGst18Per(long gst18Per) {
        this.gst18Per = gst18Per;
    }

    public long getInsuranceAmt() {
        return insuranceAmt;
    }

    public void setInsuranceAmt(long insuranceAmt) {
        this.insuranceAmt = insuranceAmt;
    }

    public long getProcessingFeeToPay() {
        return processingFeeToPay;
    }

    public void setProcessingFeeToPay(long processingFeeToPay) {
        this.processingFeeToPay = processingFeeToPay;
    }
}
