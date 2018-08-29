package cn.com.startai.mqttsdk.control.entity;

/**
 * 区域和外网ip
 * Created by Robin on 2018/5/11.
 * qq: 419109715 彬影
 */

public class AreaOutterIp {

    public String area;
    public String outterIp;

    public AreaOutterIp(String area, String outterIp) {
        this.area = area;
        this.outterIp = outterIp;
    }

    @Override
    public String toString() {
        return "AreaOutterIp{" +
                "area='" + area + '\'' +
                ", outterIp='" + outterIp + '\'' +
                '}';
    }
}
