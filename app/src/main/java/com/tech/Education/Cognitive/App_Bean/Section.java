package com.tech.Education.Cognitive.App_Bean;

/**
 * Created by pratap.kesaboyina on 01-12-2015.
 */
public class Section {

    private String id;
    private String name;
    private String image;
    private String path;
    private String duration;
    private String description;
    private String category;

    public Section() {
    }

    public Section(String id, String name, String image, String path, String duration, String description, String category) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.path = path;
        this.duration = duration;
        this.description = description;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
