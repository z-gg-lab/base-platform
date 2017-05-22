package com.hdos.platform.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.component.model.CodeGenerateVO;
import com.hdos.platform.base.component.service.CodeGenerateService;
import com.hdos.platform.core.SpringContextHolder;

/**
 * 编码工具类
 * 
 * @author zhuw
 */
public final class CodeGenerateUtils {

	private static CodeGenerateService codeGenerateService;

	private final static Logger logger = LoggerFactory.getLogger(CodeGenerateUtils.class);
	
	private static CodeGenerateService getCodeGenerateService() {
		if (null == codeGenerateService) {
			codeGenerateService = SpringContextHolder.getBean(CodeGenerateService.class);
		}
		return codeGenerateService;
	}

	/**
	 * 获得编码
	 * @param busniessKey
	 * @param businessId 
	 * @return
	 */
	public static String getCodeGenerator(String busniessKey,String businessId) {
		// String string = {prefix:aaa,formatter:yyyyMMdd,suffix:0000};
		
		String code = null;
		List<CodeGenerateVO> list = getCodeGenerateService().getCode(busniessKey,businessId);
		if (list.isEmpty()) {
			return code;
		}
		String string = list.get(0).getRule();
		int no = list.get(0).getNo();
		try {
			JSONObject jsonObject = (JSONObject) JSONObject.parse(string);
			String prefix = jsonObject.getString("prefix");
			String formatter = jsonObject.getString("formatter");
			String suffix = jsonObject.getString("suffix");
			// 如果注册时的格式不正确则不生成编码
			if(prefix==null||formatter==null||suffix==null){
				return null;
			}
			Date date = new Date();
			
			SimpleDateFormat sdf = new SimpleDateFormat(formatter);
			String strFill = getStr(list.get(0).getLength()).substring(0, list.get(0).getLength() - suffix.length()-String.valueOf(no).length());
			code = prefix+ sdf.format(date)+strFill+suffix+no;
		
		} catch (Exception e) {
			//捕获到异常则不生成编码
			logger.info(e.getMessage());
			return null;
		}
		return code;
	}

	/**
	 * 生成指定长度
	 * @param num
	 * @return
	 */
	private static String getStr(int num){
		StringBuffer sb = new StringBuffer("");
		for(int i=0;i<num;i++){
		   sb.append("0");
		}
		return sb.toString();
	}
	
}
