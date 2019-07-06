package com.android.gift.room.bean;

import com.android.gift.bean.GiftItemInfo;
import com.android.gift.manager.UserManager;
import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2018/6/21
 * 直播间的自定义消息发送和接收封装
 */

public class CustomMsgInfo {

    private List<String> cmd;
    private String childCmd;
    private String sendUserID;
    private String sendUserName;
    private String sendUserHead;
    private String accapGroupID;//接收者群ID
    private String msgContent;
    private String accapUserID;
    private String accapUserName;
    private String accapUserHead;
    private String roomid;
    private boolean isTanmu;
    private long onlineNumer;
    //私有多媒体文件消息
    private GiftItemInfo gift;//礼物信息

    public CustomMsgInfo(){

    }

    /**
     * 构造方法
     * @param init
     */
    public CustomMsgInfo(int init){
        //初始构造自己的基本信息
        if(0==init){
            setSendUserName(UserManager.getInstance().getNickName());
            setSendUserHead(UserManager.getInstance().getAcatar());
            setSendUserID(UserManager.getInstance().getUserId());
        }
    }


    public List<String> getCmd() {
        return cmd;
    }

    public void setCmd(List<String> cmd) {
        this.cmd = cmd;
    }

    public String getChildCmd() {
        return childCmd;
    }

    public void setChildCmd(String childCmd) {
        this.childCmd = childCmd;
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

    public String getSendUserHead() {
        return sendUserHead;
    }

    public void setSendUserHead(String sendUserHead) {
        this.sendUserHead = sendUserHead;
    }

    public String getAccapGroupID() {
        return accapGroupID;
    }

    public void setAccapGroupID(String accapGroupID) {
        this.accapGroupID = accapGroupID;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
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

    public String getAccapUserHead() {
        return accapUserHead;
    }

    public void setAccapUserHead(String accapUserHead) {
        this.accapUserHead = accapUserHead;
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

    public long getOnlineNumer() {
        return onlineNumer;
    }

    public void setOnlineNumer(long onlineNumer) {
        this.onlineNumer = onlineNumer;
    }

    public GiftItemInfo getGift() {
        return gift;
    }

    public void setGift(GiftItemInfo gift) {
        this.gift = gift;
    }
}