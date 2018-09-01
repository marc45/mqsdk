package cn.com.startai.mqsdk.util.eventbus;

/**
 * Created by Robin on 2018/7/10.
 * qq: 419109715 彬影
 */

public class EventBean<T> {


    public static final String S_2_A_LOGIN_RESULT = "S_2_A_LOGIN_RESULT";
    public static final String S_2_A_REGISTER_RESULT = "S_2_A_REGISTER_RESULT";
    public static final String S_2_A_ACTIVATE_RESULT = "S_2_A_ACTIVATE_RESULT";
    public static final String S_2_A_CUSTOMER_ACTIVATE_RESULT = "S_2_A_CUSTOMER_ACTIVATE_RESULT";
    public static final String S_2_A_BIND_RESULT = "S_2_A_BIND_RESULT";
    public static final String S_2_A_BIND_RESULT_ = "S_2_A_BIND_RESULT_";
    public static final String S_2_A_UNBIND_RESULT = "S_2_A_UNBIND_RESULT";
    public static final String S_2_A_UNBIND_RESULT_ = "S_2_A_UNBIND_RESULT_";
    public static final String S_2_A_BINDLIST_RESULT = "S_2_A_BINDLIST_RESULT";
    public static final String S_2_A_BINDLIST_RESULT_ = "S_2_A_BINDLIST_RESULT_";
    public static final String S_2_A_GET_IDENTIFY_RESULT = "S_2_A_GET_IDENTIFY_RESULT";
    public static final String S_2_A_CHECK_IDENTIFY_RESULT = "S_2_A_CHECK_IDENTIFY_RESULT";
    public static final String S_2_A_UN_ACTIVITE_RESULT = "S_2_A_UN_ACTIVITE_RESULT";
    public static final String S_2_A_LOGOUT_RESULT = "S_2_A_LOGOUT_RESULT";
    public static final String S_2_A_PASSTHROUTH_RESULT = "S_2_A_PASSTHROUTH_RESULT";
    public static final String S_2_A_CONN_SUCCESS = "S_2_A_CONN_SUCCESS";
    public static final String S_2_A_CONN_FAILED = "S_2_A_CONN_FAILED";
    public static final String S_2_A_TOKEN_EXPIRE = "S_2_A_TOKEN_EXPIRE";
    public static final String S_2_A_CONN_BREAK = "S_2_A_CONN_BREAK";
    public static final String S_2_A_0x8101_RESP = "S_2_A_0x8101_RESP";
    public static final String S_2_A_UPDATEUSEINFO_RESULT = "S_2_A_UPDATEUSEINFO_RESULT";
    public static final String S_2_A_GETUSERINFO_RESULT = "S_2_A_GETUSERINFO_RESULT";
    public static final String S_2_A_GETUSERINFO_RESULT_ = "S_2_A_GETUSERINFO_RESULT_";
    public static final String S_2_A_DEVICE_CONNECT_STATUS_CHANGE = "S_2_A_DEVICE_CONNECT_STATUS_CHANGE";
    public static final String S_2_A_GETLATEST_VERSION_RESULT = "S_2_A_GETLATEST_VERSION_RESULT";
    public static final String S_2_A_UPDATE_USER_PWD = "S_2_A_UPDATE_USER_PWD";
    public static final String S_2_A_UPDATE_REMARK = "S_2_A_UPDATE_REMARK";
    public static final String S_2_A_RESET_MOBILE_LOGIN_PWD = "S_2_A_RESET_MOBILE_LOGIN_PWD";
    public static final String S_2_A_SEND_EMAIL_RESULT = "S_2_A_SEND_EMAIL_RESULT";

    private String eventType;
    private T eventContent;

    @Override
    public String toString() {
        return "EventBean{" +
                "eventType='" + eventType + '\'' +
                ", eventContent=" + eventContent +
                '}';
    }

    public EventBean(String eventType, T eventContent) {
        this.eventType = eventType;
        this.eventContent = eventContent;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public T getEventContent() {
        return eventContent;
    }

    public void setEventContent(T eventContent) {
        this.eventContent = eventContent;
    }
}
