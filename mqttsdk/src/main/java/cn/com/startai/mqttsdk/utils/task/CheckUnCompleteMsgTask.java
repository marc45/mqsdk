package cn.com.startai.mqttsdk.utils.task;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.TimerTask;

import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.busi.entity.C_0x8001;
import cn.com.startai.mqttsdk.control.SDBmanager;
import cn.com.startai.mqttsdk.control.SPController;
import cn.com.startai.mqttsdk.control.entity.MsgWillSendBean;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;
import cn.com.startai.mqttsdk.utils.STimerUtil;

/**
 * 检查是否有未发送成功的消息
 * Created by Robin on 2018/6/20.
 * qq: 419109715 彬影
 */

public class CheckUnCompleteMsgTask extends AsyncTask {
    private String TAG = CheckUnCompleteMsgTask.class.getSimpleName();

    @Override
    protected Object doInBackground(Object[] objects) {


        ArrayList<MsgWillSendBean> allMsgWillSend = SDBmanager.getInstance().getAllMsgWillSend();

        for (MsgWillSendBean msgWillSendBean : allMsgWillSend) {
            if (msgWillSendBean != null) {
                SLog.d(TAG, "找到一条待发送的消息 " + msgWillSendBean);
                MqttPublishRequest request = new MqttPublishRequest();
                request.topic = msgWillSendBean.getToid();
                request.message = SJsonUtils.fromJson(msgWillSendBean.getMsgWillSend(), StartaiMessage.class);

                StartaiMqttPersistent.getInstance().sendMessage(request, null);

            }
        }

        return null;
    }
}
