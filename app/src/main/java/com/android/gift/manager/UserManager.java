package com.android.gift.manager;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/6
 */

public class UserManager {

    private static volatile UserManager mInstance;
    private String mUserId="88888888";
    private String mNickName="周杰伦";
    private String mAcatar="http://img.mp.sohu.com/upload/20170811/eaec0c0513e544adb68c6b2af0fdbb1b_th.png";
    private int mUserGradle=30;

    /**
     * 单例初始化
     * @return
     */
    public static synchronized UserManager getInstance() {
        synchronized (UserManager.class) {
            if (null == mInstance) {
                mInstance = new UserManager();
            }
        }
        return mInstance;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickName) {
        mNickName = nickName;
    }

    public String getAcatar() {
        return mAcatar;
    }

    public void setAcatar(String acatar) {
        mAcatar = acatar;
    }

    public int getUserGradle() {
        return mUserGradle;
    }

    public void setUserGradle(int userGradle) {
        mUserGradle = userGradle;
    }
}