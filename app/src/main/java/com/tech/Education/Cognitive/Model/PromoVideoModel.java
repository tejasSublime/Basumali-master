package com.tech.Education.Cognitive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromoVideoModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<PromoVideo> data = null;

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

    public List<PromoVideo> getData() {
        return data;
    }

    public void setData(List<PromoVideo> data) {
        this.data = data;
    }


    public class PromoVideo {

        @SerializedName("promoid")
        @Expose
        private String promoid;
        @SerializedName("promo_category")
        @Expose
        private String promoCategory;
        @SerializedName("promo_title")
        @Expose
        private String promoTitle;
        @SerializedName("promo_description")
        @Expose
        private String promoDescription;
        @SerializedName("promo_video")
        @Expose
        private String promoVideo;
        @SerializedName("promo_fullvideo")
        @Expose
        private String promoFullvideo;
        @SerializedName("promo_audio")
        @Expose
        private String promoAudio;
        @SerializedName("promo_thumbnail")
        @Expose
        private String promoThumbnail;
        @SerializedName("play_console_id")
        @Expose
        private String playConsoleId;
        @SerializedName("purchase_point")
        @Expose
        private String purchasePoint;
        @SerializedName("promo_status")
        @Expose
        private String promoStatus;
        @SerializedName("purchase")
        @Expose
        private String purchase;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("category_title")
        @Expose
        private String categoryTitle;

        public String getPromoid() {
            return promoid;
        }

        public void setPromoid(String promoid) {
            this.promoid = promoid;
        }

        public String getPromoCategory() {
            return promoCategory;
        }

        public void setPromoCategory(String promoCategory) {
            this.promoCategory = promoCategory;
        }

        public String getPromoTitle() {
            return promoTitle;
        }

        public void setPromoTitle(String promoTitle) {
            this.promoTitle = promoTitle;
        }

        public String getPromoDescription() {
            return promoDescription;
        }

        public void setPromoDescription(String promoDescription) {
            this.promoDescription = promoDescription;
        }

        public String getPromoVideo() {
            return promoVideo;
        }

        public void setPromoVideo(String promoVideo) {
            this.promoVideo = promoVideo;
        }

        public String getPromoFullvideo() {
            return promoFullvideo;
        }

        public void setPromoFullvideo(String promoFullvideo) {
            this.promoFullvideo = promoFullvideo;
        }

        public String getPromoAudio() {
            return promoAudio;
        }

        public void setPromoAudio(String promoAudio) {
            this.promoAudio = promoAudio;
        }

        public String getPromoThumbnail() {
            return promoThumbnail;
        }

        public void setPromoThumbnail(String promoThumbnail) {
            this.promoThumbnail = promoThumbnail;
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

        public String getPromoStatus() {
            return promoStatus;
        }

        public void setPromoStatus(String promoStatus) {
            this.promoStatus = promoStatus;
        }

        public String getPurchase() {
            return purchase;
        }

        public void setPurchase(String purchase) {
            this.purchase = purchase;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getCategoryTitle() {
            return categoryTitle;
        }

        public void setCategoryTitle(String categoryTitle) {
            this.categoryTitle = categoryTitle;
        }
    }
}
