package com.android.gift.room.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.android.gift.R;
import com.android.gift.base.BaseRoomActivity;
import com.android.gift.bean.UserInfo;
import com.android.gift.constant.Constants;
import com.android.gift.gift.manager.GiftBoardManager;
import com.android.gift.manager.VibratorManager;
import com.android.gift.room.bean.CustomMsgExtra;
import com.android.gift.room.bean.CustomMsgInfo;
import com.android.gift.room.bean.RoomItem;
import com.android.gift.room.view.VideoLiveControllerView;
import com.android.gift.util.AppUtils;
import com.android.live.player.lib.manager.VideoPlayerManager;
import com.android.live.player.lib.view.VideoPlayerTrack;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/5
 * 礼物交互示例直播间
 */

public class LiveRoomActivity extends BaseRoomActivity<VideoLiveControllerView> {

    private VideoPlayerTrack mPlayerTrack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);
        //直播间控制器初始化
        initRoomViews();
        //视频初始化配置
        VideoPlayerManager.getInstance().setLoop(true);
        VideoPlayerManager.getInstance().setMobileWorkEnable(true);
        mPlayerTrack = (VideoPlayerTrack) findViewById(R.id.video_track);
        mPlayerTrack.setLoop(true);
        onParams(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onParams(intent);
    }

    @Override
    protected void initRoomViews() {
        mController = new VideoLiveControllerView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mController.setLayoutParams(layoutParams);
        super.initRoomViews();
        mController.setFunctionListener(new VideoLiveControllerView.OnLiveRoomFunctionListener() {
            @Override
            public void backPress() {
                finish();
            }

            @Override
            public void showGift(UserInfo anchorUser) {
                showGiftBoard(anchorUser);
            }

            @Override
            public void showInput() {
                showInputMsgDialog();
            }

            @Override
            public void share(String userid) {
//                ShareDialog.getInstance(LiveRoomActivity.this)
//                        .setItems(null)
//                        .setOnItemClickListener(new ShareDialog.OnShareItemClickListener() {
//                            @Override
//                            public void onItemClick(ShareMenuItemInfo shareMenuItemInfo) {
//                                try {
//                                    Intent sendIntent = new Intent();
//                                    //sendIntent.setPackage("com.tencent.mm")
//                                    sendIntent.setAction(Intent.ACTION_SEND);
//                                    sendIntent.putExtra(Intent.EXTRA_TEXT, "我正在使用"+getResources().getString(R.string.app_name)+"观看直播，戳下方链接前往下载：\nhttps://github.com/Yuye584312311/Live/wiki/HistoryVersion");
//                                    sendIntent.setType("text/plain");
//                                    startActivity(Intent.createChooser(sendIntent, "iMusic分享"));
//                                }catch (RuntimeException e){
//                                    e.printStackTrace();
//                                    Toast.makeText(LiveRoomActivity.this,"未找到合适的分享程序",Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        })
//                        .show();
                try {
                    Intent sendIntent = new Intent();
                    //sendIntent.setPackage("com.tencent.mm")
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "我正在使用"+getResources().getString(R.string.app_name)+"观看直播，戳下方链接前往下载：\nhttps://github.com/Yuye584312311/Live/wiki/HistoryVersion");
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "iLive分享"));
                }catch (RuntimeException e){
                    e.printStackTrace();
                    Toast.makeText(LiveRoomActivity.this,"未找到合适的分享程序",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 解析参数并准备播放
     * @param intent
     */
    private void onParams(Intent intent) {
        //直播间入参
        RoomItem roomItem = intent.getParcelableExtra("roomItem");
        if(null==roomItem){
            Toast.makeText(this,"参数错误",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mRoomid=roomItem.getRoomid();
        //清屏
        if(null!=mController){
            mController.onReset();
            mController.setAnchorData(roomItem.getAnchor());
            mController.setOnLinesNumber(roomItem.getOnlineNumber());
        }
        //视频画面清屏
        VideoPlayerManager.getInstance().reset();
        mPlayerTrack.setVideoCover(roomItem.getRoom_front(),false);
        //在聊天列表中增加一条本地系统消息
        CustomMsgExtra sysMsg=new CustomMsgExtra();
        sysMsg.setCmd(Constants.MSG_CUSTOM_NOTICE);
        sysMsg.setMsgContent("系统公告：此项目直播流取自映客API，为非商业演示用途项目。严禁用于任何商业活动中！后果自负！");
        CustomMsgInfo customInfo = AppUtils.getInstance().packMessage(sysMsg, null);
        newSystemCustomMessage(customInfo,false);
        //开始拉流
        mPlayerTrack.startPlayer(roomItem.getStream_url());
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
    protected void onDestroy() {
        VideoPlayerManager.getInstance().onDestroy();
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //震动持有释放
        VibratorManager.getInstance().onDestroy();
    }
}