package cn.com.startai.mqttsdk.event;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.busi.entity.C_0x8001;
import cn.com.startai.mqttsdk.busi.entity.C_0x8002;
import cn.com.startai.mqttsdk.busi.entity.C_0x8003;
import cn.com.startai.mqttsdk.busi.entity.C_0x8004;
import cn.com.startai.mqttsdk.busi.entity.C_0x8005;
import cn.com.startai.mqttsdk.busi.entity.C_0x8015;
import cn.com.startai.mqttsdk.busi.entity.C_0x8016;
import cn.com.startai.mqttsdk.busi.entity.C_0x8017;
import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.busi.entity.C_0x8020;
import cn.com.startai.mqttsdk.busi.entity.C_0x8021;
import cn.com.startai.mqttsdk.busi.entity.C_0x8022;
import cn.com.startai.mqttsdk.busi.entity.C_0x8023;
import cn.com.startai.mqttsdk.busi.entity.C_0x8024;
import cn.com.startai.mqttsdk.busi.entity.C_0x8025;
import cn.com.startai.mqttsdk.busi.entity.C_0x8026;
import cn.com.startai.mqttsdk.busi.entity.C_0x8028;
import cn.com.startai.mqttsdk.busi.entity.C_0x8031;
import cn.com.startai.mqttsdk.busi.entity.C_0x8033;
import cn.com.startai.mqttsdk.busi.entity.C_0x8034;
import cn.com.startai.mqttsdk.busi.entity.C_0x8035;
import cn.com.startai.mqttsdk.busi.entity.C_0x8036;
import cn.com.startai.mqttsdk.busi.entity.C_0x8037;
import cn.com.startai.mqttsdk.busi.entity.C_0x8038;
import cn.com.startai.mqttsdk.busi.entity.C_0x8039;
import cn.com.startai.mqttsdk.busi.entity.C_0x8200;
import cn.com.startai.mqttsdk.listener.HostChangeListener;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.utils.SStringUtils;

public class PersistentEventDispatcher {

    Handler handler = new Handler(Looper.getMainLooper());
    private static PersistentEventDispatcher instance;

    private static final String TAG = PersistentEventDispatcher.class.getName();

    protected ArrayList<IOnMessageArriveListener> messageArriveListenerList;
    protected ArrayList<IConnectionStateListener> connectStateListenerList;
    protected ArrayList<HostChangeListener> hostChangeListenerList;


    protected PersistentEventDispatcher() {
        this.messageArriveListenerList = null;
        this.connectStateListenerList = null;
        this.hostChangeListenerList = null;
    }

    public static void setInstance(PersistentEventDispatcher instance) {
        PersistentEventDispatcher.instance = instance;
    }

    public static PersistentEventDispatcher getInstance() {

        if (instance == null) {
            instance = new PersistentEventDispatcher();
        }
        return instance;
    }

    public void registerHostChangeListener(HostChangeListener listener) {
        synchronized (this) {
            if (null != listener) {
                if (null == this.hostChangeListenerList) {
                    this.hostChangeListenerList = new ArrayList<>();
                }
                this.hostChangeListenerList.add(listener);
            }
        }
    }

    public void unregisterHostChangeListener(HostChangeListener listener) {
        synchronized (this) {
            if (null != listener && null != this.hostChangeListenerList && 0 < this.hostChangeListenerList.size()) {
                this.hostChangeListenerList.remove(listener);
            }
        }
    }


    public void registerOnPushListener(IOnMessageArriveListener listener) {
        synchronized (this) {
            if (null != listener) {
                if (null == this.messageArriveListenerList) {
                    this.messageArriveListenerList = new ArrayList<>();
                }
                this.messageArriveListenerList.add(listener);
            }
        }
    }

    public void unregisterOnPushListener(IOnMessageArriveListener listener) {
        synchronized (this) {
            if (null != listener && null != this.messageArriveListenerList && 0 < this.messageArriveListenerList.size()) {
                this.messageArriveListenerList.remove(listener);
            }
        }
    }

    public void registerOnTunnelStateListener(IConnectionStateListener listener) {
        synchronized (this) {
            if (null != listener) {
                if (null == this.connectStateListenerList) {
                    this.connectStateListenerList = new ArrayList<>();
                }

                this.connectStateListenerList.add(listener);
            }
        }
    }

    public void unregisterOnTunnelStateListener(IConnectionStateListener listener) {
        synchronized (this) {
            if (null != listener && null != this.connectStateListenerList && 0 < this.connectStateListenerList.size()) {
                this.connectStateListenerList.remove(listener);
            }
        }
    }


    public void onConnectFailed(final int error, final String errorMsg) {

        if (connectStateListenerList != null) {
            for (final IConnectionStateListener listener : connectStateListenerList) {
                if (listener.needUISafety()) {
                    StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onConnectFail(error, errorMsg);
                        }
                    });
                } else {
                    listener.onConnectFail(error, errorMsg);
                }
            }
        }

    }

    /**
     * @param resp
     */
    public void onTokenExpire(final C_0x8018.Resp.ContentBean resp) {
        if (connectStateListenerList != null) {
            for (final IConnectionStateListener listener : connectStateListenerList) {
                if (listener.needUISafety()) {
                    StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {

                        @Override
                        public void run() {
                            if (listener instanceof ICommonStateListener) {
                                ((ICommonStateListener) listener).onTokenExpire(resp);
                            }
                        }
                    });
                } else {
                    if (listener instanceof ICommonStateListener) {
                        ((ICommonStateListener) listener).onTokenExpire(resp);
                    }
                }
            }
        }
    }

    public void onConnectSuccess() {

        if (connectStateListenerList != null) {
            for (final IConnectionStateListener listener : connectStateListenerList) {
                if (listener.needUISafety()) {
                    StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onConnected();
                        }
                    });
                } else {
                    listener.onConnected();
                }
            }
        }
    }


    public void onDisconnect(final int errorCode) {

        onDisconnect(errorCode, StartaiError.getErrorMsgByCode(errorCode));
    }

    public void onDisconnect(final int errorCode, final String errorMsg) {


        if (connectStateListenerList != null) {
            for (final IConnectionStateListener listener : connectStateListenerList) {
                if (listener.needUISafety()) {
                    StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onDisconnect(errorCode, errorMsg);
                        }
                    });
                } else {
                    listener.onDisconnect(errorCode, errorMsg);
                }
            }
        }
    }


    /**
     * 透传消息
     */
    public void onPassthroughResult(final C_0x8200.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {


                            @Override
                            public void run() {

                                try {
                                    byte[] bytes = SStringUtils.hexStr2ByteArr(resp.getContent().replace(" ", ""));
                                    list.onPassthroughResult(resp.getResult(), resp, "", "", resp.getContent(), bytes);
                                    list.onPassthroughResult(resp, resp.getContent(), bytes);
                                    return;
                                } catch (NumberFormatException e) {
//                                    e.printStackTrace();
                                }
                                list.onPassthroughResult(resp.getResult(), resp, "", "", resp.getContent(), null);
                                list.onPassthroughResult(resp, resp.getContent(), null);
                            }
                        });
                    } else {
                        try {
                            byte[] bytes = SStringUtils.hexStr2ByteArr(resp.getContent().replace(" ", ""));
                            list.onPassthroughResult(resp.getResult(), resp, "", "", resp.getContent(), bytes);
                            list.onPassthroughResult(resp, resp.getContent(), bytes);
                            return;
                        } catch (NumberFormatException e) {
//                                    e.printStackTrace();
                        }
                        list.onPassthroughResult(resp.getResult(), resp, "", "", resp.getContent(), null);
                        list.onPassthroughResult(resp, resp.getContent(), null);
                    }
                }


            }
        }


    }


    /**
     * 回调第三方智能硬件激活结果
     *
     * @param resp
     */
    public void onHardwareActivateResult(final C_0x8001.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onHardwareActivateResult(resp);
                                list.onHardwareActivateResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());
                            }
                        });
                    } else {
                        list.onHardwareActivateResult(resp);
                        list.onHardwareActivateResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());

                    }
                }


            }
        }


    }

    /**
     * 设备激活结果
     */
    public void onActiviteResult(final C_0x8001.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onActiviteResult(resp);

                                list.onActiviteResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg());


                            }
                        });
                    } else {
                        list.onActiviteResult(resp);
                        list.onActiviteResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg());


                    }
                }


            }
        }


    }

    /**
     * 设备注销激活结果
     */
    public void onUnActiviteResult(final C_0x8003.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onUnActiviteResult(resp);
                                list.onUnActiviteResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg());


                            }
                        });
                    } else {
                        list.onUnActiviteResult(resp);
                        list.onUnActiviteResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg());

                    }
                }

            }
        }


    }

    /**
     * 修改备注名返回
     */
    public void onUpdateRemarkResult(final C_0x8015.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onUpdateRemarkResult(resp);
                                list.onUpdateRemarkResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());
                            }
                        });
                    } else {

                        list.onUpdateRemarkResult(resp);
                        list.onUpdateRemarkResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());
                    }
                }


            }
        }


    }


    /**
     * 查询最新软件版本返回
     */
    public void onGetLatestVersionResult(final C_0x8016.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onGetLatestVersionResult(resp);
                                list.onGetLatestVersionResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());

                            }
                        });
                    } else {
                        list.onGetLatestVersionResult(resp);
                        list.onGetLatestVersionResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());

                    }
                }


            }
        }


    }

    /**
     * 注册结果返回
     */
    public void onRegisterResult(final C_0x8017.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onRegisterResult(resp);
                                list.onRegisterResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());


                            }
                        });
                    } else {
                        list.onRegisterResult(resp);
                        list.onRegisterResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());

                    }
                }


            }
        }


    }

    /**
     * 回调登录 结果到应用层
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onLogoutResult(final int result, final String errorCode, final String errorMsg) {
        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onLogoutResult(result, errorCode, errorMsg);
                            }
                        });
                    } else {
                        list.onLogoutResult(result, errorCode, errorMsg);

                    }
                }


            }
        }
    }


    /**
     * 回调登录 结果到应用层
     */
    public void onLoginResult(final C_0x8018.Resp resp) {
        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onLoginResult(resp);
                                list.onLoginResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());

                            }
                        });
                    } else {
                        list.onLoginResult(resp);
                        list.onLoginResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());

                    }
                }


            }
        }
    }

    /**
     * 回调获取验证码结果
     */
    public void onGetIdentifyResult(final C_0x8021.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onGetIdentifyCodeResult(resp);
                                list.onGetIdentifyCodeResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());

                            }
                        });
                    } else {
                        list.onGetIdentifyCodeResult(resp);
                        list.onGetIdentifyCodeResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());

                    }
                }

            }
        }
    }

    /**
     * 回调邮件发送结果
     */
    public void onSendEmailResult(final C_0x8023.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onSendEmailResult(resp);
                                list.onSendEmailResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());


                            }
                        });
                    } else {
                        list.onSendEmailResult(resp);
                        list.onSendEmailResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());

                    }
                }


            }
        }
    }

    /**
     * 回调更新用户信息结果
     */
    public void onGetUserInfoResult(final C_0x8024.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onGetUserInfoResult(resp);
                                list.onGetUserInfoResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());

                            }
                        });
                    } else {
                        list.onGetUserInfoResult(resp);
                        list.onGetUserInfoResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());
                    }
                }


            }
        }
    }


    /**
     * 修改密码结果回调
     */
    public void onUpdateUserPwdResult(final C_0x8025.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onUpdateUserPwdResult(resp);
                                list.onUpdateUserPwdResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());

                            }
                        });
                    } else {
                        list.onUpdateUserPwdResult(resp);
                        list.onUpdateUserPwdResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());

                    }
                }


            }
        }
    }



    /**
     *   重置登录密码 结果回调
     */
    public void onResetLoginPwdResult(final C_0x8026.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onResetLoginPwdResult(resp);

                            }
                        });
                    } else {
                        list.onResetLoginPwdResult(resp);

                    }
                }


            }
        }
    }

    /**
     * 回调更新用户信息结果
     */
    public void onUpdateUserInfoResult(final C_0x8020.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onUpdateUserInfoResult(resp);
                                list.onUpdateUserInfoResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());


                            }
                        });
                    } else {
                        list.onUpdateUserInfoResult(resp);
                        list.onUpdateUserInfoResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), resp.getContent());


                    }
                }


            }
        }
    }

    /**
     * 回调 检验验证码结果
     */
    public void onCheckIdentifyResult(final C_0x8022.Resp resp) {
        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onCheckIdetifyResult(resp);
                                list.onCheckIdetifyResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg());


                            }
                        });
                    } else {
                        list.onCheckIdetifyResult(resp);
                        list.onCheckIdetifyResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg());

                    }
                }


            }
        }
    }

    public void onThirdPaymentUnifiedOrderResult(final C_0x8028.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onThirdPaymentUnifiedOrderResult(resp);


                            }
                        });
                    } else {
                        list.onThirdPaymentUnifiedOrderResult(resp);

                    }
                }


            }
        }

    }


    /**
     * 回调绑定结果
     *
     * @param bebindInfo
     */
    public void onBindResult(final C_0x8002.Resp resp, final String id, final C_0x8002.Resp.ContentBean.BebindingBean bebindInfo) {
        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onBindResult(resp, id, bebindInfo);
                                list.onBindResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), id, bebindInfo);

                            }
                        });
                    } else {
                        list.onBindResult(resp, id, bebindInfo);
                        list.onBindResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), id, bebindInfo);
                    }
                }


            }
        }
    }


    /**
     * 解绑回调
     *
     * @param beunbindid
     */
    public void onUnBindResult(final C_0x8004.Resp resp, final String id, final String beunbindid) {
        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onUnBindResult(resp, id, beunbindid);
                                list.onUnBindResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), id, beunbindid);


                            }
                        });
                    } else {
                        list.onUnBindResult(resp, id, beunbindid);
                        list.onUnBindResult(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg(), id, beunbindid);
                    }
                }


            }
        }
    }


    /**
     * 智能设备的连接状态变更
     *
     * @param userid 接收消息的userid
     * @param status 1 上线 0下线
     * @param sn     状态变更的设备sn
     */
    public void onDeviceConnectStatusChanged(final String userid, final int status, final String sn) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onDeviceConnectStatusChange(userid, status, sn);
                            }
                        });
                    } else {
                        list.onDeviceConnectStatusChange(userid, status, sn);
                    }
                }


            }
        }

    }


    /**
     * 查询订单支付状态
     */
    public void onGetRealOrderPayStatusResult(final C_0x8031.Resp response) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onGetRealOrderPayStatus(response);
                            }
                        });
                    } else {
                        list.onGetRealOrderPayStatus(response);
                    }
                }


            }
        }
    }

    /**
     * 查询支付宝认证信息
     */
    public void onGetAlipayAuthInfoResult(final C_0x8033.Resp response) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onGetAlipayAuthInfoResult(response);
                            }
                        });
                    } else {
                        list.onGetAlipayAuthInfoResult(response);
                    }
                }


            }
        }
    }

    /**
     * 绑定手机号 结果
     */
    public void onBindMobileNumResult(final C_0x8034.Resp response) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onBindMobileNumResult(response);
                            }
                        });
                    } else {
                        list.onBindMobileNumResult(response);
                    }
                }


            }
        }
    }

    /**
     * 回调查询绑定关系
     */
    public void onGetBindListResult(final C_0x8005.Response response) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onGetBindListResult(response);
                                list.onGetBindListResult(response.getResult(), response.getErrcode(), response.getErrmsg(), response.getToid(), response.getResp());
                            }
                        });
                    } else {
                        list.onGetBindListResult(response);
                        list.onGetBindListResult(response.getResult(), response.getErrcode(), response.getErrmsg(), response.getToid(), response.getResp());
                    }
                }


            }
        }
    }


    /**
     * 解绑第三方账号返回
     *
     * @param resp
     */
    public void onUnBindThirdAccountResult(final C_0x8036.Resp resp) {
        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onUnBindThirdAccountResult(resp);
                            }
                        });
                    } else {
                        list.onUnBindThirdAccountResult(resp);
                    }
                }


            }
        }
    }

    /**
     * 查询天气返回
     *
     * @param resp
     */
    public void onGetWeatherInfoResult(final C_0x8035.Resp resp) {
        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onGetWeatherInfoResult(resp);
                            }
                        });
                    } else {
                        list.onGetWeatherInfoResult(resp);
                    }
                }


            }
        }
    }

    /**
     * 绑定第三方账号返回
     *
     * @param resp
     */
    public void onBindThirdAccountResult(final C_0x8037.Resp resp) {
        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onBindThirdAccountResult(resp);
                            }
                        });
                    } else {
                        list.onBindThirdAccountResult(resp);
                    }
                }


            }
        }
    }

    /**
     * 查询好友列表 分页返回
     *
     * @param resp
     */
    public void onGetBindListByPageResult(final C_0x8038.Resp resp) {
        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onGetBindListByPageResult(resp);
                            }
                        });
                    } else {
                        list.onGetBindListByPageResult(resp);
                    }
                }


            }
        }
    }

    /**
     * 绑定邮箱账号返回
     *
     * @param resp
     */
    public void onBindEmailResult(final C_0x8039.Resp resp) {
        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onBindEmailResult(resp);
                            }
                        });
                    } else {
                        list.onBindEmailResult(resp);
                    }
                }


            }
        }
    }


    public void onMessageArrived(final String topic, final String msg) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {


                if (listener.needUISafety()) {
                    StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                        @Override
                        public void run() {

                            listener.onCommand(topic, msg);
                        }
                    });
                } else {
                    listener.onCommand(topic, msg);
                }

            }
        }


    }


    public void onHostChange(String url) {
        if (hostChangeListenerList != null) {
            for (HostChangeListener hostChangeListener : hostChangeListenerList) {

                hostChangeListener.onHostChange(url);
            }
        }
    }


}