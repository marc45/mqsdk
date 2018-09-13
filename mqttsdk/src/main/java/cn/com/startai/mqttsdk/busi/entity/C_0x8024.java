package cn.com.startai.mqttsdk.busi.entity;

import java.io.Serializable;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.busi.ErrorMiofMsg;
import cn.com.startai.mqttsdk.event.PersistentEventDispatcher;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 查询用户信息
 * Created by Robin on 2018/7/11.
 * qq: 419109715 彬影
 */

public class C_0x8024 implements Serializable {

    private static final String TAG = C_0x8024.class.getSimpleName();

    /**
     * 请求查询用户信息
     *
     * @param listener
     */
    public static void m_0x8024_req(String userid, IOnCallListener listener) {

        MqttPublishRequest x8024_req_msg = MqttPublishRequestCreator.create_0x8024_req_msg(userid);
        if (x8024_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8024_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8024_req_msg, listener);

    }


    /**
     * 请求查询 用户信息返回结果
     *
     * @param miof
     */
    public static void m_0x8024_resp(String miof) {
        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, "返回数据格式错误");
            return;
        }

        if (resp.getResult() == 1) {
            SLog.e(TAG, "查询用户信息成功");
        } else {
            Resp.ContentBean content = resp.getContent();
            Req.ContentBean errcontent = content.getErrcontent();
            content.setUserid(errcontent.getUserid());

            SLog.e(TAG, "查询用户信息失败");
        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onGetUserInfoResult(resp);
    }

    public static class Req {
        private ContentBean content;

        public Req(ContentBean content) {
            this.content = content;
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean {


            private String userid = null;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "userid='" + userid + '\'' +
                        '}';
            }

            public ContentBean() {
            }

            public ContentBean(String userid) {
                this.userid = userid;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }
        }


    }


    /**
     * 查询用户信息返回
     */
    public static class Resp extends BaseMessage implements Serializable {

        @Override
        public String toString() {
            return "Resp{" +
                    "msgcw='" + msgcw + '\'' +
                    ", msgtype='" + msgtype + '\'' +
                    ", fromid='" + fromid + '\'' +
                    ", toid='" + toid + '\'' +
                    ", domain='" + domain + '\'' +
                    ", appid='" + appid + '\'' +
                    ", ts=" + ts +
                    ", msgid='" + msgid + '\'' +
                    ", m_ver='" + m_ver + '\'' +
                    ", result=" + result +
                    ", content=" + content +
                    '}';
        }

        private ContentBean content;

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends BaseContentBean implements Serializable {


            /**
             * userid :
             * userName : Robin
             * birthday : 1991-10-15
             * province : 广东省
             * city : 广州市
             * town : 天河区
             * address : 中山大道
             * nickName : 会飞的企鹅
             * headPic : http://ggg.pic
             * sex : 男
             * firstName : 罗
             * lastName : 彬心
             */

            private String userid = null;
            private String userName = null;
            private String birthday = null;
            private String province = null;
            private String city = null;
            private String town = null;
            private String address = null;
            private String nickName = null;
            private String headPic = null;
            private String sex = null;
            private String firstName = null;
            private String email = null;
            private String mobile = null;
            private String lastName = null;

            private int isHavePwd;


            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", userid='" + userid + '\'' +
                        ", userName='" + userName + '\'' +
                        ", birthday='" + birthday + '\'' +
                        ", province='" + province + '\'' +
                        ", city='" + city + '\'' +
                        ", town='" + town + '\'' +
                        ", address='" + address + '\'' +
                        ", nickName='" + nickName + '\'' +
                        ", headPic='" + headPic + '\'' +
                        ", sex='" + sex + '\'' +
                        ", firstName='" + firstName + '\'' +
                        ", email='" + email + '\'' +
                        ", mobile='" + mobile + '\'' +
                        ", lastName='" + lastName + '\'' +
                        ", isHavePwd=" + isHavePwd +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public int getIsHavePwd() {
                return isHavePwd;
            }

            public void setIsHavePwd(int isHavePwd) {
                this.isHavePwd = isHavePwd;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getTown() {
                return town;
            }

            public void setTown(String town) {
                this.town = town;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getHeadPic() {
                return headPic;
            }

            public void setHeadPic(String headPic) {
                this.headPic = headPic;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }


        }
    }


}
