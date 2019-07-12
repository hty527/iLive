package com.android.gift.room.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.android.gift.R;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.bean.UserInfo;
import com.android.gift.gift.listener.OnGiveGiftListener;
import com.android.gift.gift.manager.GiftBoardManager;
import com.android.gift.gift.dialog.LiveGiftDialog;
import com.android.gift.listener.OnItemClickListener;
import com.android.gift.model.IndexLinLayoutManager;
import com.android.gift.net.OkHttpUtils;
import com.android.gift.room.adapter.LiveRoomAdapter;
import com.android.gift.room.bean.RoomItem;
import com.android.gift.room.contract.RoomContact;
import com.android.gift.room.presenter.RoomPresenter;
import com.android.gift.util.Logger;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/5
 * 主页示例
 */

public class MainActivity extends AppCompatActivity implements RoomContact.View {

    private static final String TAG = "MainActivity";
    private LiveRoomAdapter mAdapter;
    private RoomPresenter mPresenter;
    private SwipeRefreshLayout mRefreshLayout;

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
        setContentView(R.layout.activity_main);
        
        //配置SVGA缓存
        File cacheDir = new File(this.getApplicationContext().getCacheDir(), "http");
        try {
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d(TAG,"INIT CACHE ERROR:"+e.getMessage());
        }
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
        //直播间列表
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new IndexLinLayoutManager(this,IndexLinLayoutManager.VERTICAL,false));
        mAdapter = new LiveRoomAdapter(this);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long itemId) {
                if(null!=view.getTag()){
                    RoomItem roomItem= (RoomItem) view.getTag();
                    Intent intent=new Intent(MainActivity.this,LiveRoomActivity.class);
                    intent.putExtra("roomItem",roomItem);
                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRefreshLayout.setProgressViewOffset(false,0,150);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this,R.color.colorAccent));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(null!=mPresenter&&!mPresenter.isRequsting()){
                    mPresenter.getRooms(getApplicationContext());
                }
            }
        });

        OkHttpUtils.DEBUG=true;
        mPresenter = new RoomPresenter();
        mPresenter.attachView(this);
        mPresenter.getRooms(getApplicationContext());
    }

    /**
     * 显示礼物面板及交互
     * @param view
     */
    public void showGift(View view) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserid("234353455");
        userInfo.setNickName("刘亦菲");
        userInfo.setAvatar("http://c4.haibao.cn/img/600_0_100_0/1473652712.0005/87c7805c10e60e9a6db94f86d6014de8.jpg");
        LiveGiftDialog instance = LiveGiftDialog.getInstance(this);
        instance.setSendeeUser(userInfo);
        instance.setSendeeRoomID("er43te5yttrywrer4t");
        instance.show();
    }

    /**
     * 前往直播间
     * @param view
     */
    public void startRoom(View view) {
        startActivity(new Intent(this,LiveRoomActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GiftBoardManager.getInstance().onDestroy();
    }

    @Override
    public void showLoading() {
        if(null!=mRefreshLayout&&!mRefreshLayout.isShown()){
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void showError(int code, String errorMsg) {}

    @Override
    public void showRooms(List<RoomItem> data) {
        if(null!=mRefreshLayout&&mRefreshLayout.isShown()){
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            });
        }
        if(null!=mAdapter){
            mAdapter.setNewData(data);
        }
    }

    @Override
    public void showRoomsError(int code, String errMsg) {
        if(null!=mRefreshLayout&&mRefreshLayout.isShown()){
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            });
        }
        Toast.makeText(MainActivity.this,errMsg,Toast.LENGTH_SHORT).show();
    }
}