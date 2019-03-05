package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

import java.io.Serializable;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.DistributeParam;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.listener.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 查询支付宝密钥
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class C_0x8033 implements Serializable {

    private static final String TAG = C_0x8033.class.getSimpleName();
    public static final String MSGTYPE = "0x8033";
    public static String MSGCW = "0x07";
    public static String MSG_DESC = "查询支付宝密钥 ";

    public static final String AUTH_TYPE_AUTH = "AUTH";
    public static final String AUTH_TYPE_LOGIN = "LOGIN";

    /**
     * 请求 查询支付宝密钥
     *
     * @param listener
     */
    public static void req(String authType, IOnCallListener listener) {

        MqttPublishRequest<StartaiMessage<Req.ContentBean>> req_msg = create_req_msg(authType);
        if (req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }

        StartaiMqttPersistent.getInstance().send(req_msg, listener);
    }

    private static MqttPublishRequest<StartaiMessage<Req.ContentBean>> create_req_msg(String authType) {

        if (TextUtils.isEmpty(authType)) {
            SLog.e(TAG, "authTargetId is empty");
            return null;
        }
        if (!AUTH_TYPE_LOGIN.equals(authType) && !AUTH_TYPE_AUTH.equals(authType)) {
            SLog.e(TAG, "authTargetId is error,must be 'AUTH' or 'LOGIN' ");
            return null;
        }
        authType = authType + "_" + System.currentTimeMillis();

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(MSGTYPE)
                .setMsgcw(MSGCW)
                .setContent(new C_0x8033.Req.ContentBean(authType)).create();

        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }



        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }


    /**
     * 请求 查询支付宝密钥 返回结果
     *
     * @param miof
     */
    public static void m_resp(String miof) {


        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, MSG_DESC + " 返回格式错误");
            return;
        }
        if (resp.getResult() == 1) {
            SLog.e(TAG, MSG_DESC + " 成功");

        } else {
            C_0x8033.Resp.ContentBean content = resp.getContent();
            C_0x8033.Req.ContentBean errcontent = content.getErrcontent();
            content.setAuthTargetId(errcontent.getAuthTargetId());
            SLog.e(TAG, MSG_DESC+" 失败 "+resp.getContent().getErrmsg());
        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onGetAlipayAuthInfoResult(resp);
    }

    public static class Req {
        private ContentBean content;

        public Req(ContentBean content) {
            this.content = content;
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean {
            private String authTargetId;

            public ContentBean(String authTargetId) {
                this.authTargetId = authTargetId;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "authTargetId='" + authTargetId + '\'' +
                        '}';
            }

            public String getAuthTargetId() {
                return authTargetId;
            }

            public void setAuthTargetId(String authTargetId) {
                this.authTargetId = authTargetId;
            }
        }


    }


    public static class Resp extends BaseMessage implements Serializable {

        private ContentBean content;

        @Override
        public String toString() {
            return "Resp{" +
                    "content=" + content +
                    ", msgcw='" + msgcw + '\'' +
                    ", msgtype='" + msgtype + '\'' +
                    ", fromid='" + fromid + '\'' +
                    ", toid='" + toid + '\'' +
                    ", domain='" + domain + '\'' +
                    ", appid='" + appid + '\'' +
                    ", ts=" + ts +
                    ", msgid='" + msgid + '\'' +
                    ", m_ver='" + m_ver + '\'' +
                    ", result=" + result +
                    '}';
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends BaseContentBean {


            private Req.ContentBean errcontent = null;

            private String aliPayAuthInfo;
            private String authTargetId;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", aliPayAuthInfo='" + aliPayAuthInfo + '\'' +
                        ", authTargetId='" + authTargetId + '\'' +
                        '}';
            }

            public String getAuthTargetId() {
                return authTargetId;
            }

            public void setAuthTargetId(String authTargetId) {
                this.authTargetId = authTargetId;
            }

            public String getAliPayAuthInfo() {

                return aliPayAuthInfo;
            }

            public void setAliPayAuthInfo(String aliPayAuthInfo) {
                this.aliPayAuthInfo = aliPayAuthInfo;
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

        }
    }


}
