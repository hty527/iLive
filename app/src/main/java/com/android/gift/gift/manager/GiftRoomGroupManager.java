package com.android.gift.gift.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.gift.view.GiftRoomItemView;
import com.android.gift.room.bean.CustomMsgInfo;
import com.android.gift.util.AppUtils;
import com.android.gift.util.Logger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * TinyHung@Outlook.com
 * 2018/7/30
 * 直播间普通礼物动画播放管理者，请调用initGiftPass或者initGiftPassCount方法初始化礼物通道
 * 内部动态创建礼物通道队列及个数(已调用initGiftPass且未调用initGiftPassCount情况下)
 */

public class GiftRoomGroupManager extends LinearLayout {

    private static final String TAG = "GiftRoomGroupManager";
    //礼物动画ITEM通道
    private GiftRoomItemView[] mRoomGiftItemView;
    //通道对应的礼物栈队列
    private List<Queue<CustomMsgInfo>> mQueues=new ArrayList<>();
    //临时的动画任务总队列，内部智能分配到mQueues队列中
    private Queue<CustomMsgInfo> mGroupGiftQueue;
    //礼物动画是否正在执行
    private boolean taskRunning;

    public GiftRoomGroupManager(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public GiftRoomGroupManager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        this.setOrientation(LinearLayout.VERTICAL);
    }

    /**
     * 根据礼物动画UI容器可用的高度来分类礼物通道个数
     * @param resolutionPX 礼物动画最大可用高度
     */
    public void initGiftPass(int resolutionPX) {
        int itemHeight = AppUtils.getInstance().dpToPxInt(getContext(), 76f);
        int count = resolutionPX / itemHeight;
        initGiftPassCount(itemHeight,count);
    }

    /**
     * 自定指定礼物通道ITEM高度及个数
     * @param itemHeight 礼物通道ITEM高度
     * @param count 总个数
     */
    public void initGiftPassCount(int itemHeight,int count){
        removeAllViews();
        mRoomGiftItemView=new GiftRoomItemView[count];
        Logger.d(TAG,"initGift-->itemHeight:"+itemHeight+",count:"+count);
        if(null==mQueues){
            mQueues=new ArrayList<>();
        }
        for (int i = 0; i < count; i++) {
            GiftRoomItemView giftRoomItemView=new GiftRoomItemView(getContext());
            giftRoomItemView.setClipChildren(false);
            giftRoomItemView.setClipToPadding(false);
            //倒过来添加到views
            mRoomGiftItemView[((count-1)-i)]=giftRoomItemView;
            GiftRoomGroupManager.this.addView(giftRoomItemView,new LinearLayoutCompat.LayoutParams(-1,itemHeight));
            Queue<CustomMsgInfo> mChildGiftQueue=new ArrayDeque<>();
            mQueues.add(mChildGiftQueue);
        }
    }

    /**
     * 提供给外界直接调用
     */
    public void onCommenTask() {
        playGiftAnimation();
    }

    /**
     * 添加一个普通礼物动画
     * @param data 礼物数据
     * 如果是自己赠送的礼物，优先展示
     */
    public synchronized void addGiftAnimationItem(CustomMsgInfo data){
        if(null==mRoomGiftItemView||null==data) return;
        GiftItemInfo giftInfo = data.getGift();
        if(null==giftInfo) return;
        String tagID=data.getSendUserID()+giftInfo.getId()+data.getAccapUserID();
        //1.该礼物已经在前台队列中
        for (int i = 0; i < mRoomGiftItemView.length; i++) {
            GiftRoomItemView frameLayout = mRoomGiftItemView[i];
            //当前队列正在显示的View属性与新动画相符
            if(null != frameLayout.getTag() && TextUtils.equals(tagID, (String) frameLayout.getTag())){
                Logger.d(TAG,"前台存在此任务："+tagID);
                addTask(data,i);
                return;
            }
        }
        Logger.d(TAG,"新礼物："+tagID);
        //新礼物 添加至总队任务队列，等待空闲子队列产生
        if(null==mGroupGiftQueue) mGroupGiftQueue=new ArrayDeque<>();
        mGroupGiftQueue.add(data);
    }


    /**
     * 开始普通礼物的播放逻辑
     */
    private synchronized void playGiftAnimation() {
        if(null==mRoomGiftItemView) return;
        //先分配一把

        if(null!=mGroupGiftQueue&&mGroupGiftQueue.size()>0){
            ///取出最前面一个元素但不擦除源数据
            CustomMsgInfo peekInfo = mGroupGiftQueue.peek();
            if(null!=peekInfo&&null!=peekInfo.getGift()){
                String tagID=peekInfo.getSendUserID()+peekInfo.getGift().getId()+peekInfo.getAccapUserID();
                //分配任务
                out: for (int i = 0; i < mRoomGiftItemView.length; i++) {
                    GiftRoomItemView frameLayout = mRoomGiftItemView[i];
                    //当前列表展示的属性和新的分配的相符
                    if(null!=frameLayout.getTag()&&TextUtils.equals(tagID, (String) frameLayout.getTag())){
                        addTask(mGroupGiftQueue.poll(),i);
                        Logger.d(TAG,"跳出循环1");
                        break out;
                    }else{
                        //新的动画任务
                        if(null==frameLayout.getTag()){
                            addTask(mGroupGiftQueue.poll(),i);
                            Logger.d(TAG,"跳出循环2：index:"+i);
                            break out;
                        }
                    }
                }
            }
        }
        if(null!=mQueues){
            //检查礼物动画待播放队列
            for (int i = 0; i < mQueues.size(); i++) {
                Queue<CustomMsgInfo> customMsgInfos = mQueues.get(i);
                //判断是否播放礼物动画,若有数据，同步播放礼物动画
                if(null!=customMsgInfos&&customMsgInfos.size()>0){
                    addGiftItemView(customMsgInfos.poll(),i);
                }
            }
        }
    }

    /**
     * 添加至对应任务队列
     * @param data
     * @param index
     */
    private void addTask(CustomMsgInfo data, int index) {
        if(null!=mQueues){
            Queue<CustomMsgInfo> customMsgInfos = mQueues.get(index);
            customMsgInfos.add(data);
        }
    }

    /**
     * 根据TAG是否包含某个元素
     * @param childGiftQueue
     * @param tag
     * @return
     */
    private boolean isContainsInfo(Queue<CustomMsgInfo> childGiftQueue, String tag) {
        if(null==childGiftQueue) return false;
        if(childGiftQueue.size()<=0) return false;
        if(TextUtils.isEmpty(tag)) return false;
        for (CustomMsgInfo info: childGiftQueue) {
            if(null!=info.getGift()){
                if(TextUtils.equals(tag,info.getSendUserID()+info.getGift().getId()+info.getAccapUserID())){
                    return true;
                }
            }
        }
        return false;
    }

    //========================================View动画的绘制和播放====================================
    /**
     * 添加到某个子队列下开始执行
     * @param pollInfo
     * @param index 父容器Index
     */
    private void addGiftItemView(final CustomMsgInfo pollInfo, final int index) {
        if(null==pollInfo||null==pollInfo.getGift()) return;
        this.post(new Runnable() {
            @Override
            public void run() {
                //根据TAG绘制礼物动画，保证其唯一性
                String tagID=pollInfo.getSendUserID()+pollInfo.getGift().getId()+pollInfo.getAccapUserID();
                addGifView(tagID,pollInfo,index);
            }
        });
    }

    /**
     * 增加一个礼物，
     * @param tagID 赠送人和礼物的ID合并起来的
     * @param msgInfo 赠送人的基本信息
     * @param index 父容器index
     */
    private void addGifView(String tagID, CustomMsgInfo msgInfo,int index) {
        if(null==mRoomGiftItemView) return;
        //动画的父容器
        GiftRoomItemView frameLayout = mRoomGiftItemView[index];
        boolean b = frameLayout.addGiftItem(tagID, msgInfo);
        if(!b){
            if(null==mGroupGiftQueue) mGroupGiftQueue=new ArrayDeque<>();
            Logger.d(TAG,"未添加成功，回收");
            mGroupGiftQueue.add(msgInfo);
        }
    }

    /**
     * 开启动画播放任务
     */
    public void startPlayTask(){
        if(taskRunning) return;
        new Thread(){
            @Override
            public void run() {
                super.run();
                taskRunning =true;
                while (taskRunning){
                    playGiftAnimation();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 结束播放任务
     */
    public void stopPlayTask(){
        taskRunning =false;
        if(null!=mGroupGiftQueue) mGroupGiftQueue.clear(); mGroupGiftQueue=null;
        if(null!=mQueues){
            for (Queue<CustomMsgInfo> queue : mQueues) {
                queue.clear();
            }
            mQueues.clear();
        }
    }

    /**
     * 停止播放任务
     */
    public void onReset(){
        taskRunning =false;
        if(null!=mGroupGiftQueue) mGroupGiftQueue.clear();
        if(null!=mQueues){
            for (Queue<CustomMsgInfo> queue : mQueues) {
                queue.clear();
            }
        }
    }

    /**
     * 对应方法调用
     */
    public void onDestroy(){
        stopPlayTask();
        if(null!=mRoomGiftItemView){
            for (GiftRoomItemView roomGiftItemView : mRoomGiftItemView) {
                roomGiftItemView.onDestroy();
            }
            mRoomGiftItemView=null;
        }
    }
}