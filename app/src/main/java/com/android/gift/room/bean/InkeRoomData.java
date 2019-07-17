package com.android.gift.room.bean;

import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/17
 */

public class InkeRoomData {

    /**
     * dm_error : 0
     * end_text :
     * error_msg : 操作成功
     * expire_time : 40
     * full_refresh_interval : 60
     * global_tab_key :
     * group_refresh_interval : 40
     * has_more : 1
     * loadmore_max : 400
     * loadmore_threshod : 6
     * offset : 24
     */
    private int dm_error;
    private String end_text;
    private String error_msg;
    private int expire_time;
    private int full_refresh_interval;
    private String global_tab_key;
    private int group_refresh_interval;
    private int has_more;
    private int loadmore_max;
    private int loadmore_threshod;
    private int offset;
    private List<InkeRoomItem> cards;


    public int getDm_error() {
        return dm_error;
    }

    public void setDm_error(int dm_error) {
        this.dm_error = dm_error;
    }

    public String getEnd_text() {
        return end_text;
    }

    public void setEnd_text(String end_text) {
        this.end_text = end_text;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public int getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(int expire_time) {
        this.expire_time = expire_time;
    }

    public int getFull_refresh_interval() {
        return full_refresh_interval;
    }

    public void setFull_refresh_interval(int full_refresh_interval) {
        this.full_refresh_interval = full_refresh_interval;
    }

    public String getGlobal_tab_key() {
        return global_tab_key;
    }

    public void setGlobal_tab_key(String global_tab_key) {
        this.global_tab_key = global_tab_key;
    }

    public int getGroup_refresh_interval() {
        return group_refresh_interval;
    }

    public void setGroup_refresh_interval(int group_refresh_interval) {
        this.group_refresh_interval = group_refresh_interval;
    }

    public int getHas_more() {
        return has_more;
    }

    public void setHas_more(int has_more) {
        this.has_more = has_more;
    }

    public int getLoadmore_max() {
        return loadmore_max;
    }

    public void setLoadmore_max(int loadmore_max) {
        this.loadmore_max = loadmore_max;
    }

    public int getLoadmore_threshod() {
        return loadmore_threshod;
    }

    public void setLoadmore_threshod(int loadmore_threshod) {
        this.loadmore_threshod = loadmore_threshod;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<InkeRoomItem> getCards() {
        return cards;
    }

    public void setCards(List<InkeRoomItem> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "InkeRoomData{" +
                "dm_error=" + dm_error +
                ", end_text='" + end_text + '\'' +
                ", error_msg='" + error_msg + '\'' +
                ", expire_time=" + expire_time +
                ", full_refresh_interval=" + full_refresh_interval +
                ", global_tab_key='" + global_tab_key + '\'' +
                ", group_refresh_interval=" + group_refresh_interval +
                ", has_more=" + has_more +
                ", loadmore_max=" + loadmore_max +
                ", loadmore_threshod=" + loadmore_threshod +
                ", offset=" + offset +
                ", cards=" + cards +
                '}';
    }
}
