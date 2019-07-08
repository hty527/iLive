package com.android.gift.gift.adapter;

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
import com.android.gift.room.bean.CustomMsgInfo;

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
                String content;
                if(Constants.MSG_CUSTOM_NOTICE.equals(itemData.getChildCmd())){
                    content="<font color='#FF7575'>"+itemData.getMsgContent()+"</font>";
                }else{
                    content="<font color='#E0DBDB'>"+itemData.getSendUserName()+"</font>  <font color='#FFF566'>"+itemData.getMsgContent()+"</font>";
                }
                viewHolder.itemContent.setText(Html.fromHtml(content));
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