package cn.com.startai.mqttsdk.base;

/**
 * Created by Robin on 2018/5/7.
 * qq: 419109715 彬影
 */

public class StartaiError {
    private int errorCode;
    private String errorMsg;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public StartaiError() {
    }

    public StartaiError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public StartaiError(int errorCode) {
        this.errorCode = errorCode;
        this.errorMsg = getErrorMsgByCode(errorCode);
    }

    public static final int UNKOWN = 3000;

    public static final int ERROR_CONN_NET = 4001;
    public static final int ERROR_CONN_AUTHENTICATION = 4002;
    public static final int ERROR_CONN_CER = 4003;
    public static final int ERROR_CONN_TIMEROUT = 4004;
    public static final int ERROR_CONN_SERVER = 4005;
    public static final int ERROR_CONN_UNKNOW = 4006;

    public static final int ERROR_SEND_CLIENT_DISCONNECT = 5001;
    public static final int ERROR_SEND_TIMEOUT = 5002;
    public static final int ERROR_SEND_UNKOWN = 5003; //发送失败，未知异常
    public static final int ERROR_SEND_NET = 5004;
    public static final int ERROR_SEND_NO_LOGIN = 5006;
    public static final int ERROR_SEND_NO_ACTIVITE = 5007;
    public static final int ERROR_SEND_PARAM_INVALIBLE = 5008;
    public static final int ERROR_SEND_NO_FID = 5009;


    public static final int ERROR_LOST_SAME_CLIENTID = 6001;//帐号在别处登录
    public static final int ERROR_LOST_CLIENT = 6002;
    public static final int ERROR_LOST_TIMEOUT = 6003;
    public static final int ERROR_LOST_HEART_TIMEOUT = 6004;
    public static final int ERROR_LOST_SELF_DISCONNECT = 6005;//用户主动断开
    public static final int ERROR_LOST_NET_UNVALAIBLE = 6006;//断开，网络不可用


    public static final int ERROR_SUB_NULL_TOPIC = 7001;//订阅失败，空主题
    public static final int ERROR_SUB_UNVALIABLE_TOPIC = 7002;//订阅失败，主题格式非法

    public static final int ERROR_UNSUB_NULL_TOPIC = 8001;//取消订阅失败，空主题
    public static final int ERROR_UNSUB_UNVALIABLE_TOPIC = 8002;//取消订阅失败，主题格式非法


    public static String getErrorMsgByCode(int errorCode) {

        String errorMsg = "";

        switch (errorCode) {
            case ERROR_SEND_NET:
                errorMsg = "消息发送失败，终端网络异常";
                break;
            case ERROR_LOST_SAME_CLIENTID:
                errorMsg = "账号在别处登录";
                break;
            case ERROR_LOST_SELF_DISCONNECT:
                errorMsg = "主动断开连接";
                break;
            case ERROR_SEND_NO_LOGIN:
                errorMsg = "消息发送失败，未登录";
                break;
            case ERROR_SUB_NULL_TOPIC:
                errorMsg = "订阅失败，空主题";
                break;
            case ERROR_UNSUB_NULL_TOPIC:
                errorMsg = "取消订阅失败，空主题";
                break;
            case ERROR_SEND_NO_ACTIVITE:

                errorMsg = "消息发送失败，终端未激活";
                break;
            case ERROR_SEND_PARAM_INVALIBLE:

                errorMsg = "消息发送失败，参数非法";
                break;
            case ERROR_LOST_NET_UNVALAIBLE:
                errorMsg = "连接断开，网络不可用";
                break;
            case ERROR_SEND_NO_FID:
                errorMsg = "消息发送失败，不存在此好友";
                break;
            case ERROR_SEND_UNKOWN:

                errorMsg = "消息发送失败，未知异常";
                break;

            case ERROR_SEND_TIMEOUT:

                errorMsg = "消息发送失败，超时";

                break;
            case ERROR_SEND_CLIENT_DISCONNECT:

                errorMsg = "消息发送失败，发送失败，未连接";
                break;
            case ERROR_CONN_SERVER:
                errorMsg = "连接失败，服务器异常";
                break;
            case ERROR_CONN_TIMEROUT:
                errorMsg = "连接失败，超时";
                break;
            case ERROR_CONN_CER:
                errorMsg = "连接失败，密钥无效";
                break;
            case ERROR_CONN_NET:
                errorMsg = "连接失败，网络不可用";
                break;
            case ERROR_CONN_AUTHENTICATION:
                errorMsg = "连接失败，认证失败";
                break;
            case ERROR_SUB_UNVALIABLE_TOPIC:
                errorMsg = "订阅失败，主题格式非法";
                break;
            case ERROR_UNSUB_UNVALIABLE_TOPIC:
                errorMsg = "取消订阅失败，主题格式非法";
                break;
            case ERROR_LOST_CLIENT:
                errorMsg = "连接断开，mqtt断开连接";
                break;
            case ERROR_LOST_TIMEOUT:
                errorMsg = "连接断开，超时";
                break;
            case ERROR_LOST_HEART_TIMEOUT:
                errorMsg = "连接断开，心跳超时";
                break;
        }

        return errorMsg;

    }

    @Override
    public String toString() {
        return "StartaiError{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
