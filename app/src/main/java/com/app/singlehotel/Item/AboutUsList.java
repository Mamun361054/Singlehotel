package com.app.singlehotel.Item;

import java.io.Serializable;

/**
 * Created by admin on 04-07-2017.
 */

public class AboutUsList implements Serializable {

    private String app_name,app_logo,app_version,app_author,app_contact,app_email,app_website,app_description,app_developed_by,app_privacy_policy,publisher_id, interstital_ad_id, interstital_ad_click, banner_ad_id;
    private boolean interstital_ad = false;
    private boolean banner_ad = false;

    public AboutUsList(String app_name, String app_logo, String app_version, String app_author, String app_contact, String app_email, String app_website, String app_description, String app_developed_by, String app_privacy_policy, String publisher_id, String interstital_ad_id, String interstital_ad_click, String banner_ad_id, boolean interstital_ad, boolean banner_ad) {
        this.app_name = app_name;
        this.app_logo = app_logo;
        this.app_version = app_version;
        this.app_author = app_author;
        this.app_contact = app_contact;
        this.app_email = app_email;
        this.app_website = app_website;
        this.app_description = app_description;
        this.app_developed_by = app_developed_by;
        this.app_privacy_policy = app_privacy_policy;
        this.publisher_id = publisher_id;
        this.interstital_ad_id = interstital_ad_id;
        this.interstital_ad_click = interstital_ad_click;
        this.banner_ad_id = banner_ad_id;
        this.interstital_ad = interstital_ad;
        this.banner_ad = banner_ad;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_logo() {
        return app_logo;
    }

    public void setApp_logo(String app_logo) {
        this.app_logo = app_logo;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getApp_author() {
        return app_author;
    }

    public void setApp_author(String app_author) {
        this.app_author = app_author;
    }

    public String getApp_contact() {
        return app_contact;
    }

    public void setApp_contact(String app_contact) {
        this.app_contact = app_contact;
    }

    public String getApp_email() {
        return app_email;
    }

    public void setApp_email(String app_email) {
        this.app_email = app_email;
    }

    public String getApp_website() {
        return app_website;
    }

    public void setApp_website(String app_website) {
        this.app_website = app_website;
    }

    public String getApp_description() {
        return app_description;
    }

    public void setApp_description(String app_description) {
        this.app_description = app_description;
    }

    public String getApp_developed_by() {
        return app_developed_by;
    }

    public void setApp_developed_by(String app_developed_by) {
        this.app_developed_by = app_developed_by;
    }

    public String getApp_privacy_policy() {
        return app_privacy_policy;
    }

    public void setApp_privacy_policy(String app_privacy_policy) {
        this.app_privacy_policy = app_privacy_policy;
    }

    public String getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(String publisher_id) {
        this.publisher_id = publisher_id;
    }

    public String getInterstital_ad_id() {
        return interstital_ad_id;
    }

    public void setInterstital_ad_id(String interstital_ad_id) {
        this.interstital_ad_id = interstital_ad_id;
    }

    public String getInterstital_ad_click() {
        return interstital_ad_click;
    }

    public void setInterstital_ad_click(String interstital_ad_click) {
        this.interstital_ad_click = interstital_ad_click;
    }

    public String getBanner_ad_id() {
        return banner_ad_id;
    }

    public void setBanner_ad_id(String banner_ad_id) {
        this.banner_ad_id = banner_ad_id;
    }

    public boolean isInterstital_ad() {
        return interstital_ad;
    }

    public void setInterstital_ad(boolean interstital_ad) {
        this.interstital_ad = interstital_ad;
    }

    public boolean isBanner_ad() {
        return banner_ad;
    }

    public void setBanner_ad(boolean banner_ad) {
        this.banner_ad = banner_ad;
    }
}
