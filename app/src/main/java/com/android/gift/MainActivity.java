package com.android.gift;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.android.gift.bean.UserInfo;
import com.android.gift.gift.GiftDataCache;
import com.android.gift.gift.dialog.LiveGiftDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //先初始化好礼物交互面板，等到需要展示的时候直接显示出来
        GiftDataCache.getInstance().init(this);
    }

    public void showGift(View view) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserid("234353455");
        userInfo.setNickName("刘亦菲");
        LiveGiftDialog.getInstance(this,userInfo,"er43te5yttry").show();
    }

    public void startRoom(View view) {}
}