package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.busi.ErrorMiofMsg;
import cn.com.startai.mqttsdk.control.SDBmanager;
import cn.com.startai.mqttsdk.control.entity.UserBean;
import cn.com.startai.mqttsdk.event.PersistentEventDispatcher;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.localbusi.UserBusi;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * Created by Robin on 2018/7/16.
 * qq: 419109715 彬影
 */

public class C_0x9999 {

    private static String TAG = C_0x9999.class.getSimpleName();

    /**
     * 自己上报 自己的连接断开事件
     *
     * @param listener
     */
    public static void m_0x9999_req(IOnCallListener listener) {


    }

    /**
     * 处理对端的 终端连接断开事件
     */
    public static void m_0x9999_resp(String miof) {

        Resp resp = SJsonUtils.fromJson(miof, Resp.class);


        if (resp == null) {
            SLog.e(TAG, "接收的数据格式错误");
            return;
        }

        String sn = resp.getContent().getSn();
        SLog.e(TAG, "设备离线 " + sn);

        C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
        if (userBean != null) {

            StartAI.getInstance().getPersisitnet().getEventDispatcher().onDeviceConnectStatusChanged(userBean.getUserid(), 0, sn);

        }

    }


    public static class Req {

        private ContentBean content;

        public static class ContentBean {
            private String sn;
            private String reason;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "sn='" + sn + '\'' +
                        ", reason='" + reason + '\'' +
                        '}';
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }
        }


    }


    public static class Resp extends BaseMessage {


        private ContentBean content;

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean {
            private String sn;
            private String reason;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "sn='" + sn + '\'' +
                        ", reason='" + reason + '\'' +
                        '}';
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }
        }

    }


}
