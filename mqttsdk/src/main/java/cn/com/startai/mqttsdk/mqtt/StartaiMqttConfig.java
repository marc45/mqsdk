package cn.com.startai.mqttsdk.mqtt;

import android.content.Context;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.security.KeyStore;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Robin on 2018/5/9.
 * qq: 419109715 彬影
 */

public class StartaiMqttConfig {

    /**
     * 连接参数
     *
     * @return
     */
    public static MqttConnectOptions getConnectOptions(Context context) {


        MqttConnectOptions options = new MqttConnectOptions();

        options.setCleanSession(MqttConfigure.cleanSession);
        options.setUserName(MqttConfigure.mqusername);
        options.setPassword(MqttConfigure.mqpassword.toCharArray());
        options.setKeepAliveInterval(MqttConfigure.keepAliveInterval);
        options.setConnectionTimeout(MqttConfigure.connectTimeOut);
        //mqsdk已经处理自动重连
//        options.setAutomaticReconnect(MqttConfigure.isAutoReconnection);


        Will will = MqttConfigure.getWill(context);
        if (will != null) {
            options.setWill(will.getTopic(), will.getPlayload(), will.getQos(), will.getRetains());
        }

        boolean checkCrt = MqttConfigure.isCheckRootCrt;
        if (checkCrt) {
            try {
                options.setSocketFactory(getSocketFactory(context, MqttConfigure.DEFAULT_ROOTCRT));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return options;
    }


    private static SocketFactory getSocketFactory(Context ctx, String assestPath) throws Exception {
        SSLContext context;
        KeyStore ts = KeyStore.getInstance("BKS");
        ts.load(ctx.getResources().getAssets().open(assestPath),
                "Qixing123".toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance("X509");
        tmf.init(ts);
        TrustManager[] tm = tmf.getTrustManagers();
        context = SSLContext.getInstance("SSL");
        context.init(null, tm, null);
        // SocketFactory factory= SSLSocketFactory.getDefault();
        // Socket socket =factory.createSocket("localhost", 10000);
        SocketFactory factory = context.getSocketFactory();
        return factory;
    }

}
