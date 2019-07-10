package com.android.gift.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TinyHung@outlook.com
 * 2019/6/20
 * 用户信息
 */

public class UserInfo implements Parcelable{

    private String userid;
    private String nickName;
    private String avatar;
    //默认男生，1：女生
    private int userSex;

    public UserInfo(){

    }


    protected UserInfo(Parcel in) {
        userid = in.readString();
        nickName = in.readString();
        avatar = in.readString();
        userSex = in.readInt();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userid='" + userid + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", userSex=" + userSex +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeString(nickName);
        dest.writeString(avatar);
        dest.writeInt(userSex);
    }
}