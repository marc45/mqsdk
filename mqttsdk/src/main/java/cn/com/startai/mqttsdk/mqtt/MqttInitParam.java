package cn.com.startai.mqttsdk.mqtt;

/**
 * sdk 初始化相关参数
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class MqttInitParam {


    public String domain;
    public String apptype;
    public String appid;
    public String m_ver;

    public MqttInitParam() {
    }

    public MqttInitParam(String domain, String apptype, String appid, String m_ver) {
        this.domain = domain;
        this.apptype = apptype;
        this.appid = appid;
        this.m_ver = m_ver;
    }
}
