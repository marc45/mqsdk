package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

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
 * 绑定
 * Created by Robin on 2018/5/10.
 * qq: 419109715 彬影
 */

public class C_0x8002 {

    private static String TAG = C_0x8002.class.getSimpleName();
    public static String MSG_DESC = "绑定 ";
    public static final String MSGTYPE = "0x8002";
    public static String MSGCW = "0x07";

    /**
     * 添加设备或好友
     *
     * @param bebindid 对端 的user或sn
     */
    public static void m_0x8002_req(String userid, String bebindid, IOnCallListener listener) {

        MqttPublishRequest<StartaiMessage<Req.ContentBean>> x8002_req_msg = create_0x8002_req_msg(userid, bebindid);
        if (x8002_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8002_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }

        StartaiMqttPersistent.getInstance().send(x8002_req_msg, listener);

    }
    /**
     * 组添加好友包
     *
     * @param beBindingid 设备的sn
     * @return
     */
    public static MqttPublishRequest<StartaiMessage<C_0x8002.Req.ContentBean>> create_0x8002_req_msg(String userid, String beBindingid) {

        String bindingid = userid;
        if (TextUtils.isEmpty(bindingid)) {

            C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
            if (userBean != null && !TextUtils.isEmpty(userBean.getUserid())) {
                bindingid = userBean.getUserid();
            }
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8002")
                .setMsgcw("0x07")
                .setContent(new C_0x8002.Req.ContentBean(bindingid, beBindingid)).create();

        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }
        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }
    /**
     * 绑定消息
     *
     * @param miof
     */
    public static void m_resp(String miof) {


        C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
        String id = "";
        if (userBean != null) {
            id = userBean.getUserid();
        } else {
            id = MqttConfigure.getSn(StartAI.getContext());

        }
        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, "返回数据格式错误");
            return;
        }

        if (resp.getResult() == 1) {
            SLog.d(TAG, "绑定成功");

            Resp.ContentBean.BebindingBean bebinding = null;
            if (id.equals(resp.getContent().getBebinding().getId())) {

                Resp.ContentBean.BindingBean binding = resp.getContent().getBinding();
                bebinding = new Resp.ContentBean.BebindingBean(binding.getId(), binding.getApptype(), binding.getFeatureid(), binding.getTopic(), binding.getConnstatus(), binding.getMac());
            } else {
                bebinding = resp.getContent().getBebinding();
            }

            StartAI.getInstance().getPersisitnet().getEventDispatcher().onBindResult(resp, id, bebinding);

            if (userBean != null) {

                TopicBean topicBeanWill = new TopicBean(TopicConsts.getSubFriendWillTopic(bebinding.getId()), "set", "", id);
                SDBmanager.getInstance().addOrUpdateTopic(topicBeanWill);

                TopicBean topicBeanReport = new TopicBean(TopicConsts.getSubFriendReportTopic(bebinding.getId()), "set", "", id);
                SDBmanager.getInstance().addOrUpdateTopic(topicBeanReport);

                StartaiMqttPersistent.getInstance().subFriendReportTopic();

            }
        } else {
            SLog.e(TAG, "绑定失败 " +resp.getContent().getErrmsg());

            StartAI.getInstance().getPersisitnet().getEventDispatcher().onBindResult(resp, id, null);

        }


    }


    /**
     * 绑定请求json 对应实体类
     */
    public static class Req {

        private ContentBean content;

        public static class ContentBean {

            private String bindingid;
            private String bebindingid;

            public ContentBean() {
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "bindingid='" + bindingid + '\'' +
                        ", bebindingid='" + bebindingid + '\'' +
                        '}';
            }

            public ContentBean(String bindingid, String bebindingid) {
                this.bindingid = bindingid;
                this.bebindingid = bebindingid;
            }

            public String getBindingid() {
                return bindingid;
            }

            public void setBindingid(String bindingid) {
                this.bindingid = bindingid;
            }

            public String getBebindingid() {
                return bebindingid;
            }

            public void setBebindingid(String bebindingid) {
                this.bebindingid = bebindingid;
            }
        }


    }

    /**
     * 绑定请求返回 json 对应实体类
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
             * binding : {"id":"","apptype":"","featureid":"","connstatus":""}
             * bebinding : {"id":"","apptype":"","featureid":"","connstatus":""}
             */

            private BindingBean binding;
            private BebindingBean bebinding;
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", binding=" + binding +
                        ", bebinding=" + bebinding +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public BindingBean getBinding() {
                return binding;
            }

            public void setBinding(BindingBean binding) {
                this.binding = binding;
            }

            public BebindingBean getBebinding() {
                return bebinding;
            }

            public void setBebinding(BebindingBean bebinding) {
                this.bebinding = bebinding;
            }

            public static class BindingBean {


                private String id;
                private String apptype;
                private String featureid;
                private String topic;
                private String mac;
                private int connstatus;

                @Override
                public String toString() {
                    return "BindingBean{" +
                            "id='" + id + '\'' +
                            ", apptype='" + apptype + '\'' +
                            ", featureid='" + featureid + '\'' +
                            ", topic='" + topic + '\'' +
                            ", mac='" + mac + '\'' +
                            ", connstatus=" + connstatus +
                            '}';
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

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getApptype() {
                    return apptype;
                }

                public void setApptype(String apptype) {
                    this.apptype = apptype;
                }

                public String getFeatureid() {
                    return featureid;
                }

                public void setFeatureid(String featureid) {
                    this.featureid = featureid;
                }

                public int getConnstatus() {
                    return connstatus;
                }

                public void setConnstatus(int connstatus) {
                    this.connstatus = connstatus;
                }
            }

            public static class BebindingBean {
                public BebindingBean() {
                }

                private String id;
                private String apptype;
                private String featureid;
                private String topic;
                private int connstatus;
                private String mac;

                @Override
                public String toString() {
                    return "BebindingBean{" +
                            "id='" + id + '\'' +
                            ", apptype='" + apptype + '\'' +
                            ", featureid='" + featureid + '\'' +
                            ", topic='" + topic + '\'' +
                            ", connstatus=" + connstatus +
                            ", mac='" + mac + '\'' +
                            '}';
                }

                public BebindingBean(String id, String apptype, String featureid, String topic, int connstatus, String mac) {
                    this.id = id;
                    this.apptype = apptype;
                    this.featureid = featureid;
                    this.topic = topic;
                    this.connstatus = connstatus;
                    this.mac = mac;
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

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getApptype() {
                    return apptype;
                }

                public void setApptype(String apptype) {
                    this.apptype = apptype;
                }

                public String getFeatureid() {
                    return featureid;
                }

                public void setFeatureid(String featureid) {
                    this.featureid = featureid;
                }

                public int getConnstatus() {
                    return connstatus;
                }

                public void setConnstatus(int connstatus) {
                    this.connstatus = connstatus;
                }
            }

        }
    }


}
