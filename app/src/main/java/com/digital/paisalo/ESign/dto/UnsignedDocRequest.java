package com.digital.paisalo.ESign.dto;

import com.google.gson.annotations.Expose;

/**
 * Created by sachindra on 6/8/2017.
 */
public class UnsignedDocRequest {
    @Expose
    public String FiCreator;

    @Expose
    public int FiCode;

    @Expose
    public String DocName;

    @Expose
    public String UserID;
}
