package com.amazon.kindle.gift.bean;

/**
 * hty_Yuye@Outlook.com
 * 2019/5/12
 * 礼物类型
 */

public class GiftType {

    /**
     * addtime : 1530260031
     * id : 1
     * name : hot
     * title : 热门
     */

    private int addtime;
    private int id;
    private String name;
    private String title;

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "GiftType{" +
                "addtime=" + addtime +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}