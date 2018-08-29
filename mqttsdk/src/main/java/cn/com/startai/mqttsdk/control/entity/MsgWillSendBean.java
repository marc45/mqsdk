package cn.com.startai.mqttsdk.control.entity;


import cn.com.startai.mqttsdk.db.annotation.Column;
import cn.com.startai.mqttsdk.db.annotation.Table;

/**
 * 名称：MsgWillSendBean.java
 * 描述：即将发送的消息
 * Created by Robin on 2016-8-29
 * QQ 419109715 彬影
 */
@Table(name = "msgwillsend")
public class MsgWillSendBean {

    /**
     * id
     */
    @Column(name = "_id", isId = true)
    private int _id;
    public static final String FIELD_ID = "_id";
    /**
     * 消息id
     */
    @Column(name = "id")
    private String id = "";
    public static final String FIELD_MSGID = "id";
    /**
     * 消息内容
     */
    @Column(name = "msgWillSend")
    private String msgWillSend = "";
    public static final String FIELD_MSGWILLSEND = "msgWillSend";

    /**
     * 消息类型
     */
    @Column(name = "msgtype")
    private String msgtype = "";
    public static final String FIELD_MSGTYPE = "msgtype";
    /**
     * 是否已经发送成功
     */
    @Column(name = "issend")
    private int isSend;
    public static final String FIELD_ISSEND = "isSend";
    /**
     * 消息发送的主题
     */
    @Column(name = "topic")
    private String topic = "";
    public static final String FIELD_TOPIC = "topic";

    @Column(name = "time")
    private long time;
    public static final String FIELD_TIME = "time";

    /**
     * 消息接收者
     */
    @Column(name = "toid")
    private String toid;
    public static final String FIELD_TOID = "toid";


    @Override
    public String toString() {
        return "MsgWillSendBean{" +
                "_id=" + _id +
                ", id='" + id + '\'' +
                ", msgWillSend='" + msgWillSend + '\'' +
                ", msgtype='" + msgtype + '\'' +
                ", isSend=" + isSend +
                ", topic='" + topic + '\'' +
                ", time=" + time +
                ", toid='" + toid + '\'' +
                '}';
    }

    public MsgWillSendBean(String msgWillSend, String msgtype, String topic) {
        this.msgWillSend = msgWillSend;
        this.msgtype = msgtype;
        this.topic = topic;
    }

    public MsgWillSendBean() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public static String getFieldId() {
        return FIELD_ID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static String getFieldMsgid() {
        return FIELD_MSGID;
    }

    public String getMsgWillSend() {
        return msgWillSend;
    }

    public void setMsgWillSend(String msgWillSend) {
        this.msgWillSend = msgWillSend;
    }

    public static String getFieldMsgwillsend() {
        return FIELD_MSGWILLSEND;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public static String getFieldMsgtype() {
        return FIELD_MSGTYPE;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }

    public static String getFieldIssend() {
        return FIELD_ISSEND;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public static String getFieldTopic() {
        return FIELD_TOPIC;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static String getFieldTime() {
        return FIELD_TIME;
    }

    public String getToid() {
        return toid;
    }

    public void setToid(String toid) {
        this.toid = toid;
    }

    public static String getFieldToid() {
        return FIELD_TOID;
    }
}
