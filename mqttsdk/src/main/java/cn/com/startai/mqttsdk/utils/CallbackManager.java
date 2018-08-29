package cn.com.startai.mqttsdk.utils;


import cn.com.startai.mqttsdk.base.StartaiError;
import cn.com.startai.mqttsdk.event.IConnectionStateListener;
import cn.com.startai.mqttsdk.listener.IOnCallListener;
import cn.com.startai.mqttsdk.listener.IOnSubscribeListener;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;
import cn.com.startai.mqttsdk.mqtt.request.MqttPublishRequest;

/**
 * Created by Robin on 2018/5/23.
 * qq: 419109715 彬影
 */

public class CallbackManager {


    /**
     * 回调取消订阅结果
     *
     * @param result   订阅结果
     * @param topic    主题
     * @param error    异常信息
     * @param listener 回调
     */
    public static void callbackUnSubResult(final boolean result, final String topic, final StartaiError error, final IOnSubscribeListener listener) {

        if (listener != null) {


            if (listener.needUISafety()) {
                StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        callbackSubResult_(result, topic, error, listener);
                    }
                });
            } else {
                callbackSubResult_(result, topic, error, listener);
            }

        }

    }

    private static void callbackUnSubResult_(final boolean result, final String topic, StartaiError error, final IOnSubscribeListener listener) {

        if (result) {
            listener.onSuccess(topic);
        } else {
            listener.onFailed(topic, new StartaiError(error.getErrorCode(), error.getErrorMsg()));
        }
    }


    /**
     * 回调订阅结果
     *
     * @param result   订阅结果
     * @param topic    主题
     * @param error    异常信息
     * @param listener 回调
     */
    public static void callbackSubResult(final boolean result, final String topic, final StartaiError error, final IOnSubscribeListener listener) {

        if (listener != null) {


            if (listener.needUISafety()) {
                StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        callbackSubResult_(result, topic, error, listener);
                    }
                });
            } else {
                callbackSubResult_(result, topic, error, listener);
            }

        }

    }

    private static void callbackSubResult_(final boolean result, final String topic, StartaiError error, final IOnSubscribeListener listener) {

        if (result) {
            listener.onSuccess(topic);
        } else {
            listener.onFailed(topic, new StartaiError(error.getErrorCode(), error.getErrorMsg()));
        }
    }


    /**
     * 回调消息发送的结果
     *
     * @param result       消息发送成功
     * @param listener     监听
     * @param request      请求
     * @param startaiError 错误信息
     */
    public static void callbackMessageSendResult(final boolean result, final IOnCallListener listener, final MqttPublishRequest request, final StartaiError startaiError) {
        if (listener != null) {
            if (listener.needUISafety()) {
                StartaiMqttPersistent.getInstance().getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        callbackMessageSendResult_(result, listener, request, startaiError);
                    }
                });
            } else {
                callbackMessageSendResult_(result, listener, request, startaiError);
            }

        }
    }

    private static void callbackMessageSendResult_(boolean result, IOnCallListener listener, MqttPublishRequest request, StartaiError startaiError) {

        if (result) {
            listener.onSuccess(request);
        } else {
            listener.onFailed(request, startaiError);
        }

    }




}
