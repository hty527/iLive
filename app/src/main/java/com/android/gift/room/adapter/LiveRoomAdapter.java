package com.android.gift.room.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.android.gift.R;
import com.android.gift.base.BaseAdapter;
import com.android.gift.room.bean.RoomItem;
import com.android.gift.util.AppUtils;
import com.android.gift.view.LayoutProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.math.BigDecimal;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/10
 * 示例直播间列表
 */

public class LiveRoomAdapter extends BaseAdapter<RoomItem,LiveRoomAdapter.ViewHolder> {

    private final int mItemWidth;

    public LiveRoomAdapter(Context context) {
        super(context);
        mItemWidth = (AppUtils.getInstance().getScreenWidth(context) - AppUtils.getInstance().dpToPxInt(context, 12f))/2;
    }

    @Override
    public ViewHolder inCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_live_room_item, null));
    }

    @Override
    public void inBindViewHolder(ViewHolder viewHolder, int position) {
        RoomItem itemData = getItemData(position);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.itemIcon.getLayoutParams();
        int stream_width = Integer.parseInt(itemData.getStream_width());
        int stream_height = Integer.parseInt(itemData.getStream_height());
        float scaleMath = (float) stream_height / (float) stream_width;
        double scale = new BigDecimal(scaleMath).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        double itemHeight = mItemWidth * scale;
        layoutParams.width=mItemWidth;
        layoutParams.height= (int) itemHeight;
        viewHolder.itemIcon.setLayoutParams(layoutParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.itemIcon.setOutlineProvider(new LayoutProvider(AppUtils.getInstance().dpToPxInt(6f)));
        }
        if(null!=itemData){
            viewHolder.itemView.setTag(itemData);
            Glide.with(getContext())
                    .load(itemData.getRoom_front())
                    .error(R.drawable.ic_video_default_cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(viewHolder.itemIcon);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView itemIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            itemIcon = (ImageView) itemView.findViewById(R.id.item_iv_icon);
        }
    }
}
