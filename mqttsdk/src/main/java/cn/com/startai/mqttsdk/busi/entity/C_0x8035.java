package cn.com.startai.mqttsdk.busi.entity;

import java.io.Serializable;

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
 * 查询天气
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class C_0x8035 implements Serializable {

    private static final String TAG = C_0x8035.class.getSimpleName();
    public static final String MSGTYPE = "0x8035";
    public static String MSGCW = "0x07";
    public static String MSG_DESC = "查询天气 ";


    /**
     * 请求 查询天气
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
     * 请求 查询天气 返回结果
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
            C_0x8035.Resp.ContentBean content = resp.getContent();
            C_0x8035.Req.ContentBean errcontent = content.getErrcontent();
            content.setLat(errcontent.getLat());
            content.setLng(errcontent.getLng());
            SLog.e(TAG, MSG_DESC+" 失败 "+resp.getContent().getErrmsg());
        }

        StartAI.getInstance().getPersisitnet().getEventDispatcher().onGetWeatherInfoResult(resp);

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
             * lat :
             * lng :
             */

            private String lat;
            private String lng;

            public ContentBean() {
            }

            public ContentBean(String lat, String lng) {
                this.lat = lat;
                this.lng = lng;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "lat='" + lat + '\'' +
                        ", lng='" + lng + '\'' +
                        '}';
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
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
             * lat :
             * lng :
             * province : 广东省
             * city : 广州市
             * district : 天河区
             * qlty : 优
             * tmp : 14
             * weather :
             * weatherPic :
             */

            private String lat;
            private String lng;
            private String province;
            private String city;
            private String district;
            private String qlty;
            private String tmp;
            private String weather;
            private String weatherPic;
            private String pubtime; // 天气发布时间

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", lat='" + lat + '\'' +
                        ", lng='" + lng + '\'' +
                        ", province='" + province + '\'' +
                        ", city='" + city + '\'' +
                        ", district='" + district + '\'' +
                        ", qlty='" + qlty + '\'' +
                        ", tmp='" + tmp + '\'' +
                        ", weather='" + weather + '\'' +
                        ", weatherPic='" + weatherPic + '\'' +
                        ", pubtime='" + pubtime + '\'' +
                        '}';
            }

            public String getPubtime() {
                return pubtime;
            }

            public void setPubtime(String pubtime) {
                this.pubtime = pubtime;
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getQlty() {
                return qlty;
            }

            public void setQlty(String qlty) {
                this.qlty = qlty;
            }

            public String getTmp() {
                return tmp;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public String getWeatherPic() {
                return weatherPic;
            }

            public void setWeatherPic(String weatherPic) {
                this.weatherPic = weatherPic;
            }
        }
    }


}
