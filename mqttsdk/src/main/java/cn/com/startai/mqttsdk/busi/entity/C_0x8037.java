package cn.com.startai.mqttsdk.busi.entity;

import android.text.TextUtils;

import java.io.Serializable;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.BaseMessage;
import cn.com.startai.mqttsdk.base.DistributeParam;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.localbusi.UserBusi;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 绑定第三方账号
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class C_0x8037 implements Serializable {

    private static final String TAG = C_0x8037.class.getSimpleName();
    public static final String MSGTYPE = "0x8037";
    public static String MSGCW = "0x07";
    public static String MSG_DESC = "绑定第三方账号 ";

    public static final int THIRD_WECHAT = 10;
    public static final int THIRD_ALIPAY = 11;
    public static final int THIRD_QQ = 12;
    public static final int THIRD_GOOGLE = 13;
    public static final int THIRD_TWITTER = 14;
    public static final int THIRD_AMAZON = 15;
    public static final int THIRD_FACEBOOK = 16;
    public static final int THIRD_MI = 17;

    /**
     * 请求 绑定第三方账号
     *
     * @param listener
     */
    public static void req(Req.ContentBean req, IOnCallListener listener) {

        MqttPublishRequest<StartaiMessage<Req.ContentBean>> req_msg = create_req_msg(req);
        if (req_msg == null) {
            CallbackManager.callbackMessageSendResult(false, listener, req_msg, new StartaiError(StartaiError.ERROR_SEND_PARAM_INVALIBLE));
            return;
        }

        StartaiMqttPersistent.getInstance().send(req_msg, listener);
    }

    private static MqttPublishRequest<StartaiMessage<Req.ContentBean>> create_req_msg(Req.ContentBean req) {

        String userid = req.getUserid();
        int type = req.getType();

        if (TextUtils.isEmpty(userid)) {
            C_0x8018.Resp.ContentBean currUser = new UserBusi().getCurrUser();
            if (currUser != null) {
                req.setUserid(currUser.getUserid());
            }
        }


        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(MSGTYPE)
                .setMsgcw(MSGCW)
                .setContent(req).create();


        if (!DistributeParam.UNBINDTHIRDACCOUNT_DISTRIBUTE) {
            message.setFromid(MqttConfigure.getSn(StartAI.getContext()));
        }

        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.message = message;
        mqttPublishRequest.topic = TopicConsts.getServiceTopic();
        return mqttPublishRequest;
    }


    /**
     * 请求 解绑第三方账号 返回结果
     *
     * @param miof
     */
    public static void m_resp(String miof) {


        Resp resp = SJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            SLog.e(TAG, MSG_DESC + " 返回格式错误");
            return;
        }
        if (resp.getResult() == 1) {
            SLog.e(TAG, MSG_DESC + " 成功");

        } else {
            C_0x8037.Resp.ContentBean content = resp.getContent();
            C_0x8037.Req.ContentBean errcontent = content.getErrcontent();
            content.setCode(errcontent.getCode());
            content.setUserid(errcontent.getUserid());
            content.setType(errcontent.getType());
            content.setUserinfo(errcontent.getUserinfo());
            SLog.e(TAG, MSG_DESC + " 失败");
        }

        StartAI.getInstance().getPersisitnet().getEventDispatcher().onBindThirdAccountResult(resp);

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
             * code :
             * type : 1
             * userinfo : {"openid":"OPENID","nickname":"NICKNAME","email":"email","sex":1,"province":"PROVINCE","city":"CITY","country":"COUNTRY","headimgurl":"http://url","username":"username","firstName":"firstName","lastName":"lastName","address":"address","unionid":"unionid"}
             */

            private String userid;
            private String code;
            private int type;
            private UserinfoBean userinfo;

            public ContentBean() {
            }

            public ContentBean(String userid, String code, int type, UserinfoBean userinfo) {
                this.userid = userid;
                this.code = code;
                this.type = type;
                this.userinfo = userinfo;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "userid='" + userid + '\'' +
                        ", code='" + code + '\'' +
                        ", type=" + type +
                        ", userinfo=" + userinfo +
                        '}';
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
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

                public UserinfoBean() {
                }

                public UserinfoBean(String openid, String nickname, String email, int sex, String province, String city, String country, String headimgurl, String username, String firstName, String lastName, String address, String unionid) {
                    this.openid = openid;
                    this.nickname = nickname;
                    this.email = email;
                    this.sex = sex;
                    this.province = province;
                    this.city = city;
                    this.country = country;
                    this.headimgurl = headimgurl;
                    this.username = username;
                    this.firstName = firstName;
                    this.lastName = lastName;
                    this.address = address;
                    this.unionid = unionid;
                }

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


            private Req.ContentBean errcontent = null;


            /**
             * userid :
             * code :
             * type : 1
             * userinfo : {"openid":"OPENID","nickname":"NICKNAME","email":"email","sex":1,"province":"PROVINCE","city":"CITY","country":"COUNTRY","headimgurl":"http://url","username":"username","firstName":"firstName","lastName":"lastName","address":"address","unionid":"unionid"}
             */

            private String userid;
            private String code;
            private int type;
            private Req.ContentBean.UserinfoBean userinfo;

            public ContentBean() {
            }

            public ContentBean(Req.ContentBean errcontent, String userid, String code, int type, Req.ContentBean.UserinfoBean userinfo) {
                this.errcontent = errcontent;
                this.userid = userid;
                this.code = code;
                this.type = type;
                this.userinfo = userinfo;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", userid='" + userid + '\'' +
                        ", code='" + code + '\'' +
                        ", type=" + type +
                        ", userinfo=" + userinfo +
                        '}';
            }

            public Req.ContentBean getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req.ContentBean errcontent) {
                this.errcontent = errcontent;
            }


            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
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

            public Req.ContentBean.UserinfoBean getUserinfo() {
                return userinfo;
            }

            public void setUserinfo(Req.ContentBean.UserinfoBean userinfo) {
                this.userinfo = userinfo;
            }
        }
    }


}
