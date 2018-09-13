package cn.com.startai.mqsdk;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import cn.com.startai.mqsdk.adapter.MyRecyclerViewAdapter;
import cn.com.startai.mqsdk.util.airkiss.AirkissActivity;
import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqsdk.util.esptouch.EsptouchActivity;
import cn.com.startai.mqsdk.util.eventbus.E_Conn_Break;
import cn.com.startai.mqsdk.util.eventbus.E_Conn_Failed;
import cn.com.startai.mqsdk.util.eventbus.E_Conn_Success;
import cn.com.startai.mqsdk.util.permission.DialogHelper;
import cn.com.startai.mqsdk.util.zxing.ScanActivity;
import cn.com.startai.mqttsdk.PersistentConnectState;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.busi.entity.C_0x8002;
import cn.com.startai.mqttsdk.busi.entity.C_0x8004;
import cn.com.startai.mqttsdk.busi.entity.C_0x8005;
import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.localbusi.UserBusi;

public class HomeActivity extends BaseActivity {

    private Button btLogout;
    private RecyclerView mRecyclerView;

    ArrayList<C_0x8005.Resp.ContentBean> list = new ArrayList<>();

    private MyRecyclerViewAdapter mAdapter;
    private long t;
    private TextView tvConnect;

    private String TAG = "AudioTrackTest";
    private final long WAIT_MSEC = 200;
    private final int OFFSET_DEFAULT = 0;
    private final int OFFSET_NEGATIVE = -10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("设备列表");
        setSupportActionBar(toolbar);


        initview();
        initAdapter();

        initListener();

    }


    public static final String topicTestSUB = "Q/client/432EA4016B40B0BDFEB1313B062A3EAE/#";
    public static final String topicTest = "Q/client/432EA4016B40B0BDFEB1313B062A3EAE";


    @Override
    public void onConnected(E_Conn_Success e_conn_success) {
        super.onConnected(e_conn_success);
        tvConnect.setVisibility(View.GONE);
//        StartAI.getInstance().subscribe(topicTestSUB, new IOnSubscribeListener() {
//            @Override
//            public void onSuccess(String topic) {
//                TAndL.TL(getApplicationContext(), "订阅成功 " + topic);
//            }
//
//            @Override
//            public void onFailed(String topic, StartaiError error) {
//                TAndL.TL(getApplicationContext(), "订阅失败 " + topic);
//            }
//
//            @Override
//            public boolean needUISafety() {
//                return true;
//            }
//        });
//
    }


    @Override
    public void onConnectFail(E_Conn_Failed e_conn_failed) {
        super.onConnectFail(e_conn_failed);
        tvConnect.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDisconnect(E_Conn_Break e_conn_break) {
        super.onDisconnect(e_conn_break);
        tvConnect.setVisibility(View.VISIBLE);
    }


//    @Override
//    public void onBackPressed() {
//
//        if (System.currentTimeMillis() - t < 2 * 1000) {
//            finish();
//            System.exit(0);
//        } else {
//            t = System.currentTimeMillis();
//            TAndL.TL(getApplicationContext(), "再次点击返回键退出应用");
//        }
//
//    }


    private void initAdapter() {


        //设置RecyclerView管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //初始化适配器
        mAdapter = new MyRecyclerViewAdapter(list);
        //设置添加或删除item时的动画，这里使用默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
    }


    private void initListener() {


        mAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                Intent intent = new Intent(HomeActivity.this, DeviceControlActivity.class);
                intent.putExtra("device", mAdapter.getItem(position));
                startActivity(intent);
            }
        });


        mAdapter.setOnItemLongClickListener(new MyRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                final C_0x8005.Resp.ContentBean item = mAdapter.getItem(position);

                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("操作")
                        .setSingleChoiceItems(new String[]{"修改备注名", "删除设备"}, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent(HomeActivity.this, UpdateRemarkActivity.class);
                                        intent.putExtra("C_0x8005.Resp.ContentBean", item);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        //解绑设备
                                        new AlertDialog.Builder(HomeActivity.this)
                                                .setTitle("提示")
                                                .setMessage("是否删除")
                                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

//
                                                        C_0x8018.Resp.ContentBean currUser = new UserBusi().getCurrUser();


                                                        StartAI.getInstance().getBaseBusiManager().unBind(item.getId(), onCallListener);


                                                    }
                                                })
                                                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                })
                                                .create().show();
                                        break;
                                }
                            }
                        })
                        .show();


            }
        });

    }


    @Override
    public void onBindResult(C_0x8002.Resp errResp, String id, C_0x8002.Resp.ContentBean.BebindingBean bebinding) {
        TAndL.TL(getApplicationContext(), "添加结果  " + errResp);
        if (errResp.getResult() == 1) {
            StartAI.getInstance().getBaseBusiManager().getBindList(1, onCallListener);
        }
    }


    @Override
    public void onGetBindListResult(C_0x8005.Response response) {
        super.onGetBindListResult(response);
        TAndL.TL(getApplicationContext(), "获取好友列表 结果" + response.getResult() + " " + response);
        if (response.getResult() == 1) {

            mAdapter.setList(response.getResp());
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onDeviceConnectStatusChange(String userid, int status, String sn) {
        super.onDeviceConnectStatusChange(userid, status, sn);


        TAndL.TL(getApplicationContext(), userid + " 用户的 " + sn + " " + (status == 1 ? "上线" : "下线" + " 了"));
        if (list != null) {
            for (C_0x8005.Resp.ContentBean contentBean : list) {


                if (contentBean.getId().equals(sn)) {
                    contentBean.setConnstatus(status);
                }
            }
        }
        mAdapter.setList(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUnBindResult(C_0x8004.Resp resp, String id, String beUnbindid) {
        super.onUnBindResult(resp, id, beUnbindid);


        TAndL.TL(getApplicationContext(), "删除结果 " + resp);

        if (resp.getResult() == 1) {
            StartAI.getInstance().getBaseBusiManager().getBindList(1, onCallListener);
        }

    }


    private void initview() {

        btLogout = (Button) findViewById(R.id.bt_home_logout);
        //通过findViewById拿到RecyclerView实例
        mRecyclerView = findViewById(R.id.rv_devicelist);
        btLogout.setVisibility(View.GONE);
        tvConnect = findViewById(R.id.tv_connect);
    }

    private void toScanBarCode() {

        if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {

            PermissionUtils.permission(Manifest.permission.CAMERA)
                    .rationale(new PermissionUtils.OnRationaleListener() {
                        @Override
                        public void rationale(final ShouldRequest shouldRequest) {
                            DialogHelper.showRationaleDialog(shouldRequest);
                        }
                    })
                    .callback(new PermissionUtils.FullCallback() {
                        @Override
                        public void onGranted(List<String> permissionsGranted) {

                            toScanActivity();

                        }

                        @Override
                        public void onDenied(List<String> permissionsDeniedForever,
                                             List<String> permissionsDenied) {
                            if (!permissionsDeniedForever.isEmpty()) {
                                DialogHelper.showOpenAppSettingDialog();
                            }
                            LogUtils.d(permissionsDeniedForever, permissionsDenied);
                        }
                    })
                    .request();

        } else {
            toScanActivity();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_CODE_BIND_DEVICE:

                    String scanResult = data.getStringExtra("result");
                    Log.i(TAG, "scan result = " + scanResult);

                    String sn = getSnFromQRCodrResult(scanResult);

                    if (TextUtils.isEmpty(sn)) {
                        TAndL.TL(getApplicationContext(), getResources().getString(R.string.scan_no_device_code));
                    } else {
                        C_0x8018.Resp.ContentBean currUser = new UserBusi().getCurrUser();
                        if (currUser != null) {
                            StartAI.getInstance().getBaseBusiManager().bind(sn, onCallListener);
                        }
                    }

                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    String QR_CODE_INDEX = "http://www.startai.com.cn/qr/?";

    /**
     * 检查 是否是sn的二码维码
     */
    private String getSnFromQRCodrResult(String result) {

        if (result.startsWith(QR_CODE_INDEX)) {
            String[] datas = result.split("\\?");
            if (datas.length >= 2 && !TextUtils.isEmpty(datas[1])) {
                String sn = datas[1];
                return sn;
            }
        }
        return "";
    }


    /**
     * 跳转到绑定设备页面的 请求码
     */
    public static final int REQUEST_CODE_BIND_DEVICE = 1;

    private void toScanActivity() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ScanActivity.showActivityForResult(HomeActivity.this, REQUEST_CODE_BIND_DEVICE);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_scan) {

            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("添加方式")
                    .setSingleChoiceItems(new String[]{"扫一扫", "局域网发现"}, -1, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            switch (which) {
                                case 0:
                                    toScanBarCode();
                                    break;
                                case 1:

//                                    TAndL.TL(getApplicationContext(), "开发中...");

                                    startActivity(new Intent(HomeActivity.this, LanDeviceFindActivity.class));
//                                    C_0x8018.Resp.ContentBean currUser = new UserBusi().getCurrUser();
//                                    if (currUser != null) {
//                                        LanDeviceFinder.getInstance().find(currUser.getUserid(), "", 60000, new LanDeviceFinder.IDeviceFindListener() {
//                                            @Override
//                                            public void onDeviceFind(LanDevice devices) {
//                                                TAndL.TL(getApplicationContext(), "发现设备");
//                                            }
//
//                                            @Override
//                                            public void onTimeout() {
//                                                TAndL.TL(getApplicationContext(), "超时");
//                                            }
//
//                                            @Override
//                                            public void onException(Exception e) {
//                                                TAndL.TL(getApplicationContext(), e.getMessage());
//                                            }
//                                        });
//                                    }


                                    break;

                            }
                        }
                    })
                    .show();


        } else if (id == R.id.menu_account) {

            startActivity(new Intent(HomeActivity.this, AccountActivity.class));

        } else if (id == R.id.menu_airkiss) {

            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("配网方式")
                    .setSingleChoiceItems(new String[]{"Airkiss", "Esptouch"}, -1, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            switch (which) {
                                case 0:
                                    startActivity(new Intent(HomeActivity.this, AirkissActivity.class));
                                    break;
                                case 1:
                                    startActivity(new Intent(HomeActivity.this, EsptouchActivity.class));
                                    break;
                            }
                        }
                    })
                    .show();


        } else if (id == R.id.menu_refresh) {


            StartAI.getInstance().getBaseBusiManager().getBindList(1, onCallListener);


        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        TAndL.L("onResume()");


        TAndL.L("connectState = " + connectStatus);
        if (connectStatus) {
            tvConnect.setVisibility(View.GONE);
//            StartAI.getInstance().getBaseBusiManager().getBindList(1, onCallListener);
        } else {
            if (StartAI.getInstance().getPersisitnet().getConnectState() == PersistentConnectState.CONNECTED) {
                tvConnect.setVisibility(View.GONE);
            } else {
                tvConnect.setVisibility(View.VISIBLE);

            }
        }
    }
}
