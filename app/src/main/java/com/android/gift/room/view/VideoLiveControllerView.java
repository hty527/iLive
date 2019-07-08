package com.android.gift.room.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.gift.R;
import com.android.gift.base.RoomBaseController;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.bean.UserInfo;
import com.android.gift.constant.Constants;
import com.android.gift.gift.adapter.LiveFansListAdapter;
import com.android.gift.gift.dialog.LiveGiftDialog;
import com.android.gift.gift.listener.OnGiveGiftListener;
import com.android.gift.gift.manager.GiftBoardManager;
import com.android.gift.gift.manager.GiftRoomGroupManager;
import com.android.gift.gift.view.CountdownGiftView;
import com.android.gift.manager.UserManager;
import com.android.gift.model.ItemSpacesItemDecoration;
import com.android.gift.model.ScrollSpeedLinearLayoutManger;
import com.android.gift.room.bean.CustomMsgExtra;
import com.android.gift.room.bean.CustomMsgInfo;
import com.android.gift.room.bean.NumberChangedInfo;
import com.android.gift.util.AnimationUtil;
import com.android.gift.util.AppUtils;
import com.android.gift.util.DataFactory;
import com.android.gift.util.Logger;
import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2018/5/31
 * 直播房间 推、拉 控制器
 * 必须给定应用场景 mMode
 */

public class VideoLiveControllerView extends RoomBaseController implements View.OnClickListener,OnGiveGiftListener {

    private static final String TAG = "VideoLiveControllerView";
    private Context mContext;
    private LiveFansListAdapter mAvatarListAdapter;//在线观众
    private BrightConversationListView mConversationListView;//会话列表
    //主播端
    private long SECOND=0;//直播、观看时长
    private TextView mOnlineNumber;//直播间在线人数
    private Handler mHandler;
    private LikeHeartLayout mHeartLayout;//飘心
    private CountdownGiftView mCountdownGiftView;//连击赠送礼物
    private GiftRoomGroupManager mGiftGroupManager;//普通礼物
    private View mEmptyView;
    private LiveGiftDialog mFragment;
    private View mTopBar;

    public VideoLiveControllerView(Context context) {
        super(context);
        init(context,null);
    }

    public VideoLiveControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    /**
     * 初始化
     * @param context
     * @param attrs
     */
    @SuppressLint("WrongViewCast")
    private void init(Context context, AttributeSet attrs) {
        Logger.d(TAG,"init");
        this.mContext=context;
        View.inflate(context, R.layout.view_live_controller_layout,this);
        //状态栏高度,整个Activity是沉浸式的
        findViewById(R.id.view_top_bar_empty).getLayoutParams().height=AppUtils.getInstance().getStatusBarHeight(getContext());
        //直播间在线人数
        if(null== mOnlineNumber) mOnlineNumber = (TextView) findViewById(R.id.view_online_number);
        mOnlineNumber.setText(AppUtils.getInstance().formatWan(100000,true)+"人");
        mTopBar = findViewById(R.id.tool_bar_view);
        //房间内会话列表
        mConversationListView = (BrightConversationListView) findViewById(R.id.view_bright_conversation);
        mConversationListView.initConversation();
        //初始化在线观众列表
        initMemberAdapter();
        //飘心
        mHeartLayout = (LikeHeartLayout) findViewById(R.id.heart_layout);
        //普通礼物
        mGiftGroupManager = (GiftRoomGroupManager) findViewById(R.id.room_gift_manager);
        mHandler = new Handler(Looper.getMainLooper());
        //连击赠送礼物
        mCountdownGiftView = (CountdownGiftView) findViewById(R.id.view_countdown_view);
        //确定倒计时按钮的的位置，金币掉落时的终点位置
        mCountdownGiftView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] location=new int[2];
                mCountdownGiftView.getLocationOnScreen(location);
                GiftBoardManager.getInstance().setAwardEndLocation(location);
                mCountdownGiftView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        //输入框弹起占位
        mEmptyView = findViewById(R.id.empty_view);
        findViewById(R.id.view_btn_close).setOnClickListener(this);
        findViewById(R.id.view_btn_menu4).setOnClickListener(this);
        findViewById(R.id.re_root_view).setOnClickListener(this);
        findViewById(R.id.view_btn_menu0).setOnClickListener(this);
        //监听礼物交互
        GiftBoardManager.getInstance().addOnGiveGiftListener(this);
        startGiftTask();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Logger.d(TAG,"onFinishInflate");
    }

    /**
     * 初始化在线观众列表
     */
    private void initMemberAdapter() {
        FrameLayout userContent = (FrameLayout) findViewById(R.id.fans_root_view);
        if(null==userContent) return;
        userContent.removeAllViews();
        mAvatarListAdapter=null;
        RecyclerView recyclerView=new RecyclerView(getContext());
        FrameLayout.LayoutParams layoutParams=new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutParams(layoutParams);
        recyclerView.setHorizontalFadingEdgeEnabled(true);
        recyclerView.setFadingEdgeLength(AppUtils.getInstance().dpToPxInt(20f));
        ScrollSpeedLinearLayoutManger linearLayoutManger=new ScrollSpeedLinearLayoutManger(getContext());
        linearLayoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManger);
        recyclerView.addItemDecoration(new ItemSpacesItemDecoration(AppUtils.getInstance().dpToPxInt(8f)));
        mAvatarListAdapter = new LiveFansListAdapter(getContext());
        recyclerView.setAdapter(mAvatarListAdapter);
        userContent.addView(recyclerView);
        List<UserInfo> userInfos= DataFactory.createLiveFans();
        mAvatarListAdapter.setNewData(userInfos);
    }

    private void startGiftTask() {
        if(null!=mGiftGroupManager){
            mGiftGroupManager.startPlayTask();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //礼物
            case R.id.view_btn_menu4:
                if(null!=mFunctionListener){
                    mFunctionListener.showGift();
                }
                break;
            //关闭
            case R.id.view_btn_close:
                if(null!=mFunctionListener){
                    mFunctionListener.backPress();
                }
                break;
            case R.id.re_root_view:
                clickHeart();
                break;
            //聊天
            case R.id.view_btn_menu0:
                if(null!=mFunctionListener){
                    mFunctionListener.showInput();
                }
                break;
        }
    }

    /**
     * 触发飘心
     */
    public void clickHeart() {
        if(null!=mHeartLayout) mHeartLayout.addFavor();
    }

    //==========================================礼物交互事件=========================================

    /**
     * 赠送礼物事件
     * @param giftItemInfo 礼物信息
     * @param userInfo 接收人信息
     * @param roomid 房间ID
     * @param count 赠送数量
     */
    @Override
    public void onReceiveGift(GiftItemInfo giftItemInfo, UserInfo userInfo, String roomid, int count) {
        if(null!=giftItemInfo){
            Logger.d(TAG,"onReceiveGift-->接收人："+userInfo.getUserid()+",群组ID："+roomid+",礼物ID："+giftItemInfo.getId()+",数量："+count);
            if(null!=mCountdownGiftView){
                mCountdownGiftView.updataView(giftItemInfo);
            }

            //本地播放礼物动画，远程端交由服务器推送
            CustomMsgExtra customMsgExtra=new CustomMsgExtra();
            customMsgExtra.setCmd(Constants.MSG_CUSTOM_GIFT);
            if(null!=userInfo){
                customMsgExtra.setAccapUserID(userInfo.getUserid());
                customMsgExtra.setAccapUserName(userInfo.getNickName());
                customMsgExtra.setAccapUserHeader(userInfo.getAvatar());
            }
            giftItemInfo.setDrawLevel(0);
            giftItemInfo.setDrawTimes(0);
            giftItemInfo.setCount(count);
            giftItemInfo.setSource_room_id("AAAAAA");
            CustomMsgInfo customMsgInfo = AppUtils.getInstance().packMessage(customMsgExtra, giftItemInfo);
            customMsgInfo.setAccapGroupID("er43te5yttrywrer4t");
            // TODO: 2019/7/8 模拟奇数小倍率中奖
            int randomNum = AppUtils.getInstance().getRandomNum(0, 100);
            if((randomNum&1)==1){
                //收紧概率区间
                if(randomNum>90){
                    giftItemInfo.setDrawLevel(Constants.ROOM_GIFT_DRAW_ONE_LEVEL);
                    giftItemInfo.setDrawTimes(randomNum);
                }
            }else{
                // TODO: 2019/7/8 模拟偶数大倍率中奖
                if(randomNum>93){
                    giftItemInfo.setDrawLevel(Constants.ROOM_GIFT_DRAW_TWO_LEVEL);
                    giftItemInfo.setDrawTimes(randomNum);
                }
            }
            // TODO: 2019/7/8  特别注意，这里的虚拟中奖，不会计算增加到赠送的礼物的数量中！实际中奖也不需要计数
            //礼物动画展示
            if(null!=mGiftGroupManager){
                mGiftGroupManager.addGiftAnimationItem(customMsgInfo);
            }
        }
    }

    /**
     * 选中的礼物数量发生了变化
     * @param count 礼物个数
     */
    @Override
    public void onReceiveGiftCount(int count) {
        if(null!=mCountdownGiftView){
            mCountdownGiftView.setCount(count);
        }
    }

    /**
     * 新的礼物被选中了
     * @param giftItemInfo 礼物对象
     */
    @Override
    public void onReceiveNewGift(GiftItemInfo giftItemInfo) {
        if(null!=mCountdownGiftView&&null!=giftItemInfo){
            mCountdownGiftView.updataView(giftItemInfo);
        }
    }

    //=====================================直播间控制器交互和消息=====================================

    /**
     * 显示、隐藏软键盘
     * @param flag 输入法是否显示
     * @param keyBordHeight 软键盘高度
     * 采坑：room_gift_manager；礼物展示空间高度挤压了屏幕的剩余空间，导致empty_view设置高度无效
     */
    @Override
    public void showInputKeyBord(boolean flag, int keyBordHeight) {
        if(null==mEmptyView) return;
        if(flag){
            if(null!=mGiftGroupManager) mGiftGroupManager.setVisibility(GONE);
            if(mEmptyView.getLayoutParams().height!=(keyBordHeight+30)){
                mEmptyView.getLayoutParams().height=(keyBordHeight+30);
            }
            mEmptyView.setVisibility(VISIBLE);//显示输入法将聊天列表顶上去
            AnimationUtil.goneTransparentView(mTopBar);//输入框弹起隐藏头部
        }else{
            if(mEmptyView.getVisibility()!=GONE) mEmptyView.setVisibility(GONE);//输入法隐藏占位布局不可见
            if(null!=mGiftGroupManager&&mGiftGroupManager.getVisibility()!=VISIBLE) mGiftGroupManager.setVisibility(VISIBLE);
            if(findViewById(R.id.tool_bar_view).getVisibility()==VISIBLE) return;
            AnimationUtil.visibTransparentView(mTopBar);//输入框消失显示头部
        }
    }

    /**
     * 群组纯文本消息
     * @param customMsgInfo
     * @param isSystemPro
     */
    @Override
    public void onNewTextMessage(CustomMsgInfo customMsgInfo, boolean isSystemPro) {
        if(null!=customMsgInfo&&!TextUtils.isEmpty(customMsgInfo.getChildCmd())){
            if(null!=mConversationListView){
                mConversationListView.addConversation(customMsgInfo,isSystemPro);
            }
        }
    }

    /**
     * 小型的迷你消息
     * @param groupId
     * @param sender
     * @param changedInfo 人数变化
     */
    @Override
    public void onNewMinMessage(String groupId, String sender, NumberChangedInfo changedInfo) {
        if(null==changedInfo) return;
        //在线人数更新
        if(null!= mOnlineNumber){
            mOnlineNumber.setText(AppUtils.getInstance().formatWan(changedInfo.getOnlineNumer(),true)+"人");
        }
    }

    /**
     * 系统发送的自定义消息、系统通知、礼物赠送、成员的进出场、聊天列表消息、中奖信息 等
     * @param customMsgInfo 消息的封装体
     * @param isSystemPro 是否来自系统推送
     */
    @Override
    public void newSystemCustomMessage(CustomMsgInfo customMsgInfo, boolean isSystemPro) {
        if(null==customMsgInfo||null==customMsgInfo.getCmd()) return;
        for (String cmd : customMsgInfo.getCmd()) {
            customMsgInfo.setChildCmd(cmd);//聊天列表所需
            GiftItemInfo giftInfo = customMsgInfo.getGift();
                //自定义消息、系统消息、成员进场
                switch (cmd) {
                    case Constants.MSG_CUSTOM_TEXT://文本消息
                    case Constants.MSG_CUSTOM_NOTICE://系统公告
                    case Constants.MSG_CUSTOM_ADD_USER://新增用户
                    case Constants.MSG_CUSTOM_FOLLOW_ANCHOR://关注主播
                        //是否拦截
                        boolean isIntercept = false;
                        //拦截来自网络的自己发送的消息和进入房间消息
                        if (isSystemPro && TextUtils.equals(UserManager.getInstance().getUserId(),customMsgInfo.getSendUserID())) {
                            isIntercept = true;
                        }
                        if (!isIntercept && null != mConversationListView) {
                            mConversationListView.addConversation(customMsgInfo,isSystemPro);
                        }
                        //会员入场，飘心处理
                        if (cmd.equals(Constants.MSG_CUSTOM_ADD_USER)) {
                            //如果是远程消息,对自己不可见
                            if(isSystemPro && UserManager.getInstance().getUserId().equals(customMsgInfo.getSendUserID())){
                                continue;
                            }
                            //入场飘心
                            clickHeart();
                            // TODO: 2019/7/6 这里根据自己业务逻辑处理会员进场和动画，示例工程已将会员进场动画和逻辑删除
                        }
                        continue;
                        //点赞消息
                    case Constants.MSG_CUSTOM_PRICE:
                        clickHeart();
                        continue;
                        //观众离开,人数发生了变化
                    case Constants.MSG_CUSTOM_REDUCE_USER:
                        if (null != mOnlineNumber)
                            mOnlineNumber.setText(AppUtils.getInstance().formatWan(customMsgInfo.getOnlineNumer(),true)+"人");
                        continue;
                        //观众列表发生了变化
                    case Constants.MSG_CUSTOM_TOP_USER:

                        continue;
                        //礼物信息
                    case Constants.MSG_CUSTOM_GIFT:
                        if(null!=giftInfo){
                            //过滤来自远程自己发送的礼物消息
                            if(isSystemPro && UserManager.getInstance().getUserId().equals(customMsgInfo.getSendUserID())){
                                continue;
                            }
                            if (null != mGiftGroupManager) {
                                mGiftGroupManager.addGiftAnimationItem(customMsgInfo);
                            }
                        }
                        continue;
                        //中奖信息
                    case Constants.MSG_CUSTOM_ROOM_DRAW:
                        if(null!=mGiftGroupManager) mGiftGroupManager.addGiftAnimationItem(customMsgInfo);
                            continue;
            }
        }
    }

    /**
     * 返回当前直播间推流的时长
     * @return
     */
    @Override
    public long getSecond() {
        return SECOND;
    }

    /**
     * 主播端开始计时
     */
    @Override
    public void startReckonTime(){}

    /**
     * 主播端结束计时
     */
    @Override
    public void stopReckonTime() {}

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    /**
     * 伪onDestroy()，一定要调用
     */
    @Override
    public void onDestroy(){
        stopReckonTime();
        if(null!=mFragment){
            mFragment.dismiss();
            mFragment=null;
        }
        //移除礼物交互监听事件
        GiftBoardManager.getInstance().removeOnGiveGiftListener(this);
        if(null!=mConversationListView) mConversationListView.onDestroy();
        if(null!=mGiftGroupManager) mGiftGroupManager.onDestroy();
        if(null!=mCountdownGiftView) mCountdownGiftView.onDestroy();
        if(null!=mAvatarListAdapter) mAvatarListAdapter.setNewData(null);
        if(null!=mHandler) mHandler.removeMessages(0);
        mAvatarListAdapter=null;mCountdownGiftView=null;mGiftGroupManager=null;mConversationListView=null;
        mHandler=null;mFragment=null;mContext=null;
    }

    public interface OnLiveRoomFunctionListener{
        void backPress();
        void showGift();
        void showInput();
    }

    private OnLiveRoomFunctionListener mFunctionListener;

    public void setFunctionListener(OnLiveRoomFunctionListener functionListener) {
        mFunctionListener = functionListener;
    }
}