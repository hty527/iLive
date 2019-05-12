package com.android.gift;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.bean.GiftType;
import com.android.gift.gift.contract.GiftContact;
import com.android.gift.gift.presenter.GiftPresenter;
import com.android.gift.util.Logger;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GiftContact.View {

    private static final String TAG = "MainActivity";
    private GiftPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new GiftPresenter();
        mPresenter.attachView(this);
    }

    public void showGift(View view) {
        mPresenter.getGiftsType(getApplicationContext());
    }

    public void startRoom(View view) {
        mPresenter.getGiftsByType(getApplicationContext(),"1");
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError(int code, String errorMsg) {

    }

    @Override
    public void showGiftTypes(List<GiftType> data) {
        Logger.d(TAG,"showGiftTypes-->"+data.toString());
    }

    @Override
    public void showGiftTypesError(int code, String errMsg) {

    }

    @Override
    public void showGifts(List<GiftItemInfo> data, String type) {
        Logger.d(TAG,"showGifts-->"+data.toString());
    }

    @Override
    public void showGiftError(int code, String type, String errMsg) {

    }

    @Override
    public void showGivePresentSuccess(GiftItemInfo giftItemInfo, int giftCount, boolean isDoubleClick) {

    }

    @Override
    public void showGivePresentError(int code, String errMsg) {

    }
}