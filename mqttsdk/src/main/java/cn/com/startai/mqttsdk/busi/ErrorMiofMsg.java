package cn.com.startai.mqttsdk.busi;

import cn.com.startai.mqttsdk.base.BaseMessage;

/**
 * Created by Robin on 2018/6/19.
 * qq: 419109715 彬影
 */

public class ErrorMiofMsg extends BaseMessage {


    private ContentBean content;


    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * errcode : 00000063
         * errmsg  : Device activation failure,params loss
         */

        private String errcode;
        private String errmsg;

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
}
