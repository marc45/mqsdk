package cn.com.startai.mqsdk;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.Date;
import java.util.TimerTask;

import cn.com.startai.mqsdk.busi.C_0x8101;
import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqsdk.util.eventbus.E_0x8001_Resp_;
import cn.com.startai.mqsdk.util.eventbus.E_0x8101_Resp;
import cn.com.startai.mqsdk.util.eventbus.E_0x8200_Resp;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.busi.entity.C_0x8001;
import cn.com.startai.mqttsdk.busi.entity.C_0x8005;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.STimerUtil;

public class DeviceControlActivity extends BaseActivity implements View.OnClickListener {

    private EditText etHexStr;
    private Button btHexStr;
    private Button btMiof;
    private C_0x8005.Resp.ContentBean device;
    private EditText etNormal;
    private Button btNormal;
    private Button btHardwareAct;
    private EditText etPeroid;
    private TextView tvLog;
    private TextView tvRecvCount;
    private TextView tvSendCount;
    private int recvCount;
    private int recvCountLen;

    private int sendCount;
    private int sendCountLen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);


        Toolbar toolbar = (Toolbar) findViewById(R.id.include4);
        toolbar.setTitle("设备控制");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        device = (C_0x8005.Resp.ContentBean) getIntent().getSerializableExtra("device");

        initview();
        initListener();

    }

    @Override
    public void onPassthrouthResult(E_0x8200_Resp resp) {
        super.onPassthrouthResult(resp);
        if (device != null) {
            String id = device.getId();
            if (resp.getResp() != null) {
                if (resp.getResp().getFromid().equals(id)) {
//                    TAndL.TL(getApplicationContext(), "透传 result = " + resp.getResult() + "fromid = " + resp.getResp().getFromid() + " errorMsg = " + resp.getErrorMsg() + " data = " + resp.getDataString() + " dataArr = " + Arrays.toString(resp.getDataByteArray()));

                    String dataString = resp.getDataString();
                    recvCountLen += dataString.length();
                    recvCount++;
                    tvRecvCount.setText("共接收 " + recvCount + " 消息 总长度 " + recvCountLen);
                    apendLog("recv = " + dataString + "  ---" + recvCount);
                }
            }
        }
    }


    private void apendLog(String dataString) {



        String currTimeStr = new SimpleDateFormat("HH:mm:ss--sss").format(new Date());

        CharSequence text = tvLog.getText();

        if(text.length()>50*1024*1024){
            tvLog.setText("");
        }

        tvLog.setText("\n" + dataString + " ----- " + currTimeStr + "\n" + text);

    }

    @Override
    public void on_0x8101_resp(E_0x8101_Resp resp) {
        super.on_0x8101_resp(resp);
        if (device != null) {
            String id = device.getId();
            if (resp.getResp() != null) {
                if (resp.getResp().getFromid().equals(id)) {
                    TAndL.TL(getApplicationContext(), "设置音量结果 " + resp.getResult() + " value = " + resp.getResp().getContent().getValue());
                }
            }
        }
    }

    @Override
    public void onHardwareActivateResult(E_0x8001_Resp_ resp_) {
        super.onHardwareActivateResult(resp_);

        TAndL.TL(getApplicationContext(), "代激活结果 result = " + resp_.getResult() + " errorMsg = " + resp_.getErrorMsg());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        STimerUtil.close("etPeroid");
    }

    private void initview() {
        etHexStr = (EditText) findViewById(R.id.et_device_control_hexstr);
        etNormal = (EditText) findViewById(R.id.et_devicecontrol_msg);
        btHexStr = (Button) findViewById(R.id.bt_device_control_passthrough);
        btMiof = (Button) findViewById(R.id.bt_device_control_miof);
        btHardwareAct = (Button) findViewById(R.id.bt_hardware_act);
        btNormal = (Button) findViewById(R.id.bt_normal_msg);
        etPeroid = (EditText) findViewById(R.id.et_ps_peroid);
        tvLog = (TextView) findViewById(R.id.id_ps_log);
        tvLog.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvRecvCount = (TextView) findViewById(R.id.id_ps_recvcount);
        tvSendCount = (TextView) findViewById(R.id.id_ps_sendcount);

    }

    private void initListener() {
        btHexStr.setOnClickListener(this);
        btMiof.setOnClickListener(this);
        btNormal.setOnClickListener(this);
        btHardwareAct.setOnClickListener(this);
        tvLog.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                tvLog.setText("");

                return false;
            }
        });

    }

    private IOnCallListener lis = new IOnCallListener() {
        @Override
        public void onSuccess(MqttPublishRequest request) {

            StartaiMessage message = (StartaiMessage) request.message;
            String s = message.getContent().toString();
            sendCountLen += s.length();

            sendCount++;
            tvSendCount.setText("共发送 " + sendCount + " 消息 总长度 = " + sendCountLen);
            apendLog("send = " + s + "  ---" + sendCount);
        }

        @Override
        public void onFailed(MqttPublishRequest request, StartaiError startaiError) {

        }

        @Override
        public boolean needUISafety() {
            return true;
        }
    };


    @Override
    public void onClick(View v) {


        int i = v.getId();
        if (i == R.id.bt_device_control_passthrough) {//消息透传

            String s = etPeroid.getText().toString();
            long peroid = 0;
            try {

                peroid = Long.parseLong(s);

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (peroid == 0) {
                STimerUtil.close("etPeroid");
                String hexStr = etHexStr.getText().toString();
                StartAI.getInstance().getBaseBusiManager().passthrough(device.getId(), hexStr, lis);
            } else {
                STimerUtil.schedule("etPeroid", new TimerTask() {
                    @Override
                    public void run() {
                        String hexStr = etHexStr.getText().toString();
                        StartAI.getInstance().getBaseBusiManager().passthrough(device.getId(), hexStr, lis);
                    }
                }, 0, peroid);
            }


        } else if (i == R.id.bt_normal_msg) {//待发送的消息
            String msg = etNormal.getText().toString();

            MqttPublishRequest request = new MqttPublishRequest();
            request.topic = device.getTopic();
            request.message = msg;

            StartAI.getInstance().send(request, onCallListener);


        } else if (i == R.id.bt_device_control_miof) {
            C_0x8101.m_0x8101_req(device.getTopic(), device.getId(), 4, onCallListener);


        } else if (i == R.id.bt_hardware_act) {
            C_0x8001.Req.ContentBean contentBean = new C_0x8001.Req.ContentBean();

            contentBean.setAppid("ae6529f2fc52782a6d75db3259257084");
            contentBean.setApptype("smartOlWifi");
            contentBean.setClientid("SNSNSNSNSNSNSNSNSNSNSNSNSNSNSNSN");
            contentBean.setDomain("startai");
            contentBean.setSn("SNSNSNSNSNSNSNSNSNSNSNSNSNSNSNSN");
            contentBean.setM_ver("Json_1.2.9_9.2.1");

            C_0x8001.Req.ContentBean.FirmwareParamBean firmwareParamBean = new C_0x8001.Req.ContentBean.FirmwareParamBean();
            firmwareParamBean.setBluetoothMac("AA:AA:AA:AA:AA:AA");
            firmwareParamBean.setFirmwareVersion("abc");


            contentBean.setFirmwareParam(firmwareParamBean);

            //代智能硬件激活
            StartAI.getInstance().getBaseBusiManager().hardwareActivate(contentBean, onCallListener);


        }


    }


}
