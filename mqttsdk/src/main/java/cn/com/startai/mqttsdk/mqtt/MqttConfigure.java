package cn.com.startai.mqttsdk.mqtt;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.DeviceInfoManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * sdk 初始化相关参数
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class MqttConfigure {

    private static final String TAG = MqttConfigure.class.getSimpleName();

    private static String sn;
    public static String domain;
    public static String apptype;
    public static String appid;
    public static String clientid;
    public static String m_ver = "Json_1.6.0_9.2.19";
    public static ArrayList<String> mqttHosts = new ArrayList<>();
    public static boolean isCheckRootCrt = true;
    public static final String DEFAULT_ROOTCRT = "startai/startai.bks";
    public static String mqusername = "jfeo39nvoire90rjf9fer9303ugrg";
    public static String mqpassword = "fjfier3mkdf83rfdk9ncju88329328fjjff";
    public static boolean cleanSession = false;
    //    public static boolean isAutoReconnection = true;
    public static int connectTimeOut = 10;// mqtt连接超时时长
    public static int keepAliveInterval = 60;// mqtt的心跳时长，单位秒
    public static int messageSendRetryTimes = 3; // 消息发送失败重试次数默认为3
    public static int changeHostTimeDelay = 500;// 触发节点切换算法的最低时延 ，设置为0即关闭节点自动切换功能
    public static Will will;


    private static final String HOST_CN = "ssl://cn.startai.net:8883";
    private static final String HOST_US = "ssl://us.startai.net:8883";
//    private static final String HOST_TEST = "ssl://192.168.1.189:8883";


    public static ArrayList<String> getHosts() {

        if (mqttHosts == null || mqttHosts.size() == 0) {

            mqttHosts.add(HOST_CN);
            mqttHosts.add(HOST_US);
//            mqttHosts.add(HOST_TEST);
        }
        return mqttHosts;
    }

    public static boolean isSn_16 = true;

    /**
     * @param context
     * @return
     */
    public static String getSn(Context context) {
        if (TextUtils.isEmpty(appid)) {
            SLog.e(TAG, "appid is empty");
            return null;
        }
        long t = System.currentTimeMillis();

        if (TextUtils.isEmpty(sn)) {


            DeviceInfoManager deviceInfoManager = StartAI.getInstance().getDeviceInfoManager();
            sn = deviceInfoManager.getSn_16(deviceInfoManager.getSn(context) + appid);
        }

        SLog.d(TAG, "getSn use time = " + (System.currentTimeMillis() - t) + " sn = " + sn);
        return sn;
    }


    public static Will getWill() {
        if (will == null) {
            MqttPublishRequest x9999_req_msg = MqttPublishRequestCreator.create_0x9999_req_msg();
            String willMsg = SJsonUtils.toJson(x9999_req_msg.message);
            will = new Will(x9999_req_msg.topic, willMsg.getBytes(), 1, x9999_req_msg.retain);
        }
        return will;
    }
}
