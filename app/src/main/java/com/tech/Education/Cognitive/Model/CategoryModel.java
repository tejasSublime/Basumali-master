package com.tech.Education.Cognitive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Categories> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Categories> getData() {
        return data;
    }

    public void setData(List<Categories> data) {
        this.data = data;
    }
    public class Categories {

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
}
