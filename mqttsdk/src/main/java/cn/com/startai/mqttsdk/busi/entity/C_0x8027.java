package cn.com.startai.mqttsdk.busi.entity;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.UUID;

import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 第三方登录
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class C_0x8027 implements Serializable {

    private static final String TAG = C_0x8027.class.getSimpleName();
    public static final String MSGTYPE = "0x8027";
    public static String MSGCW = "0x07";

    /**
     * 请求 第三方登录
     *
     * @param listener
     */
    public static void req(int type, String code, IOnCallListener listener) {

        MqttPublishRequest<StartaiMessage<Req.ContentBean>> req_msg = create_req_msg(type, code);
        if (req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }

        StartaiMessage<Req.ContentBean> message = req_msg.message;


        String uuid = UUID.randomUUID().toString();

        C_0x8018.Req.ContentBean contentBean = new C_0x8018.Req.ContentBean();
        contentBean.setType(contentBean.getType());

        C_0x8018.maps.put(uuid, contentBean);
        message.setMsgid(uuid);

        StartaiMqttPersistent.getInstance().send(req_msg, listener);

    }

    private static MqttPublishRequest<StartaiMessage<Req.ContentBean>> create_req_msg(@NonNull int type, String code) {

        if (TextUtils.isEmpty(code)) {
            SLog.e(TAG, "code  can not be empty");
            return null;
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(MSGTYPE)
                .setMsgcw(MSGCW)
                .setContent(new Req.ContentBean(type, code)).create();


        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }


    /**
     * 请求 第三方登录 返回结果
     *
     * @param miof
     */
    public static void m_0x8027_resp(String miof) {


        C_0x8018.m_0x8018_resp(miof);

//        if (result == 1 && resp != null) {
//            SLog.d(TAG, "第三方登录成功");
//            StartAI.getInstance().getPersisitnet().getEventDispatcher().onloginWithThirdAccountResult(result, "", "", resp.getContent());
//        } else if (result == 0 && errorMiofMsg != null) {
//            SLog.d(TAG, "第三方登录失败");
//            StartAI.getInstance().getPersisitnet().getEventDispatcher().onloginWithThirdAccountResult(result, errorMiofMsg.getContent().getErrcode(), errorMiofMsg.getContent().getErrmsg(), resp.getContent());
//        } else {
//            SLog.e(TAG, "返回数据格式错误");
//        }

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


            /**
             * type : 2
             * code : cidigjapfijgijcoodoodap
             */

            private int type;
            private String code;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "type=" + type +
                        ", code='" + code + '\'' +
                        '}';
            }

            public ContentBean(int type, String code) {
                this.type = type;
                this.code = code;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
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


            /**
             * userid :
             * token :
             * expire_in : 7200
             */

            private String userid;
            private String token;
            private int expire_in;
            private int type;
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", userid='" + userid + '\'' +
                        ", token='" + token + '\'' +
                        ", expire_in=" + expire_in +
                        ", type=" + type +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public ContentBean() {
            }

            public ContentBean(String userid, String token, int expire_in, int type) {
                this.userid = userid;
                this.token = token;
                this.expire_in = expire_in;
                this.type = type;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public int getExpire_in() {
                return expire_in;
            }

            public void setExpire_in(int expire_in) {
                this.expire_in = expire_in;
            }
        }
    }


}
