package com.android.gift.index.ui.fragment;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.android.gift.R;
import com.android.gift.base.BaseFragment;
import com.android.gift.listener.OnItemClickListener;
import com.android.gift.model.IndexLinLayoutManager;
import com.android.gift.room.activity.LiveRoomActivity;
import com.android.gift.index.adapter.LivePrivateRoomAdapter;
import com.android.gift.room.bean.InkeRoomData;
import com.android.gift.room.bean.RoomItem;
import com.android.gift.room.contract.RoomContact;
import com.android.gift.room.presenter.RoomPresenter;
import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/17
 * 在线1v1直播间
 */

public class IndexPrivateRoomFragment extends BaseFragment<RoomPresenter> implements RoomContact.View {

    private static final String TAG = "IndexPrivateRoomFragment";
    private LivePrivateRoomAdapter mAdapter;
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
        mAdapter = new LivePrivateRoomAdapter(getContext());
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long itemId) {
                if(null!=view.getTag()){
                    RoomItem roomItem= (RoomItem) view.getTag();
                    Intent intent=new Intent(getActivity(),LiveRoomActivity.class);
                    intent.putExtra("roomItem",roomItem);
                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(),R.color.colorAccent));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(null!=mPresenter&&!mPresenter.isRequsting()){
                    mPresenter.getPrivateRooms();
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
    public void showPrivateRooms(List<RoomItem> data) {
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
            mAdapter.setNewData(data);
        }
    }

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
    }
}