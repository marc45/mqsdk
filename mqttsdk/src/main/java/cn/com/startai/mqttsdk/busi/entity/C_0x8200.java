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
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 消息透传
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class C_0x8200 {

    private static final String TAG = C_0x8200.class.getSimpleName();


    /**
     * 消息透传
     *
     * @param toid     对端 主题
     * @param content  内容
     * @param listener
     */
    public static void m_0x8200_req(String toid, String content, IOnCallListener listener) {

        MqttPublishRequest x8200_req_msg = MqttPublishRequestCreator.create_0x8200_req_msg(toid, content);
        if (x8200_req_msg != null) {

            StartaiMqttPersistent.getInstance().send(x8200_req_msg, listener);
        }else{
            CallbackManager.callbackMessageSendResult(false, listener, x8200_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
        }

    }

    /**
     * 处理消息透传返回
     */
    public static void m_0x8200_resp(int result, Resp resp, ErrorMiofMsg errorMiofMsg) {

        SLog.e(TAG, "透传消息");
        String passContent = "";
        String errorCode = "";
        String errorMsg = "";
        if (resp != null) {
            passContent = resp.getContent();
        }
        if (errorMiofMsg != null) {
            errorCode = errorMiofMsg.getContent().getErrcode();
            errorMsg = errorMiofMsg.getContent().getErrmsg();
        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onPassthroughResult(result,resp, errorCode, errorMsg, passContent);
    }



    /**
     * 透传请求包
     */
    public static class Req {
        private String content;

        public Req(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


    /**
     * 透传返回包
     */
    public static class Resp extends BaseMessage {

        private String content;

        public Resp(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


}
