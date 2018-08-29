package cn.com.startai.mqttsdk.event;

import java.util.ArrayList;

import cn.com.startai.mqttsdk.busi.entity.C_0x8001;
import cn.com.startai.mqttsdk.busi.entity.C_0x8002;
import cn.com.startai.mqttsdk.busi.entity.C_0x8004;
import cn.com.startai.mqttsdk.busi.entity.C_0x8005;
import cn.com.startai.mqttsdk.busi.entity.C_0x8015;
import cn.com.startai.mqttsdk.busi.entity.C_0x8016;
import cn.com.startai.mqttsdk.busi.entity.C_0x8017;
import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.busi.entity.C_0x8020;
import cn.com.startai.mqttsdk.busi.entity.C_0x8021;
import cn.com.startai.mqttsdk.busi.entity.C_0x8023;
import cn.com.startai.mqttsdk.busi.entity.C_0x8024;
import cn.com.startai.mqttsdk.busi.entity.C_0x8025;
import cn.com.startai.mqttsdk.busi.entity.C_0x8026;
import cn.com.startai.mqttsdk.busi.entity.C_0x8200;

public abstract class AOnStartaiMessageArriveListener implements IOnMessageArriveListener {


    /**
     * 注册结果回调
     *
     * @param result    1 成功 0 失败
     * @param errorCode 失败异常码
     * @param errorMsg  抵账异常码描述
     */
    public void onRegisterResult(int result, String errorCode, String errorMsg, C_0x8017.Resp.ContentBean resp) {

    }

    /**
     * 添加好友回调
     *
     * @param result
     * @param id        自己的id
     * @param bebinding 被绑定者 开发者需要持久化，在向对端发送消息时需要携带此bebinding的id
     */
    public void onBindResult(int result, String errorCode, String errorMsg, String id, C_0x8002.Resp.ContentBean.BebindingBean bebinding) {

    }

    /**
     * 删除好友回调
     *
     * @param result     1 成功 0失败
     * @param errorCode  失败异常码
     * @param errorMsg   成功异常码
     * @param beUnbindid 解绑对端
     */
    public void onUnBindResult(int result, String errorCode, String errorMsg, String id, String beUnbindid) {
    }

    /**
     * 登录 回调
     *
     * @param result    0 失败| 1 成功
     * @param errorCode 失败的异常码 ，成功为""
     * @param errorMsg  失败的异常码描述 ， 成功为""
     */
    public void onLoginResult(int result, String errorCode, String errorMsg, C_0x8018.Resp.ContentBean loginInfo) {
    }

    /**
     * 获取绑定关系列表回调
     *
     * @param result    1成功 0失败
     * @param errorCode 失败异常码
     * @param id        自己的id
     * @param errorMsg  失败异常码描述
     * @param bindList  绑定列表
     */
    public void onGetBindListResult(int result, String errorCode, String errorMsg, String id, ArrayList<C_0x8005.Resp.ContentBean> bindList) {
    }

    /**
     * 设备激活回调，如果激活成功只会回调一次
     *
     * @param initResult 0 激活失败 |  1 成功
     * @param errcode    失败的异常码 ，成功为""
     * @param errmsg     失败的异常码描述 ， 成功为""
     */
    public void onActiviteResult(int initResult, String errcode, String errmsg) {
    }

    /**
     * 获取验证码结果
     *
     * @param result    1 成功 0失败
     * @param errorCode 失败时的异常码
     * @param errorMsg  失败异常码描述
     */
    public void onGetIdentifyCodeResult(int result, String errorCode, String errorMsg,C_0x8021.Resp.ContentBean contentBean) {
    }
    /**
     * 获取验证码结果
     *
     * @param result    1 成功 0失败
     * @param errorCode 失败时的异常码
     * @param errorMsg  失败异常码描述
     */
    @Deprecated
    public void onGetIdentifyCodeResult(int result, String errorCode, String errorMsg) {
    }

    /**
     * 检验验证码结果
     *
     * @param result    成功 1 失败 0
     * @param errorCode 失败异常码
     * @param errorMsg  失败异常码描述
     */
    public void onCheckIdetifyResult(int result, String errorCode, String errorMsg) {
    }


    /**
     * 消息透传结果
     *
     * @param result        1 成功 0失败
     * @param errorCode     失败的异常码
     * @param errorMsg      失败异常码描述
     * @param dataString    回调的strintg 内容
     * @param dataByteArray 回调的byte[] 内容
     */
    public void onPassthroughResult(int result, C_0x8200.Resp resp, String errorCode, String errorMsg, String dataString, byte[] dataByteArray) {

    }


    /**
     * 登出
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onLogoutResult(int result, String errorCode, String errorMsg) {


    }

    /**
     * 注销激活
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onUnActiviteResult(int result, String errorCode, String errorMsg) {

    }


    /**
     * 第三方硬件激活结果
     *
     * @param result      1成功 0失败
     * @param contentBean 设备参数
     * @param errorCode   失败异常码
     * @param errorMsg    失败异常描述
     */
    public void onHardwareActivateResult(int result, String errorCode, String errorMsg, C_0x8001.Resp.ContentBean contentBean) {

    }

    /**
     * 更新用户信息结果
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     * @param contentBean
     */
    public void onUpdateUserInfoResult(int result, String errorCode, String errorMsg, C_0x8020.Resp.ContentBean contentBean) {

    }

    /**
     * 查询用户信息结果
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     * @param contentBean
     */
    public void onGetUserInfoResult(int result, String errorCode, String errorMsg, C_0x8024.Resp.ContentBean contentBean) {

    }

    /**
     * 智能设备的连接状态变更
     *
     * @param userid 接收消息的userid
     * @param status 1 上线 0下线
     * @param sn     状态变更的设备sn
     */
    public void onDeviceConnectStatusChange(String userid, int status, String sn) {

    }

    /**
     * 查询最新软件版本结果
     *
     * @param result      1查询成功 0查询失败
     * @param errorCode   失败异常码
     * @param errorMsg    失败异常描述
     * @param contentBean 最新软件版本信息
     */
    public void onGetLatestVersionResult(int result, String errorCode, String errorMsg, C_0x8016.Resp.ContentBean contentBean) {

    }

    /**
     * 更新用户密码返回
     *
     * @param result      1成功 0失败
     * @param errorCode   失败异常码
     * @param errorMsg    失败异常描述
     * @param contentBean 用户密码信息
     */
    public void onUpdateUserPwdResult(int result, String errorCode, String errorMsg, C_0x8025.Resp.ContentBean contentBean) {

    }

    /**
     * 发送邮件结果返回
     *
     * @param result      1成功 0失败
     * @param errorCode   失败异常码
     * @param errorMsg    失败异常码描述
     * @param contentBean 成功的信息
     */
    public void onSendEmailResult(int result, String errorCode, String errorMsg, C_0x8023.Resp.ContentBean contentBean) {

    }

    /**
     * 修改备注名结果
     *
     * @param result      1 成功 0失败
     * @param errorCode   失败异常码
     * @param errorMsg    失败异常码
     * @param contentBean 成功内容
     */
    public void onUpdateRemarkResult(int result, String errorCode, String errorMsg, C_0x8015.Resp.ContentBean contentBean) {

    }

    /**
     * 重置手机登录密码结果
     *
     * @param result      1 成功 0失败
     * @param errorCode   失败异常码
     * @param errorMsg    失败异常码
     * @param contentBean 成功内容
     */
    public void onResetMobileLoginPwdResult(int result, String errorCode, String errorMsg, C_0x8026.Resp.ContentBean contentBean) {

    }

}
