package cn.com.startai.mqttsdk.listener;


import cn.com.startai.mqttsdk.base.StartaiError;

public interface IOnSubscribeListener {
    void onSuccess(String topic);

    void onFailed(String topic, StartaiError error);


}