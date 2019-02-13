package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.DistributeParam;
import cn.com.startai.mqttsdk.base.MqttPublishRequestCreator;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.busi.ErrorMiofMsg;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.event.PersistentEventDispatcher;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.localbusi.UserBusi;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 更新用户信息
 * Created by Robin on 2018/7/11.
 * qq: 419109715 彬影
 */

public class C_0x8020 {

    private static final String TAG = C_0x8020.class.getSimpleName();
    public static String MSG_DESC = "更新用户信息 ";
    public static final String MSGTYPE = "0x8020";
    public static String MSGCW = "0x07";
    /**
     * 请求更新用户信息
     *
     * @param contentBean 待更新的用户数据
     * @param listener
     */
    public static void m_0x8020_req(Req.ContentBean contentBean, IOnCallListener listener) {

        MqttPublishRequest x8020_req_msg = create_0x8020_req_msg(contentBean);
        if (x8020_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8020_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8020_req_msg, listener);

    }
    /**
     * 组更新用户信息数据包
     */
    public static MqttPublishRequest create_0x8020_req_msg(C_0x8020.Req.ContentBean contentBean) {

        if (contentBean == null) {
            SLog.e(TAG, "参数非法 参数为空");
            return null;
        }
        String userid = "";
        C_0x8018.Resp.ContentBean currUser = new UserBusi().getCurrUser();
        if (currUser != null) {
            userid = currUser.getUserid();
        }
        if (TextUtils.isEmpty(contentBean.getUserid())) {
            contentBean.setUserid(userid);
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype("0x8020")
                .setMsgcw("0x07")
                .setContent(contentBean).create();

        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;

        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;

    }


    /**
     * 请求更新用户信息返回结果
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


            SLog.e(TAG, "用户信息更新成功");
        } else {

            Resp.ContentBean content = resp.getContent();
            Req.ContentBean errcontent = content.getErrcontent();
            content.setAddress(errcontent.getAddress());
            content.setBirthday(errcontent.getBirthday());
            content.setCity(errcontent.getCity());
            content.setCountry(errcontent.getCountry());
            content.setFirstName(errcontent.getFirstName());
            content.setLastName(errcontent.getLastName());
            content.setHeadPic(errcontent.getHeadPic());
            content.setTown(errcontent.getTown());
            content.setSex(errcontent.getSex());
            content.setUserName(errcontent.getUserName());
            content.setUserid(errcontent.getUserid());
            content.setProvince(errcontent.getProvince());
            content.setNickName(errcontent.getNickName());
            content.setAddress(errcontent.getAddress());


            SLog.e(TAG, MSG_DESC+" 失败 "+resp.getContent().getErrmsg());

        }
        StartAI.getInstance().getPersisitnet().getEventDispatcher().onUpdateUserInfoResult(resp);

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
            private String country = null;
            private String town = null;
            private String address = null;
            private String nickName = null;
            private String headPic = null;
            private String sex = null;
            private String firstName = null;
            private String lastName = null;


            @Override
            public String toString() {
                return "ContentBean{" +
                        "userid='" + userid + '\'' +
                        ", userName='" + userName + '\'' +
                        ", birthday='" + birthday + '\'' +
                        ", province='" + province + '\'' +
                        ", city='" + city + '\'' +
                        ", country='" + country + '\'' +
                        ", town='" + town + '\'' +
                        ", address='" + address + '\'' +
                        ", nickName='" + nickName + '\'' +
                        ", headPic='" + headPic + '\'' +
                        ", sex='" + sex + '\'' +
                        ", firstName='" + firstName + '\'' +
                        ", lastName='" + lastName + '\'' +
                        '}';
            }


            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
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


    /**
     * 更新用户信息返回
     */
    public static class Resp extends BaseMessage {

        private ContentBean content;

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

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends BaseContentBean {


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
            private String country = null;
            private String province = null;
            private String city = null;
            private String town = null;
            private String address = null;
            private String nickName = null;
            private String headPic = null;
            private String sex = null;
            private String firstName = null;
            private String lastName = null;
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", userid='" + userid + '\'' +
                        ", userName='" + userName + '\'' +
                        ", birthday='" + birthday + '\'' +
                        ", country='" + country + '\'' +
                        ", province='" + province + '\'' +
                        ", city='" + city + '\'' +
                        ", town='" + town + '\'' +
                        ", address='" + address + '\'' +
                        ", nickName='" + nickName + '\'' +
                        ", headPic='" + headPic + '\'' +
                        ", sex='" + sex + '\'' +
                        ", firstName='" + firstName + '\'' +
                        ", lastName='" + lastName + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }


            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
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
