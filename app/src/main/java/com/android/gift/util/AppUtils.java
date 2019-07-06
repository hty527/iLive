package com.android.gift.util;

import android.content.Context;
import android.util.DisplayMetrics;

import com.android.gift.APPLication;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.room.bean.CustomMsgExtra;
import com.android.gift.room.bean.CustomMsgInfo;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by TinyHung@outlook.com
 * 2019/6/20
 */

public class AppUtils {

    /**
     * 对礼物进行分组包装
     * @param giftInfos  源数据
     * @param limitCount 每组封装的个数
     * @return
     */
    public static TreeMap<Integer, List<GiftItemInfo>> subGroupGift(List<GiftItemInfo> giftInfos, int limitCount) {
        if (null != giftInfos && giftInfos.size() > 0) {
            TreeMap<Integer, List<GiftItemInfo>> groupList = new TreeMap<>();
            int start = 1;//开始位置
            int end = limitCount;//结束位置
            int index = 0;
            boolean flag = true;//是否继续分组操作
            while (flag) {
                if (giftInfos.size() < start) {
                    break;
                }
                if (end > giftInfos.size()) {
                    end = giftInfos.size();
                }
                List<GiftItemInfo> newList = giftInfos.subList(start - 1, end);
                if (null != newList && newList.size() > 0) {
                    groupList.put(index, newList);
                    // 重新设置获取章节的位置参数
                    start += limitCount;
                    end += limitCount;
                    index++;
                } else {
                    break;
                }
            }
            return groupList;
        }
        return new TreeMap<>();
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return getScreenWidth(APPLication.getInstance().getApplicationContext());
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        return getScreenHeight(APPLication.getInstance().getApplicationContext());
    }

    /**
     * 将dp转换成px
     *
     * @param dp
     * @return
     */
    public static float dpToPx(float dp) {
        return dp * APPLication.getInstance().getApplicationContext().getResources().getDisplayMetrics().density;
    }

    /**
     * 将dp转换成px
     *
     * @param dp
     * @return
     */
    public static float dpToPx(Context context,float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPxInt(Context context,float dp) {
        return (int) (dpToPx(context,dp) + 0.5f);
    }


    public static int dpToPxInt(float dp) {
        return (int) (dpToPx(APPLication.getInstance().getApplicationContext(),dp) + 0.5f);
    }

    /**
     * 将px转换成dp
     *
     * @param px
     * @return
     */
    public static float pxToDp(float px) {
        return px / APPLication.getInstance().getApplicationContext().getResources().getDisplayMetrics().density;
    }

    /**
     * 将px转换成dp
     *
     * @param px
     * @return
     */
    public static float pxToDp(Context context,float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static int pxToDpInt(Context context,float px) {
        return (int) (pxToDp(context,px) + 0.5f);
    }

    /**
     * 将px值转换为sp值
     *
     * @param pxValue
     * @return
     */
    public static float pxToSp(Context context,float pxValue) {
        return pxValue / context.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * 将sp值转换为px值
     *
     * @param spValue
     * @return
     */
    public static float spToPx(Context context,float spValue) {
        return spValue * context.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * 获取屏幕密度
     */
    public static int getScreenDensity() {
        //初始化屏幕密度
        DisplayMetrics dm = APPLication.getInstance().getApplicationContext().getResources().getDisplayMetrics();
        return dm.densityDpi;
    }

    /**
     * 生成 min 到 max之间的随机数,包含 min max
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandomNum(int min, int max) {
        return min + (int) (Math.random() * max);
    }

    /**
     * 自定义消息封装
     * @param extra    参数
     * @param giftInfo 礼物信息
     * @return 要发送出去的消息
     */
    public static CustomMsgInfo packMessage(final CustomMsgExtra extra, GiftItemInfo giftInfo) {
        if (null == extra) return null;
        //发送人基本信息
        CustomMsgInfo customMsgInfo = new CustomMsgInfo(0);
        //消息类型
        List<String> cmds = new ArrayList<>();
        cmds.add(extra.getCmd());
        customMsgInfo.setCmd(cmds);
        customMsgInfo.setMsgContent(extra.getMsgContent());
        customMsgInfo.setTanmu(extra.isTanmu());
        //接收人基本信息
        customMsgInfo.setAccapUserID(extra.getAccapUserID());
        customMsgInfo.setAccapUserName(extra.getAccapUserName());
        customMsgInfo.setAccapUserHead(extra.getAccapUserHeader());
        //群信息
        customMsgInfo.setRoomid(extra.getRoomid());
        //封装礼物信息
        if (null != giftInfo) {
            customMsgInfo.setGift(giftInfo);
        }
        return customMsgInfo;
    }

    public static String formatWan(long no, boolean round) {
        if (round && no <= 10000) return String.valueOf(no);
        double n = (double) no / 10000;
        return changeDouble(n) + "万";
    }


    public static double changeDouble(Double dou) {
        try {
            NumberFormat nf = new DecimalFormat("0.0 ");
            dou = Double.parseDouble(nf.format(dou));
            return dou;
        }catch (RuntimeException e){

        }
        return dou;
    }


    /**
     * 获取状态栏高度
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpToPxInt(25f);
    }
}