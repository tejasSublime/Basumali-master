package com.tech.Education.Cognitive.App_Bean;

/**
 * Created by admin on 2/1/2018.
 */

public class History {

    int id;
    int day;
    String  current_date;

    public History() {
    }

    public History(int id, int day, String current_date) {
        this.id = id;
        this.day = day;
        this.current_date = current_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }
}
