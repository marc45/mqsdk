package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.busi.entity.type.Type;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 统一下单
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class C_0x8028 implements Serializable {

    private static final String TAG = C_0x8028.class.getSimpleName();
    public static final String MSGTYPE = "0x8028";
    public static String MSGCW = "0x07";

    /**
     * 请求 统一下单
     *
     * @param listener
     */
    public static void req(Req.ContentBean request, IOnCallListener listener) {

        MqttPublishRequest<StartaiMessage<Req.ContentBean>> req_msg = create_req_msg(request);
        if (req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }

        StartaiMqttPersistent.getInstance().send(req_msg, listener);
    }

    private static MqttPublishRequest<StartaiMessage<Req.ContentBean>> create_req_msg(Req.ContentBean request) {

        if (request == null) {
            SLog.e(TAG, "request  can not be empty");
            return null;
        }

        if (TextUtils.isEmpty(request.order_num)) {
            SLog.e(TAG, "order_num can not be empty");
            return null;
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(MSGTYPE)
                .setMsgcw(MSGCW)
                .setContent(request).create();


        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }


    /**
     * 请求 统一下单 返回结果
     *
     * @param miof
     */
    public static void m_0x8028_resp(String miof) {


        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, "返回数据格式错误");
            return;
        }
        if (resp.getResult() == 1) {
            SLog.e(TAG, "统一下单 成功");

            if (resp.getContent().getPlatform() == Type.ThirdPayment.PLATFOME_WECHAT) {
                //package 是关键字 不能实体映射 需要单独取出
                try {
                    JSONObject jsonObject = new JSONObject(miof);
                    JSONObject content = jsonObject.getJSONObject(MiofTag.TAG_CONTENT);
                    String aPackage = content.getString("package");
                    resp.getContent().setWX_Package_(aPackage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } else {

            Resp.ContentBean content = resp.getContent();
            Req.ContentBean errcontent = content.getErrcontent();
            content.setFee_type(errcontent.getFee_type());
            content.setGoods_discription(errcontent.getGoods_description());
            content.setOrderId(errcontent.getOrder_num());
            content.setType(errcontent.getType());
            content.setTotal_fee(errcontent.getTotal_fee());
            content.setPlatform(errcontent.getPlatform());

            SLog.e(TAG, "统一下单 失败");
        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onThirdPaymentUnifiedOrderResult(resp);
    }

    public static class Req {
        private ContentBean content;

        public Req(ContentBean content) {
            this.content = content;
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean {


            private int type;
            private int platform;
            private String order_num;
            private String goods_description;
            private String fee_type;
            private String total_fee;

            public ContentBean(int type, int platform, String order_num, String goods_description, String fee_type, String total_fee) {
                this.type = type;
                this.platform = platform;
                this.order_num = order_num;
                this.goods_description = goods_description;
                this.fee_type = fee_type;
                this.total_fee = total_fee;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "type=" + type +
                        ", platform=" + platform +
                        ", order_num='" + order_num + '\'' +
                        ", goods_description='" + goods_description + '\'' +
                        ", fee_type='" + fee_type + '\'' +
                        ", total_fee='" + total_fee + '\'' +
                        '}';
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getPlatform() {
                return platform;
            }

            public void setPlatform(int platform) {
                this.platform = platform;
            }

            public String getOrder_num() {
                return order_num;
            }

            public void setOrder_num(String order_num) {
                this.order_num = order_num;
            }

            public String getGoods_description() {
                return goods_description;
            }

            public void setGoods_description(String goods_description) {
                this.goods_description = goods_description;
            }

            public String getFee_type() {
                return fee_type;
            }

            public void setFee_type(String fee_type) {
                this.fee_type = fee_type;
            }

            public String getTotal_fee() {
                return total_fee;
            }

            public void setTotal_fee(String total_fee) {
                this.total_fee = total_fee;
            }
        }


    }


    public static class Resp extends BaseMessage implements Serializable {

        private ContentBean content;

        @Override
        public String toString() {
            return "Resp{" +
                    "content=" + content +
                    ", msgcw='" + msgcw + '\'' +
                    ", msgtype='" + msgtype + '\'' +
                    ", fromid='" + fromid + '\'' +
                    ", toid='" + toid + '\'' +
                    ", domain='" + domain + '\'' +
                    ", appid='" + appid + '\'' +
                    ", ts=" + ts +
                    ", msgid='" + msgid + '\'' +
                    ", m_ver='" + m_ver + '\'' +
                    ", result=" + result +
                    '}';
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends BaseContentBean {


            private int type;
            private int platform;
            private String orderId;
            private String goods_discription;
            private String fee_type;
            private String total_fee;

            private String appid;
            private String partnerid;
            private String prepayid;
            private String package_;
            private String noncestr;
            private String timestamp;
            private String sign;


            private Req.ContentBean errcontent = null;


            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getPlatform() {
                return platform;
            }

            public void setPlatform(int platform) {
                this.platform = platform;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getGoods_discription() {
                return goods_discription;
            }

            public void setGoods_discription(String goods_discription) {
                this.goods_discription = goods_discription;
            }

            public String getFee_type() {
                return fee_type;
            }

            public void setFee_type(String fee_type) {
                this.fee_type = fee_type;
            }

            public String getTotal_fee() {
                return total_fee;
            }

            public void setTotal_fee(String total_fee) {
                this.total_fee = total_fee;
            }

            public String getWX_Appid() {
                return appid;
            }

            public void setWX_Appid(String appid) {
                this.appid = appid;
            }

            public String getWX_Partnerid() {
                return partnerid;
            }

            public void setWX_Partnerid(String partnerid) {
                this.partnerid = partnerid;
            }

            public String getWX_Prepayid() {
                return prepayid;
            }

            public void setWX_Prepayid(String prepayid) {
                this.prepayid = prepayid;
            }

            public String getWX_Package_() {
                return package_;
            }

            public void setWX_Package_(String package_) {
                this.package_ = package_;
            }

            public String getWX_Noncestr() {
                return noncestr;
            }

            public void setWX_Noncestr(String noncestr) {
                this.noncestr = noncestr;
            }

            public String getWX_Timestamp() {
                return timestamp;
            }

            public void setWX_Timestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getWX_Sign() {
                return sign;
            }

            public void setWX_Sign(String sign) {
                this.sign = sign;
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", type=" + type +
                        ", platform=" + platform +
                        ", order_num='" + orderId + '\'' +
                        ", goods_description='" + goods_discription + '\'' +
                        ", fee_type='" + fee_type + '\'' +
                        ", total_fee='" + total_fee + '\'' +
                        ", appid='" + appid + '\'' +
                        ", partnerid='" + partnerid + '\'' +
                        ", prepayid='" + prepayid + '\'' +
                        ", package_='" + package_ + '\'' +
                        ", noncestr='" + noncestr + '\'' +
                        ", timestamp='" + timestamp + '\'' +
                        ", sign='" + sign + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }
        }
    }


}
