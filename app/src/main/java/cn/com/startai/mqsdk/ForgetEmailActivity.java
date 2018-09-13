package cn.com.startai.mqsdk;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.busi.entity.C_0x8023;
import cn.com.startai.mqttsdk.busi.entity.type.Type;

public class ForgetEmailActivity extends BaseActivity {

    private EditText etEmail;
    private Button btNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_email);


        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("邮箱号重置 密码");
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
    }



    @Override
    public void onSendEmailResult(  C_0x8023.Resp resp) {

        if (resp.getResult() == resp.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "邮件发送成功 " + resp);
        } else {

            TAndL.TL(getApplicationContext(), "邮件发送失败 " + resp);
        }

    }



    private void initListener() {

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();

                StartAI.getInstance().getBaseBusiManager().sendEmail(email, Type.SendEmail.FORGET_PWD, onCallListener);

            }
        });
    }

    private void initView() {

        etEmail = findViewById(R.id.et_forget_email_email);
        btNext = findViewById(R.id.bt_forget_email_ok);
    }
}
