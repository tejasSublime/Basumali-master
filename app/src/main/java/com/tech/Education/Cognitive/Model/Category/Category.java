package com.tech.Education.Cognitive.Model.Category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable {

    @SerializedName("categoryid")
    @Expose
    private String categoryid;
    @SerializedName("category_title")
    @Expose
    private String categoryTitle;
    @SerializedName("play_console_id")
    @Expose
    private String playConsoleId;
    @SerializedName("purchase_point")
    @Expose
    private String purchasePoint;
    @SerializedName("category_status")
    @Expose
    private String categoryStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getPlayConsoleId() {
        return playConsoleId;
    }

    public void setPlayConsoleId(String playConsoleId) {
        this.playConsoleId = playConsoleId;
    }

    public String getPurchasePoint() {
        return purchasePoint;
    }

    public void setPurchasePoint(String purchasePoint) {
        this.purchasePoint = purchasePoint;
    }

    public String getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(String categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return categoryTitle;
    }
}
