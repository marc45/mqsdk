package cn.com.startai.mqsdk;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.UUID;

import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqsdk.util.eventbus.E_0x8002_Resp;
import cn.com.startai.mqsdk.util.view.RadarView;
import cn.com.startai.mqsdk.util.view.RandomTextView;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.utils.udp.LanDevice;
import cn.com.startai.mqttsdk.utils.udp.LanDeviceFinder;

public class LanDeviceFindActivity extends BaseActivity {

    private RadarView radarView;
    private RandomTextView randomTextView;
    private Button btShowDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lan_device_find);


        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
        toolbar.setTitle("局域网发现");
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

        radarView.start();
        startFindLanDevice();
    }

    private HashMap<String, LanDevice> map = new HashMap<>();

    private void stopFindLanDevice() {
        LanDeviceFinder.getInstance().stop();
    }

    private void startFindLanDevice() {


        String userid = UUID.randomUUID().toString().replace("-", "");
        LanDeviceFinder.getInstance().find(userid, "", 30 * 1000, new LanDeviceFinder.IDeviceFindListener() {
            @Override
            public void onDeviceFind(final LanDevice devices) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        randomTextView.addKeyWord(devices.getMac());
                        randomTextView.show();

                        map.put(devices.getMac(), devices);
                    }
                });

            }

            @Override
            public void onTimeout() {
                TAndL.TL(getApplicationContext(), "超时");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        radarView.stop();

                        retryNotice();
                    }
                });
            }

            @Override
            public void onException(Exception e) {
                TAndL.TL(getApplicationContext(), "异常 = " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        radarView.stop();

                        retryNotice();
                    }
                });
            }

            @Override
            public void onStop() {
                radarView.stop();
            }
        });
    }

    /**
     * 重试确认
     */
    private void retryNotice() {
        new AlertDialog.Builder(LanDeviceFindActivity.this)
                .setTitle("提示")
                .setMessage("未扫描到局域网设备，超时!!!")
                .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        radarView.start();
                        startFindLanDevice();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

    private void initListener() {

        randomTextView.setOnRippleViewClickListener(new RandomTextView.OnRippleViewClickListener() {
            @Override
            public void onRippleViewClicked(View view, String keywords) {


                final LanDevice lanDevice = map.get(keywords);
                if (lanDevice != null) {

                    new AlertDialog.Builder(LanDeviceFindActivity.this)
                            .setTitle("提示")
                            .setMessage("sn:" + lanDevice.getSn()
                                    + "\nmac:" + lanDevice.getMac()
                                    + "\nname:" + lanDevice.getName()
                                    + "\ncpuid:" + lanDevice.getCpuId()
                                    + "\nip:" + lanDevice.getIp()
                                    + "\nport:" + lanDevice.getPort()
                                    + "\nconnectStatus:" + lanDevice.getDevConnStatus()
                                    + "\nactivateStatus:" + lanDevice.getDevActivateStatus()
                                    + "\nRssi:" + lanDevice.getRssi()
                            )
                            .setPositiveButton("绑定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    StartAI.getInstance().getBaseBusiManager().bind(lanDevice.getSn(), onCallListener);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                }

            }
        });

    }

    @Override
    public void onBindResult(E_0x8002_Resp resp) {
        super.onBindResult(resp);
        int result = resp.getResult();
        if (result == 1) {
            TAndL.TL(getApplicationContext(), "绑定成功" + resp.getBebinding());
            finish();
        } else {
            TAndL.TL(getApplicationContext(), "绑定失败 " + resp.getErrorMsg());
        }
    }

    private void initView() {
        radarView = findViewById(R.id.radar);
        randomTextView = findViewById(R.id.random_textview);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        radarView.stop();
        stopFindLanDevice();
    }
}

