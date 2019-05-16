package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.DistributeParam;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.control.SDBmanager;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.control.entity.TopicBean;
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
 * 删除好友或设备
 * Created by Robin on 2018/5/10.
 * qq: 419109715 彬影
 */

public class C_0x8004 {

    public static String MSG_DESC = "删除好友或设备 ";
    public static final String MSGTYPE = "0x8004";
    public static String MSGCW = "0x07";

    /**
     * 删除好友或设备
     *
     * @param bebindid 对端的userid或sn
     */
    public static void m_0x8004_req(String userid, String bebindid, IOnCallListener listener) {

        MqttPublishRequest x8004_req_msg = create_0x8004_req_msg(userid, bebindid);
        if (x8004_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8004_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8004_req_msg, listener);

    }

    /**
     * 组删除好友包
     *
     * @param beBindingid 设备的sn
     * @return
     */
    public static MqttPublishRequest create_0x8004_req_msg(String userid, String beBindingid) {

        String bindingid = userid;
        if (TextUtils.isEmpty(bindingid)) {
            bindingid = SUserManager.getInstance().getUserId();
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8004")
                .setMsgcw("0x07")
                .setContent(new C_0x8004.Req.ContentBean(bindingid, beBindingid)).create();
        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }
        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }

    /**
     * 解绑结果
     *
     * @param miof
     */
    public static void m_resp(String miof) {

        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, "返回数据格式错误");
            return;
        }
        int result = resp.getResult();

        C_0x8018.Resp.ContentBean userBean = SUserManager.getInstance().getCurrUser();
        String id = "";
        if (userBean != null) {
            id = userBean.getUserid();
        } else {
            id = MqttConfigure.getSn(StartAI.getContext());
        }


        String friendId = "";


        if (result == 1) {
            SLog.e(TAG, "解绑成功");

            if (id.equals(resp.getContent().getBeunbindingid())) {
                friendId = resp.getContent().getUnbindingid();
            } else {
                friendId = resp.getContent().getBeunbindingid();
            }

        } else {

            SLog.e(TAG, "解绑失败 " + resp.getContent().getErrmsg());

            if (id.equals(resp.getContent().getBeunbindingid())) {
                friendId = resp.getContent().getErrcontent().getUnbindingid();
            } else {
                friendId = resp.getContent().getErrcontent().getBeunbindingid();
            }

        }

        StartAI.getInstance().getPersisitnet().getEventDispatcher().onUnBindResult(resp, id, friendId);

        if (userBean != null) {
            TopicBean topicBeanWill = new TopicBean(TopicConsts.getSubFriendWillTopic(friendId), "remove", "", userBean.getUserid());
            SDBmanager.getInstance().addOrUpdateTopic(topicBeanWill);

            TopicBean topicBeanReport = new TopicBean(TopicConsts.getSubFriendReportTopic(friendId), "remove", "", userBean.getUserid());
            SDBmanager.getInstance().addOrUpdateTopic(topicBeanReport);

            StartaiMqttPersistent.getInstance().subFriendReportTopic();

        }

    }


    public static class Req {

        private ContentBean content;

        @Override
        public String toString() {
            return "Req{" +
                    "content=" + content +
                    '}';
        }

        public static class ContentBean {

            private String unbindingid;
            private String beunbindingid;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "unbindingid='" + unbindingid + '\'' +
                        ", beunbindingid='" + beunbindingid + '\'' +
                        '}';
            }

            public ContentBean() {
            }

            public ContentBean(String unbindingid, String beunbindingid) {
                this.unbindingid = unbindingid;
                this.beunbindingid = beunbindingid;
            }

            public String getUnbindingid() {
                return unbindingid;
            }

            public void setUnbindingid(String unbindingid) {
                this.unbindingid = unbindingid;
            }

            public String getBeunbindingid() {
                return beunbindingid;
            }

            public void setBeunbindingid(String beunbindingid) {
                this.beunbindingid = beunbindingid;
            }
        }


    }


    public static class Resp extends BaseMessage {


        private ContentBean content;

        public ContentBean getContent() {
            return content;
        }

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

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends BaseContentBean {


            /**
             * unbindingid :
             * beunbindingid :
             */

            private String unbindingid;
            private String beunbindingid;
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", unbindingid='" + unbindingid + '\'' +
                        ", beunbindingid='" + beunbindingid + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public String getUnbindingid() {
                return unbindingid;
            }

            public void setUnbindingid(String unbindingid) {
                this.unbindingid = unbindingid;
            }

            public String getBeunbindingid() {
                return beunbindingid;
            }

            public void setBeunbindingid(String beunbindingid) {
                this.beunbindingid = beunbindingid;
            }
        }
    }


}
