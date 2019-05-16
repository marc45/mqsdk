package cn.com.startai.mqttsdk.localbusi;


import android.text.TextUtils;

import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.control.SDBmanager;
import cn.com.startai.mqttsdk.control.entity.UserBean;
import cn.com.startai.mqttsdk.utils.SLog;

import static cn.com.startai.mqttsdk.StartAI.TAG;

/**
 * Created by Robin on 2019/4/11.
 * 419109715@qq.com 彬影
 */
public class SUserManager {


    private SUserManager() {
    }

    public static SUserManager getInstance() {
        return SingleTonHoulder.singleTonInstance;
    }


    private static class SingleTonHoulder {
        private static final SUserManager singleTonInstance = new SUserManager();
    }


    private String userId;
    private C_0x8018.Resp.ContentBean currUser;

    public synchronized C_0x8018.Resp.ContentBean getCurrUser() {
        if (currUser == null) {
            currUser = new UserBusi().getCurrUser();
            if (currUser != null) {
                userId = currUser.getUserid();
            }
        }
        return currUser;
    }

    public synchronized String getUserId() {

        if (TextUtils.isEmpty(userId)) {
            getCurrUser();
        }
        SLog.d(TAG, "userid = " + userId);
        return userId;
    }

    public synchronized void setUserId(String userId) {
        this.userId = userId;
    }

    public synchronized void setCurrUser(C_0x8018.Resp.ContentBean currUser) {
        this.currUser = currUser;
    }


    public synchronized void onTokenExpire() {

    }

    public synchronized void onLogin(UserBean userBean) {

    }

    public synchronized void onLogout() {

    }

}
