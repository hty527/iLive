package com.android.gift.gift;

import android.content.Context;
import com.android.gift.bean.GiftItemInfo;
import com.android.gift.bean.GiftType;
import com.android.gift.gift.view.GiftLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by TinyHung@outlook.com
 * 2019/6/20
 */

public class GiftDataCache {

    private volatile static GiftDataCache mInstance;
    //礼物分类
    private List<GiftType> mGiftTypes;
    //分类下的礼物
    private Map<String,ArrayList<GiftItemInfo>> mGiftItemInfos;
    //展示、与用户交互的礼物容器
    private GiftLayout mGiftLayout;

    public static synchronized GiftDataCache getInstance(){
        synchronized (GiftDataCache.class){
            if(null==mInstance){
                mInstance=new GiftDataCache();
            }
        }
        return mInstance;
    }

    public void init(Context context){
        getGiftView(context);
    }

    public List<GiftType> getGiftTypes() {
        return mGiftTypes;
    }

    public void setGiftTypes(List<GiftType> giftTypes) {
        mGiftTypes = giftTypes;
    }

    public List<GiftItemInfo> getGiftItemInfos(String type) {
        if(null!=mGiftItemInfos&&mGiftItemInfos.size()>0){
            return mGiftItemInfos.get(type);
        }
        return null;
    }

    public void setGiftItemInfos(ArrayList<GiftItemInfo> giftItemInfos,String type) {
        if(null==mGiftItemInfos){
            mGiftItemInfos=new TreeMap<>();
        }
        mGiftItemInfos.put(type,giftItemInfos);
    }

    /**
     * 返回单例的礼物交互组件
     * @param context Activity上下文
     * @return
     */
    public GiftLayout getGiftView(Context context) {
        if(null==mGiftLayout){
            mGiftLayout=new GiftLayout(context);
        }
        return mGiftLayout;
    }
}