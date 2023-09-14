package com.tech.Education.Cognitive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SAMI on 4/11/2018.
 */

public class OfferModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Offers> data = null;

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

    public List<Offers> getData() {
        return data;
    }

    public void setData(List<Offers> data) {
        this.data = data;
    }


    public class Offers {

        @SerializedName("offerid")
        @Expose
        private String offerid;
        @SerializedName("offer_title")
        @Expose
        private String offerTitle;
        @SerializedName("offer_description")
        @Expose
        private String offerDescription;
        @SerializedName("offer_image")
        @Expose
        private String offerImage;
        @SerializedName("offer_link")
        @Expose
        private String offerLink;
        @SerializedName("offer_start")
        @Expose
        private String offerStart;
        @SerializedName("offer_end")
        @Expose
        private String offerEnd;
        @SerializedName("offer_status")
        @Expose
        private String offerStatus;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

        public String getOfferid() {
            return offerid;
        }

        public void setOfferid(String offerid) {
            this.offerid = offerid;
        }

        public String getOfferTitle() {
            return offerTitle;
        }

        public void setOfferTitle(String offerTitle) {
            this.offerTitle = offerTitle;
        }

        public String getOfferDescription() {
            return offerDescription;
        }

        public void setOfferDescription(String offerDescription) {
            this.offerDescription = offerDescription;
        }

        public String getOfferImage() {
            return offerImage;
        }

        public void setOfferImage(String offerImage) {
            this.offerImage = offerImage;
        }

        public String getOfferLink() {
            return offerLink;
        }

        public void setOfferLink(String offerLink) {
            this.offerLink = offerLink;
        }

        public String getOfferStart() {
            return offerStart;
        }

        public void setOfferStart(String offerStart) {
            this.offerStart = offerStart;
        }

        public String getOfferEnd() {
            return offerEnd;
        }

        public void setOfferEnd(String offerEnd) {
            this.offerEnd = offerEnd;
        }

        public String getOfferStatus() {
            return offerStatus;
        }

        public void setOfferStatus(String offerStatus) {
            this.offerStatus = offerStatus;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }


}
