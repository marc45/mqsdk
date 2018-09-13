package cn.com.startai.mqsdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import cn.com.startai.mqsdk.util.TAndL;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.busi.entity.C_0x8005;
import cn.com.startai.mqttsdk.busi.entity.C_0x8015;

public class UpdateRemarkActivity extends BaseActivity {

    private EditText etRemark;
    private C_0x8005.Resp.ContentBean device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_remark);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("修改备注名");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initview();
        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        device = (C_0x8005.Resp.ContentBean) intent.getSerializableExtra("C_0x8005.Resp.ContentBean");
        if (device != null) {
            etRemark.setText(device.getAlias());
        }


    }

    private void initview() {

        etRemark = (EditText) findViewById(R.id.et_update_remark);

    }

    @Override
    public void onUpdateRemarkResult(C_0x8015.Resp resp) {

        if (resp.getResult() == 1) {
            if (resp.getContent().getFid().equals(device.getId())) {
                TAndL.TL(getApplicationContext(), "备注修改成功 " + resp);
                etRemark.setText(resp.getContent().getRemark());
                finish();
            }
        } else {
            TAndL.TL(getApplicationContext(), "备注修改失败 " + resp);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle("保存");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            String remark = etRemark.getText().toString();

            StartAI.getInstance().getBaseBusiManager().updateRemark( device.getId(), remark, onCallListener);



            return true;

        }
        return super.onOptionsItemSelected(item);
    }


}

