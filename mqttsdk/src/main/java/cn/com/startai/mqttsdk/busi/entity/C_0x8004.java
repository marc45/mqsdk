package cn.com.startai.mqttsdk.busi.entity;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.busi.ErrorMiofMsg;
import cn.com.startai.mqttsdk.control.SDBmanager;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.control.entity.TopicBean;
import cn.com.startai.mqttsdk.control.entity.UserBean;
import cn.com.startai.mqttsdk.event.PersistentEventDispatcher;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.localbusi.UserBusi;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * Created by Robin on 2018/5/10.
 * qq: 419109715 彬影
 */

public class C_0x8004 {

    private static String TAG = C_0x8004.class.getSimpleName();

    /**
     * 删除好友或设备
     *
     * @param bebindid 对端的userid或sn
     */
    public static void m_0x8004_req(String userid, String bebindid, IOnCallListener listener) {

        MqttPublishRequest x8004_req_msg = MqttPublishRequestCreator.create_0x8004_req_msg(userid, bebindid);
        if (x8004_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8004_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8004_req_msg, listener);

    }


    /**
     * 解绑结果
     *
     * @param miof
     */
    public static void m_0x8004_resp(String miof) {

        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, "返回数据格式错误");
            return;
        }
        int result = resp.getResult();

        C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
        String id = "";
        if (userBean != null) {
            id = userBean.getUserid();
        } else {
            id = MqttConfigure.getSn(StartAI.getContext());
        }

        if (result == 1) {
            SLog.e(TAG, "解绑成功");
        } else {
            SLog.e(TAG, "解绑失败 " + resp.getContent().getErrmsg());
        }

        String friendId = "";
        if (resp.getContent().getBeunbindingid().equals(id)) {
            friendId = resp.getContent().getUnbindingid();
        } else {
            friendId = resp.getContent().getBeunbindingid();
        }

        StartAI.getInstance().getPersisitnet().getEventDispatcher().onUnBindResult(resp, id, friendId);

        if (userBean != null) {
            TopicBean topicBean = new TopicBean(TopicConsts.getSubFriendTopic(friendId), "remove", "", userBean.getUserid());
            SDBmanager.getInstance().addOrUpdateTopic(topicBean);
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
