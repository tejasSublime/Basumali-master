package com.tech.Education.Cognitive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MVideo {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<SingleVideo> data = null;

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

    public List<SingleVideo> getData() {
        return data;
    }

    public void setData(List<SingleVideo> data) {
        this.data = data;
    }

    public  class SingleVideo{

        @SerializedName("promo_title")
        @Expose
        private String promoTitle;
        @SerializedName("promo_description")
        @Expose
        private String promoDescription;
        @SerializedName("promo_video")
        @Expose
        private String promoVideo;
        @SerializedName("promo_thumbnail")
        @Expose
        private String promoThumbnail;
        @SerializedName("category_title")
        @Expose
        private String categoryTitle;

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
    }


}