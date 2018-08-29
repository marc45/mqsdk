package cn.com.startai.mqttsdk.utils.task;

import android.os.AsyncTask;

import java.util.TimerTask;

import cn.com.startai.mqttsdk.busi.entity.C_0x8001;
import cn.com.startai.mqttsdk.control.SPController;
import cn.com.startai.mqttsdk.utils.SLog;
import cn.com.startai.mqttsdk.utils.STimerUtil;

/**
 * Created by Robin on 2018/6/20.
 * qq: 419109715 彬影
 */

public class CheckActiviteTask extends AsyncTask {
    private String TAG = CheckActiviteTask.class.getSimpleName();

    @Override
    protected Object doInBackground(Object[] objects) {

        //如果设备激活没有成功，将会在每60秒去激活一次

        STimerUtil.schedule("CheckActiviteTask", new TimerTask() {
            @Override
            public void run() {

                boolean isActivite = SPController.getIsActivite();
                if (!isActivite) {
                    SLog.d(TAG, "设备未激活，正在准备激活");
                    C_0x8001.m_0x8001_req(null,null);
                } else {
                    SLog.d(TAG, "设备已经正常激活");
                    cancel();
                }
            }
        }, 0, 60 * 1000);


        return null;
    }
}
