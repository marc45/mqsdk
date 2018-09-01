package cn.com.startai.mqttsdk.busi;

import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.localbusi.UserBusi;

/**
 * Created by Robin on 2018/8/31.
 * qq: 419109715 彬影
 */

public class CommonBusiManager {
    private static final CommonBusiManager ourInstance = new CommonBusiManager();

    public static CommonBusiManager getInstance() {
        return ourInstance;
    }

    private CommonBusiManager() {
    }


    /**
     * 获取当前登录 用户
     *
     * @return null 表示 未登录用户 或 已登录用户过期
     */
    public C_0x8018.Resp.ContentBean getCurrUser() {

        C_0x8018.Resp.ContentBean currUser = new UserBusi().getCurrUser();
        return currUser;

    }


}
