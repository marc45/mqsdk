package cn.com.startai.mqttsdk.busi.entity;

import java.io.Serializable;

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
 * 手机重置密码
 * <p>
 * Created by Robin on 2018/8/3.
 * qq: 419109715 彬影
 */

public class C_0x8026 implements Serializable {

    private static final String TAG = C_0x8026.class.getSimpleName();
    public static String MSGTYPE = "0x8026";
    public static String MSGCW = "0x07";
    public static String MSG_DESC = "手机重置密码 ";
    /**
     * 手机重置登录密码
     *
     * @param listener
     */
    public static void m_0x8026_req(String mobile, String newPwd, IOnCallListener listener) {

        MqttPublishRequest x8026_req_msg = MqttPublishRequestCreator.create_0x8026_req_msg(mobile, newPwd);
        if (x8026_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8026_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8026_req_msg, listener);

    }


    /**
     * 手机重置登录密码 结果返回
     * @param miof
     */
    public static void m_0x8026_resp(String miof) {

        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, "返回数据格式错误");
            return;
        }
        if (resp.getResult() == 1) {
            SLog.e(TAG, "手机重置登录密码 成功");
        } else {
            Resp.ContentBean content = resp.getContent();
            Req.ContentBean errcontent = content.getErrcontent();
            content.setMobile(errcontent.getMobile());
            content.setPwd(errcontent.getPwd());

            SLog.e(TAG, "手机重置登录密码 失败");
        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onResetMobileLoginPwdResult(resp);

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
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "mobile='" + mobile + '\'' +
                        ", pwd='" + pwd + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public ContentBean() {
            }

            public ContentBean(String mobile, String pwd) {
                this.mobile = mobile;
                this.pwd = pwd;
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


            private String mobile = null; //手机号
            private String pwd = null; //新密码
            private Req.ContentBean errcontent = null;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", mobile='" + mobile + '\'' +
                        ", pwd='" + pwd + '\'' +
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

            public ContentBean(String mobile, String pwd) {
                this.mobile = mobile;
                this.pwd = pwd;
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
