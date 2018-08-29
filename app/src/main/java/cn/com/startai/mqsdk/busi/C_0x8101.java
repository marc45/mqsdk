package cn.com.startai.mqsdk.busi;

import android.util.EventLog;

import org.greenrobot.eventbus.EventBus;

import cn.com.startai.mqsdk.util.eventbus.E_0x8101_Resp;
import cn.com.startai.mqsdk.util.eventbus.EventBean;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.busi.ErrorMiofMsg;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;

/**
 * 登录
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class C_0x8101 {

    private static String TAG = C_0x8101.class.getSimpleName();

    private static MqttPublishRequest create_req_0x8101(String topic, String toid, int value) {

        StartaiMessage message = (new StartaiMessage.Builder())
                .setMsgtype("0x8101")
                .setMsgcw("0x01")
                .setToid(toid)
                .setContent(new Req.ContentBean(value)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.qos = 1;
        mqttPublishRequest.topic = topic;
        return mqttPublishRequest;

    }


    /**
     * 设置音量
     *
     * @param value
     * @param listener
     */
    public static void m_0x8101_req(String topic, String toid, int value, IOnCallListener listener) {
        MqttPublishRequest req_0x8101 = create_req_0x8101(topic, toid, value);
        if (req_0x8101 == null) {
            return;
        }

        MessageSender.sendMessage(req_0x8101, listener);
    }

    /**
     * 手机端处理设置音量的返回回结果
     *
     * @param result
     * @param resp
     * @param errorMiofMsg
     * @return
     */
    public static void m_0x8101_resp(int result, Resp resp, ErrorMiofMsg errorMiofMsg) {

        EventBus.getDefault().post(new EventBean<>(EventBean.S_2_A_0x8101_RESP, new E_0x8101_Resp(result, resp, errorMiofMsg)));

    }

//    /**
//     * 受控端处理设置音量业务 并返回结果
//     *
//     * @param resp
//     * @return
//     */
//    public static BusiResult m_0x8101_handler(Resp resp) {
//
//
//        if (resp == null || resp.getContent() == null) {
//            return new BusiResult(0, new BusiResultErrorContent(BusiErrorCode.ERROR_CODE_0x810101));
//        }
//
//
//        String type = resp.getContent().getType();
//        int value = resp.getContent().getValue();
//
//        if (value < 0) {
//            return new BusiResult(0, new BusiResultErrorContent(BusiErrorCode.ERROR_CODE_0x810102));
//        }
//
//
//        DeviceControl.setVolum(SmartAdApp.getContext(), type, value);
//
//        return new BusiResult(1, resp.getContent());
//
//    }


    public static class Req {
        private ContentBean content;

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean {

            public ContentBean(int value) {
                this.value = value;
            }

            /**
             * type : Music
             * value : 12
             */

            private String type = "Music";
            private int value;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
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
            public ContentBean(int value) {
                this.value = value;
            }

            private String type;
            private int value;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }
    }


}
