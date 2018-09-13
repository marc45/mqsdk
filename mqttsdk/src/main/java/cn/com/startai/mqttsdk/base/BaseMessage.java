package cn.com.startai.mqttsdk.base;


import cn.com.startai.mqttsdk.mqtt.MqttConfigure;

/**
 * startai miof 基础消息
 * Created by Robin on 2018/5/7.
 * qq: 419109715 彬影
 */

public class BaseMessage {

    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_ERROR = 0;
    public static final int RESULT_STATUS = -1;

    protected String msgcw;
    protected String msgtype;
    protected String fromid;
    protected String toid;
    protected String domain = null;
    protected String appid = MqttConfigure.appid;
    protected Long ts = System.currentTimeMillis();
    protected String msgid = null;
    protected String m_ver = MqttConfigure.m_ver;
    protected Integer result = 0;


    public String getMsgcw() {
        return msgcw;
    }

    public void setMsgcw(String msgcw) {
        this.msgcw = msgcw;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public String getToid() {
        return toid;
    }

    public void setToid(String toid) {
        this.toid = toid;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getM_ver() {
        return m_ver;
    }

    public void setM_ver(String m_ver) {
        this.m_ver = m_ver;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public static class Builder {
        private BaseMessage msg = new BaseMessage();

        public Builder() {
        }

        public BaseMessage.Builder setToid(String toid) {
            this.msg.setToid(toid);
            return this;
        }

        public BaseMessage.Builder setMsgtype(String msgtype) {
            this.msg.setMsgtype(msgtype);
            return this;
        }

        public BaseMessage.Builder setMsgcw(String msgcw) {
            this.msg.setMsgcw(msgcw);
            return this;
        }

        public BaseMessage.Builder setResult(int result) {
            this.msg.setResult(result);
            return this;
        }


        public BaseMessage.Builder setMsgid(String msgid) {
            this.msg.setMsgid(msgid);
            return this;
        }

        public BaseMessage.Builder setFromid(String fromid) {
            this.msg.setFromid(fromid);
            return this;
        }

        public BaseMessage.Builder setTs(long time) {
            this.msg.setTs(time);
            return this;
        }

        public BaseMessage.Builder setM_ver(String m_ver) {
            this.msg.setM_ver(m_ver);
            return this;
        }

        public BaseMessage.Builder setApptype(String apptype) {
            this.msg.setAppid(apptype);
            return this;
        }

        public BaseMessage.Builder setDomain(String domain) {
            this.msg.setDomain(domain);
            return this;
        }

        public BaseMessage create() {
            return this.msg;
        }
    }


}
