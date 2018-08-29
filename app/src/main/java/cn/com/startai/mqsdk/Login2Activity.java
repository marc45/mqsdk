package cn.com.startai.mqsdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqsdk.util.TimeCount;
import cn.com.startai.mqsdk.util.eventbus.E_0x8018_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8021_Resp;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.control.SDBmanager;
import cn.com.startai.mqttsdk.control.entity.UserBean;
import cn.com.startai.mqttsdk.localbusi.UserBusi;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;

/**
 * 验证码快速登录
 */
public class Login2Activity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = Login2Activity.class.getSimpleName();
    private EditText etMobile;
    private EditText etIdentify;
    private Button btLogin;
    private Button btGetIdentify;
    private TimeCount timeCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("快捷登录");
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
    protected void onResume() {
        super.onResume();
        /**
         * 自动登录
         */
        checkLoginStatus();
    }

    private void checkLoginStatus() {



        C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
        if (userBean != null && !TextUtils.isEmpty(userBean.getUserid())) {
            //TODO:还需要判断token是否过期
            startActivity(new Intent(Login2Activity.this, HomeActivity.class));
            finish();
        }
    }


    @Override
    public void onClick(View v) {

        String mobile = etMobile.getText().toString();
        int i = v.getId();
        if (i == R.id.bt_login2_login) {
            String identify = etIdentify.getText().toString();

            StartAI.getInstance().getBaseBusiManager().login(mobile, "", identify, onCallListener);


        } else if (i == R.id.bt_login2_getIdentify) {
            timeCount.start();
            StartAI.getInstance().getBaseBusiManager().getIdentifyCode(mobile, 1, onCallListener);

        }
    }

    private void initView() {

        etMobile = (EditText) findViewById(R.id.et_login2_mobile);
        etIdentify = (EditText) findViewById(R.id.et_login2_identify);
        btLogin = (Button) findViewById(R.id.bt_login2_login);
        btGetIdentify = (Button) findViewById(R.id.bt_login2_getIdentify);
        timeCount = new TimeCount(60 * 1000, 1000, btGetIdentify);
    }

    private void initListener() {

        btLogin.setOnClickListener(this);
        btGetIdentify.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle("密码登录");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onGetIdentifyResult(E_0x8021_Resp resp) {
        super.onGetIdentifyResult(resp);
        TAndL.TL(getApplicationContext(), "获取验证码结果 " + resp.getResult() + " " +resp.getErrorMsg() + resp.getContentBean());
    }


    @Override
    public void onLoginResult(E_0x8018_Resp resp) {
        super.onLoginResult(resp);

        int result = resp.getResult();
        C_0x8018.Resp.ContentBean loginInfo = resp.getLoginInfo();
        TAndL.TL(getApplicationContext(), "登录结果 " + resp.getResult() + " errorMsg = " + resp.getErrorMsg());
        if (resp.getResult() == 1) {
            //TODO:开发者需要在此保存登录信息
            TAndL.TL(getApplicationContext(), "登录成功 " + loginInfo.getUserid() + " " + loginInfo.getuName() + " " + loginInfo.getType());
            startActivity(new Intent(Login2Activity.this, HomeActivity.class));
            finish();
        } else if (result == 0) {
            String errmsg = resp.getErrorMsg();
            TAndL.TL(getApplicationContext(), "登录失败 " + errmsg + " loginInfo = " + loginInfo);
        }

    }
}

