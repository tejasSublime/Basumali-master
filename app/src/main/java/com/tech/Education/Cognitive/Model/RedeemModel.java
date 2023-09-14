package com.tech.Education.Cognitive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SAMI on 4/24/2018.
 */

public class RedeemModel {
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Redeem> data = null;

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

    public List<Redeem> getData() {
        return data;
    }

    public void setData(List<Redeem> data) {
        this.data = data;
    }


    public  class Redeem{

        @SerializedName("redeemid")
        @Expose
        private String redeemid;
        @SerializedName("redeem_title")
        @Expose
        private String redeemTitle;
        @SerializedName("redeem_description")
        @Expose
        private String redeemDescription;
        @SerializedName("redeem_image")
        @Expose
        private String redeemImage;
        @SerializedName("redeem_points")
        @Expose
        private String redeemPoints;
        @SerializedName("redeem_status")
        @Expose
        private String redeemStatus;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

        public String getRedeemid() {
            return redeemid;
        }

        public void setRedeemid(String redeemid) {
            this.redeemid = redeemid;
        }

        public String getRedeemTitle() {
            return redeemTitle;
        }

        public void setRedeemTitle(String redeemTitle) {
            this.redeemTitle = redeemTitle;
        }

        public String getRedeemDescription() {
            return redeemDescription;
        }

        public void setRedeemDescription(String redeemDescription) {
            this.redeemDescription = redeemDescription;
        }

        public String getRedeemImage() {
            return redeemImage;
        }

        public void setRedeemImage(String redeemImage) {
            this.redeemImage = redeemImage;
        }

        public String getRedeemPoints() {
            return redeemPoints;
        }

        public void setRedeemPoints(String redeemPoints) {
            this.redeemPoints = redeemPoints;
        }

        public String getRedeemStatus() {
            return redeemStatus;
        }

        public void setRedeemStatus(String redeemStatus) {
            this.redeemStatus = redeemStatus;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }

}
