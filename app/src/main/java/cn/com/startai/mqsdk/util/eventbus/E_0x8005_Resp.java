package cn.com.startai.mqsdk.util.eventbus;

import java.util.ArrayList;

import cn.com.startai.mqttsdk.busi.entity.C_0x8005;

/**
 * Created by Robin on 2018/7/10.
 * qq: 419109715 彬影
 */

public class E_0x8005_Resp {

    int result;
    String errorCode;
    String errorMsg;
    String id;
    ArrayList<C_0x8005.Resp.ContentBean> bindList;

    public E_0x8005_Resp() {
    }

    public E_0x8005_Resp(int result, String errorCode, String errorMsg, String id, ArrayList<C_0x8005.Resp.ContentBean> bindList) {
        this.result = result;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.id = id;
        this.bindList = bindList;
    }

    @Override
    public String toString() {
        return "E_0x8005_Resp{" +
                "result=" + result +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", id='" + id + '\'' +
                ", bindList=" + bindList +
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<C_0x8005.Resp.ContentBean> getBindList() {
        return bindList;
    }

    public void setBindList(ArrayList<C_0x8005.Resp.ContentBean> bindList) {
        this.bindList = bindList;
    }
}
