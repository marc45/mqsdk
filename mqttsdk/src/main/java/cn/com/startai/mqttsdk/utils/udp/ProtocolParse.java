package cn.com.startai.mqttsdk.utils.udp;

import cn.com.startai.mqttsdk.utils.SLog;
import cn.com.startai.mqttsdk.utils.SStringUtils;

/**
 * Created by Robin on 2018/8/11.
 * qq: 419109715 彬影
 */

public class ProtocolParse {

    private String TAG = ProtocolParse.class.getSimpleName();


    public LanDevice parse(byte[] bytes) {

        LanDevice resp = null;
        int len = bytes.length;

        byte header = bytes[0];
        byte len_h = bytes[1];
        byte len_l = bytes[2];
        byte version = bytes[3];
        byte sequence = bytes[4];
        byte reserve4 = bytes[5];
        byte reserve3 = bytes[6];
        byte reserve2 = bytes[7];
        byte reserve1 = bytes[8];
        byte custom_h = bytes[9];
        byte custom_l = bytes[10];
        byte type = bytes[11];
        byte cmd = bytes[12];
        byte result = bytes[13];

        if (cmd == 4) {

            byte devtype = bytes[14];
            byte[] macArr = new byte[6];
            System.arraycopy(bytes, 15, macArr, 0, 6);
            String mac = SStringUtils.byteArr2HexStr(macArr);
            String macStr = "";
            for (int i = 0; i < mac.length(); i++) {
                macStr += mac.charAt(i);
                if (i == mac.length() - 1) {
                    break;
                }
                if (i % 2 != 0) {
                    macStr += ":";
                }

            }


            byte[] devnameArr = new byte[32];
            System.arraycopy(bytes, 21, devnameArr, 0, 32);
            String devname1 = SStringUtils.byteArr2HexStr(devnameArr);
            String devname2 = new String(devnameArr, 0, devname1.replace("00", "").length() / 2);


            byte mainVersion = bytes[53];
            byte subVersion = bytes[54];
            byte pushLanguageType = bytes[55];
            byte bindStatus = bytes[56];
            byte remoteStatus = bytes[57];
            byte rssi = bytes[58];
            byte CRC8 = bytes[59];
            byte tail = bytes[60];

            String s = Integer.toBinaryString(remoteStatus);
            int isActivate = 0;
            int isConnected = 0;
            for (int i = 0; i < s.length(); i++) {
                isActivate = Integer.parseInt(s.charAt(0) + "");
                isConnected = Integer.parseInt(s.charAt(1) + "");
            }

            SLog.d(TAG, " macStr = " + macStr + " devName2 = " + devname2 + " isActivate = " + isActivate + " idConnected = " + isConnected);

            resp = new LanDevice();
            resp.setMac(macStr);
            resp.setName(devname2);
            resp.setRssi(rssi);
            resp.setDevConnStatus(isConnected);
            resp.setDevActivateStatus(isActivate);
            return resp;
        } else if (cmd == 6) {

//            if (result == 0) {


            byte[] snArr = new byte[32];
            System.arraycopy(bytes, 15, snArr, 0, 32);
            String snStr = new String(snArr, 0, snArr.length);


            byte[] macArr = new byte[6];
            System.arraycopy(bytes, 15 + snArr.length + 32, macArr, 0, 6);
            String mac = SStringUtils.byteArr2HexStr(macArr);
            String macStr = "";
            for (int i = 0; i < mac.length(); i++) {
                macStr += mac.charAt(i);
                if (i == mac.length() - 1) {
                    break;
                }
                if (i % 2 != 0) {
                    macStr += ":";
                }

            }


            byte[] cupidArr = new byte[4];
            System.arraycopy(bytes, 15 + snArr.length + snArr.length + 6, cupidArr, 0, 4);
            String cpuid1 = SStringUtils.byteArr2HexStr(cupidArr).replace("00", "");


            SLog.d(TAG, "macStr = " + macStr + " snStr = " + snStr + "cpuid = " + cpuid1 + "macStr = " + macStr);
            resp = new LanDevice();
            resp.setCpuId(cpuid1);
            resp.setSn(snStr);
            resp.setMac(macStr);
            return resp;
//            } else {
//                SLog.d(TAG, "虚拟绑定失败");
//                return null;
//            }
        } else {
            SLog.d(TAG, "其他广播包，不做处理");
            return null;
        }

    }
}
