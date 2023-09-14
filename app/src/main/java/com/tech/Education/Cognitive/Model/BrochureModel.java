package com.tech.Education.Cognitive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SAMI on 4/10/2018.
 */

public class BrochureModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Brochure> data = null;

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

    public List<Brochure> getData() {
        return data;
    }

    public void setData(List<Brochure> data) {
        this.data = data;
    }

    public class Brochure {

        @SerializedName("brochuresid")
        @Expose
        private String brochuresid;
        @SerializedName("brochures_title")
        @Expose
        private String brochuresTitle;
        @SerializedName("brochures_description")
        @Expose
        private String brochuresDescription;
        @SerializedName("brochures_status")
        @Expose
        private String brochuresStatus;
        @SerializedName("brochures_image")
        @Expose
        private String brochuresImage;

        public String getBrochuresid() {
            return brochuresid;
        }

        public void setBrochuresid(String brochuresid) {
            this.brochuresid = brochuresid;
        }

        public String getBrochuresTitle() {
            return brochuresTitle;
        }

        public void setBrochuresTitle(String brochuresTitle) {
            this.brochuresTitle = brochuresTitle;
        }

        public String getBrochuresDescription() {
            return brochuresDescription;
        }

        public void setBrochuresDescription(String brochuresDescription) {
            this.brochuresDescription = brochuresDescription;
        }

        public String getBrochuresStatus() {
            return brochuresStatus;
        }

        public void setBrochuresStatus(String brochuresStatus) {
            this.brochuresStatus = brochuresStatus;
        }

        public String getBrochuresImage() {
            return brochuresImage;
        }

        public void setBrochuresImage(String brochuresImage) {
            this.brochuresImage = brochuresImage;
        }

    }
}