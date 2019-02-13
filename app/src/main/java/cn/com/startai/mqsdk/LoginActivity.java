package cn.com.startai.mqsdk;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.busi.entity.C_0x8028;
import cn.com.startai.mqttsdk.busi.entity.C_0x8035;
import cn.com.startai.mqttsdk.busi.entity.C_0x8039;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.MqttInitParam;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;

/**
 * 账号加密码登录
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUname;
    private EditText etPwd;
    private Button btLogin;
    private TextView tvForget;
    private TextView tvForeteEmail;
    private TextView tvRegister;
    private String TAG = LoginActivity.class.getSimpleName();
    private TextView tvProtocol;
    private TextView tvRegister2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
        toolbar.setTitle("用户登录");
        setSupportActionBar(toolbar);


        initView();
        initListener();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            return true;
        } else if (id == R.id.action_3_login) {


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.bt_login2_login) {


//            C_0x8035.Req.ContentBean req = new C_0x8035.Req.ContentBean("36.4626820000", "115.9918900000");
//            StartAI.getInstance().getBaseBusiManager().getWeatherInfo(req, new IOnCallListener() {
//                @Override
//                public void onSuccess(MqttPublishRequest request) {
//
//                }
//
//                @Override
//                public void onFailed(MqttPublishRequest request, StartaiError startaiError) {
//
//                }
//
//                @Override
//                public boolean needUISafety() {
//                    return false;
//                }
//            });


//            C_0x8028.Req.ContentBean request = new C_0x8028.Req.ContentBean(C_0x8028.TYPE_DEPOSIT, C_0x8028.PLATFOME_WECHAT, "aaabbcc", "测试充值", "CNY", "11");
//
//            StartAI.getInstance().getBaseBusiManager().thirdPaymentUnifiedOrder(request, new IOnCallListener() {
//                @Override
//                public void onSuccess(MqttPublishRequest request) {
//
//                }
//
//                @Override
//                public void onFailed(MqttPublishRequest request, StartaiError startaiError) {
//
//                }
//
//                @Override
//                public boolean needUISafety() {
//                    return false;
//                }
//            });


//            String num = "419109715@qq.com";
//            StartAI.getInstance().getBaseBusiManager().resetLoginPwd(num, "qq123456", new IOnCallListener() {
//                @Override
//                public void onSuccess(MqttPublishRequest request) {
//
//                }
//
//                @Override
//                public void onFailed(MqttPublishRequest request, StartaiError startaiError) {
//
//                }
//
//                @Override
//                public boolean needUISafety() {
//                    return false;
//                }
//            });

            C_0x8039.Req.ContentBean req = new C_0x8039.Req.ContentBean("", "419109715@qq.com");

            StartAI.getInstance().getBaseBusiManager().bindEmail(req, new IOnCallListener() {
                @Override
                public void onSuccess(MqttPublishRequest request) {

                }

                @Override
                public void onFailed(MqttPublishRequest request, StartaiError startaiError) {

                }

                @Override
                public boolean needUISafety() {
                    return false;
                }
            });

        } else if (i == R.id.tv_main_new_register) {


        } else if (i == R.id.tv_main_forget_pwd) {

        } else if (i == R.id.tv_login_forget_email) {

        } else if (i == R.id.tv_main_new_register2) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        StartAI.getInstance().unInit();

    }

    private void initView() {

        etUname = (EditText) findViewById(R.id.et_login2_mobile);
        etPwd = (EditText) findViewById(R.id.et_login2_identify);
        btLogin = (Button) findViewById(R.id.bt_login2_login);
        tvForget = (TextView) findViewById(R.id.tv_main_forget_pwd);
        tvForget.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvForeteEmail = (TextView) findViewById(R.id.tv_login_forget_email);
        tvForeteEmail.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvRegister = (TextView) findViewById(R.id.tv_main_new_register);
        tvRegister.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvRegister2 = (TextView) findViewById(R.id.tv_main_new_register2);
        tvRegister2.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        tvProtocol = (TextView) findViewById(R.id.tv_main_protocl);
        tvProtocol.setText("登录则表您同意遵守亓行智能wifi插座的");

    }

    String text1 = "用户协议";
    String and = "和";
    String text2 = "隐私协议";

    private void initListener() {

        btLogin.setOnClickListener(this);
        tvForget.setOnClickListener(this);
        tvForeteEmail.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvRegister2.setOnClickListener(this);

        SpannableString spStr1 = new SpannableString(text1);

        spStr1.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#99cc33"));
                ds.setUnderlineText(true);
            }

            @Override
            public void onClick(View widget) {
                TAndL.TL(getApplicationContext(), text1);
            }
        }, 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvProtocol.append(spStr1);
        tvProtocol.append(and);
        SpannableString spStr2 = new SpannableString(text2);
        spStr2.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#99cc33"));
                ds.setUnderlineText(true);
            }

            @Override
            public void onClick(View widget) {
                TAndL.TL(getApplicationContext(), text2);

            }
        }, 0, text2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvProtocol.append(spStr2);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());

    }


}
