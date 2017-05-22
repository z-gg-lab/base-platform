package com.hdos.platform.common.util;

/**
 * 系统常量管理
 * 
 * @author zm
 *
 */
public class ConfigContants {
	/**
	 * 是否开启打印地址的模式
	 */
	public static final String IS_DEBUG = "IS_DEBUG";
	/**
	 * 机构类型
	 */
	public static final String DEPARTMENT_TYPE = "DEPARTMENT_TYPE";
	/**
	 * 密码错误次数
	 */
	public static final int FAILURETHREHOLDWITHOUTCAPTCHA = 5;
	/**
	 * 是否允许发送短信
	 */
	public static final String SMS_ALLOWED = "SMS_ALLOWED";
	/**
	 * AppID
	 */
	public static final String APP_ID = "YTX_APP_ID";
	/**
	 * ACCOUNT_SID
	 */
	public static final String ACCOUNT_SID = "YTX_ACCOUNT_SID";
	/**
	 * AUTH_TOKEN
	 */
	public static final String AUTH_TOKEN = "YTX_AUTH_TOKEN";
	
	/**
	 * 文件上传存储方式
	 * 上传模式 ：local本地、ftp服务器 
	 */
	public static final String FILE_UPLOAD_STORAGE = "FILE_UPLOAD_STORAGE";
	
	/**
	 * 本地文件存储路径
	 */
	public static final String LOCAL_FILE_PATH = "LOCAL_FILE_PATH";
	
	/**
	 * FTP文件存储路径
	 */
	
	public static final String FTP_UPLOAD_PATH = "FTP_UPLOAD_PATH";
	
	/**
	 * FTP服务器IP
	 */
	public static final String FTP_IP = "FTP_IP";

	/**
	 * FTP服务器端口
	 */
	public static final String FTP_PORT = "FTP_PORT";
	
	/**
	 * FTP服务器账号
	 */
	public static final String FTP_ACCOUNT = "FTP_ACCOUNT";
	
	/**
	 * FTP服务器密码
	 */
	public static final String FTP_PASSWD = "FTP_PASSWD";
	
	/**
	 * 每分钟发送的短信次数限制
	 */
	public static final String SMS_COUNT = "SMS_COUNT";
	
	/**
	 * 超过短信次数限制的接收短信的安全号码
	 */
	public static final String SMS_SAFE_MOBILE = "SMS_SAFE_MOBILE";	
	
	/**
	 * 系统缓存超时时间
	 */
	public static final long CACHE_TIMEOUT = 300000;
	
}
