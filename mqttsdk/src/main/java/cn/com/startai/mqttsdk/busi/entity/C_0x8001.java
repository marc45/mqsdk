package cn.com.startai.mqttsdk.busi.entity;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.control.SPController;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**终端激活
 * Created by Robin on 2018/5/10.
 * qq: 419109715 彬影
 */

public class C_0x8001 {

    private static String TAG = C_0x8001.class.getSimpleName();
    public static String MSG_DESC = "终端激活 ";
    public static final String MSGTYPE = "0x8001";
    public static String MSGCW = "0x07";
    /**
     * 终端激活
     * 如果 contentbean 为null表示自己激活  否则为第三方硬件激活
     *
     * @param contentBean
     * @param listener
     */
    public static void m_0x8001_req(C_0x8001.Req.ContentBean contentBean, IOnCallListener listener) {


        MqttPublishRequest x8001_req_msg = MqttPublishRequestCreator.create_0x8001_req_msg(contentBean);
        if (x8001_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8001_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8001_req_msg, listener);

    }

    /**
     * 处理激活返回
     *
     * @param miof   失败实体
     */
    public static void m_0x8001_resp(String miof) {



        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, "返回数据格式错误");
            return;
        }

        if (resp.getResult() == 1) {

            //自己激活成功
            if (MqttConfigure.getSn(StartAI.getContext()).equals(resp.content.getSn())) {
                SPController.setIsActivite(true);
                StartAI.getInstance().getPersisitnet().getEventDispatcher().onActiviteResult(resp);

                StartaiMqttPersistent.getInstance().checkGetAreaNode();

                C_0x9998.m_0x9998_req(null);
                SLog.e(TAG, "激活成功");
            } else {
                //代发的激活包 激活成功
                SLog.e(TAG, "代激活成功");
                StartAI.getInstance().getPersisitnet().getEventDispatcher().onHardwareActivateResult(resp);
            }
        } else {

            String errcode = resp.getContent().getErrcode();
            //自己激活失败
            if ("0x800101".equals(errcode)
                    || "0x800102".equals(errcode)
                    || "0x800103".equals(errcode)
                    || "0x800104".equals(errcode)
                    || "000000".equals(errcode)
                    || "0x800105".equals(errcode)
                    ) {
                SLog.e(TAG, "激活失败");
                StartAI.getInstance().getPersisitnet().getEventDispatcher().onActiviteResult(resp);
            } else {
                SLog.e(TAG, "代激活失败");
                //代发的激活失败
                StartAI.getInstance().getPersisitnet().getEventDispatcher().onHardwareActivateResult(resp);
            }

        }

    }

    public static class Req {

        private ContentBean content;

        public static class ContentBean extends BaseContentBean {

            /*
            终端请求云端注册业务，clientid项为GUID，clientid,sn,apptype, m_ver,appid,domian不能为空
apptype:设备类型
广告屏为smartAd,智能音响为smartBox,智能插座 smartOl,网关为smartGw,智能手机为smartCrtl等
firmwareParam:
为固件参数
activateType:激活类型 如果APP代智能设备激活 值为2 ,自己激活为1 或者不填写该字段
             */

            private String apptype;
            private String domain;
            private String sn;
            private String appid;
            private String clientid;
            private int activateType;
            private String m_ver;

            public int getActivateType() {
                return activateType;
            }

            public void setActivateType(int activateType) {
                this.activateType = activateType;
            }

            private FirmwareParamBean firmwareParam;

            public String getApptype() {
                return apptype;
            }

            public void setApptype(String apptype) {
                this.apptype = apptype;
            }

            public String getDomain() {
                return domain;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public String getClientid() {
                return clientid;
            }

            public void setClientid(String clientid) {
                this.clientid = clientid;
            }

            public String getM_ver() {
                return m_ver;
            }

            public void setM_ver(String m_ver) {
                this.m_ver = m_ver;
            }

            public FirmwareParamBean getFirmwareParam() {
                return firmwareParam;
            }

            public void setFirmwareParam(FirmwareParamBean firmwareParam) {
                this.firmwareParam = firmwareParam;
            }

            public static class FirmwareParamBean {


                private String sysVersion;
                private String product;
                private String firmwareVersion;
                private String iNetMac;
                private String bluetoothMac;
                private String imei;
                private String wifiMac;
                private String screenSize;
                private String cpuSerial;
                private String androidId;
                private String modelNumber;

                @Override
                public String toString() {
                    return "FirmwareParamBean{" +
                            "sysVersion='" + sysVersion + '\'' +
                            ", product='" + product + '\'' +
                            ", firmwareVersion='" + firmwareVersion + '\'' +
                            ", iNetMac='" + iNetMac + '\'' +
                            ", bluetoothMac='" + bluetoothMac + '\'' +
                            ", imei='" + imei + '\'' +
                            ", wifiMac='" + wifiMac + '\'' +
                            ", screenSize='" + screenSize + '\'' +
                            ", cpuSerial='" + cpuSerial + '\'' +
                            ", androidId='" + androidId + '\'' +
                            ", modelNumber='" + modelNumber + '\'' +
                            '}';
                }

                public String getModelNumber() {
                    return modelNumber;
                }

                public void setModelNumber(String modelNumber) {
                    this.modelNumber = modelNumber;
                }

                public String getBluetoothMac() {
                    return bluetoothMac;
                }

                public void setBluetoothMac(String bluetoothMac) {
                    this.bluetoothMac = bluetoothMac;
                }


                public String getAndroidId() {
                    return androidId;
                }

                public void setAndroidId(String androidId) {
                    this.androidId = androidId;
                }

                public String getCpuSerial() {
                    return cpuSerial;
                }

                public void setCpuSerial(String cpuSerial) {
                    this.cpuSerial = cpuSerial;
                }

                public String getSysVersion() {
                    return sysVersion;
                }

                public void setSysVersion(String sysVersion) {
                    this.sysVersion = sysVersion;
                }

                public String getProduct() {
                    return product;
                }

                public void setProduct(String product) {
                    this.product = product;
                }

                public String getFirmwareVersion() {
                    return firmwareVersion;
                }

                public void setFirmwareVersion(String firmwareVersion) {
                    this.firmwareVersion = firmwareVersion;
                }

                public String getINetMac() {
                    return iNetMac;
                }

                public void setINetMac(String iNetMac) {
                    this.iNetMac = iNetMac;
                }

                public String getImei() {
                    return imei;
                }

                public void setImei(String imei) {
                    this.imei = imei;
                }

                public String getWifiMac() {
                    return wifiMac;
                }

                public void setWifiMac(String wifiMac) {
                    this.wifiMac = wifiMac;
                }

                public String getScreenSize() {
                    return screenSize;
                }

                public void setScreenSize(String screenSize) {
                    this.screenSize = screenSize;
                }
            }
        }


    }

    public static class Resp extends BaseMessage {


        private ContentBean content;

        @Override
        public String toString() {
            return "Resp{" +
                    "msgcw='" + msgcw + '\'' +
                    ", msgtype='" + msgtype + '\'' +
                    ", fromid='" + fromid + '\'' +
                    ", toid='" + toid + '\'' +
                    ", domain='" + domain + '\'' +
                    ", appid='" + appid + '\'' +
                    ", ts=" + ts +
                    ", msgid='" + msgid + '\'' +
                    ", m_ver='" + m_ver + '\'' +
                    ", result=" + result +
                    ", content=" + content +
                    '}';
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends BaseContentBean {

            /*
                      终端请求云端注册业务，clientid项为GUID，clientid,sn,apptype, m_ver,appid,domian不能为空
          apptype:设备类型
          广告屏为smartAd,智能音响为smartBox,智能插座 smartOl,网关为smartGw,智能手机为smartCrtl等
          firmwareParam:
          为固件参数
          activateType:激活类型 如果APP代智能设备激活 值为2 ,自己激活为1 或者不填写该字段
                       */
            private String apptype;
            private String domain;
            private String sn;
            private String appid;
            private String clientid;
            private String m_ver;
            private int activateType;
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", apptype='" + apptype + '\'' +
                        ", domain='" + domain + '\'' +
                        ", sn='" + sn + '\'' +
                        ", appid='" + appid + '\'' +
                        ", clientid='" + clientid + '\'' +
                        ", m_ver='" + m_ver + '\'' +
                        ", activateType=" + activateType +
                        ", errcontent=" + errcontent +
                        ", firmwareParam=" + firmwareParam +
                        '}';
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public int getActivateType() {
                return activateType;
            }

            public void setActivateType(int activateType) {
                this.activateType = activateType;
            }

            private FirmwareParamBean firmwareParam;

            public String getApptype() {
                return apptype;
            }

            public void setApptype(String apptype) {
                this.apptype = apptype;
            }

            public String getDomain() {
                return domain;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public String getClientid() {
                return clientid;
            }

            public void setClientid(String clientid) {
                this.clientid = clientid;
            }

            public String getM_ver() {
                return m_ver;
            }

            public void setM_ver(String m_ver) {
                this.m_ver = m_ver;
            }

            public FirmwareParamBean getFirmwareParam() {
                return firmwareParam;
            }

            public void setFirmwareParam(FirmwareParamBean firmwareParam) {
                this.firmwareParam = firmwareParam;
            }

            public static class FirmwareParamBean {
                /**
                 * sysVersion : 4.4.2
                 * product : astar_sc3812r
                 * firmwareVersion : astar_sc3812r-eng 4.4.2
                 * iNetMac : 以太网mac
                 * imei : wfewfwfwfwef
                 * wifiMac : cc:b8:a8:1d:4e:da
                 * screenSize : 1280*720
                 */

                private String sysVersion;
                private String product;
                private String firmwareVersion;
                private String iNetMac;
                private String bluetoothMac;
                private String imei;
                private String wifiMac;
                private String screenSize;
                private String cpuSerial;
                private String androidId;
                private String modelNumber;

                @Override
                public String toString() {
                    return "FirmwareParamBean{" +
                            "sysVersion='" + sysVersion + '\'' +
                            ", product='" + product + '\'' +
                            ", firmwareVersion='" + firmwareVersion + '\'' +
                            ", iNetMac='" + iNetMac + '\'' +
                            ", bluetoothMac='" + bluetoothMac + '\'' +
                            ", imei='" + imei + '\'' +
                            ", wifiMac='" + wifiMac + '\'' +
                            ", screenSize='" + screenSize + '\'' +
                            ", cpuSerial='" + cpuSerial + '\'' +
                            ", androidId='" + androidId + '\'' +
                            ", modelNumber='" + modelNumber + '\'' +
                            '}';
                }

                public String getModelNumber() {
                    return modelNumber;
                }

                public void setModelNumber(String modelNumber) {
                    this.modelNumber = modelNumber;
                }

                public String getBluetoothMac() {
                    return bluetoothMac;
                }

                public void setBluetoothMac(String bluetoothMac) {
                    this.bluetoothMac = bluetoothMac;
                }

                public String getiNetMac() {
                    return iNetMac;
                }

                public void setiNetMac(String iNetMac) {
                    this.iNetMac = iNetMac;
                }

                public String getAndroidId() {
                    return androidId;
                }

                public void setAndroidId(String androidId) {
                    this.androidId = androidId;
                }

                public String getCpuSerial() {
                    return cpuSerial;
                }

                public void setCpuSerial(String cpuSerial) {
                    this.cpuSerial = cpuSerial;
                }

                public String getSysVersion() {
                    return sysVersion;
                }

                public void setSysVersion(String sysVersion) {
                    this.sysVersion = sysVersion;
                }

                public String getProduct() {
                    return product;
                }

                public void setProduct(String product) {
                    this.product = product;
                }

                public String getFirmwareVersion() {
                    return firmwareVersion;
                }

                public void setFirmwareVersion(String firmwareVersion) {
                    this.firmwareVersion = firmwareVersion;
                }

                public String getINetMac() {
                    return iNetMac;
                }

                public void setINetMac(String iNetMac) {
                    this.iNetMac = iNetMac;
                }

                public String getImei() {
                    return imei;
                }

                public void setImei(String imei) {
                    this.imei = imei;
                }

                public String getWifiMac() {
                    return wifiMac;
                }

                public void setWifiMac(String wifiMac) {
                    this.wifiMac = wifiMac;
                }

                public String getScreenSize() {
                    return screenSize;
                }

                public void setScreenSize(String screenSize) {
                    this.screenSize = screenSize;
                }
            }
        }

    }


}
