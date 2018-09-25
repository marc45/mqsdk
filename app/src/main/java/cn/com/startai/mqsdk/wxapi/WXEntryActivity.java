package cn.com.startai.mqsdk.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import cn.com.startai.mqsdk.BaseActivity;
import cn.com.startai.mqsdk.MyApp;
import cn.com.startai.mqsdk.R;
import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private String TAG = WXEntryActivity.class.getSimpleName();
    private static final int RETURN_MSG_TYPE_LOGIN = 1; //登录
    private static final int RETURN_MSG_TYPE_SHARE = 2; //分享

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        TAndL.L("WXEntryActivity.onCreate()");

        MyApp.getWxAPI().handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp baseResp) {
        TAndL.L("WXEntryActivity onResp:------>");
        TAndL.L("WXEntryActivity error_code:---->" + baseResp.errCode);
        int type = baseResp.getType(); //类型：分享还是登录
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝授权
                TAndL.TL(getApplicationContext(), "拒绝授权微信登录");

            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                String message = "";
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    message = "取消了微信登录";
                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    message = "取消了微信分享";
                }
                TAndL.TL(getApplicationContext(), "WXEntryActivity " + message);
                break;
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    //用户换取access_token的code，仅在ErrCode为0时有效
                    String code = ((SendAuth.Resp) baseResp).code;
                    TAndL.L("WXEntryActivity code:------>" + code);

                    //这里拿到了这个code，去做2次网络请求获取access_token和用户个人信息
//                    toLogin2Wechat(code);

                    // 为了安全起见此接口的调用已经放到了云端 ， 在此处只需直接调用 startai 的第三方登录接口即可

                    StartAI.getInstance().getBaseBusiManager().loginWithThirdAccount(10, code, new IOnCallListener() {
                        @Override
                        public void onSuccess(MqttPublishRequest mqttPublishRequest) {
                            TAndL.TL(getApplicationContext(), "消息发送成功");
                        }

                        @Override
                        public void onFailed(MqttPublishRequest mqttPublishRequest, StartaiError startaiError) {
                            TAndL.TL(getApplicationContext(), "消息发送失败 " + startaiError.getErrorMsg());
                        }

                        @Override
                        public boolean needUISafety() {
                            return true;
                        }
                    });


                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    TAndL.TL(getApplicationContext(), "WXEntryActivity 微信分享成功");
                }
                break;
        }
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        TAndL.L(TAG + " onStart()");

    }

    @Override
    protected void onStop() {
        super.onStop();
        TAndL.L(TAG + " onStop()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        TAndL.L(TAG + " onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        TAndL.L(TAG + " onPause()");
    }

}
