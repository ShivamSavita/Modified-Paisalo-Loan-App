package com.softeksol.paisalo.dealers.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ABFDocsModel {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Id1")
    @Expose
    private Object id1;
    @SerializedName("DocId")
    @Expose
    private Object docId;
    @SerializedName("DocType")
    @Expose
    private Object docType;
    @SerializedName("UserId")
    @Expose
    private Object userId;
    @SerializedName("UserType")
    @Expose
    private Object userType;
    @SerializedName("DocName")
    @Expose
    private String docName;
    @SerializedName("Type")
    @Expose
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getId1() {
        return id1;
    }

    public void setId1(Object id1) {
        this.id1 = id1;
    }

    public Object getDocId() {
        return docId;
    }

    public void setDocId(Object docId) {
        this.docId = docId;
    }

    public Object getDocType() {
        return docType;
    }

    public void setDocType(Object docType) {
        this.docType = docType;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getUserType() {
        return userType;
    }

    public void setUserType(Object userType) {
        this.userType = userType;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
