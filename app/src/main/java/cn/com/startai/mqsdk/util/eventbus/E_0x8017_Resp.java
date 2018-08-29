package cn.com.startai.mqsdk.util.eventbus;

import cn.com.startai.mqttsdk.busi.entity.C_0x8017;

/**
 * Created by Robin on 2018/7/10.
 * qq: 419109715 彬影
 */

public class E_0x8017_Resp {

    int result;
    String errorCode;
    String errorMsg;
    C_0x8017.Resp.ContentBean contentBean;


    @Override
    public String toString() {
        return "E_0x8017_Resp{" +
                "result=" + result +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", contentBean=" + contentBean +
                '}';
    }

    public E_0x8017_Resp(int result, String errorCode, String errorMsg, C_0x8017.Resp.ContentBean contentBean) {
        this.result = result;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.contentBean = contentBean;
    }

    public C_0x8017.Resp.ContentBean getContentBean() {
        return contentBean;
    }

    public void setContentBean(C_0x8017.Resp.ContentBean contentBean) {
        this.contentBean = contentBean;
    }

    public E_0x8017_Resp() {
    }

    public E_0x8017_Resp(int result, String errorCode, String errorMsg) {
        this.result = result;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
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
}
