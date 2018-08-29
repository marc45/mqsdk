package cn.com.startai.mqsdk.util.eventbus;

import cn.com.startai.mqttsdk.busi.entity.C_0x8018;

/**
 * Created by Robin on 2018/7/10.
 * qq: 419109715 彬影
 */

 public class E_0x8018_Resp {


    int result;
    String errorCode;
    String errorMsg;
    C_0x8018.Resp.ContentBean loginInfo;

    @Override
    public String toString() {
        return "E_0x8018_Resp{" +
                "result=" + result +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", loginInfo=" + loginInfo +
                '}';
    }

    public E_0x8018_Resp() {
    }

    public E_0x8018_Resp(int result, String errorCode, String errorMsg, C_0x8018.Resp.ContentBean loginInfo) {
        this.result = result;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.loginInfo = loginInfo;
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

    public C_0x8018.Resp.ContentBean getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(C_0x8018.Resp.ContentBean loginInfo) {
        this.loginInfo = loginInfo;
    }
}
