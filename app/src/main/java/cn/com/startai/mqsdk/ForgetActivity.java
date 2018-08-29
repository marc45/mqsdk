package cn.com.startai.mqsdk;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqsdk.util.TimeCount;
import cn.com.startai.mqsdk.util.eventbus.E_0x8021_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8022_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8026_Resp;
import cn.com.startai.mqttsdk.StartAI;

public class ForgetActivity extends BaseActivity {

    private EditText etMobile;
    private EditText etCode;
    private EditText etNewPwd;
    private Button btGetCode;
    private Button btOk;
    TimeCount timeCount;
    private Button btCheckCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);


        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("手机号重置密码");
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


        timeCount = new TimeCount(60 * 1000, 1000, btGetCode);
    }

    private void initListener() {

        btGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mobile = etMobile.getText().toString();

                timeCount.start();
                StartAI.getInstance().getBaseBusiManager().getIdentifyCode(mobile, 2, onCallListener);

            }
        });

        btCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = etMobile.getText().toString();
                String code = etCode.getText().toString();

                StartAI.getInstance().getBaseBusiManager().checkIdentifyCode(mobile, code, 2, onCallListener);


            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mobile = etMobile.getText().toString();
                String pwd = etNewPwd.getText().toString();

                StartAI.getInstance().getBaseBusiManager().resetMobileLoginPwd(mobile, pwd, onCallListener);

            }
        });

    }

    private void initView() {

        etMobile = findViewById(R.id.et_forget_email_email);
        etCode = findViewById(R.id.et_forget_code);
        etNewPwd = findViewById(R.id.et_forget_new_pwd);

        btGetCode = findViewById(R.id.bt_forget_getcode);
        btOk = findViewById(R.id.bt_forget_ok);
        btCheckCode = findViewById(R.id.bt_forget_checkcode);

    }

    @Override
    public void onGetIdentifyResult(E_0x8021_Resp resp) {
        super.onGetIdentifyResult(resp);
        int result = resp.getResult();

        if (result == 1) {
            TAndL.TL(getApplicationContext(), "获取验证码成功");

        } else {
            TAndL.TL(getApplicationContext(), "获取验证码失败 " + resp.getErrorMsg());
        }
    }

    @Override
    public void onCheckIdentifyResult(E_0x8022_Resp resp) {
        super.onCheckIdentifyResult(resp);
        int result = resp.getResult();

        if (result == 1) {
            TAndL.TL(getApplicationContext(), "校验验证码成功");

        } else {
            TAndL.TL(getApplicationContext(), "校验验证码失败 " + resp.getErrorMsg());
        }
    }


    @Override
    public void onResetMobileLoginPwdResult(E_0x8026_Resp resp) {
        super.onResetMobileLoginPwdResult(resp);
        int result = resp.getResult();

        if (result == 1) {
            TAndL.TL(getApplicationContext(), "重置密码成功 \n" + resp.getContentBean().getMobile() + " \n" + resp.getContentBean().getPwd());

        } else {
            TAndL.TL(getApplicationContext(), "重置密码失败 " + resp.getErrorMsg());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
        }

    }
}
