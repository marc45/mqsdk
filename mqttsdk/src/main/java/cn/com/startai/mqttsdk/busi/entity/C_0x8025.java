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
import cn.com.startai.mqttsdk.localbusi.SUserManager;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.listener.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

import static cn.com.startai.mqttsdk.StartAI.TAG;

/**
 * 修改密码
 * Created by Robin on 2018/8/3.
 * qq: 419109715 彬影
 */

public class C_0x8025 implements Serializable {

    public static String MSG_DESC = "修改密码 ";
    public static final  String MSGTYPE = "0x8025";
    public static String MSGCW = "0x07";

    /**
     * 请求修改登录 密码
     *
     * @param listener
     */
    public static void m_0x8025_req(String userid, String oldPwd, String newPwd, IOnCallListener listener) {

        MqttPublishRequest x8025_req_msg = create_0x8025_req_msg(userid, oldPwd, newPwd);
        if (x8025_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8025_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8025_req_msg, listener);

    }

    /**
     * 组修改密码数据包
     *
     * @return
     */
    public static MqttPublishRequest create_0x8025_req_msg(String uid, String oldPwd, String newPwd) {

        String userid = uid;
        if (TextUtils.isEmpty(userid)) {
            C_0x8018.Resp.ContentBean userBean = SUserManager.getInstance().getCurrUser();
            if (userBean != null && !TextUtils.isEmpty(userBean.getUserid())) {
                userid = userBean.getUserid();
            }
        }

//        if (TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd)) {
//            SLog.e(TAG, "参数非法");
//            return null;
//        }

        if (oldPwd.equals(newPwd)) {
            SLog.e(TAG, "参数非法，新旧密码一致");
            return null;
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8025")
                .setMsgcw("0x07")
                .setContent(new C_0x8025.Req.ContentBean(userid, oldPwd, newPwd)).create();

        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }



    /**
     * 请求修改密码返回结果
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
            SLog.e(TAG, "修改密码成功");
        } else {

            Resp.ContentBean content = resp.getContent();
            Req.ContentBean errcontent = content.getErrcontent();
            content.setNewPwd(errcontent.getNewPwd());
            content.setOldPwd(errcontent.getOldPwd());
            content.setUserid(errcontent.getUserid());

            SLog.e(TAG, MSG_DESC+" 失败 "+resp.getContent().getErrmsg());
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
