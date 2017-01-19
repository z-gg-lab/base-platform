package com.hdos.platform.common.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

public class SHA1 {

	/**
	   * 串接arr参数，生成sha1 digest
	   *
	   * @param arr
	   * @return
	   */
	  public static String gen(String... arr){
	    Arrays.sort(arr);
	    StringBuilder sb = new StringBuilder();
	    for (String a : arr) {
	      sb.append(a);
	    }
	    return DigestUtils.sha1Hex(sb.toString());
	  }

	  /**
	   * 用&串接arr参数，生成sha1 digest
	   *
	   * @param arr
	   * @return
	   */
	  public static String genWithAmple(String... arr) throws NoSuchAlgorithmException {
	    Arrays.sort(arr);
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < arr.length; i++) {
	      String a = arr[i];
	      sb.append(a);
	      if (i != arr.length - 1) {
	        sb.append('&');
	      }
	    }
	    MessageDigest SHA1 = MessageDigest.getInstance("SHA-1");  
	    SHA1.update(sb.toString().getBytes());  
        byte[] m = SHA1.digest();//加密  
	    return getString(m);
	  }
	//将加密后的签名转换成字符串
		private static String getString(byte[] b){  
	        StringBuffer sb = new StringBuffer();  
	         for(int i = 0; i < b.length; i ++){  
	        	 sb.append(b[i]);  
	         }  
	     	return sb.toString();  
		}

}
