package com.hdos.platform.base.fileuploader.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.base.fileuploader.mapper.FileUploaderMapper;
import com.hdos.platform.base.fileuploader.model.FileVO;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.page.PageImpl;
import com.hdos.platform.common.util.ConfigContants;
import com.hdos.platform.common.util.ConfigUtils;
import com.hdos.platform.common.util.StringUtils;
import com.hdos.platform.core.base.BaseService;

/**
 * 文件上传Service
 * 
 * @author chenyang
 * 
 */
@Service
@Transactional
public class FileUploaderService extends BaseService<FileVO> {

	@Autowired
	private FileUploaderMapper fileUploaderMapper;

	private static final Logger logger = LoggerFactory.getLogger(FileUploaderService.class);

	/**
	 * 分页查询
	 * 
	 * @param condition
	 *            条件
	 * @param pageNumber
	 *            页码，从 1 开始
	 * @param pageSize
	 *            每页条数
	 * @return
	 */
	public Page<FileVO> findPage(Map<String, Object> condition, int pageNumber, int pageSize) {
		int total = fileUploaderMapper.count(condition);
		RowBounds rowBounds = new RowBounds((pageNumber - 1) * pageSize, pageSize);
		List<FileVO> content = total > 0 ? fileUploaderMapper.list(condition, rowBounds) : new ArrayList<FileVO>(0);
		return new PageImpl<FileVO>(content, pageNumber, pageSize, total);
	}

	/**
	 * 根据id获取文件
	 * 
	 * @param fileId
	 * @return
	 */
	public FileVO readById(String fileId) {
		return fileUploaderMapper.getById(fileId);
	}

	/**
	 * 保存文件
	 * 
	 * @param fileVO
	 */
	public void save(FileVO fileVO) {
		if (null == fileVO) {
			logger.error("保存菜单ERROR：" + fileVO);
			throw new IllegalArgumentException();
		}
		fileVO.setCreateTime(new Timestamp(System.currentTimeMillis()));
		fileVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		if (StringUtils.isEmpty(fileVO.getFileId())) {// 新增
			fileVO.setFileId(generateKey(fileVO));
			fileUploaderMapper.insert(fileVO);
		} else {// 更新
			fileUploaderMapper.update(fileVO);
		}
	}

	/**
	 * 插入文件信息
	 * 
	 * @param fileVO
	 * @return
	 */
	public FileVO uploadFile(FileVO fileVO) {
		if (null == fileVO) {
			logger.error("保存菜单ERROR：" + fileVO);
			throw new IllegalArgumentException();
		}
		fileVO.setFileId(generateKey(fileVO));
		fileVO.setCreateTime(new Timestamp(System.currentTimeMillis()));
		fileVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		fileUploaderMapper.insert(fileVO);
		return fileVO;
	}

	/**
	 * 批量删除文件
	 * 
	 * @param fileIds
	 */
	public void delete(String... fileIds) {
		fileUploaderMapper.deleteInBulk(fileIds);
	}

	/**
	 * 根据文件ID获取文件信息
	 * 
	 * @param fileIds
	 * @return
	 */
	public List<FileVO> queryFilePath(String... fileIds) {
		return fileUploaderMapper.queryFilePath(fileIds);
	}

	public String insertPhoto(String idNo, int length, String userId, String filePath) {
		FileVO fileVO = new FileVO();
		String tmp = generateKey(fileVO);
		fileVO.setFileId(tmp);
		fileVO.setFileSize(Integer.toString(length));
		fileVO.setCreater(userId);
		fileVO.setFileName(idNo + ".jpg");
		fileVO.setFileType("jpg");
		fileVO.setSortNo(0);
		fileVO.setDiscPath(filePath);
		fileUploaderMapper.insert(fileVO);
		return tmp;
	}
	

	/**
	 * 从FTP下载文件
	 * 
	 * @param netPath
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public InputStream downloadFromFtp(String path) throws IOException {

		FTPClient ftpClient = new FTPClient();
		
		String ftpIp = ConfigUtils.get(ConfigContants.FTP_IP);
		String ftpPort = ConfigUtils.get(ConfigContants.FTP_PORT);
		String ftpAccout = ConfigUtils.get(ConfigContants.FTP_ACCOUNT);
		String ftpPassword = ConfigUtils.get(ConfigContants.FTP_PASSWD);

		InputStream in = null;

		try {

			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setConnectTimeout(30000);

			ftpClient.connect(ftpIp, Integer.parseInt(ftpPort));

			boolean loginResult = ftpClient.login(ftpAccout, ftpPassword);// 登录

			if (loginResult) {
				ftpClient.setBufferSize(1024);
				// 设置文件类型（二进制）
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				in = ftpClient.retrieveFileStream(path);
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				logger.info(e.getMessage());
			}
		}
		return in;
	}
}
