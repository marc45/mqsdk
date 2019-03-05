package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.DistributeParam;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.localbusi.UserBusi;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.listener.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 分页获取好友列表
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class C_0x8038 implements Serializable {

    private static final String TAG = C_0x8038.class.getSimpleName();
    public static final String MSGTYPE = "0x8038";
    public static String MSGCW = "0x07";
    public static String MSG_DESC = "分页获取好友列表 ";


    /*
    1.查询用户绑定的设备
2.查询用户的用户好友
3.查询设备的设备列表
4.查询设备的用户好友
5.查询用户的手机列表
6.查询手机的用户好友
7.查询所有
     */
    public static final int TYPE_USER_DEVICE = 1;
    public static final int TYPE_USER_USER = 2;
    public static final int TYPE_DEVICE_DEVICE = 3;
    public static final int TYPE_DEVICE_USER = 4;
    public static final int TYPE_USER_MOBILE = 5;
    public static final int TYPE_MOBILE_USER = 6;
    public static final int TYPE_ALL = 7;

    /**
     * 请求 分页获取好友列表
     *
     * @param listener
     */
    public static void req(Req.ContentBean req, IOnCallListener listener) {

        MqttPublishRequest<StartaiMessage<Req.ContentBean>> req_msg = create_req_msg(req);
        if (req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }

        StartaiMqttPersistent.getInstance().send(req_msg, listener);
    }

    private static MqttPublishRequest<StartaiMessage<Req.ContentBean>> create_req_msg(Req.ContentBean req) {

        if (req == null) {
            SLog.e(TAG, "req is empty");
            return null;
        }

        if (TextUtils.isEmpty(req.getId())) {
            UserBusi userBusi = new UserBusi();
            C_0x8018.Resp.ContentBean currUser = userBusi.getCurrUser();
            if (currUser != null && !TextUtils.isEmpty(currUser.getUserid())) {
                req.setId(currUser.getUserid());
            }
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(MSGTYPE)
                .setMsgcw(MSGCW)
                .setContent(req).create();


        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }


        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }


    /**
     * 请求 分页获取好友列表 返回结果
     *
     * @param miof
     */
    public static void m_resp(String miof) {


        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, MSG_DESC + " 返回格式错误");
            return;
        }
        if (resp.getResult() == 1) {
            SLog.e(TAG, MSG_DESC + " 成功");

        } else {
            C_0x8038.Resp.ContentBean content = resp.getContent();
            C_0x8038.Req.ContentBean errcontent = content.getErrcontent();
            content.setId(errcontent.getId());
            content.setPage(errcontent.getPage());
            content.setRows(errcontent.getRows());
            content.setType(errcontent.getType());
            SLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }

        StartAI.getInstance().getPersisitnet().getEventDispatcher().onGetBindListByPageResult(resp);

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


            /**
             * id :
             * type : 1
             * page : 1
             * rows : 1
             */

            private String id;
            private int type;
            private int page;
            private int rows;

            public ContentBean() {
            }

            public ContentBean(int type, int page, int rows) {
                this.type = type;
                this.page = page;
                this.rows = rows;
            }

            public ContentBean(String id, int type, int page, int rows) {
                this.id = id;
                this.type = type;
                this.page = page;
                this.rows = rows;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "id='" + id + '\'' +
                        ", type=" + type +
                        ", page=" + page +
                        ", rows=" + rows +
                        '}';
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

            public int getPage() {
                return page;
            }

            public void setPage(int page) {
                this.page = page;
            }

            public int getRows() {
                return rows;
            }

            public void setRows(int rows) {
                this.rows = rows;
            }
        }


    }


    public static class Resp extends BaseMessage implements Serializable {

        private ContentBean content;

        @Override
        public String toString() {
            return "Resp{" +
                    "content=" + content +
                    ", msgcw='" + msgcw + '\'' +
                    ", msgtype='" + msgtype + '\'' +
                    ", fromid='" + fromid + '\'' +
                    ", toid='" + toid + '\'' +
                    ", domain='" + domain + '\'' +
                    ", appid='" + appid + '\'' +
                    ", ts=" + ts +
                    ", msgid='" + msgid + '\'' +
                    ", m_ver='" + m_ver + '\'' +
                    ", result=" + result +
                    '}';
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends BaseContentBean {


            private Req.ContentBean errcontent = null;


            /**
             * id :
             * type : 1
             * page : 1
             * rows : 1
             * total : 12
             * friends : [{"id":"","bindingtime":111,"alias":"alias","connstatus":1,"featureid":"","mac":"","type":1}]
             */

            private String id;
            private int type;
            private int page;
            private int rows;
            private int total;
            private List<C_0x8005.Resp.ContentBean> friends;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", id='" + id + '\'' +
                        ", type=" + type +
                        ", page=" + page +
                        ", rows=" + rows +
                        ", total=" + total +
                        ", friends=" + friends +
                        '}';
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
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

            public int getPage() {
                return page;
            }

            public void setPage(int page) {
                this.page = page;
            }

            public int getRows() {
                return rows;
            }

            public void setRows(int rows) {
                this.rows = rows;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<C_0x8005.Resp.ContentBean> getFriends() {
                return friends;
            }

            public void setFriends(List<C_0x8005.Resp.ContentBean> friends) {
                this.friends = friends;
            }

        }
    }


}
