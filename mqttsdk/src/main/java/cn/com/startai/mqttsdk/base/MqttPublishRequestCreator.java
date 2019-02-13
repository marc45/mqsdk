package cn.com.startai.mqttsdk.base;


import android.text.TextUtils;
import android.view.KeyEvent;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.busi.entity.C_0x8000;
import cn.com.startai.mqttsdk.busi.entity.C_0x8001;
import cn.com.startai.mqttsdk.busi.entity.C_0x8002;
import cn.com.startai.mqttsdk.busi.entity.C_0x8003;
import cn.com.startai.mqttsdk.busi.entity.C_0x8004;
import cn.com.startai.mqttsdk.busi.entity.C_0x8005;
import cn.com.startai.mqttsdk.busi.entity.C_0x8015;
import cn.com.startai.mqttsdk.busi.entity.C_0x8016;
import cn.com.startai.mqttsdk.busi.entity.C_0x8017;
import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.busi.entity.C_0x8019;
import cn.com.startai.mqttsdk.busi.entity.C_0x8020;
import cn.com.startai.mqttsdk.busi.entity.C_0x8021;
import cn.com.startai.mqttsdk.busi.entity.C_0x8022;
import cn.com.startai.mqttsdk.busi.entity.C_0x8023;
import cn.com.startai.mqttsdk.busi.entity.C_0x8024;
import cn.com.startai.mqttsdk.busi.entity.C_0x8025;
import cn.com.startai.mqttsdk.busi.entity.C_0x8026;
import cn.com.startai.mqttsdk.busi.entity.C_0x9998;
import cn.com.startai.mqttsdk.busi.entity.C_0x9999;
import cn.com.startai.mqttsdk.control.SPController;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.control.entity.AreaLocation;
import cn.com.startai.mqttsdk.localbusi.UserBusi;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.DeviceInfoManager;
import cn.com.startai.mqttsdk.utils.ProductConsts;
import cn.com.startai.mqttsdk.utils.SLog;
import cn.com.startai.mqttsdk.utils.SRegexUtil;

/**
 * Created by Robin on 2018/5/11.
 * qq: 419109715 彬影
 */

public class MqttPublishRequestCreator {

    private static final String TAG = MqttPublishRequestCreator.class.getSimpleName();

    /**
     * 组查询ip组
     *
     * @param outterIp
     * @return
     */
    public static MqttPublishRequest create_0x8000_req_msg(String outterIp) {


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8000")
                .setMsgcw("0x07")
                .setDomain(MqttConfigure.domain)
                .setContent(new C_0x8000.Req.ContentBean(outterIp)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.qos = 1;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();

        return mqttPublishRequest;

    }


    /**
     * 组发送连接事件包
     *
     * @return
     */
    public static MqttPublishRequest create_0x9998_req_msg() {

        C_0x9998.Req.ContentBean contentBean = new C_0x9998.Req.ContentBean();
        contentBean.setSn(MqttConfigure.getSn(StartAI.getContext()));
        AreaLocation location = SPController.getLocation();
        if (location != null) {
            contentBean.setIpaddress(location.getQuery());
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x9998")
                .setMsgcw("0x12")
                .setFromid(MqttConfigure.getSn(StartAI.getContext()))
                .setToid(MqttConfigure.appid)
                .setAppid(null)
                .setContent(contentBean).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.retain = false;
        mqttPublishRequest.topic = TopicConsts.getWillTopic();

        return mqttPublishRequest;

    }

    /**
     * 组发送连接断开事件包
     *
     * @return
     */
    public static MqttPublishRequest create_0x9999_req_msg() {

        C_0x9999.Req.ContentBean contentBean = new C_0x9999.Req.ContentBean();
        contentBean.setSn(MqttConfigure.getSn(StartAI.getContext()));
        contentBean.setReason("Connect Lost");

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x9999")
                .setMsgcw("0x12")
                .setAppid(null)
                .setFromid(MqttConfigure.getSn(StartAI.getContext()))
                .setToid(MqttConfigure.appid)
                .setContent(contentBean).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.retain = false;
        mqttPublishRequest.topic = TopicConsts.getWillTopic();

        return mqttPublishRequest;

    }



    /**
     * 消息透传
     *
     * @param toid 消息接收方的userid或sn
     * @param data 透传的数据
     * @return
     */
    public static MqttPublishRequest create_0x8200_req_msg(String toid, String data) {

        if (TextUtils.isEmpty(toid) || TextUtils.isEmpty(data)) {
            SLog.e(TAG, "参数非法 对方id为空 或 数据包为空");
            return null;
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8200")
                .setMsgcw("0x01")
                .setToid(toid)
                .setContent(data).create();

        if (!DistributeParam.PASSTHROUGH_DISTRIBUTE) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.Q_CLIENT + "/" + toid;
        return mqttPublishRequest;

    }









    /**
     * 组设备激活包
     *
     * @return
     */
    public static MqttPublishRequest create_0x8001_req_msg(C_0x8001.Req.ContentBean contentBean) {
        if (contentBean == null) {
            contentBean = new C_0x8001.Req.ContentBean();
            contentBean.setAppid(MqttConfigure.appid);
            contentBean.setApptype(MqttConfigure.apptype);
            contentBean.setDomain(MqttConfigure.domain);
            contentBean.setClientid(MqttConfigure.clientid);
            contentBean.setSn(MqttConfigure.getSn(StartAI.getContext()));

            DeviceInfoManager instance = DeviceInfoManager.getInstance();
            contentBean.setM_ver(MqttConfigure.m_ver);
            C_0x8001.Req.ContentBean.FirmwareParamBean firmwareParamBean = new C_0x8001.Req.ContentBean.FirmwareParamBean();
            firmwareParamBean.setImei(instance.getImei(StartAI.getContext()));
            firmwareParamBean.setFirmwareVersion(instance.getFirmwareVersion());
            firmwareParamBean.setINetMac(instance.getInetMac());
            firmwareParamBean.setWifiMac(instance.getWifiMac(StartAI.getContext()));
            firmwareParamBean.setINetMac(instance.getInetMac());
            firmwareParamBean.setScreenSize(instance.getScreenSize(StartAI.getContext()));
            firmwareParamBean.setSysVersion(instance.getSysVersion());
            firmwareParamBean.setProduct(ProductConsts.getProduct());
            firmwareParamBean.setCpuSerial(instance.getCpuSerial());
            firmwareParamBean.setAndroidId(instance.getAndroidId(StartAI.getContext()));
            firmwareParamBean.setBluetoothMac(instance.getBluetoothMac());
            firmwareParamBean.setModelNumber(instance.getModel());
            contentBean.setFirmwareParam(firmwareParamBean);
        } else {

            if (contentBean.getActivateType() == 0) {
                contentBean.setActivateType(2);
            }

        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8001")
                .setMsgcw("0x07")
                .setAppid(MqttConfigure.appid)
                .setFromid(MqttConfigure.getSn(StartAI.getContext()))
                .setDomain(MqttConfigure.domain)
                .setContent(contentBean).create();



        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }

}
