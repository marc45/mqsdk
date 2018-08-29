package cn.com.startai.mqsdk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqsdk.util.eventbus.E_0x8020_Resp;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.busi.entity.C_0x8020;

public class UserInfoActivity extends BaseActivity {

    private TextView tvHead;
    private TextView tvNickName;
    private String TAG = UserInfoActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("用户信息");
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
        initData();
    }

    private void initData() {
        if (AccountActivity.userInfo != null) {
            tvNickName.setText("昵称:" + (TextUtils.isEmpty(AccountActivity.userInfo.getNickName()) ? "未设置" : AccountActivity.userInfo.getNickName()));
        }
    }


    private void initListener() {

        tvHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //登出
                new AlertDialog.Builder(UserInfoActivity.this)
                        .setTitle("设置头像")
                        .setSingleChoiceItems(new String[]{"拍照", "从相册中选择"}, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                TAndL.TL(getApplicationContext(), "index = " + which);

                            }
                        })
                        .create().show();


            }
        });

        tvNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoActivity.this, UpdateNickNameActivity.class));
            }
        });

    }

    private void initView() {
        tvHead = (TextView) findViewById(R.id.tv_userinfo_head);
        tvNickName = (TextView) findViewById(R.id.tv_userinfo_nickname);

    }
}
