package cn.com.startai.mqttsdk.busi.entity;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.busi.ErrorMiofMsg;
import cn.com.startai.mqttsdk.event.PersistentEventDispatcher;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 请求发送邮件
 * Created by Robin on 2018/8/4.
 * qq: 419109715 彬影
 */

public class C_0x8023 {

    private static final String TAG = C_0x8023.class.getSimpleName();

    /**
     * 请求发送邮件
     *
     * @param email    邮箱
     * @param type     类型 1 为重新发送激活邮件 2 为发送忘记密码邮件
     * @param listener
     */
    public static void m_0x8023_req(String email, int type, IOnCallListener listener) {

        MqttPublishRequest x8023_req_msg = MqttPublishRequestCreator.create_0x8023_req_msg(email, type);
        if (x8023_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8023_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8023_req_msg, listener);

    }


    /**
     * 请求发送邮件结果
     *
     * @param miof
     */
    public static void m_0x8023_resp(String miof) {
        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, "返回数据格式错误");
            return;
        }

        if (resp.getResult() == 1) {


            SLog.e(TAG, "请求发送邮件成功");
        } else {

            SLog.e(TAG, "请求发送邮件失败");
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
