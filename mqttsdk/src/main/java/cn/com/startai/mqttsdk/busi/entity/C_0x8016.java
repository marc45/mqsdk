package cn.com.startai.mqttsdk.busi.entity;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.DistributeParam;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.listener.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 查询最新版本
 * Created by Robin on 2018/7/19.
 * qq: 419109715 彬影
 */

public class C_0x8016 {

    private static final String TAG = C_0x8016.class.getSimpleName();
    public static String MSG_DESC = "查询最新版本 ";
    public static final String MSGTYPE = "0x8016";
    public static String MSGCW = "0x07";

    /**
     * 查询最新版本
     *
     * @param os          系统
     * @param packageName 包名
     * @param listener
     */
    public static void m_0x8016_req(String os, String packageName, IOnCallListener listener) {

        MqttPublishRequest x8016_req_msg = create_0x8016_req_msg(os, packageName);
        if (x8016_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8016_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8016_req_msg, listener);


    }

    /**
     * 查询最新软件版本
     *
     * @return
     */
    public static MqttPublishRequest create_0x8016_req_msg(String os, String packageName) {


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(C_0x8016.MSGTYPE)
                .setMsgcw("0x07")
                .setContent(new C_0x8016.Req.ContentBean(os, packageName)).create();


        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }


        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }
    /**
     * 查询最新版本
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
            SLog.e(TAG, "查询最新版本成功");
        } else {
            Resp.ContentBean content = resp.getContent();
            Req.ContentBean errcontent = content.getErrcontent();
            content.setAppid(errcontent.getAppid());
            content.setOs(errcontent.getOs());
            content.setPackageName(errcontent.getPackageName());
            SLog.e(TAG, MSG_DESC+" 失败 "+resp.getContent().getErrmsg());
        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onGetLatestVersionResult(resp);

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

            private String os;
            private String packageName;
            private String appid = MqttConfigure.appid;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "os='" + os + '\'' +
                        ", packageName='" + packageName + '\'' +
                        ", appid='" + appid + '\'' +
                        '}';
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public ContentBean() {
            }

            public ContentBean(String os, String packageName) {
                this.os = os;
                this.packageName = packageName;
            }

            public String getOs() {
                return os;
            }

            public void setOs(String os) {
                this.os = os;
            }

            public String getPackageName() {
                return packageName;
            }

            public void setPackageName(String packageName) {
                this.packageName = packageName;
            }
        }


    }

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
             * os : android
             * versionName : 1.0
             * versionCode : 1
             * packageName :
             * updateUrl : http:// /fid
             * hash :
             * updateLog :
             * forcedUpdate : 1
             * fileName : 文件名
             */
            private String appid;
            private String os;
            private String versionName;
            private int versionCode;
            private String packageName;
            private String updateUrl;
            private String hash;
            private String updateLog;
            private int forcedUpdate;
            private String fileName;
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", appid='" + appid + '\'' +
                        ", os='" + os + '\'' +
                        ", versionName='" + versionName + '\'' +
                        ", versionCode=" + versionCode +
                        ", packageName='" + packageName + '\'' +
                        ", updateUrl='" + updateUrl + '\'' +
                        ", hash='" + hash + '\'' +
                        ", updateLog='" + updateLog + '\'' +
                        ", forcedUpdate=" + forcedUpdate +
                        ", fileName='" + fileName + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public String getAppid() {

                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public String getOs() {
                return os;
            }

            public void setOs(String os) {
                this.os = os;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getPackageName() {
                return packageName;
            }

            public void setPackageName(String packageName) {
                this.packageName = packageName;
            }

            public String getUpdateUrl() {
                return updateUrl;
            }

            public void setUpdateUrl(String updateUrl) {
                this.updateUrl = updateUrl;
            }

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public String getUpdateLog() {
                return updateLog;
            }

            public void setUpdateLog(String updateLog) {
                this.updateLog = updateLog;
            }

            public int getForcedUpdate() {
                return forcedUpdate;
            }

            public void setForcedUpdate(int forcedUpdate) {
                this.forcedUpdate = forcedUpdate;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

        }


    }


}
