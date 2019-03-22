package cn.com.startai.mqttsdk.utils.task;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.util.TimerTask;

import cn.com.startai.mqttsdk.base.GlobalVariable;
import cn.com.startai.mqttsdk.busi.entity.C_0x8000;
import cn.com.startai.mqttsdk.busi.entity.C_0x8001;
import cn.com.startai.mqttsdk.busi.entity.C_0x8019;
import cn.com.startai.mqttsdk.control.AreaConfig;
import cn.com.startai.mqttsdk.control.SPController;
import cn.com.startai.mqttsdk.control.entity.AreaLocation;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.utils.SLog;
import cn.com.startai.mqttsdk.utils.STimerUtil;

/**
 * Created by Robin on 2018/6/20.
 * qq: 419109715 彬影
 */

public class CheckAreNodeTask extends AsyncTask {
    private String TAG = CheckAreNodeTask.class.getSimpleName();

    @Override
    protected Object doInBackground(Object[] objects) {


        if (!SPController.getIsActivite()) {
            //如果设备还没有激活，取消获取

            return null;
        }

        boolean isNeedToGet_0x8000 = false;

        if (MqttConfigure.changeHostTimeDelay == 0) {
            SLog.d(TAG, "没有设置自动切换节点，不需要去获取区域节点信息");
            cancel(true);
            return null;
        }

        if (GlobalVariable.areaNodeBean == null || GlobalVariable.areaNodeBean.getNode().size() == 0) {

            SLog.d(TAG, "没有获取过节点信息");
            isNeedToGet_0x8000 = true;
        }

        if (!isNeedToGet_0x8000) {
            //获取上次同步 区域节点的时间， 如果 大于 设定值就去同步一下
            long lastGet_0x8000_RespTime = SPController.getLastGet_0x800_respTime(); //上次同步时间
            int syncTime = GlobalVariable.areaNodeBean.getCycle() * 1000; //同步周期
            long peroid = System.currentTimeMillis() - lastGet_0x8000_RespTime;
            SLog.d(TAG, "验证上次同步区域节点时间 lastGet = " + lastGet_0x8000_RespTime + " syncTime = " + syncTime + " peroid = " + peroid);
            if (!isNeedToGet_0x8000 && peroid >= syncTime) {
                SLog.d(TAG, "获取过节点信息，但离上次获取已经超过了限定值,重新同步区域节点信息");
                isNeedToGet_0x8000 = true;
            } else {
                SLog.d(TAG, "未到同步区域节点时间，无需同步");

                long delay = syncTime - peroid;
                SLog.d(TAG, delay + "ms后将会再次同步");
                STimerUtil.schedule("checkGetAreaNode", new TimerTask() {
                    @Override
                    public void run() {
                        new CheckAreNodeTask().execute();
                    }
                }, delay, delay);

            }
        }


        if (isNeedToGet_0x8000) {
            AreaLocation areaLocation = AreaConfig.getArea();


            C_0x8000.m_0x8000_req(areaLocation == null ? "" : areaLocation.getQuery(), null);

            STimerUtil.schedule("checkGetAreaNode", new TimerTask() {
                @Override
                public void run() {
                    new CheckAreNodeTask().execute();
                }
            }, 60 * 1000, 60 * 1000);

        }
        return null;
    }




}
