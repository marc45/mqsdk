package cn.com.startai.mqttsdk.busi.entity;

/**
 * Created by Robin on 2018/8/31.
 * qq: 419109715 彬影
 */

public class BaseContentBean {

    protected String errcode;
    protected String errmsg;

    @Override
    public String toString() {
        return "BaseContentBean{" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
