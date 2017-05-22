package com.hdos.platform.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 * 
 * @author matao
 * @date 2016年6月18日
 */
public class DateUtils {

	public static final String MESSAGE_DATE_FORMAT_DD = "yyyy-MM-dd";
	public static final String MESSAGE_DATE_FORMAT_MM = "yyyy-MM-dd HH:mm";
	public static final String MESSAGE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String MESSAGE_DATE_FORMAT_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
	public static final String MESSAGE_DATE_FORMAT_NOBANK = "yyyyMMddHHmmss";

	public static String formatDate(String pattern, Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
		return format.format(date);
	}

	public static Date parseDate(String pattern, String dateTime) {
		if (dateTime == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
		try {
			return format.parse(dateTime);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String formatDate(Date date) {
		return formatDate(MESSAGE_DATE_FORMAT, date);
	}

	public static String getNowDateTime(String pattern) {
		return formatDate(StringUtils.isEmpty(pattern) ? MESSAGE_DATE_FORMAT : pattern, new Date());
	}

	public static String getNowDateTime() {
		return formatDate(new Date());
	}
}
