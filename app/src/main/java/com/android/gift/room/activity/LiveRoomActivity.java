package com.android.gift.room.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.gift.R;
import com.android.gift.bean.UserInfo;
import com.android.gift.gift.dialog.LiveGiftDialog;
import com.android.gift.gift.manager.GiftBoardManager;
import com.android.gift.room.view.VideoLiveControllerView;
import com.video.player.lib.constants.VideoConstants;
import com.video.player.lib.controller.DefaultCoverController;
import com.video.player.lib.manager.VideoPlayerManager;
import com.video.player.lib.view.VideoPlayerTrackView;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/5
 * 直播间
 */

public class LiveRoomActivity extends AppCompatActivity {

    private static final String TAG = "LiveRoomActivity";
    //礼物面板
    private LiveGiftDialog mGiftDialog;
    private VideoLiveControllerView mControllerView;

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

        setContentView(R.layout.activity_live_room);
        //直播间控制器准备
        mControllerView = findViewById(R.id.live_controller);
        mControllerView.setFunctionListener(new VideoLiveControllerView.OnLiveRoomFunctionListener() {
            @Override
            public void onBack() {
                finish();
            }

            @Override
            public void onGift() {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserid("234353455");
                userInfo.setNickName("刘亦菲");
                mGiftDialog = LiveGiftDialog.getInstance(LiveRoomActivity.this, userInfo, "er43te5yttrywrer4t");
                mGiftDialog.show();
            }
        });
        //视频准备
        VideoPlayerManager.getInstance().setLoop(true);
        VideoPlayerManager.getInstance().setVideoDisplayType(VideoConstants.VIDEO_DISPLAY_TYPE_CUT);
        VideoPlayerTrackView playerTrackView = (VideoPlayerTrackView) findViewById(R.id.video_track);
        DefaultCoverController coverController = playerTrackView.getCoverController();
        if(null!=coverController){
            //http://ht.tn990.com/uploads/appupload/y44fbnxmsfp8Qci4rf_1548388204.jpg
            coverController.mVideoCover.setImageResource(R.drawable.cover_video);
        }
        playerTrackView.startPlayVideo("http://s.tn990.com/zb/video/9d0ba1260e72b0c856d471644473def0.mp4","测试");
    }

    @Override
    protected void onResume() {
        super.onResume();
        VideoPlayerManager.getInstance().onResume();
        if(null!=mGiftDialog&&mGiftDialog.isShowing()){
            GiftBoardManager.getInstance().onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        VideoPlayerManager.getInstance().onPause();
        GiftBoardManager.getInstance().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoPlayerManager.getInstance().onDestroy();
        if(null!=mGiftDialog&&mGiftDialog.isShowing()){
            mGiftDialog.dismiss();
            mGiftDialog=null;
        }
        if(null!=mControllerView){
            mControllerView.onDestroy();
            mControllerView=null;
        }
    }
}