package com.android.gift.room.model;

import com.android.gift.base.BaseEngin;
import com.android.gift.net.OkHttpUtils;
import com.android.gift.net.OnResultCallBack;
import com.android.gift.util.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * hty_Yuye@Outlook.com
 * 2019/7/10
 * 直播间
 */

public class RoomEngin extends BaseEngin {

    /**
     * 获取在线直播间列表
     * 映客API数据提供
     * @param offset 数据集起始偏移位置
     * @param callBack 回调监听器
     */
    public void getGiftRooms(int offset,final OnResultCallBack callBack){
        if(null==callBack){
            return;
        }
        //OkHttpUtils.get("https://raw.githubusercontent.com/Yuye584312311/ConfigFile/master/index/rooms.json",callBack);
        Map<String,String> params=new HashMap<>();
        params.put("cc","TG36001");
        params.put("source_info","eyJhcHBpZCI6IjEwMDAwIiwidWlkIjoiNzI3OTk5MzEwIiwicGFnZSI6ImNvbS5tZWVsaXZlLmlu\n" +
                "Z2tlZS5idXNpbmVzcy5sb2dpbi51aS5kaWFsb2cuTG9naW5EaWFsb2dBY3Rpdml0eSIsInRpbWUi\n" +
                "OiIxNTYzMzI3NDc0NzE3In0=\n");
        params.put("lc","37625e94208cc895");
        params.put("cpu","mumu");
        params.put("devi","867368032090000");
        params.put("sid","20km4ihmj0v3Rl2wGBCKdnu7wSFuhi2FnuPnGYdKG6bYANTZESa3ajwi3");
        params.put("osversion","android_23");
        params.put("imei","867368032090000");
        params.put("conn","wifi");
        params.put("ram","3715420160");
        params.put("icc","16875101462049313317");
        params.put("ast","1");
        params.put("imsi","460051248872052");
        params.put("mtid","d2bc35182f3a917267afa6d19b99afe4");
        params.put("mtxid","0180c2000003");
        params.put("cv","IK7.1.05_Android");
        params.put("ua","HUAWEIBLA-AL00");
        params.put("logid","264,293,197,198,229,236,10002,10202,20102,30209,40201,50110,50205,50309,50403,60201,70009,80003,80102,80205,80306,90006,100004,100107,100203,200002");
        params.put("uid","727999310");
        params.put("aid","9e2b123ad6b07d24");
        params.put("smid","DuamVqMG5xcuuYAnMcSVH8Kw7WRf9sVErfBG8Vmhh7yq/2kg08c3Idf+/rtw505JFR5mLMInwcODBqHVv/Fv2xNg");
        params.put("session_id","1563328028611");
        params.put("offset",String.valueOf(offset));
        params.put("refurbish_mode",String.valueOf(offset>0?0:2));
        OkHttpUtils.post("https://service.inke.cn/api/live/card_recommend",params,callBack);
    }

    /**
     * 获取在线直播间列表
     * 映客API数据提供
     * @param callBack 回调监听器
     */
    public void getPrivateGiftRooms(final OnResultCallBack callBack){
        if(null==callBack){
            return;
        }
        OkHttpUtils.get("https://raw.githubusercontent.com/Yuye584312311/ConfigFile/master/index/rooms.json",callBack);
    }
}