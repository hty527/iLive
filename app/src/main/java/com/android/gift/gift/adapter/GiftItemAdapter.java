package com.android.gift.gift.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.gift.R;
import com.android.gift.base.BaseAdapter;
import com.android.gift.gift.bean.GiftItemInfo;
import com.android.gift.gift.manager.GiftBoardManager;
import com.android.gift.util.AppUtils;
import com.bumptech.glide.Glide;
import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2019/6/20
 * 礼物选择面板适配器
 */

public class GiftItemAdapter extends BaseAdapter<GiftItemInfo,GiftItemAdapter.ViewHolder> {

    private static final String TAG = "GiftItemAdapter";
    //条目高度、图表宽度、SVGA动画宽度、分类index、当前Adapter在此分类下index
    private final int mItemHeight,mIconWidth,mSvgaIconWidth,mGroupPosition,mAdapterlayoutPosition;

    public GiftItemAdapter(Context context, List<GiftItemInfo> data, int groupPosition,int indexPosition) {
        super(context,data);
        mItemHeight = AppUtils.getInstance().getScreenWidth(context)/4;
        mIconWidth = mItemHeight - AppUtils.getInstance().dpToPxInt(context,40f);
        mSvgaIconWidth = mItemHeight - AppUtils.getInstance().dpToPxInt(context,25f);
        this.mAdapterlayoutPosition=indexPosition;
        this.mGroupPosition=groupPosition;
    }

    @Override
    public ViewHolder inCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_re_gift_item_layout,null));
    }

    @Override
    public void inBindViewHolder(ViewHolder viewHolder, final int position) {
        GiftItemInfo itemData = getItemData(position);
        if(null!=itemData){
            viewHolder.item_view_group.getLayoutParams().height=mItemHeight;
            //ICON
            ViewGroup.LayoutParams layoutParams = viewHolder.ic_item_icon.getLayoutParams();
            layoutParams.height=mIconWidth;
            layoutParams.width=mIconWidth;
            viewHolder.ic_item_icon.setLayoutParams(layoutParams);
            //SVGA容器
            RelativeLayout.LayoutParams layoutParamsSvga = (RelativeLayout.LayoutParams) viewHolder.view_svga_icon.getLayoutParams();
            layoutParamsSvga.height=mSvgaIconWidth;
            layoutParamsSvga.width=mSvgaIconWidth;
            viewHolder.view_svga_icon.setLayoutParams(layoutParamsSvga);

            viewHolder.tv_item_title.setText(itemData.getTitle());
            viewHolder.item_tv_price.setText(String.valueOf(itemData.getPrice()));
            //普通的ICON设置
            Glide
                .with(viewHolder.ic_item_icon.getContext().getApplicationContext())
                .load(itemData.getSrc())
                .error(R.drawable.ic_default_gift_icon)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(viewHolder.ic_item_icon);
            viewHolder.itemView.setTag(itemData);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null!=mOnItemClickListener){
                        mOnItemClickListener.onItemClick(position,v, (GiftItemInfo) v.getTag());
                    }
                }
            });
            //只缓存 第一个分类下的第一页礼物中的第一个礼物
            if(0==mAdapterlayoutPosition&&0==mGroupPosition){
                GiftBoardManager.getInstance().putFirstItemView(viewHolder.itemView,position);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private FrameLayout view_svga_icon;
        private View item_view_group;
        private ImageView ic_item_icon;
        private TextView tv_item_title;
        private TextView item_tv_price;

        public ViewHolder(View itemView) {
            super(itemView);
            view_svga_icon=itemView.findViewById(R.id.gift_svga_view);
            item_view_group=itemView.findViewById(R.id.gift_item_view_group);
            ic_item_icon=itemView.findViewById(R.id.gift_item_ic_icon);
            tv_item_title=itemView.findViewById(R.id.gift_item_tv_title);
            item_tv_price=itemView.findViewById(R.id.gift_item_tv_price);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int poistion, View view, GiftItemInfo giftInfo);
    }
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}