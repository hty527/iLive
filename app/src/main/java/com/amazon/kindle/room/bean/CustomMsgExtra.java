package com.amazon.kindle.room.bean;

/**
 * TinyHung@Outlook.com
 * 2018/6/21
 * 自定义消息封装所需参数
 */

public class CustomMsgExtra {

    private String cmd;
    private String sendUserID;
    private String sendUserName;
    private String sendUserHeader;
    private int sendUserGradle;
    private int itemType;
    private String msgContent;
    private String accapUserID;
    private String accapUserName;
    private String accapUserHeader;
    private String groupid;
    private String roomid;
    private boolean isTanmu;
    private int totalPrice;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getSendUserID() {
        return sendUserID;
    }

    public void setSendUserID(String sendUserID) {
        this.sendUserID = sendUserID;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getSendUserHeader() {
        return sendUserHeader;
    }

    public void setSendUserHeader(String sendUserHeader) {
        this.sendUserHeader = sendUserHeader;
    }

    public int getSendUserGradle() {
        return sendUserGradle;
    }

    public void setSendUserGradle(int sendUserGradle) {
        this.sendUserGradle = sendUserGradle;
    }

    public String getAccapUserID() {
        return accapUserID;
    }

    public void setAccapUserID(String accapUserID) {
        this.accapUserID = accapUserID;
    }

    public String getAccapUserName() {
        return accapUserName;
    }

    public void setAccapUserName(String accapUserName) {
        this.accapUserName = accapUserName;
    }

    public String getAccapUserHeader() {
        return accapUserHeader;
    }

    public void setAccapUserHeader(String accapUserHeader) {
        this.accapUserHeader = accapUserHeader;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

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

    public boolean isTanmu() {
        return isTanmu;
    }

    public void setTanmu(boolean tanmu) {
        isTanmu = tanmu;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

}
