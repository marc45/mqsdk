package cn.com.startai.mqttsdk.busi;

import cn.com.startai.mqttsdk.StartAI;
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
import cn.com.startai.mqttsdk.busi.entity.C_0x8200;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.utils.SStringUtils;

/**
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class BaseBusiManager implements IMiofBusi {
    private static final BaseBusiManager ourInstance = new BaseBusiManager();

    public static BaseBusiManager getInstance() {
        return ourInstance;
    }

    protected BaseBusiManager() {
    }


    /**
     * 注册
     *
     * @param uName    邮箱或手机号
     * @param pwd      密码
     * @param listener
     */
    @Override
    public void register(String uName, String pwd, IOnCallListener listener) {
        C_0x8017.m_0x8017_req(uName, pwd, listener);
    }


    /**
     * 登录
     *
     * @param uName        邮箱或手机号
     * @param pwd          密码
     * @param identifyCode 验证码
     * @param listener
     */
    @Override
    public void login(String uName, String pwd, String identifyCode, IOnCallListener listener) {
        C_0x8018.m_0x8018_req(uName, pwd, identifyCode, listener);
    }


    /**
     * 添加设备或好友
     *
     * @param bebindingid 对端 的用户id或sn
     */
    @Override
    public void bind(String bebindingid, IOnCallListener listener) {

        bind("", bebindingid, listener);
    }

    /**
     * 添加设备或好友
     *
     * @param bindingid   自己的userid
     * @param bebindingid 对端 的用户id或sn
     * @param listener
     */
    @Override
    public void bind(String bindingid, String bebindingid, IOnCallListener listener) {

        C_0x8002.m_0x8002_req(bindingid, bebindingid, listener);
    }


    /**
     * 删除设备或好友
     *
     * @param bebindingid 对端的用户id 或 sn
     * @param listener
     */
    @Override
    public void unBind(String bebindingid, IOnCallListener listener) {
        unBind("", bebindingid, listener);
    }

    /**
     * 删除设备或好友
     *
     * @param bindingid   自己的userid
     * @param bebindingid 对端的用户id 或 sn
     * @param listener
     */
    @Override
    public void unBind(String bindingid, String bebindingid, IOnCallListener listener) {
        C_0x8004.m_0x8004_req(bindingid, bebindingid, listener);
    }

    /**
     * 查询绑定关系
     *
     * @param type 1.查询用户绑定的设备
     *             2.查询设备的用户列表
     *             3.查询用户的用户好友
     *             4.查询设备的设备列表
     *             5.查询用户的手机列表
     *             6.查询手机的用户好友
     *             7.查询所有
     * @return
     */
    @Override
    public void getBindList(int type, IOnCallListener listener) {
        getBindList("", type, listener);
    }

    /**
     * 查询绑定关系
     *
     * @param id       userid/sn
     * @param type     1.查询智能设备列表
     *                 2.查询用户好友列表
     *                 3.查询用户-手机列表
     *                 4.查询所有的好友列表
     * @param listener
     * @return
     */
    @Override
    public void getBindList(String id, int type, IOnCallListener listener) {
        C_0x8005.m_0x8005_req(id, type, listener);
    }


    /**
     * 获取手机验证码
     *
     * @param mobile   手机号
     * @param type     类型
     *                 1表示用户登录
     *                 2表示重置登录密码
     *                 3表示用户注册
     * @param listener
     */
    @Override
    public void getIdentifyCode(String mobile, int type, IOnCallListener listener) {
        C_0x8021.m_0x8021_req(mobile, type, listener);
    }


    /**
     * 检验手机验证码
     *
     * @param mobile       手机号
     * @param identifyCode 验证码
     * @param type         1表示用户登录
     *                     2表示重置登录密码
     *                     3表示用户注册
     * @param listener
     */
    @Override
    public void checkIdentifyCode(String mobile, String identifyCode, int type, IOnCallListener listener) {
        C_0x8022.m_0x8022_req(mobile, identifyCode, type, listener);
    }

    /**
     * 消息透传
     *
     * @param toid        对端的userid或sn
     * @param dateByteArr 透传的数据
     */
    @Override
    public void passthrough(String toid, byte[] dateByteArr, IOnCallListener listener) {


        passthrough(toid, SStringUtils.byteArr2HexStr(dateByteArr), listener);

    }

    /**
     * 消息透传
     *
     * @param toid        对端的 toid
     * @param dateByteArr 透传的数据
     */
    @Override
    public void passthrough(String toid, String dateByteArr, IOnCallListener listener) {

        C_0x8200.m_0x8200_req(toid, dateByteArr, listener);

    }


    /**
     * 登出
     */
    @Override
    public void logout() {
        C_0x8018.logout();
    }

    @Override
    public void unActivite(IOnCallListener listener) {
        C_0x8003.m_0x8003_req(listener);
    }

    /**
     * 给智能硬件激活
     *
     * @param contentBean
     * @param listener
     */
    @Override
    public void hardwareActivate(C_0x8001.Req.ContentBean contentBean, IOnCallListener listener) {
        C_0x8001.m_0x8001_req(contentBean, listener);
    }

    @Override
    public void updateUserInfo(C_0x8020.Req.ContentBean contentBean, IOnCallListener listener) {
        C_0x8020.m_0x8020_req(contentBean, listener);
    }

    /**
     * 更新设备信息
     *
     * @param listener
     */
    @Override
    public void updateDeviceInfo(IOnCallListener listener) {

    }

    /**
     * 获取用户信息
     */
    @Override
    public void getUserInfo(IOnCallListener listener) {
        getUserInfo("", listener);

    }

    /**
     * 查询用户信息
     *
     * @param userid
     * @param listener
     */
    @Override
    public void getUserInfo(String userid, IOnCallListener listener) {
        C_0x8024.m_0x8024_req(userid, listener);
    }

    /**
     * 查询最新软件 版本号
     *
     * @param listener
     */
    @Override
    public void getLatestVersion(String os, String packageName, IOnCallListener listener) {
        C_0x8016.m_0x8016_req(os, packageName, listener);
    }

    /**
     * 更新用户登录 密码
     *
     * @param oldPwd   老密码
     * @param newPwd   新密码
     * @param listener
     */
    @Override
    public void updateUserPwd(String oldPwd, String newPwd, IOnCallListener listener) {
        updateUserPwd("", oldPwd, newPwd, listener);
    }

    /**
     * 修改用户登录密码
     *
     * @param userid   userid
     * @param oldPwd   老密码
     * @param newPwd   新密码
     * @param listener
     */
    @Override
    public void updateUserPwd(String userid, String oldPwd, String newPwd, IOnCallListener listener) {
        C_0x8025.m_0x8025_req(userid, oldPwd, newPwd, listener);
    }


    /**
     * 请求发送邮件
     *
     * @param email 邮箱
     * @param type  类型 1 为重新发送激活邮件 2 为发送忘记密码邮件
     */
    @Override
    public void sendEmail(String email, int type, IOnCallListener listener) {
        C_0x8023.m_0x8023_req(email, type, listener);
    }

    /**
     * 修改备注名
     *
     * @param fid      对端的sn
     * @param remark   备注名
     * @param listener
     */
    @Override
    public void updateRemark(String fid, String remark, IOnCallListener listener) {
        updateRemark("", fid, remark, listener);
    }

    /**
     * 修改好友|设备 备注名
     *
     * @param userid   自己id
     * @param fid      对端 id
     * @param remark   备注
     * @param listener
     */
    @Override
    public void updateRemark(String userid, String fid, String remark, IOnCallListener listener) {
        C_0x8015.m_0x8015_req(userid, fid, remark, listener);
    }

    /**
     * 重置手机登录密码
     *
     * @param mobile   手机号
     * @param pwd      登录密码
     * @param listener
     */
    @Override
    public void resetMobileLoginPwd(String mobile, String pwd, IOnCallListener listener) {
        C_0x8026.m_0x8026_req(mobile, pwd, listener);
    }

    /**
     * 第三方账号登录
     *
     * @param type     类型 1 微信登录 2 支付宝登录
     * @param code
     * @param listener
     */
    @Override
    public void loginWithThirdAccount(int type, String code, IOnCallListener listener) {
        C_0x8027.req(type, code, listener);
    }


}

