package com.android.gift.room.model;

import android.content.Context;
import android.text.TextUtils;
import com.android.gift.base.BaseEngin;
import com.android.gift.net.OkHttpUtils;
import com.android.gift.net.OnResultCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * hty_Yuye@Outlook.com
 * 2019/7/10
 * 直播间
 */

public class RoomEngin extends BaseEngin {

    /**
     * 获取礼物类型,这里只是示例，取自本地文件，自行替换为API获取
     * @param callBack 回调监听器
     * 根据gift_type传参
     * API: http://b.clyfb.dandanq.cn/api/gift/gift_type?debug=1&qwe=1q2w3e4r&app_version=3000&userid=26563974
     */
    public void getGiftRooms(final Context context,final OnResultCallBack callBack){
        if(null==callBack){
            return;
        }
        Subscription subscribe = Observable
                .just("")
                .map(new Func1<String, Object>() {
                    @Override
                    public Object call(String type) {
                        return getGiftFromType(context,"room.json",callBack.getType());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object object) {
                        if (null != callBack) {
                            if (null != object) {
                                callBack.onResponse(object);
                            } else {
                                callBack.onError(OkHttpUtils.ERROR_EMPTY, "暂无主播");
                            }
                        }
                    }
                });
        addSubscrebe(subscribe);
    }

    /**
     * 读取本地assets目录下gifts
     * @param context 全局上下文
     * @param filePath assets 目录文件绝对路径
     * @param dataType 数据类型
     * @return 礼物列表
     */
    private Object getGiftFromType(Context context, String filePath, Type dataType) {
        if(null!=context){
            String json = getFromAssets(context, filePath);
            Object gameInfos = null;
            if (!TextUtils.isEmpty(json)) {
                try {
                    if (null != dataType) {
                        gameInfos = new Gson().fromJson(json, dataType);
                    } else {
                        gameInfos = new Gson().fromJson(json, new TypeToken<String>() {}.getType());
                    }
                    return gameInfos;
                }catch (RuntimeException e){
                    e.printStackTrace();
                    return gameInfos;
                }
            }
        }
        return null;
    }
}