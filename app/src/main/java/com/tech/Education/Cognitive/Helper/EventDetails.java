package com.tech.Education.Cognitive.Helper;


import android.os.Parcel;
import android.os.Parcelable;

public class EventDetails implements Parcelable {
    private int id;
    private int position;
    private String eventName;
    private String eventPath;
    private String imagesName;

    public EventDetails(int id, int position, String eventName, String eventPath, String imagesName) {
        this.id = id;
        this.position = position;
        this.eventName = eventName;
        this.eventPath = eventPath;
        this.imagesName = imagesName;
    }

    protected EventDetails(Parcel in) {
        id = in.readInt();
        position = in.readInt();
        eventName = in.readString();
        eventPath = in.readString();
        imagesName = in.readString();
    }

    public static final Creator<EventDetails> CREATOR = new Creator<EventDetails>() {
        @Override
        public EventDetails createFromParcel(Parcel in) {
            return new EventDetails(in);
        }

        @Override
        public EventDetails[] newArray(int size) {
            return new EventDetails[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventPath() {
        return eventPath;
    }

    public void setEventPath(String eventPath) {
        this.eventPath = eventPath;
    }

    public String getImagesName() {
        return imagesName;
    }

    public void setImagesName(String imagesName) {
        this.imagesName = imagesName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(position);
        dest.writeString(eventName);
        dest.writeString(eventPath);
        dest.writeString(imagesName);
    }
}
