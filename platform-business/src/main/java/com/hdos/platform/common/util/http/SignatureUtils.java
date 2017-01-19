package com.hdos.railway.http;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignatureUtils {
	//生成签名
	public static String getSignature(String... arr){
		String signature = null;
		Arrays.sort(arr);//将参与签名的字段排序
		//排序后将参与签名的字段用&拼接成字符串
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < arr.length; i++) {
	      String a = arr[i];
	      sb.append(a);
	      if (i != arr.length - 1) {
	        sb.append('&');
	      }
	    }
		try {
			MessageDigest SHA1 = MessageDigest.getInstance("SHA-1");
			SHA1.update(sb.toString().getBytes());  
	        byte[] m = SHA1.digest();//加密  
	        signature =  getString(m);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}  	    
        return signature;
	}
		
	//将加密后的签名转换成字符串
	public static String getString(byte[] b){  
        StringBuffer sb = new StringBuffer();  
         for(int i = 0; i < b.length; i ++){  
        	 sb.append(b[i]);  
         }  
	     	return sb.toString();  
		}
}
