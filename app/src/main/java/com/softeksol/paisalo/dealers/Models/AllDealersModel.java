package com.softeksol.paisalo.dealers.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllDealersModel {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("P_Address")
    @Expose
    private String pAddress;
    @SerializedName("Panno")
    @Expose
    private String panno;
    @SerializedName("P_Pincode")
    @Expose
    private String pPincode;
    @SerializedName("P_City")
    @Expose
    private String pCity;
    @SerializedName("P_District")
    @Expose
    private String pDistrict;
    @SerializedName("P_State")
    @Expose
    private String pState;
    @SerializedName("Firm_Name")
    @Expose
    private String firmName;
    @SerializedName("Firm_Type")
    @Expose
    private String firmType;
    @SerializedName("O_Address")
    @Expose
    private String oAddress;
    @SerializedName("O_Pincode")
    @Expose
    private String oPincode;
    @SerializedName("O_City")
    @Expose
    private String oCity;
    @SerializedName("O_District")
    @Expose
    private String oDistrict;
    @SerializedName("O_State")
    @Expose
    private String oState;
    @SerializedName("Vehicle_Type")
    @Expose
    private String vehicleType;
    @SerializedName("Manufacturer")
    @Expose
    private String manufacturer;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("brandid")
    @Expose
    private Integer brandid;
    @SerializedName("GroupCode")
    @Expose
    private String groupCode;
    @SerializedName("Creator")
    @Expose
    private String creator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPAddress() {
        return pAddress;
    }

    public void setPAddress(String pAddress) {
        this.pAddress = pAddress;
    }

    public String getPanno() {
        return panno;
    }

    public void setPanno(String panno) {
        this.panno = panno;
    }

    public String getPPincode() {
        return pPincode;
    }

    public void setPPincode(String pPincode) {
        this.pPincode = pPincode;
    }

    public String getPCity() {
        return pCity;
    }

    public void setPCity(String pCity) {
        this.pCity = pCity;
    }

    public String getPDistrict() {
        return pDistrict;
    }

    public void setPDistrict(String pDistrict) {
        this.pDistrict = pDistrict;
    }

    public String getPState() {
        return pState;
    }

    public void setPState(String pState) {
        this.pState = pState;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getFirmType() {
        return firmType;
    }

    public void setFirmType(String firmType) {
        this.firmType = firmType;
    }

    public String getOAddress() {
        return oAddress;
    }

    public void setOAddress(String oAddress) {
        this.oAddress = oAddress;
    }

    public String getOPincode() {
        return oPincode;
    }

    public void setOPincode(String oPincode) {
        this.oPincode = oPincode;
    }

    public String getOCity() {
        return oCity;
    }

    public void setOCity(String oCity) {
        this.oCity = oCity;
    }

    public String getODistrict() {
        return oDistrict;
    }

    public void setODistrict(String oDistrict) {
        this.oDistrict = oDistrict;
    }

    public String getOState() {
        return oState;
    }

    public void setOState(String oState) {
        this.oState = oState;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getBrandid() {
        return brandid;
    }

    public void setBrandid(Integer brandid) {
        this.brandid = brandid;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

}
