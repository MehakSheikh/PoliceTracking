
package com.example.policetracking.viewmodels.UserListing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserListingModel {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
