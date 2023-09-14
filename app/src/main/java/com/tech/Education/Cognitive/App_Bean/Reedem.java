package com.tech.Education.Cognitive.App_Bean;

/**
 * Created by admin on 2/14/2018.
 */

public class Reedem {


    String id,title,des,coin,imgurl;


    public Reedem(String id, String title, String des, String coin, String imgurl) {
        this.id = id;
        this.title = title;
        this.des = des;
        this.coin = coin;
        this.imgurl = imgurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
