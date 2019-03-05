package cn.com.startai.mqttsdk.event;

public interface IOnMessageArriveListener {

    /**
     * 返回的原始消息
     *
     * @param topic
     * @param msg
     */
    void onCommand(String topic, String msg);


}
