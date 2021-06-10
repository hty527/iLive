package com.amazon.kindle.index.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.amazon.kindle.R;
import com.amazon.kindle.room.bean.ImageInfo;
import com.amazon.kindle.room.bean.RoomItem;
import com.amazon.kindle.util.AppUtils;
import com.amazon.kindle.view.LayoutProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2019/7/12
 * 主页直播间列表
 */

public class IndexRoomItemLayout extends FrameLayout{

    private static final String TAG = "IndexRoomItemLayout";
    private int mPosition;//当前正被选中的项
    private ImageView mIconView;
    private LinearLayout mViewLayout;
    private List<View> mViewImages;
    private List<ImageInfo> mImages;

    public IndexRoomItemLayout(@NonNull Context context) {
        this(context,null);
    }

    public IndexRoomItemLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IndexRoomItemLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.view_index_room_layout,this);
        mViewLayout = (LinearLayout) findViewById(R.id.view_images);
        mViewImages=new ArrayList<>();
        mIconView = (ImageView) findViewById(R.id.view_item_iv_icon);
    }

    /**
     * 给定数据
     */
    public void setData(RoomItem roomItem) {
        TextView textView = (TextView) findViewById(R.id.view_item_title_name);
        //昵称
        if(null!=roomItem&&null!=roomItem.getAnchor()){
            textView.setText(roomItem.getAnchor().getNickName());
        }else{
            textView.setText("");
        }
        if(null!=mViewLayout){
            mViewLayout.removeAllViews();
            mViewImages.clear();
            if(null!=roomItem&&null!=roomItem.getImages()&&roomItem.getImages().size()>0){
                mImages = roomItem.getImages();
                mPosition=0;
                mImages.get(mPosition).setSelected(true);
                setHeadCover(roomItem.getImages().get(mPosition));
                //初始化相册
                int itemWidth = AppUtils.getInstance().dpToPxInt(48f);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(itemWidth,itemWidth);
                layoutParams.setMargins(AppUtils.getInstance().dpToPxInt(getContext(),5f),0,AppUtils.getInstance().dpToPxInt(getContext(),5f),0);
                for (int i = 0; i < mImages.size(); i++) {
                    View inflate = View.inflate(getContext(), R.layout.item_idnex_user_image_item, null);
                    inflate.setLayoutParams(layoutParams);
                    ImageInfo itemData = mImages.get(i);
                    if(null!=itemData){
                        View viewBorder = inflate.findViewById(R.id.view_border);
                        ImageView itemIcon = inflate.findViewById(R.id.item_head_cover);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            itemIcon.setOutlineProvider(new LayoutProvider(AppUtils.getInstance().dpToPxInt(5f)));
                        }
                        viewBorder.setBackgroundResource(itemData.isSelected()?R.drawable.index_head_border:0);
                        inflate.setTag(i);
                        Glide.with(inflate.getContext())
                                .load(itemData.getIcon_mini())
                                .error(R.drawable.ic_video_default_cover)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .dontAnimate()
                                .into(itemIcon);
                        //点击ITEM交互
                        inflate.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(null!=v.getTag()){
                                    int tag = (int) v.getTag();
                                    if(mPosition!= tag){
                                        mViewImages.get(mPosition).findViewById(R.id.view_border).setBackgroundResource(0);
                                        mViewImages.get(tag).findViewById(R.id.view_border).setBackgroundResource(R.drawable.index_head_border);
                                        if(null!=mImages&&mImages.size()>0){
                                            setHeadCover(mImages.get(tag));
                                        }
                                    }
                                    mPosition= tag;
                                }
                            }
                        });
                    }
                    mViewImages.add(inflate);
                    mViewLayout.addView(inflate,layoutParams);
                }
            }
        }
    }

    /**
     * 设置主封面
     * @param imageInfo
     */
    private void setHeadCover(ImageInfo imageInfo) {
        if(null!=mIconView)
            if(null!=imageInfo){
                Glide.with(getContext())
                        .load(imageInfo.getIcon())
                        .error(R.drawable.ic_video_default_cover)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .dontAnimate()
                        .centerCrop()
                        .into(mIconView);
            }else{
                mIconView.setImageResource(0);
            }
    }
}