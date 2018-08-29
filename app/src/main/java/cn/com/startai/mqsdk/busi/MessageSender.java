package cn.com.startai.mqsdk.busi;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;

/**
 * Created by Robin on 2018/6/29.
 * qq: 419109715 彬影
 */

public class MessageSender {

    public static void sendMessage(MqttPublishRequest request, IOnCallListener callListener) {

        StartAI.getInstance().send(request, callListener);
    }

    private static String getBackMsgcw(String msgcw) {
        return msgcw.equals("0x01") ? "0x02" : (msgcw.equals("0x03") ? "0x04" : (msgcw.equals("0x05") ? "0x06" : (msgcw.equals("0x07") ? "0x08" : (msgcw.equals("0x09") ? "0x10" : ""))));
    }


}
