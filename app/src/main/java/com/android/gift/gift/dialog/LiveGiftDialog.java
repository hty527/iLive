package com.android.gift.gift.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.gift.R;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.bean.GiftType;
import com.android.gift.bean.UserInfo;
import com.android.gift.gift.GiftDataCache;
import com.android.gift.gift.contract.GiftContact;
import com.android.gift.gift.presenter.GiftPresenter;
import com.android.gift.gift.view.GiftLayout;
import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/6/20
 * 礼物面板容器
 */

public class LiveGiftDialog extends AppCompatDialog implements GiftContact.View {

    private final UserInfo mUserInfo;
    private final String mRoomid;
    private final GiftPresenter mPresenter;
    private FrameLayout mGtiftLayout;

    public static LiveGiftDialog getInstance(Context context, UserInfo userInfo, String roomid){
        return new LiveGiftDialog(context,userInfo,roomid);
    }

    public LiveGiftDialog(@NonNull Context context,UserInfo userInfo,String roomid) {
        super(context, R.style.ButtomDialogTransparentAnimationStyle);
        this.mUserInfo=userInfo;
        this.mRoomid=roomid;
        setContentView(R.layout.dialog_gift_group);
        initLayoutPrams();
        mPresenter = new GiftPresenter();
        mPresenter.attachView(this);
        initViews();
    }

    private void initViews() {
        if(null!=mUserInfo){
            ((TextView) findViewById(R.id.dig_sned_user)).setText("送给："+mUserInfo.getNickName());
            View.OnClickListener onClickListener=new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            };
            findViewById(R.id.dig_btn_sned).setOnClickListener(onClickListener);
            //礼物交互面板初始化
            mGtiftLayout = (FrameLayout) findViewById(R.id.gift_layout);
            GiftLayout giftLayout=GiftDataCache.getInstance().getGiftView(getContext());
            mGtiftLayout.removeAllViews();
            mGtiftLayout.addView(giftLayout);
        }
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

    @Override
    public void showLoading() {}
    @Override
    public void showError(int code, String errorMsg) {}
    @Override
    public void showGiftTypes(List<GiftType> data) {}
    @Override
    public void showGiftTypesError(int code, String errMsg) {}
    @Override
    public void showGifts(List<GiftItemInfo> data, String type) {}
    @Override
    public void showGiftError(int code, String type, String errMsg) {}
    @Override
    public void showGivePresentSuccess(GiftItemInfo giftItemInfo, int giftCount, boolean isDoubleClick) {

    }

    @Override
    public void showGivePresentError(int code, String errMsg) {

    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(null!=mGtiftLayout){
            mGtiftLayout.removeAllViews();
        }
    }
}