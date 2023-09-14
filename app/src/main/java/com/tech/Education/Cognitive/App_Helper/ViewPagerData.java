package com.tech.Education.Cognitive.App_Helper;

/**
 * Created by admin on 1/30/2018.
 */

public class ViewPagerData {

    String offerid;
    String offer_title;
    String offer_description;
    String offer_link;
    String offer_image;

    public ViewPagerData(String offerid, String offer_title, String offer_description, String offer_link, String offer_image) {
        this.offerid = offerid;
        this.offer_title = offer_title;
        this.offer_description = offer_description;
        this.offer_link = offer_link;
        this.offer_image = offer_image;
    }

    public String getOfferid() {
        return offerid;
    }

    public void setOfferid(String offerid) {
        this.offerid = offerid;
    }

    public String getOffer_title() {
        return offer_title;
    }

    public void setOffer_title(String offer_title) {
        this.offer_title = offer_title;
    }

    public String getOffer_description() {
        return offer_description;
    }

    public void setOffer_description(String offer_description) {
        this.offer_description = offer_description;
    }

    public String getOffer_link() {
        return offer_link;
    }

    public void setOffer_link(String offer_link) {
        this.offer_link = offer_link;
    }

    public String getOffer_image() {
        return offer_image;
    }

    public void setOffer_image(String offer_image) {
        this.offer_image = offer_image;
    }
}
