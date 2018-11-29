package cn.com.startai.mqttsdk.control.entity;


import cn.com.startai.mqttsdk.db.annotation.Column;
import cn.com.startai.mqttsdk.db.annotation.Table;

/**
 * Created by Robin on 2018/7/17.
 * qq: 419109715 彬影
 */
@Table(name = "topic")
public class TopicBean {
    public static final String TABLE_NAME = "topic";

    @Column(isId = true, name = "_id")
    private int _id; //数据库主键id自增，不参与任何业务
    public static final String F__ID = "_id";

    @Column(name = "topic")
    private String topic;
    public static final String F_TOPIC = "topic";


    @Column(name = "type")
    private String type; //set or remove
    public static final String F_TYPE = "type";

    @Column(name = "currType")
    private String currType; //当前 类型
    public static final String F_CURRTYPE = "currType";

    @Column(name = "id")
    private String id; //用户id或sn
    public static final String F_ID = "id";

    @Column(name = "time")
    private long time; //更新时间
    public static final String F_TIME = "time";


    public TopicBean() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public TopicBean(String topic, String type, String currType, String id) {
        this.topic = topic;
        this.type = type;
        this.currType = currType;
        this.id = id;
    }

    @Override
    public String toString() {
        return "TopicBean{" +
                "_id=" + _id +
                ", topic='" + topic + '\'' +
                ", type='" + type + '\'' +
                ", currType='" + currType + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrType() {
        return currType;
    }

    public void setCurrType(String currType) {
        this.currType = currType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

