package cn.com.startai.mqttsdk.event;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.busi.entity.C_0x8001;
import cn.com.startai.mqttsdk.busi.entity.C_0x8002;
import cn.com.startai.mqttsdk.busi.entity.C_0x8005;
import cn.com.startai.mqttsdk.busi.entity.C_0x8015;
import cn.com.startai.mqttsdk.busi.entity.C_0x8016;
import cn.com.startai.mqttsdk.busi.entity.C_0x8017;
import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.busi.entity.C_0x8020;
import cn.com.startai.mqttsdk.busi.entity.C_0x8021;
import cn.com.startai.mqttsdk.busi.entity.C_0x8023;
import cn.com.startai.mqttsdk.busi.entity.C_0x8024;
import cn.com.startai.mqttsdk.busi.entity.C_0x8025;
import cn.com.startai.mqttsdk.busi.entity.C_0x8026;
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
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onPassthroughResult(final int result, final C_0x8200.Resp resp, final String errorCode, final String errorMsg, final String passContent) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {


                            @Override
                            public void run() {

                                try {
                                    byte[] bytes = SStringUtils.hexStr2ByteArr(passContent.replace(" ", ""));
                                    list.onPassthroughResult(result, resp, errorCode, errorMsg, passContent, bytes);
                                    return;
                                } catch (NumberFormatException e) {
//                                    e.printStackTrace();
                                }
                                list.onPassthroughResult(result, resp, errorCode, errorMsg, passContent, null);
                            }
                        });
                    } else {
                        try {
                            byte[] bytes = SStringUtils.hexStr2ByteArr(passContent.replace(" ", ""));
                            list.onPassthroughResult(result, resp, errorCode, errorMsg, passContent, bytes);
                            return;
                        } catch (NumberFormatException e) {
//                            e.printStackTrace();
                        }
                        list.onPassthroughResult(result, resp, errorCode, errorMsg, passContent, null);
                    }
                }


            }
        }


    }


    /**
     * 回调第三方智能硬件激活结果
     *
     * @param result
     * @param contentBean
     * @param errorCode
     * @param errorMsg
     */
    public void onHardwareActivateResult(final int result, final String errorCode, final String errorMsg, final C_0x8001.Resp.ContentBean contentBean) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onHardwareActivateResult(result, errorCode, errorMsg, contentBean);
                            }
                        });
                    } else {
                        list.onHardwareActivateResult(result, errorCode, errorMsg, contentBean);
                    }
                }


            }
        }


    }

    /**
     * 设备激活结果
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onActiviteResult(final int result, final String errorCode, final String errorMsg) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onActiviteResult(result, errorCode, errorMsg);
                            }
                        });
                    } else {
                        list.onActiviteResult(result, errorCode, errorMsg);
                    }
                }


            }
        }


    }

    /**
     * 设备注销激活结果
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onUnActiviteResult(final int result, final String errorCode, final String errorMsg) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onUnActiviteResult(result, errorCode, errorMsg);
                            }
                        });
                    } else {
                        list.onUnActiviteResult(result, errorCode, errorMsg);
                    }
                }

            }
        }


    }

    /**
     * 修改备注名返回
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onUpdateRemarkResult(final int result, final String errorCode, final String errorMsg, final C_0x8015.Resp.ContentBean contentBean) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onUpdateRemarkResult(result, errorCode, errorMsg, contentBean);
                            }
                        });
                    } else {
                        list.onUpdateRemarkResult(result, errorCode, errorMsg, contentBean);

                    }
                }


            }
        }


    }


    /**
     * 查询最新软件版本返回
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onGetLatestVersionResult(final int result, final String errorCode, final String errorMsg, final C_0x8016.Resp.ContentBean contentBean) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onGetLatestVersionResult(result, errorCode, errorMsg, contentBean);
                            }
                        });
                    } else {
                        list.onGetLatestVersionResult(result, errorCode, errorMsg, contentBean);

                    }
                }


            }
        }


    }

    /**
     * 注册结果返回
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onRegisterResult(final int result, final String errorCode, final String errorMsg, final C_0x8017.Resp.ContentBean resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onRegisterResult(result, errorCode, errorMsg, resp);
                            }
                        });
                    } else {
                        list.onRegisterResult(result, errorCode, errorMsg, resp);
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
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     * @param loginInfo
     */
    public void onLoginResult(final int result, final String errorCode, final String errorMsg, final C_0x8018.Resp.ContentBean loginInfo) {
        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onLoginResult(result, errorCode, errorMsg, loginInfo);
                            }
                        });
                    } else {
                        list.onLoginResult(result, errorCode, errorMsg, loginInfo);
                    }
                }


            }
        }
    }

    /**
     * 回调获取验证码结果
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onGetIdentifyResult(final int result, final String errorCode, final String errorMsg, final C_0x8021.Resp.ContentBean contentBean) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onGetIdentifyCodeResult(result, errorCode, errorMsg);
                                list.onGetIdentifyCodeResult(result, errorCode, errorMsg, contentBean);
                            }
                        });
                    } else {
                        list.onGetIdentifyCodeResult(result, errorCode, errorMsg);
                        list.onGetIdentifyCodeResult(result, errorCode, errorMsg, contentBean);
                    }
                }

            }
        }
    }

    /**
     * 回调邮件发送结果
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onSendEmailResult(final int result, final String errorCode, final String errorMsg, final C_0x8023.Resp.ContentBean contentBean) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onSendEmailResult(result, errorCode, errorMsg, contentBean);
                            }
                        });
                    } else {
                        list.onSendEmailResult(result, errorCode, errorMsg, contentBean);
                    }
                }


            }
        }
    }

    /**
     * 回调更新用户信息结果
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onGetUserInfoResult(final int result, final String errorCode, final String errorMsg, final C_0x8024.Resp.ContentBean contentBean) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onGetUserInfoResult(result, errorCode, errorMsg, contentBean);
                            }
                        });
                    } else {
                        list.onGetUserInfoResult(result, errorCode, errorMsg, contentBean);
                    }
                }


            }
        }
    }


    /**
     * 修改密码结果回调
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onUpdateUserPwdResult(final int result, final String errorCode, final String errorMsg, final C_0x8025.Resp.ContentBean contentBean) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onUpdateUserPwdResult(result, errorCode, errorMsg, contentBean);
                            }
                        });
                    } else {
                        list.onUpdateUserPwdResult(result, errorCode, errorMsg, contentBean);
                    }
                }


            }
        }
    }


    /**
     * 手机重置登录密码 结果回调
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onResetMobileLoginPwdResult(final int result, final String errorCode, final String errorMsg, final C_0x8026.Resp.ContentBean contentBean) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onResetMobileLoginPwdResult(result, errorCode, errorMsg, contentBean);
                            }
                        });
                    } else {
                        list.onResetMobileLoginPwdResult(result, errorCode, errorMsg, contentBean);
                    }
                }


            }
        }
    }


    /**
     * 回调更新用户信息结果
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onUpdateUserInfoResult(final int result, final String errorCode, final String errorMsg, final C_0x8020.Resp.ContentBean contentBean) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onUpdateUserInfoResult(result, errorCode, errorMsg, contentBean);
                            }
                        });
                    } else {
                        list.onUpdateUserInfoResult(result, errorCode, errorMsg, contentBean);
                    }
                }


            }
        }
    }

    /**
     * 回调 检验验证码结果
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     */
    public void onCheckIdentifyResult(final int result, final String errorCode, final String errorMsg) {
        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onCheckIdetifyResult(result, errorCode, errorMsg);
                            }
                        });
                    } else {
                        list.onCheckIdetifyResult(result, errorCode, errorMsg);
                    }
                }


            }
        }
    }

    /**
     * 回调绑定结果
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     * @param bebindInfo
     */
    public void onBindResult(final int result, final String errorCode, final String errorMsg, final String id, final C_0x8002.Resp.ContentBean.BebindingBean bebindInfo) {
        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onBindResult(result, errorCode, errorMsg, id, bebindInfo);
                            }
                        });
                    } else {
                        list.onBindResult(result, errorCode, errorMsg, id, bebindInfo);
                    }
                }


            }
        }
    }

    /**
     * 解绑回调
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     * @param beunbindid
     */
    public void onUnBindResult(final int result, final String errorCode, final String errorMsg, final String id, final String beunbindid) {
        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onUnBindResult(result, errorCode, errorMsg, id, beunbindid);
                            }
                        });
                    } else {
                        list.onUnBindResult(result, errorCode, errorMsg, id, beunbindid);
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
     * 回调查询绑定关系
     *
     * @param result
     * @param errorCode
     * @param errorMsg
     * @param bindList
     */
    public void onGetBindListResult(final int result, final String errorCode, final String errorMsg, final String id, final ArrayList<C_0x8005.Resp.ContentBean> bindList) {
        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof AOnStartaiMessageArriveListener) {
                    final AOnStartaiMessageArriveListener list = (AOnStartaiMessageArriveListener) listener;

                    if (listener.needUISafety()) {
                        StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                list.onGetBindListResult(result, errorCode, errorMsg, id, bindList);
                            }
                        });
                    } else {
                        list.onGetBindListResult(result, errorCode, errorMsg, id, bindList);
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