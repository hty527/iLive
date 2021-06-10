package com.amazon.kindle.index.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.amazon.kindle.R;
import com.amazon.kindle.base.BaseFragment;
import com.amazon.kindle.bean.UserInfo;
import com.amazon.kindle.index.adapter.LivePublicRoomAdapter;
import com.amazon.kindle.net.OkHttpUtils;
import com.amazon.kindle.room.activity.LiveRoomActivity;
import com.amazon.kindle.room.bean.InkeRoomData;
import com.amazon.kindle.room.bean.InkeRoomItem;
import com.amazon.kindle.room.bean.LiveRoomInfo;
import com.amazon.kindle.room.bean.RoomItem;
import com.amazon.kindle.room.contract.RoomContact;
import com.amazon.kindle.room.presenter.RoomPresenter;
import com.amazon.kindle.util.AppUtils;
import com.amazon.kindle.view.DataChangeView;
import com.amazon.kindle.view.LoadingMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
    private DataChangeView mMLoadingView;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_index_public_room;
    }

    @Override
    protected void initViews() {
        //直播间列表
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false));
        mAdapter = new LivePublicRoomAdapter(getContext(),null);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
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
        //加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if(null!=mPresenter&&!mPresenter.isRequsting()){
                    mPresenter.getRooms(mPage,mOffset);
                }
            }
        },recyclerView);
        //加载中、数据为空、加载失败
        mMLoadingView = new DataChangeView(getContext());
        mMLoadingView.setOnRefreshListener(new DataChangeView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(null!=mPresenter){
                    mOffset=0;mPage=0;
                    mPresenter.getRooms(mPage,mOffset);
                }
            }
        });
        mAdapter.setEmptyView(mMLoadingView);
        mAdapter.setLoadMoreView(new LoadingMoreView());
        recyclerView.setAdapter(mAdapter);
        //下拉刷新
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(),R.color.colorAccent));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(null!=mPresenter&&!mPresenter.isRequsting()) {
                    mOffset = 0;
                    mPage = 0;
                    mPresenter.getRooms(mPage, mOffset);
                }else{
                    mRefreshLayout.setRefreshing(false);
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
        if(null!=mMLoadingView){
            mMLoadingView.reset();
        }
        if(null!=mRefreshLayout&&mRefreshLayout.isShown()){
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            });
        }
        if(null!=mAdapter){
            mAdapter.loadMoreComplete();
            if(null!=data.getCards()){
                if(mPage==0&&mOffset==0){
                    //插入一组广告
//                    InkeRoomItem inkeRoomItem=new InkeRoomItem();
//                    inkeRoomItem.setItemCategory("2");
//                    inkeRoomItem.setWidth("1080");
//                    inkeRoomItem.setHeight("404");
//                    List<BannerInfo> bannerInfos= AppUtils.getInstance().createBanners();
//                    inkeRoomItem.setBanners(bannerInfos);
//                    data.getCards().add(0,inkeRoomItem);
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
        if(0==offset){
            if(null!=mMLoadingView&&null!=mAdapter){
                if(mAdapter.getData().size()==0){
                    mMLoadingView.showLoadingView("主播正在赶来~请稍后...");
                }
            }
        }
    }

    @Override
    public void showPrivateRooms(List<RoomItem> data) {}

    @Override
    public void showRoomsError(int code, String errMsg) {
        isRefresh=true;
        if(null!=mMLoadingView){
            mMLoadingView.reset();
        }
        if(null!=mRefreshLayout&&mRefreshLayout.isShown()){
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            });
        }
        if(null!=mAdapter){
            if(OkHttpUtils.ERROR_EMPTY==code){
                mAdapter.loadMoreEnd();
                if(mAdapter.getData().size()==0&&null!=mMLoadingView){
                    mMLoadingView.showEmptyView(errMsg);
                }
            }else{
                mAdapter.loadMoreFail();
                if(mAdapter.getData().size()==0&&null!=mMLoadingView){
                    mMLoadingView.showErrorView("加载失败,点击重试");
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null!=mAdapter){
            mAdapter.onDestroy();
        }
        if(null!=mMLoadingView){
            mMLoadingView.onDestroy();
        }
    }
}