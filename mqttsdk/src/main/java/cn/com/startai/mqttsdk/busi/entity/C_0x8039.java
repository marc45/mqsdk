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
import cn.com.startai.mqttsdk.localbusi.UserBusi;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 绑定邮箱
 * Created by Robin on 2019/2/13.
 * qq: 419109715 彬影
 */

public class C_0x8039 implements Serializable {

    private static final String TAG = C_0x8039.class.getSimpleName();
    public static final String MSGTYPE = "0x8039";
    public static String MSGCW = "0x07";
    public static String MSG_DESC = "绑定邮箱 ";

    /**
     * 请求 绑定邮箱
     *
     * @param listener
     */
    public static void req(Req.ContentBean req, IOnCallListener listener) {

        MqttPublishRequest<StartaiMessage<Req.ContentBean>> req_msg = create_req_msg(req);
        if (req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }

        StartaiMqttPersistent.getInstance().send(req_msg, listener);
    }

    private static MqttPublishRequest<StartaiMessage<Req.ContentBean>> create_req_msg(Req.ContentBean req) {

        if (req == null) {
            SLog.e(TAG, " req is empty");
            return null;
        }

        if (TextUtils.isEmpty(req.getUserid())) {
            UserBusi userBusi = new UserBusi();
            C_0x8018.Resp.ContentBean currUser = userBusi.getCurrUser();
            if (currUser != null && !TextUtils.isEmpty(currUser.getUserid())) {
                req.setUserid(currUser.getUserid());
            }
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(MSGTYPE)
                .setMsgcw(MSGCW)
                .setContent(req).create();


        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }


        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }


    /**
     * 请求 绑定邮箱 返回结果
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
            C_0x8039.Resp.ContentBean content = resp.getContent();
            C_0x8039.Req.ContentBean errcontent = content.getErrcontent();
            content.setEmail(errcontent.getEmail());
            content.setUserid(errcontent.getUserid());
            SLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }

        StartAI.getInstance().getPersisitnet().getEventDispatcher().onBindEmailResult(resp);

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
             * userid :
             * email :
             */

            private String userid;
            private String email;

            public ContentBean() {
            }

            public ContentBean(String email) {
                this.email = email;
            }

            public ContentBean(String userid, String email) {
                this.userid = userid;
                this.email = email;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "userid='" + userid + '\'' +
                        ", email='" + email + '\'' +
                        '}';
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
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


            /**
             * userid :
             * email :
             */

            private String userid;
            private String email;

            public ContentBean() {
            }

            public ContentBean(Req.ContentBean errcontent, String userid, String email) {
                this.errcontent = errcontent;
                this.userid = userid;
                this.email = email;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", userid='" + userid + '\'' +
                        ", email='" + email + '\'' +
                        '}';
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }
    }


}
