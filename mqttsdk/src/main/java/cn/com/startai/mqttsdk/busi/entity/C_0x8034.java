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
 * 更改手机号
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class C_0x8034 implements Serializable {

    private static final String TAG = C_0x8034.class.getSimpleName();
    public static final String MSGTYPE = "0x8034";
    public static String MSGCW = "0x07";
    public static String MSG_DESC = "更改手机号 ";

    /**
     * 请求 更改手机号
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

        String userid = req.getUserid();
        String mobile = req.getMobile();
        if (TextUtils.isEmpty(mobile)) {
            SLog.e(TAG, "mobile 号不能为空");
            return null;
        }

        if (TextUtils.isEmpty(userid)) {
            C_0x8018.Resp.ContentBean currUser = new UserBusi().getCurrUser();
            if (currUser != null) {
                req.setUserid(currUser.getUserid());
            }
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(MSGTYPE)
                .setMsgcw(MSGCW)
                .setContent(req).create();


        if(!DistributeParam.BINDMOBILENUM_DISTRIBUTE){
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }


    /**
     * 请求 更改手机号 返回结果
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
            C_0x8034.Resp.ContentBean content = resp.getContent();
            C_0x8034.Req.ContentBean errcontent = content.getErrcontent();
            content.setMobile(errcontent.getMobile());
            content.setUserid(errcontent.getUserid());

            SLog.e(TAG, MSG_DESC + " 失败");
        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onBindMobileNumResult(resp);
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
             * mobile :
             */

            private String userid;
            private String mobile;

            public ContentBean() {
            }

            public ContentBean(String userid, String mobile) {
                this.userid = userid;
                this.mobile = mobile;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "userid='" + userid + '\'' +
                        ", mobile='" + mobile + '\'' +
                        '}';
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
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
             * mobile :
             */

            private String userid;
            private String mobile;

            public ContentBean() {
            }

            public ContentBean(Req.ContentBean errcontent, String userid, String mobile) {
                this.errcontent = errcontent;
                this.userid = userid;
                this.mobile = mobile;
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", userid='" + userid + '\'' +
                        ", mobile='" + mobile + '\'' +
                        '}';
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }
        }
    }


}
