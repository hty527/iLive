package com.android.gift.room.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.gift.R;
import com.android.gift.base.BaseAdapter;
import com.android.gift.room.bean.ImageInfo;
import com.android.gift.util.AppUtils;
import com.android.gift.view.LayoutProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2019/7/12
 * 主页直播间列表主播快照,快照用小图
 */

public class IndexRoomImagesAdapter extends BaseAdapter<ImageInfo,IndexRoomImagesAdapter.ViewHolder> {

    public IndexRoomImagesAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder inCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_idnex_user_image_item, null));
    }

    @Override
    public void inBindViewHolder(ViewHolder viewHolder, int position) {
        ImageInfo itemData = getItemData(position);
        if(null!=itemData){
            viewHolder.viewBorder.setBackgroundResource(itemData.isSelected()?R.drawable.index_head_border:0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewHolder.itemIcon.setOutlineProvider(new LayoutProvider(AppUtils.getInstance().dpToPxInt(6f)));
            }
            viewHolder.itemView.setTag(itemData);
            Glide.with(mContext)
                    .load(itemData.getIcon_mini())
                    .error(R.drawable.ic_video_default_cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(viewHolder.itemIcon);
        }
    }

    @Override
    protected void inBindViewHolder(ViewHolder viewHolder, int position, List<Object> payloads) {
        ImageInfo itemData = getItemData(position);
        if(null!=itemData){
            viewHolder.viewBorder.setBackgroundResource(itemData.isSelected()?R.drawable.index_head_border:0);
            viewHolder.itemView.setTag(itemData);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView itemIcon;
        private View viewBorder;

        public ViewHolder(View itemView) {
            super(itemView);
            itemIcon = (ImageView) itemView.findViewById(R.id.item_head_cover);
            viewBorder = itemView.findViewById(R.id.view_border);
        }
    }
}