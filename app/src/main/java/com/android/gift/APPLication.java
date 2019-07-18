package com.android.gift;

import android.app.Application;
import com.android.gift.net.OkHttpUtils;
import com.android.gift.util.Logger;

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
        if(BuildConfig.FLAVOR.equals("ilivePublish")){
            Logger.IS_DEBUG=false;
            OkHttpUtils.DEBUG=false;
        }
    }
}