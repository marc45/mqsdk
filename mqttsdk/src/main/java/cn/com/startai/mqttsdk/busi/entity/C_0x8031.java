package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

import java.io.Serializable;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.DistributeParam;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 查询支付结果
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class C_0x8031 implements Serializable {

    private static final String TAG = C_0x8031.class.getSimpleName();
    public static final String MSGTYPE = "0x8031";
    public static String MSGCW = "0x07";
    public static String MSG_DESC = "查询支付订单状态 ";

    public static final String TRADE_STATE_SUCCESS = "SUCCESS"; //支付成功
    public static final String TRADE_STATE_REFUND = "REFUND"; //转入退款
    public static final String TRADE_STATE_NOTPAY = "NOTPAY";//未支付
    public static final String TRADE_STATE_CLOSED = "CLOSED";//已关闭
    public static final String TRADE_STATE_REVOKED = "REVOKED";//已撤销（刷卡支付）
    public static final String TRADE_STATE_USERPAYING = "USERPAYING";//用户支付中
    public static final String TRADE_STATE_PAYERROR = "PAYERROR";//支付失败(其他原因，如银行返回失败)
    public static final String TRADE_STATE_ERROR = "ERROR";//错误

    public static final int PLATFORM_WECHAT = 1;
    public static final int PLATFORM_ALIPAY = 2;
    public static final int PLATFORM_SMALL_APP = 3;



    /**
     * 请求 查询支付结果
     *
     * @param listener
     */
    public static void req(String orderNum, IOnCallListener listener) {

        MqttPublishRequest<StartaiMessage<Req.ContentBean>> req_msg = create_req_msg(orderNum);
        if (req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }

        StartaiMqttPersistent.getInstance().send(req_msg, listener);
    }

    private static MqttPublishRequest<StartaiMessage<Req.ContentBean>> create_req_msg(String orderNum) {

        if (TextUtils.isEmpty(orderNum)) {
            SLog.e(TAG, "orderNum can not be empty");
            return null;
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(MSGTYPE)
                .setMsgcw(MSGCW)
                .setContent(new C_0x8031.Req.ContentBean(orderNum)).create();

        if(!DistributeParam.GETREALORDERPAYSTATUS_DISTRIBUTE){
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }


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
    public static void m_resp(String miof) {


        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, MSG_DESC + " 返回格式错误");
            return;
        }
        if (resp.getResult() == 1) {
            SLog.e(TAG, MSG_DESC + " 成功");
            Resp.ContentBean content = resp.getContent();
            content.setOrder_num(content.getOut_trade_no());
        } else {

            Resp.ContentBean content = resp.getContent();
            Req.ContentBean errcontent = content.getErrcontent();
            content.setOrder_num(errcontent.getOrder_num());

            SLog.e(TAG, MSG_DESC + " 失败");
        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onGetRealOrderPayStatusResult(resp);
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


            private String order_num;

            public ContentBean(String order_num) {
                this.order_num = order_num;
            }

            public ContentBean() {
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "order_num='" + order_num + '\'' +
                        '}';
            }

            public String getOrder_num() {
                return order_num;
            }

            public void setOrder_num(String order_num) {
                this.order_num = order_num;
            }

        }


    }


    public static class Resp extends BaseMessage implements Serializable {

        public static final String TRADE_STATE_SUCCESS = "SUCCESS"; //支付成功
        public static final String TRADE_STATE_REFUND = "REFUND"; //转入退款
        public static final String TRADE_STATE_NOTPAY = "NOTPAY";//未支付
        public static final String TRADE_STATE_CLOSED = "CLOSED";//已关闭
        public static final String TRADE_STATE_REVOKED = "REVOKED";//已撤销（刷卡支付）
        public static final String TRADE_STATE_USERPAYING = "USERPAYING";//用户支付中
        public static final String TRADE_STATE_PAYERROR = "PAYERROR";//支付失败(其他原因，如银行返回失败)
        public static final String TRADE_STATE_ERROR = "ERROR";//错误


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


            private Req.ContentBean errcontent = null;
            private String order_num;

            /**
             * platform : 1
             * userid :
             * transaction_id :
             * bank_type : CFT
             * openid :
             * fee_type :
             * out_trade_no :
             * total_fee : 1
             * trade_type : APP
             * time_end :
             * is_subscribe : N
             * cash_fee :
             * coupon_fee :
             * coupon_count :
             * trade_state :
             * trade_state_desc :
             * err_code :
             * err_code_des :
             */

            private int platform;
            private String userid;
            private String transaction_id;
            private String bank_type;
            private String openid;
            private String fee_type;
            private String out_trade_no;
            private String total_fee;
            private String trade_type;
            private String time_end;
            private String is_subscribe;
            private String cash_fee;
            private String coupon_fee;
            private String coupon_count;
            private String trade_state;
            private String trade_state_desc;
            private String err_code;
            private String err_code_des;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", order_num='" + order_num + '\'' +
                        ", platform=" + platform +
                        ", userid='" + userid + '\'' +
                        ", transaction_id='" + transaction_id + '\'' +
                        ", bank_type='" + bank_type + '\'' +
                        ", openid='" + openid + '\'' +
                        ", fee_type='" + fee_type + '\'' +
                        ", out_trade_no='" + out_trade_no + '\'' +
                        ", total_fee='" + total_fee + '\'' +
                        ", trade_type='" + trade_type + '\'' +
                        ", time_end='" + time_end + '\'' +
                        ", is_subscribe='" + is_subscribe + '\'' +
                        ", cash_fee='" + cash_fee + '\'' +
                        ", coupon_fee='" + coupon_fee + '\'' +
                        ", coupon_count='" + coupon_count + '\'' +
                        ", trade_state='" + trade_state + '\'' +
                        ", trade_state_desc='" + trade_state_desc + '\'' +
                        ", err_code='" + err_code + '\'' +
                        ", err_code_des='" + err_code_des + '\'' +
                        '}';
            }

            public String getOrder_num() {
                return order_num;
            }

            public void setOrder_num(String order_num) {
                this.order_num = order_num;
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public int getPlatform() {
                return platform;
            }

            public void setPlatform(int platform) {
                this.platform = platform;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getTransaction_id() {
                return transaction_id;
            }

            public void setTransaction_id(String transaction_id) {
                this.transaction_id = transaction_id;
            }

            public String getBank_type() {
                return bank_type;
            }

            public void setBank_type(String bank_type) {
                this.bank_type = bank_type;
            }

            public String getOpenid() {
                return openid;
            }

            public void setOpenid(String openid) {
                this.openid = openid;
            }

            public String getFee_type() {
                return fee_type;
            }

            public void setFee_type(String fee_type) {
                this.fee_type = fee_type;
            }

            public String getOut_trade_no() {
                return out_trade_no;
            }

            public void setOut_trade_no(String out_trade_no) {
                this.out_trade_no = out_trade_no;
            }

            public String getTotal_fee() {
                return total_fee;
            }

            public void setTotal_fee(String total_fee) {
                this.total_fee = total_fee;
            }

            public String getTrade_type() {
                return trade_type;
            }

            public void setTrade_type(String trade_type) {
                this.trade_type = trade_type;
            }

            public String getTime_end() {
                return time_end;
            }

            public void setTime_end(String time_end) {
                this.time_end = time_end;
            }

            public String getIs_subscribe() {
                return is_subscribe;
            }

            public void setIs_subscribe(String is_subscribe) {
                this.is_subscribe = is_subscribe;
            }

            public String getCash_fee() {
                return cash_fee;
            }

            public void setCash_fee(String cash_fee) {
                this.cash_fee = cash_fee;
            }

            public String getCoupon_fee() {
                return coupon_fee;
            }

            public void setCoupon_fee(String coupon_fee) {
                this.coupon_fee = coupon_fee;
            }

            public String getCoupon_count() {
                return coupon_count;
            }

            public void setCoupon_count(String coupon_count) {
                this.coupon_count = coupon_count;
            }

            public String getTrade_state() {
                return trade_state;
            }

            public void setTrade_state(String trade_state) {
                this.trade_state = trade_state;
            }

            public String getTrade_state_desc() {
                return trade_state_desc;
            }

            public void setTrade_state_desc(String trade_state_desc) {
                this.trade_state_desc = trade_state_desc;
            }

            public String getErr_code() {
                return err_code;
            }

            public void setErr_code(String err_code) {
                this.err_code = err_code;
            }

            public String getErr_code_des() {
                return err_code_des;
            }

            public void setErr_code_des(String err_code_des) {
                this.err_code_des = err_code_des;
            }
        }
    }


}
