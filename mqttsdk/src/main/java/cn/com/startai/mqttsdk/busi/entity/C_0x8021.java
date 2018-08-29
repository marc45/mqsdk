package cn.com.startai.mqttsdk.busi.entity;

import java.util.HashMap;
import java.util.UUID;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.busi.ErrorMiofMsg;
import cn.com.startai.mqttsdk.event.PersistentEventDispatcher;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 获取验证码
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class C_0x8021 {

    private static final String TAG = C_0x8021.class.getSimpleName();
    private static HashMap<String, Req.ContentBean> maps = new HashMap<>();

    /**
     * 获取验证码
     *
     * @param mobile   手机号
     * @param type     类型
     *                 1表示用户登录
     *                 2表示修改登录密码
     *                 3表示用户注册
     * @param listener
     */
    public static void m_0x8021_req(String mobile, int type, IOnCallListener listener) {

        MqttPublishRequest<StartaiMessage<Req.ContentBean>> x8021_req_msg = MqttPublishRequestCreator.create_0x8021_req_msg(mobile, type);
        if (x8021_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8021_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }


        String uuid = UUID.randomUUID().toString();

        maps.put(uuid, x8021_req_msg.message.getContent());

        x8021_req_msg.message.setMsgid(uuid);

        StartaiMqttPersistent.getInstance().send(x8021_req_msg, listener);

    }


    /**
     * 获取验证码
     *
     * @param result
     * @param resp
     * @param errorMiofMsg
     */
    public static void m_0x8021_resp(int result, Resp resp, ErrorMiofMsg errorMiofMsg) {

        if (result == 1 && resp != null) {
            SLog.d(TAG, "获取验证码成功");
            StartAI.getInstance().getPersisitnet().getEventDispatcher().onGetIdentifyResult(result, "", "", resp.getContent());
        } else if (result == 0 && errorMiofMsg != null) {
            SLog.d(TAG, "获取验证码失败");

            Req.ContentBean contentBean = maps.get(resp.getMsgid());

            Resp.ContentBean re = new Resp.ContentBean();
            if (contentBean != null) {
                re.setMobile(contentBean.getMobile());
                re.setType(contentBean.getType());

            }
            StartAI.getInstance().getPersisitnet().getEventDispatcher().onGetIdentifyResult(result, errorMiofMsg.getContent().getErrcode(), errorMiofMsg.getContent().getErrmsg(), re);
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

            private String mobile;
            /**
             * 1表示用户登录
             * 2表示修改登录密码
             * 3表示用户注册
             */
            private int type;

            public ContentBean() {
            }

            public ContentBean(String mobile, int type) {
                this.mobile = mobile;
                this.type = type;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "mobile='" + mobile + '\'' +
                        ", type=" + type +
                        '}';
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }


    }


    /**
     * 获取验证码 返回
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

            private String mobile;
            /**
             * 1表示用户登录
             * 2表示修改登录密码
             * 3表示用户注册
             */
            private int type;

            public ContentBean() {
            }

            public ContentBean(String mobile, int type) {
                this.mobile = mobile;
                this.type = type;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "mobile='" + mobile + '\'' +
                        ", type=" + type +
                        '}';
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }


}
