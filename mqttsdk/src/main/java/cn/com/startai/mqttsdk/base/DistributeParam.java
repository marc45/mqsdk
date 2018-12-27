package cn.com.startai.mqttsdk.base;


import cn.com.startai.mqttsdk.busi.entity.C_0x8001;
import cn.com.startai.mqttsdk.busi.entity.C_0x8002;
import cn.com.startai.mqttsdk.busi.entity.C_0x8003;
import cn.com.startai.mqttsdk.busi.entity.C_0x8004;
import cn.com.startai.mqttsdk.busi.entity.C_0x8005;
import cn.com.startai.mqttsdk.busi.entity.C_0x8015;
import cn.com.startai.mqttsdk.busi.entity.C_0x8016;
import cn.com.startai.mqttsdk.busi.entity.C_0x8017;
import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.busi.entity.C_0x8020;
import cn.com.startai.mqttsdk.busi.entity.C_0x8021;
import cn.com.startai.mqttsdk.busi.entity.C_0x8022;
import cn.com.startai.mqttsdk.busi.entity.C_0x8023;
import cn.com.startai.mqttsdk.busi.entity.C_0x8024;
import cn.com.startai.mqttsdk.busi.entity.C_0x8025;
import cn.com.startai.mqttsdk.busi.entity.C_0x8026;
import cn.com.startai.mqttsdk.busi.entity.C_0x8027;
import cn.com.startai.mqttsdk.busi.entity.C_0x8028;
import cn.com.startai.mqttsdk.busi.entity.C_0x8031;
import cn.com.startai.mqttsdk.busi.entity.C_0x8033;
import cn.com.startai.mqttsdk.busi.entity.C_0x8034;
import cn.com.startai.mqttsdk.busi.entity.C_0x8200;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.utils.SStringUtils;

/**
 * Created by Robin on 2018/12/27.
 * qq: 419109715 彬影
 */

public class DistributeParam {

    public static boolean REGISTER_DISTRIBUTE = true;//注册
    public static boolean LOGIN_DISTRIBUTE = true;//登录
    public static boolean BIND_DISTRIBUTE = true;//添加设备或好友
    public static boolean UNBIND_DISTRIBUTE = true;//删除设备或好友
    public static boolean GETBINDLIST_DISTRIBUTE = true;//查询绑定关系
    public static boolean GETIDENTIFYCODE_DISTRIBUTE = true;//获取手机验证码
    public static boolean CHECKIDENTIFYCODE_DISTRIBUTE = true;//检验手机验证码
    public static boolean PASSTHROUGH_DISTRIBUTE = true;//消息透传
    public static boolean UNACTIVITE_DISTRIBUTE = true;//注销激活
    public static boolean UPDATEUSERINFO_DISTRIBUTE = true;//更新用户信息
    public static boolean UPDATEDEVICEINFO_DISTRIBUTE = true;//更新设备信息
    public static boolean GETUSERINFO_DISTRIBUTE = true;//查询用户信息
    public static boolean GETLATESTVERSION_DISTRIBUTE = true;//获取最新软件版本
    public static boolean UPDATEUSERPWD_DISTRIBUTE = true;//修改用户登录密码
    public static boolean SENDEMAIL_DISTRIBUTE = true;//请求发送邮件
    public static boolean UPDATEREMARK_DISTRIBUTE = true;//修改好友|设备 备注名
    public static boolean RESETMOBILELOGINPWD_DISTRIBUTE = true;//重置手机登录密码
    public static boolean LOGINWITHTHIRDACCOUNT_DISTRIBUTE = true;//第三方账号登录
    public static boolean THIRDPAYMENTUNIFIEDORDER_DISTRIBUTE = true;//第三方支付 统一下单
    public static boolean GETREALORDERPAYSTATUS_DISTRIBUTE = true;//查询真实订单支付结果
    public static boolean GETALIPAYAUTHINFO_DISTRIBUTE = true;//查询支付宝认证信息
    public static boolean BINDMOBILENUM_DISTRIBUTE = true;//关联手机号
    public static boolean UNBINDTHIRDACCOUNT_DISTRIBUTE = true;//解绑第三方账号
    public static boolean BINDTHIRDACCOUNT_DISTRIBUTE = true;//绑定第三方账号

}
