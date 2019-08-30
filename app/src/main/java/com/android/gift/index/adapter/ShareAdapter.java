package com.android.gift.index.adapter;

import android.widget.ImageView;
import com.android.gift.R;
import com.android.gift.bean.ShareMenuItemInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

/**
 * TinyHung@outlook.com
 * 2017-06-22 12:29
 * @des 分享弹窗适配器
 */

public class ShareAdapter extends BaseQuickAdapter<ShareMenuItemInfo,BaseViewHolder> {

    public ShareAdapter(List<ShareMenuItemInfo> homeItemInfos) {
        super(R.layout.item_share_item,homeItemInfos);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShareMenuItemInfo item) {
        if(null!=item){
            helper.setText(R.id.tv_item_title,item.getItemName());
            Glide.with(mContext)
                    .load(item.getItemLogo())
                    .crossFade()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .into((ImageView) helper.getView(R.id.iv_item_icon));
        }
    }
}