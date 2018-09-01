package cn.com.startai.mqsdk.util.eventbus;

import cn.com.startai.mqsdk.busi.C_0x8101;
import cn.com.startai.mqttsdk.busi.ErrorMiofMsg;

/**
 * Created by Robin on 2018/7/10.
 * qq: 419109715 彬影
 */

public class E_0x8101_Resp {


    C_0x8101.Resp resp;

    public E_0x8101_Resp(C_0x8101.Resp resp) {

        this.resp = resp;
    }

    @Override
    public String toString() {
        return "E_0x8101_Resp{" +
                "resp=" + resp +
                '}';
    }

    public C_0x8101.Resp getResp() {
        return resp;
    }

    public void setResp(C_0x8101.Resp resp) {
        this.resp = resp;
    }
}
