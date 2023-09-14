package com.tech.Education.Cognitive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SAMI on 4/10/2018.
 */

public class EventsModel {
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Event> data = null;

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

    public List<Event> getData() {
        return data;
    }

    public void setData(List<Event> data) {
        this.data = data;
    }


    public class Event {


        @SerializedName("eventid")
        @Expose
        private String eventid;
        @SerializedName("event_name")
        @Expose
        private String eventName;
        @SerializedName("event_description")
        @Expose
        private String eventDescription;
        @SerializedName("play_console_id")
        @Expose
        private String playConsoleId;
        @SerializedName("purchase_point")
        @Expose
        private String purchasePoint;
        @SerializedName("start_date")
        @Expose
        private String startDate;
        @SerializedName("end_date")
        @Expose
        private String endDate;
        @SerializedName("event_image")
        @Expose
        private String eventImage;
        @SerializedName("event_status")
        @Expose
        private String eventStatus;

        public String getEventid() {
            return eventid;
        }

        public void setEventid(String eventid) {
            this.eventid = eventid;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getEventDescription() {
            return eventDescription;
        }

        public void setEventDescription(String eventDescription) {
            this.eventDescription = eventDescription;
        }

        public String getPlayConsoleId() {
            return playConsoleId;
        }

        public void setPlayConsoleId(String playConsoleId) {
            this.playConsoleId = playConsoleId;
        }

        public String getPurchasePoint() {
            return purchasePoint;
        }

        public void setPurchasePoint(String purchasePoint) {
            this.purchasePoint = purchasePoint;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getEventImage() {
            return eventImage;
        }

        public void setEventImage(String eventImage) {
            this.eventImage = eventImage;
        }

        public String getEventStatus() {
            return eventStatus;
        }

        public void setEventStatus(String eventStatus) {
            this.eventStatus = eventStatus;
        }
    }
}