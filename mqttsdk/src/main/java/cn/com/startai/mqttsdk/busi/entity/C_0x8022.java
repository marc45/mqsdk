package cn.com.startai.mqttsdk.busi.entity;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 检验验证码
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class C_0x8022 {

    private static final String TAG = C_0x8022.class.getSimpleName();

    /**
     * 检验验证码
     *
     * @param mobile       手机号
     * @param identifyCode 验证码
     * @param type         类型1表示用户登录2表示修改登录密码3表示用户注册
     * @param listener
     */
    public static void m_0x8022_req(String mobile, String identifyCode, int type, IOnCallListener listener) {

        MqttPublishRequest x8022_req_msg = MqttPublishRequestCreator.create_0x8022_req_msg(mobile, identifyCode, type);
        if (x8022_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8022_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8022_req_msg, listener);

    }


    /**
     * 检验验证码
     *
     * @param miof
     */
    public static void m_0x8022_resp(String miof) {
        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, "返回数据格式错误");
            return;
        }
        if (resp.getResult() == 1) {


            SLog.e(TAG, "检验验证码成功");
        } else {

            SLog.e(TAG, "检验验证码失败");
        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onCheckIdentifyResult(resp);
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

            private String mobile;
            private String identifyCode;
            private int type;//1表示用户登录2表示修改登录密码3表示用户注册

            @Override
            public String toString() {
                return "ContentBean{" +
                        "mobile='" + mobile + '\'' +
                        ", identifyCode='" + identifyCode + '\'' +
                        ", type=" + type +
                        '}';
            }

            public ContentBean() {
            }

            public ContentBean(String mobile, String identifyCode, int type) {
                this.mobile = mobile;
                this.identifyCode = identifyCode;
                this.type = type;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getIdentifyCode() {
                return identifyCode;
            }

            public void setIdentifyCode(String identifyCode) {
                this.identifyCode = identifyCode;
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

            /**
             * mobile : 13332965499
             * identifyCode : 123456
             * type : 1
             */


            private String mobile;
            private String identifyCode;
            private int type;
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", mobile='" + mobile + '\'' +
                        ", identifyCode='" + identifyCode + '\'' +
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

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getIdentifyCode() {
                return identifyCode;
            }

            public void setIdentifyCode(String identifyCode) {
                this.identifyCode = identifyCode;
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
