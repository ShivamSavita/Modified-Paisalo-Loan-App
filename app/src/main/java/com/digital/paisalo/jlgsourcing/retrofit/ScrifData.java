package com.digital.paisalo.jlgsourcing.retrofit;


import com.google.gson.annotations.SerializedName;

public class ScrifData {
    private String data;
    private String message;
    private boolean status;

    @SerializedName("data")
    public String getData() { return data; }
    @SerializedName("data")
    public void setData(String value) { this.data = value; }

    @SerializedName("message")
    public String getMessage() { return message; }
    @SerializedName("message")
    public void setMessage(String value) { this.message = value; }

    @SerializedName("status")
    public boolean getStatus() { return status; }
    @SerializedName("status")
    public void setStatus(boolean value) { this.status = value; }
}
