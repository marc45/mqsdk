package cn.com.startai.mqsdk;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;

import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqsdk.util.eventbus.E_0x8003_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8016_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8018_Resp_;
import cn.com.startai.mqsdk.util.eventbus.E_0x8024_Resp;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.busi.entity.C_0x8016;
import cn.com.startai.mqttsdk.busi.entity.C_0x8024;

/**
 * 个人中心
 */
public class AccountActivity extends BaseActivity {

    private TextView btLogout;
    private TextView btUnActivite;
    private TextView tvAboutUs;
    private TextView tvUpdatePwd;
    private TextView tvCheckUpdate;
    private TextView tvNickName;
    private TextView tvDeviceSize;
    private ImageView ivSet;
    private ImageView ivHead;
    public static C_0x8024.Resp.ContentBean userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include4);
        toolbar.setTitle("我的");
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
    protected void onResume() {
        super.onResume();
        initDate();
    }

    private void initDate() {

        if (userInfo != null) {
            tvNickName.setText(userInfo.getNickName());
            int isHavePwd = userInfo.getIsHavePwd();
            if (isHavePwd == 1) {
                tvUpdatePwd.setText("修改密码");
            } else {
                tvUpdatePwd.setText("设置登录密码");
            }
        }

        StartAI.getInstance().getBaseBusiManager().getUserInfo(onCallListener);

    }

    private void initListener() {

        btUnActivite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StartAI.getInstance().getBaseBusiManager().unActivite(onCallListener);

            }
        });
        ivSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //登出
                new AlertDialog.Builder(AccountActivity.this)
                        .setTitle("提示")
                        .setMessage("退出登录")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                StartAI.getInstance().getBaseBusiManager().logout();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();


            }
        });

        tvCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String os = "android";
                String packageName = getApplicationInfo().packageName;
                StartAI.getInstance().getBaseBusiManager().getLatestVersion(os, packageName, onCallListener);

            }
        });


        tvUpdatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, UpdatePasswordActivity.class);
                if (userInfo != null) {
                    intent.putExtra("isHavePwd", userInfo.getIsHavePwd());
                }
                startActivity(intent);

            }
        });

    }

    @Override
    public void onGetLatestVersionResult(E_0x8016_Resp e_0x8016_resp) {
        super.onGetLatestVersionResult(e_0x8016_resp);

        int result = e_0x8016_resp.getResult();
        if (result != 1) {
            TAndL.TL(getApplicationContext(), e_0x8016_resp.getErrorMsg());
            return;
        }

        final C_0x8016.Resp.ContentBean contentBean = e_0x8016_resp.getContentBean();
        if (!getApplicationInfo().packageName.equals(contentBean.getPackageName())) {
            return;
        }

        int versionCode = contentBean.getVersionCode();
        int currCode = AppUtils.getAppInfo().getVersionCode();
        if (currCode >= versionCode) {
            TAndL.TL(getApplicationContext(), "已经是最新版本了 " + AppUtils.getAppInfo().getVersionName());
        } else {
            new AlertDialog.Builder(AccountActivity.this)
                    .setTitle("检测到新版本 " + contentBean.getVersionName())
                    .setMessage(contentBean.getUpdateLog())
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            new AlertDialog.Builder(AccountActivity.this)
                                    .setTitle("下载文件")
                                    .setSingleChoiceItems(new String[]{"浏览下载", "应用内下载", "应用市场下载"}, -1, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            switch (which) {
                                                case 0:
                                                    gotoBrowserDownload(contentBean.getUpdateUrl());
                                                    break;
                                                case 1:
                                                    TAndL.TL(getApplicationContext(), "应用内下载 开发中...");
                                                    break;
                                                case 2:
                                                    TAndL.TL(getApplicationContext(), "应用市场下载 开发中...");
                                                    break;
                                            }
                                        }
                                    })
                                    .show();


                            dialog.dismiss();

//                            TAndL.TL(getApplicationContext(), "开始下载");


                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        }


    }


    private void gotoBrowserDownload(String downloadUrl) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(downloadUrl);
        intent.setData(content_url);
        startActivity(intent);
    }

    @Override
    public void onLogoutResult(E_0x8018_Resp_ resp_) {
        super.onLogoutResult(resp_);

        if (resp_.getResult() == 1) {
            finishAllActivity();
            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onUnActiviteResult(E_0x8003_Resp resp) {
        super.onUnActiviteResult(resp);
        if (resp.getResult() == 1) {
            TAndL.TL(getApplicationContext(), "注销成功");
            finishAllActivity();
            System.exit(0);
        } else {
            TAndL.TL(getApplicationContext(), "注销激活失败 " + resp.getErrorMsg());
        }
    }

    @Override
    public void onGetUserInfoResult(E_0x8024_Resp resp) {
        super.onGetUserInfoResult(resp);

        int result = resp.getResult();
        if (result == 1) {
            userInfo = resp.getMessage();
            if (TextUtils.isEmpty(userInfo.getNickName())) {
                tvNickName.setText("昵称未设置");
            } else {
                tvNickName.setText(userInfo.getNickName());
            }
            int isHavePwd = userInfo.getIsHavePwd();
            if (isHavePwd != 1) {
                tvUpdatePwd.setText("设置登录密码");
            }
        }

    }

    private void initview() {


        btLogout = (TextView) findViewById(R.id.bt_account_exit_login);
        btUnActivite = (TextView) findViewById(R.id.bt_unactivite);

        tvAboutUs = (TextView) findViewById(R.id.tv_account_abouttus);
        tvNickName = (TextView) findViewById(R.id.tv_account_nickname);
        tvCheckUpdate = (TextView) findViewById(R.id.tv_account_app_update);
        tvDeviceSize = (TextView) findViewById(R.id.tv_account_devicesize);
        tvUpdatePwd = (TextView) findViewById(R.id.tv_account_pwd);
        ivSet = (ImageView) findViewById(R.id.iv_account_set);
        ivHead = (ImageView) findViewById(R.id.iv_account_head);

    }

}
