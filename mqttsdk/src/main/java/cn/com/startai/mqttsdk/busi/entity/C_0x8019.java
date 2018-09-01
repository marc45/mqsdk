package cn.com.startai.mqttsdk.busi.entity;

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
 * 更新设备信息
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */
public class C_0x8019 {
    //TODO:
    private static final String TAG = C_0x8019.class.getSimpleName();

    /**
     * 更新设备信息
     *
     * @param listener
     */
    public static void m_0x8019req(C_0x8019.Req.ContentBean contentBean, IOnCallListener listener) {

        MqttPublishRequest x8019_req_msg = MqttPublishRequestCreator.create_0x8019_req_msg(contentBean);
        if (x8019_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8019_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8019_req_msg, listener);

    }


    /**
     * 更新设备信息 返回
     *
     * @param miof
     */
    public static void m_0x8019_resp( String miof) {

        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, "返回数据格式错误");
            return;
        }
        if (resp.getResult() == 1) {


            SLog.e(TAG, "更新设备信息成功");
        } else {

            SLog.e(TAG, "更新设备信息失败");
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
