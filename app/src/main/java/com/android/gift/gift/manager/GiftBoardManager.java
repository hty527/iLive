package com.android.gift.gift.manager;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.gift.R;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.bean.GiftType;
import com.android.gift.bean.UserInfo;
import com.android.gift.gift.listener.OnGiveGiftListener;
import com.android.gift.gift.view.GiftLayout;
import com.android.gift.listener.OnAnimationListener;
import com.android.gift.util.AnimationUtil;
import com.android.gift.util.AppUtils;
import com.android.gift.util.Logger;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by TinyHung@outlook.com
 * 2019/6/20
 * 一个持有全局唯一的礼物面板VIEW 及 SVGA VIEW管理类
 * 礼物面板容器需要渲染界面，请使用此Manager管理
 * mGiftLayout mSvgaImageView mSvgaItemView 为保障回显显示ITEM选中状态，三者生命周期需一致
 */

public class GiftBoardManager {

    private static final String TAG = "GiftCacheManager";
    private volatile static GiftBoardManager mInstance;
    //礼物分类缓存
    private List<GiftType> mGiftTypes;
    //礼物分类下的礼物列表缓存
    private Map<String,ArrayList<GiftItemInfo>> mGiftItemInfos;
    //监听器
    private List<OnGiveGiftListener> mOnGiveGiftListeners;
    private OnGiveGiftListener mOnGiveGiftListener;
    //礼物选中个数
    private int mGiftCount=0;
    //礼物选中的倍率
    private List<Integer> mMultipNums;
    //展示、与用户交互的礼物容器
    private GiftLayout mGiftLayout;
    private SVGAImageView mSvgaImageView;
    private SVGAParser mParser;
    //itemView,defaultItemView
    private View mSvgaItemView,mDefaultItemView;
    //接收者群ID
    private String mReceiveRoomID;
    //接收者信息
    private UserInfo mReceiveUser;
    //将要发送出去的礼物信息
    private GiftItemInfo mReceiveGiftItemInfo;
    //礼物中奖后，金币掉落的结束位置
    private int[] mAwardEndLocation;
    //礼物面板是否完全初始化了？渲染完成认为是初始化完成了
    private boolean mInitFinish=false;

    public static synchronized GiftBoardManager getInstance(){
        synchronized (GiftBoardManager.class){
            if(null==mInstance){
                mInstance=new GiftBoardManager();
            }
        }
        return mInstance;
    }

    /**
     * 设置接收者群ID
     * @param receiveRoomID
     */
    public GiftBoardManager setReceiveRoomID(String receiveRoomID) {
        mReceiveRoomID = receiveRoomID;
        return mInstance;
    }

    /**
     * 设置接收人信息
     * @param receiveUser
     */
    public GiftBoardManager setReceiveUser(UserInfo receiveUser) {
        mReceiveUser = receiveUser;
        return mInstance;
    }

    /**
     * 添加礼物选择监听器至监听池
     * @param listener
     */
    public GiftBoardManager addOnGiveGiftListener(OnGiveGiftListener listener){
        if(null==mOnGiveGiftListeners){
            mOnGiveGiftListeners=new ArrayList<>();
        }
        mOnGiveGiftListeners.add(listener);
        return mInstance;
    }

    /**
     * 设置礼物选择赠送监听器
     * @param listener
     */
    public GiftBoardManager setOnGiveGiftListener(OnGiveGiftListener listener){
        this.mOnGiveGiftListener=listener;
        return mInstance;
    }

    /**
     * 从监听池移除某个监听器实现类
     * @param listener
     */
    public void removeOnGiveGiftListener(OnGiveGiftListener listener){
        if(null!=mOnGiveGiftListeners){
            mOnGiveGiftListeners.remove(listener);
        }
    }

    /**
     * 清空所有监听器
     */
    public void removeAllGiveGiftListener(){
        if(null!=mOnGiveGiftListeners){
            mOnGiveGiftListeners.clear();
        }
        mOnGiveGiftListener=null;
    }

    public GiftBoardManager init(Context context){
        //个数
        mMultipNums=new ArrayList<>();
        mMultipNums.add(1);
        mMultipNums.add(9);
        mMultipNums.add(50);
        mMultipNums.add(99);
        mMultipNums.add(520);
        mMultipNums.add(1314);
        getGiftView(context);
        return mInstance;
    }

    /**
     * 返回礼物一级分类列表
     * @return
     */
    public List<GiftType> getGiftTypes() {
        return mGiftTypes;
    }

    public void setGiftTypes(List<GiftType> giftTypes) {
        mGiftTypes = giftTypes;
    }

    /**
     * 根据分类返回分类下的礼物列表
     * @param type
     * @return
     */
    public List<GiftItemInfo> getGiftItemInfos(String type) {
        if(null!=mGiftItemInfos&&mGiftItemInfos.size()>0){
            return mGiftItemInfos.get(type);
        }
        return null;
    }

    public void setGiftItemInfos(ArrayList<GiftItemInfo> giftItemInfos,String type) {
        if(null==mGiftItemInfos){
            mGiftItemInfos=new TreeMap<>();
        }
        mGiftItemInfos.put(type,giftItemInfos);
    }

    /**
     * 返回单例的礼物交互组件
     * @param context Activity上下文
     * @return
     */
    public GiftLayout getGiftView(Context context) {
        if(null==mGiftLayout){
            mGiftLayout=new GiftLayout(context);
        }
        return mGiftLayout;
    }

    /**
     * 返回ITEM实例
     * @return
     */
    public View getItemView() {
        return mSvgaItemView;
    }

    /**
     * 单例的SvgaImageView初始化
     * @param context
     */
    private void initSvgaImageView(Context context) {
        mSvgaImageView = new SVGAImageView(context);
        mSvgaImageView.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
                Logger.d(TAG,"onPause");
            }

            @Override
            public void onFinished() {
                Logger.d(TAG,"onFinished");
            }

            @Override
            public void onRepeat() {
                Logger.d(TAG,"onRepeat");
            }

            @Override
            public void onStep(int i, double v) {
                Logger.d(TAG,"onStep,i:"+i+",v:"+v);
            }
        });
        mParser = new SVGAParser(context.getApplicationContext());
    }

    /**
     * 移除自身的父容器
     */
    public void removeParentGroup() {
        if(null!=mSvgaImageView&&null!=mSvgaImageView.getParent()){
            ViewGroup parent = (ViewGroup) mSvgaImageView.getParent();
            parent.removeView(mSvgaImageView);
        }
    }

    /**
     * 还原旧的ITEM VIEW默认显示项
     */
    private void resetItemView() {
        if(null!=mSvgaItemView){
            mSvgaItemView.findViewById(R.id.gift_item_ic_icon).setVisibility(View.VISIBLE);
            mSvgaItemView.findViewById(R.id.gift_item_tv_title).setVisibility(View.GONE);
            mSvgaItemView.findViewById(R.id.gift_item_re_view).setBackgroundResource(0);
            TextView textCount = mSvgaItemView.findViewById(R.id.gift_item_tv_count);
            textCount.setText("0");
            textCount.setVisibility(View.GONE);
        }
    }

    /**
     * 礼物面板中的ITEM点击事件
     * @param view itemView
     * @param giftInfo 礼物对象
     */
    public void onClick(View view, final GiftItemInfo giftInfo) {
        FrameLayout svgaIcon = view.findViewById(R.id.gift_svga_view);
        //重复点击交互逻辑
        if(svgaIcon.getChildCount()>0){
            Logger.d(TAG,"onClick-->已添加SVGA");
            //只是增加选中数量并回调到礼物面板
            mGiftCount++;
            if(mGiftCount>=mMultipNums.size()){
                mGiftCount=0;
            }
            if(null!=mSvgaItemView){
                TextView textCount = mSvgaItemView.findViewById(R.id.gift_item_tv_count);
                textCount.setText(Html.fromHtml("x<font><big>"+mMultipNums.get(mGiftCount)+"<big></font>"));
                AnimationUtil.playTextCountAnimation(textCount);
            }
            //将个数发生变化事件回调出去
            if(null!=mOnGiveGiftListener){
                mOnGiveGiftListener.onReceiveGiftCount(mMultipNums.get(mGiftCount));
            }
            if(null!=mOnGiveGiftListeners){
                for (OnGiveGiftListener onGiveGiftListener : mOnGiveGiftListeners) {
                    onGiveGiftListener.onReceiveGiftCount(mMultipNums.get(mGiftCount));
                }
            }
            return;
        }
        if(null==giftInfo){
            return;
        }
        if(null!=mGiftLayout){
            mGiftLayout.setGiftDesp(giftInfo.getDesp());
        }
        //新的选中项交互
        //移除SVGA可能依附的父Parent
        removeParentGroup();
        //还原旧的ITEM为初始状态
        resetItemView();
        //检查SVGA初始化状态
        initSvgaImageView(view.getContext());
        //再清除一下上一个动画的痕迹
        mSvgaImageView.stopAnimation(true);
        //添加到新的ITEM VIEW中
        svgaIcon.addView(mSvgaImageView,new FrameLayout.LayoutParams(-1,-1));
        //显示逻辑
        view.findViewById(R.id.gift_item_ic_icon).setVisibility(View.GONE);
        view.findViewById(R.id.gift_item_tv_title).setVisibility(View.VISIBLE);
        mGiftCount=0;
        TextView textCount = view.findViewById(R.id.gift_item_tv_count);
        textCount.setVisibility(View.VISIBLE);
        textCount.setText(Html.fromHtml("x<font><big>"+mMultipNums.get(mGiftCount)+"<big></font>"));
        //ITEM容器
        View itemGroup = view.findViewById(R.id.gift_item_re_view);
        itemGroup.setBackgroundResource(R.drawable.bg_gift_item_selected);
        //绑定ITEM VIEW
        this.mSvgaItemView=view;
        this.mReceiveGiftItemInfo=giftInfo;
        mReceiveGiftItemInfo.setCount(mMultipNums.get(mGiftCount));
        //播放背景选中的动画,动画完成开始加载SVGA
        AnimationUtil.playAnimation(itemGroup, new OnAnimationListener() {
            @Override
            public void onStart() {}

            @Override
            public void onEnd() {
                //等待背景框动画加载完成开始加载SVGA动画
                try {
                    mParser.parse(new URL(giftInfo.getSvga()), new SVGAParser.ParseCompletion() {
                        @Override
                        public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                            Logger.d(TAG,"onComplete");
                            SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                            if(null!=mSvgaImageView){
                                mSvgaImageView.setImageDrawable(drawable);
                                mSvgaImageView.startAnimation();
                            }
                        }

                        @Override
                        public void onError() {
                            Logger.d(TAG,"onError");
                            if(null!=mSvgaImageView){
                                mSvgaImageView.stopAnimation(true);
                            }
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    if(null!=mSvgaImageView){
                        mSvgaImageView.stopAnimation(true);
                    }
                }
            }
        });
        //将新的选中的礼物对象回调出去
        if(null!=mOnGiveGiftListener){
            mOnGiveGiftListener.onReceiveNewGift(mReceiveGiftItemInfo);
        }
        if(null!=mOnGiveGiftListeners){
            for (OnGiveGiftListener onGiveGiftListener : mOnGiveGiftListeners) {
                onGiveGiftListener.onReceiveNewGift(mReceiveGiftItemInfo);
            }
        }
    }

    /**
     * 赠送礼物，礼物面板容器触发
     */
    public void sendGift(){
        if(null==mReceiveGiftItemInfo){
            return;
        }
        if(null!=mOnGiveGiftListener){
            mOnGiveGiftListener.onReceiveGift(mReceiveGiftItemInfo,mReceiveUser,mReceiveRoomID,mMultipNums.get(mGiftCount));
        }
        if(null!=mOnGiveGiftListeners){
            for (OnGiveGiftListener onGiveGiftListener : mOnGiveGiftListeners) {
                onGiveGiftListener.onReceiveGift((GiftItemInfo) mSvgaItemView.getTag(),mReceiveUser,mReceiveRoomID,mMultipNums.get(mGiftCount));
            }
        }
    }

    /**
     * 设定金币掉落位于屏幕的具体位置
     * @param awardEndLocation
     */
    public void setAwardEndLocation(int[] awardEndLocation) {
        mAwardEndLocation = awardEndLocation;
    }
    /**
     * 获取金币掉落位于屏幕的具体位置
     */
    public int[] getAwardEndLocation() {
        if(null==mAwardEndLocation||mAwardEndLocation.length<=0){
            mAwardEndLocation=new int[2];
            mAwardEndLocation[0]=(AppUtils.getInstance().getScreenWidth()-AppUtils.getInstance().dpToPxInt(32f));
            mAwardEndLocation[1]= (AppUtils.getInstance().getScreenHeight()-AppUtils.getInstance().dpToPxInt(96f)) ;
        }
        return mAwardEndLocation;
    }

    /**
     * 保存第0个ITEM实例
     * @param itemView
     * @param position
     */
    public void putFirstItemView(View itemView,int position) {
        if(null==mDefaultItemView){
            this.mDefaultItemView=itemView;
        }
    }

    /**
     * 标记礼物面板是否初始化完成
     * @param initFinish
     */
    public void setInitFinish(boolean initFinish) {
        mInitFinish = initFinish;
    }

    public boolean isInitFinish() {
        return mInitFinish;
    }

    /**
     * 默认选中某个位置，请在礼物面板初始化后调用
     */
    public void defaultSelectedIndex() {
        if(null==mSvgaItemView&&null!=mDefaultItemView&&null!=mDefaultItemView.getTag()){
            //仅当未选中任何项时才默认选中某个
            GiftItemInfo itemInfo = (GiftItemInfo) mDefaultItemView.getTag();
            onClick(mDefaultItemView,itemInfo);
        }
    }

    /**
     * 礼物面板处于可见调用
     */
    public void onResume(){
        if(null!=mSvgaImageView){
            mSvgaImageView.startAnimation();
        }
    }

    /**
     * 礼物面板处于不可见调用
     */
    public void onPause(){
        if(null!=mSvgaImageView){
            mSvgaImageView.pauseAnimation();
        }
    }

    /**
     * 礼物面板载体销毁时，也可无需调用
     */
    public void onDestroy(){
        if(null!=mSvgaImageView){
            mSvgaImageView.stopAnimation(true);
            mSvgaImageView=null;
        }
        //移除SVGA可能依附的父Parent
        removeParentGroup();
        //还原旧的ITEM为初始状态
        resetItemView();
        mGiftCount=0;
        if(null!=mSvgaItemView&&null!=mSvgaItemView.getParent()){
            ViewGroup parent = (ViewGroup) mSvgaItemView.getParent();
            parent.removeView(mSvgaItemView);
        }
        mSvgaItemView=null;mDefaultItemView=null;
    }
}