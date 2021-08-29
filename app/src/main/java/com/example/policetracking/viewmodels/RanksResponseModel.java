package com.example.policetracking.viewmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RanksResponseModel {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("Success")
    @Expose
    private Boolean success;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}