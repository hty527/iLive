package com.amazon.kindle.room.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/12
 */

public class ImageInfo implements Parcelable{

    private String icon;
    private String icon_mini;
    private boolean selected;

    public ImageInfo(){

    }

    protected ImageInfo(Parcel in) {
        icon = in.readString();
        icon_mini = in.readString();
        selected = in.readByte() != 0;
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel in) {
            return new ImageInfo(in);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon_mini() {
        return icon_mini;
    }

    public void setIcon_mini(String icon_mini) {
        this.icon_mini = icon_mini;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(icon);
        dest.writeString(icon_mini);
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "icon='" + icon + '\'' +
                "icon_mini='" + icon_mini + '\'' +
                ", selected=" + selected +
                '}';
    }
}