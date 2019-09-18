package com.app.singlehotel.Item;

import java.io.Serializable;

/**
 * Created by admin on 09-08-2017.
 */

public class GalleryDetailList implements Serializable {

    private String id,cat_id,wallpaper_image,wallpaper_image_thumb,category_name;

    public GalleryDetailList(String id, String cat_id, String wallpaper_image, String wallpaper_image_thumb, String category_name) {
        this.id = id;
        this.cat_id = cat_id;
        this.wallpaper_image = wallpaper_image;
        this.wallpaper_image_thumb = wallpaper_image_thumb;
        this.category_name = category_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getWallpaper_image() {
        return wallpaper_image;
    }

    public void setWallpaper_image(String wallpaper_image) {
        this.wallpaper_image = wallpaper_image;
    }

    public String getWallpaper_image_thumb() {
        return wallpaper_image_thumb;
    }

    public void setWallpaper_image_thumb(String wallpaper_image_thumb) {
        this.wallpaper_image_thumb = wallpaper_image_thumb;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
