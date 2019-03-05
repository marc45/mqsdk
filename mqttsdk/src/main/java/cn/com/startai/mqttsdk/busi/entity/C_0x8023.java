package cn.com.startai.mqttsdk.busi.entity;

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

/**
 * 请求发送邮件
 * Created by Robin on 2018/8/4.
 * qq: 419109715 彬影
 */

public class C_0x8023 {

    public static String MSG_DESC = "请求发送邮件 ";
    private static final String TAG = C_0x8023.class.getSimpleName();
    public static final String MSGTYPE = "0x8023";
    public static String MSGCW = "0x07";


    /*
    1 为重新发送激活邮件
2 为发送忘记密码邮件
6 为发送注册验证码邮件
7 为发送重置密码验证码邮件
8 为发送更换/添加绑定邮箱验证码邮件

     */

    public static final int TYPE_LINK_TO_RE_ACTIVATE= 1;
    public static final int TYPE_LINK_TO_RESET_PWD= 2;
    public static final int TYPE_CODE_TO_REGISTER = 6;
    public static final int TYPE_CODE_TO_RESET_PWD = 7;
    public static final int TYPE_CODE_TO_BIND_EMAIL = 8;

    /**
     * 请求发送邮件
     *
     * @param email    邮箱
     * @param type     类型 1 为重新发送激活邮件 2 为发送忘记密码邮件
     * @param listener
     */
    public static void m_0x8023_req(String email, int type, IOnCallListener listener) {

        MqttPublishRequest x8023_req_msg = create_0x8023_req_msg(email, type);
        if (x8023_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8023_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8023_req_msg, listener);

    }

    /**
     * 请求发送邮件
     *
     * @param req
     * @param listener
     */
    public static void req(Req.ContentBean req, IOnCallListener listener) {
        if (req != null) {
            m_0x8023_req(req.getEmail(), req.getType(), listener);
        }
    }

    /**
     * 发送邮件
     *
     * @param email 邮箱
     * @param type  1 为重新发送激活邮件 2 为发送忘记密码邮件
     * @return
     */
    public static MqttPublishRequest create_0x8023_req_msg(String email, int type) {

        boolean isEmail = SRegexUtil.isEmail(email);
        if (!isEmail  ) {
            SLog.e(TAG, "参数非法 email 格式不正确 ");
            return null;
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8023")
                .setMsgcw("0x07")
                .setFromid(MqttConfigure.getSn(StartAI.getContext()))
                .setContent(new C_0x8023.Req.ContentBean(email, type)).create();

        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }

    /**
     * 请求发送邮件结果
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


            SLog.e(TAG, "请求发送邮件成功");
        } else {
            Resp.ContentBean content = resp.getContent();
            Req.ContentBean errcontent = content.getErrcontent();
            content.setType(errcontent.getType());
            content.setEmail(errcontent.getEmail());

            SLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onSendEmailResult(resp);
    }

    /**
     * 请求
     */
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

            private String email;
            private int type;//1 为重新发送激活邮件 2 为发送忘记密码邮件

            public ContentBean() {
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "email='" + email + '\'' +
                        ", type=" + type +
                        '}';
            }

            public ContentBean(String email, int type) {
                this.email = email;
                this.type = type;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }


    }


    /**
     * 返回
     */
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

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends BaseContentBean {

            private String email;
            private int type;//1 为重新发送激活邮件 2 为发送忘记密码邮件
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", email='" + email + '\'' +
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

            public ContentBean(String email, int type) {
                this.email = email;
                this.type = type;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
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
