package com.android.gift.room.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/12
 */

public class BannerInfo implements Parcelable{

    private String icon;
    private String jump_url;

    protected BannerInfo(Parcel in) {
        icon = in.readString();
        jump_url = in.readString();
    }

    public static final Creator<BannerInfo> CREATOR = new Creator<BannerInfo>() {
        @Override
        public BannerInfo createFromParcel(Parcel in) {
            return new BannerInfo(in);
        }

        @Override
        public BannerInfo[] newArray(int size) {
            return new BannerInfo[size];
        }
    };

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(icon);
        dest.writeString(jump_url);
    }

    @Override
    public String toString() {
        return "BannerInfo{" +
                "icon='" + icon + '\'' +
                ", jump_url='" + jump_url + '\'' +
                '}';
    }
}