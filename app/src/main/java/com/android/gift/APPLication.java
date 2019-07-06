package com.android.gift;

import android.app.Application;

/**
 * TinyHung@outlook.com
 * 2019/7/5
 */

public class APPLication extends Application {

    private static APPLication mInstance;

    public static APPLication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = APPLication.this;
    }
}