package com.softeksol.paisalo.dealers.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.softeksol.paisalo.jlgsourcing.entities.BankData;

import java.util.List;

public  class BrandDataList{

    BrandData[] brandData;

    public BrandData[] getBrandData() {
        return brandData;
    }

    public void setBrandData(BrandData[] brandData) {
        this.brandData = brandData;
    }
}
