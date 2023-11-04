package com.softeksol.paisalo.jlgsourcing.entities;

import com.google.gson.annotations.SerializedName;

public class HomeVisitFiList {


        @SerializedName("Code")
        public int code;
        @SerializedName("Creator")
        public String creator;
        @SerializedName("SanctionedAmt")
        public int sanctionedAmt;
        @SerializedName("Remarks")
        public String remarks;
        @SerializedName("Dt_Fin")
        public String dt_Fin;
        @SerializedName("Dt_Start")
        public String dt_Start;
        @SerializedName("SchCode")
        public String schCode;
        @SerializedName("Fname")
        public String fname;
        @SerializedName("Mname")
        public String mname;
        @SerializedName("Lname")
        public String lname;
        @SerializedName("F_fname")
        public String f_fname;
        @SerializedName("F_mname")
        public String f_mname;
        @SerializedName("F_lname")
        public String f_lname;
        @SerializedName("Description")
        public String description;
        @SerializedName("Period")
        public int period;
        @SerializedName("Rate")
        public double rate;
        public String aadharid;
        @SerializedName("Addr")
        public String addr;
        @SerializedName("P_ph3")
        public String p_ph3;
        @SerializedName("GroupCode")
        public String groupCode;


    public HomeVisitFiList(int code, String creator, int sanctionedAmt, String remarks, String dt_Fin, String dt_Start, String schCode, String fname, String mname, String lname, String f_fname, String f_mname, String f_lname, String description, int period, double rate, String aadharid, String addr, String p_ph3, String groupCode) {
        this.code = code;
        this.creator = creator;
        this.sanctionedAmt = sanctionedAmt;
        this.remarks = remarks;
        this.dt_Fin = dt_Fin;
        this.dt_Start = dt_Start;
        this.schCode = schCode;
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.f_fname = f_fname;
        this.f_mname = f_mname;
        this.f_lname = f_lname;
        this.description = description;
        this.period = period;
        this.rate = rate;
        this.aadharid = aadharid;
        this.addr = addr;
        this.p_ph3 = p_ph3;
        this.groupCode = groupCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getSanctionedAmt() {
        return sanctionedAmt;
    }

    public void setSanctionedAmt(int sanctionedAmt) {
        this.sanctionedAmt = sanctionedAmt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDt_Fin() {
        return dt_Fin;
    }

    public void setDt_Fin(String dt_Fin) {
        this.dt_Fin = dt_Fin;
    }

    public String getDt_Start() {
        return dt_Start;
    }

    public void setDt_Start(String dt_Start) {
        this.dt_Start = dt_Start;
    }

    public String getSchCode() {
        return schCode;
    }

    public void setSchCode(String schCode) {
        this.schCode = schCode;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getF_fname() {
        return f_fname;
    }

    public void setF_fname(String f_fname) {
        this.f_fname = f_fname;
    }

    public String getF_mname() {
        return f_mname;
    }

    public void setF_mname(String f_mname) {
        this.f_mname = f_mname;
    }

    public String getF_lname() {
        return f_lname;
    }

    public void setF_lname(String f_lname) {
        this.f_lname = f_lname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getAadharid() {
        return aadharid;
    }

    public void setAadharid(String aadharid) {
        this.aadharid = aadharid;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getP_ph3() {
        return p_ph3;
    }

    public void setP_ph3(String p_ph3) {
        this.p_ph3 = p_ph3;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}
