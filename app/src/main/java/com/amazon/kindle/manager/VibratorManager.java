package com.amazon.kindle.manager;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/18
 * 震动
 */

public class VibratorManager {

    private static volatile VibratorManager mInstance;
    private Vibrator mVibrator;

    public static synchronized VibratorManager getInstance() {
        synchronized (VibratorManager.class) {
            if (null == mInstance) {
                mInstance = new VibratorManager();
            }
        }
        return mInstance;
    }

    /**
     * 开始震动
     * @param context 全局上下文
     * @param milliss 时长
     */
    public void vibrate(Context context,long milliss){
        if(null==mVibrator&&null!=context){
            mVibrator = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        }
        if(null!=mVibrator){
            mVibrator.vibrate(milliss);
        }
    }

    public void onReset(){
        if(null!=mVibrator){
            mVibrator.cancel();
        }
    }

    public void onDestroy(){
        if(null!=mVibrator){
            mVibrator.cancel();
            mVibrator=null;
        }
    }
}