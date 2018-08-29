package cn.com.startai.mqttsdk.mqtt.request;


public class MqttPublishRequest<RequestType> {
    public boolean retain;
    public String topic;
    public int qos = 1;
    public RequestType message;


    public MqttPublishRequest() {

    }

    public MqttPublishRequest(String topic, int qos, RequestType message) {
        this.topic = topic;
        this.qos = qos;
        this.message = message;
    }

    public MqttPublishRequest(String topic, int qos, boolean retain, RequestType message) {
        this.topic = topic;
        this.qos = qos;
        this.retain = retain;
        this.message = message;
    }
}
