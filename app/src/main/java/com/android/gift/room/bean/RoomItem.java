package com.android.gift.room.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.gift.bean.UserInfo;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/10
 */

public class RoomItem implements Parcelable {

    private String roomid;
    private String room_front;
    private String stream_url;
    private String stream_width;
    private String stream_height;
    private UserInfo anchor;

    public RoomItem(){

    }

    protected RoomItem(Parcel in) {
        roomid = in.readString();
        room_front = in.readString();
        stream_url = in.readString();
        stream_width = in.readString();
        stream_height = in.readString();
        anchor = in.readParcelable(UserInfo.class.getClassLoader());
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

    public String getStream_width() {
        return stream_width;
    }

    public void setStream_width(String stream_width) {
        this.stream_width = stream_width;
    }

    public String getStream_height() {
        return stream_height;
    }

    public void setStream_height(String stream_height) {
        this.stream_height = stream_height;
    }

    public UserInfo getAnchor() {
        return anchor;
    }

    public void setAnchor(UserInfo anchor) {
        this.anchor = anchor;
    }

    @Override
    public String toString() {
        return "RoomItem{" +
                "roomid='" + roomid + '\'' +
                ", room_front='" + room_front + '\'' +
                ", stream_url='" + stream_url + '\'' +
                ", stream_width='" + stream_width + '\'' +
                ", stream_height='" + stream_height + '\'' +
                ", anchor=" + anchor +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roomid);
        dest.writeString(room_front);
        dest.writeString(stream_url);
        dest.writeString(stream_width);
        dest.writeString(stream_height);
        dest.writeParcelable(anchor, flags);
    }
}