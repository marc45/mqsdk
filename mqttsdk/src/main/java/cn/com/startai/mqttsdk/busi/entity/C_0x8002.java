package cn.com.startai.mqttsdk.busi.entity;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.busi.ErrorMiofMsg;
import cn.com.startai.mqttsdk.control.SDBmanager;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.control.entity.TopicBean;
import cn.com.startai.mqttsdk.control.entity.UserBean;
import cn.com.startai.mqttsdk.event.PersistentEventDispatcher;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.localbusi.UserBusi;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 绑定
 * Created by Robin on 2018/5/10.
 * qq: 419109715 彬影
 */

public class C_0x8002 {

    private static String TAG = C_0x8002.class.getSimpleName();


//    private static HashMap<String, String> maps = new HashMap<>();

    /**
     * 添加设备或好友
     *
     * @param bebindid 对端 的user或sn
     */
    public static void m_0x8002_req(String bebindid, IOnCallListener listener) {

        MqttPublishRequest<StartaiMessage<Req.ContentBean>> x8002_req_msg = MqttPublishRequestCreator.create_0x8002_req_msg(bebindid);
        if (x8002_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8002_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }


//        String uuid = UUID.randomUUID().toString();
//        maps.put(uuid, x8002_req_msg.message.getContent().getBindingid());
//        x8002_req_msg.message.setMsgid(uuid);

        StartaiMqttPersistent.getInstance().send(x8002_req_msg, listener);

    }

    /**
     * 绑定消息
     *
     * @param result
     * @param resp
     * @param errorMiofMsg
     */
    public static void m_0x8002_resp(int result, Resp resp, ErrorMiofMsg errorMiofMsg) {


        C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
        String id = "";
        if (userBean != null) {
            id = userBean.getUserid();
        } else {
            id = MqttConfigure.getSn(StartAI.getContext());

        }


        if (result == 1 && resp != null) {
            SLog.d(TAG, "绑定成功");

            Resp.ContentBean.BebindingBean bebinding = null;
            if (id.equals(resp.getContent().getBebinding().getId())) {

                Resp.ContentBean.BindingBean binding = resp.getContent().getBinding();
                bebinding = new Resp.ContentBean.BebindingBean(binding.getId(), binding.getApptype(), binding.getFeatureid(), binding.getTopic(), binding.getConnstatus(), binding.getMac());
            } else {
                bebinding = resp.getContent().getBebinding();
            }

            StartAI.getInstance().getPersisitnet().getEventDispatcher().onBindResult(result, "", "", id, bebinding);

            if (userBean != null) {

                TopicBean topicBean = new TopicBean(TopicConsts.getSubFriendTopic(bebinding.getId()), "set", "", id);
                SDBmanager.getInstance().addOrUpdateTopic(topicBean);

                StartaiMqttPersistent.getInstance().subFriendReportTopic();

            }

        } else if (result == 0 && errorMiofMsg != null) {

            SLog.e(TAG, "绑定失败");

            StartAI.getInstance().getPersisitnet().getEventDispatcher().onBindResult(result, errorMiofMsg.getContent().getErrcode(), errorMiofMsg.getContent().getErrmsg(), id, null);

        } else {
            SLog.e(TAG, "返回数据格式错误");
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

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean {

            @Override
            public String toString() {
                return "ContentBean{" +
                        "binding=" + binding +
                        ", bebinding=" + bebinding +
                        '}';
            }

            /**
             * binding : {"id":"","apptype":"","featureid":"","connstatus":""}
             * bebinding : {"id":"","apptype":"","featureid":"","connstatus":""}
             */

            private BindingBean binding;
            private BebindingBean bebinding;

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
