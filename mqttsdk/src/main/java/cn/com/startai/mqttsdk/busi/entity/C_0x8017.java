package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

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
import cn.com.startai.mqttsdk.utils.SRegexUtil;

import static cn.com.startai.mqttsdk.StartAI.TAG;

/**
 * 注册
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class C_0x8017 {

    public static String MSG_DESC = "注册 ";
    public static final String MSGTYPE = "0x8017";
    public static String MSGCW = "0x07";

    public static final int TYPE_EMAIL_ACTIVATE_EMAIL = 1; //发送激活邮件注册
    public static final int TYPE_MOBILE_AFTER_CHECK_CODE = 2; //检验手机验证码注册
    public static final int TYPE_MOBILE_CODE_FAST_LOGIN = 3; //暂时用不上
    public static final int TYPE_EMAIL_AFTER_CHECK_CODE = 4;//检验邮箱验证码注册

    /**
     * 注册
     *
     * @param uName    手机或邮箱
     * @param pwd      密码
     * @param listener
     */
    public static void m_0x8017_req(String uName, String pwd, IOnCallListener listener) {

        MqttPublishRequest x8017_req_msg = create_0x8017_req_msg(uName, pwd);
        if (x8017_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8017_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8017_req_msg, listener);

    }

    /**
     * 注册
     *
     * @param req
     * @param listener
     */
    public static void req(Req.ContentBean req, IOnCallListener listener) {

        MqttPublishRequest x8017_req_msg = create_req_msg(req);
        if (x8017_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8017_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8017_req_msg, listener);
    }

    /**
     * 组注册包
     *
     * @param req
     * @return
     */
    public static MqttPublishRequest create_req_msg(Req.ContentBean req) {
        if (req == null) {
            SLog.e(TAG, "req is empty ");
            return null;
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(MSGTYPE)
                .setMsgcw(MSGCW)
                .setFromid(MqttConfigure.getSn(StartAI.getContext()))
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
     * 组注册包
     *
     * @param uName
     * @param pwd
     * @return
     */
    public static MqttPublishRequest create_0x8017_req_msg(String uName, String pwd) {

        int type = 0;
        if (SRegexUtil.isEmail(uName)) {
            type = TYPE_EMAIL_ACTIVATE_EMAIL;
        } else if (SRegexUtil.isMobileSimple(uName)) {
            type = TYPE_MOBILE_AFTER_CHECK_CODE;
        }
        if (type == 0) {
            SLog.e(TAG, "参数非法 类型不对");
            return null;
        }
        if (TextUtils.isEmpty(pwd)) {
            SLog.e(TAG, "参数非法 密码为空");
            return null;
        }
        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8017")
                .setMsgcw("0x07")
                .setFromid(MqttConfigure.getSn(StartAI.getContext()))
                .setContent(new C_0x8017.Req.ContentBean(uName, pwd, type)).create();


        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }


        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }

    /**
     * 用户注册返回
     *
     * @param miof
     */
    public static void m_resp(String miof) {


        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, "返回数据格式错误");
            return;
        }
        if (resp.getResult() == 1) {
            SLog.e(TAG, "用户注册成功");
        } else {

            Resp.ContentBean content = resp.getContent();
            Req.ContentBean errcontent = content.getErrcontent();
            content.setType(errcontent.getType());
            content.setUname(errcontent.getUname());
            content.setPwd(errcontent.getPwd());

            SLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onRegisterResult(resp);

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

            private String uname;
            private String pwd;
            private int type;


            public ContentBean() {
            }

            public ContentBean(String uname, String pwd, int type) {
                this.uname = uname;
                this.pwd = pwd;
                this.type = type;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "uname='" + uname + '\'' +
                        ", pwd='" + pwd + '\'' +
                        ", type=" + type +
                        '}';
            }

            public String getUname() {
                return uname;
            }

            public void setUname(String uname) {
                this.uname = uname;
            }

            public String getPwd() {
                return pwd;
            }

            public void setPwd(String pwd) {
                this.pwd = pwd;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }


    }

    public static class Resp extends BaseMessage {

        private ContentBean content;

        @Override
        public String toString() {
            return "Resp{" +
                    "msgcw='" + msgcw + '\'' +
                    ", msgtype='" + msgtype + '\'' +
                    ", fromid='" + fromid + '\'' +
                    ", toid='" + toid + '\'' +
                    ", domain='" + domain + '\'' +
                    ", appid='" + appid + '\'' +
                    ", ts=" + ts +
                    ", msgid='" + msgid + '\'' +
                    ", m_ver='" + m_ver + '\'' +
                    ", result=" + result +
                    ", content=" + content +
                    '}';
        }

        public Resp(ContentBean content) {
            this.content = content;
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends BaseContentBean {

            private String uname;
            private String pwd;
            private int type;
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", uname='" + uname + '\'' +
                        ", pwd='" + pwd + '\'' +
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

            public ContentBean(String uname, String pwd, int type) {
                this.uname = uname;
                this.pwd = pwd;
                this.type = type;
            }

            public String getUname() {
                return uname;
            }

            public void setUname(String uname) {
                this.uname = uname;
            }

            public String getPwd() {
                return pwd;
            }

            public void setPwd(String pwd) {
                this.pwd = pwd;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }


    }


}
