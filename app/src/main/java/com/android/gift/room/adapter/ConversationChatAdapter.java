package com.android.gift.room.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.gift.R;
import com.android.gift.base.BaseAdapter;
import com.android.gift.constant.Constants;
import com.android.gift.manager.VibratorManager;
import com.android.gift.room.bean.CustomMsgInfo;
import com.android.gift.util.AppUtils;
import com.android.gift.util.SpannableStringUtils;

/**
 * TinyHung@Outlook.com
 * 2018/5/11
 * 直播间聊天、消息列表适配器
 */

public class ConversationChatAdapter extends BaseAdapter<CustomMsgInfo,ConversationChatAdapter.ConversationViewHolder> {

    public ConversationChatAdapter(Context context) {
        super(context);
    }

    @Override
    public ConversationViewHolder inCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ConversationViewHolder(mInflater.inflate(R.layout.item_live_conversation_layout,null));
    }

    @Override
    public void inBindViewHolder(ConversationViewHolder viewHolder, int position) {
        CustomMsgInfo itemData = getItemData(position);
        if(null!=itemData){
            if(!TextUtils.isEmpty(itemData.getMsgContent())){
                viewHolder.itemContent.setBackgroundResource(R.drawable.bg_shape_room_cacht_content);
                viewHolder.itemContent.setTag(itemData.getMsgContent());
                if(Constants.MSG_CUSTOM_NOTICE.equals(itemData.getChildCmd())){
                    viewHolder.itemContent.setText(Html.fromHtml("<font color='#F62E89'>"+itemData.getMsgContent()+"</font>"));
                }else{
                    String contentStr = " <font color='#DADADA'>" + itemData.getSendUserName() + "</font>  <font color='#FFFFFF'>" + itemData.getMsgContent() + "</font>";
                    viewHolder.itemContent.setText(SpannableStringUtils.getInstance().formatUserGradle(contentStr,itemData.getSendUserGradle()));
                }
                viewHolder.itemContent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        VibratorManager.getInstance().vibrate(getContext(),50);
                        AppUtils.getInstance().copyString((String) v.getTag());
                        return false;
                    }
                });
            }
        }
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder{
        private TextView itemContent;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            itemContent = (TextView) itemView.findViewById(R.id.item_tv_content);
        }
    }
}