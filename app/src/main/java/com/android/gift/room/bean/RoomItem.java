package com.android.gift.room.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.gift.bean.UserInfo;
import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/10
 */

public class RoomItem implements Parcelable {

    //未知的
    public static final int ITEM_TYPE_UNKNOWN = 0;
    public static final int ITEM_TYPE_ROOM = 1;
    public static final int ITEM_TYPE_BANNER = 2;

    private int itemType;
    private String roomid;
    private String room_front;
    private String stream_url;
    //BANNER、封面 宽
    private String width;
    //BANNER、封面 高
    private String height;
    //主播信息
    private UserInfo anchor;
    //主播封面
    private List<ImageInfo> images;
    //广告
    private List<BannerInfo> banners;

    public RoomItem(){}

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getRoom_front() {
        return room_front;
    }

    public void setRoom_front(String room_front) {
        this.room_front = room_front;
    }

    public String getStream_url() {
        return stream_url;
    }

    public void setStream_url(String stream_url) {
        this.stream_url = stream_url;
    }

    public UserInfo getAnchor() {
        return anchor;
    }

    public void setAnchor(UserInfo anchor) {
        this.anchor = anchor;
    }

    public List<ImageInfo> getImages() {
        return images;
    }

    public void setImages(List<ImageInfo> images) {
        this.images = images;
    }


    public List<BannerInfo> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerInfo> banners) {
        this.banners = banners;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    protected RoomItem(Parcel in) {
        itemType = in.readInt();
        roomid = in.readString();
        room_front = in.readString();
        stream_url = in.readString();
        width = in.readString();
        height = in.readString();
        anchor = in.readParcelable(UserInfo.class.getClassLoader());
        images = in.createTypedArrayList(ImageInfo.CREATOR);
        banners = in.createTypedArrayList(BannerInfo.CREATOR);
    }

    public static final Creator<RoomItem> CREATOR = new Creator<RoomItem>() {
        @Override
        public RoomItem createFromParcel(Parcel in) {
            return new RoomItem(in);
        }

        @Override
        public RoomItem[] newArray(int size) {
            return new RoomItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemType);
        dest.writeString(roomid);
        dest.writeString(room_front);
        dest.writeString(stream_url);
        dest.writeString(width);
        dest.writeString(height);
        dest.writeParcelable(anchor, flags);
        dest.writeTypedList(images);
        dest.writeTypedList(banners);
    }

    @Override
    public String toString() {
        return "RoomItem{" +
                "itemType=" + itemType +
                ", roomid='" + roomid + '\'' +
                ", room_front='" + room_front + '\'' +
                ", stream_url='" + stream_url + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", anchor=" + anchor +
                ", images=" + images +
                ", banners=" + banners +
                '}';
    }
}