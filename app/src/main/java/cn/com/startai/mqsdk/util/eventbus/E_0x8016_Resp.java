package cn.com.startai.mqsdk.util.eventbus;

import cn.com.startai.mqttsdk.busi.entity.C_0x8016;

/**
 * Created by Robin on 2018/7/19.
 * qq: 419109715 彬影
 */

public class E_0x8016_Resp {

    int result;
    String errorCode;
    String errorMsg;
    C_0x8016.Resp.ContentBean contentBean;

    public E_0x8016_Resp(int result, String errorCode, String errorMsg, C_0x8016.Resp.ContentBean contentBean) {
        this.result = result;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.contentBean = contentBean;
    }

    public E_0x8016_Resp() {
    }

    @Override
    public String toString() {
        return "E_0x8016_Resp{" +
                "result=" + result +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", contentBean=" + contentBean +
                '}';
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

    public C_0x8016.Resp.ContentBean getContentBean() {
        return contentBean;
    }

    public void setContentBean(C_0x8016.Resp.ContentBean contentBean) {
        this.contentBean = contentBean;
    }
}
