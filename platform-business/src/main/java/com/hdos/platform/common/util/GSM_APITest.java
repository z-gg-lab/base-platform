package com.hdos.platform.common.util;

import com.sun.jna.Memory;
import com.sun.jna.ptr.IntByReference;

/**
 * 调用虚拟加密机接口测试
 * 
 * @author Administrator
 *
 */
public class GSM_APITest {

	public static void main(String[] args) {
		try {

			String result = GSMUtils.GSM_SealMessage(3, 4,
					"{\"id\":\"999\",\"username\":\"huada\",\"password\":\"huada\"}");
			System.out.println(result);
			String string = "uUCS9/+DWEFBT4SLpPtyCUGOeRhvbnlEmxAkb2dcI/ZMc9vFs9rZcgaKu5z+7QZFJcRRNsQPyR8w"
					+ "SwUSAUUq/yPi6nEtEdi4XCnhcRgrLouBPFywnjhSwgx1cAoO4AzVueL9Qe4z7BQMqfDFTbXubaOo"
					+ "kSZBb2DyfLiC+AqnLLsDhXBc/7uPIpaZ3RtCcJEFuvEPVpAaM/0xvzMR6B62lE8UbNti/8aq3iU1"
					+ "Vc11s5KV5Rc1De0WKQhg/9tkWekvCjVZM0DUvrjWslca1vnUgfBjoB7jqJTCpm6pA8FWu7wtNXnZ"
					+ "8s6GpxxBeTw+qKF66xiCrJ8wgrnyPKJ0iustUqN9hgLeMwfYu43ZudfzWq9b004Z82Svhlu7+G+O"
					+ "q+CMio182DnC0Z6aTFfrggRqAki6eQ/KhgcdTNoPV5/1RYMvHspo+AcIEjDpL+xPqM0Flerguf/E"
					+ "CMKsG06FTZRNUdlfEQfBu6cCmqf0U8YyyQfuGdt4sqCGZSX60llLFKCj85J+MBP59TJOPJgfYO08"
					+ "tB7y256Icfxjlez4ZqO8gffEDDNOMPAyytDoo5xmg8OXqwEAAN13rksZ3tBz/rqBWMZk4t/jTs9o"
					+ "Mz1NxCF/dCFKLe8B";
			GSMUtils.GSM_UnSealMessage(3, 4, string);
			System.out.println(GSMUtils.GSM_UnSealMessage(3, 4, string));

			IntByReference uiSockfd = new IntByReference();

			GSM_API.instance.GSM_KGC_Connect("10.11.72.3", 9878, uiSockfd);

			Memory pstRequestData = new Memory(1024);

			GSM_API.instance.GSM_GetKi_Init(3, 1, 400, 121, pstRequestData);

			Memory ucA4Ki = new Memory(1024 * 1024);
			IntByReference uiSN1 = new IntByReference();

			GSM_API.instance.GSM_GetA4Ki(uiSockfd.getValue(), pstRequestData, 11, ucA4Ki, uiSN1);
			System.out.println(uiSN1.getValue() + "---------ucA4Ki---------" + ucA4Ki + "-----");

			byte[] fterEncryptByteArray = new byte[20];
			ucA4Ki.read(0, fterEncryptByteArray, 0,20);
//			byte[] b = new byte[20];
//			for (int i = 0; i < b.length; i++) {
//				b[i] = fterEncryptByteArray[i];
//			}
			String s = bytesToHexString(fterEncryptByteArray);
			System.out.println(s);
			GSMUtils.GSM_Release_Connect(uiSockfd);
			byte[] ucKi = new byte[20];

			GSM_API.instance.GSM_GetKi(11,fterEncryptByteArray, 400, "111111", "111111".length(), ucKi);
			
			System.out.println(bytesToHexString(ucKi));
			String sss = bytesToHexString(ucKi);
			byte[] b1 = new byte[32];
			byte[] b2 = new byte[8];
			
			String s1 = sss.substring(0, 32);
			String s2 = sss.substring(32);
			
			System.out.println(s1);
			System.out.println(s2);
			
			b1 = hexStringToByte(s1);
			b2 = hexStringToByte(s2);
			System.out.println(GSM_API.instance.check_ckv(b1, b2));
			
			System.out.println(bytesToHexString(b1));
			System.out.println(bytesToHexString(b2));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final String bytesToHexString(byte[] bArray) {
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

	public static byte[] hexStringToByte(String hex) {   
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