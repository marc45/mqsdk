package cn.com.startai.mqttsdk.busi.entity;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.UUID;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.DistributeParam;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 第三方登录
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class C_0x8027 implements Serializable {

    private static final String TAG = C_0x8027.class.getSimpleName();
    public static final String MSGTYPE = "0x8027";
    public static String MSGCW = "0x07";
    public static String MSG_DESC = "第三方登录 ";


    /*
 1表示邮箱加密码
 2表示手机号加密码
 3表示手机号加验证码
 4表示用户名加密码
 5双重认证 手机号+验证码+密码
 10:微信登录
 11:支付宝登录
 12:QQ登录
 13:谷歌登录
 14:推特登录
 15:亚马逊登录
 16:脸书登录
 17:小米
 */
    public static final int EMAIL_PWD = 1;
    public static final int MOBILE_PWD = 2;
    public static final int MOBILE_CODE = 3;
    public static final int UNAME_PWD = 4;
    public static final int MOBILE_CODE_PWD = 5;
    public static final int THIRD_WECHAT = 10;
    public static final int THIRD_ALIPAY = 11;
    public static final int THIRD_QQ = 12;
    public static final int THIRD_GOOGLE = 13;
    public static final int THIRD_TWITTER = 14;
    public static final int THIRD_AMAZON = 15;
    public static final int THIRD_FACEBOOK = 16;
    public static final int THIRD_MI = 17;




    /**
     * 请求 第三方登录
     *
     * @param listener
     */
    public static void req(int type, String code, IOnCallListener listener) {

        MqttPublishRequest<StartaiMessage<Req.ContentBean>> req_msg = create_req_msg(type, code);
        if (req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }

        StartaiMessage<Req.ContentBean> message = req_msg.message;


        String uuid = UUID.randomUUID().toString();

        C_0x8018.Req.ContentBean contentBean = new C_0x8018.Req.ContentBean();
        contentBean.setType(contentBean.getType());

        C_0x8018.maps.put(uuid, contentBean);
        message.setMsgid(uuid);

        StartaiMqttPersistent.getInstance().send(req_msg, listener);

    }

    /**
     * 请求 第三方登录
     *
     * @param listener
     */
    public static void req(Req.ContentBean contentBean, IOnCallListener listener) {


        MqttPublishRequest<StartaiMessage<Req.ContentBean>> req_msg = create_req_msg(contentBean);
        if (req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }

        String uuid = UUID.randomUUID().toString();
        StartaiMessage<Req.ContentBean> message = req_msg.message;

        contentBean.setType(contentBean.getType());
        C_0x8018.Req.ContentBean contentBean8018 = new C_0x8018.Req.ContentBean();
        C_0x8018.maps.put(uuid, contentBean8018);
        message.setMsgid(uuid);

        StartaiMqttPersistent.getInstance().send(req_msg, listener);

    }

    private static MqttPublishRequest<StartaiMessage<Req.ContentBean>> create_req_msg(@NonNull int type, String code) {

        if (TextUtils.isEmpty(code)) {
            SLog.e(TAG, "code  can not be empty");
            return null;
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(MSGTYPE)
                .setMsgcw(MSGCW)
                .setContent(new Req.ContentBean(type, code)).create();


        if (!DistributeParam.isDistribute(MSGTYPE)) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }

    private static MqttPublishRequest<StartaiMessage<Req.ContentBean>> create_req_msg(Req.ContentBean contentBean) {

        if (contentBean == null) {
            SLog.e(TAG, "contentBean  can not be null");
            return null;
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(MSGTYPE)
                .setMsgcw(MSGCW)
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
     * 请求 第三方登录 返回结果
     *
     * @param miof
     */
    public static void m_resp(String miof) {


        C_0x8018.m_resp(miof);

//        if (result == 1 && resp != null) {
//            SLog.d(TAG, "第三方登录成功");
//            StartAI.getInstance().getPersisitnet().getEventDispatcher().onloginWithThirdAccountResult(result, "", "", resp.getContent());
//        } else if (result == 0 && errorMiofMsg != null) {
//            SLog.d(TAG, "第三方登录失败");
//            StartAI.getInstance().getPersisitnet().getEventDispatcher().onloginWithThirdAccountResult(result, errorMiofMsg.getContent().getErrcode(), errorMiofMsg.getContent().getErrmsg(), resp.getContent());
//        } else {
//            SLog.e(TAG, "返回数据格式错误");
//        }

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
             * code :
             * type : 1
             * userinfo : {"openid":"OPENID","nickname":"NICKNAME","email":"email","sex":1,"province":"PROVINCE","city":"CITY","country":"COUNTRY","headimgurl":"http://url","username":"username","firstName":"firstName","lastName":"lastName","address":"address","unionid":"unionid"}
             */

            private String code;
            private int type;
            private UserinfoBean userinfo;

            public ContentBean() {
            }

            public ContentBean(int type, UserinfoBean userinfo) {
                this.type = type;
                this.userinfo = userinfo;
            }

            public ContentBean(int type, String code) {
                this.code = code;
                this.type = type;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "code='" + code + '\'' +
                        ", type=" + type +
                        ", userinfo=" + userinfo +
                        '}';
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public UserinfoBean getUserinfo() {
                return userinfo;
            }

            public void setUserinfo(UserinfoBean userinfo) {
                this.userinfo = userinfo;
            }

            public static class UserinfoBean {
                /**
                 * openid : OPENID
                 * nickname : NICKNAME
                 * email : email
                 * sex : 1
                 * province : PROVINCE
                 * city : CITY
                 * country : COUNTRY
                 * headimgurl : http://url
                 * username : username
                 * firstName : firstName
                 * lastName : lastName
                 * address : address
                 * unionid : unionid
                 */

                private String openid;
                private String nickname;
                private String email;
                private int sex;
                private String province;
                private String city;
                private String country;
                private String headimgurl;
                private String username;
                private String firstName;
                private String lastName;
                private String address;
                private String unionid;

                @Override
                public String toString() {
                    return "UserinfoBean{" +
                            "openid='" + openid + '\'' +
                            ", nickname='" + nickname + '\'' +
                            ", email='" + email + '\'' +
                            ", sex=" + sex +
                            ", province='" + province + '\'' +
                            ", city='" + city + '\'' +
                            ", country='" + country + '\'' +
                            ", headimgurl='" + headimgurl + '\'' +
                            ", username='" + username + '\'' +
                            ", firstName='" + firstName + '\'' +
                            ", lastName='" + lastName + '\'' +
                            ", address='" + address + '\'' +
                            ", unionid='" + unionid + '\'' +
                            '}';
                }

                public String getOpenid() {
                    return openid;
                }

                public void setOpenid(String openid) {
                    this.openid = openid;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public int getSex() {
                    return sex;
                }

                public void setSex(int sex) {
                    this.sex = sex;
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

                public String getCountry() {
                    return country;
                }

                public void setCountry(String country) {
                    this.country = country;
                }

                public String getHeadimgurl() {
                    return headimgurl;
                }

                public void setHeadimgurl(String headimgurl) {
                    this.headimgurl = headimgurl;
                }

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
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

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getUnionid() {
                    return unionid;
                }

                public void setUnionid(String unionid) {
                    this.unionid = unionid;
                }
            }
        }


    }


    public static class Resp extends BaseMessage implements Serializable {

        private ContentBean content;

        @Override
        public String toString() {
            return "Resp{" +
                    "content=" + content +
                    ", msgcw='" + msgcw + '\'' +
                    ", msgtype='" + msgtype + '\'' +
                    ", fromid='" + fromid + '\'' +
                    ", toid='" + toid + '\'' +
                    ", domain='" + domain + '\'' +
                    ", appid='" + appid + '\'' +
                    ", ts=" + ts +
                    ", msgid='" + msgid + '\'' +
                    ", m_ver='" + m_ver + '\'' +
                    ", result=" + result +
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
             * token :
             * expire_in : 7200
             */

            private String userid;
            private String token;
            private int expire_in;
            private int type;
            private Req.ContentBean errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", userid='" + userid + '\'' +
                        ", token='" + token + '\'' +
                        ", expire_in=" + expire_in +
                        ", type=" + type +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }

            public ContentBean() {
            }

            public ContentBean(String userid, String token, int expire_in, int type) {
                this.userid = userid;
                this.token = token;
                this.expire_in = expire_in;
                this.type = type;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public int getExpire_in() {
                return expire_in;
            }

            public void setExpire_in(int expire_in) {
                this.expire_in = expire_in;
            }
        }
    }


}
