package com.tech.Education.Cognitive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SAMI on 4/13/2018.
 */

public class QuizModel {


    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Quiz> data = null;

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

    public List<Quiz> getData() {
        return data;
    }

    public void setData(List<Quiz> data) {
        this.data = data;
    }

    public class Quiz {

        @SerializedName("quizid")
        @Expose
        private String quizid;
        @SerializedName("quiz_title")
        @Expose
        private String quizTitle;
        @SerializedName("quiz_video")
        @Expose
        private String quizVideo;
        @SerializedName("quiz_status")
        @Expose
        private String quizStatus;
        @SerializedName("quiz_point")
        @Expose
        private String quizPoint;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

        public String getQuizid() {
            return quizid;
        }

        public void setQuizid(String quizid) {
            this.quizid = quizid;
        }

        public String getQuizTitle() {
            return quizTitle;
        }

        public void setQuizTitle(String quizTitle) {
            this.quizTitle = quizTitle;
        }

        public String getQuizVideo() {
            return quizVideo;
        }

        public void setQuizVideo(String quizVideo) {
            this.quizVideo = quizVideo;
        }

        public String getQuizStatus() {
            return quizStatus;
        }

        public void setQuizStatus(String quizStatus) {
            this.quizStatus = quizStatus;
        }

        public String getQuizPoint() {
            return quizPoint;
        }

        public void setQuizPoint(String quizPoint) {
            this.quizPoint = quizPoint;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }
}
