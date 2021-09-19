package com.example.policetracking.viewmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReceiveLocationResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("lat")
    @Expose
    private Double lat;
   @SerializedName("lng")
    @Expose
    private Double lng;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}