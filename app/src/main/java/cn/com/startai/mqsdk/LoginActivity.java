package cn.com.startai.mqsdk;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
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

import org.json.JSONException;

import java.util.List;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.busi.entity.C_0x8017;
import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.busi.entity.C_0x8020;
import cn.com.startai.mqttsdk.busi.entity.C_0x8021;
import cn.com.startai.mqttsdk.busi.entity.C_0x8022;
import cn.com.startai.mqttsdk.busi.entity.C_0x8023;
import cn.com.startai.mqttsdk.busi.entity.C_0x8026;
import cn.com.startai.mqttsdk.busi.entity.C_0x8027;
import cn.com.startai.mqttsdk.busi.entity.C_0x8035;
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

        MqttInitParam initParam = new MqttInitParam(MyApp.appid);
        StartAI.getInstance().initialization(getApplicationContext(), initParam);
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

        } else if (id == R.id.action_3_login) {

            StartAI.getInstance().initialization(getApplicationContext(), new MqttInitParam(MyApp.appid));

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.bt_login2_login) {


            C_0x8035.Req.ContentBean req = new C_0x8035.Req.ContentBean("36.4626820000", "115.9918900000");
            StartAI.getInstance().getBaseBusiManager().getWeatherInfo(req, new IOnCallListener() {
                @Override
                public void onSuccess(MqttPublishRequest request) {

                }

                @Override
                public void onFailed(MqttPublishRequest request, StartaiError startaiError) {

                }


            });


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
//
//            C_0x8039.Req.ContentBean req = new C_0x8039.Req.ContentBean("", "419109715@qq.com");
//
//            StartAI.getInstance().getBaseBusiManager().bindEmail(req, new IOnCallListener() {
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
//
//            C_0x8038.Req.ContentBean req = new C_0x8038.Req.ContentBean("18b0ae177fce4e5a", C_0x8038.TYPE_USER_DEVICE, 2, 3);
//            StartAI.getInstance().getBaseBusiManager().getBindListByPage(req, new IOnCallListener() {
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


            //获取验证码
//            String mobile = "13333333333"; //手机号
////            int type = C_0x8021.TYPE_FAST_LOGIN; // 业务类型为 快捷登录
//            int type = C_0x8021.TYPE_RESET_PWD; // 业务类型为 验证码重置密码
//            C_0x8021.Req.ContentBean req = new C_0x8021.Req.ContentBean(mobile, type);
//            StartAI.getInstance().getBaseBusiManager().getIdentifyCode(req, onCallListener);

            //校验验证码

//            String account = "123456789@qq.com";
//            String code= "666555";
//            int type = C_0x8022.TYPE_EMAIL_RESET_PWD; //业务类型为邮箱验证码 重置密码
//            C_0x8022.Req.ContentBean req = new C_0x8022.Req.ContentBean(account,code,type);
//            StartAI.getInstance().getBaseBusiManager().checkIdentifyCode(req,onCallListener);
//注册
//            String uName = "123456789@qq.com"; //邮箱号
//            String pwd = "abc123456"; //设置的登录密码
//            int type = C_0x8017.TYPE_EMAIL_AFTER_CHECK_CODE;
//            C_0x8017.Req.ContentBean req = new C_0x8017.Req.ContentBean(uName, pwd, type);
//            StartAI.getInstance().getBaseBusiManager().register(req, onCallListener);

            //发送邮件
//            String email = "132456789@qq.com"; //注册的邮箱号
////            int type = C_0x8023.TYPE_CODE_TO_REGISTER; //发送邮件的业务类型
////            int type = C_0x8023.TYPE_LINK_TO_RESET_PWD; //发送邮件的业务类型
//            int type = C_0x8023.TYPE_CODE_TO_RESET_PWD; //发送邮件的业务类型 通过验证码重置密码
//            C_0x8023.Req.ContentBean req = new C_0x8023.Req.ContentBean(email, type);
//            StartAI.getInstance().getBaseBusiManager().sendEmail(req, onCallListener);

            //登录

//            String uname = "13333333333"; //手机号
//            String code = "666555"; //验证码
//            String pwd = "abc123456"; //登录密码
////            int type = C_0x8018.TYPE_MOBILE_CODE; //手机号 加验证码登录
////            int type = C_0x8018.TYPE_MOBILE_PWD; //手机号加密码
////            int type = C_0x8018.TYPE_EMAIL_PWD; //邮箱号加密码
////            int type = C_0x8018.TYPE_UNAME_PWD; //用户名加密码
//            int type = C_0x8018.TYPE_MOBILE_CODE_PWD; //双重验证登录
//            C_0x8018.Req.ContentBean req = new C_0x8018.Req.ContentBean(uname,pwd, code, type);
//            StartAI.getInstance().getBaseBusiManager().login(req, onCallListener);


            //第三方登录

//            int type = C_0x8027.THIRD_WECHAT;
//            String code = "15W6E1F648WEW98E4FWE54FW";
//            C_0x8027.Req.ContentBean req = new C_0x8027.Req.ContentBean(type,code);

//            将从第三方登录后拿到的 用户通过 userInfo存储并提交登录
//            C_0x8027.Req.ContentBean req = new C_0x8027.Req.ContentBean();
//            try {
//                String facebookJson = "{\"id\":\"109241263559640\",\"name\":\"罗彬彬\",\"picture\":{\"data\":{\"height\":50,\"is_silhouette\":true,\"url\":\"https:\\/\\/platform-lookaside.fbsbx.com\\/platform\\/profilepic\\/?asid=109241263559640&height=50&width=50&ext=1553756148&hash=AeRRBT13kE6s5D9Y\",\"width\":50}},\"first_name\":\"彬彬\",\"last_name\":\"罗\"}";
//                req.fromFacebookJson(facebookJson);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            StartAI.getInstance().getBaseBusiManager().loginWithThirdAccount(req,onCallListener);
//重置登录密码

//            String account = "123456789@qq.com"; //手机号
//            String pwd = "abc123456"; //新密码
//            C_0x8026.Req.ContentBean req = new C_0x8026.Req.ContentBean(account,pwd);
//            StartAI.getInstance().getBaseBusiManager().resetLoginPwd(req, onCallListener);
//查询用户信息
//            StartAI.getInstance().getBaseBusiManager().getUserInfo(onCallListener);

            //修改用户信息
//            C_0x8020.Req.ContentBean req = new C_0x8020.Req.ContentBean();
//            req.setNickName("新昵称"); //修改昵称
//            StartAI.getInstance().getBaseBusiManager().updateUserInfo(req,onCallListener);

        } else if (i == R.id.tv_main_new_register) {


        } else if (i == R.id.tv_main_forget_pwd) {

        } else if (i == R.id.tv_login_forget_email) {

        } else if (i == R.id.tv_main_new_register2) {

        }
    }

    private IOnCallListener onCallListener = new IOnCallListener() {
        @Override
        public void onSuccess(MqttPublishRequest request) {

        }

        @Override
        public void onFailed(MqttPublishRequest request, StartaiError startaiError) {

        }

    };


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
