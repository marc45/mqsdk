package cn.com.startai.mqsdk;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.facebook.stetho.Stetho;

import org.greenrobot.eventbus.EventBus;

import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqsdk.util.eventbus.E_Conn_Break;
import cn.com.startai.mqsdk.util.eventbus.E_Conn_Failed;
import cn.com.startai.mqsdk.util.eventbus.E_Conn_Success;
import cn.com.startai.mqsdk.util.eventbus.EventBean;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.event.ICommonStateListener;
import cn.com.startai.mqttsdk.mqtt.MqttInitParam;

/**
 * Created by Robin on 2018/6/21.
 * qq: 419109715 彬影
 */

public class MyApp extends Application {

//    public static String appid = "7446b4eaf72aafe4fabc5dac3374fcb8";//开发者平台获取 rp8


    public static String appid = "f818c2704026de3c35c5aee06120ff98";//开发者平台获取 wifi插座

//    public static String appid = "abceabceabceabce1111111111111111";


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

//        startService(new Intent(this, MyService.class));

        //工具类初始化
        Utils.init(getApplicationContext());


        MqttInitParam initParam = new MqttInitParam(appid);

        StartAI.getInstance().initialization(getApplicationContext(), initParam);
//        StartAI.getInstance().getPersisitnet().setBusiHandler(new ChargerBusiHandler());
//        StartAI.getInstance().getPersisitnet().setEventDispatcher(PersistentEventChargerDispatcher.getInstance());


        StartAI.getInstance().getPersisitnet().getEventDispatcher().registerOnTunnelStateListener(new ICommonStateListener() {
            /**
             * 登录 tokent 失效
             *
             * @param resp
             */
            @Override
            public void onTokenExpire(C_0x8018.Resp.ContentBean resp) {
                TAndL.TL(getApplicationContext(), "token过期，" + resp);
                EventBus.getDefault().post(new EventBean(EventBean.S_2_A_TOKEN_EXPIRE, resp));
            }

            @Override
            public void onConnectFail(int errorCode, String errorMsg) {
                TAndL.TL(getApplicationContext(), "连接失败，" + "errcoe = " + errorCode + "errmsg = " + errorMsg);
                EventBus.getDefault().post(new EventBean(EventBean.S_2_A_CONN_FAILED, new E_Conn_Failed(errorCode, errorMsg)));
            }

            @Override
            public void onConnected() {
                TAndL.TL(getApplicationContext(), "连接成功");
                EventBus.getDefault().post(new EventBean(EventBean.S_2_A_CONN_SUCCESS, new E_Conn_Success()));
            }

            @Override
            public void onDisconnect(int errorCode, String errorMsg) {
                TAndL.TL(getApplicationContext(), "连接断开 " + "errcoe = " + errorCode + "errmsg = " + errorMsg);
                EventBus.getDefault().post(new EventBean(EventBean.S_2_A_CONN_BREAK, new E_Conn_Break(errorCode, errorMsg)));
            }

            @Override
            public boolean needUISafety() {
                return true;
            }
        });


    }


}


