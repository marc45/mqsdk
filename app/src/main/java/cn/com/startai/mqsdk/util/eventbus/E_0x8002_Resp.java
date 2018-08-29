package cn.com.startai.mqsdk.util.eventbus;

import cn.com.startai.mqttsdk.busi.entity.C_0x8002;

/**
 * Created by Robin on 2018/7/10.
 * qq: 419109715 彬影
 */

public class E_0x8002_Resp {
    int result;
    String errorCode;
    String errorMsg;
    String id;
    C_0x8002.Resp.ContentBean.BebindingBean bebinding;

    public E_0x8002_Resp() {
    }

    public E_0x8002_Resp(int result, String errorCode, String errorMsg, String id, C_0x8002.Resp.ContentBean.BebindingBean bebinding) {
        this.result = result;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.id = id;
        this.bebinding = bebinding;
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

    public C_0x8002.Resp.ContentBean.BebindingBean getBebinding() {
        return bebinding;
    }

    public void setBebinding(C_0x8002.Resp.ContentBean.BebindingBean bebinding) {
        this.bebinding = bebinding;
    }
}
