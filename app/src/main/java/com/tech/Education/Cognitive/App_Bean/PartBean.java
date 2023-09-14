package com.tech.Education.Cognitive.App_Bean;

/**
 * Created by admin on 2/2/2018.
 */

public class PartBean {

    String video_title,video_description,video_thumbnail,videospartid,vp_videoid,vp_path,vp_duration,vp_status;

    public PartBean(String video_title, String video_description, String video_thumbnail, String videospartid, String vp_videoid, String vp_path, String vp_duration, String vp_status) {
        this.video_title = video_title;
        this.video_description = video_description;
        this.video_thumbnail = video_thumbnail;
        this.videospartid = videospartid;
        this.vp_videoid = vp_videoid;
        this.vp_path = vp_path;
        this.vp_duration = vp_duration;
        this.vp_status = vp_status;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_description() {
        return video_description;
    }

    public void setVideo_description(String video_description) {
        this.video_description = video_description;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }

    public String getVideospartid() {
        return videospartid;
    }

    public void setVideospartid(String videospartid) {
        this.videospartid = videospartid;
    }

    public String getVp_videoid() {
        return vp_videoid;
    }

    public void setVp_videoid(String vp_videoid) {
        this.vp_videoid = vp_videoid;
    }

    public String getVp_path() {
        return vp_path;
    }

    public void setVp_path(String vp_path) {
        this.vp_path = vp_path;
    }

    public String getVp_duration() {
        return vp_duration;
    }

    public void setVp_duration(String vp_duration) {
        this.vp_duration = vp_duration;
    }

    public String getVp_status() {
        return vp_status;
    }

    public void setVp_status(String vp_status) {
        this.vp_status = vp_status;
    }
}
