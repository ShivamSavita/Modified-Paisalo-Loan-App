package com.digital.paisalo.jlgsourcing.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckCrifData {


    @SerializedName("data")
    @Expose
    private Data data ;

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public static class Data {
        @SerializedName("fiCode")
        @Expose
        private Integer fiCode;
        @SerializedName("creator")
        @Expose
        private String creator;
        @SerializedName("err_code")
        @Expose
        private String errCode;
        @SerializedName("is_success")
        @Expose
        private Boolean isSuccess;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("bank")
        @Expose
        private String bank;
        @SerializedName("duration")
        @Expose
        private Integer duration;
        @SerializedName("income")
        @Expose
        private Integer income;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("expense")
        @Expose
        private Integer expense;
        @SerializedName("loan_amount")
        @Expose
        private String loanAmount;

        public Integer getFiCode() {
            return fiCode;
        }

        public void setFiCode(Integer fiCode) {
            this.fiCode = fiCode;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getErrCode() {
            return errCode;
        }

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

        public Boolean getIsSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(Boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public Integer getDuration() {
            return duration;
        }

        public void setDuration(Integer duration) {
            this.duration = duration;
        }

        public Integer getIncome() {
            return income;
        }

        public void setIncome(Integer income) {
            this.income = income;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public Integer getExpense() {
            return expense;
        }

        public void setExpense(Integer expense) {
            this.expense = expense;
        }

        public String getLoanAmount() {
            return loanAmount;
        }

        public void setLoanAmount(String loanAmount) {
            this.loanAmount = loanAmount;
        }
    }
}

