package com.android.gift.gift.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.android.gift.R;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.gift.view.GiftRoomItemView;
import com.android.gift.room.bean.CustomMsgInfo;
import com.android.gift.util.Logger;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * TinyHung@Outlook.com
 * 2018/7/30
 * 直播间的普通礼物动画容器兼管理者
 */

public class GiftRoomGroupManager extends FrameLayout {

    private GiftRoomItemView[] mRoomGiftItemView;
    private static final String TAG = "RoomGiftGroupManager";
    // 由一个父队列来向四个子队列分配任务，当队列为空，创建新的队列，当队列不为空，根据用户ID和礼物ID分配之任务到子队列中
    private Queue<CustomMsgInfo> mGroupGiftQueue;//父队列，负责统一接收任务，心跳线程中分发任务给子队列
    private Queue<CustomMsgInfo> mChildGiftQueue1=new ArrayDeque<>();//子队列1
    private Queue<CustomMsgInfo> mChildGiftQueue2=new ArrayDeque<>();//子队列2
    private Queue<CustomMsgInfo> mChildGiftQueue3=new ArrayDeque<>();//子队列3
    private Queue<CustomMsgInfo> mChildGiftQueue4=new ArrayDeque<>();//子队列4
    private boolean taskRunning;//礼物动画是否正在执行

    public GiftRoomGroupManager(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public GiftRoomGroupManager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        View.inflate(context, R.layout.view_gift_room_group_layout,this);
        GiftRoomItemView giftItemView1 = (GiftRoomItemView) findViewById(R.id.room_gift_item1);
        GiftRoomItemView giftItemView2 = (GiftRoomItemView) findViewById(R.id.room_gift_item2);
        GiftRoomItemView giftItemView3 = (GiftRoomItemView) findViewById(R.id.room_gift_item3);
        GiftRoomItemView giftItemView4 = (GiftRoomItemView) findViewById(R.id.room_gift_item4);

        mRoomGiftItemView=new GiftRoomItemView[4];
        mRoomGiftItemView[0]=giftItemView1;
        mRoomGiftItemView[1]=giftItemView2;
        mRoomGiftItemView[2]=giftItemView3;
        mRoomGiftItemView[3]=giftItemView4;
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

        //判断是否播放礼物动画,若有数据，同步播放礼物动画
        if(null!=mChildGiftQueue1&&mChildGiftQueue1.size()>0){
            addGiftItemView(mChildGiftQueue1.poll(),0);
        }
        //同上判断
        if(null!=mChildGiftQueue2&&mChildGiftQueue2.size()>0){
            addGiftItemView(mChildGiftQueue2.poll(),1);
        }
        //同上判断
        if(null!=mChildGiftQueue3&&mChildGiftQueue3.size()>0){
            addGiftItemView(mChildGiftQueue3.poll(),2);
        }
        //同上判断
        if(null!=mChildGiftQueue4&&mChildGiftQueue4.size()>0){
            addGiftItemView(mChildGiftQueue4.poll(),3);
        }
    }

    /**
     * 添加至对应任务队列
     * @param data
     * @param index
     */
    private void addTask(CustomMsgInfo data, int index) {
        switch (index) {
            case 0:
                if(null!=mChildGiftQueue1) mChildGiftQueue1.add(data);
                break;
            case 1:
                if(null!=mChildGiftQueue2) mChildGiftQueue2.add(data);
                break;
            case 2:
                if(null!=mChildGiftQueue3) mChildGiftQueue3.add(data);
                break;
            case 3:
                if(null!=mChildGiftQueue4) mChildGiftQueue4.add(data);
                break;
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
        if(null!=mChildGiftQueue1) mChildGiftQueue1.clear(); mChildGiftQueue1=null;
        if(null!=mChildGiftQueue2) mChildGiftQueue2.clear();mChildGiftQueue2=null;
        if(null!=mChildGiftQueue3) mChildGiftQueue3.clear();mChildGiftQueue3=null;
        if(null!=mChildGiftQueue4) mChildGiftQueue4.clear();mChildGiftQueue4=null;
    }

    /**
     * 停止播放任务
     */
    public void onReset(){
        taskRunning =false;
        if(null!=mGroupGiftQueue) mGroupGiftQueue.clear();
        if(null!=mChildGiftQueue1) mChildGiftQueue1.clear();
        if(null!=mChildGiftQueue2) mChildGiftQueue2.clear();
        if(null!=mChildGiftQueue3) mChildGiftQueue3.clear();
        if(null!=mChildGiftQueue4) mChildGiftQueue4.clear();
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