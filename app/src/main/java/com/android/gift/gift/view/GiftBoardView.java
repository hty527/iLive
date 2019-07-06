package com.android.gift.gift.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.android.gift.R;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.bean.GiftType;
import com.android.gift.gift.manager.GiftBoardManager;
import com.android.gift.gift.adapter.GiftItemAdapter;
import com.android.gift.gift.contract.GiftContact;
import com.android.gift.gift.presenter.GiftPresenter;
import com.android.gift.util.AppUtils;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by TinyHung@outlook.com
 * 2019/6/20
 * 礼物交互面板
 */

public class GiftBoardView extends FrameLayout implements GiftContact.View {

    private final GiftPresenter mPresenter;
    private LinearLayout mDotRootView;
    private GiftPagerAdapter mAdapter;

    public GiftBoardView(@NonNull Context context) {
        this(context,null);
    }

    public GiftBoardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GiftBoardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_gift_board,this);
        mPresenter = new GiftPresenter();
        mPresenter.attachView(this);
    }

    /**
     * 绑定礼物分类，根据分类获取礼物列表
     * @param giftTypeInfo
     */
    public void setGiftTypeInfo(GiftType giftTypeInfo) {
        if(null!=giftTypeInfo&&null!=mPresenter){
            mPresenter.getGiftsByType(getContext(),giftTypeInfo.getId()+"");
        }
    }

    //=========================================礼物列表渲染及交互=====================================

    /**
     * 礼物分类、分类下列表加载中
     */
    @Override
    public void showLoading() {}

    /**
     * 礼物数据获取成功
     * @param data 礼物列表
     * @param type 礼物类别
     */
    @Override
    public void showGifts(List<GiftItemInfo> data, String type) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.gift_view_pager);
        if(null!=viewPager){
            viewPager.getLayoutParams().height= AppUtils.getScreenWidth(getContext())/2;
            viewPager.addOnPageChangeListener(onPageChangeListener);
            TreeMap<Integer, List<GiftItemInfo>> subGroupGift = AppUtils.subGroupGift(data, 8);
            mAdapter = new GiftPagerAdapter(subGroupGift);
            viewPager.setOffscreenPageLimit(5);
            viewPager.setAdapter(mAdapter);
            addDots(subGroupGift.size());
        }
    }

    /**
     * 礼物获取失败
     * @param code 错误码，3002 为空，其他为失败
     * @param type 礼物类别
     * @param errMsg 描述信息
     */
    @Override
    public void showGiftError(int code, String type, String errMsg) {

    }

    /**
     * 绘制页眉小圆点
     * @param gifNum
     */
    private void addDots(int gifNum) {
        int pxFor10Dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        int pxFor8Dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        mDotRootView = (LinearLayout) findViewById(R.id.gift_dot_view);
        mDotRootView.removeAllViews();
        for (int i=0;i<gifNum;i++) {
            View dot = new View(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pxFor8Dp, pxFor8Dp);
            layoutParams.setMargins(0, 0, pxFor10Dp, 0);
            dot.setLayoutParams(layoutParams);
            dot.setBackgroundResource(R.drawable.dot_arl_selector);
            mDotRootView.addView(dot);
        }
        if(null!=onPageChangeListener) onPageChangeListener.onPageSelected(0);
    }

    /**
     * 礼物面板分页片段适配器
     */
    private class GiftPagerAdapter extends PagerAdapter {

        private final TreeMap<Integer, List<GiftItemInfo>> mGiftLists;

        public GiftPagerAdapter(TreeMap<Integer, List<GiftItemInfo>> subGroupGift) {
            this.mGiftLists=subGroupGift;
        }

        @Override
        public int getCount() {
            return mGiftLists == null ? 0 : mGiftLists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GiftBoardLayoutItem item = new GiftBoardLayoutItem(mGiftLists.get(position),position);
            container.addView(item.getView());
            return item.getView();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            if(null!=mDotRootView&&mDotRootView.getChildCount()>0&&null!=mAdapter){
                for (int i = 0; i < mAdapter.getCount(); i++) {
                    mDotRootView.getChildAt(i).setSelected(i == position);
                }
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {}
    };


    /**
     * 分页礼物
     */
    private class GiftBoardLayoutItem {

        private View mView;

        /**
         * @param giftInfos 数据
         * @param position 子界面位置
         */
        public GiftBoardLayoutItem(List<GiftItemInfo> giftInfos, int position) {
            mView = View.inflate(getContext(), R.layout.view_gift_board_item, null);
            RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_gift_item);
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            GiftItemAdapter adapter= new GiftItemAdapter(giftInfos,getContext());
            adapter.setOnItemClickListener(new GiftItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int poistion, View view, GiftItemInfo giftInfo) {
                    selectedChangedGift(poistion,view,giftInfo);
                }
            });
            recyclerView.setAdapter(adapter);
        }

        /**
         * 返回View
         * @return
         */
        public View getView() {
            return mView;
        }

        /**
         * ItemAdapter 发生了点击事件
         * @param poistion 当前点击的Item位置
         * @param view 当前点击的ItemView
         * @param giftInfo 当前点击的礼物
         */
        private void selectedChangedGift(int poistion, View view, GiftItemInfo giftInfo) {
            GiftBoardManager.getInstance().onClick(view,giftInfo);
        }
    }

    @Override
    public void showError(int code, String errorMsg) {}
    @Override
    public void showGiftTypes(List<GiftType> data) {}
    @Override
    public void showGiftTypesError(int code, String errMsg) {}
}