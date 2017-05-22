package com.hdos.platform.base.uploader.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.parser.DefaultFTPFileEntryParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hdos.platform.base.fileuploader.model.FileVO;
import com.hdos.platform.base.fileuploader.service.FileUploaderService;
import com.hdos.platform.base.filter.LoginContext;
import com.hdos.platform.base.filter.LoginUserInfo;
import com.hdos.platform.common.util.ConfigContants;
import com.hdos.platform.common.util.ConfigUtils;
import com.hdos.platform.common.util.DateUtils;
import com.hdos.platform.common.util.StreamUtils;
import com.hdos.platform.common.util.StringUtils;

/**
 * 文件上传控制器
 * 
 * @author chenyang
 *
 */
@RequestMapping("/file")
@Controller
public class FileUploadController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private FileUploaderService fileUploaderService;

	/**
	 * 文件上传
	 * 
	 * @param files
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/fileupload")
	@ResponseBody
	public FileVO uploadFile(@RequestParam("file") MultipartFile[] files, HttpServletRequest request) throws Exception {
		FileVO fileVO = null;
		String additionPath = null;
		MultipartFile file = null;
		for (int i = 0; i < files.length; i++) {
			file = files[i];
			if (file != null) {
				// 取得当前上传文件的文件名称
				String fileName = file.getOriginalFilename();
				if (StringUtils.isNotEmpty(fileName)) {

					fileVO = new FileVO();
					String filePath = FileUploadConfig.getLocalPath();
					fileVO.setFileName(fileName);
					if (StringUtils.isEmpty(filePath)) {
						logger.error("文件路径没有配置。");
						return fileVO;
					}

					fileVO.setFileSize(String.valueOf(file.getSize()));

					String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
					fileVO.setFileType(fileType);

					// 重命名上传后的文件名,防止文件被覆盖
					String generatorName = UUID.randomUUID() + "." + fileType;

					// 读取系统配置的文件存储方式
					String mode = ConfigUtils.get(ConfigContants.FILE_UPLOAD_STORAGE);
					if ("ftp".equalsIgnoreCase(mode)) {
						File targetFile = new File(generatorName);
						file.transferTo(targetFile);
						// 写文件到FTP
						String ftpPath = this.uploadToFtp(generatorName, targetFile, fileVO);
						fileVO.setDiscPath(ftpPath);
					} else if ("local".equalsIgnoreCase(mode)) {
						additionPath = createDirectory(filePath);
						filePath += (File.separator + additionPath);
						String newFileName = filePath + File.separator + generatorName;
						fileVO.setDiscPath(newFileName);
						// 写文件到本地
						File localFile = new File(newFileName);
						file.transferTo(localFile);
					}
					try {
						// 记录文件信息
						LoginUserInfo loginUserInfo = LoginContext.getCurrentUser();
						fileVO.setCreater(loginUserInfo.getUserId());
						fileVO = fileUploaderService.uploadFile(fileVO);
					} catch (Exception e) {
						logger.error("磁盘写文件错误。", e);
						throw e;
					}

				}
			}
		}
		return fileVO;
	}

	@RequestMapping("/filedelete")
	@ResponseBody
	public String deleteFile(String fileIds, HttpServletRequest request) {
		return "";
	}
	
	/**
	 * 上传文件到FTP
	 * 
	 * @param fileName
	 * @param file
	 * @param fileVO
	 * @return
	 */
	private String uploadToFtp(String fileName, File file, FileVO fileVO) {

		String dirName = null;

		String ftpIp = ConfigUtils.get(ConfigContants.FTP_IP);
		String ftpPort = ConfigUtils.get(ConfigContants.FTP_PORT);
		String ftpAccout = ConfigUtils.get(ConfigContants.FTP_ACCOUNT);
		String ftpPassword = ConfigUtils.get(ConfigContants.FTP_PASSWD);

		FTPClient ftp = new FTPClient();
		ftp.setControlEncoding("UTF-8");
		ftp.setConnectTimeout(30000);
		try {
			ftp.connect(ftpIp, Integer.parseInt(ftpPort));
			logger.info(ftpAccout+ftpPassword);
			boolean loginResult = ftp.login(ftpAccout,ftpPassword);// 登录
			logger.info("loginResult:"+loginResult);
			if (!loginResult) {
				ftp.disconnect();
				logger.error("FTP服务器拒绝连接。");
				return null;
			}
			try {

				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
				// 切换文件夹
				ftp.changeWorkingDirectory(FileUploadConfig.getNetPath());
				dirName = DateUtils.getNowDateTime("yyyy-MM-dd");
				// 创建文件夹
				ftp.mkd(FileUploadConfig.getNetPath() + "/" + dirName);
				ftp.changeWorkingDirectory(FileUploadConfig.getNetPath() + "/" + dirName);
				InputStream in = new FileInputStream(file);
				try {
					ftp.storeFile(fileName, in);
				} finally {
					StreamUtils.close(in);
				}
			} finally {
				ftp.logout();
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					logger.error("IOException错误。", ioe);
				}
			}
		}
		return FileUploadConfig.getNetPath()+"/"+dirName+"/"+fileName;
	}


	/**
	 * 在本地创建文件夹
	 * 
	 * @param path
	 * @return
	 */
	private String createDirectory(String path) {
		String fileDir = DateUtils.getNowDateTime("yyyy-MM-dd");
		String additionPath = fileDir;
		String curPath = path + File.separator + additionPath;
		List<String> folderPaths = new ArrayList<String>();

		while (curPath.indexOf(File.separator) > -1) {
			folderPaths.add(curPath);
			curPath = curPath.substring(0, curPath.lastIndexOf(File.separator));
		}

		for (int i = folderPaths.size() - 1; i >= 0; i--) {
			File file = new File(folderPaths.get(i));
			// 如果文件夹不存在则创建
			if (!file.exists() && !file.isDirectory()) {
				file.mkdir();
			}
		}
		return additionPath;
	}

	/**
	 * 判断文件大小
	 * 
	 * @param disconfigGroup
	 * @param fileSize
	 * @return
	 */
	private String isFileOverSize(long fileSize) {

		String maxSize = FileUploadConfig.getMaxSize();
		long fileMaxSize = 0;
		if (StringUtils.isEmpty(maxSize)) {// 默认10MB
			fileMaxSize = 10 * 1024 * 1024;
		} else {
			fileMaxSize = Long.valueOf(maxSize).longValue() * 1024;
		}

		String result = "";
		if (fileSize > fileMaxSize) {
			result = "文档大小超出限制。最大上传文件大小不能超过：" + fileMaxSize / 1024 + "KB";
		}

		return result;
	}

	/**
	 * 判断文件类型
	 * 
	 * @param disconfigGroup
	 * @param fileType
	 * @return
	 */
	private String isTypeValidate(String fileType) {
		String fileTypes = FileUploadConfig.getFileType();
		String result = "";
		if (!StringUtils.isEmpty(fileTypes)) {
			String[] types = fileTypes.split(",");
			int flag = 0;
			for (int i = 0; i < types.length; i++) {
				if (fileType.equalsIgnoreCase(types[i])) {
					flag = 1;
					break;
				}
			}
			if (flag == 0) {
				result = "文件类型不合法。支持的文件类型为：" + fileTypes;
			}
		}

		return result;
	}

	/**
	 * 文件下载
	 * 
	 * @param downloadfiles
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/filedownload")
	public void uploadFile(String downloadfiles, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		List<FileVO> list = fileUploaderService.queryFilePath(downloadfiles.split(","));
		InputStream responseIn = null;
		OutputStream responseout = null;
		try {
			if (list.size() != 0) {
				String fileName = list.get(0).getFileName();
				 String userAgent = request.getHeader("User-Agent"); 
					try {
						//针对IE或者以IE为内核的浏览器：
						if (userAgent.contains("MSIE")||userAgent.contains("Trident")) {
						fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
						} else {
						//非IE浏览器的处理：
						fileName = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
						}
					} catch (UnsupportedEncodingException e) {
						logger.info(e.getMessage());
					}
				/*	response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));
					response.setContentType("application/vnd.ms-excel;charset=utf-8");
					response.setCharacterEncoding("UTF-8"); */
				response.setContentType("application/ostet-stream");
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
				
				responseout = response.getOutputStream();
				if ("ftp".equalsIgnoreCase(ConfigUtils.get(ConfigContants.FILE_UPLOAD_STORAGE))) {
					responseIn = fileUploaderService.downloadFromFtp(list.get(0).getDiscPath());
				} else if ("local".equalsIgnoreCase(ConfigUtils.get(ConfigContants.FILE_UPLOAD_STORAGE))) {
					responseIn = new FileInputStream(new File(list.get(0).getDiscPath()));
				}

				StreamUtils.copyStream(responseIn, responseout);
			}
		} catch (Exception e) {
			logger.error("ImageServer for " + request.getRequestURI() + " with error.", e);
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} finally {
			StreamUtils.close(responseIn);
			StreamUtils.close(responseout);
		}
	}

}
