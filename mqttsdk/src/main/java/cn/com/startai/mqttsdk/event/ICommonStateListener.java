package cn.com.startai.mqttsdk.event;

import cn.com.startai.mqttsdk.busi.entity.C_0x8018;

public interface ICommonStateListener extends IConnectionStateListener {


    /**
     * 登录 tokent 失效
     */
    void onTokenExpire(C_0x8018.Resp.ContentBean resp);

}
