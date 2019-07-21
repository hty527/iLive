package com.android.gift.observer;

import java.util.Observable;

/**
 * TinyHung@Outlook.com
 * 2018/4/12.
 * 统一的观察者订阅中心
 */

public class SubjectObservable extends Observable{

    public SubjectObservable(){

    }

    public void updataSubjectObserivce(Object obj){
        setChanged();
        notifyObservers(obj);
    }
}
