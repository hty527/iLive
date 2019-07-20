package com.android.gift;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
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

    private void setStrictPow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }
}