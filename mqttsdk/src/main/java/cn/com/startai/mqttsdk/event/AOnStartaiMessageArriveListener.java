package cn.com.startai.mqttsdk.event;

import java.util.ArrayList;

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

public abstract class AOnStartaiMessageArriveListener implements IOnStartaiMsgArriveListener {


    @Override
    public void onActiviteResult(C_0x8001.Resp resp) {

    }

    @Override
    public void onHardwareActivateResult(C_0x8001.Resp resp) {

    }

    @Override
    public void onBindResult(C_0x8002.Resp resp, String id, C_0x8002.Resp.ContentBean.BebindingBean bebinding) {

    }

    @Override
    public void onUnActiviteResult(C_0x8003.Resp resp) {

    }

    @Override
    public void onUnBindResult(C_0x8004.Resp resp, String id, String beUnbindid) {

    }

    @Override
    public void onGetBindListResult(C_0x8005.Response response) {

    }

    @Override
    public void onUpdateRemarkResult(C_0x8015.Resp resp) {

    }

    @Override
    public void onGetLatestVersionResult(C_0x8016.Resp resp) {

    }

    @Override
    public void onRegisterResult(C_0x8017.Resp resp) {

    }

    @Override
    public void onLoginResult(C_0x8018.Resp resp) {

    }

    @Override
    public void onUpdateUserInfoResult(C_0x8020.Resp resp) {

    }

    @Override
    public void onGetIdentifyCodeResult(C_0x8021.Resp resp) {

    }

    @Override
    public void onCheckIdetifyResult(C_0x8022.Resp resp) {

    }

    @Override
    public void onSendEmailResult(C_0x8023.Resp resp) {

    }

    @Override
    public void onGetUserInfoResult(C_0x8024.Resp resp) {

    }

    @Override
    public void onUpdateUserPwdResult(C_0x8025.Resp resp) {

    }

    @Override
    public void onResetLoginPwdResult(C_0x8026.Resp resp) {

    }

    @Override
    public void onThirdPaymentUnifiedOrderResult(C_0x8028.Resp resp) {

    }

    @Override
    public void onGetRealOrderPayStatus(C_0x8031.Resp resp) {

    }

    @Override
    public void onGetAlipayAuthInfoResult(C_0x8033.Resp resp) {

    }

    @Override
    public void onBindMobileNumResult(C_0x8034.Resp resp) {

    }

    @Override
    public void onGetWeatherInfoResult(C_0x8035.Resp resp) {

    }

    @Override
    public void onUnBindThirdAccountResult(C_0x8036.Resp resp) {

    }

    @Override
    public void onBindThirdAccountResult(C_0x8037.Resp resp) {

    }

    @Override
    public void onGetBindListByPageResult(C_0x8038.Resp resp) {

    }

    @Override
    public void onBindEmailResult(C_0x8039.Resp resp) {

    }

    @Override
    public void onPassthroughResult(C_0x8200.Resp resp, String dataString, byte[] dataByteArray) {

    }

    @Override
    public void onLogoutResult(int result, String errorCode, String errorMsg) {

    }

    @Override
    public void onDeviceConnectStatusChange(String userid, int status, String sn) {

    }

    @Override
    public void onCommand(String topic, String msg) {

    }
}
