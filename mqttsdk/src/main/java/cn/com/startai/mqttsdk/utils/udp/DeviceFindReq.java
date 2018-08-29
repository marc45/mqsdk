package cn.com.startai.mqttsdk.utils.udp;


import cn.com.startai.mqttsdk.utils.CrcUtil;

/**
 * Created by Robin on 2018/8/11. qq: 419109715 彬影
 */

public class DeviceFindReq {

	private byte header = (byte) 0xFF;
	private byte len_h;
	private byte len_l;
	private byte version = 01;
	private byte sequence;
	private byte reserve4;
	private byte reserve3;
	private byte reserve2;
	private byte reserve1;
	private byte custom_h;
	private byte custom_l;
	private byte type = 01;
	private byte cmd = 03;
	private String userid;
	private byte CRC8;
	private byte tail = (byte) 0xee;

	public DeviceFindReq(byte sequence, byte custom_h, byte custom_l,
			String userid) {
		this.sequence = sequence;
		this.custom_h = custom_h;
		this.custom_l = custom_l;
		this.userid = userid;
	}

	public byte[] getByteArr() {
		len_h = (byte) 0x00;
		len_l = (byte) 0x2a;

		byte[] useridbyte = userid.getBytes();

		byte[] b = new byte[47];
		b[0] = header;
		b[1] = len_h;
		b[2] = len_l;
		b[3] = version;
		b[4] = sequence;
		b[5] = reserve4;
		b[6] = reserve3;
		b[7] = reserve2;
		b[8] = reserve1;
		b[9] = custom_h;
		b[10] = custom_l;
		b[11] = type;
		b[12] = cmd;
		b[46] = tail;

		System.arraycopy(useridbyte, 0, b, 13, useridbyte.length);
		CRC8 = CrcUtil.CRC8(b, 3, b.length - 2);
		b[45] = CRC8;

		return b;
	}
}
