package com.android.gift.index.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.gift.R;
import com.android.gift.base.BaseAdapter;
import com.android.gift.model.GlideImageLoader;
import com.android.gift.room.bean.BannerInfo;
import com.android.gift.room.bean.InkeRoomItem;
import com.android.gift.room.bean.LiveRoomInfo;
import com.android.gift.util.AppUtils;
import com.android.gift.view.LayoutProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/17
 * 示例在线直播间列表
 */

public class LivePublicRoomAdapter extends BaseAdapter<InkeRoomItem,RecyclerView.ViewHolder> {

    private final int mItemBannerWidth;
    private final int mItemHeight;

    public LivePublicRoomAdapter(Context context) {
        super(context);
        mItemBannerWidth = (AppUtils.getInstance().getScreenWidth(context) - AppUtils.getInstance().dpToPxInt(context, 20f));
        mItemHeight = (AppUtils.getInstance().getScreenWidth(context) - AppUtils.getInstance().dpToPxInt(context, 30f))/2;
    }

    @Override
    public int getItemViewType(int position) {
        return null==mData?0:mData.get(position).getItemType();
    }

    @Override
    public RecyclerView.ViewHolder inCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType==InkeRoomItem.ITEM_TYPE_ROOM){
            return new RoomViewHolder(mInflater.inflate(R.layout.item_live_public_room_item, null));
        }else if(viewType==InkeRoomItem.ITEM_TYPE_BANNER){
            return new BannerViewHolder(mInflater.inflate(R.layout.item_live_public_banner_item, null));
        }
        return new UnknownViewHolder(mInflater.inflate(R.layout.item_unknown, null));
    }

    @Override
    public void inBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        InkeRoomItem itemData = getItemData(position);
        if(null!=itemData){
            int itemViewType = getItemViewType(position);
            //直播间列表
            if(itemViewType==InkeRoomItem.ITEM_TYPE_ROOM){
                RoomViewHolder roomHolder= (RoomViewHolder) viewHolder;
                roomHolder.mItemCoverView.getLayoutParams().height= mItemHeight;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    roomHolder.mRotItemView.setOutlineProvider(new LayoutProvider(AppUtils.getInstance().dpToPxInt(6f)));
                }
                if(null!=itemData.getData()){
                    if(null!=itemData.getData().getLive_info()){
                        setItemRoomData(itemData,roomHolder);
                    }else if(null!=itemData.getData().getChannel()&&null!=itemData.getData().getChannel().getCards()){
                        List<LiveRoomInfo> cards = itemData.getData().getChannel().getCards();
                        if(cards.size()>0){
                            LiveRoomInfo inkeRoomItem = cards.get(0);
                            setItemRoomData(inkeRoomItem,roomHolder);
                        }
                    }else{
                        resetItemData(roomHolder);
                    }
                }else{
                    resetItemData(roomHolder);
                }
                //Banner广告
            }else if(itemViewType==InkeRoomItem.ITEM_TYPE_BANNER){
                BannerViewHolder bannerHolder= (BannerViewHolder) viewHolder;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) bannerHolder.mBanner.getLayoutParams();
                layoutParams.width= mItemBannerWidth;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    bannerHolder.mBanner.setOutlineProvider(new LayoutProvider(AppUtils.getInstance().dpToPxInt(6f)));
                }
                try {
                    int itemHeight= mItemBannerWidth *Integer.parseInt(itemData.getHeight())/Integer.parseInt(itemData.getWidth());
                    layoutParams.height=itemHeight;
                }catch (RuntimeException e){
                    e.printStackTrace();
                    layoutParams.height=AppUtils.getInstance().dpToPxInt(mContext,140f);
                } finally {
                    bannerHolder.mBanner.setLayoutParams(layoutParams);
                    if(null!=itemData.getBanners()){
                        List<String> strings=new ArrayList<>();
                        for (BannerInfo bannerInfo : itemData.getBanners()) {
                            strings.add(bannerInfo.getIcon());
                        }
                        bannerHolder.mBanner.update(strings);
                    }
                }
            }
        }
    }

    /**
     * ITEM还原,防止类型不匹配数据条目复用问题
     * @param viewHolder
     */
    private void resetItemData(RoomViewHolder viewHolder) {
        viewHolder.itemView.setTag(null);
        //在线人数
        viewHolder.viewNum.setText(AppUtils.getInstance().formatWan(0,false));
        viewHolder.itemIcon.setImageResource(0);
        viewHolder.viewTitle.setText("--");
        viewHolder.viewLocation.setText("火星");
        viewHolder.viewState.setText("");
        viewHolder.viewState.setVisibility(View.GONE);
    }

    /**
     * ITEM数据绑定
     * @param itemData
     * @param viewHolder
     */
    private void setItemRoomData(InkeRoomItem itemData, RoomViewHolder viewHolder) {
        viewHolder.itemView.setTag(itemData);
        //在线人数
        viewHolder.viewNum.setText(AppUtils.getInstance().formatWan(itemData.getData().getLive_info().getOnline_users(),false));
        //封面
        if(null!=itemData.getData().getLive_info().getCreator()){
            LiveRoomInfo.CreatorBean creator = itemData.getData().getLive_info().getCreator();
            Glide.with(getContext())
                    .load(creator.getPortrait())
                    .error(R.drawable.ic_video_default_cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(viewHolder.itemIcon);
            //昵称
            viewHolder.viewTitle.setText(creator.getNick());
            if(!TextUtils.isEmpty(creator.getProfession())){
                viewHolder.viewState.setVisibility(View.VISIBLE);
                viewHolder.viewState.setText(creator.getProfession());
            }else{
                viewHolder.viewState.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(creator.getHometown())){
                viewHolder.viewLocation.setText(creator.getHometown());
            }
        }
    }

    /**
     * ITEM数据绑定
     * @param itemData
     * @param viewHolder
     */
    private void setItemRoomData(LiveRoomInfo itemData, RoomViewHolder viewHolder) {
        viewHolder.itemView.setTag(itemData);
        //在线人数
        viewHolder.viewNum.setText(AppUtils.getInstance().formatWan(itemData.getOnline_users(),false));
        //封面
        if(null!=itemData.getCreator()){
            LiveRoomInfo.CreatorBean creator = itemData.getCreator();
            Glide.with(getContext())
                    .load(creator.getPortrait())
                    .error(R.drawable.ic_video_default_cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(viewHolder.itemIcon);
            //昵称
            viewHolder.viewTitle.setText(creator.getNick());
            if(!TextUtils.isEmpty(creator.getProfession())){
                viewHolder.viewState.setVisibility(View.VISIBLE);
                viewHolder.viewState.setText(creator.getProfession());
            }else{
                viewHolder.viewState.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(creator.getHometown())){
                viewHolder.viewLocation.setText(creator.getHometown());
            }
        }
    }

    public void onResume() {
        if(null!=mBanner){
            mBanner.startAutoPlay();
        }
    }

    public void onPause() {
        if(null!=mBanner){
            mBanner.stopAutoPlay();
        }
    }

    /**
     * 直播间Holder
     */
    public class RoomViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout mItemCoverView;
        private TextView viewTitle,viewNum,viewLocation,viewState;
        private ImageView itemIcon;
        private View mRotItemView;

        public RoomViewHolder(View itemView) {
            super(itemView);
            mItemCoverView = (RelativeLayout) itemView.findViewById(R.id.root_cover_view);
            mRotItemView = itemView.findViewById(R.id.root_item_view);
            itemIcon = (ImageView) itemView.findViewById(R.id.view_item_iv_icon);
            viewTitle = (TextView) itemView.findViewById(R.id.view_item_title_name);
            viewNum = (TextView) itemView.findViewById(R.id.view_item_title_num);
            viewLocation = (TextView) itemView.findViewById(R.id.view_item_location);
            viewState = (TextView) itemView.findViewById(R.id.view_state);
        }
    }

    /**
     * 广告Holder
     */
    private Banner mBanner;
    public class BannerViewHolder extends RecyclerView.ViewHolder{

        private Banner mBanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            mBanner = (Banner) itemView.findViewById(R.id.item_banner);
            LivePublicRoomAdapter.this.mBanner=this.mBanner;
            mBanner.setBannerAnimation(Transformer.Default);
            mBanner.setImageLoader(new GlideImageLoader()).setDelayTime(5000);
            mBanner.setIndicatorGravity(BannerConfig.RIGHT);
            mBanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {

                }
            });
        }
    }

    public class UnknownViewHolder extends RecyclerView.ViewHolder{

        public UnknownViewHolder(View itemView) {
            super(itemView);
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
                            return 2;
                    }
                    //Anapter 加载中、空布局、头部、尾部 等
                    return 2;
                }
            });
        }
    }
}