package com.android.gift.room.bean;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/18
 */

public class InkeWebItem {

    private String image;
    private String link;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "InkeWebItem{" +
                "image='" + image + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}