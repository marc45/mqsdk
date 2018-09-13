package cn.com.startai.mqttsdk.base;


import android.text.TextUtils;

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
        mqttPublishRequest.retain = true;
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
        mqttPublishRequest.retain = true;
        mqttPublishRequest.topic = TopicConsts.getWillTopic();

        return mqttPublishRequest;

    }

    /**
     * 组 修改备注名数据包
     *
     * @return
     */
    public static MqttPublishRequest create_0x8015_req_msg(String uid, String fid, String remark) {


        String userid = uid;
        if (TextUtils.isEmpty(userid)) {

            C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
            if (userBean != null) {
                userid = userBean.getUserid();
            }
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(C_0x8015.MSGTYPE)
                .setMsgcw("0x07")
                .setContent(new C_0x8015.Req.ContentBean(userid, fid, remark)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }

    /**
     * 查询最新软件版本
     *
     * @return
     */
    public static MqttPublishRequest create_0x8016_req_msg(String os, String packageName) {


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(C_0x8016.MSGTYPE)
                .setMsgcw("0x07")
                .setContent(new C_0x8016.Req.ContentBean(os, packageName)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }

    /**
     * 组注册包
     *
     * @param uName
     * @param pwd
     * @return
     */
    public static MqttPublishRequest create_0x8017_req_msg(String uName, String pwd) {

        int type = 0;
        if (SRegexUtil.isEmail(uName)) {
            type = 1;
        } else if (SRegexUtil.isMobileSimple(uName)) {
            type = 2;
        }
        if (type == 0) {
            SLog.e(TAG, "参数非法 类型不对");
            return null;
        }
        if (TextUtils.isEmpty(pwd)) {
            SLog.e(TAG, "参数非法 密码为空");
            return null;
        }
        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8017")
                .setMsgcw("0x07")
                .setFromid(MqttConfigure.getSn(StartAI.getContext()))
                .setContent(new C_0x8017.Req.ContentBean(uName, pwd, type)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }

    /**
     * 组登录包
     *
     * @param uName
     * @param pwd
     * @param identifyCode
     * @return
     */
    public static MqttPublishRequest<StartaiMessage<C_0x8018.Req.ContentBean>> create_0x8018_req_msg(String uName, String pwd, String identifyCode) {
        if (TextUtils.isEmpty(uName)) {

            SLog.e(TAG, "参数非法 uName为空 ");
            return null;
        }

        if (TextUtils.isEmpty(pwd) && TextUtils.isEmpty(identifyCode)) {
            SLog.e(TAG, "参数非法  密码或密码验证码为空");
            return null;
        }


        int type = 0;
        if (SRegexUtil.isEmail(uName)) {
            type = 1;
        } else if (SRegexUtil.isMobileSimple(uName)) {

            if (TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(identifyCode)) {
                type = 3;
            } else if (!TextUtils.isEmpty(pwd) && TextUtils.isEmpty(identifyCode)) {
                type = 2;
            } else if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(identifyCode)) {
                type = 5;
            } else {
                SLog.e(TAG, "参数非法 类型与 uname不匹配 ");
                return null;
            }

        } else if (SRegexUtil.isUsername(uName)) {
            type = 4;
        } else {
            SLog.e(TAG, "参数非法 uname 格式不对");
            return null;
        }

        if (type == 1 || type == 2 || type == 4 || type == 5) {
            if (TextUtils.isEmpty(pwd)) {
                SLog.e(TAG, "参数非法 密码为空 ");
                return null;
            }
        } else if (type == 3) {
            if (TextUtils.isEmpty(identifyCode)) {
                SLog.e(TAG, "参数非法 验证码为空");
                return null;
            }
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8018")
                .setMsgcw("0x07")
                .setFromid(MqttConfigure.getSn(StartAI.getContext()))
                .setContent(new C_0x8018.Req.ContentBean(uName, pwd, identifyCode, type)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }

    /**
     * 组 更新用户信息数据包
     */
    public static MqttPublishRequest create_0x8019_req_msg(C_0x8019.Req.ContentBean contentBean) {

        if (contentBean == null) {
            SLog.e(TAG, "参数非法 参数为空");
            return null;
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8019")
                .setMsgcw("0x07")
                .setContent(contentBean).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }

    /**
     * 组更新用户信息数据包
     */
    public static MqttPublishRequest create_0x8020_req_msg(C_0x8020.Req.ContentBean contentBean) {

        if (contentBean == null) {
            SLog.e(TAG, "参数非法 参数为空");
            return null;
        }
        String userid = "";
        C_0x8018.Resp.ContentBean currUser = new UserBusi().getCurrUser();
        if (currUser != null) {
            userid = currUser.getUserid();
        }
        if (TextUtils.isEmpty(contentBean.getUserid())) {
            contentBean.setUserid(userid);
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8020")
                .setMsgcw("0x07")
                .setContent(contentBean).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }

    /**
     * 组获取验证码包
     *
     * @param mobile 手机号
     * @param type   类型
     *               1表示用户登录
     *               2表示修改登录密码
     *               3表示用户注册
     * @return
     */
    public static MqttPublishRequest<StartaiMessage<C_0x8021.Req.ContentBean>> create_0x8021_req_msg(String mobile, int type) {


        if (TextUtils.isEmpty(mobile) || type == 0) {
            SLog.e(TAG, "参数非法 mobile 为空 或 type 类型不对");
            return null;
        }

        if (!SRegexUtil.isMobileSimple(mobile)) {
            SLog.e(TAG, "参数非法 mobile 格式不对");
            return null;
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8021")
                .setMsgcw("0x07")
                .setFromid(MqttConfigure.getSn(StartAI.getContext()))
                .setContent(new C_0x8021.Req.ContentBean(mobile, type)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }

    /**
     * 组校验验证码包
     *
     * @param mobile       手机号
     * @param identifyCode 验证码
     * @param type         1表示用户登录  2表示修改登录密码 3表示用户注册
     * @return
     */
    public static MqttPublishRequest create_0x8022_req_msg(String mobile, String identifyCode, int type) {

        if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(identifyCode) || type == 0) {
            SLog.e(TAG, "参数非法 mibile 及验证码不能为空 或type类型不对");
            return null;
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8022")
                .setMsgcw("0x07")
                .setFromid(MqttConfigure.getSn(StartAI.getContext()))
                .setContent(new C_0x8022.Req.ContentBean(mobile, identifyCode, type)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }

    /**
     * 发送邮件
     *
     * @param email 邮箱
     * @param type  1 为重新发送激活邮件 2 为发送忘记密码邮件
     * @return
     */
    public static MqttPublishRequest create_0x8023_req_msg(String email, int type) {

        boolean isEmail = SRegexUtil.isEmail(email);
        if (!isEmail || (type != 1 && type != 2)) {
            SLog.e(TAG, "参数非法 email 格式不正确或 type 不匹配");
            return null;
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8023")
                .setMsgcw("0x07")
                .setFromid(MqttConfigure.getSn(StartAI.getContext()))
                .setContent(new C_0x8023.Req.ContentBean(email, type)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }


    /**
     * 组查询用户信息数据包
     *
     * @return
     */
    public static MqttPublishRequest create_0x8024_req_msg(String uid) {

        String userid = uid;
        if (TextUtils.isEmpty(userid)) {

            C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
            if (userBean != null && !TextUtils.isEmpty(userBean.getUserid())) {
                userid = userBean.getUserid();
            }
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8024")
                .setMsgcw("0x07")
                .setContent(new C_0x8024.Req.ContentBean(userid)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }

    /**
     * 组修改密码数据包
     *
     * @return
     */
    public static MqttPublishRequest create_0x8025_req_msg(String uid, String oldPwd, String newPwd) {

        String userid = uid;
        if (TextUtils.isEmpty(userid)) {
            C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
            if (userBean != null && !TextUtils.isEmpty(userBean.getUserid())) {
                userid = userBean.getUserid();
            }
        }

//        if (TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd)) {
//            SLog.e(TAG, "参数非法");
//            return null;
//        }

        if (oldPwd.equals(newPwd)) {
            SLog.e(TAG, "参数非法，新旧密码一致");
            return null;
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8025")
                .setMsgcw("0x07")
                .setContent(new C_0x8025.Req.ContentBean(userid, oldPwd, newPwd)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }


    /**
     * 组 重置手机登录密码 数据包
     *
     * @return
     */
    public static MqttPublishRequest create_0x8026_req_msg(String mobile, String pwd) {


        if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(pwd)) {

            SLog.e(TAG, "参数非法 mobile pwd 不能为空");
            return null;
        }

        if (!SRegexUtil.isMobileSimple(mobile)) {
            SLog.e(TAG, "参数非法 mobile格式不对");
            return null;
        }
        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8026")
                .setMsgcw("0x07")
                .setFromid(MqttConfigure.getSn(StartAI.getContext()) + "/" + MqttConfigure.appid)
                .setContent(new C_0x8026.Req.ContentBean(mobile, pwd)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
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

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.Q_CLIENT + "/" + toid;
        return mqttPublishRequest;

    }

    /**
     * 组添加好友包
     *
     * @param beBindingid 设备的sn
     * @return
     */
    public static MqttPublishRequest<StartaiMessage<C_0x8002.Req.ContentBean>> create_0x8002_req_msg(String userid, String beBindingid) {

        String bindingid = userid;
        if (TextUtils.isEmpty(bindingid)) {

            C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
            if (userBean != null && !TextUtils.isEmpty(userBean.getUserid())) {
                bindingid = userBean.getUserid();
            }
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8002")
                .setMsgcw("0x07")
                .setContent(new C_0x8002.Req.ContentBean(bindingid, beBindingid)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }


    /**
     * 组添注销激活包
     *
     * @return
     */
    public static MqttPublishRequest create_0x8003_req_msg() {


        String sn = MqttConfigure.getSn(StartAI.getContext());

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8003")
                .setMsgcw("0x07")
                .setContent(new C_0x8003.Req.ContentBean(sn)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }

    /**
     * 组删除好友包
     *
     * @param beBindingid 设备的sn
     * @return
     */
    public static MqttPublishRequest create_0x8004_req_msg(String userid, String beBindingid) {

        String bindingid = userid;
        if (TextUtils.isEmpty(bindingid)) {

            C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
            if (userBean != null && !TextUtils.isEmpty(userBean.getUserid())) {
                bindingid = userBean.getUserid();
            }
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8004")
                .setMsgcw("0x07")
                .setContent(new C_0x8004.Req.ContentBean(bindingid, beBindingid)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }

    /**
     * 组获取好友列表好友包
     *
     * @param type 1.查询智能设备列表
     *             2.查询用户好友列表
     *             3.查询用户-手机列表
     *             4.查询所有的好友列表
     * @return
     */
    public static MqttPublishRequest<StartaiMessage<C_0x8005.Req.ContentBean>> create_0x8005_req_msg(String userid, int type) {


        String id = userid;
        if (TextUtils.isEmpty(id)) {

            C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
            if (userBean != null && !TextUtils.isEmpty(userBean.getUserid())) {
                id = userBean.getUserid();
            }

        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8005")
                .setMsgcw("0x07")
                .setContent(new C_0x8005.Req.ContentBean(id, type)).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
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
                .setDomain(MqttConfigure.domain)
                .setContent(contentBean).create();

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }

}
