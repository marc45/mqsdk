package cn.com.startai.mqsdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.com.startai.mqsdk.busi.C_0x8101;
import cn.com.startai.mqsdk.listener.MySendListener;
import cn.com.startai.mqsdk.util.eventbus.E_0x8101_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_Conn_Break;
import cn.com.startai.mqsdk.util.eventbus.E_Conn_Failed;
import cn.com.startai.mqsdk.util.eventbus.E_Conn_Success;
import cn.com.startai.mqsdk.util.eventbus.EventBean;
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
import cn.com.startai.mqttsdk.busi.entity.C_0x8200;
import cn.com.startai.mqttsdk.event.AOnStartaiMessageArriveListener;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * Created by Robin on 2018/7/10.
 * qq: 419109715 彬影
 */

public class BaseActivity extends AppCompatActivity {


    static boolean connectStatus;

    static ArrayList<Activity> activityArrayList = new ArrayList<>();
    private static String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityArrayList.add(this);
        SLog.d(TAG, this.getClass().getSimpleName() + " added to activitylist" + " size = " + activityArrayList.size());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityArrayList.remove(this);
        SLog.d(TAG, this.getClass().getSimpleName() + " remove to activitylist" + " size = " + activityArrayList.size());
    }

    protected void finishAllActivity() {

        for (Activity activity : activityArrayList) {
            SLog.d(TAG, activity.getClass().getSimpleName() + " finish");
            activity.finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        StartAI.getInstance().getPersisitnet().getEventDispatcher().registerOnPushListener(pushListener);

    }

    @Override
    protected void onStop() {
        super.onStop();

        StartAI.getInstance().getPersisitnet().getEventDispatcher().unregisterOnPushListener(pushListener);

        EventBus.getDefault().unregister(this);
    }

    AOnStartaiMessageArriveListener pushListener = new AOnStartaiMessageArriveListener() {
        @Override
        public void onCommand(String topic, String msg) {

            try {
                JSONObject jsonObject = new JSONObject(msg);
                String msgtype = jsonObject.getString("msgtype");
                if ("0x8101".equals(msgtype)) {
                    C_0x8101.m_0x8101_resp(msg);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean needUISafety() {
            return true;
        }

        /**
         * 注册结果回调
         *
         * @param result    1 成功 0 失败
         * @param errorCode 失败异常码
         * @param errorMsg  抵账异常码描述
         * @param resp
         */
        @Override
        public void onRegisterResult(int result, String errorCode, String errorMsg, C_0x8017.Resp.ContentBean resp) {
            super.onRegisterResult(result, errorCode, errorMsg, resp);
            BaseActivity.this.onRegisterResult(result, errorCode, errorMsg, resp);
        }

        /**
         * 注册结果回调
         *
         * @param resp
         */
        @Override
        public void onRegisterResult(C_0x8017.Resp resp) {
            super.onRegisterResult(resp);
            BaseActivity.this.onRegisterResult(resp);
        }

        /**
         * 添加好友回调
         *
         * @param result
         * @param errorCode
         * @param errorMsg
         * @param id        自己的id
         * @param bebinding 被绑定者 开发者需要持久化，在向对端发送消息时需要携带此bebinding的id
         */
        @Override
        public void onBindResult(int result, String errorCode, String errorMsg, String id, C_0x8002.Resp.ContentBean.BebindingBean bebinding) {
            super.onBindResult(result, errorCode, errorMsg, id, bebinding);
            BaseActivity.this.onBindResult(result, errorCode, errorMsg, id, bebinding);
        }

        /**
         * 添加好友回调
         *
         * @param id        自己的id
         * @param bebinding 被绑定者 开发者需要持久化，在向对端发送消息时需要携带此bebinding的id
         */
        @Override
        public void onBindResult(C_0x8002.Resp resp, String id, C_0x8002.Resp.ContentBean.BebindingBean bebinding) {
            super.onBindResult(resp, id, bebinding);
            BaseActivity.this.onBindResult(resp, id, bebinding);
        }

        /**
         * 删除好友回调
         *
         * @param result     1 成功 0失败
         * @param errorCode  失败异常码
         * @param errorMsg   成功异常码
         * @param id
         * @param beUnbindid 解绑对端
         */
        @Override
        public void onUnBindResult(int result, String errorCode, String errorMsg, String id, String beUnbindid) {
            super.onUnBindResult(result, errorCode, errorMsg, id, beUnbindid);
            BaseActivity.this.onUnBindResult(result, errorCode, errorMsg, id, beUnbindid);
        }

        /**
         * 删除好友回调
         *
         * @param id
         * @param beUnbindid 解绑对端
         */
        @Override
        public void onUnBindResult(C_0x8004.Resp resp, String id, String beUnbindid) {
            super.onUnBindResult(resp, id, beUnbindid);
            BaseActivity.this.onUnBindResult(resp, id, beUnbindid);
        }

        /**
         * 登录 回调
         *
         * @param result    0 失败| 1 成功
         * @param errorCode 失败的异常码 ，成功为""
         * @param errorMsg  失败的异常码描述 ， 成功为""
         * @param loginInfo
         */
        @Override
        public void onLoginResult(int result, String errorCode, String errorMsg, C_0x8018.Resp.ContentBean loginInfo) {
            super.onLoginResult(result, errorCode, errorMsg, loginInfo);
            BaseActivity.this.onLoginResult(result, errorCode, errorMsg, loginInfo);
        }

        /**
         * 登录 回调
         *
         * @param resp
         */
        @Override
        public void onLoginResult(C_0x8018.Resp resp) {
            super.onLoginResult(resp);
            BaseActivity.this.onLoginResult(resp);
        }

        /**
         * 获取绑定关系列表回调
         *
         * @param result    1成功 0失败
         * @param errorCode 失败异常码
         * @param errorMsg  失败异常码描述
         * @param id        自己的id
         * @param bindList  绑定列表
         */
        @Override
        public void onGetBindListResult(int result, String errorCode, String errorMsg, String id, ArrayList<C_0x8005.Resp.ContentBean> bindList) {
            super.onGetBindListResult(result, errorCode, errorMsg, id, bindList);
            BaseActivity.this.onGetBindListResult(result, errorCode, errorMsg, id, bindList);
        }

        /**
         * 获取绑定关系列表回调
         *
         * @param result   1成功 0失败
         * @param respErr  失败异常描述
         * @param id       自己的id
         * @param bindList 绑定列表
         */
        @Override
        public void onGetBindListResult(int result, C_0x8005.RespErr respErr, String id, ArrayList<C_0x8005.Resp.ContentBean> bindList) {
            super.onGetBindListResult(result, respErr, id, bindList);
            BaseActivity.this.onGetBindListResult(result, respErr, id, bindList);
        }

        /**
         * 设备激活回调，如果激活成功只会回调一次
         *
         * @param initResult 0 激活失败 |  1 成功
         * @param errcode    失败的异常码 ，成功为""
         * @param errmsg     失败的异常码描述 ， 成功为""
         */
        @Override
        public void onActiviteResult(int initResult, String errcode, String errmsg) {
            super.onActiviteResult(initResult, errcode, errmsg);
            BaseActivity.this.onActiviteResult(initResult, errcode, errmsg);
        }

        /**
         * 设备激活回调，如果激活成功只会回调一次
         *
         * @param resp
         */
        @Override
        public void onActiviteResult(C_0x8001.Resp resp) {
            super.onActiviteResult(resp);
            BaseActivity.this.onActiviteResult(resp);
        }

        /**
         * 获取验证码结果
         *
         * @param result      1 成功 0失败
         * @param errorCode   失败时的异常码
         * @param errorMsg    失败异常码描述
         * @param contentBean
         */
        @Override
        public void onGetIdentifyCodeResult(int result, String errorCode, String errorMsg, C_0x8021.Resp.ContentBean contentBean) {
            super.onGetIdentifyCodeResult(result, errorCode, errorMsg, contentBean);
            BaseActivity.this.onGetIdentifyCodeResult(result, errorCode, errorMsg, contentBean);
        }

        /**
         * 获取验证码结果
         *
         * @param resp
         */
        @Override
        public void onGetIdentifyCodeResult(C_0x8021.Resp resp) {
            super.onGetIdentifyCodeResult(resp);
            BaseActivity.this.onGetIdentifyCodeResult(resp);
        }

        /**
         * 获取验证码结果
         *
         * @param result    1 成功 0失败
         * @param errorCode 失败时的异常码
         * @param errorMsg  失败异常码描述
         */
        @Override
        public void onGetIdentifyCodeResult(int result, String errorCode, String errorMsg) {
            super.onGetIdentifyCodeResult(result, errorCode, errorMsg);
            BaseActivity.this.onGetIdentifyCodeResult(result, errorCode, errorMsg);
        }

        /**
         * 检验验证码结果
         *
         * @param result    成功 1 失败 0
         * @param errorCode 失败异常码
         * @param errorMsg  失败异常码描述
         */
        @Override
        public void onCheckIdetifyResult(int result, String errorCode, String errorMsg) {
            super.onCheckIdetifyResult(result, errorCode, errorMsg);
            BaseActivity.this.onCheckIdetifyResult(result, errorCode, errorMsg);
        }

        /**
         * 检验验证码结果
         *
         * @param resp
         */
        @Override
        public void onCheckIdetifyResult(C_0x8022.Resp resp) {
            super.onCheckIdetifyResult(resp);
            BaseActivity.this.onCheckIdetifyResult(resp);
        }

        /**
         * 消息透传结果
         *
         * @param result        1 成功 0失败
         * @param resp
         * @param errorCode     失败的异常码
         * @param errorMsg      失败异常码描述
         * @param dataString    回调的strintg 内容
         * @param dataByteArray 回调的byte[] 内容
         */
        @Override
        public void onPassthroughResult(int result, C_0x8200.Resp resp, String errorCode, String errorMsg, String dataString, byte[] dataByteArray) {
            super.onPassthroughResult(result, resp, errorCode, errorMsg, dataString, dataByteArray);
            BaseActivity.this.onPassthroughResult(result, resp, errorCode, errorMsg, dataString, dataByteArray);
        }

        /**
         * 登出
         *
         * @param result
         * @param errorCode
         * @param errorMsg
         */
        @Override
        public void onLogoutResult(int result, String errorCode, String errorMsg) {
            super.onLogoutResult(result, errorCode, errorMsg);
            BaseActivity.this.onLogoutResult(result, errorCode, errorMsg);
        }

        /**
         * 注销激活
         *
         * @param result
         * @param errorCode
         * @param errorMsg
         */
        @Override
        public void onUnActiviteResult(int result, String errorCode, String errorMsg) {
            super.onUnActiviteResult(result, errorCode, errorMsg);
            BaseActivity.this.onUnActiviteResult(result, errorCode, errorMsg);
        }

        /**
         * 注销激活
         *
         * @param resp
         */
        @Override
        public void onUnActiviteResult(C_0x8003.Resp resp) {
            super.onUnActiviteResult(resp);
            BaseActivity.this.onUnActiviteResult(resp);
        }

        /**
         * 第三方硬件激活结果
         *
         * @param result      1成功 0失败
         * @param errorCode   失败异常码
         * @param errorMsg    失败异常描述
         * @param contentBean 设备参数
         */
        @Override
        public void onHardwareActivateResult(int result, String errorCode, String errorMsg, C_0x8001.Resp.ContentBean contentBean) {
            super.onHardwareActivateResult(result, errorCode, errorMsg, contentBean);
            BaseActivity.this.onHardwareActivateResult(result, errorCode, errorMsg, contentBean);
        }

        /**
         * 第三方硬件激活结果
         *
         * @param resp    设备参数
         */
        @Override
        public void onHardwareActivateResult(C_0x8001.Resp resp) {
            super.onHardwareActivateResult(resp);
            BaseActivity.this.onHardwareActivateResult(resp);
        }

        /**
         * 更新用户信息结果
         *
         * @param result
         * @param errorCode
         * @param errorMsg
         * @param contentBean
         */
        @Override
        public void onUpdateUserInfoResult(int result, String errorCode, String errorMsg, C_0x8020.Resp.ContentBean contentBean) {
            super.onUpdateUserInfoResult(result, errorCode, errorMsg, contentBean);
            BaseActivity.this.onUpdateUserInfoResult(result, errorCode, errorMsg, contentBean);
        }

        /**
         * 更新用户信息结果
         *
         * @param resp
         */
        @Override
        public void onUpdateUserInfoResult(C_0x8020.Resp resp) {
            super.onUpdateUserInfoResult(resp);
            BaseActivity.this.onUpdateUserInfoResult(resp);
        }

        /**
         * 查询用户信息结果
         *
         * @param result
         * @param errorCode
         * @param errorMsg
         * @param contentBean
         */
        @Override
        public void onGetUserInfoResult(int result, String errorCode, String errorMsg, C_0x8024.Resp.ContentBean contentBean) {
            super.onGetUserInfoResult(result, errorCode, errorMsg, contentBean);
            BaseActivity.this.onGetUserInfoResult(result, errorCode, errorMsg, contentBean);
        }

        /**
         * 查询用户信息结果
         *
         * @param resp
         */
        @Override
        public void onGetUserInfoResult(C_0x8024.Resp resp) {
            super.onGetUserInfoResult(resp);
            BaseActivity.this.onGetUserInfoResult(resp);
        }

        /**
         * 智能设备的连接状态变更
         *
         * @param userid 接收消息的userid
         * @param status 1 上线 0下线
         * @param sn     状态变更的设备sn
         */
        @Override
        public void onDeviceConnectStatusChange(String userid, int status, String sn) {
            super.onDeviceConnectStatusChange(userid, status, sn);
            BaseActivity.this.onDeviceConnectStatusChange(userid, status, sn);
        }

        /**
         * 查询最新软件版本结果
         *
         * @param result      1查询成功 0查询失败
         * @param errorCode   失败异常码
         * @param errorMsg    失败异常描述
         * @param contentBean 最新软件版本信息
         */
        @Override
        public void onGetLatestVersionResult(int result, String errorCode, String errorMsg, C_0x8016.Resp.ContentBean contentBean) {
            super.onGetLatestVersionResult(result, errorCode, errorMsg, contentBean);
            BaseActivity.this.onGetLatestVersionResult(result, errorCode, errorMsg, contentBean);
        }

        /**
         * 查询最新软件版本结果
         *
         * @param resp    最新软件版本信息
         */
        @Override
        public void onGetLatestVersionResult(C_0x8016.Resp resp) {
            super.onGetLatestVersionResult(resp);
            BaseActivity.this.onGetLatestVersionResult(resp);
        }

        /**
         * 更新用户密码返回
         *
         * @param result      1成功 0失败
         * @param errorCode   失败异常码
         * @param errorMsg    失败异常描述
         * @param contentBean 用户密码信息
         */
        @Override
        public void onUpdateUserPwdResult(int result, String errorCode, String errorMsg, C_0x8025.Resp.ContentBean contentBean) {
            super.onUpdateUserPwdResult(result, errorCode, errorMsg, contentBean);
            BaseActivity.this.onUpdateUserPwdResult(result, errorCode, errorMsg, contentBean);
        }

        /**
         * 更新用户密码返回
         *
         * @param resp    用户密码信息
         */
        @Override
        public void onUpdateUserPwdResult(C_0x8025.Resp resp) {
            super.onUpdateUserPwdResult(resp);
            BaseActivity.this.onUpdateUserPwdResult(resp);
        }

        /**
         * 发送邮件结果返回
         *
         * @param result      1成功 0失败
         * @param errorCode   失败异常码
         * @param errorMsg    失败异常码描述
         * @param contentBean 成功的信息
         */
        @Override
        public void onSendEmailResult(int result, String errorCode, String errorMsg, C_0x8023.Resp.ContentBean contentBean) {
            super.onSendEmailResult(result, errorCode, errorMsg, contentBean);
            BaseActivity.this.onSendEmailResult(result, errorCode, errorMsg, contentBean);
        }

        /**
         * 发送邮件结果返回
         *
         * @param resp    成功的信息
         */
        @Override
        public void onSendEmailResult(C_0x8023.Resp resp) {
            super.onSendEmailResult(resp);
            BaseActivity.this.onSendEmailResult(resp);
        }

        /**
         * 修改备注名结果
         *
         * @param result      1 成功 0失败
         * @param errorCode   失败异常码
         * @param errorMsg    失败异常码
         * @param contentBean 成功内容
         */
        @Override
        public void onUpdateRemarkResult(int result, String errorCode, String errorMsg, C_0x8015.Resp.ContentBean contentBean) {
            super.onUpdateRemarkResult(result, errorCode, errorMsg, contentBean);
            BaseActivity.this.onUpdateRemarkResult(result, errorCode, errorMsg, contentBean);
        }

        /**
         * 修改备注名结果
         *
         * @param resp    成功内容
         */
        @Override
        public void onUpdateRemarkResult(C_0x8015.Resp resp) {
            super.onUpdateRemarkResult(resp);
            BaseActivity.this.onUpdateRemarkResult(resp);
        }

        /**
         * 重置手机登录密码结果
         *
         * @param result      1 成功 0失败
         * @param errorCode   失败异常码
         * @param errorMsg    失败异常码
         * @param contentBean 成功内容
         */
        @Override
        public void onResetMobileLoginPwdResult(int result, String errorCode, String errorMsg, C_0x8026.Resp.ContentBean contentBean) {
            super.onResetMobileLoginPwdResult(result, errorCode, errorMsg, contentBean);
            BaseActivity.this.onResetMobileLoginPwdResult(result, errorCode, errorMsg, contentBean);
        }

        /**
         * 重置手机登录密码结果
         *
         * @param resp    成功内容
         */
        @Override
        public void onResetMobileLoginPwdResult(C_0x8026.Resp resp) {
            super.onResetMobileLoginPwdResult(resp);
            BaseActivity.this.onResetMobileLoginPwdResult(resp);
        }

    };


    public void onConnectFail(E_Conn_Failed e_conn_failed) {
        connectStatus = false;
    }

    public void onConnected(E_Conn_Success e_conn_success) {
        connectStatus = true;
    }

    public void onDisconnect(E_Conn_Break e_conn_break) {
        connectStatus = false;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventExecute(EventBean event) {

        String eventType = event.getEventType();


        switch (eventType) {

            case EventBean.S_2_A_0x8101_RESP:

                E_0x8101_Resp e_0x8101_resp = (E_0x8101_Resp) event.getEventContent();
                onVoiceSetResult(e_0x8101_resp);

                break;
            case EventBean.S_2_A_CONN_BREAK:
                E_Conn_Break e_conn_break = (E_Conn_Break) event.getEventContent();
                onDisconnect(e_conn_break);
                break;
            case EventBean.S_2_A_CONN_FAILED:
                E_Conn_Failed e_conn_failed = (E_Conn_Failed) event.getEventContent();
                onConnectFail(e_conn_failed);
                break;
            case EventBean.S_2_A_CONN_SUCCESS:
                E_Conn_Success e_conn_success = (E_Conn_Success) event.getEventContent();
                onConnected(e_conn_success);
                break;
            case EventBean.S_2_A_TOKEN_EXPIRE:
                C_0x8018.Resp.ContentBean contentBean = (C_0x8018.Resp.ContentBean) event.getEventContent();
                tokenExpire(contentBean);
                break;

            default:
                break;
        }

    }

    private void tokenExpire(C_0x8018.Resp.ContentBean contentBean) {

        finishAllActivity();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void onVoiceSetResult(E_0x8101_Resp e_0x8101_resp) {

    }


    /**
     * 注册结果回调
     *
     * @param result    1 成功 0 失败
     * @param errorCode 失败异常码
     * @param errorMsg  抵账异常码描述
     * @param resp
     */
    public void onRegisterResult(int result, String errorCode, String errorMsg, C_0x8017.Resp.ContentBean resp) {
    }

    /**
     * 注册结果回调
     *
     * @param resp
     */
    public void onRegisterResult(C_0x8017.Resp resp) {
    }

    /**
     * 添加好友回调
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     * @param id        自己的id
     * @param bebinding 被绑定者 开发者需要持久化，在向对端发送消息时需要携带此bebinding的id
     */
    public void onBindResult(int result, String errorCode, String errorMsg, String id, C_0x8002.Resp.ContentBean.BebindingBean bebinding) {
    }

    /**
     * 添加好友回调
     *
     * @param id        自己的id
     * @param bebinding 被绑定者 开发者需要持久化，在向对端发送消息时需要携带此bebinding的id
     */
    public void onBindResult(C_0x8002.Resp resp, String id, C_0x8002.Resp.ContentBean.BebindingBean bebinding) {
    }

    /**
     * 删除好友回调
     *
     * @param result     1 成功 0失败
     * @param errorCode  失败异常码
     * @param errorMsg   成功异常码
     * @param id
     * @param beUnbindid 解绑对端
     */
    public void onUnBindResult(int result, String errorCode, String errorMsg, String id, String beUnbindid) {
    }

    /**
     * 删除好友回调
     *
     * @param id
     * @param beUnbindid 解绑对端
     */
    public void onUnBindResult(C_0x8004.Resp resp, String id, String beUnbindid) {
    }

    /**
     * 登录 回调
     *
     * @param result    0 失败| 1 成功
     * @param errorCode 失败的异常码 ，成功为""
     * @param errorMsg  失败的异常码描述 ， 成功为""
     * @param loginInfo
     */
    public void onLoginResult(int result, String errorCode, String errorMsg, C_0x8018.Resp.ContentBean loginInfo) {
    }

    /**
     * 登录 回调
     *
     * @param resp
     */
    public void onLoginResult(C_0x8018.Resp resp) {
    }

    /**
     * 获取绑定关系列表回调
     *
     * @param result    1成功 0失败
     * @param errorCode 失败异常码
     * @param errorMsg  失败异常码描述
     * @param id        自己的id
     * @param bindList  绑定列表
     */
    public void onGetBindListResult(int result, String errorCode, String errorMsg, String id, ArrayList<C_0x8005.Resp.ContentBean> bindList) {
    }

    /**
     * 获取绑定关系列表回调
     *
     * @param result   1成功 0失败
     * @param respErr  失败异常描述
     * @param id       自己的id
     * @param bindList 绑定列表
     */
    public void onGetBindListResult(int result, C_0x8005.RespErr respErr, String id, ArrayList<C_0x8005.Resp.ContentBean> bindList) {
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
     * 设备激活回调，如果激活成功只会回调一次
     *
     * @param resp
     */
    public void onActiviteResult(C_0x8001.Resp resp) {
    }

    /**
     * 获取验证码结果
     *
     * @param result      1 成功 0失败
     * @param errorCode   失败时的异常码
     * @param errorMsg    失败异常码描述
     * @param contentBean
     */
    public void onGetIdentifyCodeResult(int result, String errorCode, String errorMsg, C_0x8021.Resp.ContentBean contentBean) {
    }

    /**
     * 获取验证码结果
     *
     * @param resp
     */
    public void onGetIdentifyCodeResult(C_0x8021.Resp resp) {
    }

    /**
     * 获取验证码结果
     *
     * @param result    1 成功 0失败
     * @param errorCode 失败时的异常码
     * @param errorMsg  失败异常码描述
     */
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
     * 检验验证码结果
     *
     * @param resp
     */
    public void onCheckIdetifyResult(C_0x8022.Resp resp) {
    }

    /**
     * 消息透传结果
     *
     * @param result        1 成功 0失败
     * @param resp
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
     * 注销激活
     *
     * @param resp
     */
    public void onUnActiviteResult(C_0x8003.Resp resp) {
    }

    /**
     * 第三方硬件激活结果
     *
     * @param result      1成功 0失败
     * @param errorCode   失败异常码
     * @param errorMsg    失败异常描述
     * @param contentBean 设备参数
     */
    public void onHardwareActivateResult(int result, String errorCode, String errorMsg, C_0x8001.Resp.ContentBean contentBean) {
    }

    /**
     * 第三方硬件激活结果
     *
     * @param resp 设备参数
     */
    public void onHardwareActivateResult(C_0x8001.Resp resp) {
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
     * 更新用户信息结果
     *
     * @param resp
     */
    public void onUpdateUserInfoResult(C_0x8020.Resp resp) {
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
     * 查询用户信息结果
     *
     * @param resp
     */
    public void onGetUserInfoResult(C_0x8024.Resp resp) {
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
     * 查询最新软件版本结果
     *
     * @param resp 最新软件版本信息
     */
    public void onGetLatestVersionResult(C_0x8016.Resp resp) {
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
     * 更新用户密码返回
     *
     * @param resp 用户密码信息
     */
    public void onUpdateUserPwdResult(C_0x8025.Resp resp) {
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
     * 发送邮件结果返回
     *
     * @param resp 成功的信息
     */
    public void onSendEmailResult(C_0x8023.Resp resp) {
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
     * 修改备注名结果
     *
     * @param resp 成功内容
     */
    public void onUpdateRemarkResult(C_0x8015.Resp resp) {
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

    /**
     * 重置手机登录密码结果
     *
     * @param resp 成功内容
     */
    public void onResetMobileLoginPwdResult(C_0x8026.Resp resp) {
    }


    /**
     * 消息发送的结果回调
     */
    IOnCallListener onCallListener = new MySendListener();
}
