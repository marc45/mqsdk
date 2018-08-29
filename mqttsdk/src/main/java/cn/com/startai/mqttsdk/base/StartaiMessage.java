package cn.com.startai.mqttsdk.base;


/**
 * startai miof 基础消息
 * Created by Robin on 2018/5/7.
 * qq: 419109715 彬影
 */

public class StartaiMessage<T> extends BaseMessage {

    private T content;


    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public static class Builder {
        private StartaiMessage msg = new StartaiMessage();

        public Builder() {

        }

        public StartaiMessage.Builder setToid(String toid) {
            this.msg.setToid(toid);
            return this;
        }

        public StartaiMessage.Builder setMsgtype(String msgtype) {
            this.msg.setMsgtype(msgtype);
            return this;
        }

        public StartaiMessage.Builder setMsgcw(String msgcw) {
            this.msg.setMsgcw(msgcw);
            return this;
        }

        public StartaiMessage.Builder setResult(int result) {
            this.msg.setResult(result);
            return this;
        }


        public StartaiMessage.Builder setMsgid(String msgid) {
            this.msg.setMsgid(msgid);
            return this;
        }

        public StartaiMessage.Builder setFromid(String fromid) {
            this.msg.setFromid(fromid);
            return this;
        }

        public StartaiMessage.Builder setTs(long time) {
            this.msg.setTs(time);
            return this;
        }

        public StartaiMessage.Builder setM_ver(String m_ver) {
            this.msg.setM_ver(m_ver);
            return this;
        }

        public StartaiMessage.Builder setAppid(String apptype) {
            this.msg.setAppid(apptype);
            return this;
        }

        public StartaiMessage.Builder setDomain(String domain) {
            this.msg.setDomain(domain);
            return this;
        }


        public StartaiMessage.Builder setContent(Object Object) {
            this.msg.setContent(Object);
            return this;
        }


        public StartaiMessage create() {
            return this.msg;
        }
    }


}
