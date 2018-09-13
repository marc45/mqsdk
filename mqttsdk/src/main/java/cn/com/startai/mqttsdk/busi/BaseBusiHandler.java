package cn.com.startai.mqttsdk.busi;


import org.json.JSONException;
import org.json.JSONObject;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.busi.entity.C_0x8000;
import cn.com.startai.mqttsdk.busi.entity.C_0x8001;
import cn.com.startai.mqttsdk.busi.entity.C_0x8002;
import cn.com.startai.mqttsdk.busi.entity.C_0x8003;
import cn.com.startai.mqttsdk.busi.entity.C_0x8004;
import cn.com.startai.mqttsdk.busi.entity.C_0x8005;
import cn.com.startai.mqttsdk.busi.entity.C_0x8015;
import cn.com.startai.mqttsdk.busi.entity.C_0x8016;
import cn.com.startai.mqttsdk.busi.entity.C_0x8017;
import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.busi.entity.C_0x8019;
import cn.com.startai.mqttsdk.busi.entity.C_0x8020;
import cn.com.startai.mqttsdk.busi.entity.C_0x8021;
import cn.com.startai.mqttsdk.busi.entity.C_0x8022;
import cn.com.startai.mqttsdk.busi.entity.C_0x8023;
import cn.com.startai.mqttsdk.busi.entity.C_0x8024;
import cn.com.startai.mqttsdk.busi.entity.C_0x8025;
import cn.com.startai.mqttsdk.busi.entity.C_0x8026;
import cn.com.startai.mqttsdk.busi.entity.C_0x8027;
import cn.com.startai.mqttsdk.busi.entity.C_0x8200;
import cn.com.startai.mqttsdk.busi.entity.C_0x9998;
import cn.com.startai.mqttsdk.busi.entity.C_0x9999;
import cn.com.startai.mqttsdk.busi.entity.MiofTag;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class BaseBusiHandler {
    private static String TAG = "BaseBusiHandler";

    public void handMessage(String topic, String msg) {

        String msgtype = "";
        String msgcw = "";
        int result = 0;
        try {

            JSONObject jsonObject = new JSONObject(msg);
            msgtype = jsonObject.getString(MiofTag.TAG_MSGTYPE);
            msgcw = jsonObject.getString(MiofTag.TAG_MSGCW);
            result = jsonObject.getInt(MiofTag.TAG_RESULT);

        } catch (JSONException e) {
            e.printStackTrace();
            SLog.e(TAG, "JSON format is not correct");
        }

        switch (msgtype) {

            case "0x8000"://获取区域节点信息
                C_0x8000.m_0x8000_resp(msg);
                return;

            case "0x8001"://设备激活
                C_0x8001.m_0x8001_resp(msg);
                break;

            case "0x8002"://添加好友
                C_0x8002.m_0x8002_resp(msg);
                break;

            case "0x8003"://注销激活
                C_0x8003.m_0x8003_resp(msg);
                break;

            case "0x8004"://删除好友
                C_0x8004.m_0x8004_resp(msg);
                break;

            case "0x8005"://查询好友关系
                C_0x8005.m_0x8005_resp(result, msg);
                break;

            case "0x8015"://修改备注名
                C_0x8015.m_0x8015_resp(msg);
                break;

            case "0x8016"://查询最新版本
                C_0x8016.m_0x8016_resp(msg);
                break;

            case "0x8017"://用户注册
                C_0x8017.m_0x8017_resp(msg);
                break;

            case "0x8018"://登录
                C_0x8018.m_0x8018_resp(msg);
                break;

            case "0x8019"://更新设备信息
                C_0x8019.m_0x8019_resp(msg);
                break;

            case "0x8020"://更新用户信息
                C_0x8020.m_0x8020_resp(msg);
                break;

            case "0x8021"://获取验证码
                C_0x8021.m_0x8021_resp(msg);
                break;

            case "0x8022"://检验验证码
                C_0x8022.m_0x8022_resp(msg);
                break;

            case "0x8023"://请求发送邮件
                C_0x8023.m_0x8023_resp(msg);
                break;

            case "0x8024"://查询用户信息
                C_0x8024.m_0x8024_resp(msg);
                break;

            case "0x8025"://修改密码
                C_0x8025.m_0x8025_resp(msg);
                break;

            case "0x8026"://手机重置密码

                C_0x8026.m_0x8026_resp(msg);
                break;
            case "0x8027":

                C_0x8027.m_0x8027_resp(msg);
                break;

            case "0x8200"://消息透传

                C_0x8200.m_0x8200_resp(msg);
                break;

            case "0x9998"://终端上线

                C_0x9998.m_0x9998_resp(msg);
                break;

            case "0x9999"://终端下线

                C_0x9999.m_0x9999_resp(msg);
                break;

            default:

                break;
        }

        //数据回调到应用层
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onMessageArrived(topic, msg);


    }


    /**
     * 是否需要判断 result
     * <p>
     * 如果是云端 收到终端主动给终端发的消息或云端给终端主动发的消息，是不需要判断result的，如果是收到返回的消息则需要判断result
     *
     * @param msgcw
     * @return
     */
    private boolean isNeedCheckResult(String msgcw) {

        if ("0x01".equals(msgcw)
                || "0x03".equals(msgcw)
                || "0x05".equals(msgcw)
                || "0x09".equals(msgcw)) {
            return false;
        }
        return true;


    }
}
