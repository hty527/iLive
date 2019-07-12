package com.android.gift.room.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.gift.R;
import com.android.gift.base.BaseAdapter;
import com.android.gift.model.GlideImageLoader;
import com.android.gift.room.bean.BannerInfo;
import com.android.gift.room.bean.RoomItem;
import com.android.gift.room.view.IndexRoomItemLayout;
import com.android.gift.util.AppUtils;
import com.android.gift.view.LayoutProvider;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/10
 * 示例直播间列表
 */

public class LiveRoomAdapter extends BaseAdapter<RoomItem,RecyclerView.ViewHolder> {

    private final int mItemWidth;

    public LiveRoomAdapter(Context context) {
        super(context);
        mItemWidth = (AppUtils.getInstance().getScreenWidth(context) - AppUtils.getInstance().dpToPxInt(context, 32f));
    }

    @Override
    public int getItemViewType(int position) {
        return null==mData?0:mData.get(position).getItemType();
    }

    @Override
    public RecyclerView.ViewHolder inCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType==RoomItem.ITEM_TYPE_ROOM){
            return new RoomViewHolder(mInflater.inflate(R.layout.item_live_room_item, null));
        }else if(viewType==RoomItem.ITEM_TYPE_BANNER){
            return new BannerViewHolder(mInflater.inflate(R.layout.item_live_banner_item, null));
        }
        return new UnknownViewHolder(mInflater.inflate(R.layout.item_unknown, null));
    }

    @Override
    public void inBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RoomItem itemData = getItemData(position);
        if(null!=itemData){
            int itemViewType = getItemViewType(position);
            //直播间列表
            if(itemViewType==RoomItem.ITEM_TYPE_ROOM){
                RoomViewHolder roomHolder= (RoomViewHolder) viewHolder;
                viewHolder.itemView.setTag(itemData);
                roomHolder.mIndexRoomItemLayout.getLayoutParams().height=mItemWidth;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    roomHolder.mIndexRoomItemLayout.setOutlineProvider(new LayoutProvider(AppUtils.getInstance().dpToPxInt(8f)));
                }
                roomHolder.mIndexRoomItemLayout.setData(itemData);
            //Banner广告
            }else if(itemViewType==RoomItem.ITEM_TYPE_BANNER){
                BannerViewHolder bannerHolder= (BannerViewHolder) viewHolder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    bannerHolder.mBanner.setOutlineProvider(new LayoutProvider(AppUtils.getInstance().dpToPxInt(8f)));
                }
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) bannerHolder.mBanner.getLayoutParams();
                layoutParams.width=mItemWidth;
                try {
                    int itemHeight=mItemWidth*Integer.parseInt(itemData.getHeight())/Integer.parseInt(itemData.getWidth());
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

    public class RoomViewHolder extends RecyclerView.ViewHolder{
        private IndexRoomItemLayout mIndexRoomItemLayout;

        public RoomViewHolder(View itemView) {
            super(itemView);
            mIndexRoomItemLayout = (IndexRoomItemLayout) itemView.findViewById(R.id.coord_root_view);
        }
    }

    private Banner mBanner;
    public class BannerViewHolder extends RecyclerView.ViewHolder{

        private Banner mBanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            mBanner = (Banner) itemView.findViewById(R.id.item_banner);
            LiveRoomAdapter.this.mBanner=this.mBanner;
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
}