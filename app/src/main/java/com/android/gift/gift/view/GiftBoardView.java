package com.android.gift.gift.view;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.gift.R;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.bean.GiftType;
import com.android.gift.gift.GiftItemAdapter;
import com.android.gift.gift.contract.GiftContact;
import com.android.gift.gift.presenter.GiftPresenter;
import com.android.gift.util.AppUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.opensource.svgaplayer.SVGAImageView;
import java.io.File;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by TinyHung@outlook.com
 * 2019/6/20
 */

public class GiftBoardView extends FrameLayout implements GiftContact.View {

    private final GiftPresenter mPresenter;
    private int mCurrentPosition;

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

    public void setGiftTypeInfo(GiftType giftTypeInfo) {
        if(null!=giftTypeInfo&&null!=mPresenter){
            mPresenter.getGiftsByType(getContext(),giftTypeInfo.getId()+"");
        }
    }

    public void setPosition(int position) {
        this.mCurrentPosition=position;
    }

    /**
     * 礼物数据
     * @param data 礼物列表
     * @param type 礼物类别
     */
    @Override
    public void showGifts(List<GiftItemInfo> data, String type) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.gift_view_pager);
        if(null!=viewPager){
            TreeMap<Integer, List<GiftItemInfo>> subGroupGift = AppUtils.subGroupGift(data, 8);
            GiftPagerAdapter adapter=new GiftPagerAdapter(subGroupGift);
            viewPager.setAdapter(adapter);
        }
    }

    @Override
    public void showGiftError(int code, String type, String errMsg) {

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

    /**
     * 分页礼物
     */
    private class GiftBoardLayoutItem {

        private View mView;

        /**
         *
         * @param giftInfos 数据
         * @param position 子界面位置
         */
        public GiftBoardLayoutItem(List<GiftItemInfo> giftInfos, int position) {
            mView = View.inflate(getContext(), R.layout.view_gift_board_item, null);
            RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_gift_item);
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            final GiftItemAdapter adapter= new GiftItemAdapter(giftInfos,getContext());
            adapter.setOnItemClickListener(new GiftItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int poistion, View view, GiftItemInfo giftInfo) {
                    if(null!=adapter&&adapter.getData().size()>poistion){
                        selectedChangedGift(poistion,view,giftInfo);
                    }
                }
            });
            recyclerView.setAdapter(adapter);
            //在用户无操作礼物面板的情况下默认是第一个被选中
//            if(mIsRecovery&&!GiftHelpManager.getInstance().isExitRecoveryState()&&0==position&&0==currentFragmentIndex){
//                new android.os.Handler().postAtTime(new Runnable() {
//                    @Override
//                    public void run() {
//                        View viewByPosition = gridLayoutManager.findViewByPosition(0);
//                        if(null!=viewByPosition&&null!=viewByPosition.getTag()){
//                            GiftInfo giftInfo = (GiftInfo) viewByPosition.getTag();
//                            clickSelectedIndex=0;
//                            clickSelectedIndex--;//避免还原选中状态，数量又加上去了。。。，这里的数量永远不会小于0
//                            currentView=viewByPosition;
//                            giftInfo.setSelector(true);
//                            setSelected(currentView,giftInfo,true);
//                        }
//                    }
//                }, SystemClock.uptimeMillis()+500);
//            }
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

        }
    }

    /**
     * ItemAdapter 更新此控件是否选中
     * @param view itemView
     * @param giftInfo 礼物与ItemView绑定的元素
     * @param selected 是否选中
     */
    public void setSelected(View view,GiftItemInfo giftInfo,boolean selected){

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
    public void showGivePresentSuccess(GiftItemInfo giftItemInfo, int giftCount, boolean isDoubleClick) {}
    @Override
    public void showGivePresentError(int code, String errMsg) {}
}
