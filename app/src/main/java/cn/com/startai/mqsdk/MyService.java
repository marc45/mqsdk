package cn.com.startai.mqsdk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.Utils;

import org.greenrobot.eventbus.EventBus;

import cn.com.startai.mqsdk.listener.MyPushListener;
import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqsdk.util.eventbus.E_0x8001_Resp;
import cn.com.startai.mqsdk.util.eventbus.EventBean;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.busi.entity.C_0x8200;
import cn.com.startai.mqttsdk.event.AOnStartaiMessageArriveListener;
import cn.com.startai.mqttsdk.event.IConnectionStateListener;
import cn.com.startai.mqttsdk.event.PersistentEventDispatcher;
import cn.com.startai.mqttsdk.mqtt.MqttInitParam;

/**
 * Created by Robin on 2018/6/22.
 * qq: 419109715 彬影
 */

public class MyService extends Service {


    private String TAG = MyService.class.getSimpleName();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        initMqSdk();

        return super.onStartCommand(intent, flags, startId);


    }

    private void initMqSdk() {




    }


}
