package cn.com.startai.mqsdk.listener;

import cn.com.startai.mqsdk.MyApp;
import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.SJsonUtils;

/**
 * Created by Robin on 2018/7/10.
 * qq: 419109715 彬影
 */

public class MySendListener implements IOnCallListener {
    @Override
    public void onSuccess(MqttPublishRequest request) {

        TAndL.TL(MyApp.getContext(), "消息发送成功");

        logFile(request, null);
    }

    @Override
    public void onFailed(MqttPublishRequest request, StartaiError startaiError) {
        TAndL.TL(MyApp.getContext(), "消息发送失败 " + startaiError.getErrorMsg());

        logFile(request, startaiError);
    }

    @Override
    public boolean needUISafety() {
        return true;
    }

    private void logFile(MqttPublishRequest request, StartaiError startaiError) {
        String msg = "";
        if (request != null) {
            Object message = request.message;
            msg = message.toString();
            if (message instanceof StartaiMessage) {
                StartaiMessage startaiMessage = (StartaiMessage) message;
                msg = SJsonUtils.toJson(startaiMessage);
            }
        }

        if (startaiError == null) {
            TAndL.file(new String[]{"direction", "topic", "msg"}, new String[]{"mq send", request.topic, msg});
        } else {
            TAndL.file(new String[]{"direction", "error", "msg"}, new String[]{"消息发送失败", startaiError.getErrorMsg(), msg});
        }
    }

}
