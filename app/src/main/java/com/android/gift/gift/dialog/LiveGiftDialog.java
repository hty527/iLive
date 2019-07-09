package com.android.gift.gift.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.gift.R;
import com.android.gift.bean.UserInfo;
import com.android.gift.gift.manager.GiftBoardManager;
import com.android.gift.gift.view.GiftLayout;

/**
 * Created by TinyHung@outlook.com
 * 2019/6/20
 * 礼物面板弹窗UI交互示例容器
 */

public class LiveGiftDialog extends AppCompatDialog{

    private FrameLayout mGtiftLayout;

    public static LiveGiftDialog getInstance(Context context){
        return new LiveGiftDialog(context);
    }

    public LiveGiftDialog(@NonNull Context context) {
        super(context, R.style.ButtomDialogTransparentAnimationStyle);
        setContentView(R.layout.dialog_gift_group);
        initLayoutPrams();
        initViews();
    }

    private void initViews() {
        findViewById(R.id.dig_btn_sned).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiftBoardManager.getInstance().sendGift();
            }
        });
        //礼物交互面板初始化
        mGtiftLayout = (FrameLayout) findViewById(R.id.gift_layout);
        GiftLayout giftLayout= GiftBoardManager.getInstance().getGiftView(getContext());
        mGtiftLayout.removeAllViews();
        if(null!=giftLayout.getParent()){
            ViewGroup parent = (ViewGroup) giftLayout.getParent();
            parent.removeView(giftLayout);
        }
        mGtiftLayout.addView(giftLayout);
    }

    public void setSendeeUser(UserInfo userInfo) {
        GiftBoardManager.getInstance().setReceiveUser(userInfo);
        if(null!=userInfo){
            ((TextView) findViewById(R.id.dig_sned_user)).setText("送给："+userInfo.getNickName());
        }
    }

    public void setSendeeRoomID(String roomID) {
        GiftBoardManager.getInstance().setReceiveRoomID(roomID);
    }

    protected void initLayoutPrams(){
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        WindowManager.LayoutParams attributes = window.getAttributes();//得到布局管理者
        WindowManager systemService = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);//得到窗口管理者
        DisplayMetrics displayMetrics=new DisplayMetrics();//创建设备屏幕的管理者
        systemService.getDefaultDisplay().getMetrics(displayMetrics);//得到屏幕的宽高
        attributes.height= FrameLayout.LayoutParams.WRAP_CONTENT;
        attributes.width= systemService.getDefaultDisplay().getWidth();
        attributes.gravity= Gravity.BOTTOM;
    }

    /**
     * 设置是否自动选中
     * @param autoSelected true：自动选中第一个
     */
    public void setAutoSelected(boolean autoSelected) {
        GiftBoardManager.getInstance().setAutoSelected(autoSelected);
    }

    @Override
    public void show() {
        super.show();
        GiftBoardManager.getInstance().onResume();
    }

    public void destroy(){
        if(null!=mGtiftLayout){
            mGtiftLayout.removeAllViews();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        GiftBoardManager.getInstance().onPause();
    }
}