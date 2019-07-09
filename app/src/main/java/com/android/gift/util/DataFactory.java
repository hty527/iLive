package com.android.gift.util;

import com.android.gift.bean.UserInfo;
import com.android.gift.manager.UserManager;
import com.android.gift.room.bean.CustomMsgInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/6
 */

public class DataFactory {

    public static List<UserInfo> createLiveFans() {
        List<UserInfo> list=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            UserInfo userInfo=new UserInfo();
            if(i==0){
                userInfo.setUserSex(1);
                userInfo.setAvatar("http://c2.haibao.cn/img/600_0_100_0/1498033350.5864/17460e94710f5cba57fa6f6009119347.jpg");
            }else if(i==1){
                userInfo.setAvatar("http://c4.haibao.cn/img/600_0_100_0/1498033344.5114/3a42be4238435c975033ce4fc603d5a1.jpg");
            }else if(i==2){
                userInfo.setUserSex(1);
                userInfo.setAvatar("http://c4.haibao.cn/img/600_0_100_0/1473652712.0005/87c7805c10e60e9a6db94f86d6014de8.jpg");
            }else if(i==3){
                userInfo.setUserSex(1);
                userInfo.setAvatar("http://c2.haibao.cn/img/600_0_100_0/1493267408.0007/7726778833401511649f891023c0ea4b.jpg");
            }else{
                userInfo.setAvatar("http://c2.haibao.cn/img/600_0_100_0/1498033350.5864/17460e94710f5cba57fa6f6009119347.jpg");
            }
            list.add(userInfo);
        }
        return list;
    }

    public static List<CustomMsgInfo> createLiveConversation() {
        List<CustomMsgInfo> list=new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            CustomMsgInfo customMsgInfo=new CustomMsgInfo();
            customMsgInfo.setSendUserName(UserManager.getInstance().getNickName());
            customMsgInfo.setSendUserHead(UserManager.getInstance().getAcatar());
            customMsgInfo.setSendUserID(UserManager.getInstance().getUserId());
            customMsgInfo.setMsgContent("我是聊天内容，大家好！");
            list.add(customMsgInfo);
        }
        return list;
    }
}