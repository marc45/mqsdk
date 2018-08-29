package cn.com.startai.mqttsdk.busi.entity;

import java.io.Serializable;

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
 * 手机重置密码
 * <p>
 * Created by Robin on 2018/8/3.
 * qq: 419109715 彬影
 */

public class C_0x8026 implements Serializable {

    private static final String TAG = C_0x8026.class.getSimpleName();

    /**
     * 手机重置登录密码
     *
     * @param listener
     */
    public static void m_0x8026_req(String oldPwd, String newPwd, IOnCallListener listener) {

        MqttPublishRequest x8026_req_msg = MqttPublishRequestCreator.create_0x8026_req_msg(oldPwd, newPwd);
        if (x8026_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8026_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8026_req_msg, listener);

    }


    /**
     * 手机重置登录密码 结果返回
     *
     * @param result
     * @param resp
     * @param errorMiofMsg
     */
    public static void m_0x8026_resp(int result, Resp resp, ErrorMiofMsg errorMiofMsg) {

        if (result == 1 && resp != null) {
            SLog.d(TAG, "手机重置登录密码 成功");
            StartAI.getInstance().getPersisitnet().getEventDispatcher().onResetMobileLoginPwdResult(result, "", "", resp.getContent());
        } else if (result == 0 && errorMiofMsg != null) {
            SLog.d(TAG, "手机重置登录密码 失败");
            StartAI.getInstance().getPersisitnet().getEventDispatcher().onResetMobileLoginPwdResult(result, errorMiofMsg.getContent().getErrcode(), errorMiofMsg.getContent().getErrmsg(), null);
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


            private String mobile = null; //手机号
            private String pwd = null; //新密码

            public ContentBean() {
            }

            public ContentBean(String mobile, String pwd) {
                this.mobile = mobile;
                this.pwd = pwd;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "mobile='" + mobile + '\'' +
                        ", pwd='" + pwd + '\'' +
                        '}';
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getPwd() {
                return pwd;
            }

            public void setPwd(String pwd) {
                this.pwd = pwd;
            }
        }


    }


    /**
     * 修改密码
     */
    public static class Resp extends BaseMessage implements Serializable {

        private ContentBean content;

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean {


            private String mobile = null; //手机号
            private String pwd = null; //新密码

            public ContentBean() {
            }

            public ContentBean(String mobile, String pwd) {
                this.mobile = mobile;
                this.pwd = pwd;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "mobile='" + mobile + '\'' +
                        ", pwd='" + pwd + '\'' +
                        '}';
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getPwd() {
                return pwd;
            }

            public void setPwd(String pwd) {
                this.pwd = pwd;
            }
        }
    }


}
