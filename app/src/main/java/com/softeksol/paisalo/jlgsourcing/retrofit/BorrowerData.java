package com.softeksol.paisalo.jlgsourcing.retrofit;

import java.io.Serializable;

public class BorrowerData implements Serializable {


    String tietAadhar,tietName,tietAge,tietDob,tietGuardian,tietPAN,tietDrivingLlicense;
    String tietMotherName,tietFatherName,tietBankAccount,tietBankCIF,tietIncome,tietExpence;
    String tietAddress1,tietAddress2,tietAddress3,tietCity,tietPinCode,tietMobile,tietVoter;
    String str_acspGender,str_acspAadharState,str_acspRelationship,str_acspLoanAppFinanceLoanAmount;
    String str_acspBusinessDetail,str_acspLoanReason,str_acspOccupation,str_loanDuration,str_banktype;

    public BorrowerData(String tietAadhar,String tietName,String tietAge,String tietDob,String tietGuardian,
                        String tietPAN,String tietDrivingLlicense,String tietMotherName,String tietFatherName,
                        String tietBankAccount, String tietBankCIF,String tietIncome, String tietExpence,
                        String tietAddress1,String tietAddress2,String tietAddress3,String tietCity,String tietPinCode,String tietMobile,String tietVoter,
                        String str_acspGender,String str_acspAadharState,String str_acspRelationship,String str_acspLoanAppFinanceLoanAmount,
                        String str_acspBusinessDetail,String str_acspLoanReason,String str_acspOccupation,String str_loanDuration,String str_banktype){

        this.tietAadhar=tietAadhar;
        this.tietName=tietName;
        this.tietAge=tietAge;
        this.tietDob=tietDob;
        this.tietGuardian=tietGuardian;
        this.tietPAN=tietPAN;
        this.tietDrivingLlicense=tietDrivingLlicense;
        this.tietMotherName=tietMotherName;
        this.tietFatherName=tietFatherName;
        this.tietBankAccount=tietBankAccount;
        this.tietBankCIF=tietBankCIF;
        this.tietIncome=tietIncome;
        this.tietExpence=tietExpence;
        this.tietAddress1=tietAddress1;
        this.tietAddress2=tietAddress2;
        this.tietAddress3=tietAddress3;
        this.tietCity=tietCity;
        this.tietPinCode=tietPinCode;
        this.tietMobile=tietMobile;
        this.tietVoter=tietVoter;
        this.str_acspGender=str_acspGender;
        this.str_acspAadharState=str_acspAadharState;
        this.str_acspRelationship=str_acspRelationship;
        this.str_acspLoanAppFinanceLoanAmount=str_acspLoanAppFinanceLoanAmount;
        this.str_acspBusinessDetail=str_acspBusinessDetail;
        this.str_acspLoanReason=str_acspLoanReason;
        this.str_acspOccupation=str_acspOccupation;
        this.str_loanDuration=str_loanDuration;
        this.str_banktype=str_banktype;

    }

    public String getTietAadhar() {
        return tietAadhar;
    }

    public void setTietAadhar(String tietAadhar) {
        this.tietAadhar = tietAadhar;
    }

    public String getTietName() {
        return tietName;
    }

    public void setTietName(String tietName) {
        this.tietName = tietName;
    }

    public String getTietAge() {
        return tietAge;
    }

    public void setTietAge(String tietAge) {
        this.tietAge = tietAge;
    }

    public String getTietDob() {
        return tietDob;
    }

    public void setTietDob(String tietDob) {
        this.tietDob = tietDob;
    }

    public String getTietGuardian() {
        return tietGuardian;
    }

    public void setTietGuardian(String tietGuardian) {
        this.tietGuardian = tietGuardian;
    }

    public String getTietPAN() {
        return tietPAN;
    }

    public void setTietPAN(String tietPAN) {
        this.tietPAN = tietPAN;
    }

    public String getTietDrivingLlicense() {
        return tietDrivingLlicense;
    }

    public void setTietDrivingLlicense(String tietDrivingLlicense) {
        this.tietDrivingLlicense = tietDrivingLlicense;
    }

    public String getTietMotherName() {
        return tietMotherName;
    }

    public void setTietMotherName(String tietMotherName) {
        this.tietMotherName = tietMotherName;
    }

    public String getTietFatherName() {
        return tietFatherName;
    }

    public void setTietFatherName(String tietFatherName) {
        this.tietFatherName = tietFatherName;
    }

    public String getTietBankAccount() {
        return tietBankAccount;
    }

    public void setTietBankAccount(String tietBankAccount) {
        this.tietBankAccount = tietBankAccount;
    }

    public String getTietBankCIF() {
        return tietBankCIF;
    }

    public void setTietBankCIF(String tietBankCIF) {
        this.tietBankCIF = tietBankCIF;
    }

    public String getTietIncome() {
        return tietIncome;
    }

    public void setTietIncome(String tietIncome) {
        this.tietIncome = tietIncome;
    }

    public String getTietExpence() {
        return tietExpence;
    }

    public void setTietExpence(String tietExpence) {
        this.tietExpence = tietExpence;
    }

    public String getTietAddress1() {
        return tietAddress1;
    }

    public void setTietAddress1(String tietAddress1) {
        this.tietAddress1 = tietAddress1;
    }

    public String getTietAddress2() {
        return tietAddress2;
    }

    public void setTietAddress2(String tietAddress2) {
        this.tietAddress2 = tietAddress2;
    }

    public String getTietAddress3() {
        return tietAddress3;
    }

    public void setTietAddress3(String tietAddress3) {
        this.tietAddress3 = tietAddress3;
    }

    public String getTietCity() {
        return tietCity;
    }

    public void setTietCity(String tietCity) {
        this.tietCity = tietCity;
    }

    public String getTietPinCode() {
        return tietPinCode;
    }

    public void setTietPinCode(String tietPinCode) {
        this.tietPinCode = tietPinCode;
    }

    public String getTietMobile() {
        return tietMobile;
    }

    public void setTietMobile(String tietMobile) {
        this.tietMobile = tietMobile;
    }

    public String getTietVoter() {
        return tietVoter;
    }

    public void setTietVoter(String tietVoter) {
        this.tietVoter = tietVoter;
    }

    public String getStr_acspGender() {
        return str_acspGender;
    }

    public void setStr_acspGender(String str_acspGender) {
        this.str_acspGender = str_acspGender;
    }

    public String getStr_acspAadharState() {
        return str_acspAadharState;
    }

    public void setStr_acspAadharState(String str_acspAadharState) {
        this.str_acspAadharState = str_acspAadharState;
    }

    public String getStr_acspRelationship() {
        return str_acspRelationship;
    }

    public void setStr_acspRelationship(String str_acspRelationship) {
        this.str_acspRelationship = str_acspRelationship;
    }

    public String getStr_acspLoanAppFinanceLoanAmount() {
        return str_acspLoanAppFinanceLoanAmount;
    }

    public void setStr_acspLoanAppFinanceLoanAmount(String str_acspLoanAppFinanceLoanAmount) {
        this.str_acspLoanAppFinanceLoanAmount = str_acspLoanAppFinanceLoanAmount;
    }

    public String getStr_acspBusinessDetail() {
        return str_acspBusinessDetail;
    }

    public void setStr_acspBusinessDetail(String str_acspBusinessDetail) {
        this.str_acspBusinessDetail = str_acspBusinessDetail;
    }

    public String getStr_acspLoanReason() {
        return str_acspLoanReason;
    }

    public void setStr_acspLoanReason(String str_acspLoanReason) {
        this.str_acspLoanReason = str_acspLoanReason;
    }

    public String getStr_acspOccupation() {
        return str_acspOccupation;
    }

    public void setStr_acspOccupation(String str_acspOccupation) {
        this.str_acspOccupation = str_acspOccupation;
    }

    public String getStr_loanDuration() {
        return str_loanDuration;
    }

    public void setStr_loanDuration(String str_loanDuration) {
        this.str_loanDuration = str_loanDuration;
    }

    public String getStr_banktype() {
        return str_banktype;
    }

    public void setStr_banktype(String str_banktype) {
        this.str_banktype = str_banktype;
    }
}
