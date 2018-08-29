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
     * @param result
     * @param resp
     * @param errorMiofMsg
     */
    public static void m_0x8022_resp(int result, Resp resp, ErrorMiofMsg errorMiofMsg) {
        if (result == 1 && resp != null) {
            SLog.d(TAG, "检验验证码成功");
            StartAI.getInstance().getPersisitnet().getEventDispatcher().onCheckIdentifyResult(result, "", "");
        } else if (result == 0 && errorMiofMsg != null) {
            SLog.d(TAG, "检验验证码失败");
            StartAI.getInstance().getPersisitnet().getEventDispatcher().onCheckIdentifyResult(result, errorMiofMsg.getContent().getErrcode(), errorMiofMsg.getContent().getErrmsg());
        } else {
            SLog.e(TAG, "返回数据格式错误");
        }
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
     * 登录 返回
     */
    public static class Resp extends BaseMessage {

        private ContentBean content;

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean {

            private String userid;//用户id
            private String token; //用户token
            private long expire_in; //token时效

            @Override
            public String toString() {
                return "ContentBean{" +
                        "userid='" + userid + '\'' +
                        ", token='" + token + '\'' +
                        ", expire_in=" + expire_in +
                        '}';
            }

            public ContentBean() {
            }

            public ContentBean(String userid, String token, long expire_in) {
                this.userid = userid;
                this.token = token;
                this.expire_in = expire_in;
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

            public long getExpire_in() {
                return expire_in;
            }

            public void setExpire_in(long expire_in) {
                this.expire_in = expire_in;
            }
        }
    }


}
