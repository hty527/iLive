package com.android.gift.util;

import android.content.Context;
import com.android.gift.bean.GiftItemInfo;
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
}