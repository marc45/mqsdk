package cn.com.startai.mqsdk.util.eventbus;

import cn.com.startai.mqsdk.busi.C_0x8101;
import cn.com.startai.mqttsdk.busi.ErrorMiofMsg;

/**
 * Created by Robin on 2018/7/10.
 * qq: 419109715 彬影
 */

public class E_0x8101_Resp {


    int result;
    C_0x8101.Resp resp;
    ErrorMiofMsg errorMiofMsg;

    @Override
    public String toString() {
        return "E_0x8101_Resp{" +
                "result=" + result +
                ", resp=" + resp +
                ", errorMiofMsg=" + errorMiofMsg +
                '}';
    }

    public E_0x8101_Resp() {
    }

    public E_0x8101_Resp(int result, C_0x8101.Resp resp, ErrorMiofMsg errorMiofMsg) {
        this.result = result;
        this.resp = resp;
        this.errorMiofMsg = errorMiofMsg;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public C_0x8101.Resp getResp() {
        return resp;
    }

    public void setResp(C_0x8101.Resp resp) {
        this.resp = resp;
    }

    public ErrorMiofMsg getErrorMiofMsg() {
        return errorMiofMsg;
    }

    public void setErrorMiofMsg(ErrorMiofMsg errorMiofMsg) {
        this.errorMiofMsg = errorMiofMsg;
    }
}
