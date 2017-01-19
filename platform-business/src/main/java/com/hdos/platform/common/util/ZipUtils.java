package com.hdos.platform.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Zip 工具类
 * 
 * @author matao
 * @date 2016年7月26日
 */
public class ZipUtils {
	
	/**
	 * 压缩指定的文件，到指定的ZIP文件，如果sourceFile是目录，压缩时候会忽略其根目录
	 * 
	 * @param sourceFile
	 *            待压缩的目录或文件
	 * @param zipFile
	 *            输出的ZIP文件路径
	 * @throws IOException
	 */
	public static void zip(File sourceFile, File zipFile) throws IOException {
		zip(sourceFile, zipFile, false);
	}

	/**
	 * 压缩指定的文件，到指定的ZIP文件
	 * 
	 * @param sourceFile
	 *            待压缩的目录或文件
	 * @param zipFile
	 *            输出的ZIP文件路径
	 * @param containDir
	 *            如果sourceFile是目录，且containDir为true，则目录根也压缩进去，否则不压缩根
	 * @throws IOException
	 */
	public static void zip(File sourceFile, File zipFile, boolean containDir) throws IOException {
		if (sourceFile == null || !sourceFile.exists()) {
			throw new FileNotFoundException(sourceFile + " not found.");
		}

		// mkdirs parent file
		if (!zipFile.getParentFile().exists()) {
			zipFile.getParentFile().mkdirs();
		}

		ZipOutputStream zipOutStream = null;
		try {
			// Zip Out
			zipOutStream = new ZipOutputStream(new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32()));
			compressZip(sourceFile, zipOutStream, "", containDir);
		} finally {
			StreamUtils.close(zipOutStream);
		}
	}

	private static void compressZip(File file, ZipOutputStream out, String basedir, boolean containDir)
	        throws IOException {
		/* 判断是目录还是文件 */
		if (file.isDirectory()) {
			if (containDir) {
				basedir = basedir + file.getName() + "/";
				out.putNextEntry(new ZipEntry(basedir));
			}
			zipDir(file, out, basedir);
		} else {
			zipFile(file, out, basedir);
		}
	}

	private static void zipDir(File inputDir, ZipOutputStream zipOutStream, String baseDir) throws IOException {
		if (!inputDir.exists()) {
			return;
		}
		File[] files = inputDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			/* 递归 */
			compressZip(files[i], zipOutStream, baseDir, true);
		}
	}

	private static void zipFile(File inputFile, ZipOutputStream zipOutStream, String baseDir) throws IOException {
		InputStream in = null;
		try {
			ZipEntry entry = new ZipEntry(baseDir + inputFile.getName());
			zipOutStream.putNextEntry(entry);
			in = new BufferedInputStream(new FileInputStream(inputFile), 1024);
			StreamUtils.copyStream(in, zipOutStream);
		} finally {
			StreamUtils.close(in);
		}
	}

}
