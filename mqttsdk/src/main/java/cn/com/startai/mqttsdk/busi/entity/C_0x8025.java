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
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 修改密码
 * Created by Robin on 2018/8/3.
 * qq: 419109715 彬影
 */

public class C_0x8025 implements Serializable {

    private static final String TAG = C_0x8025.class.getSimpleName();

    /**
     * 请求修改登录 密码
     *
     * @param listener
     */
    public static void m_0x8025_req(String userid, String oldPwd, String newPwd, IOnCallListener listener) {

        MqttPublishRequest x8025_req_msg = MqttPublishRequestCreator.create_0x8025_req_msg(userid, oldPwd, newPwd);
        if (x8025_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8025_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8025_req_msg, listener);

    }


    /**
     * 请求修改密码返回结果
     *
     * @param miof
     */
    public static void m_0x8025_resp(String miof) {

        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, "返回数据格式错误");
            return;
        }
        if (resp.getResult() == 1) {
            SLog.e(TAG, "修改密码成功");
        } else {
            SLog.e(TAG, "修改密码失败");
        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onUpdateUserPwdResult(resp);

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


            private String userid = null; //用户id
            private String oldPwd = null; //老密码
            private String newPwd = null; //新密码

            @Override
            public String toString() {
                return "ContentBean{" +
                        "userid='" + userid + '\'' +
                        ", oldPwd='" + oldPwd + '\'' +
                        ", newPwd='" + newPwd + '\'' +
                        '}';
            }

            public ContentBean(String userid, String oldPwd, String newPwd) {
                this.userid = userid;
                this.oldPwd = oldPwd;
                this.newPwd = newPwd;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getOldPwd() {
                return oldPwd;
            }

            public void setOldPwd(String oldPwd) {
                this.oldPwd = oldPwd;
            }

            public String getNewPwd() {
                return newPwd;
            }

            public void setNewPwd(String newPwd) {
                this.newPwd = newPwd;
            }
        }


    }


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


            private String userid = null; //用户id
            private String oldPwd = null; //老密码
            private String newPwd = null; //新密码
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", userid='" + userid + '\'' +
                        ", oldPwd='" + oldPwd + '\'' +
                        ", newPwd='" + newPwd + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public ContentBean(String userid, String oldPwd, String newPwd) {
                this.userid = userid;
                this.oldPwd = oldPwd;
                this.newPwd = newPwd;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getOldPwd() {
                return oldPwd;
            }

            public void setOldPwd(String oldPwd) {
                this.oldPwd = oldPwd;
            }

            public String getNewPwd() {
                return newPwd;
            }

            public void setNewPwd(String newPwd) {
                this.newPwd = newPwd;
            }
        }
    }


}
