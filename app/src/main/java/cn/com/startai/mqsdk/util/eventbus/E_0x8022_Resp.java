package cn.com.startai.mqsdk.util.eventbus;

/**
 * Created by Robin on 2018/7/10.
 * qq: 419109715 彬影
 */

public class E_0x8022_Resp {

    private int result;
    private String errorCode;
    private String errorMsg;

    @Override
    public String toString() {
        return "E_0x8022_Resp{" +
                "result=" + result +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }

    public E_0x8022_Resp() {
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public E_0x8022_Resp(int result, String errorCode, String errorMsg) {
        this.result = result;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
