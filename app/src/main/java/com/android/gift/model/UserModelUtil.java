package com.android.gift.model;

import android.graphics.drawable.Drawable;
import com.android.gift.APPLication;
import com.android.gift.R;
import java.util.HashMap;
import java.util.Map;

/**
 * TinyHung@outlook.com
 * 2017/5/20
 * 用户等级
 */
public class UserModelUtil {

    public static final String REGEX_NOTIFY="通知";
    public static final String REGEX_AUTH="官方";
   public static Map<Integer, Integer> sNums;
   public static Map<Integer, Integer> sSendNums;
   public static Map<Integer, Integer> sdrawNums;
   public static Map<String, Integer> sDrawables;

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
   }

    /**
     * 根据Key获取对应的资源文件
     * @param key
     * @return
     */
    public static Drawable getDrawable(String key) {
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
    public static int getNumCount(int num) {
        if(num<0|num>9) return 0;
        return sNums.get(num);
    }

    public static int giftSendNumFromat(int num) {
        if(num<0|num>9) return 0;
        return sSendNums.get(num);
    }

    /**
     * 中奖礼物数字
     * @param num
     * @return
     */
    public static int getDrawNumCount(int num) {
        if(num<0|num>9) return 0;
        return sdrawNums.get(num);
    }
}
