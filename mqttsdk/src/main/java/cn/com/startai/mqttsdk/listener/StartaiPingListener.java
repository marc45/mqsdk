package cn.com.startai.mqttsdk.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.com.startai.mqttsdk.base.GlobalVariable;
import cn.com.startai.mqttsdk.busi.entity.C_0x8000;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.utils.SLog;


/**
 * Created by Robin on 2018/5/8.
 * qq: 419109715 彬影
 */

public class StartaiPingListener implements StartaiTimerPingSender.PingListener {


    private String TAG = this.getClass().getSimpleName();

    private long t;

    private int avarNum = 7;

    private ArrayList<Long> delayTimes = new ArrayList<>();

    @Override
    public void onHeartStart() {
        t = System.currentTimeMillis();
        SLog.d(TAG, "onHeartStart");
    }

    @Override
    public void onHeartSuccess() {
        long delay = System.currentTimeMillis() - t;

        //TODO:测试 写死延时+2s
//        delay += 2000;

        SLog.d(TAG, "onHeartSuccess time delay = " + delay);
        StartaiMqttPersistent instance = (StartaiMqttPersistent) StartaiMqttPersistent.getInstance();
        long changeHostTimeDelay = MqttConfigure.changeHostTimeDelay;
        if (changeHostTimeDelay == 0) {
            SLog.d(TAG, "没有设置自动切换主机节点，不进行平均延时计算");
            return;
        }

        if (delay > 60 * 1000) {
            SLog.d(TAG, "无效");
            return;

        }

        delayTimes.add(delay);
        if (delayTimes.size() > avarNum) {
            delayTimes.remove(0);
        }

        SLog.d(TAG, delayTimes.toString());
        if (delayTimes.size() == avarNum) {
            //满了七个 就他清除一次时延队列

            SLog.d(TAG, "已经累积七次时延开始计算平均时延并清除时延列表");
            int i = toCalculationAverageDelayTime(delayTimes);
            delayTimes.clear();

            SLog.d(TAG, "近" + avarNum + "次平均时延为 " + i + "ms(去掉最高及最低值计算)   对比设定值 " + changeHostTimeDelay + "ms");
            if (i > changeHostTimeDelay) {
                //计算权值，如果有必要需要更换节点
                calculationWeight(i, instance);
            } else {
                SLog.d(TAG, "时延正常，不做处理");
            }

        }


    }

    /**
     * 计算权值，如果有必要需要更换节点
     *
     * @param instance
     */
    public void calculationWeight(long delay, StartaiMqttPersistent instance) {

        C_0x8000.Resp.ContentBean areaNodeBeans = GlobalVariable.areaNodeBean;

        if (areaNodeBeans != null && areaNodeBeans.getNode().size() != 0) {
            List<C_0x8000.Resp.ContentBean.NodeBean> nodes = areaNodeBeans.getNode();
            for (C_0x8000.Resp.ContentBean.NodeBean nodeBean : nodes) {
                //当前的节点
                String host = instance.getHost();

                if (host.contains(nodeBean.getServer_domain())) {

                    double weight = nodeBean.getWeight();
                    SLog.d(TAG, "时延偏高，开始减权值，当前节点" + host + "权值 = " + weight);

                    double v = delay * 1.0 / 1000;
                    double delayDou = (double) Math.round(v * 100) / 100;
                    SLog.d(TAG, "delayDou = " + delayDou);
                    double v1 = 0.2 * delayDou;
                    weight -= v1;
                    weight = (double) Math.round(weight * 100) / 100;
                    nodeBean.setWeight(weight);

                    SLog.d(TAG, "权值下降 " + v1 + " ,下降后的权值为 " + weight);


                    if (weight <= 0) {
                        SLog.d(TAG, "当前节点" + host + "权值已经小于0，将自动更换节点");
                        //需要更换节点
                        instance.disconnectAndReconnect();
                        break;
                    }

                }
            }
        } else {

            SLog.d(TAG, "本地没有节点权值信息，需要去云端请求");
            StartaiMqttPersistent.getInstance().checkGetAreaNode();
        }


    }


    /**
     * 计算近几次的平均时延 去掉最高及最低值
     *
     * @param list
     * @return
     */
    private int toCalculationAverageDelayTime(ArrayList<Long> list) {

        ArrayList<Long> temp = null;
        int all1 = 0;
        try {
            temp = new ArrayList<>();

            temp.addAll(list);

//            Collections.copy(temp,list);

            //升序
            Collections.sort(temp, new Comparator<Long>() {
                @Override
                public int compare(Long o1, Long o2) {
                    if (o1 > o2) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });

            temp.remove(temp.size() - 1);
            temp.remove(0);

            all1 = 0;
            for (Long aLong : temp) {

                all1 += aLong;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return all1 / temp.size();


    }

    @Override
    public void onHeartFailed(Throwable e) {


        SLog.d(TAG, "onHeartFailed");
    }

    @Override
    public void onReset() {
        delayTimes.clear();
    }

}
