package cn.com.startai.mqsdk;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.busi.entity.C_0x8001;
import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.busi.entity.C_0x8028;
import cn.com.startai.mqttsdk.busi.entity.C_0x8035;
import cn.com.startai.mqttsdk.event.AOnStartaiMessageArriveListener;
import cn.com.startai.mqttsdk.event.ICommonStateListener;
import cn.com.startai.mqttsdk.event.PersistentEventDispatcher;
import cn.com.startai.mqttsdk.mqtt.MqttInitParam;

/**
 * Created by Robin on 2018/6/21.
 * qq: 419109715 彬影
 */

public class MyApp extends Application {

//    public static String appid = "7446b4eaf72aafe4fabc5dac3374fcb8";//开发者平台获取 rp8


    public static String appid = "f818c2704026de3c35c5aee06120ff98";//开发者平台获取 wifi插座


    private static String TAG = MyApp.class.getSimpleName();

    private static Context context;

    public static Context getContext() {

        return MyApp.context;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;


        Stetho.initializeWithDefaults(this);





        PersistentEventDispatcher.getInstance().registerOnPushListener(new AOnStartaiMessageArriveListener() {
            @Override
            public void onCommand(String topic, String msg) {

            }


            @Override
            public void onGetWeatherInfoResult(C_0x8035.Resp resp) {
                super.onGetWeatherInfoResult(resp);
                TAndL.TL(getApplicationContext(), resp.toString());
            }

            @Override
            public void onThirdPaymentUnifiedOrderResult(C_0x8028.Resp resp) {
                super.onThirdPaymentUnifiedOrderResult(resp);
                TAndL.TL(getApplicationContext(), resp + "");
            }

            @Override
            public void onActiviteResult(C_0x8001.Resp resp) {
                super.onActiviteResult(resp);
                if (resp.getResult() == resp.RESULT_SUCCESS) {
                    //激活成功
                } else {

                }
            }
        });

        StartAI.getInstance().getPersisitnet().getEventDispatcher().registerOnTunnelStateListener(new ICommonStateListener() {
            /**
             * 登录 tokent 失效
             *
             * @param resp
             */
            @Override
            public void onTokenExpire(C_0x8018.Resp.ContentBean resp) {
                TAndL.TL(getApplicationContext(), "token过期，" + resp);
            }

            @Override
            public void onConnectFail(int errorCode, String errorMsg) {
                TAndL.TL(getApplicationContext(), "连接失败，" + "errcoe = " + errorCode + "errmsg = " + errorMsg);
            }

            @Override
            public void onConnected() {
                TAndL.TL(getApplicationContext(), "连接成功");
            }

            @Override
            public void onDisconnect(int errorCode, String errorMsg) {
                TAndL.TL(getApplicationContext(), "连接断开 " + "errcoe = " + errorCode + "errmsg = " + errorMsg);
            }

        });


    }


}


