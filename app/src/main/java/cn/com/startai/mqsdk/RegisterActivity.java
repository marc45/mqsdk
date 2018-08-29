package cn.com.startai.mqsdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.com.startai.mqsdk.listener.MySendListener;
import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqsdk.util.TimeCount;
import cn.com.startai.mqsdk.util.eventbus.E_0x8017_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8021_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8022_Resp;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.event.AOnStartaiMessageArriveListener;
import cn.com.startai.mqttsdk.event.PersistentEventDispatcher;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText etMobile;
    private EditText etIdentify;
    private EditText etPwd;
    private Button btCheckIdentify;
    private Button btGetIdentify;
    private Button btRegister;
    private String TAG = RegisterActivity.class.getSimpleName();
    TimeCount timeCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("手机号注册");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();
        initListener();
        timeCount = new TimeCount(60 * 1000, 1000, btGetIdentify);

    }

    private void initView() {

        etMobile = (EditText) findViewById(R.id.et_register_mobile);
        etIdentify = (EditText) findViewById(R.id.et_register_identify);
        etPwd = (EditText) findViewById(R.id.et_register_pwd);
        btGetIdentify = (Button) findViewById(R.id.bt_register_getidentify);
        btCheckIdentify = (Button) findViewById(R.id.bt_register_check_identify);
        btRegister = (Button) findViewById(R.id.bt_register_register);

    }


    private void initListener() {


        btCheckIdentify.setOnClickListener(this);
        btGetIdentify.setOnClickListener(this);
        btRegister.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        String mobile = etMobile.getText().toString();

        int i = v.getId();
        if (i == R.id.bt_register_getidentify) {
            timeCount.start();

            StartAI.getInstance().getBaseBusiManager().getIdentifyCode(mobile, 3, onCallListener);


        } else if (i == R.id.bt_register_register) {
            String pwd = etPwd.getText().toString();

            StartAI.getInstance().getBaseBusiManager().register(mobile, pwd, onCallListener);


        } else if (i == R.id.bt_register_check_identify) {
            String identify = etIdentify.getText().toString();
            int type = 3;
            StartAI.getInstance().getBaseBusiManager().checkIdentifyCode(mobile, identify, type, onCallListener);


        }


    }

    @Override
    public void onRegisterResult(E_0x8017_Resp resp) {
        super.onRegisterResult(resp);
        TAndL.TL(getApplicationContext(), "注册结果 result = " + resp.getResult() + " errorMsg = " + resp.getErrorMsg());
    }

    @Override
    public void onGetIdentifyResult(E_0x8021_Resp resp) {
        super.onGetIdentifyResult(resp);

        TAndL.TL(getApplicationContext(), "获取验证码结果 " + resp.getResult() + " " + resp.getErrorMsg() + resp.getContentBean());
    }

    @Override
    public void onCheckIdentifyResult(E_0x8022_Resp resp) {
        super.onCheckIdentifyResult(resp);
        TAndL.TL(getApplicationContext(), "校验验证码结果 result = " + resp.getResult() + " errorMsg = " + resp.getErrorMsg());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
        }

    }
}
