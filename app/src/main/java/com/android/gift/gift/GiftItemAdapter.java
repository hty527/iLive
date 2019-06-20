package com.android.gift.gift;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.gift.R;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.util.AppUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2019/6/20
 * 礼物选择面板适配器
 */

public class GiftItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "GiftItemAdapter";
    private final LayoutInflater mInflater;
    private final Context mContext;
    private List<GiftItemInfo> mData;
    private final int mItemHeight;
    private final int mIconWidth;

    public GiftItemAdapter(List<GiftItemInfo> data, Context context) {
        this.mData=data;
        int screenWidth = AppUtils.getScreenWidth(context);
        this.mContext=context;
        mInflater = LayoutInflater.from(context);
        mItemHeight = screenWidth/4;
        mIconWidth = mItemHeight - AppUtils.dpToPxInt(context,40f);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_re_gift_item_layout,null));
    }

    /**
     * 单条目全部刷新
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        GiftItemInfo giftInfo = mData.get(position);
        if(null!=giftInfo){
            ViewHolder viewHolder= (ViewHolder) holder;
            viewHolder.item_view_group.getLayoutParams().height=mItemHeight;
            //ICON
            ViewGroup.LayoutParams layoutParams = viewHolder.ic_item_icon.getLayoutParams();
            layoutParams.height=mIconWidth;
            layoutParams.width=mIconWidth;
            viewHolder.ic_item_icon.setLayoutParams(layoutParams);
            //SVGA容器
            RelativeLayout.LayoutParams layoutParamsSvga = (RelativeLayout.LayoutParams) viewHolder.view_svga_icon.getLayoutParams();
            layoutParamsSvga.height=mIconWidth;
            layoutParamsSvga.width=mIconWidth;
            viewHolder.view_svga_icon.setLayoutParams(layoutParamsSvga);

            viewHolder.tv_item_title.setText(giftInfo.getTitle());
            viewHolder.item_tv_price.setText(String.valueOf(giftInfo.getPrice()));
            //普通的ICON设置
            viewHolder.ic_item_icon.setImageURI(Uri.parse(giftInfo.getSrc()));
            viewHolder.itemView.setTag(giftInfo);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null!=mOnItemClickListener){
                        mOnItemClickListener.onItemClick(position,v, (GiftItemInfo) v.getTag());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null==mData?0:mData.size();
    }

    public List<GiftItemInfo> getData() {
        return mData;
    }

    /**
     * 为适配器设置新的数据
     * @param data
     */
    public synchronized void setNewData(List<GiftItemInfo> data) {
        mData=data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private FrameLayout view_svga_icon;
        private View item_view_group;
        private SimpleDraweeView ic_item_icon;
        private TextView tv_item_title;
        private TextView item_tv_price;
        private TextView item_tag;

        public ViewHolder(View itemView) {
            super(itemView);
            view_svga_icon=itemView.findViewById(R.id.view_svga_icon);
            item_view_group=itemView.findViewById(R.id.item_view_group);
            ic_item_icon=itemView.findViewById(R.id.ic_item_icon);
            tv_item_title=itemView.findViewById(R.id.tv_item_title);
            item_tv_price=itemView.findViewById(R.id.item_tv_price);
            item_tag=itemView.findViewById(R.id.item_tag);
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
