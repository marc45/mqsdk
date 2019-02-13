
package cn.com.startai.mqttsdk.listener;


import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.internal.ClientComms;

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


    private ClientComms comms;
    private Timer timer;

    public void init(ClientComms comms) {
        if (comms == null) {
            throw new IllegalArgumentException("ClientComms cannot be null.");
        }
        this.comms = comms;
    }


    public void start() {
        String clientid = comms.getClient().getClientId();


        timer = new Timer("MQTT Ping: " + clientid);
        //Check ping after first keep alive interval.
        timer.schedule(new PingTask(), comms.getKeepAlive());
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void schedule(long delayInMilliseconds) {
        timer.schedule(new PingTask(), delayInMilliseconds);
    }

    private class PingTask extends TimerTask {

        public void run() {

            if (pingListener != null && comms != null) {

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
    }

    public static interface PingListener {

        void onHeartStart();

        void onHeartSuccess();

        void onHeartFailed(Throwable e);

        void onReset();

    }

}
