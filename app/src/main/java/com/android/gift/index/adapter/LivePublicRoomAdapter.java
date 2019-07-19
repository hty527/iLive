package com.android.gift.index.adapter;

import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.gift.R;
import com.android.gift.index.ui.activity.WebViewActivity;
import com.android.gift.model.GlideImageLoader;
import com.android.gift.room.bean.BannerInfo;
import com.android.gift.room.bean.InkeRoomItem;
import com.android.gift.room.bean.InkeWebItem;
import com.android.gift.room.bean.LiveRoomInfo;
import com.android.gift.util.AppUtils;
import com.android.gift.util.Logger;
import com.android.gift.view.LayoutProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/17
 * 示例在线直播间列表,直播间、Banner、WEB 等多条目类型展示
 */

public class LivePublicRoomAdapter extends BaseMultiItemQuickAdapter<InkeRoomItem,BaseViewHolder> {

    private final int mItemBannerWidth;
    private final int mItemHeight;
    private Banner mBanner,mWebBanner;
    private final int mPxInt5,mPxInt10;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public LivePublicRoomAdapter(List<InkeRoomItem> data) {
        super(data);
        addItemType(InkeRoomItem.ITEM_TYPE_UNKNOWN,R.layout.item_live_public_room_item);
        addItemType(InkeRoomItem.ITEM_TYPE_ROOM,R.layout.item_live_public_room_item);
        addItemType(InkeRoomItem.ITEM_TYPE_BANNER,R.layout.item_live_public_room_item);
        addItemType(InkeRoomItem.ITEM_TYPE_WEB,R.layout.item_live_public_room_item);
        mItemBannerWidth = (AppUtils.getInstance().getScreenWidth(mContext) - AppUtils.getInstance().dpToPxInt(mContext, 20f));
        mItemHeight = (AppUtils.getInstance().getScreenWidth(mContext) - AppUtils.getInstance().dpToPxInt(mContext, 30f))/2;
        mPxInt5 = AppUtils.getInstance().dpToPxInt(mContext, 5f);
        mPxInt10 = AppUtils.getInstance().dpToPxInt(mContext, 10f);
    }


    @Override
    protected void convert(BaseViewHolder helper, InkeRoomItem item) {
        switch (item.getItemType()) {
            case InkeRoomItem.ITEM_TYPE_ROOM:
                setItemRoomData(helper,item);
                break;
            case InkeRoomItem.ITEM_TYPE_BANNER:
                setItemBannerData(helper,item);
                break;
            case InkeRoomItem.ITEM_TYPE_WEB:
                setItemWebBannerData(helper,item);
                break;
        }
    }

    /**
     * 直播间列表
     * @param helper
     * @param item
     */
    private void setItemRoomData(BaseViewHolder helper, InkeRoomItem item){
        RelativeLayout mItemCoverView = (RelativeLayout) helper.getView(R.id.root_cover_view);
        View mRotItemView = helper.getView(R.id.root_item_view);
        mItemCoverView.getLayoutParams().height= mItemHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mRotItemView.setOutlineProvider(new LayoutProvider(AppUtils.getInstance().dpToPxInt(6f)));
        }
        if(null!=item){
            if(null!=item.getData().getLive_info()){
                setItemRoomData(item,helper);
            }else if(null!=item.getData().getChannel()&&null!=item.getData().getChannel().getCards()){
                List<LiveRoomInfo> cards = item.getData().getChannel().getCards();
                if(cards.size()>0){
                    LiveRoomInfo inkeRoomItem = cards.get(0);
                    setItemRoomData(inkeRoomItem,helper);
                }
            }else{
                resetItemData(helper);
            }
        }else{
            resetItemData(helper);
        }
    }

    /**
     * 绑定广告数据
     * @param helper
     * @param item
     */
    private void setItemBannerData(BaseViewHolder helper, InkeRoomItem item){
        if(null!=mBanner){
            mBanner.stopAutoPlay();
        }
        mBanner = (Banner) helper.getView(R.id.item_banner);
        LivePublicRoomAdapter.this.mBanner=this.mBanner;
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.setImageLoader(new GlideImageLoader()).setDelayTime(5000);
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Logger.d(TAG,"BannerViewHolder:position:"+position);
                if(null!=mBanner.getTag() && mBanner.getTag() instanceof List){
                    List<BannerInfo> bannerInfos = (List<BannerInfo>) mBanner.getTag();
                    String jump_url = bannerInfos.get(position).getJump_url();
                    WebViewActivity.start(jump_url);
                }
            }
        });
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mBanner.getLayoutParams();
        layoutParams.width= mItemBannerWidth;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBanner.setOutlineProvider(new LayoutProvider(AppUtils.getInstance().dpToPxInt(6f)));
        }
        try {
            int itemHeight= mItemBannerWidth *Integer.parseInt(item.getHeight())/Integer.parseInt(item.getWidth());
            layoutParams.height=itemHeight;
        }catch (RuntimeException e){
            e.printStackTrace();
            layoutParams.height=AppUtils.getInstance().dpToPxInt(mContext,140f);
        } finally {
            if(helper.getAdapterPosition()==0){
                layoutParams.setMargins(mPxInt5,mPxInt10,mPxInt5,mPxInt5);
            }else{
                layoutParams.setMargins(mPxInt5,mPxInt5,mPxInt5,mPxInt5);
            }
            mBanner.setLayoutParams(layoutParams);
            if(null!=item.getBanners()){
                mBanner.setTag(item.getBanners());
                List<String> strings=new ArrayList<>();
                for (BannerInfo bannerInfo : item.getBanners()) {
                    strings.add(bannerInfo.getIcon());
                }
                mBanner.update(strings);
            }else{
                mBanner.setTag(null);
            }
        }
    }

    /**
     * 绑定广告数据
     * @param helper
     * @param item
     */
    private void setItemWebBannerData(BaseViewHolder helper, InkeRoomItem item){
        if(null!=mBanner){
            mBanner.stopAutoPlay();
        }
        if(null!=mBanner){
            mBanner.stopAutoPlay();
        }
        mBanner = (Banner) helper.getView(R.id.item_banner);
        LivePublicRoomAdapter.this.mWebBanner=this.mBanner;
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.setImageLoader(new GlideImageLoader()).setDelayTime(5000);
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Logger.d(TAG,"WebViewHolder:position:"+position);
                if(null!=mBanner.getTag() && mBanner.getTag() instanceof List){
                    List<InkeWebItem> bannerInfos = (List<InkeWebItem>) mBanner.getTag();
                    String jump_url = bannerInfos.get(position).getLink();
                    WebViewActivity.start(jump_url);
                }
            }
        });
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mBanner.getLayoutParams();
        layoutParams.width= mItemBannerWidth;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBanner.setOutlineProvider(new LayoutProvider(AppUtils.getInstance().dpToPxInt(6f)));
        }
        try {
            int itemHeight= mItemBannerWidth *Integer.parseInt(item.getHeight())/Integer.parseInt(item.getWidth());
            layoutParams.height=itemHeight;
        }catch (RuntimeException e){
            e.printStackTrace();
            layoutParams.height=AppUtils.getInstance().dpToPxInt(mContext,140f);
        } finally {
            if(helper.getAdapterPosition()==0){
                layoutParams.setMargins(mPxInt5,mPxInt10,mPxInt5,mPxInt5);
            }else{
                layoutParams.setMargins(mPxInt5,mPxInt5,mPxInt5,mPxInt5);
            }
            mBanner.setLayoutParams(layoutParams);
            if(null!=item.getData()&&null!=item.getData().getTicker()){
                mBanner.setTag(item.getData().getTicker());
                List<String> strings=new ArrayList<>();
                for (InkeWebItem webItem : item.getData().getTicker()) {
                    strings.add(webItem.getImage());
                }
                mBanner.update(strings);
            }else{
                mBanner.setTag(null);
            }
        }
    }

    /**
     * ITEM还原,防止类型不匹配数据条目复用问题
     * @param helper
     */
    private void resetItemData(BaseViewHolder helper) {
        helper.itemView.setTag(null);
        ImageView itemIcon = (ImageView) helper.getView(R.id.view_item_iv_icon);
        TextView viewTitle = (TextView) helper.getView(R.id.view_item_title_name);
        TextView viewNum = (TextView) helper.getView(R.id.view_item_title_num);
        TextView viewLocation = (TextView) helper.getView(R.id.view_item_location);
        TextView viewState = (TextView) helper.getView(R.id.view_state);
        //在线人数
        viewNum.setText(AppUtils.getInstance().formatWan(0,false));
        itemIcon.setImageResource(0);
        viewTitle.setText("--");
        viewLocation.setText("火星");
        viewState.setText("");
        viewState.setVisibility(View.GONE);
    }

    /**
     * ITEM数据绑定
     * @param itemData
     * @param viewHolder
     */
    private void setItemRoomData(InkeRoomItem itemData, BaseViewHolder viewHolder) {
        viewHolder.itemView.setTag(itemData);
        //在线人数
        ImageView itemIcon = (ImageView) viewHolder.getView(R.id.view_item_iv_icon);
        TextView viewTitle = (TextView) viewHolder.getView(R.id.view_item_title_name);
        TextView viewNum = (TextView) viewHolder.getView(R.id.view_item_title_num);
        TextView viewLocation = (TextView) viewHolder.getView(R.id.view_item_location);
        TextView viewState = (TextView) viewHolder.getView(R.id.view_state);
        viewNum.setText(AppUtils.getInstance().formatWan(itemData.getData().getLive_info().getOnline_users(),false));
        //封面
        if(null!=itemData.getData().getLive_info().getCreator()){
            LiveRoomInfo.CreatorBean creator = itemData.getData().getLive_info().getCreator();
            Glide.with(mContext)
                    .load(creator.getPortrait())
                    .error(R.drawable.ic_video_default_cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(itemIcon);
            //昵称
            viewTitle.setText(creator.getNick());
            if(!TextUtils.isEmpty(creator.getProfession())){
                viewState.setVisibility(View.VISIBLE);
                viewState.setText(creator.getProfession());
            }else{
                viewState.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(creator.getHometown())){
                viewLocation.setText(creator.getHometown());
            }
        }
    }

    /**
     * ITEM数据绑定
     * @param itemData
     * @param viewHolder
     */
    private void setItemRoomData(LiveRoomInfo itemData, BaseViewHolder viewHolder) {
        viewHolder.itemView.setTag(itemData);
        ImageView itemIcon = (ImageView) viewHolder.getView(R.id.view_item_iv_icon);
        TextView viewTitle = (TextView) viewHolder.getView(R.id.view_item_title_name);
        TextView viewNum = (TextView) viewHolder.getView(R.id.view_item_title_num);
        TextView viewLocation = (TextView) viewHolder.getView(R.id.view_item_location);
        TextView viewState = (TextView) viewHolder.getView(R.id.view_state);

        //在线人数
        viewNum.setText(AppUtils.getInstance().formatWan(itemData.getOnline_users(),false));
        //封面
        if(null!=itemData.getCreator()){
            LiveRoomInfo.CreatorBean creator = itemData.getCreator();
            Glide.with(mContext)
                    .load(creator.getPortrait())
                    .error(R.drawable.ic_video_default_cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(itemIcon);
            //昵称
            viewTitle.setText(creator.getNick());
            if(!TextUtils.isEmpty(creator.getProfession())){
                viewState.setVisibility(View.VISIBLE);
                viewState.setText(creator.getProfession());
            }else{
                viewState.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(creator.getHometown())){
                viewLocation.setText(creator.getHometown());
            }
        }
    }

    public void onResume() {
        if(null!=mBanner){
            mBanner.startAutoPlay();
        }
        if(null!=mWebBanner){
            mWebBanner.startAutoPlay();
        }
    }

    public void onPause() {
        if(null!=mBanner){
            mBanner.stopAutoPlay();
        }
        if(null!=mWebBanner){
            mWebBanner.stopAutoPlay();
        }
    }

    /**
     * 动态给定权重 这里的SpanSize是两列 2 表示占据一整行
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(null!=layoutManager&&layoutManager instanceof GridLayoutManager){
            GridLayoutManager gridLayoutManager= (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = getItemViewType(position);
                    switch (itemViewType) {
                        //房间ITEM
                        case InkeRoomItem.ITEM_TYPE_ROOM:
                            return 1;
                        //广告组件
                        case InkeRoomItem.ITEM_TYPE_BANNER:
                        //WEB
                        case InkeRoomItem.ITEM_TYPE_WEB:
                            return 2;
                    }
                    //Anapter 加载中、空布局、头部、尾部 等
                    return 2;
                }
            });
        }
    }
}