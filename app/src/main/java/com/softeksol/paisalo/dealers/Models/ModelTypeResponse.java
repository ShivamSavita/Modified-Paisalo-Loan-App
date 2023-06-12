package com.softeksol.paisalo.dealers.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelTypeResponse {


    @SerializedName("ModelId")
    @Expose
    private Integer ModelId;
    @SerializedName("VIId")
    @Expose
    private Integer vIId;
    @SerializedName("FuelType")
    @Expose
    private String fuelType;
    @SerializedName("BId")
    @Expose
    private Integer bId;
    @SerializedName("Name")
    @Expose
    private String name;

    public Integer getModelId() {
        return ModelId;
    }

    public void setModelId(Integer modelId) {
        ModelId = modelId;
    }

    public Integer getvIId() {
        return vIId;
    }

    public void setvIId(Integer vIId) {
        this.vIId = vIId;
    }

    public Integer getbId() {
        return bId;
    }

    public void setbId(Integer bId) {
        this.bId = bId;
    }

    public Integer getVIId() {
        return vIId;
    }

    public void setVIId(Integer vIId) {
        this.vIId = vIId;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public Integer getBId() {
        return bId;
    }

    public void setBId(Integer bId) {
        this.bId = bId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
