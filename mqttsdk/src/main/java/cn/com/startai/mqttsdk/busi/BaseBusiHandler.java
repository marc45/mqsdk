package cn.com.startai.mqttsdk.busi;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
import cn.com.startai.mqttsdk.busi.entity.C_0x8028;
import cn.com.startai.mqttsdk.busi.entity.C_0x8031;
import cn.com.startai.mqttsdk.busi.entity.C_0x8033;
import cn.com.startai.mqttsdk.busi.entity.C_0x8034;
import cn.com.startai.mqttsdk.busi.entity.C_0x8035;
import cn.com.startai.mqttsdk.busi.entity.C_0x8036;
import cn.com.startai.mqttsdk.busi.entity.C_0x8037;
import cn.com.startai.mqttsdk.busi.entity.C_0x8038;
import cn.com.startai.mqttsdk.busi.entity.C_0x8039;
import cn.com.startai.mqttsdk.busi.entity.C_0x8200;
import cn.com.startai.mqttsdk.busi.entity.C_0x9998;
import cn.com.startai.mqttsdk.busi.entity.C_0x9999;
import cn.com.startai.mqttsdk.busi.entity.MiofTag;
import cn.com.startai.mqttsdk.control.SPController;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class BaseBusiHandler {
    private static String TAG = "BaseBusiHandler";

    public void handMessage(String topic, String msg) {

        String appid = "";
        String msgtype = "";
        String msgcw = "";


        try {

            JSONObject jsonObject = new JSONObject(msg);
            if (msg.contains("\"" + MiofTag.TAG_APPID + "\"")) {
                appid = jsonObject.getString(MiofTag.TAG_APPID);
            }
            if (msg.contains("\"" + MiofTag.TAG_MSGTYPE + "\"")) {
                msgtype = jsonObject.getString(MiofTag.TAG_MSGTYPE);
            }
            if (msg.contains("\"" + MiofTag.TAG_MSGCW + "\"")) {
                msgcw = jsonObject.getString(MiofTag.TAG_MSGCW);
            }


            if (!checkMsg(appid, msgcw)) {
                return;
            }


        } catch (JSONException e) {
            e.printStackTrace();
            SLog.e(TAG, "JSON format is not correct");
            return;
        }

        //反射调用
//        reflexCallMiofMethod(msgtype, msg);


        switch (msgtype) {
            case C_0x9998.MSGTYPE://终端上线

                C_0x9998.m_resp(msg);
                break;

            case C_0x9999.MSGTYPE://终端下线

                C_0x9999.m_resp(msg);
                break;

            case C_0x8000.MSGTYPE://获取区域节点信息
                C_0x8000.m_resp(msg);
                return;

            case C_0x8001.MSGTYPE://设备激活
                C_0x8001.m_resp(msg);
                break;

            case C_0x8002.MSGTYPE://添加好友
                C_0x8002.m_resp(msg);
                break;

            case C_0x8003.MSGTYPE://注销激活
                C_0x8003.m_resp(msg);
                break;

            case C_0x8004.MSGTYPE://删除好友
                C_0x8004.m_resp(msg);
                break;

            case C_0x8005.MSGTYPE://查询好友关系
                C_0x8005.m_resp(msg);
                break;

            case C_0x8015.MSGTYPE://修改备注名
                C_0x8015.m_resp(msg);
                break;

            case C_0x8016.MSGTYPE://查询最新版本
                C_0x8016.m_resp(msg);
                break;

            case C_0x8017.MSGTYPE://用户注册
                C_0x8017.m_resp(msg);
                break;

            case C_0x8018.MSGTYPE://登录
                C_0x8018.m_resp(msg);
                break;

            case C_0x8019.MSGTYPE://更新设备信息
                C_0x8019.m_resp(msg);
                break;

            case C_0x8020.MSGTYPE://更新用户信息
                C_0x8020.m_resp(msg);
                break;

            case C_0x8021.MSGTYPE://获取验证码
                C_0x8021.m_resp(msg);
                break;

            case C_0x8022.MSGTYPE://检验验证码
                C_0x8022.m_resp(msg);
                break;

            case C_0x8023.MSGTYPE://请求发送邮件
                C_0x8023.m_resp(msg);
                break;

            case C_0x8024.MSGTYPE://查询用户信息
                C_0x8024.m_resp(msg);
                break;

            case C_0x8025.MSGTYPE://修改密码
                C_0x8025.m_resp(msg);
                break;

            case C_0x8026.MSGTYPE://手机重置密码

                C_0x8026.m_resp(msg);
                break;
            case C_0x8027.MSGTYPE://第三方登录

                C_0x8027.m_resp(msg);
                break;

            case C_0x8028.MSGTYPE: //第三方支付 统一下单
                C_0x8028.m_resp(msg);
                break;
            case C_0x8200.MSGTYPE://消息透传
                C_0x8200.m_resp(topic, msg);
                break;

            case C_0x8031.MSGTYPE: //查询订单支付状态
                C_0x8031.m_resp(msg);
                break;


            case C_0x8033.MSGTYPE: //查询 支付宝密钥
                C_0x8033.m_resp(msg);

                break;

            case C_0x8034.MSGTYPE: //查询 支付宝密钥
                C_0x8034.m_resp(msg);

                break;
            case C_0x8035.MSGTYPE://查询天气
                C_0x8035.m_resp(msg);
                break;
            case C_0x8036.MSGTYPE: //解绑 第三方账号
                C_0x8036.m_resp(msg);

                break;

            case C_0x8037.MSGTYPE: //绑定 第三方账号
                C_0x8037.m_resp(msg);

                break;
            case C_0x8038.MSGTYPE: //分页获取好友列表
                C_0x8038.m_resp(msg);

                break;
            case C_0x8039.MSGTYPE: //绑定邮箱
                C_0x8039.m_resp(msg);

                break;

            default:

                break;
        }

        //原始数据数据回调到应用层
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onMessageArrived(topic, msg);

    }

    /**
     * 反射调用 miof 处理方法
     *
     * @param msgType
     * @param msg
     */
    private void reflexCallMiofMethod(String msgType, String msg) {

        long t = System.currentTimeMillis();

        String className = "cn.com.startai.mqttsdk.busi.entity.C_" + msgType;

        try {
            Class<?> aClass = Class.forName(className);
            Method m_resp = aClass.getMethod("m_resp", new Class[]{String.class});

            m_resp.invoke(null, msg);

            SLog.d(TAG, "invoke call use time = " + (System.currentTimeMillis() - t));

            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        SLog.d(TAG, "sdk 不支持的 msgtype ");

    }

    //接收到不属于自己的消息 需要排除
    private boolean checkMsg(String appid, String msgcw) throws JSONException {

        //排除无用消息
        if (SPController.getAppid().equals(appid)) {
            //正常消息 正常处理

        } else {
            //不是自己的appid
            if (msgcw.equals("0x08") || msgcw.equals("0x09")) {//从云端下来的消息 不处理
                SLog.e(TAG, "receiver other appid msg " + msgcw);
                return false;
            } else {
                //从对端 过来的消息 都认为是正常消息

            }

        }
        return true;
    }
}
