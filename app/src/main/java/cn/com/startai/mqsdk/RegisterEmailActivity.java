package cn.com.startai.mqsdk;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.busi.entity.C_0x8017;
import cn.com.startai.mqttsdk.busi.entity.C_0x8023;

public class RegisterEmailActivity extends BaseActivity {

    private EditText etLastName;
    private EditText etFirstName;
    private EditText etPwd;
    private EditText etEmail;
    private Button btRegister;
    private TextView tvResend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);


        Toolbar toolbar = (Toolbar) findViewById(R.id.include3);
        toolbar.setTitle("邮箱注册");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initview();
        initListener();

    }


    @Override
    public void onRegisterResult(C_0x8017.Resp resp) {
        TAndL.TL(getApplicationContext(), "注册结果 result = " + resp);

    }




    @Override
    public void onSendEmailResult( C_0x8023.Resp resp) {

        if (resp.getResult() == 1) {
            TAndL.TL(getApplicationContext(), "邮件发送成功 " + resp);
        } else {

            TAndL.TL(getApplicationContext(), "邮件发送失败 " + resp);
        }

    }



    private void initListener() {

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uname = etEmail.getText().toString();
                String pwd = etPwd.getText().toString();
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.toString();


                StartAI.getInstance().getBaseBusiManager().register(uname, pwd, onCallListener);

            }
        });

        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();

                StartAI.getInstance().getBaseBusiManager().sendEmail(email, 1, onCallListener);

            }
        });

    }

    private void initview() {

        etEmail = (EditText) findViewById(R.id.et_register_email_email);
        etPwd = (EditText) findViewById(R.id.et_register_email_pwd);
        etFirstName = (EditText) findViewById(R.id.et_register_email_firstname);
        etLastName = (EditText) findViewById(R.id.et_register_email_lastname);
        btRegister = (Button) findViewById(R.id.bt_register_email_register);
        tvResend = findViewById(R.id.tv_register_email_resend);
        tvResend.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

}
