package com.app.singlehotel.Item;

import java.io.Serializable;

/**
 * Created by admin on 09-08-2017.
 */

public class HotelInfoList implements Serializable {

    private String hotel_name,hotel_address,hotel_lat,hotel_long,hotel_info,hotel_amenities;

    public HotelInfoList(String hotel_name, String hotel_address, String hotel_lat, String hotel_long, String hotel_info, String hotel_amenities) {
        this.hotel_name = hotel_name;
        this.hotel_address = hotel_address;
        this.hotel_lat = hotel_lat;
        this.hotel_long = hotel_long;
        this.hotel_info = hotel_info;
        this.hotel_amenities = hotel_amenities;
    }

    public HotelInfoList(String hotel_name, String hotel_lat, String hotel_long) {
        this.hotel_name = hotel_name;
        this.hotel_lat = hotel_lat;
        this.hotel_long = hotel_long;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public String getHotel_address() {
        return hotel_address;
    }

    public void setHotel_address(String hotel_address) {
        this.hotel_address = hotel_address;
    }

    public String getHotel_lat() {
        return hotel_lat;
    }

    public void setHotel_lat(String hotel_lat) {
        this.hotel_lat = hotel_lat;
    }

    public String getHotel_long() {
        return hotel_long;
    }

    public void setHotel_long(String hotel_long) {
        this.hotel_long = hotel_long;
    }

    public String getHotel_info() {
        return hotel_info;
    }

    public void setHotel_info(String hotel_info) {
        this.hotel_info = hotel_info;
    }

    public String getHotel_amenities() {
        return hotel_amenities;
    }

    public void setHotel_amenities(String hotel_amenities) {
        this.hotel_amenities = hotel_amenities;
    }

}
