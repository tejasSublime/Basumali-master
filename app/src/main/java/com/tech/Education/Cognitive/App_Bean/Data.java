package com.tech.Education.Cognitive.App_Bean;

import java.util.List;

/**
 * Created by pratap.kesaboyina on 30-11-2015.
 */
public class Data {



    private String title;
    private List<Section> section;


    public Data() {

    }
    public Data(String title, List<Section> section) {
        this.title = title;
        this.section = section;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Section> getSection() {
        return section;
    }

    public void setSection(List<Section> section) {
        this.section = section;
    }


}
