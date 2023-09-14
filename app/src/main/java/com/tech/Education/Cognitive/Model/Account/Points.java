
package com.tech.Education.Cognitive.Model.Account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Points implements Serializable{

    @SerializedName("walletid")
    @Expose
    private String walletid;
    @SerializedName("wallet_userid")
    @Expose
    private String walletUserid;
    @SerializedName("wallet_refer")
    @Expose
    private String walletRefer;
    @SerializedName("wallet_offer")
    @Expose
    private String walletOffer;
    @SerializedName("wallet_video")
    @Expose
    private String walletVideo;
    @SerializedName("wallet_quiz")
    @Expose
    private String walletQuiz;
    @SerializedName("wallet_daily_usage")
    @Expose
    private String walletDailyUsage;
    @SerializedName("wallet_category")
    @Expose
    private String walletCategory;
    @SerializedName("wallet_event")
    @Expose
    private String walletEvent;
    @SerializedName("wallet_welcome_points")
    @Expose
    private String walletWelcomePoints;
    @SerializedName("wallet_total")
    @Expose
    private String walletTotal;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getWalletid() {
        return walletid;
    }

    public void setWalletid(String walletid) {
        this.walletid = walletid;
    }

    public String getWalletUserid() {
        return walletUserid;
    }

    public void setWalletUserid(String walletUserid) {
        this.walletUserid = walletUserid;
    }

    public String getWalletRefer() {
        return walletRefer;
    }

    public void setWalletRefer(String walletRefer) {
        this.walletRefer = walletRefer;
    }

    public String getWalletOffer() {
        return walletOffer;
    }

    public void setWalletOffer(String walletOffer) {
        this.walletOffer = walletOffer;
    }

    public String getWalletVideo() {
        return walletVideo;
    }

    public void setWalletVideo(String walletVideo) {
        this.walletVideo = walletVideo;
    }

    public String getWalletQuiz() {
        return walletQuiz;
    }

    public void setWalletQuiz(String walletQuiz) {
        this.walletQuiz = walletQuiz;
    }

    public String getWalletDailyUsage() {
        return walletDailyUsage;
    }

    public void setWalletDailyUsage(String walletDailyUsage) {
        this.walletDailyUsage = walletDailyUsage;
    }

    public String getWalletCategory() {
        return walletCategory;
    }

    public void setWalletCategory(String walletCategory) {
        this.walletCategory = walletCategory;
    }

    public String getWalletEvent() {
        return walletEvent;
    }

    public void setWalletEvent(String walletEvent) {
        this.walletEvent = walletEvent;
    }

    public String getWalletWelcomePoints() {
        return walletWelcomePoints;
    }

    public void setWalletWelcomePoints(String walletWelcomePoints) {
        this.walletWelcomePoints = walletWelcomePoints;
    }

    public String getWalletTotal() {
        return walletTotal;
    }

    public void setWalletTotal(String walletTotal) {
        this.walletTotal = walletTotal;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
