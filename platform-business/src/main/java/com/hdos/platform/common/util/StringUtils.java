package com.hdos.platform.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * 常用字符串工具类
 * 
 * @author matao
 * @date 2016年6月18日
 */
public class StringUtils {
	private static final String PREFIX = "\\u";

	/** 字符串工具类 */
	private StringUtils() {
	}

	/**
	 * 检查一个字符串是否为空字符串或者对象是否为空
	 * 
	 * @param str
	 *            the candidate String
	 */
	public static boolean isEmpty(Object str) {
		return (str == null || "".equals(str));
	}

	/**
	 * 检查指定的CharSequence是否有长度，包含空白字符
	 * 
	 * @param str
	 *            待检查的CharSequence
	 * @return 如果有长度则返回true
	 * @see #hasText(String)
	 */
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * 判断给定的字符串是否包含非空白字符
	 * 
	 * @param str
	 *            待检查的字符串
	 * @return 如果包含一个非空格就返回true
	 * @see #hasLength(CharSequence)
	 */
	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}

	/**
	 * 判断给定的字符串是否包含非空白字符
	 * 
	 * @param str
	 *            待检查的字符串
	 * @return 如果包含一个非空格就返回true
	 * @see Character#isWhitespace
	 */
	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断给定的字符串是否包含非空白字符
	 * 
	 * @param str
	 *            待检查的字符串
	 * @return 如果包含一个非空格就返回true
	 * @see #hasText(CharSequence)
	 */
	public static boolean hasText(String str) {
		return hasText((CharSequence) str);
	}

	/**
	 * 比较两个字符串是否相同,预防空指针
	 * 
	 * @param input1
	 * @param input2
	 * @return
	 */
	public static boolean isEquals(String input1, String input2) {
		if (input1 == input2) {
			return true;
		}
		boolean hasLength1 = hasLength(input1);
		boolean hasLength2 = hasLength(input2);
		if (!hasLength1 && !hasLength2) {
			return true;
		}
		if (!hasLength1 || !hasLength2) {
			return false;
		}
		return input1.equals(input2);
	}

	/**
	 * 比较两个字符串是否相同,预防空指针
	 * 
	 * @param input1
	 * @param input2
	 * @return
	 */
	public static boolean equalsIgnoreCase(String input1, String input2) {
		if (input1 == input2) {
			return true;
		}
		boolean hasLength1 = hasLength(input1);
		boolean hasLength2 = hasLength(input2);
		if (!hasLength1 && !hasLength2) {
			return true;
		}
		if (!hasLength1 || !hasLength2) {
			return false;
		}
		return input1.equalsIgnoreCase(input2);
	}

	/**
	 * 检查给定的字符串是否包含空格
	 * 
	 * @param str
	 *            待检查的字符串
	 * @return 如果包含一个空格就返回true
	 * @see Character#isWhitespace
	 */
	public static boolean containsWhitespace(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查给定的字符串是否包含空格
	 * 
	 * @param str
	 *            待检查的字符串
	 * @return 如果包含一个空格就返回true
	 * @see #containsWhitespace(CharSequence)
	 */
	public static boolean containsWhitespace(String str) {
		return containsWhitespace((CharSequence) str);
	}

	/**
	 * 移除给定字符串的前后空格部分
	 * 
	 * @param str
	 *            格式化前的字符串
	 * @return 格式化后的字符串
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
			sb.deleteCharAt(0);
		}
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 移除给定字符串中所有的空白部分
	 * 
	 * @param str
	 *            格式化前的字符串
	 * @return 格式化后的字符串
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimAllWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		int len = str.length();
		StringBuilder sb = new StringBuilder(str.length());
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (!Character.isWhitespace(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 移除字符串前面的空格部分
	 * 
	 * @param str
	 *            格式化前的字符串
	 * @return 格式化后的字符串
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimLeadingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}

	/**
	 * 移除字符串后面的空格部分
	 * 
	 * @param str
	 *            格式化前的字符串
	 * @return 格式化后的字符串
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimTrailingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 测试一个字符串是否为空,包括NULL
	 * 
	 * @param input
	 *            待测试的字符串
	 * @return true 为空，false，不为空
	 */
	public static boolean isEmpty(String input) {
		return input == null ? true : input.trim().length() == 0;
	}

	/** 测试一个字符串是否不为空,包括NULL */
	public static boolean isNotEmpty(String input) {
		return !isEmpty(input);
	}

	/** 如果字符串为NULL，则返回NULL,否则trim这个字符串 */
	public static String safeTrim(String text) {
		return text == null ? null : text.trim();
	}

	/** 如果对象为NULL，则返回NUll，否则toString() */
	public static String safeToString(Object input) {
		return input == null ? null : input.toString();
	}

	/** 如果text为空，则返回defaultValue */
	public static String defaultValue(String text, String defaultValue) {
		text = safeTrim(text);
		return isNotEmpty(text) ? text : defaultValue;
	}

	/** 如果text不为合法的数字，则返回defaultValue */
	public static int defaultValue(String text, int defaultValue) {
		text = safeTrim(text);
		if (isNotEmpty(text)) {
			try {
				return Integer.parseInt(text);
			} catch (NumberFormatException e) {
			}
		}
		return defaultValue;
	}

	/** 如果text不为合法的boolean，则返回defaultValue */
	public static boolean defaultValue(String text, boolean defaultValue) {
		text = safeTrim(text);
		if (isNotEmpty(text)) {
			if (Boolean.TRUE.toString().equalsIgnoreCase(text) || Boolean.FALSE.toString().equalsIgnoreCase(text)) {
				return Boolean.parseBoolean(text);
			}
		}
		return defaultValue;
	}

	/** 获取当前对象的唯一Hash标识 */
	public static String identityHashCode(Object object) {
		return object.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(object));
	}

	/**
	 * 生产一个惟一的UUID字符串
	 * 
	 * @return 惟一的UUID字符串
	 */
	public static String generatorUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		// 去掉"-"符号
		String temp = str.replaceAll("-", "");
		return temp.toUpperCase();
	}

	/**
	 * 以指定的分隔符分割字符串
	 * 
	 * @param str
	 *            待分割的字符串
	 * @param delimiters
	 *            分隔符
	 * @return
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	/**
	 * 以指定的分隔符分割字符串
	 * 
	 * @param str
	 *            待分割的字符串
	 * @param delimiters
	 *            分隔符
	 * @param trimTokens
	 *            是否trim分割后的字符串
	 * @param ignoreEmptyTokens
	 *            是否忽略空格字符串
	 * @return
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
	        boolean ignoreEmptyTokens) {
		if (str == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return CollectionUtils.toStringArray(tokens);
	}

	/** 获取本地语言的字符串 */
	public static String getClientLocaleString(final Locale locale) {
		final String language = locale.getLanguage();
		final String country = locale.getCountry();
		if (country.length() == 0) {
			return language;
		} else {
			return language + "-" + country;
		}
	}

	/**
	 * 当指定的输入为空，则返回默认值
	 * 
	 * @param input
	 * @param defaultValue
	 * @return
	 */
	public static String getValue(String input, String defaultValue) {
		return isEmpty(input) ? defaultValue : input;
	}

	/**
	 * 当指定的输入为空，或者不合法，则返回默认值
	 * 
	 * @param input
	 * @param defaultValue
	 * @return
	 */
	public static int getValue(String input, int defaultValue) {
		if (isEmpty(input)) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * 当指定的输入为空，或者不合法，则返回默认值
	 * 
	 * @param input
	 * @param defaultValue
	 * @return
	 */
	public static boolean getValue(String input, boolean defaultValue) {
		return isEmpty(input) ? defaultValue : Boolean.parseBoolean(input) ? true : defaultValue;
	}

	/**
	 * HtmlEncode
	 * 
	 * @param source
	 * @return
	 */
	public static String htmlEncode(String source) {
		if (source == null) {
			return null;
		}
		StringBuffer buffer = new StringBuffer(source.length() * 2);
		for (int i = 0; i < source.length(); i++) {
			char c = source.charAt(i);
			switch (c) {
			case '<':
				buffer.append("&lt;");
				break;
			case '>':
				buffer.append("&gt;");
				break;
			case '&':
				buffer.append("&amp;");
				break;
			case '"':
				buffer.append("&quot;");
				break;
			default:
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	/**
	 * HtmlEncode
	 * 
	 * @param source
	 * @return
	 */
	public static String htmlEncodeForArg(String source) {
		if (source == null) {
			return null;
		}
		StringBuffer buffer = new StringBuffer(source.length() * 2);
		for (int i = 0; i < source.length(); i++) {
			char c = source.charAt(i);
			switch (c) {
			case '<':
				buffer.append("&lt;");
				break;
			case '>':
				buffer.append("&gt;");
				break;
			default:
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	/**
	 * 将本地字符串转换为Ascii字符编码
	 * 
	 * @param str
	 * @return
	 */
	public static String native2Ascii(String str) {
		char[] charArray = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (char c : charArray) {
			sb.append(char2Ascii(c));
		}
		return sb.toString();
	}

	/**
	 * 将本地字符转换为Ascii字符编码
	 * 
	 * @param c
	 * @return
	 */
	public static String char2Ascii(char c) {
		if (c > 255) {
			StringBuffer sb = new StringBuffer();
			sb.append(PREFIX);

			// 高8位
			int code = (c >> 8);
			String tmp = Integer.toHexString(code);
			if (tmp.length() == 1) {
				sb.append("0");
			}
			sb.append(tmp);

			// 处理低8位
			code = (c & 0xff);
			tmp = Integer.toHexString(code);
			if (tmp.length() == 1) {
				sb.append("0");
			}
			sb.append(tmp);
			return sb.toString();
		} else {
			return Character.toString(c);
		}
	}

	/**
	 * 将Ascii字符编码转换为本地字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String acsii2Native(String str) {
		StringBuffer sb = new StringBuffer();
		int begin = 0;
		int index = str.indexOf(PREFIX);
		while (index != -1) {
			//
			sb.append(str.substring(begin, index));
			sb.append(ascii2Char(str.substring(index, index + 6)));
			begin = index + 6;
			index = str.indexOf(PREFIX, begin);
		}
		sb.append(str.substring(begin));
		return sb.toString();
	}

	/**
	 * 将Ascii字符编码转换为本地字符
	 * 
	 * @param str
	 * @return
	 */
	private static char ascii2Char(String str) {
		if (str.length() != 6) {
			throw new IllegalArgumentException("Ascii string of a native character must be 6 character.");
		}

		if (!PREFIX.equals(str.substring(0, 2))) {
			throw new IllegalArgumentException("Ascii string of a native character must start with \"\\u\".");
		}

		// 处理高8位
		String tmp = str.substring(2, 4);
		int code = Integer.parseInt(tmp, 16) << 8;

		// 处理低8位
		tmp = str.substring(4, 6);
		code += Integer.parseInt(tmp, 16);

		return (char) code;
	}

}
