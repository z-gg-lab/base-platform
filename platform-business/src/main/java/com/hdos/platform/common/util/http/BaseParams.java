package com.hdos.platform.common.util.http;

public class BaseParams {
	
	/**
	 * 连接超时
	 */
	public static final int  CONNECT_TIMEOUT = 8000;
	
	/**
	 * 读取超时
	 */
	public static final int READ_TIMEOUT = 1000000;
	
	/**
	 * outlink的url
	 */
	public static final String OUTLINK_URL = "http://192.168.2.108:8080/railway-outlink";
	
	/**	
	 * 调用dll加密
	 */
	public static final String SEALMESSAGE_URL = "/gsm/sealMessage";
	

	/**	
	 * 调用dll解密
	 */
	public static final String UNSEALMESSAGE_URL = "/gsm/unSealMessage";
}
