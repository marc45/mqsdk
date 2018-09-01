package cn.com.startai.mqsdk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqsdk.util.eventbus.E_0x8025_Resp;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.busi.entity.C_0x8025;
import cn.com.startai.mqttsdk.listener.IOnCallListener;

public class UpdatePasswordActivity extends BaseActivity {

    private EditText etOld;
    private EditText etNew1;
    private EditText etNew;
    private int isHavePwd;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        toolbar = (Toolbar) findViewById(R.id.include4);
        toolbar.setTitle("修改密码");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etOld = findViewById(R.id.et_upldate_pwd_old);
        etNew1 = findViewById(R.id.et_update_pwd_new1);
        etNew = findViewById(R.id.et_update_pwd_new);


    }

    @Override
    protected void onResume() {
        super.onResume();

        isHavePwd = getIntent().getIntExtra("isHavePwd", 0);
        if (isHavePwd == 0) {
            etNew1.setVisibility(View.GONE);
            etNew.setVisibility(View.GONE);
            etOld.setHint("输入密码");
            toolbar.setTitle("设置登录密码");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle("保存");
        return true;
    }


    @Override
    public void onUpdateUserPwdResult(C_0x8025.Resp resp) {

        if (resp.getResult() == 1) {
            TAndL.TL(getApplicationContext(), "密码修改成功 " + resp);
            finish();
        } else {
            TAndL.TL(getApplicationContext(), "密码修改失败" + resp);
        }

    }

    @Override
    public void onUpdateUserPwdResult(int result, String errorCode, String errorMsg, C_0x8025.Resp.ContentBean contentBean) {
        super.onUpdateUserPwdResult(result, errorCode, errorMsg, contentBean);
        if (result == 1) {
            TAndL.TL(getApplicationContext(), "密码修改成功 " + contentBean);
            finish();
        } else {
            TAndL.TL(getApplicationContext(), "密码修改失败 " + errorMsg);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            String pwdOld = etOld.getText().toString();
            String pwdNew1 = etNew.getText().toString();
            String pwdNew2 = etNew1.getText().toString();

            if (isHavePwd == 1) {


                if (!TextUtils.isEmpty(pwdNew1) && !TextUtils.isEmpty(pwdNew2) && !pwdNew1.equals(pwdNew2)) {
                    TAndL.TL(getApplicationContext(), "两次输入的新密码不一致");
                    return true;
                }

                StartAI.getInstance().getBaseBusiManager().updateUserPwd(pwdOld, pwdNew1, onCallListener);
            } else {

                StartAI.getInstance().getBaseBusiManager().updateUserPwd("", pwdOld, onCallListener);

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
