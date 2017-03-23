package com.hdos.platform.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Memory;
import com.sun.jna.ptr.IntByReference;

/**
 * 加密机调用工具类
 * 
 * @author TAIL
 *
 */
public class GSMUtils {

	private static GSM_API gsm_API = GSM_API.instance;

	private final static Logger logger = LoggerFactory.getLogger(GSMUtils.class);

	/**
	 * 加密
	 * 
	 * @param ID1
	 *            本方ID
	 * @param ID2
	 *            接收方ID
	 * @param ucDataIn
	 * @return
	 */
	public static String GSM_SealMessage(int ID1, int ID2, String ucDataIn) {

		IntByReference intByReference = new IntByReference();
		Memory memory = new Memory(ucDataIn.length() * 1024);

		int status = gsm_API.GSM_SealMessage(ID1, ID2, ucDataIn, ucDataIn.length(), memory, intByReference);
		if (status != 0) {
			logger.error("加密错误，错误码" + status);
			throw new RuntimeException();
		}
		byte[] afterEncryptByteArray = new byte[intByReference.getValue()];
		memory.read(0, afterEncryptByteArray, 0, intByReference.getValue());
		return Base64.encodeBase64String(afterEncryptByteArray);
	}

	/**
	 * 解密
	 * 
	 * @param ID1
	 * @param ID2
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String GSM_UnSealMessage(int ID1, int ID2, String message) {
		String result = null;
		// 如果为空返回空字符串
		if (StringUtils.isBlank(message)) {
			return result;
		}
		try {
			byte[] ucDataIn = Base64.decodeBase64(message);
			// 接收解密的message
			Memory ucDataOut = new Memory(ucDataIn.length);
			IntByReference uiOutDataLen = new IntByReference();
			int driverDllInt = gsm_API.GSM_UnSealMessage(ID1, ID2, ucDataIn, ucDataIn.length, ucDataOut, uiOutDataLen);

			if (driverDllInt != 0) {
				String errMsg = "解密错误!错误码：" + driverDllInt;
				logger.error(errMsg);
				throw new Exception(errMsg);
			}
			result = ucDataOut.getString(0);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {

		}
		return result;

	}

	/**
	 * 连接加密机
	 * 
	 * @throws Exception
	 */
	public static void GSM_Connect(IntByReference uiSockfd) throws Exception {
		logger.debug("连接加密机");
		// 返回的结果
		int driverDllInt = gsm_API.GSM_KGC_Connect("10.11.72.3", 9878, uiSockfd);
		if (driverDllInt != 0) {
			String errMsg = "加密机连接错误!错误码：" + driverDllInt;
			logger.error(errMsg);
		}

		logger.debug("连接加密机成功，套接字：" + uiSockfd);
	}

	/**
	 * 释放加密机连接
	 * 
	 * @throws Exception
	 */
	public static void GSM_Release_Connect(IntByReference uiSockfd) throws Exception {

		int driverDllInt = gsm_API.GSM_Release_Connect(uiSockfd);
		if (driverDllInt != 0) {
			String errMsg = "释放加密机连接错误!错误码：" + driverDllInt;
			logger.error(errMsg);
			throw new Exception(errMsg);
		}
		logger.debug("释放加密机连接成功");
	}

	/**
	 * 获取A4Ki
	 * 
	 * @param ID1
	 * @param ID2
	 * @param uiKiNum
	 * @param SN1
	 * @param pstRequestData
	 * @param uiK4Index
	 */
	public static String GSM_GetA4Ki(int ID1, int ID2, int uiKiNum, int SN1, int uiK4Index) {

		String result = null;
		try {
			Memory pstRequestData = new Memory(1024 * 100);
			IntByReference uiSockfd = new IntByReference();
			// 建立socket连接
			GSM_Connect(uiSockfd);
			// 初始化加密机
			gsm_API.GSM_GetKi_Init(ID1, ID2, uiKiNum, SN1, pstRequestData);

			Memory ucA4Ki = new Memory(1024 * 1024);

			IntByReference uiSN1 = new IntByReference();

			int status = gsm_API.GSM_GetA4Ki(uiSockfd.getValue(), pstRequestData, uiK4Index, ucA4Ki, uiSN1);
			if (status != 0) {
				String errMsg = "获取A4Ki失败，错误码：" + result;
				logger.error(errMsg);
				throw new Exception(errMsg);
			}

			byte[] fterEncryptByteArray = new byte[400 * 20];
			ucA4Ki.read(0, fterEncryptByteArray, 0, 400 * 20);
			byte[] resultArray = new byte[20];
			for (int i = 0; i < resultArray.length; i++) {
				resultArray[i] = fterEncryptByteArray[i];
			}
			result = bytesToHexString(resultArray);
			// 关闭socket连接
			GSM_Release_Connect(uiSockfd);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}

		return result;

	}

	/**
	 * 获取KI
	 * 
	 * @param ID1
	 * @param ID2
	 * @param uiKiNum
	 * @param SN1
	 * @param pstRequestData
	 * @param uiK4Index
	 */
	public static String GSM_GetKi(String Eki, int A4KeyID, String KIUserPWD) {
		String result = null;
		try {
			byte[] ucKi = new byte[20];

			int status = gsm_API.GSM_GetKi(A4KeyID, hexStringToByte(Eki), 1, KIUserPWD, KIUserPWD.length(), ucKi);

			if (status != 0) {
				result = "解密Eki错误，错误码：" + status;
				logger.error(result);
				throw new Exception(result);
			}

			result = bytesToHexString(ucKi);

			byte[] Key11 = new byte[32];
			byte[] Key22 = new byte[8];

			Key11 = hexStringToByte(result.substring(0, 32));
			Key22 = hexStringToByte(result.substring(32));
			int flag = GSM_API.instance.check_ckv(Key11, Key22);

			if (flag != 0) {
				String errMsg = "验证ki错误，错误码：" + status;
				logger.error(errMsg);
				throw new Exception(errMsg);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}

		return result;

	}

	/**
	 * 把字节数组转换成16进制字符串
	 * 
	 * @param bArray
	 * @return
	 */
	private static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 把16进制字符串转换成字节数组
	 * 
	 * @param hex
	 * @return
	 */
	private static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

}
