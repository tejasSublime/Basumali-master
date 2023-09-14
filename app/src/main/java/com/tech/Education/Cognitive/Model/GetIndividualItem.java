package com.tech.Education.Cognitive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetIndividualItem {


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
