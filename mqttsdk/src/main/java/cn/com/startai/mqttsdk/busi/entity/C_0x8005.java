package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.DistributeParam;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.control.SDBmanager;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.control.entity.TopicBean;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.localbusi.UserBusi;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.listener.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 查询绑定关系
 * Created by Robin on 2018/5/10.
 * qq: 419109715 彬影
 */

public class C_0x8005 implements Serializable {

    private static String TAG = C_0x8005.class.getSimpleName();

    public static String MSG_DESC = "查询绑定关系 ";
    public static final String MSGTYPE = "0x8005";
    public static String MSGCW = "0x07";
    
//    private static HashMap<String, Req.ContentBean> maps = new HashMap<>();


    /**
     * 查询绑定关系
     *
     * @param type 1.查询用户绑定的设备
     *             2.查询设备的用户列表
     *             3.查询用户的用户好友
     *             4.查询设备的设备列表
     *             5.查询用户的手机列表
     *             6.查询手机的用户好友
     *             7.查询所有
     * @return
     */
    public static void m_0x8005_req(String userid, int type, IOnCallListener listener) {

        MqttPublishRequest<StartaiMessage<Req.ContentBean>> x8005_req_msg = create_0x8005_req_msg(userid, type);
        if (x8005_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8005_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }

//        StartaiMessage<Req.ContentBean> message = x8005_req_msg.message;


//        String uuid = UUID.randomUUID().toString();
//
//        maps.put(uuid, message.getContentBean());
//        message.setMsgid(uuid);
        StartaiMqttPersistent.getInstance().send(x8005_req_msg, listener);

    }

    /**
     * 组获取好友列表好友包
     *
     * @param type 1.查询智能设备列表
     *             2.查询用户好友列表
     *             3.查询用户-手机列表
     *             4.查询所有的好友列表
     * @return
     */
    public static MqttPublishRequest<StartaiMessage<C_0x8005.Req.ContentBean>> create_0x8005_req_msg(String userid, int type) {


        String id = userid;
        if (TextUtils.isEmpty(id)) {

            C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
            if (userBean != null && !TextUtils.isEmpty(userBean.getUserid())) {
                id = userBean.getUserid();
            }

        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8005")
                .setMsgcw("0x07")
                .setContent(new C_0x8005.Req.ContentBean(id, type)).create();
        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }
        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }

    /**
     * 查询绑定关系返回
     *
     * @param miof
     * @param miof
     */
    public static void m_resp(String miof) {

        Response response = SJsonUtils.fromJson(miof, Response.class);
        if (response == null) {
            SLog.e(TAG, "返回数据格式错误");
            return;
        }
        C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();


        if (response.getResult() == 1) {

            Resp resp = SJsonUtils.fromJson(miof, Resp.class);
            if (resp == null) {
                SLog.e(TAG, "返回数据格式错误");
                return;
            }
            response.setResp(resp.getContent());
            SLog.d(TAG, "查询成功");
            StartAI.getInstance().getPersisitnet().getEventDispatcher().onGetBindListResult(response);

            //手机端订阅终端相关主题
            if (userBean != null) {

                //重置数据库，将 type 先设为 "remove"
                SDBmanager.getInstance().resetTopic(userBean.getUserid());
                if (resp.getContent().size() > 0) {

                    for (Resp.ContentBean contentBean : resp.getContent()) {
                        if (MqttConfigure.getSn(StartAI.getContext()).equals(contentBean.getId())) {
                            //是自己 的设备
                            continue;
                        }

                        TopicBean topicBeanWill = new TopicBean(TopicConsts.getSubFriendWillTopic(contentBean.id), "set", "", userBean.getUserid());
                        SDBmanager.getInstance().addOrUpdateTopic(topicBeanWill);

                        TopicBean topicBeanReport = new TopicBean(TopicConsts.getSubFriendReportTopic(contentBean.id), "set", "", userBean.getUserid());
                        SDBmanager.getInstance().addOrUpdateTopic(topicBeanReport);
                    }
                    //订阅对应主题
                    StartaiMqttPersistent.getInstance().subFriendReportTopic();

                }
            }

        } else {
            SLog.e(TAG, "查询失败 " + response.getErrmsg());
            RespErr respErr = SJsonUtils.fromJson(miof, RespErr.class);
            if (respErr == null) {
                SLog.e(TAG, "返回数据格式错误");
                return;
            }
            response.setErrcode(response.getErrcode());
            response.setErrmsg(response.getErrmsg());
            response.setErrContent(response.getErrContent());
            StartAI.getInstance().getPersisitnet().getEventDispatcher().onGetBindListResult(response);
        }


    }

    public static class Req {

        private ContentBean content;

        public static class ContentBean {

            private String id;
            private int type;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "id='" + id + '\'' +
                        ", type=" + type +
                        '}';
            }

            public ContentBean() {
            }

            public ContentBean(String id, int type) {
                this.id = id;
                this.type = type;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }


    }

    public static class Response extends BaseMessage implements Serializable {

        private ArrayList<Resp.ContentBean> resp = new ArrayList<>();
        private String errcode;
        private String errmsg;
        private Req.ContentBean errContent = new Req.ContentBean();


        public Response() {
        }


        public Response(ArrayList<Resp.ContentBean> resp, String errcode, String errmsg, Req.ContentBean errContent) {
            this.resp = resp;
            this.errcode = errcode;
            this.errmsg = errmsg;
            this.errContent = errContent;
        }

        @Override
        public String toString() {
            return "Response{" +
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
                    ", resp=" + resp +
                    ", errcode='" + errcode + '\'' +
                    ", errmsg='" + errmsg + '\'' +
                    ", errContent=" + errContent +
                    '}';
        }


        public String getErrcode() {
            return errcode;
        }

        public void setErrcode(String errcode) {
            this.errcode = errcode;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public Req.ContentBean getErrContent() {
            return errContent;
        }

        public void setErrContent(Req.ContentBean errContent) {
            this.errContent = errContent;
        }

        public ArrayList<Resp.ContentBean> getResp() {
            return resp;
        }

        public void setResp(ArrayList<Resp.ContentBean> resp) {
            this.resp = resp;
        }


    }


    public static class Resp extends BaseMessage implements Serializable {

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

        private ArrayList<ContentBean> content;

        public ArrayList<ContentBean> getContent() {
            return content;
        }

        public void setContent(ArrayList<ContentBean> content) {
            this.content = content;
        }

        public static class ContentBean implements Serializable {

            /**
             * apptype : smartCtrl
             * bindingtime : 1530074475244
             * connstatus : 1
             * id : 736F6C863300F4EB1EE79CC8015B503D
             * type : 1
             */
            private String apptype;
            private long bindingtime;
            private int connstatus;
            private String id;
            private int type;
            private String alias;
            private String featureid;
            private String topic;
            private String mac;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "apptype='" + apptype + '\'' +
                        ", bindingtime=" + bindingtime +
                        ", connstatus=" + connstatus +
                        ", id='" + id + '\'' +
                        ", type=" + type +
                        ", alias='" + alias + '\'' +
                        ", featureid='" + featureid + '\'' +
                        ", topic='" + topic + '\'' +
                        ", mac='" + mac + '\'' +
                        '}';
            }

            public String getFeatureid() {
                return featureid;
            }

            public void setFeatureid(String featureid) {
                this.featureid = featureid;
            }

            public String getMac() {
                return mac;
            }

            public void setMac(String mac) {
                this.mac = mac;
            }

            public String getTopic() {
                return topic;
            }

            public void setTopic(String topic) {
                this.topic = topic;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getApptype() {
                return apptype;
            }

            public void setApptype(String apptype) {
                this.apptype = apptype;
            }

            public long getBindingtime() {
                return bindingtime;
            }

            public void setBindingtime(long bindingtime) {
                this.bindingtime = bindingtime;
            }

            public int getConnstatus() {
                return connstatus;
            }

            public void setConnstatus(int connstatus) {
                this.connstatus = connstatus;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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
     * 请求错误返回 json 对应实体类
     */
    public static class RespErr extends BaseMessage {


        private ContentBean content;

        @Override
        public String toString() {
            return "RespErr{" +
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

        public static class ContentBean {

            private String errcode;
            private String errmsg;
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public ContentBean(String errcode, String errmsg, Req.ContentBean errcontent) {
                this.errcode = errcode;
                this.errmsg = errmsg;
                this.errcontent = errcontent;
            }

            public String getErrcode() {
                return errcode;
            }

            public void setErrcode(String errcode) {
                this.errcode = errcode;
            }

            public String getErrmsg() {
                return errmsg;
            }

            public void setErrmsg(String errmsg) {
                this.errmsg = errmsg;
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
