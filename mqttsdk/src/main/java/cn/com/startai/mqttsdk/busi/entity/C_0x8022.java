package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.DistributeParam;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;
import cn.com.startai.mqttsdk.utils.SRegexUtil;

/**
 * 检验验证码
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class C_0x8022 {

    public static String MSG_DESC = "检验验证码 ";
    private static final String TAG = C_0x8022.class.getSimpleName();
    public static final String MSGTYPE = "0x8022";
    public static String MSGCW = "0x07";


    public static final int TYPE_MOBILE_LOGIN = 1;
    public static final int TYPE_MOBILE_RESET_PWD = 2;
    public static final int TYPE_MOBILE_REGISTER = 3;
    public static final int TYPE_MOBILE_THIRD_MUBICBOX_LOGIN = 4;
    public static final int TYPE_MOBILE_UPDATE_MOBILENUM = 5;
    public static final int TYPE_EMAIL_REGISTER = 6;
    public static final int TYPE_EMAIL_RESET_PWD = 7;
    public static final int TYPE_EMAIL_UPDATE_EMAILNUM = 8;

    /**
     * 检验验证码
     *
     * @param account      手机号 或邮箱
     * @param identifyCode 验证码
     * @param type         类型1表示用户登录2表示修改登录密码3表示用户注册
     * @param listener
     */
    public static void m_0x8022_req(String account, String identifyCode, int type, IOnCallListener listener) {

        MqttPublishRequest x8022_req_msg = create_0x8022_req_msg(account, identifyCode, type);
        if (x8022_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8022_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8022_req_msg, listener);

    }

    /**
     * 检验验证码
     *
     * @param req
     * @param listener
     */
    public static void req(Req.ContentBean req, IOnCallListener listener) {
        if (req != null) {
            String account = "";
            if (!TextUtils.isEmpty(req.getAccount())) {
                account = req.getAccount();
            }

            if (!TextUtils.isEmpty(req.getMobile())) {
                account = req.getMobile();
            }
            m_0x8022_req(account, req.getIdentifyCode(), req.getType(), listener);
        }
    }

    /**
     * 组校验验证码包
     *
     * @param account      手机号
     * @param identifyCode 验证码
     * @param type         1表示用户登录  2表示修改登录密码 3表示用户注册
     * @return
     */
    public static MqttPublishRequest create_0x8022_req_msg(String account, String identifyCode, int type) {

        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(identifyCode) || type == 0) {
            SLog.e(TAG, "参数非法 mibile 及验证码不能为空 或type类型不对");
            return null;
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(MSGTYPE)
                .setMsgcw(MSGCW)
                .setFromid(MqttConfigure.getSn(StartAI.getContext()))
                .setContent(new C_0x8022.Req.ContentBean(account, identifyCode, type)).create();

        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }

    /**
     * 检验验证码
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


            SLog.e(TAG, "检验验证码成功");


            String mobile = resp.getContent().getMobile();

        } else {
            Resp.ContentBean content = resp.getContent();
            Req.ContentBean errcontent = content.getErrcontent();
            content.setType(errcontent.getType());
            content.setMobile(errcontent.getMobile());
            content.setIdentifyCode(errcontent.getIdentifyCode());
            content.setAccount(errcontent.getAccount());

            SLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onCheckIdentifyResult(resp);
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
            private String identifyCode;
            private int type;//1表示用户登录2表示修改登录密码3表示用户注册
            private String account;


            @Override
            public String toString() {
                return "ContentBean{" +
                        "mobile='" + mobile + '\'' +
                        ", identifyCode='" + identifyCode + '\'' +
                        ", type=" + type +
                        ", account='" + account + '\'' +
                        '}';
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public ContentBean() {
            }

            public ContentBean(String account, String identifyCode, int type) {
                this.mobile = account;
                this.account = account;
                this.identifyCode = identifyCode;
                this.type = type;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getIdentifyCode() {
                return identifyCode;
            }

            public void setIdentifyCode(String identifyCode) {
                this.identifyCode = identifyCode;
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

            /**
             * mobile : 13332965499
             * identifyCode : 123456
             * type : 1
             */


            private String mobile;
            private String identifyCode;
            private int type;
            private String account;

            private Req.ContentBean errcontent;

            public ContentBean(String mobile, String identifyCode, int type, String account) {
                this.mobile = mobile;
                this.identifyCode = identifyCode;
                this.type = type;
                this.account = account;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", mobile='" + mobile + '\'' +
                        ", identifyCode='" + identifyCode + '\'' +
                        ", type=" + type +
                        ", account='" + account + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getIdentifyCode() {
                return identifyCode;
            }

            public void setIdentifyCode(String identifyCode) {
                this.identifyCode = identifyCode;
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
