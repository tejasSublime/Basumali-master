package com.tech.Education.Cognitive.App_Bean;

/**
 * Created by admin on 1/18/2018.
 */

public class LevelBean {

    String quizid,quiz_title,quiz_video,quiz_status,video_path,quiz_unlocked;

    public LevelBean(String quizid, String quiz_title, String quiz_video, String quiz_status, String video_path, String quiz_unlocked) {
        this.quizid = quizid;
        this.quiz_title = quiz_title;
        this.quiz_video = quiz_video;
        this.quiz_status = quiz_status;
        this.video_path = video_path;
        this.quiz_unlocked = quiz_unlocked;
    }

    public String getQuizid() {
        return quizid;
    }

    public void setQuizid(String quizid) {
        this.quizid = quizid;
    }

    public String getQuiz_title() {
        return quiz_title;
    }

    public void setQuiz_title(String quiz_title) {
        this.quiz_title = quiz_title;
    }

    public String getQuiz_video() {
        return quiz_video;
    }

    public void setQuiz_video(String quiz_video) {
        this.quiz_video = quiz_video;
    }

    public String getQuiz_status() {
        return quiz_status;
    }

    public void setQuiz_status(String quiz_status) {
        this.quiz_status = quiz_status;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getQuiz_unlocked() {
        return quiz_unlocked;
    }

    public void setQuiz_unlocked(String quiz_unlocked) {
        this.quiz_unlocked = quiz_unlocked;
    }
}
