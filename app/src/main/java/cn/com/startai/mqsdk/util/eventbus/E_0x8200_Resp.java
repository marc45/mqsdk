package cn.com.startai.mqsdk.util.eventbus;

import java.util.Arrays;

import cn.com.startai.mqttsdk.busi.entity.C_0x8200;

/**
 * Created by Robin on 2018/7/10.
 * qq: 419109715 彬影
 */

public class E_0x8200_Resp {

    int result;
    C_0x8200.Resp resp;
    String errorCode;
    String errorMsg;
    String dataString;
    byte[] dataByteArray;

    public E_0x8200_Resp() {
    }

    public E_0x8200_Resp(int result, C_0x8200.Resp resp, String errorCode, String errorMsg, String dataString, byte[] dataByteArray) {
        this.result = result;
        this.resp = resp;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.dataString = dataString;
        this.dataByteArray = dataByteArray;
    }

    @Override
    public String toString() {
        return "E_0x8200_Resp{" +
                "result=" + result +
                ", resp=" + resp +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", dataString='" + dataString + '\'' +
                ", dataByteArray=" + Arrays.toString(dataByteArray) +
                '}';
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public C_0x8200.Resp getResp() {
        return resp;
    }

    public void setResp(C_0x8200.Resp resp) {
        this.resp = resp;
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

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public byte[] getDataByteArray() {
        return dataByteArray;
    }

    public void setDataByteArray(byte[] dataByteArray) {
        this.dataByteArray = dataByteArray;
    }
}
