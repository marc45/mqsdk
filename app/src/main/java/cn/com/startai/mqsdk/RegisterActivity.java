package cn.com.startai.mqsdk;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqsdk.util.TimeCount;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.busi.entity.C_0x8017;
import cn.com.startai.mqttsdk.busi.entity.C_0x8021;
import cn.com.startai.mqttsdk.busi.entity.C_0x8022;
import cn.com.startai.mqttsdk.busi.entity.type.Type;

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

            StartAI.getInstance().getBaseBusiManager().getIdentifyCode(mobile, Type.GetIdentifyCode.REGISTER, onCallListener);


        } else if (i == R.id.bt_register_register) {
            String pwd = etPwd.getText().toString();

            StartAI.getInstance().getBaseBusiManager().register(mobile, pwd, onCallListener);


        } else if (i == R.id.bt_register_check_identify) {
            String identify = etIdentify.getText().toString();

            StartAI.getInstance().getBaseBusiManager().checkIdentifyCode(mobile, identify, Type.CheckIdentifyCode.REGISTER, onCallListener);


        }


    }

    @Override
    public void onRegisterResult(C_0x8017.Resp resp) {
        TAndL.TL(getApplicationContext(), "注册结果 result = " + resp);
        if (resp.getResult() == resp.RESULT_SUCCESS) {
            //注册成功
        } else {
            //注册失败
        }
    }



    @Override
    public void onGetIdentifyCodeResult(C_0x8021.Resp resp) {
        TAndL.TL(getApplicationContext(), "获取验证码结果 " + resp);
        if (resp.getResult() == resp.RESULT_SUCCESS) {
            //获取验证码成功
        } else {
            //获取验证码失败
        }
    }





    @Override
    public void onCheckIdetifyResult(C_0x8022.Resp resp) {
        TAndL.TL(getApplicationContext(), "校验验证码结果 result = " + resp);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
        }

    }
}
