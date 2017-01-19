package com.hdos.platform.base.uploader.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hdos.platform.base.uploader.controller.FileUploadConfig;
import com.hdos.platform.common.util.StreamUtils;
import com.hdos.platform.common.util.StringUtils;

/**
 * 文件地址转换过滤器
 * @author chenyang
 *
 */
public class FileUrlFilter extends OncePerRequestFilter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public FileUrlFilter() {
	}

	/** {@inheritDoc} */
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
		try {

			// 文件服务器，采用过滤器方式实现，文件服务器的调用，仅需要读取本地文件即可，无需判断什么类型
			// 相对文件服务器本身而言，文件服务器就是单机的
			String discPath = FileUploadConfig.getLocalPath();
			if (logger.isDebugEnabled()) {
				logger.debug("LocalPath=" + discPath);
			}

			// 构造一个文件对象，用于判断配置的文件对象是否存在
			File localPathFile = new File(discPath);
			if (!localPathFile.exists()) {
				logger.error("Unkown 'localPath' Config Group with '" + localPathFile + "', images server not ready.");
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Images server not ready.");
				return;
			}

			// 判断是否为目录，或者能否可读
			if (!localPathFile.isDirectory() || !localPathFile.canRead()) {
				logger.error("'localPath' Config Group with '" + localPathFile
				        + "' is not directory or can Read, images server not ready.");
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Images server not ready.");
				return;
			}

			// 提取系统配置的真实路径
			String canonicalPath = localPathFile.getCanonicalPath();

			String contextPath = request.getContextPath();
			String filePath = canonicalPath + request.getRequestURI();
			if (StringUtils.isNotEmpty(contextPath)) {
				filePath = canonicalPath + request.getRequestURI().replaceFirst(contextPath + "/", "/");
			}

			if (logger.isInfoEnabled()) {
				logger.info("Get Image Path: " + filePath);
			}

			// 构造真实文件
			File file = new File(filePath);
			if (!file.exists()) {
				// 这种情况最平凡
				logger.warn("Image file '" + file + "' not found.");
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			// 提取真实路径
			String requestImageRealPath = file.getCanonicalPath();
			if (!requestImageRealPath.startsWith(canonicalPath)) {
				// 如果真实图片文件不是以服务器配置的路径开头，表示非法访问
				logger.error("Image file '" + requestImageRealPath + "' not at localPath '" + canonicalPath + "'.");
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			// XXX 还缺少判断图片后缀

			InputStream in = null;
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				in = new FileInputStream(file);
				StreamUtils.copyStream(in, out);
				return;
			} finally {
				StreamUtils.close(in);
				StreamUtils.close(out);
			}
		} catch (Exception e) {
			logger.error("ImageServer for " + request.getRequestURI() + " with error.", e);
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

}
