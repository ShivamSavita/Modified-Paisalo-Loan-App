package com.digital.paisalo.ESign.dto;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by sachindra on 5/23/2017.
 */
public class ESingAuthRequestParmsPsl implements Serializable {

    @Expose
    public String CustCode;

    @Expose
    public String Creator;

    @Expose
    public String CustName;

    @Expose
    public String Reason;

    @Expose
    public String eKycID;

    @Expose
    public String CustUUID;

    @Expose
    public String AuthMethod;    // FP / BIO / OTP

    @Expose
    public String UserID;

    @Expose
    public String Concent;

    @Expose
    public String Uniquedevicecode = "123456789012345";

    @Expose
    public String TerminalID = "1234567890";

    @Expose
    public String AppName = "Paisalo";

    @Expose
    public String RRN;

    @Expose
    public String PdfBase64;

    @Expose
    public String DOB;

    @Expose
    public String Gender;

    @Expose
    public String PinCode;

    @Expose
    public String UIDLastFourDigit;


}

