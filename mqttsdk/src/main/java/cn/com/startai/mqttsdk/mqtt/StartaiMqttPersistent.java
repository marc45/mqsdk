package cn.com.startai.mqttsdk.mqtt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import cn.com.startai.mqttsdk.IPersisitentNet;
import cn.com.startai.mqttsdk.PersistentConnectState;
import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.base.GlobalVariable;
import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.base.StartaiMessage;
import cn.com.startai.mqttsdk.busi.BaseBusiHandler;
import cn.com.startai.mqttsdk.busi.entity.C_0x8000;
import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.busi.entity.C_0x9998;
import cn.com.startai.mqttsdk.control.AreaConfig;
import cn.com.startai.mqttsdk.control.SDBmanager;
import cn.com.startai.mqttsdk.control.SPController;
import cn.com.startai.mqttsdk.control.TopicConsts;
import cn.com.startai.mqttsdk.control.entity.AreaLocation;
import cn.com.startai.mqttsdk.control.entity.MsgWillSendBean;
import cn.com.startai.mqttsdk.control.entity.TopicBean;
import cn.com.startai.mqttsdk.control.entity.UserBean;
import cn.com.startai.mqttsdk.event.PersistentEventDispatcher;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.listener.IOnSubscribeListener;
import cn.com.startai.mqttsdk.listener.StartaiPingListener;
import cn.com.startai.mqttsdk.listener.StartaiTimerPingSender;
import cn.com.startai.mqttsdk.localbusi.UserBusi;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;
import cn.com.startai.mqttsdk.utils.CallbackManager;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;
import cn.com.startai.mqttsdk.utils.SStringUtils;
import cn.com.startai.mqttsdk.utils.task.CheckActiviteTask;
import cn.com.startai.mqttsdk.utils.task.CheckAreNodeTask;
import cn.com.startai.mqttsdk.utils.task.CheckUnCompleteMsgTask;

/**
 * mqtt 业务处理 主类
 * Created by Robin on 2018/5/8.
 * qq: 419109715 彬影
 */

public class StartaiMqttPersistent implements IPersisitentNet {
    private static String TAG = StartaiMqttPersistent.class.getSimpleName();
    private static String TAGBUSI = TAG + "BUSI";
    private static String TAGSEND = TAG + "SEND";

    private static StartaiMqttPersistent instance;
    private static Handler mConnectHandler;
    private static Handler mBusiHandler;
    private static Handler mMessageSendHandler;

    private PersistentConnectState connectStatus;

    private MqttAsyncClient client;


    private Context context;

    /**
     * 监听网络状态变化
     */
    private WifiReceiver wifiReceiver;

    private int networkType = -1; //-1未知 0 无网络 1 wifi网络 2 wifi网络（未连网） 3 移动网络 4 以太网

    /**
     * 当前连接的节点
     */
    private String lastHost;
    /**
     * 位置及外网ip
     */
    private AreaLocation areaLocation;
    private boolean isDisconnAndReconn;
    private PersistentEventDispatcher eventDispatcher;
    private boolean avaliToktn;
    private int hostIndex; //初始化 broak 节点下标 ，

    private HashMap<Integer, Integer> hostMaps = new HashMap<>();//第个节点连接失败次数
    private StartaiTimerPingSender startaiTimerPingSender;
    private String host;

    public String getHost() {
        return host;
    }

    private StartaiMqttPersistent() {

    }


    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public Handler getMainHandler() {
        return mainHandler;
    }

    public boolean isInit() {
        return SPController.getIsActivite();
    }

    public static StartaiMqttPersistent getInstance() {

        if (instance == null) {
            instance = new StartaiMqttPersistent();
            HandlerThread ht = new HandlerThread(TAG);
            ht.start();
            mConnectHandler = new Handler(ht.getLooper());

            HandlerThread htBusi = new HandlerThread(TAGBUSI);
            htBusi.start();
            mBusiHandler = new Handler(htBusi.getLooper());

            HandlerThread htSend = new HandlerThread(TAGSEND);
            htSend.start();
            mMessageSendHandler = new Handler(htSend.getLooper());

        }

        return instance;

    }

    private BaseBusiHandler busihandler;

    public BaseBusiHandler getBusiHandler() {
        if (busihandler == null) {
            busihandler = new BaseBusiHandler();
        }
        return busihandler;
    }

    public void setBusiHandler(BaseBusiHandler busiHandler) {
        this.busihandler = busiHandler;
    }

    public void setEventDispatcher(PersistentEventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    public PersistentEventDispatcher getEventDispatcher() {
        if (this.eventDispatcher == null) {
            eventDispatcher = PersistentEventDispatcher.getInstance();
        }
        return eventDispatcher;
    }

    @Override
    public PersistentConnectState getConnectState() {
        return connectStatus;
    }


    @Override
    public void send(final MqttPublishRequest request, final IOnCallListener listener) {

        mMessageSendHandler.post(new Runnable() {
            @Override
            public void run() {

                sendMessage(request, listener);

            }
        });
    }


    @Override
    public void subscribe(final String topic, final IOnSubscribeListener listener) {
        mConnectHandler.post(new Runnable() {
            @Override
            public void run() {
                toSubscribe(topic, listener);
            }
        });
    }

    @Override
    public void unSubscribe(final String topic, final IOnSubscribeListener listener) {
        mConnectHandler.post(new Runnable() {
            @Override
            public void run() {
                toUnSubscribe(topic, listener);
            }
        });
    }


    private synchronized void toSubscribe(final String topic, final IOnSubscribeListener listener) {

        if (connectStatus == PersistentConnectState.CONNECTED) {
            try {
                if (TextUtils.isEmpty(topic)) {
                    SLog.e(TAG, "topic must not be null");
                    if (listener != null) {
                        listener.onFailed(topic, new StartaiError(StartaiError.ERROR_SUB_NULL_TOPIC));
                    }
                    return;
                }
                client.subscribe(topic, 1).waitForCompletion();

                SLog.d(TAG, "sub topic = " + topic + " success");
                CallbackManager.callbackSubResult(true, topic, null, listener);
            } catch (MqttException e) {
                SLog.e(TAG, "sub topic = " + topic + " failed");
                CallbackManager.callbackSubResult(false, topic, new StartaiError(e.getReasonCode(), e.getMessage()), listener);
            } catch (Exception e) {
                SLog.e(TAG, "sub topic = " + topic + " failed");
                CallbackManager.callbackSubResult(false, topic, new StartaiError(StartaiError.UNKOWN, "订阅失败，异常"), listener);
            }

        }


    }


    private synchronized void toUnSubscribe(final String topic, final IOnSubscribeListener listener) {


        if (connectStatus == PersistentConnectState.CONNECTED) {
            try {
                client.unsubscribe(topic).waitForCompletion();
                SLog.d(TAG, "unsub topic = " + topic + " success");
                CallbackManager.callbackUnSubResult(true, topic, null, listener);
            } catch (MqttException e) {
                e.printStackTrace();
                SLog.e(TAG, "unsub topic = " + topic + " failed");
                CallbackManager.callbackUnSubResult(false, topic, new StartaiError(e.getReasonCode(), e.getMessage()), listener);
            } catch (Exception e) {
                SLog.e(TAG, "unsub topic = " + topic + " failed");
                CallbackManager.callbackUnSubResult(false, topic, new StartaiError(StartaiError.UNKOWN, "订阅失败，异常"), listener);
            }
        }

    }

    HashMap<String, IOnCallListener> listenerHashMap = new HashMap<>();

    public synchronized void sendMessage(final MqttPublishRequest request, final IOnCallListener listener) {


        if (connectStatus != PersistentConnectState.CONNECTED) {
            SLog.e(TAG, "终端未连接，消息发送失败");
            CallbackManager.callbackMessageSendResult(false, listener, request, new StartaiError(StartaiError.ERROR_SEND_CLIENT_DISCONNECT));

            return;
        }


        if (request == null) {
            SLog.e(TAG, "request is empty");
            return;
        }


        String topic = request.topic;
        int qos = request.qos;

        //强制限定qos为1级别
        if (qos != 0 && qos != 1) {
            qos = 1;
        }

        if (TextUtils.isEmpty(topic)) {
            topic = TopicConsts.NMC_TOPIC + "/" + MqttConfigure.appid;
        }

        String msgSend = "";
        if (request.message instanceof StartaiMessage) {
            StartaiMessage message = (StartaiMessage) request.message;


            boolean isActivite = SPController.getIsActivite();
            if (!isActivite && !message.getMsgtype().equals("0x8000")
                    && !message.getMsgtype().equals("0x8001")) {

                SLog.e(TAG, "终端未激活，消息发送失败");
                CallbackManager.callbackMessageSendResult(false, listener, request, new StartaiError(StartaiError.ERROR_SEND_NO_ACTIVITE));
                return;
            }

            if (TextUtils.isEmpty(message.getMsgtype())
                    || TextUtils.isEmpty(message.getMsgcw())) {
                SLog.e(TAG, "msgtype or msgcw is empty");
                return;
            }

            C_0x8018.Resp.ContentBean currUser = new UserBusi().getCurrUser();
            //部分业务需要终端已登录 才可调用
            if (currUser == null
                    && "0x8020".equals(message.getMsgtype())
                    && "0x8025".equals(message.getMsgtype())
                    && "0x8024".equals(message.getMsgtype())
                    ) {
                CallbackManager.callbackMessageSendResult(false, listener, request, new StartaiError(StartaiError.ERROR_SEND_NO_LOGIN));
                return;
            }


            if (TextUtils.isEmpty(message.getFromid())) {
                if (currUser != null && SPController.getIsActivite()) {
                    message.setFromid(currUser.getUserid());
                } else {
                    message.setFromid(MqttConfigure.getSn(context));
                }
            }

            if (TextUtils.isEmpty(message.getToid())) {

                message.setToid(TopicConsts.CLOUD_TOID);
                topic = topic + "/" + message.getMsgcw();

            }


            message.setM_ver(MqttConfigure.m_ver);
            message.setTs(System.currentTimeMillis() / 1000);

            msgSend = SJsonUtils.toJson(message);

            listenerHashMap.put(message.getMsgtype(), listener);

        } else if (request.message instanceof String) {

            msgSend = (String) request.message;

        }

        try {

            final MqttMessage mqttMsg = new MqttMessage(msgSend.getBytes(Charset.forName("utf-8")));
            mqttMsg.setQos(qos);
            mqttMsg.setRetained(request.retain);


            final String finalTopic = topic;
            if (SStringUtils.isMessyCode(finalTopic)) {
                SLog.e(TAG, "主题包含乱码");
                CallbackManager.callbackMessageSendResult(false, listener, request, new StartaiError(StartaiError.ERROR_SEND_MESSYCODE));
                return;
            }

            SLog.d(TAG, "public before");

            client.publish(topic, mqttMsg, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {

                    SLog.d(TAG, "topic = " + finalTopic + "\nqos = " + mqttMsg.getQos() + "\nretain = " + mqttMsg.isRetained() + "\npublish " + new String(mqttMsg.getPayload()));
                    CallbackManager.callbackMessageSendResult(true, listener, request, null);

                    if (request.message instanceof StartaiMessage) {
                        StartaiMessage m = (StartaiMessage) request.message;
                        MsgWillSendBean msgWillSendByMsgtype = SDBmanager.getInstance().getMsgWillSendByMsgtype(m.getMsgtype());
                        if (msgWillSendByMsgtype != null) {
                            SDBmanager.getInstance().deleteMsgWillSendByMsgType(m.getMsgtype());
                            IOnCallListener iOnCallListener = listenerHashMap.get(m.getMsgtype());
                            if (iOnCallListener != null) {
                                iOnCallListener.onSuccess(request);
                            }
                        }
                    } else {
                        SDBmanager.getInstance().deleteMsgWillSendByMsg(request.message.toString());
                    }
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    MqttException e = ((MqttException) throwable);
                    CallbackManager.callbackMessageSendResult(false, listener, request, new StartaiError(e.getReasonCode(), e.getMessage()));
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
            SLog.e(TAG, "消息发送失败 " + e.getReasonCode() + " " + e.getMessage());
            CallbackManager.callbackMessageSendResult(false, listener, request, new StartaiError(e.getReasonCode(), e.getMessage()));
        }
    }


    public synchronized void toDisconnect(final boolean isSelf) {

        if (connectStatus == PersistentConnectState.CONNECTED) {

            try {
                connectStatus = PersistentConnectState.DISCONNECTING;
                client.disconnect().waitForCompletion();

                SLog.d(TAG, "disConnected");
                connectStatus = PersistentConnectState.DISCONNECTED;
                if (isSelf) {
                    StartAI.getInstance().getPersisitnet().getEventDispatcher().onDisconnect(StartaiError.ERROR_LOST_SELF_DISCONNECT, StartaiError.getErrorMsgByCode(StartaiError.ERROR_LOST_SELF_DISCONNECT));
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 断开连接
     *
     * @param isSelf
     */
    public synchronized void disConnect(final boolean isSelf) {
        mConnectHandler.post(new Runnable() {
            @Override
            public void run() {

                toDisconnect(isSelf);

            }
        });
    }


    @Override
    public void initialization(final Context context, final MqttInitParam pa) {

        if (!initMqttConfiguration(pa)) {
            return;
        }
        this.context = context;
        connect();
        registerNetReceiver();
    }

    private boolean initMqttConfiguration(MqttInitParam pa) {
        if (pa == null
                || TextUtils.isEmpty(pa.appid)
                ) {
            SLog.e(TAG, "Error of initialization parameter");
            return false;
        }


        SPController.setAppid(pa.appid);

        MqttConfigure.appid = pa.appid;
        MqttConfigure.apptype = pa.apptype;
        MqttConfigure.domain = pa.domain;
        if (!TextUtils.isEmpty(pa.m_ver)) {
            MqttConfigure.m_ver = pa.m_ver;
        }


        return true;

    }

    @Override
    public void unInit() {

        if (context != null && wifiReceiver != null) {
            context.unregisterReceiver(wifiReceiver);
        }
        disConnect(true);

        mConnectHandler.removeCallbacksAndMessages(null);
        mainHandler.removeCallbacksAndMessages(null);
        mMessageSendHandler.removeCallbacksAndMessages(null);
        mBusiHandler.removeCallbacksAndMessages(null);


    }

    /**
     * 连接
     */
    private synchronized void connect() {
        mConnectHandler.post(new Runnable() {
            @Override
            public void run() {

                if (!isAvailableNet()) {
                    connectStatus = PersistentConnectState.DISCONNECTED;
                    SLog.e(TAG, "network is not available");
                    StartAI.getInstance().getPersisitnet().getEventDispatcher().onDisconnect(StartaiError.ERROR_LOST_NET_UNVALAIBLE);
                    return;
                }

                boolean realConnectToIntnet = isRealConnectToIntnet();
                if (!realConnectToIntnet) {
                    SLog.e(TAG, "没有真正可用的网络，3秒后重试");
                    mConnectHandler.postDelayed(this, 3000);
                    StartAI.getInstance().getPersisitnet().getEventDispatcher().onDisconnect(StartaiError.ERROR_LOST_NET_UNVALAIBLE);
                    return;
                }


                if (connectStatus != PersistentConnectState.CONNECTING && connectStatus != PersistentConnectState.CONNECTED) {
                    connectStatus = PersistentConnectState.CONNECTING;


                    //获取位置信息
                    String firstBroke = getFirstBroke();
//                    String firstBroke = "ssl://cn2.startai.net:8883";
                    if (TextUtils.isEmpty(firstBroke)) {
                        SLog.e(TAG, "get firstBroke failed");
                        connectStatus = PersistentConnectState.DISCONNECTED;
                        return;
                    }

                    host = firstBroke;


                    //获取上一次连接的clientid如果没有，就新生成一个
                    String clientid = SPController.getClientid();
                    if (TextUtils.isEmpty(clientid)) {
                        clientid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
                    }
                    SLog.d(TAG, "toConnect host = " + host + " clientid = " + clientid);
                    MqttConfigure.clientid = clientid;

                    try {
                        startaiTimerPingSender = new StartaiTimerPingSender(pingListener);
                        client = new MqttAsyncClient(host, clientid, new MemoryPersistence(), startaiTimerPingSender);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                    try {
                        MqttConnectOptions options = StartaiMqttConfig.getConnectOptions(context);

                        client.setCallback(mqttCallback);

                        if (options != null) {


                            client.connect(options).waitForCompletion();

                            connectStatus = PersistentConnectState.CONNECTED;

                            mConnectHandler.removeCallbacksAndMessages(null);

                            hostMaps.put(hostIndex, 0);

                            SLog.d(TAG, "connect success host = " + host);

                            pingListener.onReset();

                            SPController.setClientid(clientid);

                            subUserTopic();
                            subFriendReportTopic();
                            StartAI.getInstance().getPersisitnet().getEventDispatcher().onConnectSuccess();


                            checkActivite();

                            checkGetAreaNode();

                            checkUnCompleteMsg();

                            if (SPController.getIsActivite()) {

                                if (!TextUtils.isEmpty(lastHost) && !lastHost.equals(host)) {
                                    StartAI.getInstance().getPersisitnet().getEventDispatcher().onHostChange(host);
                                }
                                //发送设备上线消息
//                                C_0x9998.m_0x9998_req(null);

                                //判断token是否失败
                                checkIsAvaliToken();


                            }
                            lastHost = host;


                        } else {
                            StartAI.getInstance().getPersisitnet().getEventDispatcher().onConnectFailed(StartaiError.ERROR_CONN_CER, "签名文件有误或找不到");
                        }
                    } catch (MqttException e) {
                        int reasonCode = e.getReasonCode();
                        if (reasonCode == 32100 || reasonCode == 32110 || reasonCode == 32102 || reasonCode == 32111) {
                            return;
                        }


                        connectStatus = PersistentConnectState.CONNECTFAIL;

                        e.printStackTrace();

                        Integer returnCount = hostMaps.get(hostIndex);
                        if (returnCount == null) {
                            returnCount = 0;
                        }
                        returnCount++;
                        if (returnCount % 3 == 0) {//每3次回调一次连接失败
                            if (0 == reasonCode) {
                                StartAI.getInstance().getPersisitnet().getEventDispatcher().onConnectFailed(StartaiError.ERROR_CONN_NET, "Host is unresolved  " + host);
                            } else {
                                StartAI.getInstance().getPersisitnet().getEventDispatcher().onConnectFailed(e.getReasonCode(), e.getMessage() + e.getLocalizedMessage());
                            }

                            //连接3次失败 尝试切换节点连接
                            if (returnCount != 0) {
                                SLog.d(TAG, "连接3次失败 尝试切换节点连接");
                                if (GlobalVariable.areaNodeBean == null) {
                                    SLog.d(TAG, "未获取过区域节点的情况");
                                    //未获取过区域节点的情况
                                    //如果连接五次失败并且没有获取过区域节点信息
                                    // 换个默认节点连接
                                    if (hostIndex + 1 >= MqttConfigure.getHosts().size()) {
                                        hostIndex = 0;
                                    } else {
                                        hostIndex++;
                                    }
                                } else {
                                    //已经获取过区域节点的情况'
                                    //权重 直接减1
                                    SLog.d(TAG, "手动设置延时 10000 ms");
                                    pingListener.calculationWeight(10 * 1000, getInstance());
                                }
                            }
                        }

                        sleep(3000);

                        SLog.d(TAG, "hostIndex = " + hostIndex);
                        hostMaps.put(hostIndex, returnCount);
                        SLog.e(TAG, "连接失败,准备重试 " + returnCount);
                        reconnect();

                    } /*catch (Exception e) {
                        e.printStackTrace();
                        StartAI.getInstance().getPersisitnet().getEventDispatcher().onConnectFailed(StartaiError.ERROR_CONN_UNKNOW, e.getMessage());


                    }*/
                }


            }
        });
    }

    /**
     * 检查toke是否过期
     */
    private void checkIsAvaliToken() {


        UserBusi userBusi = new UserBusi();
        C_0x8018.Resp.ContentBean currUser = userBusi.getCurrUser();
        UserBean currUserFromDb = userBusi.getCurrUserFromDb();

        if (currUserFromDb != null) {
            //已经登录

            //获取上一次判断token的时间

            long l = SPController.getsetLastCheckExpireTime();
            long l1 = System.currentTimeMillis() - l;
            SLog.d(TAG, "ll = " + l1);
            if (l1 < 12 * 60 * 60 * 1000) {
                SLog.d(TAG, "未到检查 token 有效性 的时间");
                return;
            }

            SLog.d(TAG, "定期检查 token 有效性");
            SPController.setLastCheckExpireTime(System.currentTimeMillis());

            long expire_in = currUserFromDb.getExpire_in();
            long time = currUserFromDb.getTime();

            long peroid = (System.currentTimeMillis() - time) / 1000;
            long diff = peroid - expire_in;
            SLog.d(TAG, "expire_in = " + expire_in + " peroid " + peroid);
            if (expire_in > 0 && diff > 0) {
                SLog.d(TAG, "账号登录状态已过期，需要重新登录");
                userBusi.resetDBUser();
                StartAI.getInstance().getPersisitnet().getEventDispatcher().onTokenExpire(currUser);
            } else {
                SLog.d(TAG, "账号登录状态正常，可以正常使用");
            }

        }

    }


    private void checkUnCompleteMsg() {

        new CheckUnCompleteMsgTask().execute();

    }

    private boolean isRealConnectToIntnet() {

        boolean isAvailable;
        try {
            String host = "www.google.com";
            InetAddress byName = InetAddress.getByName("www.google.com");
            SLog.e(TAG, host + " byName = " + byName.getHostAddress());
            isAvailable = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            isAvailable = false;
        }

        if (!isAvailable) {

            try {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String host = "www.baidu.com";
                InetAddress byName = InetAddress.getByName(host);
                SLog.e(TAG, host + " byName = " + byName.getHostAddress());
                isAvailable = true;
            } catch (UnknownHostException e) {
                e.printStackTrace();
                isAvailable = false;
            }
        }

        return isAvailable;
    }

    /**
     * 订阅用户相关主题
     */
    public void subUserTopic() {

        //如果没有登录信息，需要订阅sn相关主题
        this.toSubscribe(TopicConsts.Q_CLIENT + "/" + MqttConfigure.getSn(context) + "/#", null);

        C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();


        SLog.d(TAG, "user = " + userBean);
        if (userBean != null && SPController.getIsActivite()) {
            this.toSubscribe(TopicConsts.Q_CLIENT + "/" + userBean.getUserid() + "/#", null);
        }

    }


    /**
     * 订阅好友遗嘱 主题
     */
    public void subFriendReportTopic() {

        boolean isActivite = SPController.getIsActivite();

        if (isActivite) {

            C_0x8018.Resp.ContentBean userBean = new UserBusi().getCurrUser();
            String id = "";
            if (userBean != null) {
                id = userBean.getUserid();
            } else {
                id = MqttConfigure.getSn(context);
            }
            ArrayList<TopicBean> allTopics = SDBmanager.getInstance().getAllTopic(id);


            SLog.d(this.TAG, "allTopics = " + allTopics);
            if (allTopics != null && allTopics.size() > 0) {
                Iterator iterator = allTopics.iterator();

                while (iterator.hasNext()) {
                    TopicBean t = (TopicBean) iterator.next();
                    if (connectStatus == PersistentConnectState.CONNECTED) {

                        if (t.getType().equals("set")) {
                            try {
                                toSubscribe(t.getTopic(), null);
                                t.setCurrType("set");
                                SDBmanager.getInstance().addOrUpdateTopic(t);
                                SLog.d(this.TAG, "subscribe topic = " + t.toString());
                            } catch (Exception var8) {
                                var8.printStackTrace();
                            }
                        } else {
                            try {
                                toUnSubscribe(t.getTopic(), null);
                                SDBmanager.getInstance().getDB().delete(t);
                                SLog.d(this.TAG, "unSubscribe topic = " + t.toString());
                            } catch (Exception var7) {
                                var7.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

    }


    /**
     * 判断是否已经激活，如果没有激活需要发送激活数据包
     */
    private boolean isActivate() {
        boolean isActivite = SPController.getIsActivite();
        SLog.d(TAG, "isActivate = " + isActivite);
        return isActivite;

    }

    private void checkActivite() {
        new CheckActiviteTask().execute();
    }

    /**
     * 判断是否需要获取区域节点信息
     */
    public void checkGetAreaNode() {

        new CheckAreNodeTask().execute();
    }

    /**
     * 监听网络状态变化
     */

    private void registerNetReceiver() {


        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        wifiReceiver = new WifiReceiver();
        context.registerReceiver(wifiReceiver, filter);

    }


    /**
     * 网络状态监听 用于重连mqtt
     */
    private class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {


            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            SLog.d(TAG, "CONNECTIVITY_ACTION = " + intent.getAction());

            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.isConnected()) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        networkType = 1;
                        SLog.e(TAG, "当前网络 WiFi");
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        networkType = 3;
                        SLog.e(TAG, "当前网络 移动网络");
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        networkType = 4;
                        SLog.e(TAG, "当前网络 以太网");
                    }
                } else {
                    networkType = 0;
                    SLog.e(TAG, "当前没有网络连接，请确保你已经打开网络 !!!");

                }
            } else {   // not connected to the internet
                networkType = 0;
                SLog.e(TAG, "当前没有网络连接，请确保你已经打开网络 ");

            }

            if (connectStatus != null && isAvailableNet()) {
                reconnect();
                return;
            }

        }

    }


    /**
     * 重连
     */
    private synchronized void reconnect() {
        toReconnect();
    }

    private synchronized void toReconnect() {
        mConnectHandler.post(new Runnable() {
            @Override
            public void run() {
                SLog.d(TAG, "reconnect");


                if (connectStatus == PersistentConnectState.CONNECTED || connectStatus == PersistentConnectState.CONNECTING) {
                    SLog.d(TAG, "连接正常或正在准备重连，不需重复连接");
                    return;
                }
                connect();

            }
        });
    }

    private StartaiPingListener pingListener = new StartaiPingListener();


    private MqttCallback mqttCallback = new MqttCallbackExtended() {

        @Override
        public void connectComplete(boolean b, String serverurl) {
            //false 表示 是第一次连接不是重连
            SLog.e(TAG, "connectComplete b = " + b + " serverurl = " + serverurl);
            if (b) { //重连的 连接成功
                connectStatus = PersistentConnectState.CONNECTED;
                StartAI.getInstance().getPersisitnet().getEventDispatcher().onConnectSuccess();
            }
        }

        @Override
        public void connectionLost(Throwable throwable) {
            SLog.e(TAG, "connectionLost " + throwable.getMessage());

            throwable.printStackTrace();

            try {

                MqttException mqttException = ((MqttException) throwable);
                int reasonCode = mqttException.getReasonCode();

                connectStatus = PersistentConnectState.DISCONNECTED;
                StartaiMqttPersistent.getInstance().getEventDispatcher().onDisconnect(reasonCode, mqttException.getMessage());

//                if (MqttConfigure.isAutoReconnection) {

                sleep(3000);
                reconnect();
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void messageArrived(final String topic, final MqttMessage mqttMessage) throws Exception {

            mBusiHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        String msg = new String(mqttMessage.getPayload(), Charset.forName("utf-8"));
                        SLog.d(TAG, "messageArrived topic = " + topic + "\nqos = " + mqttMessage.getQos() + "\nretain = " + mqttMessage.isRetained() + "\n msg = " + msg);


                        getBusiHandler().handMessage(topic, msg);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            SLog.d(TAG, "deliveryComplete ");

        }
    };


    private boolean isCorrectMsgcw(String msgcw) {

        if ("0x07".equals(msgcw)
                || "0x10".equals(msgcw)) {
            return false;
        }
        return true;


    }


    /**
     * 首次连接时获取最优节点
     *
     * @return
     */
    private String getFirstBroke() {

        String defaultHost = MqttConfigure.getHosts().get(hostIndex);
        if (MqttConfigure.changeHostTimeDelay == 0 && MqttConfigure.getHosts() != null && MqttConfigure.getHosts().size() > 0) {
            SLog.d(TAG, "不需要获取最优节点直接使用默认节点");
            return defaultHost;
        }


        //判断是否获取过节点信息
        if (GlobalVariable.areaNodeBean == null) {
            GlobalVariable.areaNodeBean = SPController.getAllAreaNodeBean();
        }

        if (GlobalVariable.areaNodeBean == null) {

            //首次登录
            //没有获取过节点信息，需要去定位获取位置信息 优先连接当地节点（固定几个节点中选择）
            String result = "";
            if (hostIndex != 0) {
                //是由于连接一个url很多次未成功，考虑切换url进行连接，固不需要定位
                return defaultHost;
            }
            SLog.d(TAG, "没有获取过节点信息，需要去定位来匹配本地节点");
            areaLocation = AreaConfig.getArea();
            if (areaLocation == null) {
                SLog.e(TAG, "定位失败，采用默认节点 " + defaultHost + " 进行连接");
                return defaultHost;
            } else {
                // cn us

                String finalUrl = "";
                String area = areaLocation.getCountryCode();

                ArrayList<String> mqttHosts = MqttConfigure.getHosts();
                if (mqttHosts != null && mqttHosts.size() != 0) {
                    for (String mqttHost : mqttHosts) {
                        if (mqttHost.toLowerCase().contains("ssl://" + area.toLowerCase())) {
                            finalUrl = mqttHost;
                        }
                    }
                }


                //如果没有匹配到 区域使用中国节点
                if (TextUtils.isEmpty(finalUrl)) {
                    //没有匹配到当地节点
                    SLog.d(TAG, "没有匹配到当地节点，默认使用中国节点");
                    return MqttConfigure.getHosts().get(0);
                } else {
                    return finalUrl;
                }
            }

        } else {


            //重连
            C_0x8000.Resp.ContentBean.NodeBean newAreaNode = null;
//            GlobalVariable.areaNodeBean = SDBManager.getInstance().getAllAreaNodeBean();
            C_0x8000.Resp.ContentBean areaNodeBean = GlobalVariable.areaNodeBean;
            //选择权值较大的节点
            if (areaNodeBean != null && areaNodeBean.getNode().size() > 0) {
                List<C_0x8000.Resp.ContentBean.NodeBean> nodes = areaNodeBean.getNode();
                for (C_0x8000.Resp.ContentBean.NodeBean nodeBean : nodes) {

                    if (!TextUtils.isEmpty(host) && host.contains(nodeBean.getServer_domain()) && nodeBean.getWeight() > 0) {
                        //只是断线重连 不需要找最大的权值节点
                        SLog.d(TAG, "只是断线重连，不需要找最大的权值节点");
                        return nodeBean.getServer_domain();
                    }


                }

                newAreaNode = getMaxWeightAreNodeBean(areaNodeBean);


            }


            if (newAreaNode != null) {
                SLog.d(TAG, "找到最优节点 host = " + newAreaNode.getServer_domain() + " weight = " + newAreaNode.getWeight());
                return newAreaNode.getServer_domain();
            } else {
                SLog.d(TAG, "权值已经全部小于0，重置所有节点权值重新计算时延");
                GlobalVariable.areaNodeBean = SPController.getAllAreaNodeBean();
                SLog.d(TAG, GlobalVariable.areaNodeBean.toString());

            }

        }

        if (GlobalVariable.areaNodeBean != null && GlobalVariable.areaNodeBean.getNode().size() != 0) {
            C_0x8000.Resp.ContentBean.NodeBean maxWeightAreNodeBean = getMaxWeightAreNodeBean(GlobalVariable.areaNodeBean);
            return maxWeightAreNodeBean.getServer_domain();
        } else {
            return null;
        }
    }


    /**
     * 获取最大的权值的节点
     *
     * @param areaNodeBeans
     * @return
     */
    private C_0x8000.Resp.ContentBean.NodeBean getMaxWeightAreNodeBean(C_0x8000.Resp.ContentBean areaNodeBeans) {

        C_0x8000.Resp.ContentBean.NodeBean newAreaNode = null;

        if (areaNodeBeans != null && areaNodeBeans.getNode().size() > 0) {
            List<C_0x8000.Resp.ContentBean.NodeBean> nodes = areaNodeBeans.getNode();
            for (C_0x8000.Resp.ContentBean.NodeBean nb : nodes) {
                //寻找最大权值节点
                if (nb.getWeight() <= 0) {
                    continue;
                }
                if (newAreaNode == null || nb.getWeight() < nb.getWeight()) {
                    newAreaNode = nb;
                }
            }
        }

        return newAreaNode;
    }


    /**
     * 网络状态
     *
     * @return
     */
    private boolean isAvailableNet() {
        SLog.d(TAG, "networkType = " + networkType);
        if (networkType == 0 || networkType == 2) {
            return false;
        }
        return true;
    }


    /**
     * 开始更新节点
     */
    public void disconnectAndReconnect() {

        SLog.d(TAG, "准备断开连接并重连");

        isDisconnAndReconn = true;
        disConnect(false);

        reconnect();


    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
