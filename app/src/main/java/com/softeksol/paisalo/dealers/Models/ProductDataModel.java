package com.softeksol.paisalo.dealers.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDataModel {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("vtype")
    @Expose
    private String vtype;
    @SerializedName("Model")
    @Expose
    private String model;
    @SerializedName("BSP")
    @Expose
    private String bsp;
    @SerializedName("MSP")
    @Expose
    private String msp;
    @SerializedName("FuelType")
    @Expose
    private String fuelType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getVtype() {
        return vtype;
    }

    public void setVtype(String vtype) {
        this.vtype = vtype;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBsp() {
        return bsp;
    }

    public void setBsp(String bsp) {
        this.bsp = bsp;
    }

    public String getMsp() {
        return msp;
    }

    public void setMsp(String msp) {
        this.msp = msp;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

}
