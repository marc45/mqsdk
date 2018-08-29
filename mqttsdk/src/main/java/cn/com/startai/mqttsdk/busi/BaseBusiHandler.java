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
import cn.com.startai.mqttsdk.busi.entity.C_0x8200;
import cn.com.startai.mqttsdk.busi.entity.C_0x9998;
import cn.com.startai.mqttsdk.busi.entity.C_0x9999;
import cn.com.startai.mqttsdk.busi.entity.MiofTag;
import cn.com.startai.mqttsdk.event.PersistentEventDispatcher;
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

        ErrorMiofMsg errorMiofMsg = null;
        if (isNeedCheckResult(msgcw) && result == 0) {
            errorMiofMsg = SJsonUtils.fromJson(msg, ErrorMiofMsg.class);
        }

        switch (msgtype) {

            case "0x8000"://获取区域节点信息
                C_0x8000.Resp resp_0x8000 = SJsonUtils.fromJson(msg, C_0x8000.Resp.class);
                C_0x8000.m_0x8000_resp(result, resp_0x8000, errorMiofMsg);
                return;

            case "0x8001"://设备激活
                C_0x8001.Resp resp_0x8001 = SJsonUtils.fromJson(msg, C_0x8001.Resp.class);
                C_0x8001.m_0x8001_resp(result, resp_0x8001, errorMiofMsg);
                break;

            case "0x8002"://添加好友
                C_0x8002.Resp resp_0x8002 = SJsonUtils.fromJson(msg, C_0x8002.Resp.class);
                C_0x8002.m_0x8002_resp(result, resp_0x8002, errorMiofMsg);

                break;
            case "0x8003"://注销激活
                C_0x8003.Resp resp = SJsonUtils.fromJson(msg, C_0x8003.Resp.class);
                C_0x8003.m_0x8003_resp(result, resp, errorMiofMsg);
                break;
            case "0x8004"://删除好友
                C_0x8004.Resp resp_0x8004 = SJsonUtils.fromJson(msg, C_0x8004.Resp.class);
                C_0x8004.m_0x8004_resp(result, resp_0x8004, errorMiofMsg);
                break;
            case "0x8005"://查询好友关系
                C_0x8005.Resp resp_0x8005 = SJsonUtils.fromJson(msg, C_0x8005.Resp.class);
                C_0x8005.m_0x8005_resp(result, resp_0x8005, errorMiofMsg);
                break;
            case "0x8015":
                C_0x8015.Resp resp_0x8015 = SJsonUtils.fromJson(msg, C_0x8015.Resp.class);
                C_0x8015.m_0x8015_resp(result, resp_0x8015, errorMiofMsg);
                break;
            case "0x8016":
                C_0x8016.Resp resp_0x8016 = SJsonUtils.fromJson(msg, C_0x8016.Resp.class);
                C_0x8016.m_0x8016_resp(result, resp_0x8016, errorMiofMsg);
                break;
            case "0x8017"://用户注册
                C_0x8017.Resp resp_0x8017 = SJsonUtils.fromJson(msg, C_0x8017.Resp.class);
                C_0x8017.m_0x8017_resp(result, resp_0x8017, errorMiofMsg);
                break;
            case "0x8018"://登录
                C_0x8018.Resp resp_0x8018 = SJsonUtils.fromJson(msg, C_0x8018.Resp.class);
                C_0x8018.m_0x8018_resp(result, resp_0x8018, errorMiofMsg);
                break;
            case "0x8019"://更新设备信息
                C_0x8019.Resp resp_0x8019 = SJsonUtils.fromJson(msg, C_0x8019.Resp.class);
                C_0x8019.m_0x8019_resp(result, resp_0x8019, errorMiofMsg);
                break;
            case "0x8020"://更新用户信息
                C_0x8020.Resp reap_0x8020 = SJsonUtils.fromJson(msg, C_0x8020.Resp.class);
                C_0x8020.m_0x8020_resp(result, reap_0x8020, errorMiofMsg);
                break;
            case "0x8021"://获取验证码
                C_0x8021.Resp resp_0x8021 = SJsonUtils.fromJson(msg, C_0x8021.Resp.class);
                C_0x8021.m_0x8021_resp(result, resp_0x8021, errorMiofMsg);

                break;
            case "0x8022"://检验验证码
                C_0x8022.Resp resp_0x8022 = SJsonUtils.fromJson(msg, C_0x8022.Resp.class);
                C_0x8022.m_0x8022_resp(result, resp_0x8022, errorMiofMsg);
                break;
            case "0x8023"://请求发送邮件
                C_0x8023.Resp resp_0x8023 = SJsonUtils.fromJson(msg, C_0x8023.Resp.class);
                C_0x8023.m_0x8023_resp(result, resp_0x8023, errorMiofMsg);
                break;
            case "0x8024"://查询用户信息
                C_0x8024.Resp resp_0x8024 = SJsonUtils.fromJson(msg, C_0x8024.Resp.class);
                C_0x8024.m_0x8024_resp(result, resp_0x8024, errorMiofMsg);
                break;
            case "0x8025"://修改密码
                C_0x8025.Resp resp_0x8025 = SJsonUtils.fromJson(msg, C_0x8025.Resp.class);
                C_0x8025.m_0x8025_resp(result, resp_0x8025, errorMiofMsg);
                break;
            case "0x8026"://手机重置密码
                C_0x8026.Resp resp_0x8026 = SJsonUtils.fromJson(msg, C_0x8026.Resp.class);
                C_0x8026.m_0x8026_resp(result, resp_0x8026, errorMiofMsg);
                break;
            case "0x8200"://消息透传
                C_0x8200.Resp resp_0x8200 = SJsonUtils.fromJson(msg, C_0x8200.Resp.class);
                C_0x8200.m_0x8200_resp(result, resp_0x8200, errorMiofMsg);
                break;
            case "0x9998"://终端上线
                C_0x9998.Resp resp_0x9998 = SJsonUtils.fromJson(msg, C_0x9998.Resp.class);
                C_0x9998.m_0x9998_resp(result, resp_0x9998, errorMiofMsg);
                break;
            case "0x9999"://终端下线
                C_0x9999.Resp resp_0x9999 = SJsonUtils.fromJson(msg, C_0x9999.Resp.class);
                C_0x9999.m_0x9999_resp(result, resp_0x9999, errorMiofMsg);
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
