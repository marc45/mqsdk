package cn.com.startai.mqttsdk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SMD5 {

	private SMD5() {

	}

	private static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String getFileMD5(File file) {

		MessageDigest md5;
		try {
			InputStream ins = new FileInputStream(file);

			byte[] buffer = new byte[8192];
			md5 = MessageDigest.getInstance("MD5");

			int len;
			while ((len = ins.read(buffer)) != -1) {
				md5.update(buffer, 0, len);
			}

			ins.close();
			// 也可以用apache自带的计算MD5方法
			// return DigestUtils.md5Hex(md5.digest());
			// 自己写的转计算MD5方法
			return toHexString(md5.digest());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}



	public static String generateMD5(String inputStr) {
		return encodeByMD5(inputStr).toUpperCase();
	}

	/**
	 * 对字符串进行MD5加密
	 */
	private static String encodeByMD5(String originStr) {
		if (originStr != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] results = md.digest(originStr.getBytes());
				String resultStr = byteArrayToHexString(results);
				// return resultStr.substring(8, 24);
				return resultStr;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 转换字节数组为十六进制字符串
	 * 
	 * @return 十六进制字符
	 */
	private static String byteArrayToHexString(byte[] bytes) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			resultSb.append(byteToHexString(bytes[i]));
		}
		return resultSb.toString();
	}

	/**
	 * 将一个字节转化成十六进制形式的字符串
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;

		return hexChar[d1] + "" + hexChar[d2];
	}

	protected static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/*
	 * 获取MessageDigest支持几种加密算法
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static String[] getCryptolmpls(String serviceType) {

		Set result = new HashSet();
		// all providers
		Provider[] providers = Security.getProviders();
		for (int i = 0; i < providers.length; i++) {
			// get services provided by each provider
			Set keys = providers[i].keySet();
			for (Iterator it = keys.iterator(); it.hasNext();) {
				String key = it.next().toString();
				key = key.split(" ")[0];

				if (key.startsWith(serviceType + ".")) {
					result.add(key.substring(serviceType.length() + 1));
				} else if (key.startsWith("Alg.Alias." + serviceType + ".")) {
					result.add(key.substring(serviceType.length() + 11));
				}
			}
		}
		return (String[]) result.toArray(new String[result.size()]);
	}

}