package com.tech.Education.Cognitive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sami3535 on 1/5/2018.
 */

public class DayDetails {
    @SerializedName("retailer_name")
    @Expose
    private String retailerName;
    @SerializedName("date")
    @Expose
    private String date;

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
