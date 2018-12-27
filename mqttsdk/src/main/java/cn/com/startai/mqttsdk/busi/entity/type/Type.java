package cn.com.startai.mqttsdk.busi.entity.type;

/**
 * Created by Robin on 2018/9/3.
 * qq: 419109715 彬影
 */

public class Type {

    public static class GetIdentifyCode {
        //1表示用户登录  2表示忘记密码 3表示用户注册
//        4第三方音响快捷登录
//5绑定/更改手机号

        public static final int LOGIN = 1;
        public static final int FORGET_PWD = 2;
        public static final int REGISTER = 3;
        public static final int MUSIC_BOX_LOGIN = 4;
        public static final int BIND_MOBILE_NUM = 5;
    }

    public static class CheckIdentifyCode {

        //1表示用户登录  2表示忘记密码 3表示用户注册
//        4第三方音响快捷登录
//5绑定/更改手机号

        public static final int LOGIN = 1;
        public static final int FORGET_PWD = 2;
        public static final int REGISTER = 3;
        public static final int MUSIC_BOX_LOGIN = 4;
        public static final int BIND_MOBILE_NUM = 5;

    }

    public static class SendEmail {

        //1 为重新发送激活邮件 2 为发送忘记密码邮件
        public static final int RESEND_ACTIVATE = 1;
        public static final int FORGET_PWD = 2;

    }

    public static class Login {
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


    }


    public static final class Register {
        /*
        1表示邮箱加密码
        2表示手机号加密码
        */
        public static final int EMAIL_PWD = 1;
        public static final int MOBILE_PWD = 2;
    }

    public static final class GetBindList {

        /*1.查询用户绑定的设备
        2.查询设备的用户列表
        3.查询用户的用户好友
        4.查询设备的设备列表
        5.查询用户的手机列表
        6.查询手机的用户好友
        7.查询所有*/
        public static final int USER_DEVICE = 1;
        public static final int DEVICE_USER = 2;
        public static final int USER_USER = 3;
        public static final int DEVICE_DEVICE = 4;
        public static final int USER_MOBILE = 5;
        public static final int MOBILE_USER = 6;
        public static final int ALL = 7;
    }

    public static final class ThirdPayment {
        public static final int TYPE_DEPOSIT = 1; //押金
        public static final int TYPE_BALANCE = 2; //余额
        public static final int TYPE_ORDER = 3; //订单

        public static final int PLATFOME_WECHAT = 1;
        public static final int PLATFOME_ALIPAY = 2;

    }
}
