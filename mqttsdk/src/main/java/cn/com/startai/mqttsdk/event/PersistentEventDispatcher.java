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
import cn.com.startai.mqttsdk.utils.SStringUtils;

public class PersistentEventDispatcher {

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
                listener.onConnectFail(error, errorMsg);
            }
        }

    }

    /**
     * @param resp
     */
    public void onTokenExpire(final C_0x8018.Resp.ContentBean resp) {
        if (connectStateListenerList != null) {
            for (final IConnectionStateListener listener : connectStateListenerList) {
                if (listener instanceof ICommonStateListener) {
                    ((ICommonStateListener) listener).onTokenExpire(resp);
                }
            }
        }
    }

    public void onConnectSuccess() {

        if (connectStateListenerList != null) {
            for (final IConnectionStateListener listener : connectStateListenerList) {
                listener.onConnected();
            }
        }
    }


    public void onDisconnect(final int errorCode) {

        onDisconnect(errorCode, StartaiError.getErrorMsgByCode(errorCode));
    }

    public void onDisconnect(final int errorCode, final String errorMsg) {


        if (connectStateListenerList != null) {
            for (final IConnectionStateListener listener : connectStateListenerList) {
                listener.onDisconnect(errorCode, errorMsg);
            }
        }
    }


    /**
     * 透传消息
     */
    public void onPassthroughResult(final C_0x8200.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;
                        try {
                            byte[] bytes = SStringUtils.hexStr2ByteArr(resp.getContent().replace(" ", ""));
                            list.onPassthroughResult(resp, resp.getContent(), bytes);
                            return;
                        } catch (NumberFormatException e) {
//                                    e.printStackTrace();
                        }
                        list.onPassthroughResult(resp, resp.getContent(), null);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onHardwareActivateResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onActiviteResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onUnActiviteResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onUpdateRemarkResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onGetLatestVersionResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onRegisterResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onLogoutResult(result, errorCode, errorMsg);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onLoginResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onGetIdentifyCodeResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onSendEmailResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onGetUserInfoResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onUpdateUserPwdResult(resp);
                }


            }
        }
    }


    /**
     * 重置登录密码 结果回调
     */
    public void onResetLoginPwdResult(final C_0x8026.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;
                    list.onResetLoginPwdResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onUpdateUserInfoResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onCheckIdetifyResult(resp);
                }


            }
        }
    }

    public void onThirdPaymentUnifiedOrderResult(final C_0x8028.Resp resp) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;
                    list.onThirdPaymentUnifiedOrderResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;
                    list.onBindResult(resp, id, bebindInfo);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;


                    list.onUnBindResult(resp, id, beunbindid);


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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onDeviceConnectStatusChange(userid, status, sn);


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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onGetRealOrderPayStatus(response);


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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onGetAlipayAuthInfoResult(response);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onBindMobileNumResult(response);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onGetBindListResult(response);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onUnBindThirdAccountResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onGetWeatherInfoResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onBindThirdAccountResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onGetBindListByPageResult(resp);
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

                if (listener instanceof IOnStartaiMsgArriveListener) {
                    final IOnStartaiMsgArriveListener list = (IOnStartaiMsgArriveListener) listener;

                    list.onBindEmailResult(resp);
                }


            }
        }
    }


    public void onMessageArrived(final String topic, final String msg) {

        if (messageArriveListenerList != null) {
            for (final IOnMessageArriveListener listener : messageArriveListenerList) {


                listener.onCommand(topic, msg);

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