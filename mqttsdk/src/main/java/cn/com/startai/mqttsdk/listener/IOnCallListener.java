package cn.com.startai.mqttsdk.listener;



import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;

public interface IOnCallListener {

    /**
     * 消息发送成功
     *
     * @param request
     */
    void onSuccess(MqttPublishRequest request);

    void onFailed(MqttPublishRequest request, StartaiError startaiError);

}
