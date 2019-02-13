package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.DistributeParam;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.busi.ErrorMiofMsg;
import cn.com.startai.mqttsdk.control.TopicConsts;
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
 * 修改备注名
 * Created by Robin on 2018/7/19.
 * qq: 419109715 彬影
 */

public class C_0x8015 {
    public static final String MSGTYPE = "0x8015";

    private static final String TAG = C_0x8015.class.getSimpleName();
    public static String MSG_DESC = "修改备注名 ";
    public static String MSGCW = "0x07";

    /**
     * 修改备注名
     *
     * @param fid      对端 的userid|sn
     * @param remark   备注名
     * @param listener
     */
    public static void m_0x8015_req(String userid, String fid, String remark, IOnCallListener listener) {

        MqttPublishRequest x8015_req_msg = create_0x8015_req_msg(userid, fid, remark);
        if (x8015_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8015_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8015_req_msg, listener);


    }
    /**
     * 组 修改备注名数据包
     *
     * @return
     */
    public static MqttPublishRequest create_0x8015_req_msg(String uid, String fid, String remark) {


        String userid = uid;
        if (TextUtils.isEmpty(userid)) {

            C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
            if (userBean != null) {
                userid = userBean.getUserid();
            }
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(C_0x8015.MSGTYPE)
                .setMsgcw("0x07")
                .setContent(new C_0x8015.Req.ContentBean(userid, fid, remark)).create();

        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }

    /**
     * 修改备注名
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

            SLog.e(TAG, "修改备注名成功");
        } else {
            Resp.ContentBean content = resp.getContent();
            Req.ContentBean errcontent = content.getErrcontent();
            content.setId(errcontent.getId());
            content.setFid(errcontent.getFid());
            content.setRemark(errcontent.getRemark());

            SLog.e(TAG, "修改备注名失败");
        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onUpdateRemarkResult(resp);

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


            /**
             * id :
             * fid :
             * remark :
             */

            private String id;
            private String fid;
            private String remark;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "id='" + id + '\'' +
                        ", fid='" + fid + '\'' +
                        ", remark='" + remark + '\'' +
                        '}';
            }

            public ContentBean(String id, String fid, String remark) {
                this.id = id;
                this.fid = fid;
                this.remark = remark;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getFid() {
                return fid;
            }

            public void setFid(String fid) {
                this.fid = fid;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
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
             * id :
             * fid :
             * remark :
             */

            private String id;
            private String fid;
            private String remark;
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", id='" + id + '\'' +
                        ", fid='" + fid + '\'' +
                        ", remark='" + remark + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getFid() {
                return fid;
            }

            public void setFid(String fid) {
                this.fid = fid;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }
        }


    }


}
