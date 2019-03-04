package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.UUID;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.DistributeParam;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.busi.ErrorMiofMsg;
import cn.com.startai.mqttsdk.control.SDBmanager;
import cn.com.startai.mqttsdk.control.SPController;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.control.entity.MsgWillSendBean;
import cn.com.startai.mqttsdk.control.entity.UserBean;
import cn.com.startai.mqttsdk.event.PersistentEventDispatcher;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.localbusi.UserBusi;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;
import cn.com.startai.mqttsdk.utils.SRegexUtil;

/**
 * 登录
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class C_0x8018 {

    public static String TAG = C_0x8018.class.getSimpleName();


    public static HashMap<String, Req.ContentBean> maps = new HashMap<>();

    public static String MSG_DESC = "登录 ";
    public static final String MSGTYPE = "0x8018";
    public static String MSGCW = "0x07";

    public static final int TYPE_EMAIL_PWD = 1;
    public static final int TYPE_MOBILE_PWD = 2;
    public static final int TYPE_MOBILE_CODE = 3;
    public static final int TYPE_UNAME_PWD = 4;
    public static final int TYPE_MOBILE_CODE_PWD = 5;

    public static final int THIRD_WECHAT = 10;
    public static final int THIRD_ALIPAY = 11;
    public static final int THIRD_QQ = 12;
    public static final int THIRD_GOOGLE = 13;
    public static final int THIRD_TWITTER = 14;
    public static final int THIRD_AMAZON = 15;
    public static final int THIRD_FACEBOOK = 16;
    public static final int THIRD_MI = 17;
    public static final int THIRD_SMALLROUTINE = 18;

    /**
     * 登录
     *
     * @param req
     * @param listener
     */
    public static void req(Req.ContentBean req, IOnCallListener listener) {
        if (req != null) {
            m_0x8018_req(req.getUname(), req.getPwd(), req.getIdentifyCode(), listener);
        }
    }

    /**
     * 登录
     *
     * @param uName        邮箱或手机号
     * @param pwd          密码
     * @param identifyCode 验证码
     * @param listener
     */
    public static void m_0x8018_req(String uName, String pwd, String identifyCode, IOnCallListener listener) {

        uName = uName == null ? "" : uName;

        boolean isChangeUser = false; //更换账号调用登录接口

        //当前登录的用户
        UserBean currUserFromDb = new UserBusi().getCurrUserFromDb();

        if (currUserFromDb == null || currUserFromDb.getType() == 0) {
            SLog.d(TAG, "当前未登录用户，需要登录");
        } else {

            String dbUname = currUserFromDb.getUName();//第三方登录时没有uname


            if (uName.equals(dbUname)) {

                //已经登录
                long expire_in = currUserFromDb.getExpire_in();

                long time = currUserFromDb.getTime();

                long diff = (System.currentTimeMillis() - time) / 1000 - expire_in;
                if (expire_in > 0 && diff > 0) {
                    SLog.d(TAG, "账号登录状态已过期，需要重新登录");

                } else {
                    SLog.d(TAG, "账号登录状态未过期，直接回调登录成功");
                    Resp.ContentBean contentBean = new Resp.ContentBean();
                    contentBean.setuName(dbUname);
                    contentBean.setToken(currUserFromDb.getToken());
                    contentBean.setType(currUserFromDb.getType());
                    contentBean.setExpire_in(currUserFromDb.getExpire_in());
                    contentBean.setUserid(currUserFromDb.getUserid());

                    Resp resp = new Resp();
                    resp.setContent(contentBean);
                    resp.setResult(1);
                    resp.setToid(contentBean.getUserid());
                    StartAI.getInstance().getPersisitnet().getEventDispatcher().onLoginResult(resp);
                    return;
                }


            } else {
                SLog.d(TAG, "切换账号登录");


                isChangeUser = true;
            }

        }


        MqttPublishRequest<StartaiMessage<Req.ContentBean>> x8018_req_msg = create_0x8018_req_msg(uName, pwd, identifyCode);

        if (x8018_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8018_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }

        StartaiMessage<Req.ContentBean> message = x8018_req_msg.message;


        String uuid = UUID.randomUUID().toString();

        maps.put(uuid, message.getContent());
        message.setMsgid(uuid);

        if (isChangeUser) {

            //保存消息 在发送成功之后 会删除 在发送失败之后 在下次重连时会重新发送
            MsgWillSendBean msgWillSendBean = new MsgWillSendBean();
            msgWillSendBean.setMsgtype(message.getMsgtype());
            msgWillSendBean.setMsgWillSend(SJsonUtils.toJson(x8018_req_msg.message));
            msgWillSendBean.setTopic(x8018_req_msg.topic);
            SDBmanager.getInstance().addOrUpdateMsgWillSend(msgWillSendBean);

//            StartaiMqttPersistent.getInstance().send(x8018_req_msg, null);
//            listener.onSuccess(x8018_req_msg);

            logoutAndReconnect();
        } else {
            StartaiMqttPersistent.getInstance().send(x8018_req_msg, listener);
        }
    }

    /**
     * 组登录包
     *
     * @param uName
     * @param pwd
     * @param identifyCode
     * @return
     */
    public static MqttPublishRequest<StartaiMessage<C_0x8018.Req.ContentBean>> create_0x8018_req_msg(String uName, String pwd, String identifyCode) {
        if (TextUtils.isEmpty(uName)) {

            SLog.e(TAG, "参数非法 uName为空 ");
            return null;
        }

        if (TextUtils.isEmpty(pwd) && TextUtils.isEmpty(identifyCode)) {
            SLog.e(TAG, "参数非法  密码或密码验证码为空");
            return null;
        }


        int type = 0;
        if (SRegexUtil.isEmail(uName)) {
            type = 1;
        } else if (SRegexUtil.isMobileSimple(uName)) {

            if (TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(identifyCode)) {
                type = 3;
            } else if (!TextUtils.isEmpty(pwd) && TextUtils.isEmpty(identifyCode)) {
                type = 2;
            } else if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(identifyCode)) {
                type = 5;
            } else {
                SLog.e(TAG, "参数非法 类型与 uname不匹配 ");
                return null;
            }

        } else if (SRegexUtil.isUsername(uName)) {
            type = 4;
        } else {
            SLog.e(TAG, "参数非法 uname 格式不对");
            return null;
        }

        if (type == 1 || type == 2 || type == 4 || type == 5) {
            if (TextUtils.isEmpty(pwd)) {
                SLog.e(TAG, "参数非法 密码为空 ");
                return null;
            }
        } else if (type == 3) {
            if (TextUtils.isEmpty(identifyCode)) {
                SLog.e(TAG, "参数非法 验证码为空");
                return null;
            }
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8018")
                .setMsgcw("0x07")
                .setFromid(MqttConfigure.getSn(StartAI.getContext()))
                .setContent(new C_0x8018.Req.ContentBean(uName, pwd, identifyCode, type)).create();

        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }


    /**
     * 登出
     */

    public static void logout() {

        logoutAndReconnect();

        StartAI.getInstance().getPersisitnet().getEventDispatcher().onLogoutResult(1, "", "");

    }

    private static void logoutAndReconnect() {

        //清空上次连接的clientid
        SPController.setClientid("");

        SPController.setUserInfo(null);

        //清空用户登录状态
        SDBmanager.getInstance().deleteUserByLoginStatus(1);


        //断开连接并重新以随机clientid连接
        StartaiMqttPersistent.getInstance().disconnectAndReconnect();


    }


    /**
     * 处理登录结果
     *
     * @param miof
     */
    public static void m_resp(String miof) {

        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        SLog.d(TAG, "resp = " + resp);
        if (resp == null) {
            SLog.e(TAG, "返回数据格式错误");
            return;
        }

        if (resp.getResult() == 1) {
            SLog.e(TAG, "登录成功");

            Resp.ContentBean content = resp.getContent();

//            SPController.setUserInfo(resp.getContent());

            SDBmanager.getInstance().resetUser();
            //保存本地登录状态
            SDBmanager.getInstance().addOrUpdateUser(new UserBean(content.getUserid(), content.getToken(), content.getExpire_in(), content.getuName(), content.getType(), 1));

            //订阅 userid相关主题
            StartaiMqttPersistent.getInstance().subUserTopic();

            //订阅对应主题
            StartaiMqttPersistent.getInstance().subFriendReportTopic();

        } else {

            Resp.ContentBean content = resp.getContent();
            Req.ContentBean errcontent = content.getErrcontent();
            content.setType(errcontent.getType());
            content.setUname(errcontent.getUname());

            SLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }

        //登录失败
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onLoginResult(resp);

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

            private String uname;
            private String pwd;
            private String identifyCode;
            private int type;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "uname='" + uname + '\'' +
                        ", pwd='" + pwd + '\'' +
                        ", identifyCode='" + identifyCode + '\'' +
                        ", type=" + type +
                        '}';
            }

            public ContentBean(String uname, String identifyCode, int type) {
                this.uname = uname;
                this.identifyCode = identifyCode;
                this.type = type;
            }

            public ContentBean(String uname, String pwd, String identifyCode, int type) {
                this.uname = uname;
                this.pwd = pwd;
                this.identifyCode = identifyCode;
                this.type = type;
            }

            public String getIdentifyCode() {
                return identifyCode;
            }

            public void setIdentifyCode(String identifyCode) {
                this.identifyCode = identifyCode;
            }

            public ContentBean() {
            }

            public String getUname() {
                return uname;
            }

            public void setUname(String uname) {
                this.uname = uname;
            }

            public String getPwd() {
                return pwd;
            }

            public void setPwd(String pwd) {
                this.pwd = pwd;
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
     * 登录 返回
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

            private String userid;//用户id
            private String token; //用户token
            private long expire_in; //token时效
            private String uname;  // 用户名
            private int type;// 登录类型
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", userid='" + userid + '\'' +
                        ", token='" + token + '\'' +
                        ", expire_in=" + expire_in +
                        ", uname='" + uname + '\'' +
                        ", type=" + type +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public String getUname() {
                return uname;
            }

            public void setUname(String uname) {
                this.uname = uname;
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public ContentBean(String userid, String token, long expire_in, String uName, int type) {
                this.userid = userid;
                this.token = token;
                this.expire_in = expire_in;
                this.uname = uName;
                this.type = type;
            }

            public String getuName() {
                return uname;
            }

            public void setuName(String uName) {
                this.uname = uName;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public ContentBean() {
            }

            public ContentBean(String userid, String token, long expire_in) {
                this.userid = userid;
                this.token = token;
                this.expire_in = expire_in;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public long getExpire_in() {
                return expire_in;
            }

            public void setExpire_in(long expire_in) {
                this.expire_in = expire_in;
            }
        }
    }


}
