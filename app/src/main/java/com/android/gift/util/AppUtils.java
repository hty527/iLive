package com.android.gift.util;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;
import com.android.gift.APPLication;
import com.android.gift.BuildConfig;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.room.bean.BannerInfo;
import com.android.gift.room.bean.CustomMsgExtra;
import com.android.gift.room.bean.CustomMsgInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

    private static volatile AppUtils mInstance;

    /**
     * 单例初始化
     * @return
     */
    public static synchronized AppUtils getInstance() {
        synchronized (AppUtils.class) {
            if (null == mInstance) {
                mInstance = new AppUtils();
            }
        }
        return mInstance;
    }

    /**
     * 对礼物进行分组包装
     * @param giftInfos  源数据
     * @param limitCount 每组封装的个数
     * @return
     */
    public TreeMap<Integer, List<GiftItemInfo>> subGroupGift(List<GiftItemInfo> giftInfos, int limitCount) {
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
    public int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getScreenWidth() {
        return getScreenWidth(APPLication.getInstance().getApplicationContext());
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public int getScreenHeight() {
        return getScreenHeight(APPLication.getInstance().getApplicationContext());
    }

    /**
     * 将dp转换成px
     *
     * @param dp
     * @return
     */
    public float dpToPx(float dp) {
        return dp * APPLication.getInstance().getApplicationContext().getResources().getDisplayMetrics().density;
    }

    /**
     * 将dp转换成px
     *
     * @param dp
     * @return
     */
    public float dpToPx(Context context,float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public int dpToPxInt(Context context,float dp) {
        return (int) (dpToPx(context,dp) + 0.5f);
    }


    public int dpToPxInt(float dp) {
        return (int) (dpToPx(APPLication.getInstance().getApplicationContext(),dp) + 0.5f);
    }

    /**
     * 将px转换成dp
     *
     * @param px
     * @return
     */
    public float pxToDp(float px) {
        return px / APPLication.getInstance().getApplicationContext().getResources().getDisplayMetrics().density;
    }

    /**
     * 将px转换成dp
     *
     * @param px
     * @return
     */
    public float pxToDp(Context context,float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public int pxToDpInt(Context context,float px) {
        return (int) (pxToDp(context,px) + 0.5f);
    }

    /**
     * 将px值转换为sp值
     *
     * @param pxValue
     * @return
     */
    public float pxToSp(Context context,float pxValue) {
        return pxValue / context.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * 将sp值转换为px值
     *
     * @param spValue
     * @return
     */
    public float spToPx(Context context,float spValue) {
        return spValue * context.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * 获取屏幕密度
     */
    public int getScreenDensity() {
        //初始化屏幕密度
        DisplayMetrics dm = APPLication.getInstance().getApplicationContext().getResources().getDisplayMetrics();
        return dm.densityDpi;
    }

    public int dp2pxConvertInt(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public float sp2px(Context context, float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 生成 min 到 max之间的随机数,包含 min max
     *
     * @param min
     * @param max
     * @return
     */
    public int getRandomNum(int min, int max) {
        return min + (int) (Math.random() * max);
    }

    /**
     * 自定义消息封装
     * @param extra    参数
     * @param giftInfo 礼物信息
     * @return 要发送出去的消息
     */
    public CustomMsgInfo packMessage(final CustomMsgExtra extra, GiftItemInfo giftInfo) {
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

    public String formatWan(long no, boolean round) {
        if (round && no <= 10000) return String.valueOf(no);
        double n = (double) no / 10000;
        return changeDouble(n) + "万";
    }


    public double changeDouble(Double dou) {
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
    public int getStatusBarHeight(Context context) {
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

    /**
     * 分割字符串
     * @param content
     * @param count
     * @return
     */
    public String subString(String content, int count) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        if (content.length() <= count) {
            return content;
        }
        return content.substring(0, count);
    }

    /**
     * 获取内部版本号
     * @return
     */
    public int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    /**
     * 获取版本号
     * @return
     */
    public String getVersion() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     * 初始化一组广告Banners
     * @return
     */
    public List<BannerInfo> createBanners() {
        List<BannerInfo> bannerInfos=new ArrayList<>();
        String content="[\n" +
                "\t\t  {\n" +
                "            \"icon\":\"http://sta-op.douyucdn.cn/douyu-vrp-admin/2019/04/17/e63c5563968b8438b6f70e1c6a309cf5.jpg\",\n" +
                "            \"jump_url\":\"\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"icon\":\"http://sta-op.douyucdn.cn/adxdsp/2019/06/11/04c6f32c57c55b8a2ced35d857836ce2.jpg\",\n" +
                "            \"jump_url\":\"\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"icon\":\"https://sta-op.douyucdn.cn/adxdsp/2019/07/16/0fc0911ce91a76d0a52eb23a1ef45cc4.jpg\",\n" +
                "            \"jump_url\":\"\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"icon\":\"http://sta-op.douyucdn.cn/adxdsp/2019/05/21/5f6137a80f939cec0da616b36b99def4.jpg\",\n" +
                "            \"jump_url\":\"\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"icon\":\"http://sta-op.douyucdn.cn/adxdsp/2019/04/29/51d2b4c2a1c2421ee3dbd835c9cd4ef8.jpg\",\n" +
                "            \"jump_url\":\"\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"icon\":\"https://sta-op.douyucdn.cn/adxdsp/2019/07/15/5252676b15701857cf6691f110cf4721.jpg\",\n" +
                "            \"jump_url\":\"\"\n" +
                "          }\n" +
                "        ]";
        List<BannerInfo> infoList = new Gson().fromJson(content, new TypeToken<List<BannerInfo>>() {}.getType());
        return infoList;
    }

    /**
     * 将映客的直播流地址后面的参数去掉
     * @param streamUrl
     * @return
     */
    public String formatRoomStream(String streamUrl) {
        if(!TextUtils.isEmpty(streamUrl)){
            int endIndex = streamUrl.indexOf("?");
            return streamUrl.substring(0,endIndex);
        }
        return null;
    }

    /**
     * 复制字符串到粘贴板
     * @param content
     */
    public void copyString(String content) {
        if(!TextUtils.isEmpty(content)){
            try {
                Context context = APPLication.getInstance().getApplicationContext();
                ClipboardManager cm = (ClipboardManager) APPLication.getInstance().getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(content);
                Toast.makeText(context,"已复制到粘贴板",Toast.LENGTH_SHORT).show();
            }catch (RuntimeException e){
                e.printStackTrace();
            }
        }
    }
}