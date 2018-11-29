package cn.com.startai.mqttsdk.control;

import java.io.Serializable;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;


/**
 * 名称：
 * 描述：
 * Created by Robin on 2016-7-6
 * QQ 419109715 彬影
 */
public class TopicConsts implements Serializable {

    public static final String SERVICE_TOPIC = "SERVICE/";
    public static final String CLOUD_TOID = "Cloud/BXTM";

    public static final String Q_WILL = "Q/client/will";
    public static final String Q_APPTYPE = "Q/apptype";
    public static final String Q_CLIENT = "Q/client";
    public static final String EXT_A = "-A";
    public static final String Q_NODE = "Q/node";
    public static final String Q_AREA = "Q/area";
    public static final String NMC_TOPIC = "SERVICE/NMC";
    public static final String CDN_TOPIC = "SERVICE/CDN";
    public static final String ALARM_TOPIC = "alarm";
    public static final String DISTRIBUTE = "distribute";


    private TopicConsts() {

    }

    public static String getWillTopic() {
        return Q_WILL + "/" + MqttConfigure.appid + "/" + MqttConfigure.getSn(StartAI.getContext());
    }

    public static String getServiceTopic() {
        return NMC_TOPIC + "/" + MqttConfigure.appid;
    }

    /**
     * 订阅对端的消息上报主题
     *
     * @param friendSn
     * @return
     */
    public static String getSubFriendWillTopic(String friendSn) {

        return Q_WILL + "/+/" + friendSn;
    }

    /**
     * 订阅对端的遗嘱主题
     *
     * @param friendSn
     * @return
     */
    public static String getSubFriendReportTopic(String friendSn) {

        return Q_CLIENT + "/" + friendSn + EXT_A;

    }
}
