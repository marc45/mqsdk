package cn.com.startai.mqttsdk.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.control.SPController;
import cn.com.startai.mqttsdk.mqtt.MqttConfigure;

/**
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class DeviceInfoManager {
    private static final DeviceInfoManager ourInstance = new DeviceInfoManager();
    private static String TAG = DeviceInfoManager.class.getSimpleName();


    public static DeviceInfoManager getInstance() {
        return ourInstance;
    }

    private DeviceInfoManager() {
    }


    public String getImei(Context context) {
//        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//        telephonyManager.getDeviceId();
        return "";
    }

    /**
     * 获取屏幕尺寸
     *
     * @return
     */
    public String getScreenSize(Context context) {

        return getScreenWidth(context) + "*" + getScreenHeight(context);

    }

    @SuppressLint("HardwareIds")
    public String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getBluetoothMac() {
        return "";
    }

    public String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 固件版本
     *
     * @return
     */
    public String getFirmwareVersion() {
        return Build.DISPLAY;
    }

    /**
     * 系统版本
     *
     * @return
     */
    public String getSysVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * Return the width of screen, in pixel.
     *
     * @return the width of screen, in pixel
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return context.getResources().getDisplayMetrics().widthPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    /**
     * Return the height of screen, in pixel.
     *
     * @return the height of screen, in pixel
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    /**
     * SN生成,MD5加密
     */
    public String getSn(Context context) {
        String serial = getCpuSerial();//cpu序列号，有可能为全0
        String mac = getMAC(context);//先获取以太网mac  如果没有则使用wifimac 如果再没有 就为空
        if ((serial.contains("0000") || TextUtils.isEmpty(serial)) && TextUtils.isEmpty(mac)) {
            return "";
        }

        if (ProductConsts.isAdaptation()) { //兼容老设备
            if (ProductConsts.is_wing_mbox203()) {
                //如果是A20就用cpu 序列号作为sn
                if (!serial.contains("00000000") && !TextUtils.isEmpty(serial)) {
                    return serial;
                }
            }

            String wholeStr = "";
            if (ProductConsts.is_fiber_a31st()) { //如果是a31的机器
                wholeStr = "11e6-7359-f51a8c58-b892-2fe2325b4c74" + mac;
            } else {

                if (mac.equals("02:00:00:00:00:00")) {
                    wholeStr = getAndroidId(context) + serial + mac;
                } else {
                    wholeStr = serial + mac;
                }

            }

            String tempStr = null;
            try {
                tempStr = URLEncoder.encode(wholeStr, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String sn = getSn(tempStr);

            return sn;
        } else {
            String sn = getSn_16(getAndroidId(context) + serial + mac);
            return sn;
        }


    }

    public String getSn(String str) {
        return SMD5.generateMD5(str);
    }

    public String getSn_16(String str) {
        String sn = getSn(str);
        if (sn.length() == 32) {
            sn = sn.substring(8, 24);
        }
        return sn;
    }

    /**
     * 获取cpu序列号
     *
     * @return
     */
    public String getCpuSerial() {
        String str = "", strCPU = "", cpuAddress = "0000000000000000";
        try {
            //读取CPU信息
            Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            //查找CPU序列号
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    //查找到序列号所在行
                    if (str.contains("Serial")) {
                        //提取序列号
                        strCPU = str.substring(str.indexOf(":") + 1,
                                str.length());
                        //去空格
                        cpuAddress = strCPU.trim();
                        break;
                    }
                } else {
                    //文件结尾
                    break;
                }
            }
        } catch (Exception ex) {
            //赋予默认值
            ex.printStackTrace();
        }
        return cpuAddress;
    }

    /**
     * 上报数据：MAC地址
     */
    public String getMAC(Context context) {
        //先获取以太网mac  如果没有则使用wifimac 如果再没有 就为空
        String mac = getInetMac();
        if (TextUtils.isEmpty(mac)) {
            //获取wifimac
            mac = getWifiMac(context);
            if (!TextUtils.isEmpty(mac)) {
                Log.i("info", "MAC地址为 ：" + mac);
                return mac;
            } else {
                Log.i("info", "MAC地址为 ：" + mac);
                return "";
            }
        } else {
            Log.i("info", "MAC地址为 ：" + mac);
            return mac;
        }

    }

    /**
     * 获取以太网mac
     *
     * @return
     */
    public String getInetMac() {
        try {
            return loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
        } catch (IOException var1) {
//            Log.w(TAG, "can not find inter Mac : /sys/class/net/eth0/address");
            return "";
        }
    }


    private static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取wifimac
     *
     * @return
     */
    public String getWifiMac(Context context) {

        String wifiMac = "";
        WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null) {

            WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
            if (mWifiInfo != null) {

                wifiMac = mWifiInfo.getMacAddress();

            } else {
                wifiMac = "";
            }
        } else {
            wifiMac = "";
        }

        if (TextUtils.isEmpty(wifiMac) || wifiMac.equals("02:00:00:00:00:00")) {
            wifiMac = getMacAddr();
        }

        return wifiMac;
    }

    private String loadFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        boolean numRead = false;

        int numRead1;
        while ((numRead1 = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead1);
            fileData.append(readData);
        }

        reader.close();
        return fileData.toString();
    }

}
