package cn.com.startai.mqttsdk.event;


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
import cn.com.startai.mqttsdk.busi.entity.C_0x8028;
import cn.com.startai.mqttsdk.busi.entity.C_0x8031;
import cn.com.startai.mqttsdk.busi.entity.C_0x8033;
import cn.com.startai.mqttsdk.busi.entity.C_0x8034;
import cn.com.startai.mqttsdk.busi.entity.C_0x8035;
import cn.com.startai.mqttsdk.busi.entity.C_0x8036;
import cn.com.startai.mqttsdk.busi.entity.C_0x8037;
import cn.com.startai.mqttsdk.busi.entity.C_0x8038;
import cn.com.startai.mqttsdk.busi.entity.C_0x8039;
import cn.com.startai.mqttsdk.busi.entity.C_0x8200;

public interface IOnStartaiMsgArriveListener extends IOnMessageArriveListener {


    /**
     * 设备激活回调，如果激活成功只会回调一次
     */
    void onActiviteResult(C_0x8001.Resp resp);

    /**
     * 第三方硬件激活结果
     */
    void onHardwareActivateResult(C_0x8001.Resp resp);

    /**
     * 添加好友回调
     *
     * @param id        自己的id
     * @param bebinding 被绑定者 开发者需要持久化，在向对端发送消息时需要携带此bebinding的id
     */
    void onBindResult(C_0x8002.Resp resp, String id, C_0x8002.Resp.ContentBean.BebindingBean bebinding);

    /**
     * 注销激活
     */
    void onUnActiviteResult(C_0x8003.Resp resp);


    /**
     * 删除好友回调
     *
     * @param beUnbindid 解绑对端
     */
    void onUnBindResult(C_0x8004.Resp resp, String id, String beUnbindid);


    /**
     * 获取绑定关系列表回调
     */
    void onGetBindListResult(C_0x8005.Response response);

    /**
     * 修改备注名结果
     *
     * @param resp 成功内容
     */
    void onUpdateRemarkResult(C_0x8015.Resp resp);

    /**
     * 查询最新软件版本结果
     *
     * @param resp 最新软件版本信息
     */
    void onGetLatestVersionResult(C_0x8016.Resp resp);

    /**
     * 注册结果回调
     */
    void onRegisterResult(C_0x8017.Resp resp);

    /**
     * 登录 回调
     */
    void onLoginResult(C_0x8018.Resp resp);

    /**
     * 更新用户信息结果
     *
     * @param resp
     */
    void onUpdateUserInfoResult(C_0x8020.Resp resp);

    /**
     * 获取验证码结果
     */
    void onGetIdentifyCodeResult(C_0x8021.Resp resp);


    /**
     * 检验验证码结果
     */
    void onCheckIdetifyResult(C_0x8022.Resp resp);


    /**
     * 发送邮件结果返回
     *
     * @param resp 成功的信息
     */
    void onSendEmailResult(C_0x8023.Resp resp);


    /**
     * 查询用户信息结果
     *
     * @param resp
     */
    void onGetUserInfoResult(C_0x8024.Resp resp);


    /**
     * 更新用户密码返回
     *
     * @param resp 用户密码信息
     */
    void onUpdateUserPwdResult(C_0x8025.Resp resp);


    /**
     * 重置登录密码结果
     *
     * @param resp 成功内容
     */
    void onResetLoginPwdResult(C_0x8026.Resp resp);

    /**
     * 第三方支付 统一下单结果
     *
     * @param resp
     */
    void onThirdPaymentUnifiedOrderResult(C_0x8028.Resp resp);

    /**
     * 查询真实订单支付结果
     *
     * @param resp
     */
    void onGetRealOrderPayStatus(C_0x8031.Resp resp);

    /**
     * 查询支付宝认证信息
     *
     * @param resp
     */
    void onGetAlipayAuthInfoResult(C_0x8033.Resp resp);

    /**
     * 绑定手机号返回
     *
     * @param resp
     */
    void onBindMobileNumResult(C_0x8034.Resp resp);

    /**
     * 查询天气信息返回
     *
     * @param resp
     */
    void onGetWeatherInfoResult(C_0x8035.Resp resp);

    /**
     * 解绑第三方账号返回
     *
     * @param resp
     */
    void onUnBindThirdAccountResult(C_0x8036.Resp resp);

    /**
     * 绑定第三方账号返回
     *
     * @param resp
     */
    void onBindThirdAccountResult(C_0x8037.Resp resp);


    /**
     * 获取绑定列表 分页
     *
     * @param resp
     */
    void onGetBindListByPageResult(C_0x8038.Resp resp);

    /**
     * 绑定邮箱返回
     *
     * @param resp
     */
    void onBindEmailResult(C_0x8039.Resp resp);


    /**
     * 消息透传结果
     *
     * @param dataString    回调的strintg 内容
     * @param dataByteArray 回调的byte[] 内容
     */
    void onPassthroughResult(C_0x8200.Resp resp, String dataString, byte[] dataByteArray);


    /**
     * 登出
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    void onLogoutResult(int result, String errorCode, String errorMsg);


    /**
     * 智能设备的连接状态变更
     *
     * @param userid 接收消息的userid
     * @param status 1 上线 0下线
     * @param sn     状态变更的设备sn
     */
    void onDeviceConnectStatusChange(String userid, int status, String sn);

}
