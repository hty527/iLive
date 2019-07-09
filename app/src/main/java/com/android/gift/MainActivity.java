package com.android.gift;

import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.bean.UserInfo;
import com.android.gift.gift.listener.OnGiveGiftListener;
import com.android.gift.gift.manager.GiftBoardManager;
import com.android.gift.gift.dialog.LiveGiftDialog;
import com.android.gift.room.activity.LiveRoomActivity;
import com.android.gift.util.Logger;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //配置SVGA缓存
        File cacheDir = new File(this.getApplicationContext().getCacheDir(), "http");
        try {
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d(TAG,"INIT CACHE ERROR:"+e.getMessage());
        }
        GiftBoardManager.getInstance()
                //初始化礼物交互面板
                .init(this)
                //设置礼物赠送监听
                .addOnGiveGiftListener(new OnGiveGiftListener() {
                    @Override
                    public void onReceiveGift(GiftItemInfo giftItemInfo, UserInfo userInfo, String roomid, int count) {
                        Logger.d(TAG,"onReceiveGift-->接收人："+userInfo.getUserid()+",群组ID："+roomid+",礼物ID："+giftItemInfo.getId()+",数量："+count);
                    }

                    @Override
                    public void onReceiveNewGift(GiftItemInfo giftItemInfo) {

                    }

                    @Override
                    public void onReceiveGiftCount(int count) {

                    }
                });
    }

    /**
     * 显示礼物面板及交互
     * @param view
     */
    public void showGift(View view) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserid("234353455");
        userInfo.setNickName("刘亦菲");
        userInfo.setAvatar("http://c4.haibao.cn/img/600_0_100_0/1473652712.0005/87c7805c10e60e9a6db94f86d6014de8.jpg");
        LiveGiftDialog instance = LiveGiftDialog.getInstance(this);
        instance.setSendeeUser(userInfo);
        instance.setSendeeRoomID("er43te5yttrywrer4t");
        instance.show();
    }

    /**
     * 前往直播间
     * @param view
     */
    public void startRoom(View view) {
        startActivity(new Intent(this,LiveRoomActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GiftBoardManager.getInstance().onDestroy();
    }
}