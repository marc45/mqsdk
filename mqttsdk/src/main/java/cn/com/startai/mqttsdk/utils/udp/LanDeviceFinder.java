package cn.com.startai.mqttsdk.utils.udp;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TimerTask;

import cn.com.startai.mqttsdk.utils.SLog;
import cn.com.startai.mqttsdk.utils.STimerUtil;

/**
 * Created by Robin on 2018/8/11.
 * qq: 419109715 彬影
 */

public class LanDeviceFinder {

    private static final int MAXIMUM_PACKET_BYTES = 102400;
    private static final LanDeviceFinder ourInstance = new LanDeviceFinder();
    private Handler mHandler;
    private HandlerThread ht;
    private boolean running = true;
    private DatagramSocket socket;
    private static final String IP = "255.255.255.255";
    private String pwd;

    public static LanDeviceFinder getInstance() {
        return ourInstance;
    }

    private static final String TAG = LanDeviceFinder.class.getSimpleName();
    private long timeout;
    private static int PORT = 9222;
    private static int LOCALPORT = 9221;

    private IDeviceFindListener listener;
    private String userid;

    private LanDeviceFinder() {
    }

    byte index = 0;

    private static HashMap<String, LanDevice> map = new HashMap<>();

    public void find(String userid, String pwd, long timeout, final IDeviceFindListener listener) {
        this.userid = userid;
        this.pwd = pwd;
        this.listener = listener;
        this.timeout = timeout;
        map.clear();
        userid = "7c38f25fd86f4b989bcbfa4ce2e6b7e5";
//        final String userid = UUID.randomUUID().toString().replace("-", "");

        if (timeout > 60 * 1000) {
            timeout = 60 * 1000;
        }
        if (timeout < 1000) {
            timeout = 1000;
        }

        if (mHandler == null) {
            ht = new HandlerThread(TAG);
            ht.start();
            mHandler = new Handler(ht.getLooper());
        }

        initUdpSocket("", LOCALPORT);
        initUdpRecv();
        toFindDevice();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stop();
                if (map == null || map.size() == 0) {
                    listener.onTimeout();
                }
            }
        }, timeout);

    }

    private void initUdpSocket(final String ip, final int port) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!TextUtils.isEmpty(ip) && port >= 0) {

                        InetAddress addr = null;
                        addr = InetAddress.getByName(ip);
                        socket = new DatagramSocket(port, addr);
                    } else if (TextUtils.isEmpty(ip) && port >= 0) {
                        socket = new DatagramSocket(port);
                    } else {
                        socket = new DatagramSocket();
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                    listener.onException(e);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    listener.onException(e);
                }

            }
        });

    }

    private void toFindDevice() {
        running = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                STimerUtil.schedule(TAG, new TimerTask() {
                    @Override
                    public void run() {
                        try {

                            index++;
                            DeviceFindReq req = new DeviceFindReq(index, (byte) 0x00, (byte) 0x02, userid);
                            byte[] byteArr = req.getByteArr();
                            //发送广播包

                            DatagramPacket packet = new DatagramPacket(
                                    byteArr,
                                    byteArr.length,
                                    InetAddress.getByName(IP),
                                    PORT
                            );
                            socket.setBroadcast(true);
                            socket.send(packet);
                            SLog.d(TAG, "发送发现包 = " + Arrays.toString(byteArr));

                        } catch (SocketException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 3000);
            }
        });

    }

    private void toBindDevice(final String ip, final int port) {
        if (mHandler != null) {

            mHandler.post(new Runnable() {
                @Override
                public void run() {


                    try {

                        index++;
                        DeviceBindReq req = new DeviceBindReq(index, (byte) 0x00, (byte) 0x02, userid, pwd);
                        byte[] byteArr = req.getByteArr();
                        //发送广播包

                        DatagramPacket packet = new DatagramPacket(
                                byteArr,
                                byteArr.length,
                                InetAddress.getByName(ip),
                                port
                        );

                        socket.setBroadcast(true);
                        socket.send(packet);
                        SLog.d(TAG, "发送虚拟绑定包 = " + Arrays.toString(byteArr));

                    } catch (SocketException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //[-1, 0, 42, 1, 13, 0, 0, 0, 0, 0, 2, 1, 3, 55, 99, 51, 56, 102, 50, 53, 102, 100, 56, 54, 102, 52, 98, 57, 56, 57, 98, 99, 98, 102, 97, 52, 99, 101, 50, 101, 54, 98, 55, 101, 53, 28, -18]
    //[ff, 0, 2a, 1, d, 0, 0, 0, 0, 0, 2, 1, 3, 37, 63, 33, 38, 66, 32, 35, 66, 64, 38, 36, 66, 34, 62, 39, 38, 39, 62, 63, 62, 66, 61, 34, 63, 65, 32, 65, 36, 62, 37, 65, 35, 1c, ee]

    private void initUdpRecv() {

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        while (running) {
                            DatagramPacket packet = new DatagramPacket(new byte[MAXIMUM_PACKET_BYTES], MAXIMUM_PACKET_BYTES);

                            String intentUri = "";
                            try {
                                socket.setBroadcast(true);
                                socket.receive(packet);

                                String hostName = packet.getAddress().getHostName();

                                byte[] data = packet.getData();
                                int length = packet.getLength();
                                byte[] d = new byte[length];
                                System.arraycopy(data, 0, d, 0, length);
                                SLog.d(TAG, "接收到广播包 " + Arrays.toString(d));
                                SLog.d(TAG, "host = " + hostName + "长度 " + length);

                                //开始解析协议
                                LanDevice parseDevice = new ProtocolParse().parse(d);
                                if (parseDevice != null) {

                                    if (TextUtils.isEmpty(parseDevice.getSn())) {

                                        LanDevice lanDevice = map.get(parseDevice.getMac());
                                        if (lanDevice != null && !TextUtils.isEmpty(lanDevice.getSn())) {
                                            SLog.d(TAG, "已经发现过此设备，不再回调到应用层");
                                            continue;
                                        }

                                        parseDevice.setIp(hostName);
                                        parseDevice.setPort(PORT);
                                        parseDevice.setMac(parseDevice.getMac());

                                        map.put(parseDevice.getMac(), parseDevice);

                                        toBindDevice(hostName, PORT);

                                    } else {

                                        LanDevice lan = map.get(parseDevice.getMac());
                                        lan.setSn(parseDevice.getSn());
                                        lan.setCpuId(parseDevice.getCpuId());
                                        listener.onDeviceFind(lan);
                                    }
                                }
                            } catch (SocketException exception) {
                                exception.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }.start();


            }
        });


    }

    public void stop() {
        index = 0;
        running = false;
        STimerUtil.close(TAG);
        if (mHandler != null) {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (ht != null) {

                        socket.close();
                        mHandler = null;
                        ht.quit();

                        listener.onStop();
                    }
                }
            });

        }

    }


    public interface IDeviceFindListener {
        void onDeviceFind(LanDevice devices);

        void onTimeout();

        void onException(Exception e);

        void onStop();
    }

}
