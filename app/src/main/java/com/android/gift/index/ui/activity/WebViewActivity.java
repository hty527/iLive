package com.android.gift.index.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.android.gift.APPLication;
import com.android.gift.R;
import com.android.gift.view.CommentTitleView;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.WebChromeClient;

/**
 * hty_Yuye@Outlook.com
 * 2019/7/18
 * WEB VIEW
 */

public class WebViewActivity extends AppCompatActivity {

    public final static String TAG = "WebViewActivity";
    private AgentWeb mAgentWeb;
    private String mUrl = "";
    private FrameLayout mWebView;
    private CommentTitleView mTitleView;

    public static void start(String url) {
        Intent intent=new Intent(APPLication.getInstance().getApplicationContext(),WebViewActivity.class);
        intent.putExtra("url",url);
        APPLication.getInstance().getApplicationContext().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getIntent().getStringExtra("url");
        if(TextUtils.isEmpty(mUrl)){
            Toast.makeText(this,"URL不能为空",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        setContentView(R.layout.activity_webview);
        mTitleView = (CommentTitleView) findViewById(R.id.title_view);
        mTitleView.setOnTitleClickListener(new CommentTitleView.OnTitleClickListener() {
            @Override
            public void onBack(View v) {
                finish();
            }
        });
        mTitleView.setTitle(getIntent().getStringExtra("title"));
        mWebView = (FrameLayout) findViewById(R.id.webview);
        mAgentWeb = AgentWeb.with(WebViewActivity.this)
                .setAgentWebParent(mWebView, new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(ContextCompat.getColor(WebViewActivity.this,R.color.colorAccent))
                .setMainFrameErrorView(R.layout.view_web_error_page, -1)//加载失败占位视图
                .setWebView(new WebView(WebViewActivity.this))//自定义WebView
                .setWebViewClient(new NewsWebViewClient())//内部状态
                .setWebChromeClient(mWebChromeClient)//浏览器交互
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DERECT)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .createAgentWeb()
                .ready()
                .go(mUrl);
    }

    private com.just.agentweb.WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (null!=mTitleView) {
                mTitleView.setTitle(title);
            }
        }
    };

    private class NewsWebViewClient extends com.just.agentweb.WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    @Override
    protected void onPause() {
        if(null!=mAgentWeb){
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();

    }

    @Override
    protected void onResume() {
        if(null!=mAgentWeb){
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if(null!=mAgentWeb&&mAgentWeb.back()){
            return;
        }
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null!=mAgentWeb){
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
    }
}