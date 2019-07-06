package com.android.gift.gift.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.gift.R;
import com.android.gift.base.BaseAdapter;
import com.android.gift.bean.UserInfo;
import com.android.gift.model.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by TinyHung@outlook.com
 * 2018/5/11
 * 在线观众列表
 */

public class LiveFansListAdapter extends BaseAdapter<UserInfo,LiveFansListAdapter.FansViewHolder> {

    public LiveFansListAdapter(Context context) {
        super(context);
    }

    @Override
    public FansViewHolder inCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new FansViewHolder(mInflater.inflate(R.layout.item_live_user_fans_item, null));
    }

    @Override
    public void inBindViewHolder(FansViewHolder viewHolder, int position) {
        UserInfo itemData = getItemData(position);
        if(null!=itemData){
            Glide.with(getContext())
                    .load(itemData.getAvatar())
                    .error(R.drawable.ic_default_user_head)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .transform(new GlideCircleTransform(mContext))
                    .dontAnimate()
                    .into(viewHolder.itemIcon);
        }
    }

    public class FansViewHolder extends RecyclerView.ViewHolder{
        private ImageView itemIcon;

        public FansViewHolder(View itemView) {
            super(itemView);
            itemIcon = (ImageView) itemView.findViewById(R.id.item_iv_user_icon);
        }
    }
}
