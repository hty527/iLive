package com.amazon.kindle.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.amazon.kindle.R;
import com.amazon.kindle.util.AppUtils;
import com.amazon.kindle.util.StatusUtils;

/**
 * TinyHung@Outlook.com
 * 2018/5/29
 * 通用的标题栏
 */

public class CommentTitleView extends LinearLayout implements View.OnClickListener {

    public static final int STYLE_LIGHT=0;//白底
    public static final int STYLE_COLOR=1;//彩底
    private TextView mTitleView,mSubTitle;
    private long[] clickCount = new long[2];
    private int mTitleStyle=STYLE_LIGHT;
    private Drawable mBackGround;

    public CommentTitleView(Context context) {
        super(context);
        init(context,null);
    }

    public CommentTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    @SuppressLint("WrongViewCast")
    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.comment_title_layout,this);
        ImageView btnback = (ImageView) findViewById(R.id.view_btn_back);
        ImageView btnMenu = (ImageView) findViewById(R.id.view_btn_menu);
        mTitleView = (TextView) findViewById(R.id.view_title);
        mSubTitle = (TextView) findViewById(R.id.view_sub_title);
        if(null!=attrs){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommentTitleView);
            Drawable backDrawable = typedArray.getDrawable(R.styleable.CommentTitleView_commentBackRes);
            Drawable menuDrawable = typedArray.getDrawable(R.styleable.CommentTitleView_commentMenuRes);
            mBackGround = typedArray.getDrawable(R.styleable.CommentTitleView_commentBackGroundRes);
            if(null!=mBackGround){
                findViewById(R.id.root_top_bar).setBackground(mBackGround);
            }
            boolean showBack = typedArray.getBoolean(R.styleable.CommentTitleView_commentShowBack,true);
            btnback.setVisibility(showBack?VISIBLE:GONE);
            if(null!=backDrawable){
                btnback.setImageDrawable(backDrawable);
            }
            if(null!=menuDrawable){
                btnMenu.setImageDrawable(menuDrawable);
            }
            //标题
            String titleText = typedArray.getString(R.styleable.CommentTitleView_commentTitle);
            int titleColor = typedArray.getColor(R.styleable.CommentTitleView_commentTitleColor,
                    Color.parseColor("#FFFFFF"));
            float titleSize = typedArray.getDimensionPixelSize(R.styleable.CommentTitleView_commentTitleSize, 18);
            //副标题
            String subTitleText = typedArray.getString(R.styleable.CommentTitleView_commentSubTitle);
            int subTitleColor = typedArray.getColor(R.styleable.CommentTitleView_commentSubTitleColor,
                    Color.parseColor("#EFEFEF"));
            float subTitleSize = typedArray.getDimensionPixelSize(R.styleable.CommentTitleView_commentSubTitleSize, 14);
            //标题
            mTitleView.setText(titleText);
            mTitleView.setTextColor(titleColor);
            mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,titleSize);
            if(!TextUtils.isEmpty(subTitleText)){
                btnMenu.setVisibility(GONE);
                //副标题
                mSubTitle.setText(subTitleText);
                mSubTitle.setTextColor(subTitleColor);
                mSubTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP,subTitleSize);
            }
            mTitleStyle = typedArray.getInt(R.styleable.CommentTitleView_commentTitleStyle, STYLE_LIGHT);
            typedArray.recycle();
        }
        btnback.setOnClickListener(this);
        mTitleView.setOnClickListener(this);
        mSubTitle.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        findViewById(R.id.statusbar_view).getLayoutParams().height= AppUtils.getInstance().getStatusBarHeight(getContext());
        //用户未指定自定义样式，使用默认的
        if(mTitleStyle==STYLE_LIGHT){
            mTitleView.setTextColor(Color.parseColor("#333333"));
            btnback.setColorFilter(Color.parseColor("#666666"));
            btnMenu.setColorFilter(Color.parseColor("#666666"));
            StatusUtils.setStatusTextColor1(true,(Activity) getContext());//白色背景，黑色字体
            return;
        }
        setTitleBarStyle(mTitleStyle);
    }

    /**
     * 设置标题栏样式
     * @param titleStyle 0：白色黑字 1：彩底白字
     */
    public void setTitleBarStyle(int titleStyle) {
        ImageView btnback = (ImageView) findViewById(R.id.view_btn_back);
        ImageView btnMenu = (ImageView) findViewById(R.id.view_btn_menu);
        TextView titleView = (TextView) findViewById(R.id.view_title);
        TextView viewSubTitle = (TextView) findViewById(R.id.view_sub_title);
        if(titleStyle==STYLE_LIGHT){//白底黑字
            if(null!=btnback) btnback.setImageResource(R.drawable.btn_nav_menu_back_selector_black);
            if(null!=btnMenu) btnMenu.setColorFilter(Color.parseColor("#666666"));
            if(null!=titleView) titleView.setTextColor(Color.parseColor("#333333"));
            if(null!=viewSubTitle) viewSubTitle.setTextColor(Color.parseColor("#666666"));
            StatusUtils.setStatusTextColor1(true,(Activity) getContext());//白色背景，黑色字体
        }else if(titleStyle==STYLE_COLOR){//彩底白字
            if(null!=btnback) btnback.setImageResource(R.drawable.btn_nav_menu_back_selector_white);
            if(null!=btnMenu) btnMenu.setColorFilter(Color.parseColor("#DDDDDD"));
            if(null!=titleView) titleView.setTextColor(Color.parseColor("#FFFFFF"));
            if(null!=viewSubTitle) viewSubTitle.setTextColor(Color.parseColor("#DDDDDD"));
            StatusUtils.setStatusTextColor1(false,(Activity) getContext());//透明背景，白色字体
        }
        this.mTitleStyle=titleStyle;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.view_btn_back){
            if(null!=mOnTitleClickListener) mOnTitleClickListener.onBack(v);
        }else if(id==R.id.view_sub_title){
            if(null!=mOnTitleClickListener) mOnTitleClickListener.onSubTitleClick(v);
        }else if(id==R.id.view_btn_menu){
            if(null!=mOnTitleClickListener) mOnTitleClickListener.onMenuClick(v);
        }else if(id==R.id.view_title){
            if(null!=clickCount&&null!=mOnTitleClickListener){
                System.arraycopy(clickCount,1,clickCount,0,clickCount.length - 1);
                clickCount[clickCount.length - 1] = SystemClock.uptimeMillis();
                if (clickCount[0] >= (clickCount[clickCount.length - 1] - 1000)) {
                    if(null!=mOnTitleClickListener) mOnTitleClickListener.onTitleClick(v,true);
                    return;
                }
                if(null!=mOnTitleClickListener) mOnTitleClickListener.onTitleClick(v,false);
            }
        }
    }

    public void setTitle(String title){
        if(null!=mTitleView){
            mTitleView.setText(title);
        }
    }

    public void setSubTitle(String subTitle) {
        if(null!=mSubTitle){
            mSubTitle.setVisibility(VISIBLE);
            findViewById(R.id.view_btn_menu).setVisibility(GONE);
            mSubTitle.setText(subTitle);
        }
    }

    public abstract static class OnTitleClickListener{
        public void onBack(View view){}
        public void onTitleClick(View view,boolean doubleClick){}
        public void onSubTitleClick(View v){}
        public void onMenuClick(View v){}
    }

    private OnTitleClickListener mOnTitleClickListener;

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        mOnTitleClickListener = onTitleClickListener;
    }
}