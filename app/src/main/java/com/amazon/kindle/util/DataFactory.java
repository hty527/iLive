package com.amazon.kindle.util;

import com.amazon.kindle.manager.UserManager;
import com.amazon.kindle.bean.UserInfo;
import com.amazon.kindle.room.bean.CustomMsgInfo;
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
                userInfo.setAvatar("https://img2.woyaogexing.com/2021/06/10/cdec93915b98418caea1cf5fd2ba86dd!400x400.png");
            }else if(i==1){
                userInfo.setAvatar("https://img2.woyaogexing.com/2021/06/10/7a876b3228d940769625af085d8a6610!400x400.png");
            }else if(i==2){
                userInfo.setUserSex(1);
                userInfo.setAvatar("https://img2.woyaogexing.com/2021/06/10/e70d4fa4e3be4a9fa3ad4a474dc973bc!400x400.jpeg");
            }else if(i==3){
                userInfo.setUserSex(1);
                userInfo.setAvatar("https://img2.woyaogexing.com/2021/06/10/966a3844c1a441c58c6ce7432e53a592!400x400.png");
            }else{
                userInfo.setAvatar("https://img2.woyaogexing.com/2021/06/10/ebcd37043b2b410d87427d58d0a84b7b!400x400.png");
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