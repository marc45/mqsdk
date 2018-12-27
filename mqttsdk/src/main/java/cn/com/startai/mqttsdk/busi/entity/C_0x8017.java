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
 * 注册
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class C_0x8017 {

    private static final String TAG = C_0x8017.class.getSimpleName();
    public static String MSG_DESC = "注册 ";
    public static String MSGTYPE = "0x8017";
    public static String MSGCW = "0x07";

    /**
     * 注册
     *
     * @param uName    手机或邮箱
     * @param pwd      密码
     * @param listener
     */
    public static void m_0x8017_req(String uName, String pwd, IOnCallListener listener) {

        MqttPublishRequest x8017_req_msg = MqttPublishRequestCreator.create_0x8017_req_msg(uName, pwd);
        if (x8017_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8017_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8017_req_msg, listener);

    }


    /**
     * 用户注册返回
     *
     * @param miof
     */
    public static void m_0x8017_resp(String miof) {


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

            SLog.e(TAG, "用户注册失败");
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
