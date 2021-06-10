package com.amazon.kindle.util;

import android.graphics.drawable.Drawable;
import com.amazon.kindle.APPLication;
import com.amazon.kindle.R;
import java.util.HashMap;
import java.util.Map;

/**
 * TinyHung@outlook.com
 * 2017/5/20
 * 用户等级
 */
public class UserModelUtil {

    private static volatile UserModelUtil mInstance;

    /**
     * 单例初始化
     * @return
     */
    public static synchronized UserModelUtil getInstance() {
        synchronized (UserModelUtil.class) {
            if (null == mInstance) {
                mInstance = new UserModelUtil();
            }
        }
        return mInstance;
    }


    public static final String REGEX_NOTIFY="通知";
    public static final String REGEX_AUTH="官方";
    public static Map<Integer, Integer> sNums;
    public static Map<Integer, Integer> sSendNums;
    public static Map<Integer, Integer> sdrawNums;
    public static Map<String, Integer> sDrawables;
    public static Map<Integer, Integer> sUserVipGradleFace;

    static {

       sNums = new HashMap<>();
       sNums.put(0,R.drawable.num0);
       sNums.put(1,R.drawable.num1);
       sNums.put(2,R.drawable.num2);
       sNums.put(3,R.drawable.num3);
       sNums.put(4,R.drawable.num4);
       sNums.put(5,R.drawable.num5);
       sNums.put(6,R.drawable.num6);
       sNums.put(7,R.drawable.num7);
       sNums.put(8,R.drawable.num8);
       sNums.put(9,R.drawable.num9);

       sdrawNums=new HashMap<>();
       sdrawNums.put(0,R.drawable.ic_draw_0);
       sdrawNums.put(1,R.drawable.ic_draw_1);
       sdrawNums.put(2,R.drawable.ic_draw_2);
       sdrawNums.put(3,R.drawable.ic_draw_3);
       sdrawNums.put(4,R.drawable.ic_draw_4);
       sdrawNums.put(5,R.drawable.ic_draw_5);
       sdrawNums.put(6,R.drawable.ic_draw_6);
       sdrawNums.put(7,R.drawable.ic_draw_7);
       sdrawNums.put(8,R.drawable.ic_draw_8);
       sdrawNums.put(9,R.drawable.ic_draw_9);

       sSendNums=new HashMap<>();
       sSendNums.put(0,R.drawable.gift_card_level_three_n_0);
       sSendNums.put(1,R.drawable.gift_card_level_three_n_1);
       sSendNums.put(2,R.drawable.gift_card_level_three_n_2);
       sSendNums.put(3,R.drawable.gift_card_level_three_n_3);
       sSendNums.put(4,R.drawable.gift_card_level_three_n_4);
       sSendNums.put(5,R.drawable.gift_card_level_three_n_5);
       sSendNums.put(6,R.drawable.gift_card_level_three_n_6);
       sSendNums.put(7,R.drawable.gift_card_level_three_n_7);
       sSendNums.put(8,R.drawable.gift_card_level_three_n_8);
       sSendNums.put(9,R.drawable.gift_card_level_three_n_9);

       sUserVipGradleFace = new HashMap<>();
       sUserVipGradleFace.put(1, R.drawable.vip_gradle1);
       sUserVipGradleFace.put(2, R.drawable.vip_gradle2);
       sUserVipGradleFace.put(3, R.drawable.vip_gradle3);
       sUserVipGradleFace.put(4, R.drawable.vip_gradle4);
       sUserVipGradleFace.put(5, R.drawable.vip_gradle5);
       sUserVipGradleFace.put(6, R.drawable.vip_gradle6);
       sUserVipGradleFace.put(7, R.drawable.vip_gradle7);
       sUserVipGradleFace.put(8, R.drawable.vip_gradle8);
       sUserVipGradleFace.put(9, R.drawable.vip_gradle9);
       sUserVipGradleFace.put(10, R.drawable.vip_gradle10);
       sUserVipGradleFace.put(11, R.drawable.vip_gradle11);
       sUserVipGradleFace.put(12, R.drawable.vip_gradle12);
       sUserVipGradleFace.put(13, R.drawable.vip_gradle13);
       sUserVipGradleFace.put(14, R.drawable.vip_gradle14);
       sUserVipGradleFace.put(15, R.drawable.vip_gradle15);
       sUserVipGradleFace.put(16, R.drawable.vip_gradle16);
       sUserVipGradleFace.put(17, R.drawable.vip_gradle17);
       sUserVipGradleFace.put(18, R.drawable.vip_gradle18);
       sUserVipGradleFace.put(19, R.drawable.vip_gradle19);
       sUserVipGradleFace.put(20, R.drawable.vip_gradle20);
       sUserVipGradleFace.put(21, R.drawable.vip_gradle21);
       sUserVipGradleFace.put(22, R.drawable.vip_gradle22);
       sUserVipGradleFace.put(23, R.drawable.vip_gradle23);
       sUserVipGradleFace.put(24, R.drawable.vip_gradle24);
       sUserVipGradleFace.put(25, R.drawable.vip_gradle25);
       sUserVipGradleFace.put(26, R.drawable.vip_gradle26);
       sUserVipGradleFace.put(27, R.drawable.vip_gradle27);
       sUserVipGradleFace.put(28, R.drawable.vip_gradle28);
       sUserVipGradleFace.put(29, R.drawable.vip_gradle29);
       sUserVipGradleFace.put(30, R.drawable.vip_gradle30);
       sUserVipGradleFace.put(31, R.drawable.vip_gradle31);
       sUserVipGradleFace.put(32, R.drawable.vip_gradle32);
       sUserVipGradleFace.put(33, R.drawable.vip_gradle33);
       sUserVipGradleFace.put(34, R.drawable.vip_gradle34);
       sUserVipGradleFace.put(35, R.drawable.vip_gradle35);
       sUserVipGradleFace.put(36, R.drawable.vip_gradle36);
       sUserVipGradleFace.put(37, R.drawable.vip_gradle37);
       sUserVipGradleFace.put(38, R.drawable.vip_gradle38);
       sUserVipGradleFace.put(39, R.drawable.vip_gradle39);
       sUserVipGradleFace.put(40, R.drawable.vip_gradle40);
       sUserVipGradleFace.put(41, R.drawable.vip_gradle41);
       sUserVipGradleFace.put(42, R.drawable.vip_gradle42);
       sUserVipGradleFace.put(43, R.drawable.vip_gradle43);
       sUserVipGradleFace.put(44, R.drawable.vip_gradle44);
       sUserVipGradleFace.put(45, R.drawable.vip_gradle45);
       sUserVipGradleFace.put(46, R.drawable.vip_gradle46);
       sUserVipGradleFace.put(47, R.drawable.vip_gradle47);
       sUserVipGradleFace.put(48, R.drawable.vip_gradle48);
       sUserVipGradleFace.put(49, R.drawable.vip_gradle49);
       sUserVipGradleFace.put(50, R.drawable.vip_gradle50);
   }

    /**
     * 根据Key获取对应的资源文件
     * @param key
     * @return
     */
    public Drawable getDrawable(String key) {
        Integer integer = sDrawables.get(key);
        if(null!=integer){
            Drawable drawable = APPLication.getInstance().getApplicationContext().getResources().getDrawable(integer);
            return drawable;
        }
        return null;
    }

    /**
     * 赠送礼物数字
     * @param num
     * @return
     */
    public int getNumCount(int num) {
        if(num<0|num>9) return 0;
        return sNums.get(num);
    }

    public int giftSendNumFromat(int num) {
        if(num<0|num>9) return 0;
        return sSendNums.get(num);
    }

    /**
     * 根据用户会员等级获取对应资源ID
     * @param gradle
     * @return
     */
    public int getUserGradleRes(int gradle) {
        if(gradle<1) return R.drawable.vip_gradle1;
        if(gradle>50) return sUserVipGradleFace.get(50);
        return sUserVipGradleFace.get(gradle);
    }

    /**
     * 中奖礼物数字
     * @param num
     * @return
     */
    public int getDrawNumCount(int num) {
        if(num<0|num>9) return 0;
        return sdrawNums.get(num);
    }
}
