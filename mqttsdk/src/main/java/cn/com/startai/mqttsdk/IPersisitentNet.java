package cn.com.startai.mqttsdk;

import android.content.Context;

import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.listener.IOnSubscribeListener;
import cn.com.startai.mqttsdk.mqtt.MqttInitParam;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;

/**
 * Created by Robin on 2018/5/3.
 * qq: 419109715 彬影
 */
public interface IPersisitentNet {


    PersistentConnectState getConnectState();

//    void subscribeAsync(String topic, IOnSubscribeListener listener);
//
//    void unSubscribeAsync(String topic, IOnSubscribeListener listener);

//    /**
//     * 发送基础消息
//     *
//     * @param message  消息内容
//     * @param listener 发送结果回调
//     */
//    void sendAsync(MqttPublishRequest message, IOnCallListener listener);

    void subscribe(String topic, IOnSubscribeListener listener);

    void unSubscribe(String topic, IOnSubscribeListener listener);

    /**
     * 发送基础消息
     *
     * @param message  消息内容
     * @param listener 发送结果回调
     */
    void send(MqttPublishRequest message, IOnCallListener listener);

    void initialization(Context context, MqttInitParam params);

    void unInit();

}

