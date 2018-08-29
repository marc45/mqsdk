package cn.com.startai.mqsdk.util.eventbus;

/**
 * Created by Robin on 2018/7/10.
 * qq: 419109715 彬影
 */

public class E_0x8004_Resp {

    int result;
    String errorCode;
    String errorMsg;
    String id;
    String beUnbindid;

    @Override
    public String toString() {
        return "E_0x8004_Resp{" +
                "result=" + result +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", id='" + id + '\'' +
                ", beUnbindid='" + beUnbindid + '\'' +
                '}';
    }

    public E_0x8004_Resp() {
    }

    public E_0x8004_Resp(int result, String errorCode, String errorMsg, String id, String beUnbindid) {
        this.result = result;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.id = id;
        this.beUnbindid = beUnbindid;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeUnbindid() {
        return beUnbindid;
    }

    public void setBeUnbindid(String beUnbindid) {
        this.beUnbindid = beUnbindid;
    }
}
