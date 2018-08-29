package cn.com.startai.mqsdk.util;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.net.InetAddress;

import cn.com.startai.udpbroadcast.discovery.Discovery;
import cn.com.startai.udpbroadcast.discovery.DiscoveryException;
import cn.com.startai.udpbroadcast.discovery.DiscoveryListener;

/**
 * airkiss 配网辅助
 * 这个类是为了解决，在airkiss或esptouch配网时在路由器通信质量不好的情况下，最后一次握手容易丢包的问题
 * 此方案是尽可能的弥补丢包的情况
 * Created by Robin on 2018/8/2.
 * qq: 419109715 彬影
 */

public class AirkissHelper {

    private static String TAG = AirkissHelper.class.getSimpleName();
    private static final AirkissHelper ourInstance = new AirkissHelper();
    private static Handler mHandler;

    private int port1 = 26743;
    private int port2 = 18378;
    private AirkissHelperListener listener;
    private Discovery discovery1;
    private Discovery discovery2;
    private long timerout;


    public static AirkissHelper getInstance() {
        HandlerThread ht = new HandlerThread(TAG);
        ht.start();
        mHandler = new Handler(ht.getLooper());
        return ourInstance;
    }

    private AirkissHelper() {

    }


    public void start(long timeout, AirkissHelperListener listener) {
        this.listener = listener;
        this.timerout = timeout;
        initDiscovery();
    }

    public void stop() {

        mHandler.removeCallbacksAndMessages(null);
        unInitDiscovery();

    }

    private void unInitDiscovery() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (discovery1 != null) {
                    discovery1.disable();
                    discovery1 = null;
                    Log.d(TAG, "unInitDiscovery 1");
                }

                if (discovery2 != null) {
                    discovery2.disable();
                    discovery2 = null;
                    Log.d(TAG, "unInitDiscovery 2");
                }
            }
        });

    }


    private void initDiscovery() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.post(new Runnable() {
            @Override
            public void run() {


                if (discovery1 == null) {
                    discovery1 = new Discovery("", port1);
                    discovery1.setBroadcast(false);
                    discovery1.setDisoveryListener(discoveryListener);
                    try {
                        discovery1.enable();
                        Log.d(TAG, "discovery1.enable();");
                    } catch (DiscoveryException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "no need to repeat enable discovery");
                }

                if (discovery2 == null) {

                    discovery2 = new Discovery("", port2);
                    discovery2.setBroadcast(false);
                    discovery2.setDisoveryListener(discoveryListener);
                    try {
                        discovery2.enable();
                        Log.d(TAG, "discovery2.enable();");
                    } catch (DiscoveryException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d(TAG, "no need to repeat enable discovery");
                }
            }
        });

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                unInitDiscovery();
            }
        }, timerout);
    }


    DiscoveryListener discoveryListener = new DiscoveryListener() {
        @Override
        public void onDiscoveryStarted() {

        }

        @Override
        public void onDiscoveryStopped() {

        }

        @Override
        public void onDiscoveryError(Exception e) {

        }

        @Override
        public void onIntentDiscovered(InetAddress inetAddress, Intent intent) {

        }

        @Override
        public void onStringDiscovered(InetAddress inetAddress, String s) {

            Log.d(TAG, "onStringDiscovered = " + s);
            if ("discover".equalsIgnoreCase(s)) {
                listener.onAirkissSuccess(inetAddress);

                unInitDiscovery();
            }

        }
    };


    public static interface AirkissHelperListener {
        void onAirkissSuccess(InetAddress inetAddress);
    }


}
