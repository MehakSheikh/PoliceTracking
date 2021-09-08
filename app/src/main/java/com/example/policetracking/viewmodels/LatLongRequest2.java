package com.example.policetracking.viewmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatLongRequest2 {

    @SerializedName("lat")
    @Expose
    private String latitude;
    @SerializedName("lng")
    @Expose
    private String longitude;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("userId")
    @Expose
    private Long userId;

    public LatLongRequest2(String latitude, String longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
