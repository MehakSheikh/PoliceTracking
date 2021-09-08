
package com.example.policetracking.viewmodels.UserListing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sort__1 {

    @SerializedName("unsorted")
    @Expose
    private Boolean unsorted;
    @SerializedName("sorted")
    @Expose
    private Boolean sorted;
    @SerializedName("empty")
    @Expose
    private Boolean empty;

    public Boolean getUnsorted() {
        return unsorted;
    }

    public void setUnsorted(Boolean unsorted) {
        this.unsorted = unsorted;
    }

    public Boolean getSorted() {
        return sorted;
    }

    public void setSorted(Boolean sorted) {
        this.sorted = sorted;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

}
