package com.hdos.platform.base.fileuploader.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hdos.platform.base.fileuploader.model.FileVO;
import com.hdos.platform.core.base.BaseMapper;

@Repository
public interface FileUploaderMapper extends BaseMapper<FileVO> {
	
	List<FileVO> queryFilePath(String... fileIds);
	String queryIdByfileName(String fileName);
}
