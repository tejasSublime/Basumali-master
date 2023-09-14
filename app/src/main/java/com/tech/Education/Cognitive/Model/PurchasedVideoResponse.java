package com.tech.Education.Cognitive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PurchasedVideoResponse {


    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<PurchasedVideos> data = null;

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

    public List<PurchasedVideos> getData() {
        return data;
    }

    public void setData(List<PurchasedVideos> data) {
        this.data = data;
    }


    public class PurchasedVideos {

        @SerializedName("promoid")
        @Expose
        private String promoid;
        @SerializedName("promo_title")
        @Expose
        private String promoTitle;
        @SerializedName("promo_description")
        @Expose
        private String promoDescription;
        @SerializedName("promo_fullvideo")
        @Expose
        private String promoFullvideo;
        @SerializedName("promo_audio")
        @Expose
        private String promoAudio;
        @SerializedName("promo_thumbnail")
        @Expose
        private String promoThumbnail;
        @SerializedName("category_title")
        @Expose
        private String categoryTitle;
        @SerializedName("play_console_id")
        @Expose
        private String playConsoleId;

        public String getPromoid() {
            return promoid;
        }

        public void setPromoid(String promoid) {
            this.promoid = promoid;
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


    }

}