package cn.com.startai.mqttsdk.utils;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/30 0030
 * desc :
 */

public class CrcUtil {

    public static byte CRC8(byte[] buffer) {
        return CRC8(buffer, 0, buffer.length);
    }

    public static byte CRC8(byte[] buffer, int start, int end) {
        int crc = 0x00;   //起始字节00
        for (int j = start; j < end; j++) {
            crc ^= buffer[j] & 0xFF;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x01) != 0) {
                    crc = (crc >> 1) ^ 0x8c;
                } else {
                    crc >>= 1;
                }
            }
        }
        return (byte) (crc & 0xFF);
    }

}
