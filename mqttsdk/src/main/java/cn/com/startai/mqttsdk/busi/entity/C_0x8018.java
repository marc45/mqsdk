package cn.com.startai.mqttsdk.busi.entity;

import java.util.HashMap;
import java.util.UUID;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.busi.ErrorMiofMsg;
import cn.com.startai.mqttsdk.control.SDBmanager;
import cn.com.startai.mqttsdk.control.SPController;
import cn.com.startai.mqttsdk.control.entity.MsgWillSendBean;
import cn.com.startai.mqttsdk.control.entity.UserBean;
import cn.com.startai.mqttsdk.event.PersistentEventDispatcher;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.localbusi.UserBusi;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 登录
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class C_0x8018 {

    public static String TAG = C_0x8018.class.getSimpleName();


    public static HashMap<String, Req.ContentBean> maps = new HashMap<>();


    /**
     * 登录
     *
     * @param uName        邮箱或手机号
     * @param pwd          密码
     * @param identifyCode 验证码
     * @param listener
     */
    public static void m_0x8018_req(String uName, String pwd, String identifyCode, IOnCallListener listener) {

        boolean isChangeUser = false; //更换账号调用登录接口

        //当前登录的用户
        UserBean currUserFromDb = new UserBusi().getCurrUserFromDb();

        if (currUserFromDb == null || currUserFromDb.getType() == 0) {
            SLog.d(TAG, "当前未登录用户，需要登录");
        } else {

            if (currUserFromDb.getUName().equals(uName)) {

                //已经登录
                long expire_in = currUserFromDb.getExpire_in();
                long time = currUserFromDb.getTime();

                long diff = (System.currentTimeMillis() - time) / 1000 - expire_in;
                if (diff > 0) {
                    SLog.d(TAG, "账号登录状态已过期，需要重新登录");

                } else {
                    SLog.d(TAG, "账号登录状态未过期，直接回调登录成功");
                    Resp.ContentBean contentBean = new Resp.ContentBean();
                    contentBean.setuName(currUserFromDb.getUName());
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


        MqttPublishRequest<StartaiMessage<Req.ContentBean>> x8018_req_msg = MqttPublishRequestCreator.create_0x8018_req_msg(uName, pwd, identifyCode);

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
     * 登出
     */

    public static void logout() {

        logoutAndReconnect();

        StartAI.getInstance().getPersisitnet().getEventDispatcher().onLogoutResult(1, "", "");

    }

    private static void logoutAndReconnect() {

        //清空clientid
        SPController.setClientid("");

        SPController.setUserInfo(null);

        //清空用户登录状态
        SDBmanager.getInstance().deleteUserByLoginStatus(1);

        //写入登出标志

        //断开连接并重新以随机clientid连接
        StartaiMqttPersistent.getInstance().disconnectAndReconnect();


    }


    /**
     * 处理登录结果
     *
     * @param miof
     */
    public static void m_0x8018_resp(String miof) {

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

            SLog.e(TAG, "登录失败");
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
