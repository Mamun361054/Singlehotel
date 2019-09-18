package com.app.singlehotel.Item;

import java.io.Serializable;

public class RatingList implements Serializable {

    private String id, room_id, user_name, rate, dt_rate, message;

    public RatingList(String id, String room_id, String user_name, String rate, String dt_rate, String message) {
        this.id = id;
        this.room_id = room_id;
        this.user_name = user_name;
        this.rate = rate;
        this.dt_rate = dt_rate;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDt_rate() {
        return dt_rate;
    }

    public void setDt_rate(String dt_rate) {
        this.dt_rate = dt_rate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
