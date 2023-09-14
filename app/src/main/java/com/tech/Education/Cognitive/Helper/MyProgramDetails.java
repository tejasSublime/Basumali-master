package com.tech.Education.Cognitive.Helper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bodacious on 29/1/18.
 */

public class MyProgramDetails implements Parcelable {
    private int id;
    private String name;
    private String imageURL;
    private String description;
    private String videoURL;
    private String audioURl;
    private boolean general;
    private boolean business;
    private boolean sales;

    public MyProgramDetails(int id, String name, String description, String imageURL, String videoURL, String audioURl, boolean general, boolean business, boolean sales) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.videoURL = videoURL;
        this.audioURl = audioURl;
        this.general = general;
        this.business = business;
        this.sales = sales;
    }

    protected MyProgramDetails(Parcel in) {
        id = in.readInt();
        name = in.readString();
        imageURL = in.readString();
        description = in.readString();
        videoURL = in.readString();
        audioURl = in.readString();
        general = in.readByte() != 0;
        business = in.readByte() != 0;
        sales = in.readByte() != 0;
    }

    public static final Creator<MyProgramDetails> CREATOR = new Creator<MyProgramDetails>() {
        @Override
        public MyProgramDetails createFromParcel(Parcel in) {
            return new MyProgramDetails(in);
        }

        @Override
        public MyProgramDetails[] newArray(int size) {
            return new MyProgramDetails[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getAudioURl() {
        return audioURl;
    }

    public void setAudioURl(String audioURl) {
        this.audioURl = audioURl;
    }

    public boolean isGeneral() {
        return general;
    }

    public void setGeneral(boolean general) {
        this.general = general;
    }

    public boolean isBusiness() {
        return business;
    }

    public void setBusiness(boolean business) {
        this.business = business;
    }

    public boolean isSales() {
        return sales;
    }

    public void setSales(boolean sales) {
        this.sales = sales;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(imageURL);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(audioURl);
        dest.writeByte((byte) (general ? 1 : 0));
        dest.writeByte((byte) (business ? 1 : 0));
        dest.writeByte((byte) (sales ? 1 : 0));
    }
}
