package cn.com.startai.mqttsdk.control;


import android.text.TextUtils;

import java.util.ArrayList;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.busi.entity.C_0x8000;
import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.control.entity.AreaLocation;
import cn.com.startai.mqttsdk.control.entity.TopicBean;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;
import cn.com.startai.mqttsdk.utils.SShareUtils;


/**
 * sp管理类
 * Created by Robin on 2018/5/11.
 * qq: 419109715 彬影
 */

public class SPController {

    private static final String TAG = SPController.class.getSimpleName();

    private static final String SP_LASTGET_0X800_TIME = "SP_LASTGET_0X800_TIME";

    /**
     * 获取上次同步 0x8000的时间
     *
     * @return
     */
    public static long getLastGet_0x800_respTime() {

        long aLong = SShareUtils.getLong(SP_LASTGET_0X800_TIME, 0);

        return aLong;
    }

    /**
     * 保存此次同步节点的时间
     *
     * @param time
     */
    public static void setLastGet_0x800_respTime(long time) {

        SShareUtils.putLong(SP_LASTGET_0X800_TIME, time);

    }

    private static final String SP_LOCATION = "SP_LOCATION";

    /**
     * 获取上次同步 0x8000的时间
     *
     * @return
     */
    public static AreaLocation getLocation() {

        String str = SShareUtils.getString(SP_LOCATION, "");
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return SJsonUtils.fromJson(str, AreaLocation.class);
    }

    /**
     * 保存此次同步节点的时间
     *
     * @param areaLocation
     */
    public static void setLocation(AreaLocation areaLocation) {
        if (areaLocation == null) {

            SShareUtils.putString(SP_LOCATION, "");
        } else {
            SShareUtils.putString(SP_LOCATION, SJsonUtils.toJson(areaLocation));
        }

    }


    private static final String SP_AREA_NODE = "SP_AREA_NODE";

    /**
     * 查所有
     *
     * @return
     */
    public static C_0x8000.Resp.ContentBean getAllAreaNodeBean() {

        String s = SShareUtils.getString(SP_AREA_NODE, "");
        if (TextUtils.isEmpty(s)) {
            return null;
        } else {
            C_0x8000.Resp.ContentBean contentBean = SJsonUtils.fromJson(s, C_0x8000.Resp.ContentBean.class);
            if (contentBean == null) {
                return null;
            } else {
                if (contentBean.getNode() == null || contentBean.getNode().size() == 0) {
                    return null;
                }
            }
            return SJsonUtils.fromJson(s, C_0x8000.Resp.ContentBean.class);
        }
    }

    /**
     * 保存0x8000区域节点信息
     *
     * @param contentBean
     */
    public static void saveAreaNodeBeans(C_0x8000.Resp.ContentBean contentBean) {
        if (contentBean == null) {
            SShareUtils.putString(SP_AREA_NODE, "");
            return;
        }
        String s = SJsonUtils.toJson(contentBean);
        SShareUtils.putString(SP_AREA_NODE, s);
    }


    private static final String SP_CLIENTID = "SP_CLIENTID";

    /**
     * 查本地的clientid
     *
     * @return
     */
    public static String getClientid() {
        return SShareUtils.getString(SP_CLIENTID, "");
    }

    /**
     * 保存clientid到本地
     */
    public static void setClientid(String clientid) {
        SShareUtils.putString(SP_CLIENTID, clientid);
    }


    private static final String SP_ACTIVITE = "SP_ACTIVITE";

    /**
     * 查本地激活状态
     *
     * @return
     */
    public static boolean getIsActivite() {
        //如果appid变化了会重新激活
        return SShareUtils.getBoolean(SP_ACTIVITE + MqttConfigure.appid + MqttConfigure.getSn(StartAI.getContext()), false);
    }

    /**
     * 保存激活状态
     */
    public static void setIsActivite(boolean isActivite) {
        SShareUtils.putBoolean(SP_ACTIVITE + MqttConfigure.appid + MqttConfigure.getSn(StartAI.getContext()), isActivite);
    }


    private static final String SP_USERINFO = "SP_USERINFO";

    /**
     * 获取本地保存的用户信息
     *
     * @return
     */
    public static C_0x8018.Resp.ContentBean getUserInfo() {

        String string = SShareUtils.getString(SP_USERINFO, "");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        C_0x8018.Resp.ContentBean contentBean = SJsonUtils.fromJson(string, C_0x8018.Resp.ContentBean.class);
        SLog.d(TAG, "sp currUser = " + contentBean);
        if (TextUtils.isEmpty(contentBean.getUserid())) {
            setUserInfo(null);
            return null;
        } else {
            return contentBean;
        }
    }

    /**
     * 将用户信息保存到本地
     */
    public static void setUserInfo(C_0x8018.Resp.ContentBean c_0x8018) {

        if (c_0x8018 != null) {
            SShareUtils.putString(SP_USERINFO, SJsonUtils.toJson(c_0x8018));

        } else {
            SShareUtils.putString(SP_USERINFO, "");

        }
    }


    private static final String SP_APPID = "SP_APPID";

    /**
     * 获取appid
     *
     * @return
     */
    public static String getAppid() {
        return SShareUtils.getString(SP_APPID, "");
    }

    public static void setAppid(String appid) {

        SShareUtils.putString(SP_APPID, appid);
    }


    /**
     * 清除所有sp缓存消息
     */
    public static void clearAllSp() {
        SShareUtils.clear();
    }
}
