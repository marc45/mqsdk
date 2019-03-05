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
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.listener.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 重置密码
 * <p>
 * Created by Robin on 2018/8/3.
 * qq: 419109715 彬影
 */

public class C_0x8026 implements Serializable {

    private static final String TAG = C_0x8026.class.getSimpleName();
    public static final String MSGTYPE = "0x8026";
    public static String MSGCW = "0x07";
    public static String MSG_DESC = "重置密码 ";


    public static void req(Req.ContentBean req,IOnCallListener listener){
        if(req== null){
            SLog.d(TAG,"req is empty");
            return;
        }

        m_0x8026_req(req.getAccount(),req.getPwd(),listener);
    }

    /**
     * 重置登录密码
     *
     * @param listener
     */
    public static void m_0x8026_req(String account, String newPwd, IOnCallListener listener) {

        MqttPublishRequest x8026_req_msg = create_0x8026_req_msg(account, newPwd);
        if (x8026_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8026_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8026_req_msg, listener);

    }

    /**
     * 组 重置登录密码 数据包
     *
     * @return
     */
    public static MqttPublishRequest create_0x8026_req_msg(String account, String pwd) {


        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {

            SLog.e(TAG, "参数非法 account pwd 不能为空");
            return null;
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(MSGTYPE)
                .setMsgcw(MSGCW)
                .setFromid(MqttConfigure.getSn(StartAI.getContext()) + "/" + MqttConfigure.appid)
                .setContent(new C_0x8026.Req.ContentBean(account, pwd)).create();

        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }

    /**
     * 重置登录密码 结果返回
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
            SLog.e(TAG, "重置登录密码 成功");


        } else {
            Resp.ContentBean content = resp.getContent();
            Req.ContentBean errcontent = content.getErrcontent();
            content.setPwd(errcontent.getPwd());
            content.setAccount(errcontent.getAccount());

            SLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onResetLoginPwdResult(resp);

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


            private String pwd = null; //新密码

            private String account = null;




            @Override
            public String toString() {
                return "ContentBean{" +
                        ", pwd='" + pwd + '\'' +
                        ", account='" + account + '\'' +
                        '}';
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public ContentBean() {
            }

            public ContentBean(String account, String pwd) {
                this.account = account;
                this.pwd = pwd;
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


            private String pwd = null; //新密码

            private String account = null;


            private Req.ContentBean errcontent = null;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", pwd='" + pwd + '\'' +
                        ", account='" + account + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public ContentBean() {
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
