package com.softeksol.paisalo.ESign.dto;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by sachindra on 5/23/2017.
 */
public class ESingAuthRequestParms implements Serializable {
    @Expose
    public int FICode;

    @Expose
    public String Creator;

    @Expose
    public String CustName;

    @Expose
    public String Reason;

    @Expose
    public String CustUUID;

    @Expose
    public String GurUUID;

    @Expose
    public int GrNo;

    @Expose
    public String OTPBioMetricData;

    @Expose
    public String AuthMethod;    // FP / BIO / OTP

    @Expose
    public String UserID;

    @Expose
    public String Concent;

    @Expose
    public String eKycID;

    @Expose
    public String Uniquedevicecode = "123456789012345";

    @Expose
    public String TerminalID = "1234567890";

    @Expose
    public String PdfToSign;

    @Expose
    public String RRN;

}

