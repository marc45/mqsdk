package cn.com.startai.mqttsdk.busi.entity;

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

import static cn.com.startai.mqttsdk.StartAI.TAG;

/**
 * 更新设备信息
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */
public class C_0x8019 {
    //TODO:
    public static String MSG_DESC = "更新设备信息 ";
    public static final String MSGTYPE = "0x8019";
    public static String MSGCW = "0x07";

    /**
     * 更新设备信息
     *
     * @param listener
     */
    public static void m_0x8019req(C_0x8019.Req.ContentBean contentBean, IOnCallListener listener) {

        MqttPublishRequest x8019_req_msg = create_0x8019_req_msg(contentBean);
        if (x8019_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8019_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8019_req_msg, listener);

    }

    /**
     * 组 更新用户信息数据包
     */
    public static MqttPublishRequest create_0x8019_req_msg(C_0x8019.Req.ContentBean contentBean) {

        if (contentBean == null) {
            SLog.e(TAG, "参数非法 参数为空");
            return null;
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(MSGTYPE)
                .setMsgcw(MSGCW)
                .setContent(contentBean).create();

        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;


        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }


    /**
     * 更新设备信息 返回
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


            SLog.e(TAG, "更新设备信息成功");
        } else {

            SLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }
//            StartAI.getInstance().getPersisitnet().getEventDispatcher().onGetIdentifyResult(result, errorMiofMsg.getContentBean().getErrcode(), errorMiofMsg.getContentBean().getErrmsg());

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

            private String sn;
            private StatusParam statusParam;
            private BusinessParam businessParam;

            public ContentBean(String sn, BusinessParam businessParam) {
                this.sn = sn;
                this.businessParam = businessParam;
            }

            public ContentBean(String sn, StatusParam statusParam) {
                this.sn = sn;
                this.statusParam = statusParam;
            }

            public ContentBean(BusinessParam businessParam) {
                this.businessParam = businessParam;
            }

            public ContentBean(StatusParam statusParam) {
                this.statusParam = statusParam;
            }

            public ContentBean() {
            }

            public ContentBean(StatusParam statusParam, BusinessParam businessParam) {
                this.statusParam = statusParam;
                this.businessParam = businessParam;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "statusParam=" + statusParam +
                        ", businessParam=" + businessParam +
                        '}';
            }

            public StatusParam getStatusParam() {
                return statusParam;
            }

            public void setStatusParam(StatusParam statusParam) {
                this.statusParam = statusParam;
            }

            public BusinessParam getBusinessParam() {
                return businessParam;
            }

            public void setBusinessParam(BusinessParam businessParam) {
                this.businessParam = businessParam;
            }

            public static class StatusParam {
                private String ip;

                public StatusParam(String ip) {
                    this.ip = ip;
                }

                @Override
                public String toString() {
                    return "StatusParam{" +
                            "ip='" + ip + '\'' +
                            '}';
                }

                public String getIp() {
                    return ip;
                }

                public void setIp(String ip) {
                    this.ip = ip;
                }
            }

            public static class BusinessParam {

            }

        }


    }


    /**
     * 返回
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

            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }
        }
    }


}
