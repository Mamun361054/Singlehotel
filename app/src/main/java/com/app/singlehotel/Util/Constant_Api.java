package com.app.singlehotel.Util;

import com.app.singlehotel.BuildConfig;
import com.app.singlehotel.Item.AboutUsList;
import com.app.singlehotel.Item.RoomList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 08-08-2017.
 */

public class Constant_Api {

    //main server api url
    public static String url = BuildConfig.My_api;

    //main server api tag
    public static String tag = "SINGLE_HOTEL_APP";

    public static String map_api = "AIzaSyDqJZDEt2DHp6Nm0aGtAIgTzoaFxVh1mag";

    //app image folder
    public static String image = url + "images/";

    //app detail
    public static String app_detail = url + "api.php";

    //App login
    public static String login = url + "api.php?users_login";

    //App booking
    public static String register = url + "api.php?user_register";

    //App forget password
    public static String forgetPassword = url + "api.php?forgot_pass&email=";

    //App profile
    public static String profile = url + "api.php?user_profile&id=";

    //App profile update
    public static String profileUpdate = url + "api.php?user_profile_update&user_id=";

    //Home url
    public static String home = url + "api.php?home";

    //Room url
    public static String room = url + "api.php?room_list";

    //Room Detail url
    public static String roomDetail = url + "api.php?room_id=";

    //Rating url
    public static String rating = url + "api_rating.php?room_id=";

    //Gallery url
    public static String gallery = url + "api.php?cat_list";

    //Gallery Detail url
    public static String galleryDetail = url + "api.php?cat_id=";

    //Hotel info url
    public static String hotel_info = url + "api.php?hotel_info";

    //contactUS url
    public static String contact_us = url + "api_contact.php?";

    //Booking url
    public static String booking = url + "api_booking.php?name=";

    //Room list
    public static List<RoomList> roomLists = new ArrayList<>();

    //AboutUs data
    public static AboutUsList aboutUsList;

    public static int AD_COUNT = 0;
    public static int AD_COUNT_SHOW = 0;

}
