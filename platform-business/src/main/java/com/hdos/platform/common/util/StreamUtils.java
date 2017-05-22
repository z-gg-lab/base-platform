package com.hdos.platform.common.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * IO流相关工具类
 * 
 * @author matao
 * @date 2016年7月23日
 */
public class StreamUtils {
	/**
	 * 关闭指定资源，并记录日志
	 * 
	 * @param closeable
	 *            资源
	 */
	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				//
			}
		}
	}

	/**
	 * 根据指定的URL获取其文本内容，以UTF-8解码
	 * 
	 * @param url
	 *            指定的文本URL
	 * @return
	 */
	public static String getResourceAsString(URL url) throws IOException {
		return getResourceAsString(url, "UTF-8");
	}

	/**
	 * 根据指定的URL获取其文本类容
	 * 
	 * @param url
	 *            指定的文本URL
	 * @param enc
	 *            文本编码
	 * @return
	 */
	public static String getResourceAsString(URL url, String enc) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream inputStream = url.openStream();
		try {
			copyStream(inputStream, outputStream);
		} finally {
			close(inputStream);
			close(outputStream);
		}
		return outputStream.toString(enc);
	}

	/**
	 * 根据指定的URL获取其文本二进制内容
	 * 
	 * @param url
	 *            指定的URL
	 * @return
	 */
	public static byte[] getResourceAsByteArray(URL url) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream inputStream = url.openStream();
		try {
			copyStream(inputStream, outputStream);
		} finally {
			close(inputStream);
			close(outputStream);
		}
		return outputStream.toByteArray();
	}

	/**
	 * 读取指定URL中的文本信息，以Map<String,String>形式返回
	 * 
	 * @param resource
	 *            资源URL
	 * @return
	 * @throws IOException
	 *             发生IO读取错误
	 */
	public static Map<String, String> loadProperties(URL resource) throws IOException {
		final Map<String, String> propertyMap = new HashMap<String, String>();
		final InputStream in = resource.openStream();
		try {
			final Properties properties = new Properties();
			properties.load(in);

			final Enumeration<?> enumeration = properties.propertyNames();
			while (enumeration.hasMoreElements()) {
				final String key = (String) enumeration.nextElement();
				propertyMap.put(key, properties.getProperty(key));
			}
		} finally {
			close(in);
		}
		return Collections.unmodifiableMap(propertyMap);
	}

	/**
	 * 将输入里面的数据拷贝至输出流
	 * 
	 * @param input
	 *            输入流
	 * @param output
	 *            输出流
	 * @throws IOException
	 */
	public static void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] buffers = new byte[2048];
		int length;
		while ((length = input.read(buffers)) != -1) {
			output.write(buffers, 0, length);
		}
		output.flush();
	}

	/**
	 * 将输入里面的数据拷贝至输出流
	 * 
	 * @param input
	 *            输入流
	 * @param output
	 *            输出流
	 * @throws IOException
	 */
	public static void copyStream(Reader input, Writer output) throws IOException {
		char[] chars = new char[2048];
		int length;
		while ((length = input.read(chars)) != -1) {
			output.write(chars, 0, length);
		}
		output.flush();
	}

	/**
	 * 将资源的URL里面的数据拷贝至输出流
	 * 
	 * @param url
	 *            资源的URL
	 * @param output
	 *            输出流
	 * @throws IOException
	 */
	public static void copyStream(URL url, OutputStream output) throws IOException {
		InputStream input = url.openStream();
		try {
			copyStream(input, output);
		} finally {
			close(input);
		}
	}

	/**
	 * 删除指定的文件或者目录
	 * 
	 * @param file
	 *            文件或者目录
	 */
	public static void deleteFile(File file) {
		if (file.isFile()) {
			file.delete();
		} else if (file.isDirectory()) {
			for (File listFile : file.listFiles()) {
				deleteFile(listFile);
			}
			file.delete();
		}
	}
}
