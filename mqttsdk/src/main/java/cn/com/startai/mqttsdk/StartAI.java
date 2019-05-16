package cn.com.startai.mqttsdk;

import android.content.Context;

import cn.com.startai.mqttsdk.busi.BaseBusiManager;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.listener.IOnSubscribeListener;
import cn.com.startai.mqttsdk.mqtt.MqttInitParam;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.DeviceInfoManager;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * sdk主入口
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class StartAI implements IPersisitentNet {
    private static StartAI instance;
    public static String TAG = "StartAI";


    public static StartAI getInstance() {
        if (instance == null) {
            synchronized (StartAI.class) {
                if (instance == null) {
                    SLog.d(TAG, "sdk version = " + BuildConfig.VERSION_NAME);
                    instance = new StartAI();
                }
            }
        }
        return instance;
    }

    private static Context context;

    public boolean isInit() {

        return getPersisitnet().isInit();
    }

    public static Context getContext() {
        if (context == null) {
            SLog.e(TAG, "please init sdk first");
            return null;
        } else {
            return StartAI.context;
        }
    }

    @Override
    public void initialization(Context context, MqttInitParam config) {
        StartAI.context = context;
        getPersisitnet().initialization(context, config);
    }


    public DeviceInfoManager getDeviceInfoManager() {
        return DeviceInfoManager.getInstance();
    }


    public BaseBusiManager getBaseBusiManager() {
        return BaseBusiManager.getInstance();
    }


    @Override
    public PersistentConnectState getConnectState() {
        return getPersisitnet().getConnectState();
    }


    @Override
    public void subscribe(String topic, IOnSubscribeListener listener) {
        getPersisitnet().subscribe(topic, listener);
    }

    @Override
    public void unSubscribe(String topic, IOnSubscribeListener listener) {
        getPersisitnet().unSubscribe(topic, listener);
    }

    @Override
    public void send(MqttPublishRequest reqStartaiMsg, IOnCallListener listener) {
        getPersisitnet().send(reqStartaiMsg, listener);
    }

    @Override
    public void unInit() {

        getPersisitnet().unInit();

    }

    public StartaiMqttPersistent getPersisitnet() {
        return StartaiMqttPersistent.getInstance();
    }


}
