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
    public static void m_0x8024_req(IOnCallListener listener) {

        MqttPublishRequest x8024_req_msg = MqttPublishRequestCreator.create_0x8024_req_msg();
        if (x8024_req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, x8024_req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }
        StartaiMqttPersistent.getInstance().send(x8024_req_msg, listener);

    }


    /**
     * 请求查询 用户信息返回结果
     *
     * @param result
     * @param resp
     * @param errorMiofMsg
     */
    public static void m_0x8024_resp(int result, Resp resp, ErrorMiofMsg errorMiofMsg) {

        if (result == 1 && resp != null) {
            SLog.d(TAG, "查询用户信息成功");
            StartAI.getInstance().getPersisitnet().getEventDispatcher().onGetUserInfoResult(result, "", "", resp.getContent());
        } else if (result == 0 && errorMiofMsg != null) {
            SLog.d(TAG, "查询用户信息失败");
            StartAI.getInstance().getPersisitnet().getEventDispatcher().onGetUserInfoResult(result, errorMiofMsg.getContent().getErrcode(), errorMiofMsg.getContent().getErrmsg(), resp.getContent());
        } else {
            SLog.e(TAG, "返回数据格式错误");
        }

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

        private ContentBean content;

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean implements Serializable {


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
            private String lastName = null;
            private int isHavePwd;


            @Override
            public String toString() {
                return "ContentBean{" +
                        "userid='" + userid + '\'' +
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
                        ", lastName='" + lastName + '\'' +
                        ", isHavePwd=" + isHavePwd +
                        '}';
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
