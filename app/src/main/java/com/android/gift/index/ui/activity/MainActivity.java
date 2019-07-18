package com.android.gift.index.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;
import com.android.gift.R;
import com.android.gift.adapter.AppFragmentPagerAdapter;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.bean.UserInfo;
import com.android.gift.gift.listener.OnGiveGiftListener;
import com.android.gift.gift.manager.GiftBoardManager;
import com.android.gift.gift.dialog.LiveGiftDialog;
import com.android.gift.index.ui.fragment.IndexPrivateRoomFragment;
import com.android.gift.index.ui.fragment.IndexPublicRoomFragment;
import com.android.gift.net.OkHttpUtils;
import com.android.gift.room.bean.BoxPixInfo;
import com.android.gift.room.view.RoundGlobeView;
import com.android.gift.util.AppUtils;
import com.android.gift.util.Logger;
import com.android.gift.util.StatusUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/5
 * 主页示例
 */

public class MainActivity extends AppCompatActivity implements RoundGlobeView.OnGlobeMoveListener {

    private static final String TAG = "MainActivity";
    public static final String ITEM_GIFT  = "GIFT";
    public static final String ITEM_ABOUT = "ABOUT";
    public static final String ITEM_HOME  = "HOME";
    private RoundGlobeView mGlobeView;
    private Map<String,BoxPixInfo> mPixInfoTreeMap = new TreeMap<>();
    private TextView mTextTips;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main1);
        StatusUtils.setStatusTextColor1(true,this);//白色背景，黑色字体
        int statusBarHeight = AppUtils.getInstance().getStatusBarHeight(this);
        findViewById(R.id.view_status).getLayoutParams().height= statusBarHeight;
        //CollapsingToolbarLayout距离屏幕顶端最小停靠距离
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout.setMinimumHeight(statusBarHeight);
        appBarLayout.setMinimumHeight(statusBarHeight);
        //配置SVGA缓存
        File cacheDir = new File(this.getApplicationContext().getCacheDir(), "http");
        try {
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //礼物模块初始化及赠送监听
        GiftBoardManager.getInstance()
                //初始化礼物交互面板
                .init(this)
                //设置礼物赠送监听
                .addOnGiveGiftListener(new OnGiveGiftListener() {
                    @Override
                    public void onReceiveGift(GiftItemInfo giftItemInfo, UserInfo userInfo, String roomid, int count) {
                        Logger.d(TAG,"onReceiveGift-->接收人："+userInfo.getUserid()+",群组ID："+roomid+",礼物ID："+giftItemInfo.getId()+",数量："+count);
                    }

                    @Override
                    public void onReceiveNewGift(GiftItemInfo giftItemInfo) {

                    }

                    @Override
                    public void onReceiveGiftCount(int count) {

                    }
                });
        //圆形菜单
        mGlobeView = (RoundGlobeView) findViewById(R.id.btn_move_view);
        mGlobeView.setOnGlobeMoveListener(this);
        mTextTips = (TextView) findViewById(R.id.tv_tips);
        //先测量预摆放的View,获取中心点并摆放
        final View globeView = findViewById(R.id.globe_view);
        globeView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                globeView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int[] viewLocation = new int[2];
                globeView.getLocationInWindow(viewLocation);
                int viewX = viewLocation[0]; // x 坐标
                int viewY = viewLocation[1]; // y 坐标
                int centerX = globeView.getMeasuredWidth() / 2;
                int centerY = globeView.getMeasuredHeight() / 2;
                int centerMoveViewX = mGlobeView.getMeasuredWidth() / 2;
                int centerMoveViewY = mGlobeView.getMeasuredHeight() / 2;
                int x = centerX - centerMoveViewX + globeView.getLeft();
                int y = centerY - centerMoveViewY + viewY;
                mGlobeView.setVisibility(View.VISIBLE);
                mGlobeView.setDefault(x, y);
            }
        });
        putLocation(findViewById(R.id.btn_left),ITEM_ABOUT);
        putLocation(findViewById(R.id.btn_top),ITEM_GIFT);
        putLocation(findViewById(R.id.btn_right),ITEM_HOME);

        OkHttpUtils.DEBUG=false;
        //界面初始化
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        //选中监听，字体大小改变
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(null!=tab&&null!=tab.getCustomView()){
                    TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tv_item_title);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                    textView.getPaint().setFakeBoldText(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(null!=tab&&null!=tab.getCustomView()){
                    TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tv_item_title);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                    textView.getPaint().setFakeBoldText(false);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new IndexPublicRoomFragment());
        fragments.add(new IndexPrivateRoomFragment());
        String[] titls=new String[]{"直播","私聊"};
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        AppFragmentPagerAdapter adapter = new AppFragmentPagerAdapter(getSupportFragmentManager(), fragments,titls);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        //使用自定义TAB VIEW
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View tabView = getTabView(i,titls,0);
                if(null!=tabView) tab.setCustomView(tabView);
            }
        }
    }

    /**
     * 充气自定义View
     * @param index 位置
     * @param mTitles titles
     * @param showIndex 默认显示的INDEX
     * @return
     */
    private View getTabView(int index, String[] mTitles, int showIndex) {
        if(null!=mTitles&&mTitles.length>index){
            View inflate = View.inflate(MainActivity.this, R.layout.view_tab_item, null);
            TextView tvItemTitle = (TextView) inflate.findViewById(R.id.tv_item_title);
            tvItemTitle.setText(mTitles[index]);
            if(showIndex==index){
                //默认选中的项高亮
                tvItemTitle.setSelected(true);
                tvItemTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                tvItemTitle.getPaint().setFakeBoldText(true);
            }else{
                tvItemTitle.setSelected(false);
                tvItemTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                tvItemTitle.getPaint().setFakeBoldText(false);
            }
            return inflate;
        }
        return null;
    }

    /**
     * 获取和存储View所处的相对于屏幕的坐标
     * @param view 要测量和获取位置的View
     * @param tag 身份标识
     */
    private void putLocation(final View view, final String tag) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int[] viewLocation = new int[2];
                view.getLocationInWindow(viewLocation);
                int viewLeft = viewLocation[0]; // x 坐标
                int viewTop = viewLocation[1]; // y 坐标
                int viewRight = view.getMeasuredWidth() + viewLeft;
                int viewBottom = view.getMeasuredHeight() + viewTop;
                mPixInfoTreeMap.put(tag, new BoxPixInfo(viewLeft, viewTop, viewRight, viewBottom));
            }
        });
    }

    /**
     * 圆形菜单持续拖动，检测是否在某个按钮区域
     * @param touchRowX 手指的RAWX
     * @param touchRowY 手指的RAWY
     * @return 不为空，则表示碰撞到了该TAG所属的View
     */
    @Override
    public String continueMove(int touchRowX, int touchRowY) {
        Iterator<Map.Entry<String, BoxPixInfo>> iterator = mPixInfoTreeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, BoxPixInfo> next = iterator.next();
            String key = next.getKey();
            BoxPixInfo boxPixInfo = next.getValue();
            if (touchRowX > boxPixInfo.getLeft() && touchRowY > boxPixInfo.getTop()
                    && touchRowX < boxPixInfo.getRight() && touchRowY < boxPixInfo.getBottom()) {
                return key;
            }
        }
        return null;
    }


    @Override
    public void boxEnterView(String tag) {
        if(null!=mTextTips){
            mTextTips.setText(getTipsContext(tag));
            showTipsView(mTextTips);
        }
    }

    /**
     * 圆球离开某个TAG区域范围
     * @param tag View所绑定的TAG
     * @param isGone 是否隐藏提示框
     */
    @Override
    public void boxOutView(String tag, boolean isGone) {
        if(null!=mTextTips){
            if(isGone){
                goneTipsView(mTextTips);
                return;
            }
            mTextTips.setText("拖动至箭头区域内选择功能");
        }
    }

    /**
     * 松手后被碰撞的VIEW
     * @param tag View对应的TAG
     */
    @Override
    public void selectedBox(String tag) {
        if(!TextUtils.isEmpty(tag)){
            //显示礼物面板
            if(TextUtils.equals(tag,ITEM_GIFT)){
                UserInfo userInfo = new UserInfo();
                userInfo.setUserid("234353455");
                userInfo.setNickName("刘亦菲");
                userInfo.setAvatar("http://c4.haibao.cn/img/600_0_100_0/1473652712.0005/87c7805c10e60e9a6db94f86d6014de8.jpg");
                LiveGiftDialog.getInstance(this)
                        //接收人
                        .setReceiveUserInfo(userInfo)
                        //房间ID
                        .setReceiveRoomID("er43te5yttrywrer4t")
                        //未选中情况下是否自动选中第0个礼物
                        .setAutoSelectedEnable(true)
                        .show();
                return;
            }
            //Github主页
            if(TextUtils.equals(tag,ITEM_HOME)){
                try {
                    Uri uri=Uri.parse("https://github.com/Yuye584312311/iLive");
                    Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }catch (RuntimeException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"未找到合适的应用打开网页",Toast.LENGTH_SHORT).show();
                }
                return;
            }
            //关于、版本信息
            if(TextUtils.equals(tag,ITEM_ABOUT)){
                startActivity(new Intent(MainActivity.this,AboutActivity.class));
                return;
            }
        }
        Toast.makeText(MainActivity.this,tag,Toast.LENGTH_SHORT).show();
    }

    /**
     * 返回功能对应的文案
     * @param tag
     * @return
     */
    private String getTipsContext(String tag) {
        if(TextUtils.isEmpty(tag)){
            return "未知事件";
        }
        if(tag.equals(ITEM_ABOUT)){
            return "松手查看版本信息";
        }else if(tag.equals(ITEM_GIFT)){
            return "松手打开礼物面板";
        }else if(tag.equals(ITEM_HOME)){
            return "松手前往Github项目主页";
        }
        return "未知事件";
    }

    /**
     * Tips View显示
     * @param view
     */
    private void showTipsView(View view) {
        if (view == null) {
            return;
        }
        if(view.getVisibility()==View.VISIBLE){
            return;
        }
        view.clearAnimation();
        view.setVisibility(View.VISIBLE);
        PropertyValuesHolder aFloatX = PropertyValuesHolder.ofFloat("scaleX", 0.0f, 1.0f);
        PropertyValuesHolder aFloatY = PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, aFloatX, aFloatY);
        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
    }

    /**
     * Tips View隐藏
     * @param view
     */
    private void goneTipsView(final View view) {
        if (view == null) {
            return;
        }
        if(view.getVisibility()==View.GONE){
            return;
        }
        view.clearAnimation();
        view.setVisibility(View.VISIBLE);
        PropertyValuesHolder aFloatX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.0f);
        PropertyValuesHolder aFloatY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, aFloatX, aFloatY);
        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
        objectAnimator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GiftBoardManager.getInstance().onDestroy();
    }
}