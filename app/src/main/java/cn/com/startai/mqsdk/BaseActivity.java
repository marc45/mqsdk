package cn.com.startai.mqsdk;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cn.com.startai.mqsdk.listener.MySendListener;
import cn.com.startai.mqsdk.util.eventbus.E_0x8001_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8001_Resp_;
import cn.com.startai.mqsdk.util.eventbus.E_0x8002_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8003_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8004_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8005_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8015_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8016_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8017_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8018_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8018_Resp_;
import cn.com.startai.mqsdk.util.eventbus.E_0x8020_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8021_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8022_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8023_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8024_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8025_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8026_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8101_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8200_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_Conn_Break;
import cn.com.startai.mqsdk.util.eventbus.E_Conn_Failed;
import cn.com.startai.mqsdk.util.eventbus.E_Conn_Success;
import cn.com.startai.mqsdk.util.eventbus.E_Device_Connect_Status;
import cn.com.startai.mqsdk.util.eventbus.EventBean;
import cn.com.startai.mqttsdk.busi.entity.C_0x8016;
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

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventExecute(EventBean event) {

        String eventType = event.getEventType();

        switch (eventType) {
            case EventBean.S_2_A_ACTIVATE_RESULT:
                E_0x8001_Resp e_0x8001_resp = (E_0x8001_Resp) event.getEventContent();
                onActiviateResult(e_0x8001_resp);
                break;
            case EventBean.S_2_A_BIND_RESULT:
                E_0x8002_Resp e_0x8002_resp = (E_0x8002_Resp) event.getEventContent();
                onBindResult(e_0x8002_resp);
                break;
            case EventBean.S_2_A_BINDLIST_RESULT:
                E_0x8005_Resp e_0x8005_resp = (E_0x8005_Resp) event.getEventContent();
                onBindListResult(e_0x8005_resp);
                break;
            case EventBean.S_2_A_CHECK_IDENTIFY_RESULT:
                E_0x8022_Resp e_0x8022_resp = (E_0x8022_Resp) event.getEventContent();
                onCheckIdentifyResult(e_0x8022_resp);
                break;
            case EventBean.S_2_A_CUSTOMER_ACTIVATE_RESULT:
                E_0x8001_Resp_ e_0x8001_resp_ = (E_0x8001_Resp_) event.getEventContent();
                onHardwareActivateResult(e_0x8001_resp_);
                break;
            case EventBean.S_2_A_GET_IDENTIFY_RESULT:
                E_0x8021_Resp e_0x8021_resp = (E_0x8021_Resp) event.getEventContent();
                onGetIdentifyResult(e_0x8021_resp);
                break;
            case EventBean.S_2_A_LOGIN_RESULT:
                E_0x8018_Resp e_0x8018_resp = (E_0x8018_Resp) event.getEventContent();
                onLoginResult(e_0x8018_resp);
                break;
            case EventBean.S_2_A_LOGOUT_RESULT:
                E_0x8018_Resp_ e_0x8018_resp_ = (E_0x8018_Resp_) event.getEventContent();
                onLogoutResult(e_0x8018_resp_);
                break;
            case EventBean.S_2_A_REGISTER_RESULT:
                E_0x8017_Resp e_0x8017_resp = (E_0x8017_Resp) event.getEventContent();
                onRegisterResult(e_0x8017_resp);
                break;
            case EventBean.S_2_A_UN_ACTIVITE_RESULT:
                E_0x8003_Resp e_0x8003_resp1 = (E_0x8003_Resp) event.getEventContent();

                onUnActiviteResult(e_0x8003_resp1);
                break;
            case EventBean.S_2_A_UNBIND_RESULT:
                E_0x8004_Resp e_0x8004_resp = (E_0x8004_Resp) event.getEventContent();
                onUnBindResult(e_0x8004_resp);
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
            case EventBean.S_2_A_PASSTHROUTH_RESULT:
                E_0x8200_Resp e_0x8200_resp = (E_0x8200_Resp) event.getEventContent();
                onPassthrouthResult(e_0x8200_resp);
                break;

            case EventBean.S_2_A_0x8101_RESP:
                E_0x8101_Resp e_0x8101_resp = (E_0x8101_Resp) event.getEventContent();

                on_0x8101_resp(e_0x8101_resp);
                break;
            case EventBean.S_2_A_UPDATEUSEINFO_RESULT:

                E_0x8020_Resp e_0x8020_resp = (E_0x8020_Resp) event.getEventContent();
                onUpdateUserInfoResult(e_0x8020_resp);
                break;
            case EventBean.S_2_A_GETUSERINFO_RESULT:
                E_0x8024_Resp e_0x8024_resp = (E_0x8024_Resp) event.getEventContent();
                onGetUserInfoResult(e_0x8024_resp);
                break;
            case EventBean.S_2_A_DEVICE_CONNECT_STATUS_CHANGE:
                E_Device_Connect_Status e_device_connect_status = (E_Device_Connect_Status) event.getEventContent();
                onDeviceConnectStatusChange(e_device_connect_status);
                break;
            case EventBean.S_2_A_GETLATEST_VERSION_RESULT:

                E_0x8016_Resp e_0x8016_resp = (E_0x8016_Resp) event.getEventContent();
                onGetLatestVersionResult(e_0x8016_resp);
                break;
            case EventBean.S_2_A_UPDATE_USER_PWD:


                E_0x8025_Resp e_0x8025_resp = (E_0x8025_Resp) event.getEventContent();
                onUpdatePwdResult(e_0x8025_resp);
                break;

            case EventBean.S_2_A_UPDATE_REMARK:
                E_0x8015_Resp e_0x8015_resp = (E_0x8015_Resp) event.getEventContent();
                onUpdateRemarkResult(e_0x8015_resp);
                break;

            case EventBean.S_2_A_SEND_EMAIL_RESULT:
                E_0x8023_Resp e_0x8023_resp = (E_0x8023_Resp) event.getEventContent();
                onSendEmailResult(e_0x8023_resp);
                break;

            case EventBean.S_2_A_RESET_MOBILE_LOGIN_PWD:
                E_0x8026_Resp e_0x8026_resp = (E_0x8026_Resp) event.getEventContent();
                onResetMobileLoginPwdResult(e_0x8026_resp);
                break;
        }
    }

    public void onSendEmailResult(E_0x8023_Resp resp) {

    }

    public void onResetMobileLoginPwdResult(E_0x8026_Resp resp) {

    }

    public void onUpdateRemarkResult(E_0x8015_Resp e_0x8015_resp) {

    }

    public void onUpdatePwdResult(E_0x8025_Resp e_0x8025_resp) {

    }


    public void onGetLatestVersionResult(E_0x8016_Resp e_0x8016_resp) {

    }

    public void onDeviceConnectStatusChange(E_Device_Connect_Status e_device_connect_status) {

    }

    public void onGetUserInfoResult(E_0x8024_Resp resp) {

    }

    public void onUpdateUserInfoResult(E_0x8020_Resp resp) {

    }

    public void on_0x8101_resp(E_0x8101_Resp resp) {

    }

    public void onBindResult(E_0x8002_Resp resp) {

    }

    public void onLoginResult(E_0x8018_Resp resp) {

    }

    public void onUnBindResult(E_0x8004_Resp resp) {

    }

    public void onRegisterResult(E_0x8017_Resp resp) {

    }

    public void onGetIdentifyResult(E_0x8021_Resp resp) {

    }

    public void onCheckIdentifyResult(E_0x8022_Resp resp) {

    }

    public void onLogoutResult(E_0x8018_Resp_ resp_) {

    }

    public void onUnActiviteResult(E_0x8003_Resp resp) {

    }

    public void onActiviateResult(E_0x8001_Resp resp) {

    }

    public void onHardwareActivateResult(E_0x8001_Resp_ resp_) {

    }

    public void onPassthrouthResult(E_0x8200_Resp resp) {

    }

    public void onBindListResult(E_0x8005_Resp resp) {

    }


    public void onConnectFail(E_Conn_Failed e_conn_failed) {
        connectStatus = false;
    }

    public void onConnected(E_Conn_Success e_conn_success) {
        connectStatus = true;
    }

    public void onDisconnect(E_Conn_Break e_conn_break) {
        connectStatus = false;
    }


    /**
     * 消息发送的结果回调
     */
    IOnCallListener onCallListener = new MySendListener();
}
