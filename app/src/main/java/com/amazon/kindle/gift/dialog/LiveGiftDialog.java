package com.amazon.kindle.gift.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.amazon.kindle.gift.manager.GiftBoardManager;
import com.amazon.kindle.util.Logger;
import com.amazon.kindle.R;
import com.amazon.kindle.bean.UserInfo;
import com.amazon.kindle.gift.view.GiftLayout;

/**
 * Created by TinyHung@outlook.com
 * 2019/6/20
 * 礼物面板弹窗UI交互示例容器
 */

public class LiveGiftDialog extends AppCompatDialog{

    private static final String TAG = "LiveGiftDialog";
    private FrameLayout mGtiftLayout;
    //当未选中任何礼物时，是否自动选中某个礼物
    private boolean mAutoSelectedEnable=false;

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
     * 绑定接收人
     * @param userInfo
     * @return
     */
    public LiveGiftDialog setReceiveUserInfo(UserInfo userInfo) {
        GiftBoardManager.getInstance().setReceiveUser(userInfo);
        if(null!=userInfo){
            ((TextView) findViewById(R.id.dig_sned_user)).setText(Html.fromHtml("送给：<font color='#EFA109'>"+userInfo.getNickName()+"</font>"));
        }
        return this;
    }

    /**
     * 绑定接收礼物的房间ID
     * @param roomID
     * @return
     */
    public LiveGiftDialog setReceiveRoomID(String roomID) {
        GiftBoardManager.getInstance().setReceiveRoomID(roomID);
        return this;
    }

    /**
     * 设置打开礼物面板时，若无选中的礼物时，是否允许自动选中第一页第0个
     * @param enable 是否自动选中 true:自动选中
     * @return
     */
    public LiveGiftDialog setAutoSelectedEnable(boolean enable){
        this.mAutoSelectedEnable=enable;
        return this;
    }

    @Override
    public void show() {
        super.show();
        GiftBoardManager.getInstance().onResume();
        if(mAutoSelectedEnable){
            Logger.d(TAG,"init:"+GiftBoardManager.getInstance().isInitFinish());
            if(GiftBoardManager.getInstance().isInitFinish()){
                GiftBoardManager.getInstance().defaultSelectedIndex();
            }else{
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GiftBoardManager.getInstance().defaultSelectedIndex();
                    }
                },300);
            }
        }
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