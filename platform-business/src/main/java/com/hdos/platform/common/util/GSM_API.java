package com.hdos.platform.common.util;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

/**
 * 加密机接口 1.需要继承Library接口 2.加载动态库 3.c和Java的数据类型类型需要对应
 * 
 * @author Administrator
 *
 */
public interface GSM_API extends Library {

	GSM_API instance = (GSM_API) Native.loadLibrary("GSM_API", GSM_API.class);

	/**
	 * 封装接口：该接口将输入文件标识符指向的文件内容传输至本地加密机，通过数字信封封装成安全数据格式并写入输出文件
	 * 
	 * @param ID1
	 *            本方ID
	 * @param ID2
	 *            接收方ID
	 * @param ucDataIn
	 *            输入数据指针
	 * @param uiDataInLen
	 *            输入数据长度
	 * @param ucDataOut
	 *            输出数据指针
	 * @param uiOutDataLen
	 *            输出数据长度
	 * @return
	 */
	public abstract int GSM_SealMessage(int ID1, int ID2, String ucDataIn, int uiDataInLen, Memory ucDataOut,
			IntByReference uiOutDataLen);

	/**
	 * 解封装接口：该接口将安全数据文件标识符指向的文件内容传输至本地加密机，解封装数字信封恢复原有数据，并写入输出文件中
	 * 
	 * @param ID1
	 *            本方ID
	 * @param ID2
	 *            接收方ID
	 * @param ucDataIn
	 *            输入数据指针
	 * @param uiDataInLen
	 *            输入数据长度
	 * @param ucDataOut
	 *            输出数据指针
	 * @param uiOutDataLen
	 *            输出数据长度
	 * @return
	 */
	public abstract int GSM_UnSealMessage(int ID1, int ID2, byte[] ucDataIn, int uiDataInLen, Memory ucDataOut,
			IntByReference uiOutDataLen);

	/**
	 * 建立Socket连接接口：该接口实现制卡系统与密钥产生系统之间Socket通信连接的建立，在后续制卡系统需要向密钥产生系统申请Ki密钥的时候，
	 * 可以使用该接口返回的Socket套接字进行通信
	 * 
	 * @param ucIPAddr
	 * @param uiPort
	 * @param uiSockfd
	 * @return
	 */
	public abstract int GSM_KGC_Connect(String ucIPAddr, int uiPort, IntByReference uiSockfd);

	/**
	 * 释放Socket连接接口 该接口释放制卡系统与密钥产生系统的Socket连接。
	 * 
	 * @param uiSockfd
	 * @return
	 */
	public abstract int GSM_Release_Connect(IntByReference uiSockfd);

	/**
	 * 制卡系统初始化申请请求接口：该接口接收制卡系统传入的制卡请求数据、一级密钥分发协议相关参数，通过调用本地加密机对以上数据进行签名，
	 * 整理并输出密钥申请请求数据包，用于下一步向密钥产生系统发起申请
	 * 
	 * @param ID1
	 * @param ID2
	 * @param uiKiNum
	 * @param SN1
	 * @return
	 */
	public abstract int GSM_GetKi_Init(int ID1, int ID2, int uiKiNum, int SN1, Memory pstRequestData);

	/**
	 * 制卡系统发送申请请求接口：该接口将密钥申请请求结构体发送到密钥产生系统并获取响应包，接到响应包后制卡系统调用本地密码机对响应包内数据进行验证和解析
	 * ，通过调用存储在本地密码机的K4密钥进行加密，获得A4Ki密钥组
	 * 
	 * @param uiSockfd
	 * @param memory
	 * @param uiK4Index
	 * @param ucA4Ki
	 * @param uiSN1
	 * @return
	 */
	public abstract int GSM_GetA4Ki(int uiSockfd, Memory pstRequestData, int uiK4Index, Memory ucA4Ki,
			IntByReference uiSN1);

	/**
	 * 解密A4Ki密钥组接口
	 * 该接口输入A4Ki密钥组，调用本地密码机进行解密，输出Ki密钥明文，进行制卡任务。其中用户口令可以在配置管理软件中进行设置，用于进行身份认证
	 * 
	 * @param uiK4Index
	 * @param ucA4Ki
	 * @param uiKiNum
	 * @param ucPwd
	 * @param uiPwdLen
	 * @param ucKi
	 * @return
	 */
	public abstract int GSM_GetKi(int uiK4Index, byte[] ucA4Ki, int uiKiNum, String ucPwd, int uiPwdLen, byte[] ucKi);

	/**
	 * 校验接口
	 * 
	 * @param ucDataIn
	 *            输入前16字节数据指针
	 * @param ucDataIn4
	 *            输入后四字节数据指针
	 * @return
	 */
	public abstract int check_ckv(byte[] ucDataIn, byte[] ucDataIn4);

}