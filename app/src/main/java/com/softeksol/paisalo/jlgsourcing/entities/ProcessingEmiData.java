package com.softeksol.paisalo.jlgsourcing.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProcessingEmiData implements Serializable {


    @SerializedName("statusCode")
    @Expose
    private long statusCode;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<DataEMI> data;


    public long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(long statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataEMI> getData() {
        return data;
    }

    public void setData(List<DataEMI> data) {
        this.data = data;
    }



}

