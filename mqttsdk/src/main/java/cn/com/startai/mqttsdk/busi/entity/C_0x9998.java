package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.localbusi.UserBusi;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.listener.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * Created by Robin on 2018/7/16.
 * qq: 419109715 彬影
 */

public class C_0x9998 {

    private static String TAG = C_0x9998.class.getSimpleName();
    public static final String MSGTYPE = "0x9998";
    /**
     * 自己上报 自己的连接状态
     *
     * @param listener
     */
    public static void m_0x9998_req(IOnCallListener listener) {

        MqttPublishRequest x9998_req_msg = MqttPublishRequestCreator.create_0x9998_req_msg();

        if (x9998_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x9998_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartAI.getInstance().send(x9998_req_msg, listener);

    }

    /**
     * 处理对端的 终端连接事件
     */
    public static void m_resp(String miof) {

        Resp resp = SJsonUtils.fromJson(miof, Resp.class);

        if (resp == null) {
            SLog.e(TAG, "接收的数据格式错误");
            return;
        }

        String sn = resp.getContent().getSn();
        if (TextUtils.isEmpty(sn)) {
            String clientid = resp.getContent().getClientid();
            SLog.e(TAG, "设备上线 sn = " + sn + " userid = " + clientid);
            sn = clientid;
        }
        C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
        String userid = "";
        if (userBean != null) {
            userid = userBean.getUserid();
        }
        SLog.e(TAG, "设备上线 " + sn);


        StartAI.getInstance().getPersisitnet().getEventDispatcher().onDeviceConnectStatusChanged(userid, 1, sn);
    }


    public static class Req {

        private ContentBean content;

        public static class ContentBean {
            private String sn;
            private String ipaddress;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "sn='" + sn + '\'' +
                        ", ipaddress='" + ipaddress + '\'' +
                        '}';
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getIpaddress() {
                return ipaddress;
            }

            public void setIpaddress(String ipaddress) {
                this.ipaddress = ipaddress;
            }


        }


    }


    public static class Resp extends BaseMessage {
        private ContentBean content;

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean {
            private String sn;
            private String ipaddress;

            private int protocol;
            private String clientid;
            private boolean clean_sess;
            private int connack;
            private String username;
            private int ts;


            @Override
            public String toString() {
                return "ContentBean{" +
                        "sn='" + sn + '\'' +
                        ", ipaddress='" + ipaddress + '\'' +
                        ", protocol=" + protocol +
                        ", clientid='" + clientid + '\'' +
                        ", clean_sess=" + clean_sess +
                        ", connack=" + connack +
                        ", username='" + username + '\'' +
                        ", ts=" + ts +
                        '}';
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getIpaddress() {
                return ipaddress;
            }

            public void setIpaddress(String ipaddress) {
                this.ipaddress = ipaddress;
            }

            public int getProtocol() {
                return protocol;
            }

            public void setProtocol(int protocol) {
                this.protocol = protocol;
            }

            public String getClientid() {
                return clientid;
            }

            public void setClientid(String clientid) {
                this.clientid = clientid;
            }

            public boolean isClean_sess() {
                return clean_sess;
            }

            public void setClean_sess(boolean clean_sess) {
                this.clean_sess = clean_sess;
            }

            public int getConnack() {
                return connack;
            }

            public void setConnack(int connack) {
                this.connack = connack;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public int getTs() {
                return ts;
            }

            public void setTs(int ts) {
                this.ts = ts;
            }
        }

    }


}
