package com.amazon.kindle.room.model;

import com.amazon.kindle.util.Logger;
import com.amazon.kindle.base.BaseEngin;
import com.amazon.kindle.net.OkHttpUtils;
import com.amazon.kindle.net.OnResultCallBack;

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
     * @param page 页眉
     * @param offset 数据集起始偏移位置
     * @param callBack 回调监听器
     */
    public void getGiftRooms(int page,int offset,final OnResultCallBack callBack){
        Logger.d(TAG,"getGiftRooms-->page:"+page+",offset:"+offset);
        if(null==callBack){
            return;
        }
        //OkHttpUtils.get("https://raw.githubusercontent.com/Yuye584312311/ConfigFile/master/index/rooms.json",callBack);
        Map<String, String> params;
        if(0==page){
            params=getCardRecommendParams(0);
        }else{
            params=getRecommendParams(offset);
        }
        OkHttpUtils.get("https://service.inke.cn/api/live/card_recommend",params,callBack);
    }

    /**
     * 获取地区推荐主播
     * @param offset
     * @return
     */
    private Map<String,String> getCardRecommendParams(int offset){
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
        params.put("refurbish_mode","2");
        return params;
    }

    /**
     * 获取地区推荐主播
     * @param offset
     * @return
     */
    private Map<String,String> getRecommendParams(int offset){
        Map<String,String> params=new HashMap<>();
        params.put("offset",String.valueOf(offset));
        params.put("cc","GF10000");
        params.put("source_info","eyJhcHBpZCI6IjEwMDAwIiwidWlkIjoiMCIsInBhZ2UiOiJjb20ubWVlbGl2ZS5pbmdrZWUuYnVz%0AaW5lc3MuY29tbWVyY2lhbC5sYXVuY2hlci51aS5JbmdrZWVMYXVuY2hlciIsInRpbWUiOiIxNTYz%0ANDU5NzU0MjQ4In0%3D%0A");
        params.put("dev_name","HUAWEI");
        params.put("lc","3f38750d6ffff2eb");
        params.put("cpu","%5BMuMu_GL_%28NVIDIA_GeForce_GTX_1060_6GB_Direct3D11_vs_5_0_ps_5_0%29%5D%5BARMv7_639_placeholder%5D");
        params.put("devi","867368032090000");
        params.put("sid","");
        params.put("osversion","android_23");
        params.put("ndid","20190718221601493e8b15d2b475cbf9d90b9fb171c2150078cec23974ded0");
        params.put("imei","867368032090000");
        params.put("conn","wifi");
        params.put("xid","247");
        params.put("ram","3715420160");
        params.put("icc","14164229802735722274");
        params.put("ast","1");
        params.put("imsi","460061842628172");
        params.put("mtid","d2bc35182f3a917267afa6d19b99afe4");
        params.put("mtxid","0180c2000003");
        params.put("cv","IK7.1.20_Android");
        params.put("code632606108","1563459755615");
        params.put("proto","8");
        params.put("ua","HUAWEIBLA-AL00");
        params.put("logid","10002%2C10208%2C30205%2C40202%2C50102%2C50208%2C50307%2C50404%2C60202%2C70008%2C80003%2C80108%2C80201%2C80302%2C90004%2C100009%2C100103%2C100206%2C200002");
        params.put("uid","0");
        params.put("vv","1.0.3-201610121413.android");
        params.put("aid","5b0223c47a32551b");
        params.put("smid","DuamVqMG5xcuuYAnMcSVH8Kw7WRf9sVErfBG8Vmhh7yq%2F2kg08c3Idf%2B%2Frtw505JFR5mLMInwcODBqHVv%2FFv2xNg");
        params.put("card_pos","0");
        params.put("longitude","");
        params.put("latitude","");
        params.put("city_tab_key","00220180702YH5JW%2C%E5%8C%97%E4%BA%AC");
        params.put("live_uid","0");
        params.put("refurbish_mode","1");
        params.put("filter_card","0");
        params.put("crv","2.0");
        params.put("gender","3");
        params.put("tab_name","%E5%8C%97%E4%BA%AC");
        params.put("user_level","0");
        params.put("tab_key","00220180702YH5JW");
        params.put("interest","0");
        params.put("session_id","1563459755613");
        params.put("channel_id","2");
        params.put("stay_time","0 HTTP/1.1");
        return params;
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
        OkHttpUtils.get("https://gitee.com/hty_Yuye/OpenFile/raw/master/index/rooms.json",callBack);
    }
}