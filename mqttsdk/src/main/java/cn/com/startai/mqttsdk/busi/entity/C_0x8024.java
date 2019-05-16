package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.DistributeParam;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.localbusi.SUserManager;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.listener.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

import static cn.com.startai.mqttsdk.StartAI.TAG;

/**
 * 查询用户信息
 * Created by Robin on 2018/7/11.
 * qq: 419109715 彬影
 */

public class C_0x8024 implements Serializable {

    public static String MSG_DESC = "查询用户信息 ";
    public static final String MSGTYPE = "0x8024";
    public static String MSGCW = "0x07";

    /**
     * 请求查询用户信息
     *
     * @param listener
     */
    public static void m_0x8024_req(String userid, IOnCallListener listener) {

        MqttPublishRequest x8024_req_msg = create_0x8024_req_msg(userid);
        if (x8024_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8024_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8024_req_msg, listener);

    }

    /**
     * 组查询用户信息数据包
     *
     * @return
     */
    public static MqttPublishRequest create_0x8024_req_msg(String uid) {

        String userid = uid;
        Integer loginType = null;

        if (TextUtils.isEmpty(userid)) {

            userid = SUserManager.getInstance().getUserId();

            C_0x8018.Resp.ContentBean userBean = SUserManager.getInstance().getCurrUser();
            if (userBean != null && !TextUtils.isEmpty(userBean.getUserid())) {
                userid = userBean.getUserid();
                loginType = userBean.getType();
            }
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8024")
                .setMsgcw("0x07")
                .setContent(new C_0x8024.Req.ContentBean(userid, loginType)).create();

        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }

    /**
     * 请求查询 用户信息返回结果
     *
     * @param miof
     */
    public static void m_resp(String miof) {
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

            SLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
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
            private Integer loginType;

            public ContentBean(String userid, Integer loginType) {
                this.userid = userid;
                this.loginType = loginType;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "userid='" + userid + '\'' +
                        ", loginType=" + loginType +
                        '}';
            }

            public Integer getLoginType() {
                return loginType;
            }

            public void setLoginType(Integer loginType) {
                this.loginType = loginType;
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
            private List<ThirdInfosBean> thirdInfos;

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
                        ", thirdInfos=" + thirdInfos +
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

            public List<ThirdInfosBean> getThirdInfos() {
                return thirdInfos;
            }

            public void setThirdInfos(List<ThirdInfosBean> thirdInfos) {
                this.thirdInfos = thirdInfos;
            }


            public static class ThirdInfosBean {
                /**
                 * nickName : 微信
                 * type : 10
                 */
                private String nickName;
                private int type;

                @Override
                public String toString() {
                    return "ThirdInfosBean{" +
                            "nickName='" + nickName + '\'' +
                            ", type=" + type +
                            '}';
                }

                public String getNickName() {
                    return nickName;
                }

                public void setNickName(String nickName) {
                    this.nickName = nickName;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }
            }
        }
    }


}
