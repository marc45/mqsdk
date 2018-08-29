package cn.com.startai.mqsdk.util.eventbus;

/**
 * Created by Robin on 2018/7/18.
 * qq: 419109715 彬影
 */

public class E_Device_Connect_Status {
    public int status;
    public String sn;
    public String userid;

    public E_Device_Connect_Status(String userid, int status, String sn) {
        this.status = status;
        this.userid = userid;
        this.sn = sn;
    }

    @Override
    public String toString() {
        return "E_Device_Connect_Status{" +
                "status=" + status +
                ", sn='" + sn + '\'' +
                '}';
    }
}
