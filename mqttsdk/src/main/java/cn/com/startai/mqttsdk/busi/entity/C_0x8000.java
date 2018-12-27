package cn.com.startai.mqttsdk.busi.entity;

import java.util.List;
import java.util.TimerTask;

import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.GlobalVariable;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.control.SPController;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;
import cn.com.startai.mqttsdk.utils.STimerUtil;

/**
 * 获取可用ip组
 * Created by Robin on 2018/5/10.
 * qq: 419109715 彬影
 */

public class C_0x8000 {

    private static String TAG = C_0x8000.class.getSimpleName();
    public static String MSG_DESC = "获取可用ip组 ";
    public static String MSGTYPE = "0x8000";
    public static String MSGCW = "0x07";

    /**
     * 获取可用ip组
     *
     * @param outerIp
     */
    public static void m_0x8000_req(String outerIp, IOnCallListener listener) {

        MqttPublishRequest x8000_req_msg = MqttPublishRequestCreator.create_0x8000_req_msg(outerIp);
        if (x8000_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8000_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }

        StartaiMqttPersistent.getInstance().send(x8000_req_msg, listener);

    }


    /**
     * 处理 节点信息
     *
     * @param miof
     */
    public static void m_0x8000_resp(String miof) {


        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, "返回数据格式错误");
            return;
        }

        if (resp.getResult() == 1) {
            SLog.e(TAG, "节点获取成功");

            int size = resp.getContent().getNode().size();
            if (size == 0) {
                SLog.d(TAG, "没有获取到区域节点信息，60秒后再次获取");
                STimerUtil.schedule("checkGetAreaNode", new TimerTask() {
                    @Override
                    public void run() {
                        StartaiMqttPersistent.getInstance().checkGetAreaNode();
                    }
                }, 60 * 1000);
                return;
            }


            //保存 节点信息
            SPController.saveAreaNodeBeans(resp.getContent());

            C_0x8000.Resp.ContentBean content = resp.getContent();
            int cycle = content.getCycle();
            List<Resp.ContentBean.NodeBean> node = content.getNode();

            boolean isNeedChangHost = false;
            StartaiMqttPersistent instance = StartaiMqttPersistent.getInstance();
            for (C_0x8000.Resp.ContentBean.NodeBean nodeBean : node) {
                //当前连接的节点权值已经为0，不可用 需要立即切换节点
                if (nodeBean.getWeight() <= 0 && nodeBean.getServer_domain().equals(instance.getHost())) {
                    isNeedChangHost = true;
                }
            }
            if (GlobalVariable.areaNodeBean == null) {
                GlobalVariable.areaNodeBean = SPController.getAllAreaNodeBean();
            }
            //保存此次获取节点信息的时候， sdk会在每次重启后去判断这个时间是否大于更新周期，如果大于周期则重新获取
            SPController.setLastGet_0x800_respTime(System.currentTimeMillis());

            if (isNeedChangHost) {
                for (Resp.ContentBean.NodeBean ab : GlobalVariable.areaNodeBean.getNode()) {
                    if (ab.getServer_domain().equals(instance.getHost())) {
                        ab.setWeight(0);
                        SLog.d(TAG, "发现新的权值列表中，当前连接的节点权值是0，为不可用节点，立即切换节点");
                        instance.disconnectAndReconnect();
                        break;
                    }
                }
            }

            STimerUtil.schedule("checkGetAreaNode", new TimerTask() {
                @Override
                public void run() {
                    StartaiMqttPersistent.getInstance().checkGetAreaNode();
                }
            }, cycle);

        }
    }


    public static class Req {
        private ContentBean contentBean;

        public static class ContentBean {
            public String ip;

            public ContentBean(String ip) {
                this.ip = ip;
            }
        }

        public Req(ContentBean contentBean) {
            this.contentBean = contentBean;
        }

        public ContentBean getContentBean() {
            return contentBean;
        }

        public void setContentBean(ContentBean contentBean) {
            this.contentBean = contentBean;
        }
    }

    public static class Resp extends BaseMessage {

        /**
         * toid : clientidclient1
         * msgcw : 0x08
         * lng :
         * apptype : Cloud/BXTM
         * msgid :
         * fromid : SERVICE/NMC/Smart/Controll/H5/0x07
         * contentBean : {"node":[{"ipport":"47.106.45.110:8883","server_domain":"cn.startai.net:8883","weight":2},{"ipport":"47.252.50.56:8883","server_domain":"us.startai.net:8883","weight":1}],"cycle":86400}
         * result : 1
         * domain : startai
         * m_ver : Json_1.1.4_4.2
         * msgtype : 0x8000
         * lat :
         * ts : 1526012849543
         */


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
             * node : [{"server_domain":"ssl:// cn.startai.net:8883","ip":"47.106.45.110","port":8883,"protocol":"ssl","weight":1}]
             * cycle : 86400
             */

            private int cycle;
            private List<NodeBean> node;
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", cycle=" + cycle +
                        ", node=" + node +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public int getCycle() {
                return cycle;
            }

            public void setCycle(int cycle) {
                this.cycle = cycle;
            }

            public List<NodeBean> getNode() {
                return node;
            }

            public void setNode(List<NodeBean> node) {
                this.node = node;
            }

            public static class NodeBean {
                /**
                 * server_domain : ssl:// cn.startai.net:8883
                 * ip : 47.106.45.110
                 * port : 8883
                 * protocol : ssl
                 * weight : 1
                 */

                private String server_domain;
                private String ip;
                private int port;
                private String protocol;
                private double weight;

                public String getServer_domain() {
                    return server_domain;
                }

                public void setServer_domain(String server_domain) {
                    this.server_domain = server_domain;
                }

                public String getIp() {
                    return ip;
                }

                public void setIp(String ip) {
                    this.ip = ip;
                }

                public int getPort() {
                    return port;
                }

                public void setPort(int port) {
                    this.port = port;
                }

                public String getProtocol() {
                    return protocol;
                }

                public void setProtocol(String protocol) {
                    this.protocol = protocol;
                }

                public double getWeight() {
                    return weight;
                }

                public void setWeight(double weight) {
                    this.weight = weight;
                }
            }
        }
    }


}
