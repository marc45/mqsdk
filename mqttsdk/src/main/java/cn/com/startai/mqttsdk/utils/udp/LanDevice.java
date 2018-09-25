package cn.com.startai.mqttsdk.utils.udp;

/**
 * Created by Robin on 2018/8/11.
 * qq: 419109715 彬影
 */

public class LanDevice {


    private String ip;
    private int port;
    private String name;
    private String mac;
    private int rssi;
    private String sn;
    private int devConnStatus;//设备的连接状态 1已连接 0 未连接
    private int devActivateStatus;//设备的激活状态 1已经激活 0未激活
    private String cpuId;
    private int bindNeedPwd; //0 局域网绑定不需要密码 1 局域网绑定需要密码
    private int isAdmin;//自己的身份 0 普通用户 1 管理员
    private int bindType; // 自己的绑定状态 0未绑定 1局域网绑定 2广域网绑定
    private int isHaveAdmin;// 是否有管理员 0 无管理员 1 有管理员

    public int getIsHaveAdmin() {
        return isHaveAdmin;
    }

    public void setIsHaveAdmin(int isHaveAdmin) {
        this.isHaveAdmin = isHaveAdmin;
    }

    public int getBindNeedPwd() {
        return bindNeedPwd;
    }

    public void setBindNeedPwd(int bindNeedPwd) {
        this.bindNeedPwd = bindNeedPwd;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getBindType() {
        return bindType;
    }

    public void setBindType(int bindType) {
        this.bindType = bindType;
    }


    public String getCpuId() {
        return cpuId;
    }

    public void setCpuId(String cpuId) {
        this.cpuId = cpuId;
    }

    @Override
    public String toString() {
        return "LanDevice{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", name='" + name + '\'' +
                ", mac='" + mac + '\'' +
                ", rssi=" + rssi +
                ", sn='" + sn + '\'' +
                ", devConnStatus=" + devConnStatus +
                ", devActivateStatus=" + devActivateStatus +
                ", cpuId='" + cpuId + '\'' +
                ", bindNeedPwd=" + bindNeedPwd +
                ", isAdmin=" + isAdmin +
                ", bindType=" + bindType +
                ", isHaveAdmin=" + isHaveAdmin +
                '}';
    }

    public LanDevice() {
    }

    public int getDevConnStatus() {
        return devConnStatus;
    }

    public void setDevConnStatus(int devConnStatus) {
        this.devConnStatus = devConnStatus;
    }

    public int getDevActivateStatus() {
        return devActivateStatus;
    }

    public void setDevActivateStatus(int devActivateStatus) {
        this.devActivateStatus = devActivateStatus;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
