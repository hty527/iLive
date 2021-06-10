package com.amazon.kindle.index.ui.fragment;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amazon.kindle.index.adapter.LivePrivateRoomAdapter;
import com.amazon.kindle.model.IndexLinLayoutManager;
import com.amazon.kindle.R;
import com.amazon.kindle.base.BaseFragment;
import com.amazon.kindle.net.OkHttpUtils;
import com.amazon.kindle.room.activity.LiveRoomActivity;
import com.amazon.kindle.room.bean.InkeRoomData;
import com.amazon.kindle.room.bean.RoomItem;
import com.amazon.kindle.room.contract.RoomContact;
import com.amazon.kindle.room.presenter.RoomPresenter;
import com.amazon.kindle.view.DataChangeView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/17
 * 在线1v1直播间
 */

public class IndexPrivateRoomFragment extends BaseFragment<RoomPresenter> implements RoomContact.View {

    private static final String TAG = "IndexPrivateRoomFragment";
    private LivePrivateRoomAdapter mAdapter;
    private DataChangeView mMLoadingView;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean isRefresh=false;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_index_private_room;
    }

    @Override
    protected void initViews() {
        //直播间列表
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new IndexLinLayoutManager(getContext(),IndexLinLayoutManager.VERTICAL,false));
        mAdapter = new LivePrivateRoomAdapter(getContext(),null);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(null!=view.getTag()){
                    RoomItem roomItem= (RoomItem) view.getTag();
                    Intent intent=new Intent(getActivity(),LiveRoomActivity.class);
                    intent.putExtra("roomItem",roomItem);
                    startActivity(intent);
                }
            }
        });

        //加载中、数据为空、加载失败
        mMLoadingView = new DataChangeView(getActivity());
        mMLoadingView.setOnRefreshListener(new DataChangeView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(null!=mPresenter){
                    mPresenter.getPrivateRooms();
                }
            }
        });
        mAdapter.setEmptyView(mMLoadingView);
        //加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mAdapter.loadMoreEnd();
            }
        },recyclerView);
        recyclerView.setAdapter(mAdapter);
        //下拉刷新
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(),R.color.colorAccent));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(null!=mPresenter&&!mPresenter.isRequsting()){
                    mPresenter.getPrivateRooms();
                }else{
                    mRefreshLayout.setRefreshing(false);
                }
            }
        });
        //获取在线直播间列表
        mPresenter.attachView(this);
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if(!isRefresh&&null!=mPresenter&&!mPresenter.isRequsting()&&null!=mAdapter&&mAdapter.getData().size()<=0){
            mPresenter.getPrivateRooms();
        }
    }

    @Override
    protected RoomPresenter createPresenter() {
        return new RoomPresenter();
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
    public void showRooms(InkeRoomData data) {}

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
    public void showPrivateRooms(List<RoomItem> data) {
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
            mAdapter.setNewData(data);
        }
    }

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
        if(OkHttpUtils.ERROR_EMPTY==code){
            if(mAdapter.getData().size()==0&&null!=mMLoadingView){
                mMLoadingView.showEmptyView(errMsg);
            }
        }else{
            if(mAdapter.getData().size()==0&&null!=mMLoadingView){
                mMLoadingView.showErrorView("加载失败,点击重试");
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