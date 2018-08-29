package cn.com.startai.mqsdk.listener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.com.startai.mqsdk.HomeActivity;
import cn.com.startai.mqsdk.MyApp;
import cn.com.startai.mqsdk.busi.C_0x8101;
import cn.com.startai.mqsdk.busi.MessageSender;
import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqsdk.util.airkiss.NetworkUtils;
import cn.com.startai.mqsdk.util.eventbus.E_0x8001_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8001_Resp_;
import cn.com.startai.mqsdk.util.eventbus.E_0x8002_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8003_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8004_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8005_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8015_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8016_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8017_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8018_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8018_Resp_;
import cn.com.startai.mqsdk.util.eventbus.E_0x8020_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8021_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8022_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8023_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8024_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8025_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8026_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8200_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_Device_Connect_Status;
import cn.com.startai.mqsdk.util.eventbus.EventBean;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.busi.ErrorMiofMsg;
import cn.com.startai.mqttsdk.busi.entity.C_0x8001;
import cn.com.startai.mqttsdk.busi.entity.C_0x8002;
import cn.com.startai.mqttsdk.busi.entity.C_0x8005;
import cn.com.startai.mqttsdk.busi.entity.C_0x8015;
import cn.com.startai.mqttsdk.busi.entity.C_0x8016;
import cn.com.startai.mqttsdk.busi.entity.C_0x8017;
import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.busi.entity.C_0x8020;
import cn.com.startai.mqttsdk.busi.entity.C_0x8021;
import cn.com.startai.mqttsdk.busi.entity.C_0x8023;
import cn.com.startai.mqttsdk.busi.entity.C_0x8024;
import cn.com.startai.mqttsdk.busi.entity.C_0x8025;
import cn.com.startai.mqttsdk.busi.entity.C_0x8026;
import cn.com.startai.mqttsdk.busi.entity.C_0x8200;
import cn.com.startai.mqttsdk.busi.entity.MiofTag;
import cn.com.startai.mqttsdk.event.AOnStartaiMessageArriveListener;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * Created by Robin on 2018/7/10.
 * qq: 419109715 彬影
 */

public class MyPushListener extends AOnStartaiMessageArriveListener {
    private String TAG = MyPushListener.class.getSimpleName();


    @Override
    public void onCommand(String topic, String miofmsg) {


        String msgtype = "";
        String msgcw = "";
        int result = 0;
        try {

            JSONObject jsonObject = new JSONObject(miofmsg);
            msgtype = jsonObject.getString(MiofTag.TAG_MSGTYPE);
            result = jsonObject.getInt(MiofTag.TAG_RESULT);
            msgcw = jsonObject.getString(MiofTag.TAG_MSGCW);

        } catch (JSONException e) {
            e.printStackTrace();
            SLog.e(TAG, "JSON format is not correct");
        }


        ErrorMiofMsg errorMiofMsg = null;
        if (isReceiverMsgHandlerResult(msgcw) && result == 0) {
            errorMiofMsg = SJsonUtils.fromJson(miofmsg, ErrorMiofMsg.class);
        }


        switch (msgtype) {


            case "0x8101":
                C_0x8101.Resp resp = SJsonUtils.fromJson(miofmsg, C_0x8101.Resp.class);
                C_0x8101.m_0x8101_resp(result, resp, errorMiofMsg);
                break;

            case "0x8102":

                break;


        }


        if (topic.startsWith(HomeActivity.topicTest)) {
            TAndL.TL(MyApp.getContext(), "topic = " + topic + " \nmsg = " + miofmsg);
        }

        TAndL.file(new String[]{"direction", "topic", "msg"}, new String[]{"mq recv", topic, miofmsg});

    }

    @Override
    public boolean needUISafety() {
        return false;
    }

    @Override
    public void onRegisterResult(int result, String errorCode, String errorMsg, C_0x8017.Resp.ContentBean contentBean) {
        super.onRegisterResult(result, errorCode, errorMsg, contentBean);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_REGISTER_RESULT, new E_0x8017_Resp(result, errorCode, errorMsg, contentBean)));
    }

    @Override
    public void onBindResult(int result, String errorCode, String errorMsg, String id, C_0x8002.Resp.ContentBean.BebindingBean bebinding) {
        super.onBindResult(result, errorCode, errorMsg, id, bebinding);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_BIND_RESULT, new E_0x8002_Resp(result, errorCode, errorMsg, id, bebinding)));
    }

    @Override
    public void onUnBindResult(int result, String errorCode, String errorMsg, String id, String beUnbindid) {
        super.onUnBindResult(result, errorCode, errorMsg, id, beUnbindid);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_UNBIND_RESULT, new E_0x8004_Resp(result, errorCode, errorMsg, id, beUnbindid)));
    }


    @Override
    public void onLoginResult(int result, String errorCode, String errorMsg, C_0x8018.Resp.ContentBean loginInfo) {
        super.onLoginResult(result, errorCode, errorMsg, loginInfo);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_LOGIN_RESULT, new E_0x8018_Resp(result, errorCode, errorMsg, loginInfo)));
    }

    @Override
    public void onGetBindListResult(int result, String errorCode, String errorMsg, String id, ArrayList<C_0x8005.Resp.ContentBean> bindList) {
        super.onGetBindListResult(result, errorCode, errorMsg, id, bindList);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_BINDLIST_RESULT, new E_0x8005_Resp(result, errorCode, errorMsg, id, bindList)));
    }

    @Override
    public void onActiviteResult(int initResult, String errcode, String errmsg) {
        super.onActiviteResult(initResult, errcode, errmsg);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_ACTIVATE_RESULT, new E_0x8001_Resp(initResult, errcode, errmsg)));
    }

    @Override
    public void onGetIdentifyCodeResult(int result, String errorCode, String errorMsg, C_0x8021.Resp.ContentBean contentBean) {

        super.onGetIdentifyCodeResult(result, errorCode, errorMsg, contentBean);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_GET_IDENTIFY_RESULT, new E_0x8021_Resp(result, errorCode, errorMsg, contentBean)));
    }


    @Override
    public void onCheckIdetifyResult(int result, String errorCode, String errorMsg) {
        super.onCheckIdetifyResult(result, errorCode, errorMsg);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_CHECK_IDENTIFY_RESULT, new E_0x8022_Resp(result, errorCode, errorMsg)));
    }

    @Override
    public void onPassthroughResult(int result, C_0x8200.Resp resp, String errorCode, String errorMsg, String dataString, byte[] dataByteArray) {
        super.onPassthroughResult(result, resp, errorCode, errorMsg, dataString, dataByteArray);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_PASSTHROUTH_RESULT, new E_0x8200_Resp(result, resp, errorCode, errorMsg, dataString, dataByteArray)));
    }

    @Override
    public void onLogoutResult(int result, String errorCode, String errorMsg) {
        super.onLogoutResult(result, errorCode, errorMsg);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_LOGOUT_RESULT, new E_0x8018_Resp_(result, errorCode, errorMsg)));
    }

    @Override
    public void onUnActiviteResult(int result, String errorCode, String errorMsg) {
        super.onUnActiviteResult(result, errorCode, errorMsg);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_UN_ACTIVITE_RESULT, new E_0x8003_Resp(result, errorCode, errorMsg)));
    }

    @Override
    public void onHardwareActivateResult(int result, String errorCode, String errorMsg, C_0x8001.Resp.ContentBean contentBean) {
        super.onHardwareActivateResult(result, errorCode, errorMsg, contentBean);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_CUSTOMER_ACTIVATE_RESULT, new E_0x8001_Resp_(result, contentBean, errorCode, errorMsg)));

    }

    @Override
    public void onUpdateUserInfoResult(int result, String errorCode, String errorMsg, C_0x8020.Resp.ContentBean contentBean) {
        super.onUpdateUserInfoResult(result, errorCode, errorMsg, contentBean);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_UPDATEUSEINFO_RESULT, new E_0x8020_Resp(result, contentBean, errorCode, errorMsg)));
    }

    @Override
    public void onGetUserInfoResult(int result, String errorCode, String errorMsg, C_0x8024.Resp.ContentBean contentBean) {
        super.onGetUserInfoResult(result, errorCode, errorMsg, contentBean);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_GETUSERINFO_RESULT, new E_0x8024_Resp(result, contentBean, errorCode, errorMsg)));
    }

    @Override
    public void onDeviceConnectStatusChange(String userid, int status, String sn) {
        super.onDeviceConnectStatusChange(userid, status, sn);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_DEVICE_CONNECT_STATUS_CHANGE, new E_Device_Connect_Status(userid, status, sn)));
    }

    @Override
    public void onGetLatestVersionResult(int result, String errorCode, String errorMsg, C_0x8016.Resp.ContentBean contentBean) {
        super.onGetLatestVersionResult(result, errorCode, errorMsg, contentBean);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_GETLATEST_VERSION_RESULT, new E_0x8016_Resp(result, errorCode, errorMsg, contentBean)));
    }

    @Override
    public void onUpdateUserPwdResult(int result, String errorCode, String errorMsg, C_0x8025.Resp.ContentBean contentBean) {
        super.onUpdateUserPwdResult(result, errorCode, errorMsg, contentBean);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_UPDATE_USER_PWD, new E_0x8025_Resp(result, errorCode, errorMsg, contentBean)));

    }

    @Override
    public void onUpdateRemarkResult(int result, String errorCode, String errorMsg, C_0x8015.Resp.ContentBean contentBean) {
        super.onUpdateRemarkResult(result, errorCode, errorMsg, contentBean);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_UPDATE_REMARK, new E_0x8015_Resp(result, errorCode, errorMsg, contentBean)));

    }

    @Override
    public void onResetMobileLoginPwdResult(int result, String errorCode, String errorMsg, C_0x8026.Resp.ContentBean contentBean) {
        super.onResetMobileLoginPwdResult(result, errorCode, errorMsg, contentBean);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_RESET_MOBILE_LOGIN_PWD, new E_0x8026_Resp(result, errorCode, errorMsg, contentBean)));

    }

    @Override
    public void onSendEmailResult(int result, String errorCode, String errorMsg, C_0x8023.Resp.ContentBean contentBean) {
        super.onSendEmailResult(result, errorCode, errorMsg, contentBean);
        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_SEND_EMAIL_RESULT, new E_0x8023_Resp(result, errorCode, errorMsg, contentBean)));

    }

    /**
     * true 接收消息处理返回结果
     * false 接收消息处理业务
     *
     * @param msgcw
     * @return
     */
    private static boolean isReceiverMsgHandlerResult(String msgcw) {

        if ("0x01".equals(msgcw)
                || "0x03".equals(msgcw)
                || "0x05".equals(msgcw)
                || "0x09".equals(msgcw)) {
            return false;
        }
        return true;


    }
}
