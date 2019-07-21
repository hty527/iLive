package com.android.gift.service.bean;

/**
 * hty_Yuye@Outlook.com
 * 2019/7/20
 */

public class BuildMessageInfo {

    private int totalSize;//总大小
    private int downloadSize;//当前下载的大小
    private String cmd;
    private int code;
    private String msg;

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(int downloadSize) {
        this.downloadSize = downloadSize;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
