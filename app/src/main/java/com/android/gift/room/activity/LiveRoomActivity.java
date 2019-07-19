package com.android.gift.room.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.android.gift.R;
import com.android.gift.bean.UserInfo;
import com.android.gift.constant.Constants;
import com.android.gift.gift.dialog.LiveGiftDialog;
import com.android.gift.gift.manager.GiftBoardManager;
import com.android.gift.manager.VibratorManager;
import com.android.gift.room.bean.CustomMsgExtra;
import com.android.gift.room.bean.CustomMsgInfo;
import com.android.gift.room.bean.RoomItem;
import com.android.gift.room.doalog.InputKeyBoardDialog;
import com.android.gift.room.view.VideoLiveControllerView;
import com.android.gift.util.AppUtils;
import com.android.gift.util.ScreenLayoutChangedHelp;
import com.android.live.player.lib.manager.VideoPlayerManager;
import com.android.live.player.lib.view.VideoPlayerTrack;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/5
 * 礼物交互示例直播间
 */

public class LiveRoomActivity extends AppCompatActivity {

    //礼物面板
    private LiveGiftDialog mGiftDialog;
    //直播间控制器
    private VideoLiveControllerView mControllerView;
    //输入框
    private InputKeyBoardDialog mInputTextMsgDialog;
    //输入框高度监听
    private ScreenLayoutChangedHelp mLayoutChangedListener;
    private VideoPlayerTrack mPlayerTrack;
    private String roomid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        //直播间入参
        RoomItem roomItem = getIntent().getParcelableExtra("roomItem");
        if(null==roomItem){
            Toast.makeText(this,"参数错误",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        roomid=roomItem.getRoomid();
        //保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_live_room);
        //直播间交互控制器
        mControllerView = findViewById(R.id.live_controller);
        mControllerView.setAnchorData(roomItem.getAnchor());
        mControllerView.setOnLinesNumber(roomItem.getOnlineNumber());
        mControllerView.setFunctionListener(new VideoLiveControllerView.OnLiveRoomFunctionListener() {
            @Override
            public void backPress() {
                finish();
            }

            @Override
            public void showGift(UserInfo anchorUser) {
                if(null==mGiftDialog){
                    mGiftDialog = LiveGiftDialog.getInstance(LiveRoomActivity.this);
                    mGiftDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if(null!=mControllerView){
                                mControllerView.closeGiftBoard();
                            }
                        }
                    });
                }
                mGiftDialog.setReceiveUserInfo(anchorUser)
                        .setReceiveRoomID(roomid)
                        .setAutoSelectedEnable(true)
                        .show();
                mControllerView.showGiftBoard();
            }

            @Override
            public void showInput() {
                showInputMsgDialog();
            }
        });
        //视频准备
        VideoPlayerManager.getInstance().setLoop(true);
        VideoPlayerManager.getInstance().setMobileWorkEnable(true);
        mPlayerTrack = (VideoPlayerTrack) findViewById(R.id.video_track);
        mPlayerTrack.setLoop(true);
        mPlayerTrack.setVideoCover(roomItem.getRoom_front(),false);
        //在聊天列表中增加一条本地系统消息
        CustomMsgExtra sysMsg=new CustomMsgExtra();
        sysMsg.setCmd(Constants.MSG_CUSTOM_NOTICE);
        sysMsg.setMsgContent("系统公告：此项目直播视频取自映客API，为非商业演示项目。对于用于商业活动带来的一切后果自行承担！若有其他问题，请联系开发者！邮箱：TinyHung@Outlook.com");
        CustomMsgInfo customInfo = AppUtils.getInstance().packMessage(sysMsg, null);
        newSystemCustomMessage(customInfo,false);

        //软键盘高度检测
        mLayoutChangedListener= ScreenLayoutChangedHelp.get(LiveRoomActivity.this).setOnSoftKeyBoardChangeListener(new ScreenLayoutChangedHelp.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                if(null!=mControllerView) mControllerView.showInputKeyBord(true,height);
            }

            @Override
            public void keyBoardHide(int height) {
                if(null!=mControllerView) mControllerView.showInputKeyBord(false,height);
            }
        });
        //开始拉流
        mPlayerTrack.startPlayer(roomItem.getStream_url());
    }

    /**
     * 收到新的消息交给控制器统一处理刷新
     * 播放礼物动画
     * @param customMsgInfo
     * @param isSystemPro   是否来系统推送的
     */
    protected void newSystemCustomMessage(CustomMsgInfo customMsgInfo, boolean isSystemPro) {
        if (null == customMsgInfo) return;
        if (null != mControllerView) mControllerView.newSystemCustomMessage(customMsgInfo, isSystemPro);
    }

    /**
     * 发消息弹出框
     */
    protected void showInputMsgDialog() {
        if(null==mInputTextMsgDialog){
            //输入框
            mInputTextMsgDialog = InputKeyBoardDialog.getInstance(this)
                    .setBackgroundWindown(0.0f)
                    .setHintText("请输入聊天内容")
                    .setMode(1)
                    .setOnActionFunctionListener(new InputKeyBoardDialog.OnActionFunctionListener() {
                        //提交发射
                        @Override
                        public void onSubmit(String content, boolean tanmuOpen) {
                            CustomMsgInfo customMsgInfo=new CustomMsgInfo(0);
                            customMsgInfo.setChildCmd(Constants.MSG_CUSTOM_TEXT);
                            customMsgInfo.setMsgContent(content);
                            customMsgInfo.setTanmu(tanmuOpen);
                            if(null!=mControllerView){
                                mControllerView.onNewTextMessage(customMsgInfo,false);
                            }
                        }
                        //告知输入框是否可发射状态
                        @Override
                        public boolean isAvailable() {
                            return true;
                        }
                    });
            mInputTextMsgDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if(null!=mControllerView) mControllerView.showInputKeyBord(false,0);
                }
            });
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();
            lp.width = (int) (display.getWidth()); //设置宽度
            mInputTextMsgDialog.getWindow().setAttributes(lp);
            mInputTextMsgDialog.setCancelable(true);
            mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        mInputTextMsgDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(null!=mGiftDialog&&mGiftDialog.isShowing()){
            //礼物动画开始
            GiftBoardManager.getInstance().onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //礼物动画暂停
        GiftBoardManager.getInstance().onPause();
    }

    @Override
    public void finish() {
        //视频播放销毁
        VideoPlayerManager.getInstance().onDestroy();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //视频播放销毁
        VideoPlayerManager.getInstance().onDestroy();
        //震动持有释放
        VibratorManager.getInstance().onDestroy();
        if(null!=mGiftDialog&&mGiftDialog.isShowing()){
            mGiftDialog.destroy();
            mGiftDialog.dismiss();
            mGiftDialog=null;
        }
        //控制器机内部销毁，礼物动画回收也在此方法内部进行
        if(null!=mControllerView){
            mControllerView.onDestroy();
            mControllerView=null;
        }
        if(null!= mLayoutChangedListener) {
            mLayoutChangedListener.onDestroy();
            mLayoutChangedListener=null;
        }
        if(null!=mInputTextMsgDialog){
            mInputTextMsgDialog.destroy();
            mInputTextMsgDialog.dismiss();
            mInputTextMsgDialog=null;
        }
    }
}