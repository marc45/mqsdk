package cn.com.startai.mqttsdk.busi.entity;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.DistributeParam;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.control.SDBmanager;
import cn.com.startai.mqttsdk.control.SPController;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.listener.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

import static cn.com.startai.mqttsdk.StartAI.TAG;

/**
 * 注销激活
 * Created by Robin on 2018/5/10.
 * qq: 419109715 彬影
 */

public class C_0x8003 {
    public static String MSG_DESC = "注销激活 ";
    public static final String MSGTYPE = "0x8003";
    public static String MSGCW = "0x07";

    /**
     * 注销激活
     */
    public static void m_0x8003_req(IOnCallListener listener) {

        MqttPublishRequest x8003_req_msg = create_0x8003_req_msg();
        if (x8003_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8003_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8003_req_msg, listener);

    }
    /**
     * 组添注销激活包
     *
     * @return
     */
    public static MqttPublishRequest create_0x8003_req_msg() {


        String sn = MqttConfigure.getSn(StartAI.getContext());

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8003")
                .setMsgcw("0x07")
                .setContent(new C_0x8003.Req.ContentBean(sn)).create();
        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }
        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }

    /**
     * 注销激活 消息
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

            //是自己的注销激活消息
            if (MqttConfigure.getSn(StartAI.getContext()).equalsIgnoreCase(resp.content.getId())) {
                SLog.d(TAG, "注销成功");
                SPController.clearAllSp();
                SDBmanager.getInstance().deleteAllDB();

                StartaiMqttPersistent.getInstance().disConnect(false);
                StartAI.getInstance().getPersisitnet().getEventDispatcher().onUnActiviteResult(resp);
            }

        } else {
            SLog.e(TAG, "注销失败");
            StartAI.getInstance().getPersisitnet().getEventDispatcher().onUnActiviteResult(resp);
        }

    }

    /**
     * 注销激活 请求json 对应实体类
     */
    public static class Req {

        private ContentBean content;

        public static class ContentBean {
            public ContentBean(String id) {
                this.id = id;
            }

            String id;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "id='" + id + '\'' +
                        '}';
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

    }

    /**
     * 注销激活 请求返回 json 对应实体类
     */
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

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends BaseContentBean {
            String id;
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", id='" + id + '\'' +
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
        }

    }


}
