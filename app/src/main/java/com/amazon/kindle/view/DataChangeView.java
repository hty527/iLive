package com.amazon.kindle.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amazon.kindle.R;

/**
 * TinyHung@Outlook.com
 * 2018/3/18
 * 通用的加载中，数据为空、加载失败、刷新重试 控件
 */

public class DataChangeView extends RelativeLayout implements View.OnClickListener {

    private TextView mTextView;
    private AnimationDrawable mAnimationDrawable;
    private ImageView mLoadingView;

    public DataChangeView(Context context) {
        super(context);
        init(context);
    }

    public DataChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressLint("WrongViewCast")
    private void init(Context context) {
        View.inflate(context, R.layout.view_list_empty, this);
        mTextView = (TextView) findViewById(R.id.view_loading_tv);
        mLoadingView = (ImageView) findViewById(R.id.view_loading_iv);
        mLoadingView.setImageResource(R.drawable.video_loading_anim);
        mLoadingView.setVisibility(INVISIBLE);
        mAnimationDrawable = (AnimationDrawable) mLoadingView.getDrawable();
    }

    /**
     * 加载中状态
     */
    public void showLoadingView(){
        showLoadingView(null);
    }

    /**
     * 加载中状态
     */
    public void showLoadingView(String content){
        DataChangeView.this.setOnClickListener(null);
        DataChangeView.this.setVisibility(View.VISIBLE);
        if(null!=mTextView){
            mTextView.setVisibility(VISIBLE);
            mTextView.setText(TextUtils.isEmpty(content)?"加载中,请稍后...":content);
        }
        if(null!=mLoadingView){
            mLoadingView.setVisibility(VISIBLE);
        }
        if(null!=mAnimationDrawable&&!mAnimationDrawable.isRunning()){
            mAnimationDrawable.start();
        }
    }

    public void showEmptyView(){
        showEmptyView("数据为空",R.drawable.ic_list_empty_icon);
    }

    public void showEmptyView(String content){
        showEmptyView(content,R.drawable.ic_list_empty_icon);
    }


    public void showEmptyView(int content, int srcResID) {
        showEmptyView(getContext().getResources().getString(content), srcResID);
    }

    /**
     * 数据为空状态
     * @param content  要显示的文本
     * @param srcResID icon
     */
    public void showEmptyView(String content, int srcResID) {
        reset();
        DataChangeView.this.setOnClickListener(null);
        if(DataChangeView.this.getVisibility()!=VISIBLE){
            DataChangeView.this.setVisibility(View.VISIBLE);
        }
        if(null!=mTextView){
            mTextView.setVisibility(VISIBLE);
            mTextView.setText(content);
        }
        if (null != mLoadingView) {
            mLoadingView.setImageResource(srcResID);
        }
    }

    /**
     * 重置所有状态
     */
    public void reset(){
        DataChangeView.this.setOnClickListener(null);
        if(null!=mAnimationDrawable&&mAnimationDrawable.isRunning()){
            mAnimationDrawable.stop();
        }
        if(null!=mLoadingView){
            mLoadingView.setVisibility(GONE);
        }
        if(null!=mTextView){
            mTextView.setVisibility(GONE);
            mTextView.setText("");
        }
        DataChangeView.this.setVisibility(View.GONE);
    }

    public void showErrorView() {
        showErrorView("加载失败,点击重试", R.drawable.ic_net_error);
    }

    public void showErrorView(String content) {
        showErrorView(content, R.drawable.ic_net_error);
    }

    public void showErrorView(int content, int srcResID) {
        showErrorView(getContext().getResources().getString(content), srcResID);
    }

    /**
     * 加载失败状态
     * @param content  要显示的文本
     * @param srcResID icon
     */
    public void showErrorView(String content, int srcResID) {
        reset();
        if(DataChangeView.this.getVisibility()!=VISIBLE){
            DataChangeView.this.setVisibility(View.VISIBLE);
        }
        if(null!=mTextView){
            mTextView.setVisibility(VISIBLE);
            mTextView.setText(content);
        }
        if (null != mLoadingView) {
            mLoadingView.setImageResource(srcResID);
        }
        DataChangeView.this.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (null != mOnRefreshListener) {
            mOnRefreshListener.onRefresh();
        }
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    private OnRefreshListener mOnRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    public void onDestroy() {
        reset();
        mAnimationDrawable=null;mOnRefreshListener = null;
    }
}