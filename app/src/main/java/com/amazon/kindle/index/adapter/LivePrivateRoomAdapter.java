package com.amazon.kindle.index.adapter;

import android.content.Context;
import android.os.Build;
import android.widget.LinearLayout;
import com.amazon.kindle.R;
import com.amazon.kindle.index.ui.activity.WebViewActivity;
import com.amazon.kindle.model.GlideImageLoader;
import com.amazon.kindle.room.bean.BannerInfo;
import com.amazon.kindle.room.bean.InkeRoomItem;
import com.amazon.kindle.room.bean.RoomItem;
import com.amazon.kindle.index.view.IndexRoomItemLayout;
import com.amazon.kindle.util.AppUtils;
import com.amazon.kindle.view.LayoutProvider;
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
 * 2019/7/10
 * 示例在线1v1直播间列表
 */

public class LivePrivateRoomAdapter extends BaseMultiItemQuickAdapter<RoomItem,BaseViewHolder> {

    private final int mItemWidth;
    private Banner mBanner;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public LivePrivateRoomAdapter(Context context,List<RoomItem> data) {
        super(data);
        addItemType(RoomItem.ITEM_TYPE_UNKNOWN,R.layout.item_unknown);
        addItemType(RoomItem.ITEM_TYPE_ROOM,R.layout.item_live_private_room_item);
        addItemType(RoomItem.ITEM_TYPE_BANNER,R.layout.item_live_private_banner_item);
        mItemWidth = (AppUtils.getInstance().getScreenWidth(context) - AppUtils.getInstance().dpToPxInt(context, 20f));
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomItem item) {
        switch (item.getItemType()) {
            case InkeRoomItem.ITEM_TYPE_ROOM:
                setItemRoomData(helper,item);
                break;
            case InkeRoomItem.ITEM_TYPE_BANNER:
                setItemBannerData(helper,item);
                break;
        }
    }

    /**
     * 绑定直播间类型数据
     * @param helper
     * @param item
     */
    private void setItemRoomData(BaseViewHolder helper, RoomItem item) {
        if(null!=item){
            IndexRoomItemLayout itemLayout = (IndexRoomItemLayout) helper.getView(R.id.coord_root_view);
            helper.itemView.setTag(item);
            itemLayout.getLayoutParams().height=mItemWidth;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemLayout.setOutlineProvider(new LayoutProvider(AppUtils.getInstance().dpToPxInt(6f)));
            }
            itemLayout.setData(item);
        }
    }

    /**
     * 绑定广告类型数据
     * @param helper
     * @param item
     */
    private void setItemBannerData(BaseViewHolder helper, RoomItem item) {
        if(null!=item){
            if(null!=mBanner){
                mBanner.stopAutoPlay();
            }
            mBanner = (Banner) helper.getView(R.id.item_banner);
            mBanner.setBannerAnimation(Transformer.Default);
            mBanner.setImageLoader(new GlideImageLoader()).setDelayTime(5000);
            mBanner.setIndicatorGravity(BannerConfig.RIGHT);
            mBanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    if(null!=mBanner.getTag() && mBanner.getTag() instanceof List){
                        List<BannerInfo> bannerInfos = (List<BannerInfo>) mBanner.getTag();
                        String jump_url = bannerInfos.get(position).getJump_url();
                        WebViewActivity.start(jump_url);
                    }
                }
            });
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mBanner.getLayoutParams();
            layoutParams.width=mItemWidth;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBanner.setOutlineProvider(new LayoutProvider(AppUtils.getInstance().dpToPxInt(6f)));
            }
            try {
                int itemHeight=mItemWidth*Integer.parseInt(item.getHeight())/Integer.parseInt(item.getWidth());
                layoutParams.height=itemHeight;
            }catch (RuntimeException e){
                e.printStackTrace();
                layoutParams.height=AppUtils.getInstance().dpToPxInt(mContext,140f);
            } finally {
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

    public void onDestroy() {
        onPause();
    }
}