/*******************************************************************************
 * Copyright (c) 2014 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution. 
 *
 * The Eclipse Public License is available at 
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 */

package cn.com.startai.mqttsdk.listener;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.internal.ClientComms;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Default ping sender implementation
 * <p>
 * <p>This class implements the {@link MqttPingSender} pinger interface
 * allowing applications to sendAsync ping packet to server every keep alive interval.
 * </p>
 *
 * @see MqttPingSender
 */
public class StartaiTimerPingSender implements MqttPingSender {

    private PingListener pingListener;

    public StartaiTimerPingSender(PingListener pingListener) {
        this.pingListener = pingListener;
    }

    private static final String CLASS_NAME = StartaiTimerPingSender.class.getName();
//    private static final Logger log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);

    private ClientComms comms;
    private Timer timer;

    public void init(ClientComms comms) {
        if (comms == null) {
            throw new IllegalArgumentException("ClientComms cannot be null.");
        }
        this.comms = comms;
    }

    public void start() {
        final String methodName = "start";
        String clientid = comms.getClient().getClientId();

        //@Trace 659=start timer for client:{0}
//        log.fine(CLASS_NAME, methodName, "659", new Object[]{clientid});

        timer = new Timer("MQTT Ping: " + clientid);
        //Check ping after first keep alive interval.
        timer.schedule(new PingTask(), comms.getKeepAlive());
    }

    public void stop() {
        final String methodName = "stop";
        //@Trace 661=stop
//        log.fine(CLASS_NAME, methodName, "661", null);
        if (timer != null) {
            timer.cancel();
        }
    }

    public void schedule(long delayInMilliseconds) {
        timer.schedule(new PingTask(), delayInMilliseconds);
    }

    private class PingTask extends TimerTask {
        private static final String methodName = "PingTask.run";

        public void run() {
            //@Trace 660=Check schedule at {0}
//            log.fine(CLASS_NAME, methodName, "660", new Object[]{new Long(System.currentTimeMillis())});
            pingListener.onHeartStart();
            comms.checkForActivity(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    pingListener.onHeartSuccess();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    pingListener.onHeartFailed(exception);
                }
            });
        }
    }

    public static interface PingListener {

        void onHeartStart();

        void onHeartSuccess();

        void onHeartFailed(Throwable e);

        void onReset();

    }

}
