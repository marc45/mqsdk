package cn.com.startai.mqsdk.util.eventbus;

/**
 * Created by Robin on 2018/7/10.
 * qq: 419109715 彬影
 */

public class E_Conn_Failed {


    private int errorCode;
    private String errorMsg;

    public E_Conn_Failed() {
    }

    @Override
    public String toString() {
        return "E_Conn_Failed{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }

    public E_Conn_Failed(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

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
}
