package cn.com.startai.mqttsdk.control.entity;


import cn.com.startai.mqttsdk.db.annotation.Column;
import cn.com.startai.mqttsdk.db.annotation.Table;

/**
 * 用户表
 * Created by Robin on 2018/7/17.
 * qq: 419109715 彬影
 */
@Table(name = "user")
public class UserBean {
    public static final String TABLE_NAME = "user";

    @Column(isId = true, name = "_id")
    private int _id; //数据库主键id自增，不参与任何业务
    public static final String F__ID = "_id";

    @Column(name = "userid")
    private String userid;//用户id
    public static final String F_USERID = "userid";

    @Column(name = "token")
    private String token; //用户token
    public static final String F_TOKEN = "token";

    @Column(name = "expire_in")
    private long expire_in; //token时效
    public static final String F_EXPIRE_IN = "expire_in";

    @Column(name = "uName")
    private String uName;  // 用户名
    public static final String F_UNAME = "uName";

    @Column(name = "type")
    private int type;// 登录类型
    public static final String F_TYPE = "type";

    @Column(name = "time")
    private long time;  //数据插入时间
    public static final String F_TIME = "time";


    @Column(name = "loginStatus")
    private int loginStatus; //登录 状态  1 登录  0未登录
    public static final String F_LOGINSTATUS = "loginStatus";

    @Override
    public String toString() {
        return "UserBean{" +
                "_id=" + _id +
                ", userid='" + userid + '\'' +
                ", token='" + token + '\'' +
                ", expire_in=" + expire_in +
                ", uName='" + uName + '\'' +
                ", type=" + type +
                ", time=" + time +
                ", loginStatus=" + loginStatus +
                '}';
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public static String getfLoginstatus() {
        return F_LOGINSTATUS;
    }

    public UserBean() {
    }

    public UserBean(String userid, String token, long expire_in, String uName, int type, int loginStatus) {
        this.userid = userid;
        this.token = token;
        this.expire_in = expire_in;
        this.uName = uName;
        this.type = type;
        this.loginStatus = loginStatus;
    }

    public UserBean(int _id, String userid, String token, long expire_in, String uName, int type, long time) {
        this._id = _id;
        this.userid = userid;
        this.token = token;
        this.expire_in = expire_in;
        this.uName = uName;
        this.type = type;
        this.time = time;
    }

    public long getExpire_in() {
        return expire_in;
    }

    public void setExpire_in(long expire_in) {
        this.expire_in = expire_in;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getUName() {
        return uName;
    }

    public void setUName(String uName) {
        this.uName = uName;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

