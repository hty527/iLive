package com.android.gift.gift.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.gift.R;
import com.android.gift.gift.bean.GiftItemInfo;
import com.android.gift.gift.bean.GiftType;
import com.android.gift.gift.contract.GiftContact;
import com.android.gift.gift.presenter.GiftPresenter;
import com.android.gift.util.AppUtils;
import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/6/20
 * 与用户交互的UI组件
 */

public class GiftLayout extends FrameLayout implements GiftContact.View {

    private final GiftPresenter mPresenter;
    private final TextView mTextDesp;

    public GiftLayout(Context context) {
        this(context,null);
    }

    public GiftLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GiftLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_gift_layout,this);
        mPresenter = new GiftPresenter();
        mPresenter.attachView(this);
        mPresenter.getGiftsType(getContext().getApplicationContext());
        mTextDesp = (TextView) findViewById(R.id.gift_tv_desp);
    }

    /**
     * 设置礼物描述
     * @param text
     */
    public void setGiftDesp(String text){
        if(null!=mTextDesp){
            mTextDesp.setText(Html.fromHtml(text));
        }
    }

    /**
     * 礼物分类、分类下列表加载中
     */
    @Override
    public void showLoading(int offset) {}

    @Override
    public void showGiftTypes(List<GiftType> data) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.gift_view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.gift_tab_layout);
        viewPager.getLayoutParams().height= AppUtils.getInstance().getScreenWidth(getContext())/2+AppUtils.getInstance().dpToPxInt(getContext(),25f);
        if(null!=viewPager){
            GiftPagerAdapter adapter = new GiftPagerAdapter(data);
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(data.size()-1);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public void showError(int code, String errorMsg) {}
    @Override
    public void showGiftTypesError(int code, String errMsg) {}
    @Override
    public void showGifts(List<GiftItemInfo> data, String type) {}
    @Override
    public void showGiftError(int code, String type, String errMsg) {}

    /**
     * 礼物面板分页适配器
     */
    private class GiftPagerAdapter extends PagerAdapter {

        private final List<GiftType> mData;

        public GiftPagerAdapter(List<GiftType> data) {
            this.mData=data;
        }

        @Override
        public int getCount() {
            return null==mData?0:mData.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null==mData?"":mData.get(position).getTitle();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GiftType giftTypeInfo = mData.get(position);
            if(null!=giftTypeInfo){
                GiftBoardView giftBoardView = new GiftBoardView(getContext());
                giftBoardView.setBoardIndex(position);
                giftBoardView.setGiftTypeInfo(giftTypeInfo);
                container.addView(giftBoardView);
                return giftBoardView;
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(null!=container){
                container.removeView(container.findViewById(position));
            }
        }
    }
}