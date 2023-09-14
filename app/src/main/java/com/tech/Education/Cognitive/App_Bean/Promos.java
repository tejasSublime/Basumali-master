package com.tech.Education.Cognitive.App_Bean;

/**
 * Created by admin on 2/19/2018.
 */

public class Promos {

    public Promos(String promoid, String promo_category, String promo_title, String promo_description, String promo_video, String promo_fullvideo, String promo_audio, String promo_thumbnail, String play_console_id, String promo_status, String created_at) {
        this.promoid = promoid;
        this.promo_category = promo_category;
        this.promo_title = promo_title;
        this.promo_description = promo_description;
        this.promo_video = promo_video;
        this.promo_fullvideo = promo_fullvideo;
        this.promo_audio = promo_audio;
        this.promo_thumbnail = promo_thumbnail;
        this.play_console_id = play_console_id;
        this.promo_status = promo_status;
        this.created_at = created_at;
    }

    String promoid;
    String promo_category;
    String promo_title;
    String promo_description;
    String promo_video;
    String promo_fullvideo;
    String promo_audio;
    String promo_thumbnail;

    public String getPromoid() {
        return promoid;
    }

    public String getPromo_category() {
        return promo_category;
    }

    public String getPromo_title() {
        return promo_title;
    }

    public String getPromo_description() {
        return promo_description;
    }

    public String getPromo_video() {
        return promo_video;
    }

    public String getPromo_fullvideo() {
        return promo_fullvideo;
    }

    public String getPromo_audio() {
        return promo_audio;
    }

    public String getPromo_thumbnail() {
        return promo_thumbnail;
    }

    public String getPlay_console_id() {
        return play_console_id;
    }

    public String getPromo_status() {
        return promo_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    String play_console_id;
    String promo_status;
    String created_at;


}
