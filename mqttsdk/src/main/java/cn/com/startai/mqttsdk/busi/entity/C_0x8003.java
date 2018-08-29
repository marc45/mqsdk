package cn.com.startai.mqttsdk.busi.entity;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.busi.ErrorMiofMsg;
import cn.com.startai.mqttsdk.control.SDBmanager;
import cn.com.startai.mqttsdk.control.SPController;
import cn.com.startai.mqttsdk.event.PersistentEventDispatcher;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 注销激活
 * Created by Robin on 2018/5/10.
 * qq: 419109715 彬影
 */

public class C_0x8003 {

    private static String TAG = C_0x8003.class.getSimpleName();


    /**
     * 注销激活
     */
    public static void m_0x8003_req(IOnCallListener listener) {

        MqttPublishRequest x8003_req_msg = MqttPublishRequestCreator.create_0x8003_req_msg();
        if (x8003_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8003_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8003_req_msg, listener);

    }

    /**
     * 注销激活 消息
     *
     * @param result
     * @param resp
     * @param errorMiofMsg
     */
    public static void m_0x8003_resp(int result, Resp resp, ErrorMiofMsg errorMiofMsg) {


        if (result == 1 && resp != null) {


            //是自己的注销激活消息
            if (MqttConfigure.getSn(StartAI.getContext()).equalsIgnoreCase(resp.content.getId())) {
                SLog.d(TAG, "注销成功");
                SPController.clearAllSp();
                SDBmanager.getInstance().deleteAllDB();

                StartaiMqttPersistent.getInstance().disConnect(true);

                StartAI.getInstance().getPersisitnet().getEventDispatcher().onUnActiviteResult(result, "", "");

            }


        } else if (result == 0 && errorMiofMsg != null) {
            SLog.e(TAG, "注销失败");
            StartAI.getInstance().getPersisitnet().getEventDispatcher().onUnActiviteResult(result, errorMiofMsg.getContent().getErrcode(), errorMiofMsg.getContent().getErrmsg());
        } else {
            SLog.e(TAG, "返回数据格式错误");
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

        public static class ContentBean {
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


}
