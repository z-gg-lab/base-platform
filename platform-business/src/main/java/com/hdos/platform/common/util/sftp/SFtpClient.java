package com.hdos.platform.common.util.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdos.platform.common.util.StreamUtils;
import com.hdos.platform.common.util.StringUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * STtp Client
 * 
 * @author matao
 * @date 2016年6月30日
 */
public class SFtpClient {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Session activeSession;
	private ChannelSftp activeChannel;

	/**
	 * 默认的构造函数
	 */
	public SFtpClient() {
	}

	/**
	 * 连接sftp服务器
	 * 
	 * @param host
	 *            主机
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 */
	public synchronized boolean connect(String host, int port, String username, String password) {
		if (StringUtils.isEmpty(host)) {
			throw new IllegalArgumentException("host is empty.");
		}
		if (StringUtils.isEmpty(username)) {
			throw new IllegalArgumentException("username is empty.");
		}

		JSch jsch = new JSch();

		Session session = null;
		try {
			session = jsch.getSession(username, host, port);
		} catch (JSchException e) {
			throw new IllegalArgumentException(e);
		}
		session.setPassword(password);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		session.setConfig(sshConfig);

		try {
			session.connect(3000);
			this.activeSession = session;
			Channel channel = session.openChannel("sftp");
			channel.connect(3000);
			this.activeChannel = (ChannelSftp) channel;
		} catch (JSchException e) {
			logger.error("Connect error.", e);
			if (this.activeSession != null) {
				this.activeSession.disconnect();
				this.activeSession = null;
			}
			return false;
		}
		return true;
	}

	/**
	 * 上传指定的文件到远程指定目录里
	 * 
	 * @param remoteDirectory
	 *            远程目录地址
	 * @param localFile
	 *            本地文件对象
	 */
	public void uploadFile(String remoteDirectory, File localFile) {
		uploadFile(remoteDirectory, localFile.getName(), localFile);
	}

	/**
	 * 上传指定的文件到远程指定目录里，并命名
	 * 
	 * @param remoteDirectory
	 *            远程目录地址
	 * @param remoteFileName
	 *            远程文件的名称
	 * @param localFile
	 *            本地文件
	 */
	public synchronized boolean uploadFile(String remoteDirectory, String remoteFileName, File localFile) {
		if (activeChannel == null) {
			throw new IllegalStateException("SFtp no connected.");
		}

		try {
			// 获取根目录
			String workHome = this.activeChannel.getHome();
			// 重新定位到远程目录
			try {
				this.activeChannel.cd(workHome + remoteDirectory);
			} catch (SftpException e) {
				this.activeChannel.cd(workHome);

				String[] folderPaths = remoteDirectory.split("/");
				for (String folderPath : folderPaths) {
					if (StringUtils.isEmpty(folderPath)) {
						continue;
					}
					try {
						this.activeChannel.cd(folderPath);
					} catch (SftpException e1) {
						this.activeChannel.mkdir(folderPath);
						this.activeChannel.cd(folderPath);
					}
				}
			}
			InputStream in = new FileInputStream(localFile);
			try {
				this.activeChannel.put(in, remoteFileName);
			} finally {
				StreamUtils.close(in);
			}
		} catch (SftpException e) {
			logger.error("UploadFile failed with sftp.", e);
			return false;
		} catch (FileNotFoundException e) {
			logger.error("UploadFile failed with local file not exist.", e);
			return false;
		}
		return true;
	}

	/**
	 * 关闭连接
	 */
	public synchronized void disconnect() {
		if (this.activeChannel != null) {
			this.activeChannel.disconnect();
			this.activeChannel = null;
		}
		if (this.activeSession != null) {
			if (this.activeSession.isConnected()) {
				this.activeSession.disconnect();
			}
			this.activeSession = null;
		}
	}

}
