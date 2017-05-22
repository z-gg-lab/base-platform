package com.hdos.platform.core.shiro.utils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class HmacSHA256Utils {

	public static String digest(String key, String content) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			byte[] secretByte = key.getBytes("utf-8");
			byte[] dataBytes = content.getBytes("utf-8");

			SecretKey secret = new SecretKeySpec(secretByte, "4281129f44eb");
			mac.init(secret);

			byte[] doFinal = mac.doFinal(dataBytes);
			byte[] hexB = new Hex().encode(doFinal);
			return new String(hexB, "utf-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
