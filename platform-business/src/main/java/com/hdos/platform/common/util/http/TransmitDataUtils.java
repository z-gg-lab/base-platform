package com.hdos.platform.common.util.http;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class TransmitDataUtils {
	/**
	 * 传递给接口的参数
	 * @param token
	 * @param account
	 * @param jsonObject
	 * @return Map<String, String>
	 */
	public static Map<String, String> transmitData(String token, String account,JSONObject jsonObject){
		String signature = null;// 定义签名
		String timestamp = String.valueOf(System.currentTimeMillis());// 获取当前系统的时间戳
		String[] arr = new String[4];
		String jsonParam = null;// 将客户端传过来的参数转转化成json字符串
		if(jsonObject == null){
			jsonParam = "";
		}else{
			jsonParam = jsonObject.toString();
		}
		arr[0] = token;
		arr[1] = account;
		arr[2] = timestamp;
		arr[3] = jsonParam;
		// 调用生成签名的方法
		signature = SignatureUtils.getSignature(arr);
		Map<String,String> data =new  HashMap<String,String>();
		data.put("signature", signature);
    	data.put("account", account);
    	data.put("timestamp", timestamp);
    	data.put("jsonParam", jsonParam);
		return data;
	}

}
