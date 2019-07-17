package com.android.gift.room.bean;

import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/17
 */

public class InkeChannel {

    private List<LiveRoomInfo> cards;
    private String name;

    public List<LiveRoomInfo> getCards() {
        return cards;
    }

    public void setCards(List<LiveRoomInfo> cards) {
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}