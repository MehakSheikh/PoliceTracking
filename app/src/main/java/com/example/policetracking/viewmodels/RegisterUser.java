package com.example.policetracking.viewmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterUser {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("fatherName")
    @Expose
    private String fatherName;
    @SerializedName("buckleNumber")
    @Expose
    private String buckleNumber;
    @SerializedName("rankId")
    @Expose
    private String rankId;
    @SerializedName("cnic")
    @Expose
    private String cnic;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("branchId")
    @Expose
    private String branchId;
    @SerializedName("contact")
    @Expose
    private String contact;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getBuckleNumber() {
        return buckleNumber;
    }

    public void setBuckleNumber(String buckleNumber) {
        this.buckleNumber = buckleNumber;
    }

    public String getRankId() {
        return rankId;
    }

    public void setRankId(String rankId) {
        this.rankId = rankId;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}