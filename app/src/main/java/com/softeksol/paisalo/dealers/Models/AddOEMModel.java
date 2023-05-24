package com.softeksol.paisalo.dealers.Models;

public class AddOEMModel {
    String Userid;
    String UserType;
    String Name;
    String Phone;
    String Email;
    String P_Address;
    String Panno;
    String Aadharno;
    String P_Pincode;
    String P_City;
    String P_District;
    String P_State;

    public AddOEMModel() {
    }

    public AddOEMModel(String userid, String userType, String name, String phone, String email, String p_Address, String panno, String aadharno, String p_Pincode, String p_City, String p_District, String p_State) {
        Userid = userid;
        UserType = userType;
        Name = name;
        Phone = phone;
        Email = email;
        P_Address = p_Address;
        Panno = panno;
        Aadharno = aadharno;
        P_Pincode = p_Pincode;
        P_City = p_City;
        P_District = p_District;
        P_State = p_State;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getP_Address() {
        return P_Address;
    }

    public void setP_Address(String p_Address) {
        P_Address = p_Address;
    }

    public String getPanno() {
        return Panno;
    }

    public void setPanno(String panno) {
        Panno = panno;
    }

    public String getAadharno() {
        return Aadharno;
    }

    public void setAadharno(String aadharno) {
        Aadharno = aadharno;
    }

    public String getP_Pincode() {
        return P_Pincode;
    }

    public void setP_Pincode(String p_Pincode) {
        P_Pincode = p_Pincode;
    }

    public String getP_City() {
        return P_City;
    }

    public void setP_City(String p_City) {
        P_City = p_City;
    }

    public String getP_District() {
        return P_District;
    }

    public void setP_District(String p_District) {
        P_District = p_District;
    }

    public String getP_State() {
        return P_State;
    }

    public void setP_State(String p_State) {
        P_State = p_State;
    }
}
