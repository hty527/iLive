package com.android.gift.bean;

/**
 * Created by TinyHung@outlook.com
 * 2019/6/20
 * 用户信息
 */

public class UserInfo {

    private String userid;
    private String nickName;
    private String avatar;

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
}