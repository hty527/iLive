package com.android.live.player.lib.utils;

import android.util.Log;

/**
 * TinyHung@outlook.com
 * 2017/6/19 9:28
 */

public class Logger {

    private static final boolean IS_DEBUG = true;

    public static void pd(String TAG, String message){
        if(IS_DEBUG){
            Log.println(Log.DEBUG,TAG,message);
        }
    }

    public static void pe(String TAG, String message){
        if(IS_DEBUG){
            Log.println(Log.ERROR,TAG,message);
        }
    }

    public static void pw(String TAG, String message){
        if(IS_DEBUG){
            Log.println(Log.WARN,TAG,message);
        }
    }

    public static void pi(String TAG, String message){
        if(IS_DEBUG){
            Log.println(Log.INFO,TAG,message);
        }
    }
    public static void d(String TAG, String message) {
        if(IS_DEBUG){
            Log.d(TAG,message);
        }
    }

    public static void e(String TAG, String message) {
        if(IS_DEBUG){
            Log.e(TAG,message);
        }
    }

    public static void v(String TAG, String message) {
        if(IS_DEBUG){
            Log.e(TAG,message);
        }
    }

    public static void w(String TAG, String message) {
        if(IS_DEBUG){
            Log.w(TAG,message);
        }
    }

    public static void i(String TAG, String message) {
        if(IS_DEBUG){
            Log.i(TAG,message);
        }
    }
}
