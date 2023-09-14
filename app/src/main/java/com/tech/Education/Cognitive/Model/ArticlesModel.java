package com.tech.Education.Cognitive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SAMI on 4/10/2018.
 */

public class ArticlesModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Articles> data = null;

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

    public List<Articles> getData() {
        return data;
    }

    public void setData(List<Articles> data) {
        this.data = data;
    }


    public class Articles {

        @SerializedName("articlesid")
        @Expose
        private String articlesid;
        @SerializedName("articles_title")
        @Expose
        private String articlesTitle;
        @SerializedName("articles_description")
        @Expose
        private String articlesDescription;
        @SerializedName("articles_image")
        @Expose
        private String articlesImage;
        @SerializedName("articles_status")
        @Expose
        private String articlesStatus;

        public String getArticlesid() {
            return articlesid;
        }

        public void setArticlesid(String articlesid) {
            this.articlesid = articlesid;
        }

        public String getArticlesTitle() {
            return articlesTitle;
        }

        public void setArticlesTitle(String articlesTitle) {
            this.articlesTitle = articlesTitle;
        }

        public String getArticlesDescription() {
            return articlesDescription;
        }

        public void setArticlesDescription(String articlesDescription) {
            this.articlesDescription = articlesDescription;
        }

        public String getArticlesImage() {
            return articlesImage;
        }

        public void setArticlesImage(String articlesImage) {
            this.articlesImage = articlesImage;
        }

        public String getArticlesStatus() {
            return articlesStatus;
        }

        public void setArticlesStatus(String articlesStatus) {
            this.articlesStatus = articlesStatus;
        }

    }




}