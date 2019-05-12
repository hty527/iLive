package com.android.gift.bean;

import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2018/6/5
 * 加载列表通配
 */

public class ResultList<T> {

    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ResultList{" +
                "list=" + list +
                '}';
    }
}
