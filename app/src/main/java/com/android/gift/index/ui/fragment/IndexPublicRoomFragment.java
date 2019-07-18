package com.android.gift.index.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.android.gift.R;
import com.android.gift.base.BaseFragment;
import com.android.gift.bean.UserInfo;
import com.android.gift.index.adapter.LivePublicRoomAdapter;
import com.android.gift.listener.OnItemClickListener;
import com.android.gift.listener.OnLoadMoreListener;
import com.android.gift.net.OkHttpUtils;
import com.android.gift.room.activity.LiveRoomActivity;
import com.android.gift.room.bean.BannerInfo;
import com.android.gift.room.bean.InkeRoomData;
import com.android.gift.room.bean.InkeRoomItem;
import com.android.gift.room.bean.LiveRoomInfo;
import com.android.gift.room.bean.RoomItem;
import com.android.gift.room.contract.RoomContact;
import com.android.gift.room.presenter.RoomPresenter;
import com.android.gift.util.AppUtils;
import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/17
 * 在线直播间,取子映客API
 */

public class IndexPublicRoomFragment extends BaseFragment<RoomPresenter> implements RoomContact.View {

    private static final String TAG = "IndexPublicRoomFragment";
    private LivePublicRoomAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    //是否更新数据成功了？
    private boolean isRefresh;
    //数据偏移位置，页眉
    private int mOffset=0,mPage=0;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_index_public_room;
    }

    @Override
    protected void initViews() {
        //直播间列表
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false));
        mAdapter = new LivePublicRoomAdapter(getContext());
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long itemId) {
                if(null!=view.getTag()){
                    Object tag = view.getTag();
                    if(tag instanceof InkeRoomItem){
                        InkeRoomItem item= (InkeRoomItem) tag;
                        RoomItem roomItem=new RoomItem();
                        LiveRoomInfo.CreatorBean creator = item.getData().getLive_info().getCreator();
                        roomItem.setRoomid(String.valueOf(item.getData().getLive_info().getRoom_id()));
                        roomItem.setRoom_front(creator.getPortrait());
                        roomItem.setStream_url(AppUtils.getInstance().formatRoomStream(item.getData().getLive_info().getStream_addr()));
                        roomItem.setOnlineNumber(item.getData().getLive_info().getOnline_users());
                        UserInfo userInfo=new UserInfo();
                        userInfo.setAvatar(creator.getPortrait());
                        userInfo.setNickName(creator.getNick());
                        userInfo.setUserid(String.valueOf(creator.getId()));
                        roomItem.setAnchor(userInfo);
                        startRoomActivity(roomItem);
                    }else if(tag instanceof LiveRoomInfo){
                        LiveRoomInfo item= (LiveRoomInfo) tag;
                        RoomItem roomItem=new RoomItem();
                        LiveRoomInfo.CreatorBean creator = item.getCreator();
                        roomItem.setRoomid(String.valueOf(item.getRoom_id()));
                        roomItem.setRoom_front(creator.getPortrait());
                        roomItem.setStream_url(AppUtils.getInstance().formatRoomStream(item.getStream_addr()));
                        roomItem.setOnlineNumber(item.getOnline_users());
                        UserInfo userInfo=new UserInfo();
                        userInfo.setAvatar(creator.getPortrait());
                        userInfo.setNickName(creator.getNick());
                        userInfo.setUserid(String.valueOf(creator.getId()));
                        roomItem.setAnchor(userInfo);
                        startRoomActivity(roomItem);
                    }
                }
            }
        });
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(null!=mPresenter&&!mPresenter.isRequsting()){
                    Toast.makeText(getContext(),"加载更多中",Toast.LENGTH_SHORT).show();
                    mPresenter.getRooms(mPage,mOffset);
                }
            }
        },recyclerView);
        recyclerView.setAdapter(mAdapter);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(),R.color.colorAccent));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(null!=mPresenter&&!mPresenter.isRequsting()){
                    mOffset=0;mPage=0;
                    mPresenter.getRooms(mPage,mOffset);
                }
            }
        });
        //获取在线直播间列表
        mPresenter.attachView(this);
    }

    /**
     * 前往直播间
     * @param roomItem
     */
    private void startRoomActivity(RoomItem roomItem) {
        Intent intent=new Intent(getActivity(),LiveRoomActivity.class);
        intent.putExtra("roomItem",roomItem);
        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(null!=mPresenter&&!mPresenter.isRequsting()){
            mOffset=0;mPage=0;
            mPresenter.getRooms(mPage,mOffset);
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if(!isRefresh&&null!=mPresenter&&!mPresenter.isRequsting()&&null!=mAdapter&&mAdapter.getData().size()<=0){
            mOffset=0;mPage=0;
            mPresenter.getRooms(mPage,mOffset);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(null!=mAdapter){
            mAdapter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(null!=mAdapter){
            mAdapter.onPause();
        }
    }

    @Override
    protected RoomPresenter createPresenter() {
        return new RoomPresenter();
    }

    @Override
    public void showRooms(InkeRoomData data) {
        isRefresh=true;
        if(null!=mRefreshLayout&&mRefreshLayout.isShown()){
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            });
        }
        if(null!=mAdapter){
            mAdapter.onLoadComplete();
            if(null!=data.getCards()){
                if(mPage==0&&mOffset==0){
                    //插入一组广告
                    InkeRoomItem inkeRoomItem=new InkeRoomItem();
                    inkeRoomItem.setItemCategory("2");
                    inkeRoomItem.setWidth("1080");
                    inkeRoomItem.setHeight("404");
                    List<BannerInfo> bannerInfos= AppUtils.getInstance().createBanners();
                    inkeRoomItem.setBanners(bannerInfos);
                    data.getCards().add(0,inkeRoomItem);
                    mAdapter.setNewData(data.getCards());
                }else{
                    mAdapter.addData(data.getCards());
                }
            }
        }
        //只有非头部数据才记录偏移量
        if(mPage>0){
            if(mOffset>0){
                mOffset=mOffset*2;
            }else{
                mOffset=data.getOffset();
            }
        }
        mPage++;
    }

    @Override
    public void showLoading(int offset) {
        if(0==offset&&null!=mRefreshLayout&&!mRefreshLayout.isRefreshing()){
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void showPrivateRooms(List<RoomItem> data) {}

    @Override
    public void showRoomsError(int code, String errMsg) {
        isRefresh=true;
        if(null!=mRefreshLayout&&mRefreshLayout.isShown()){
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            });
        }
        Toast.makeText(getContext(),errMsg,Toast.LENGTH_SHORT).show();
        if(null!=mAdapter){
            if(OkHttpUtils.ERROR_EMPTY==code){
                mAdapter.onLoadEnd();
            }else{
                mAdapter.onLoadError();
            }
        }
    }
}