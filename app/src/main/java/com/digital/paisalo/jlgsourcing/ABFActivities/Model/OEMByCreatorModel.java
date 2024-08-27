package com.digital.paisalo.jlgsourcing.ABFActivities.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OEMByCreatorModel {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Firm_Name")
    @Expose
    private String firmName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

}