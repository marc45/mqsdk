package cn.com.startai.mqsdk.util.eventbus;

/**
 * Created by Robin on 2018/7/10.
 * qq: 419109715 彬影
 */

public class E_Conn_Break {


    private int errorCode;
    private String errorMsg;

    @Override
    public String toString() {
        return "E_0x8001_Resp{" +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }

    public E_Conn_Break() {
    }

    public E_Conn_Break(int errorCode, String errorMsg) {
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
