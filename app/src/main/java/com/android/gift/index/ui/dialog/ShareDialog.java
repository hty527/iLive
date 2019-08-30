package com.android.gift.index.ui.dialog;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.android.gift.R;
import com.android.gift.bean.ShareMenuItemInfo;
import com.android.gift.index.adapter.ShareAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2019/8/22
 * 分享面板
 */

public class ShareDialog extends BottomSheetDialog {

    private static final String TAG = "ShareDialog";

    public static ShareDialog getInstance(Context activity) {
        return new ShareDialog(activity);
    }

    public ShareDialog(Context context) {
        super(context, R.style.ButtomDialogTransparentAnimationStyle);
        setContentView(R.layout.dialog_share);
        initLayoutPrams();
    }

    protected void initLayoutPrams(){
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        WindowManager.LayoutParams attributes = window.getAttributes();//得到布局管理者
        WindowManager systemService = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);//得到窗口管理者
        DisplayMetrics displayMetrics=new DisplayMetrics();//创建设备屏幕的管理者
        systemService.getDefaultDisplay().getMetrics(displayMetrics);//得到屏幕的宽高
        attributes.height= FrameLayout.LayoutParams.WRAP_CONTENT;
        attributes.width= systemService.getDefaultDisplay().getWidth();
        attributes.gravity= Gravity.BOTTOM;
    }

    /**
     * 设置ITEM
     * @param items
     */
    public ShareDialog setItems(List<ShareMenuItemInfo> items){
        if(null==items){
            items=new ArrayList<>();
            items.add(new ShareMenuItemInfo("QQ",R.drawable.iv_share_qq, 1));
            items.add(new ShareMenuItemInfo("QQ空间",R.drawable.iv_share_qzone, 2));
            items.add(new ShareMenuItemInfo("微信好友",R.drawable.iv_share_weichat, 3));
            items.add(new ShareMenuItemInfo("朋友圈",R.drawable.iv_share_weichatfriend, 4));
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4, GridLayoutManager.VERTICAL,false));
        final ShareAdapter shareAdapter = new ShareAdapter(items);
        recyclerView.setAdapter(shareAdapter);
        shareAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(null!=mOnShareItemClickListener){
                    ShareDialog.this.dismiss();
                    List<ShareMenuItemInfo> data = shareAdapter.getData();
                    ShareMenuItemInfo shareMenuItemInfo = data.get(position);
                    mOnShareItemClickListener.onItemClick(shareMenuItemInfo);
                }
            }
        });
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog.this.dismiss();
            }
        });
        return ShareDialog.this;
    }

    public interface OnShareItemClickListener{
       void onItemClick(ShareMenuItemInfo shareMenuItemInfo);
    }

    private OnShareItemClickListener mOnShareItemClickListener;

    public ShareDialog setOnItemClickListener(OnShareItemClickListener onShareItemClickListener) {
       mOnShareItemClickListener = onShareItemClickListener;
       return this;
    }
}