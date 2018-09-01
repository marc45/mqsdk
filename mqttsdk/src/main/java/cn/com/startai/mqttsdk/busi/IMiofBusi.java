package cn.com.startai.mqttsdk.busi;


import cn.com.startai.mqttsdk.busi.entity.C_0x8001;
import cn.com.startai.mqttsdk.busi.entity.C_0x8020;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.MqttInitParam;

/**
 * Created by Robin on 2018/6/20.
 * qq: 419109715 彬影
 */

public interface IMiofBusi {

    /**
     * 注册
     *
     * @param uName    邮箱或手机号
     * @param pwd      密码
     * @param listener
     */
    void register(String uName, String pwd, IOnCallListener listener);


    /**
     * 登录
     * <p>
     * 在双重验证登录的时候 才会同时需要密码加验证码
     *
     * @param uName        邮箱或手机号
     * @param pwd          密码
     * @param identifyCode 验证码
     * @param listener
     */
    void login(String uName, String pwd, String identifyCode, IOnCallListener listener);

    /**
     * 添加设备或好友
     *
     * @param bebindingid 对端 的用户id或sn
     */
    @Deprecated
    void bind(String bebindingid, IOnCallListener listener);

    /**
     * 添加设备或好友
     *
     * @param bindingid   自己的userid
     * @param bebindingid 对端 的用户id或sn
     */
    void bind(String bindingid, String bebindingid, IOnCallListener listener);


    /**
     * 删除设备或好友
     *
     * @param bebindingid 对端的用户id 或 sn
     * @param listener
     */
    @Deprecated
    void unBind(String bebindingid, IOnCallListener listener);

    /**
     * 删除设备或好友
     *
     * @param bindingid   自己的userid
     * @param bebindingid 对端的用户id 或 sn
     * @param listener
     */
    void unBind(String bindingid, String bebindingid, IOnCallListener listener);

    /**
     * 查询绑定关系
     *
     * @param type 1.查询智能设备列表
     *             2.查询用户好友列表
     *             3.查询用户-手机列表
     *             4.查询所有的好友列表
     * @return
     */
    @Deprecated
    void getBindList(int type, IOnCallListener listener);

    /**
     * 查询绑定关系
     *
     * @param id   userid/sn
     * @param type 1.查询智能设备列表
     *             2.查询用户好友列表
     *             3.查询用户-手机列表
     *             4.查询所有的好友列表
     * @return
     */
    void getBindList(String id, int type, IOnCallListener listener);


    /**
     * 获取手机验证码
     *
     * @param mobile   手机号
     * @param type     类型
     *                 1表示用户登录
     *                 2表示修改登录密码
     *                 3表示用户注册
     * @param listener
     */
    void getIdentifyCode(String mobile, int type, IOnCallListener listener);


    /**
     * 检验手机验证码
     *
     * @param mobile       手机号
     * @param identifyCode 验证码
     * @param type         1表示用户登录
     *                     2表示修改登录密码
     *                     3表示用户注册
     * @param listener
     */
    void checkIdentifyCode(String mobile, String identifyCode, int type, IOnCallListener listener);

    /**
     * 消息透传
     *
     * @param toid        对端的userid或sn
     * @param dateByteArr 透传的数据
     */
    void passthrough(String toid, byte[] dateByteArr, IOnCallListener listener);

    /**
     * 消息透传
     *
     * @param toid        对端的userid或sn
     * @param dateByteArr 透传的数据
     */
    void passthrough(String toid, String dateByteArr, IOnCallListener listener);

    /**
     * 登出
     */
    void logout();

    /**
     * 注销激活
     */
    void unActivite(IOnCallListener listener);

    /**
     * 帮第三方设备激活
     */
    void hardwareActivate(C_0x8001.Req.ContentBean contentBean, IOnCallListener listener);

    /**
     * 更新用户信息
     *
     * @param contentBean
     * @param listener
     */
    void updateUserInfo(C_0x8020.Req.ContentBean contentBean, IOnCallListener listener);

    /**
     * 更新设备信息
     */
    void updateDeviceInfo(IOnCallListener listener);

    /**
     * 查询用户信息
     *
     * @param listener
     */
    void getUserInfo(IOnCallListener listener);

    /**
     * 查询用户信息
     *
     * @param userid
     * @param listener
     */
    void getUserInfo(String userid, IOnCallListener listener);


    /**
     * 获取最新软件版本
     *
     * @param os          系统 android linux
     * @param packageName 包名 如果是固件 packagname可不填写
     * @param listener
     */
    void getLatestVersion(String os, String packageName, IOnCallListener listener);


    /**
     * 修改用户登录密码
     *
     * @param oldPwd   老密码
     * @param newPwd   新密码
     * @param listener
     */
    @Deprecated
    void updateUserPwd(String oldPwd, String newPwd, IOnCallListener listener);


    /**
     * 修改用户登录密码
     *
     * @param userid   userid
     * @param oldPwd   老密码
     * @param newPwd   新密码
     * @param listener
     */
    void updateUserPwd(String userid, String oldPwd, String newPwd, IOnCallListener listener);

    /**
     * 请求发送邮件
     *
     * @param email 邮箱
     * @param type  类型 1 为重新发送激活邮件 2 为发送忘记密码邮件
     */
    void sendEmail(String email, int type, IOnCallListener listener);


    /**
     * 修改好友|设备 备注名
     *
     * @param fid      对端 id
     * @param remark   备注
     * @param listener
     */
    @Deprecated
    void updateRemark(String fid, String remark, IOnCallListener listener);

    /**
     * 修改好友|设备 备注名
     *
     * @param userid   自己id
     * @param fid      对端 id
     * @param remark   备注
     * @param listener
     */
    void updateRemark(String userid, String fid, String remark, IOnCallListener listener);


    /**
     * 重置手机登录密码
     *
     * @param mobile   手机号
     * @param pwd      登录密码
     * @param listener
     */
    @Deprecated
    void resetMobileLoginPwd(String mobile, String pwd, IOnCallListener listener);


}

