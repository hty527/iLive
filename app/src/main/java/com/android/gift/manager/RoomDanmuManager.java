package com.android.gift.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import com.android.gift.APPLication;
import com.android.gift.R;
import com.android.gift.room.bean.CustomMsgInfo;
import com.android.gift.util.AppUtils;
import com.android.gift.view.danmaku.CircleDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

/**
 * 直播间飘屏弹幕播放管理
 * TinyHung@Outlook.com
 * 2018/12/15
 * BackgroundCacheStuffer 用于定制弹幕消息背景
 * 参考：https://github.com/wangpeiyuan/DanmuDemo，https://github.com/Bilibili/DanmakuFlameMaster
 */
public class RoomDanmuManager {

    private static final String TAG = "RoomDanmuManager";
    //自定义富文本正则解析协议
    private static final String VIP = "#VIP#[\u4e00-\u9fa5\\w]+#VIP#";//会员

    private static final String GIFT = "#GIFT_ICON#[\u4e00-\u9fa5\\w]+#GIFT_ICON#";//GIFT

    private static String REGEX = "(" + VIP + ")|(" + GIFT + ")";

    private static final long ADD_DANMU_TIME = 500;
    private int CONTENT_COLOR = 0;//文本文字颜色
    private int   BITMAP_WIDTH    = 28;//ICON的大小
    private int   BITMAP_HEIGHT   = 28;
    private float DANMU_TEXT_SIZE = 11f;//弹幕字体的大小
    //这两个用来控制两行弹幕之间的间距
    private int DANMU_PADDING       = 10;
    private int DANMU_PADDING_INNER = 7;
    private int DANMU_RADIUS        = 0;//圆角半径
    private Context mContext;
    //飘屏View
    private IDanmakuView mDanmakuView;
    private DanmakuContext mDanmakuContext;
    private HandlerThread mDanmuThread;
    private Handler mDanmuHandler;

    /**
     * 弹幕类型
     */
    public enum DanmuType{
        GIFT,
        AWARD,
        TEXT
    }

    public RoomDanmuManager(Context context) {
        this.mContext = context;
        setSize(context);
        initDanmuConfig();
        mDanmuThread = new HandlerThread("RoomDanmuThread");
        mDanmuThread.start();
        mDanmuHandler = new Handler(mDanmuThread.getLooper());
        CONTENT_COLOR = Color.parseColor("#FFFFFF");
    }

    /**
     * 初始化配置
     */
    private void initDanmuConfig() {
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 1); // 滚动弹幕最大显示1行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        mDanmakuContext = DanmakuContext.create();
        mDanmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_NONE)
                .setDuplicateMergingEnabled(true)
                .setScrollSpeedFactor(2.0f)//越大速度越慢
                .setScaleTextSize(1.2f)
                .setCacheStuffer(new BackgroundCacheStuffer(), mCacheStufferAdapter)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);
    }

    /**
     * 绑定弹幕view
     *
     * @param danmakuView 弹幕view
     */
    public void bindDanmakuView(IDanmakuView danmakuView) {
        this.mDanmakuView = danmakuView;
        initDanmuView();
    }

    /**
     * 弹幕监听初始化
     */
    private void initDanmuView() {
        if (null!=mDanmakuContext&&null!=mDanmakuView) {
            mDanmakuView.setCallback(new DrawHandler.Callback() {
                @Override
                public void prepared() {
                    if(null!=mDanmakuView) mDanmakuView.start();
                }

                @Override
                public void updateTimer(DanmakuTimer timer) {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {

                }

                @Override
                public void drawingFinished() {

                }
            });
            mDanmakuView.prepare(new BaseDanmakuParser() {
                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            }, mDanmakuContext);
            mDanmakuView.enableDanmakuDrawingCache(true);
        }
    }

    /**
     * 弹幕渲染恢复
     */
    public void onResume() {
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    /**
     * 弹幕渲染暂停
     */
    public void onPause() {
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    /**
     * 弹幕显示
     */
    public void show() {
        if (mDanmakuView != null) {
            mDanmakuView.show();
        }
    }

    /**
     * 弹幕隐藏
     */
    public void hide() {
        if (mDanmakuView != null) {
            mDanmakuView.hide();
        }
    }

    /**
     * 添加一条弹幕
     * @param customMsgInfo 直播间消息相关 必传
     * @param danmuType 弹幕类型
     */
    public void addRoomDanmu(CustomMsgInfo customMsgInfo, DanmuType danmuType) {
        addRoomDanmu(customMsgInfo,danmuType,CONTENT_COLOR);
    }

    /**
     * 添加一条弹幕
     * @param customMsgInfo 直播间消息相关 必传
     * @param danmuType 弹幕类型
     * @param textColor 指定文本颜色
     */
    public void addRoomDanmu(final CustomMsgInfo customMsgInfo, final DanmuType danmuType, final int textColor) {
        if (mDanmuHandler != null) {
            mDanmuHandler.post(new Runnable() {
                @Override
                public void run() {
                    addGiftDanmuInternal(customMsgInfo,danmuType,textColor);
                }
            });
        }
    }


    /**
     * 对数值进行转换，适配手机，必须在初始化之前，否则有些数据不会起作用
     */
    private void setSize(Context context) {
        BITMAP_WIDTH = AppUtils.getInstance().dp2pxConvertInt(context, BITMAP_HEIGHT);
        BITMAP_HEIGHT = AppUtils.getInstance().dp2pxConvertInt(context, BITMAP_HEIGHT);
        DANMU_PADDING = AppUtils.getInstance().dp2pxConvertInt(context, DANMU_PADDING);
        DANMU_PADDING_INNER = AppUtils.getInstance().dp2pxConvertInt(context, DANMU_PADDING_INNER);
        DANMU_RADIUS = AppUtils.getInstance().dp2pxConvertInt(context, DANMU_RADIUS);
        DANMU_TEXT_SIZE = AppUtils.getInstance().sp2px(context, DANMU_TEXT_SIZE);
    }

    /**
     * 绘制背景(自定义弹幕样式)
     * 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
     */
    private class BackgroundCacheStuffer extends SpannedCacheStuffer {
//        final Paint paint = new Paint();//纯颜色
        @Override
        public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
            danmaku.padding = 10;// 在背景绘制模式下增加padding
            super.measure(danmaku, paint, fromWorkerThread);
        }

        @Override
        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
            //纯颜色-蓝色背景
            //paint.setAntiAlias(true);
            //paint.setColor(Color.parseColor(backGroundColor));//背景蓝色
            //canvas.drawRoundRect(new RectF(-danmaku.paintHeight+20, top, left + danmaku.paintWidth+ 20, top + danmaku.paintHeight), DANMU_RADIUS, DANMU_RADIUS, paint);
            //自定义BackgroundDrawable：.9图 NinePatchDrawable,普通：BitmapDrawable
            if(null!=mContext){
                NinePatchDrawable bg =  (NinePatchDrawable) mContext.getResources().getDrawable(R.drawable.room_danmu_item_bg);
                if (bg != null) {
                    bg.setBounds((int) left, (int)top+20, (int)danmaku.paintWidth, (int) danmaku.paintHeight);
                    bg.draw(canvas);
                }
            }
        }

        @Override
        public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {

        }
    }

    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {

        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
//            if (danmaku.text instanceof Spanned) { // 根据你的条件检查是否需要需要更新弹幕
//            }
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            // TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
            if (danmaku.text instanceof Spanned) {
                danmaku.text = "";
            }
        }
    };


    /**
     * 添加一条中奖、赠送礼物的自定义飘屏
     * @param customMsgInfo
     * @param danmuType 弹幕类型
     * @param textColor 自定义文本颜色
     */
    private void addGiftDanmuInternal(CustomMsgInfo customMsgInfo,DanmuType danmuType,int textColor) {
        if(null==mDanmakuView) return;
        if(null==customMsgInfo||null==customMsgInfo.getGift()) return;
        BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if(null==danmaku) return;
        danmaku.userId = AppUtils.getInstance().getRandomNum(100,10000);
        danmaku.isGuest = false;
        if(danmuType== DanmuType.TEXT){
            danmaku.userId = 0;
            danmaku.text = customMsgInfo.getMsgContent();//LiveChatUserGradleSpan.getSpannableDrawableFotGift(customMsgInfo)
            danmaku.padding = DANMU_PADDING;
            danmaku.priority = 0;  //优先级
            danmaku.isLive = false;
            danmaku.setTime(mDanmakuView.getCurrentTime() + ADD_DANMU_TIME);
            danmaku.textSize = DANMU_TEXT_SIZE;
            danmaku.textColor = Color.WHITE;
            danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
            mDanmakuView.addDanmaku(danmaku);
            return;
        }
        Bitmap headBitmap = null;
        if (!TextUtils.isEmpty(customMsgInfo.getGift().getSrc())) {
            RequestManager req = Glide.with(mContext);
            try {
                headBitmap = req.load(customMsgInfo.getGift().getSrc()).asBitmap().centerCrop()
                        .into(BITMAP_WIDTH, BITMAP_HEIGHT)
                        .get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }finally {
                if (headBitmap == null) {
                    headBitmap = getDefaultBitmap(R.drawable.ic_default_gift_icon);
                }
            }
        }
        CircleDrawable circleDrawable = new CircleDrawable(mContext, headBitmap, danmaku.isGuest);
        circleDrawable.setBounds(10, 5, 10, 5);
        String content="我是弹幕内容，请指定弹幕类型";
        String vipTag ="";
        //如果对象是会员身份
        if(customMsgInfo.getSendUserVip()>0){
            vipTag="#VIP#会员#VIP#";
        }
        //赠送礼物
        if(danmuType== DanmuType.GIFT){
            content="“"+vipTag+" "+customMsgInfo.getSendUserName()
                    +"”送给“"+customMsgInfo.getAccapUserName()
                    +"”#GIFT_ICON#礼物#GIFT_ICON#"+"<font color='#FFF038'>"
                    +customMsgInfo.getGift().getTitle()+"</font>";
            //中奖消息
        }else if(danmuType== DanmuType.AWARD){
            content="！！恭喜用户“"+vipTag+" "+customMsgInfo.getSendUserName()
                    +"”送出的#GIFT_ICON#礼物#GIFT_ICON#"+"<font color='#FFF038'>"
                    +customMsgInfo.getGift().getTitle()
                    +"</font>喜中 <font color='#FF7575'>"+customMsgInfo.getGift().getDrawTimes()+"</font><font color='#FFFCAC'>钻石奖励！</font>";
        }
        danmaku.text = createSpannable(circleDrawable, content);
        danmaku.padding = DANMU_PADDING;
        danmaku.priority = 0;  // 0:无优先级 1:一定会显示,但会导致行数的限制失效，会员发送的可优先显示
        danmaku.isLive = false;
        danmaku.setTime(mDanmakuView.getCurrentTime() + ADD_DANMU_TIME);
        danmaku.textSize = DANMU_TEXT_SIZE;
        danmaku.textColor = textColor;
        danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        mDanmakuView.addDanmaku(danmaku);
    }


    /**
     * 解析副文本
     * @param drawable 网络或者ICON drawable
     * @param content 富文本格式的文本内容
     * @return
     */
    private SpannableString createSpannable(Drawable drawable, String content) {
        SpannableString spannableString = new SpannableString(Html.fromHtml(content));
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(spannableString);
        if (matcher.find()) {
            // 重置正则位置
            matcher.reset();
        }
        while (matcher.find()) {
            String vip = matcher.group(1);
            String giftIcon= matcher.group(2);
            //替换VIP图标
            if(null!=vip){
                int start = matcher.start(1);
                int end = start + vip.length();
                Bitmap bitmap = BitmapFactory.decodeResource(APPLication.getInstance().getApplicationContext().getResources(), R.drawable.ic_vip_room_enter);
                if (bitmap != null) {
                    // 压缩Bitmap
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                    // 设置VIP标识
                    ImageSpan imageSpan = new ImageSpan(APPLication.getInstance().getApplicationContext(), bitmap);
                    spannableString.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            //替换网络图片ICON
            if(null!=giftIcon){
                int start = matcher.start(2);
                int end = start + giftIcon.length();
                if (drawable != null) {
                    if (null != drawable) {
                        drawable.setBounds(0, 10, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                        spannableString.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        return spannableString;
    }

    /**
     * 创建默认的ICON
     * @param drawableId
     * @return
     */
    private Bitmap getDefaultBitmap(int drawableId) {
        Bitmap mDefauleBitmap = null;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), drawableId);
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(((float) BITMAP_WIDTH) / width, ((float) BITMAP_HEIGHT) / height);
            mDefauleBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return mDefauleBitmap;
    }

    /**
     * 弹幕资源释放
     */
    public void onDestroy() {
        if (null!=mDanmuThread)mDanmuThread.quit();
        if (null!=mDanmakuView){
            mDanmakuView.stop();
            mDanmakuView.release();
        }
        if(null!=mDanmuHandler) mDanmuHandler.removeMessages(0);
        mDanmuHandler=null;mContext = null;mDanmuThread=null;mDanmakuContext=null;
    }
}
