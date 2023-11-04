package com.softeksol.paisalo.jlgsourcing.retrofit;


import com.google.gson.annotations.SerializedName;

public class KycDocData {
    private String Type;
    private String Pan_Name;
    private String VoterId_Name;
    private String Aadhar_Name;
    private String DrivingLic_Name;
    private String BankAcc_Name;
    private String Bank_Name;
    private String FiCode;
    private String Creator;
    private boolean status;

    @SerializedName("Type")
    public String getType() { return Type ; }
    @SerializedName("Type")
    public void setType(String value) { this.Type = value; }

    @SerializedName("Pan_Name")
    public String getPanName() { return Pan_Name ; }
    @SerializedName("Pan_Name")
    public void setPanName(String PanName) { this.Pan_Name  = PanName; }



    @SerializedName("VoterId_Name")
    public String getVoterIdName() { return VoterId_Name ; }
    @SerializedName("VoterId_Name")
    public void setVoterIdName(String value) { this.VoterId_Name  = value; }

    @SerializedName("Aadhar_Name")
    public String getAadharName() { return Aadhar_Name ; }
    @SerializedName("Aadhar_Name")
    public void setAadharName(String value) { this.Aadhar_Name  = value; }

    @SerializedName("DrivingLic_Name")
    public String getDrivingLicName() { return DrivingLic_Name ; }
    @SerializedName("DrivingLic_Name")
    public void setDrivingLicName(String value) { this.DrivingLic_Name  = value; }



    @SerializedName("BankAcc_Name")
    public String getBankAccName() { return BankAcc_Name  ; }
    @SerializedName("BankAcc_Name")
    public void setBankAccName(String value) { this.BankAcc_Name   = value; }

    @SerializedName("Bank_Name")
    public String getBankName() { return Bank_Name  ; }
    @SerializedName("Bank_Name")
    public void setBankName(String value) { this.Bank_Name   = value; }


    @SerializedName("FiCode")
    public String getFiCode() { return FiCode   ; }
    @SerializedName("FiCode")
    public void setFiCode(String value) { this.FiCode   = value; }

    @SerializedName("Creator")
    public String getCreator() { return Creator    ; }
    @SerializedName("Creator")
    public void setCreator(String value) { this.Creator    = value; }
}
