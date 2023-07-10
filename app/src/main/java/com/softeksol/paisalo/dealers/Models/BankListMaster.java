package com.softeksol.paisalo.dealers.Models;

public class BankListMaster {

    String BankCode;
    String BankName;

    public BankListMaster() {
    }

    public BankListMaster(String bankCode, String bankName) {
        BankCode = bankCode;
        BankName = bankName;
    }

    public String getBankCode() {
        return BankCode;
    }

    public void setBankCode(String bankCode) {
        BankCode = bankCode;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }
}
