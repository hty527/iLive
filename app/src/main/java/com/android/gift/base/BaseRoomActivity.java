package com.android.gift.base;

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
import android.widget.RelativeLayout;
import com.android.gift.R;
import com.android.gift.bean.UserInfo;
import com.android.gift.constant.Constants;
import com.android.gift.gift.dialog.LiveGiftDialog;
import com.android.gift.room.bean.CustomMsgInfo;
import com.android.gift.room.doalog.InputKeyBoardDialog;
import com.android.gift.util.ScreenLayoutChangedHelp;

/**
 * Created by TinyHung@outlook.com
 * 2019/9/11
 * 为满足不同类型的直播间诸如：纯音频、视频通话、秀场直播等场景需要，特封装直播间统一父类，
 * 控制器基类为RoomBaseController，请参考LiveRoomActivity使用方法。
 */

public class BaseRoomActivity<C extends RoomBaseController> extends AppCompatActivity {

    //直播间控制器
    protected C mController;
    //输入框高度监听
    private ScreenLayoutChangedHelp mLayoutChangedListener;
    //输入框
    private InputKeyBoardDialog mInputTextMsgDialog;
    //礼物面板
    protected LiveGiftDialog mGiftDialog;
    //房间ID
    protected String mRoomid;

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
        //保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //软键盘高度检测
        mLayoutChangedListener= ScreenLayoutChangedHelp.get(BaseRoomActivity.this)
                .setOnSoftKeyBoardChangeListener(new ScreenLayoutChangedHelp.OnSoftKeyBoardChangeListener() {
                    @Override
                    public void keyBoardShow(int height) {
                        if(null!=mController) mController.showInputKeyBord(true,height);
                    }

                    @Override
                    public void keyBoardHide(int height) {
                        if(null!=mController) mController.showInputKeyBord(false,height);
                    }
                });
    }

    /**
     * 直播间初始化，子类初始化调用
     */
    protected void initRoomViews(){
        if(null!=mController){
            //初始化直播间控制器
            RelativeLayout videoController = (RelativeLayout) findViewById(R.id.video_controller);
            videoController.addView(mController);
        }
    }

    protected void showGiftBoard(UserInfo anchorUser){
        if(null==mGiftDialog){
            mGiftDialog = LiveGiftDialog.getInstance(BaseRoomActivity.this);
            mGiftDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if(null!=mController){
                        mController.closeGiftBoard();
                    }
                }
            });
        }
        mGiftDialog.setReceiveUserInfo(anchorUser)
                .setReceiveRoomID(mRoomid)
                .setAutoSelectedEnable(true)
                .show();
        if(null!=mController){
            mController.showGiftBoard();
        }
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
                            if(null!=mController){
                                mController.onNewTextMessage(customMsgInfo,false);
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
                    if(null!=mController) mController.showInputKeyBord(false,0);
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

    /**
     * 收到新的消息交给控制器统一处理刷新
     * 播放礼物动画
     * @param customMsgInfo
     * @param isSystemPro   是否来系统推送的
     */
    protected void newSystemCustomMessage(CustomMsgInfo customMsgInfo, boolean isSystemPro) {
        if (null == customMsgInfo) return;
        if (null != mController) mController.newSystemCustomMessage(customMsgInfo, isSystemPro);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!= mLayoutChangedListener) {
            mLayoutChangedListener.onDestroy();
            mLayoutChangedListener=null;
        }
        if(null!=mInputTextMsgDialog){
            mInputTextMsgDialog.destroy();
            mInputTextMsgDialog.dismiss();
            mInputTextMsgDialog=null;
        }
        //控制器机内部销毁，礼物动画回收也在此方法内部进行
        if(null!=mController){
            mController.onDestroy();
            mController=null;
        }
        if(null!=mGiftDialog&&mGiftDialog.isShowing()){
            mGiftDialog.destroy();
            mGiftDialog.dismiss();
        }
        mGiftDialog=null;
    }
}