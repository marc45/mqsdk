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
     * @param result
     * @param resp
     * @param errorMiofMsg
     */
    public static void m_0x8019_resp(int result, Resp resp, ErrorMiofMsg errorMiofMsg) {

        if (result == 1 && resp != null) {
            SLog.d(TAG, "更新设备信息成功");
//            StartAI.getInstance().getPersisitnet().getEventDispatcher().onGetIdentifyResult(result, "", "");
        } else if (result == 0 && errorMiofMsg != null) {
            SLog.d(TAG, "更新设备信息失败");
//            StartAI.getInstance().getPersisitnet().getEventDispatcher().onGetIdentifyResult(result, errorMiofMsg.getContent().getErrcode(), errorMiofMsg.getContent().getErrmsg());
        } else {
            SLog.e(TAG, "返回数据格式错误");
        }

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
     * 登录 返回
     */
    public static class Resp extends BaseMessage {

        private ContentBean content;

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean {


        }
    }


}
