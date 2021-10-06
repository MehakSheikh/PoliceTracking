package com.example.policetracking.viewmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationError {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cnic")
    @Expose
    private String cnic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

}