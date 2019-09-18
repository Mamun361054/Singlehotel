package com.app.singlehotel.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 08-08-2017.
 */

public class RoomList implements Serializable {

    private String id,room_name,room_image,room_image_thumb,room_description,room_rules,room_amenities,room_price,total_rate,rate_avg;
    private ArrayList<String> arrayImage;
    private List<RatingList> ratingLists;

    public RoomList(String id, String room_name, String room_image, String room_image_thumb, String room_description, String room_rules, String room_amenities, String room_price, String total_rate, String rate_avg, ArrayList<String> arrayImage, List<RatingList> ratingLists) {
        this.id = id;
        this.room_name = room_name;
        this.room_image = room_image;
        this.room_image_thumb = room_image_thumb;
        this.room_description = room_description;
        this.room_rules = room_rules;
        this.room_amenities = room_amenities;
        this.room_price = room_price;
        this.total_rate = total_rate;
        this.rate_avg = rate_avg;
        this.arrayImage = arrayImage;
        this.ratingLists = ratingLists;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getRoom_image() {
        return room_image;
    }

    public void setRoom_image(String room_image) {
        this.room_image = room_image;
    }

    public String getRoom_image_thumb() {
        return room_image_thumb;
    }

    public void setRoom_image_thumb(String room_image_thumb) {
        this.room_image_thumb = room_image_thumb;
    }

    public String getRoom_description() {
        return room_description;
    }

    public void setRoom_description(String room_description) {
        this.room_description = room_description;
    }

    public String getRoom_rules() {
        return room_rules;
    }

    public void setRoom_rules(String room_rules) {
        this.room_rules = room_rules;
    }

    public String getRoom_amenities() {
        return room_amenities;
    }

    public void setRoom_amenities(String room_amenities) {
        this.room_amenities = room_amenities;
    }

    public String getRoom_price() {
        return room_price;
    }

    public void setRoom_price(String room_price) {
        this.room_price = room_price;
    }

    public String getTotal_rate() {
        return total_rate;
    }

    public void setTotal_rate(String total_rate) {
        this.total_rate = total_rate;
    }

    public String getRate_avg() {
        return rate_avg;
    }

    public void setRate_avg(String rate_avg) {
        this.rate_avg = rate_avg;
    }

    public ArrayList<String> getArrayImage() {
        return arrayImage;
    }

    public void setArrayImage(ArrayList<String> arrayImage) {
        this.arrayImage = arrayImage;
    }

    public List<RatingList> getRatingLists() {
        return ratingLists;
    }

    public void setRatingLists(List<RatingList> ratingLists) {
        this.ratingLists = ratingLists;
    }
}
