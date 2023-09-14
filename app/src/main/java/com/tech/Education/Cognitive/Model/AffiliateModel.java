package com.tech.Education.Cognitive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AffiliateModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Affiliate> data = null;

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

    public List<Affiliate> getData() {
        return data;
    }

    public void setData(List<Affiliate> data) {
        this.data = data;
    }

    public class Affiliate {

        @SerializedName("ce_affiliate_id")
        @Expose
        private String ceAffiliateId;
        @SerializedName("logo")
        @Expose
        private String logo;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("affiliate_status")
        @Expose
        private String affiliateStatus;

        public String getCeAffiliateId() {
            return ceAffiliateId;
        }

        public void setCeAffiliateId(String ceAffiliateId) {
            this.ceAffiliateId = ceAffiliateId;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getAffiliateStatus() {
            return affiliateStatus;
        }

        public void setAffiliateStatus(String affiliateStatus) {
            this.affiliateStatus = affiliateStatus;
        }

    }

}
