package com.hdos.platform.base.fileImp;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hdos.platform.base.component.model.ExcelImportTemplateVO;
import com.hdos.platform.base.component.service.ExcelImportTemplateService;
import com.hdos.platform.base.filter.LoginContext;
import com.hdos.platform.base.filter.LoginUserInfo;
import com.hdos.platform.base.user.service.FileImpService;
import com.hdos.platform.common.util.CodeGenerateUtils;

@Controller
@RequestMapping("/excelimp")
public class FileImpController {

	private static final Logger logger = LoggerFactory.getLogger(FileImpController.class);
	@Autowired
	private FileImpService fileImpService;
	@Autowired
	private ExcelImportTemplateService excelImportTemplateService;
	
	private static String USER_KEY = "USER123";
	/**
	 * 添加excel任务
	 * 
	 * @param file
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "/userExcel", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String impDevice(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		String fileName = file.getOriginalFilename();
		File targetFile = new File(fileName);
		String returnInfo = null;
		ExcelImportTemplateVO excelImportTemplateVO  = excelImportTemplateService.getByKey(USER_KEY);
		String key = excelImportTemplateVO.getExcelKey();
		try {
			file.transferTo(targetFile);
			Map<String, Object> map = fileImpService.readExcelData(targetFile, null,key, null);
			returnInfo = map.get("msg").toString();
		} catch (Exception e) {
			logger.info(e.getMessage());
			return e.toString();
		} finally {
			if (targetFile.exists()) {
				targetFile.delete();
			}
		}
		return returnInfo;

	}
}
