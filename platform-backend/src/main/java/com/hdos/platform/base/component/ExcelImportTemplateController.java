package com.hdos.platform.base.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WriteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.component.model.ExcelImportColumnVO;
import com.hdos.platform.base.component.model.ExcelImportTemplateVO;
import com.hdos.platform.base.component.model.TableVO;
import com.hdos.platform.base.component.service.ExcelImportTemplateService;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.util.ConfigUtils;
import com.hdos.platform.common.util.StreamUtils;

/**
 * excel模板生成控制器
 * 
 * @author zhuw
 * @version 1.0
 */
@Controller
@RequestMapping("/component/excelImportTemplate")
public class ExcelImportTemplateController {

	@Autowired
	private ExcelImportTemplateService excelImportTemplateService;
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelImportTemplateController.class);

	/**
	 * 主页面页面初始化
	 * 
	 * @return
	 */
	@RequestMapping("/init")
	public String init() {
		return "base/component/excelimport/excelImportMain";
	}

	/**
	 * 新增页面初始化
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	public String add() {
		return "base/component/excelimport/excelImport";
	}

	/**
	 * 新增页面初始化
	 * 
	 * @return
	 */
	@RequestMapping(value = "/edit/{id}")
	public String edit(@PathVariable("id") String id, Model model) {
		ExcelImportTemplateVO excelImportTemplateVO = excelImportTemplateService.getById(id);
		model.addAttribute("excelImportTemplateVO", excelImportTemplateVO);
		return "base/component/excelimport/excelImport";
	}

	/**
	 * 配置页面初始化
	 * 
	 * @return
	 */
	@RequestMapping("/config")
	public String config() {
		return "base/component/excelimport/excelImportConfig";
	}

	/**
	 * 配置页面初始化
	 * 
	 * @return
	 */
	@RequestMapping("/query/{id}")
	@ResponseBody
	public String query(@PathVariable("id") String id) {
		List<ExcelImportColumnVO> list = excelImportTemplateService.query(id);
		return JSONObject.toJSONString(list);
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping("/table")
	public String table() {
		return "base/component/excelimport/excelImportTable";
	}

	/**
	 * 获取表的字段和备注
	 * 
	 * @return
	 */
	@RequestMapping("/loadtable")
	@ResponseBody
	public String loadTable(@RequestParam String tableName, String[] columnNames,String[] sortNos) {
		List<TableVO> list = excelImportTemplateService.getTable(tableName, columnNames,sortNos);
		return JSONObject.toJSONString(list);
	}

	/**
	 * 分页查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryTemplate")
	@ResponseBody
	public String queryTemplate(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "rows", defaultValue = "20") int pageSize) {

		Map<String, Object> queryCondition = new HashMap<String, Object>();
		Page<ExcelImportTemplateVO> codePage = excelImportTemplateService.findPage(queryCondition, pageNumber,
				pageSize);
		JSONObject rst = new JSONObject();
		rst.put("total", codePage.getTotalElements());
		rst.put("rows", codePage.getContent());
		return rst.toJSONString();
	}

	/**
	 * 生成模板
	 * 
	 * @param tableName
	 * @param templateName
	 * @param rule
	 * @param config
	 * @return
	 */
	@RequestMapping("/createtemplate")
	@ResponseBody
	public String createTemplate(ExcelImportTemplateVO excelImportTemplateVO) {

		try {
			excelImportTemplateService.saveTemplate(excelImportTemplateVO);
		} catch (RuntimeException e) {
			logger.info(e.getMessage());
			return "fail";
		}
		return "success";

	}

	/**
	 * 导入Excel
	 * 
	 * @param file
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String readExcelData(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
		String fileName = file.getOriginalFilename();
		File targetFile = new File(fileName);
		try {
			response.setCharacterEncoding("UTF-8");
			file.transferTo(targetFile);
			return JSONObject.toJSONString(excelImportTemplateService.readExcelData(targetFile, null, null, null));
		} catch (Exception e) {
			logger.info(e.getMessage());
			return e.toString();
		} finally {
			if (targetFile != null && targetFile.exists()) {
				targetFile.delete();
			}
		}

	}

	/**
	 * excel导出
	 * 
	 * @param titles
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/exportExcel")
	public void exportExcel(@RequestParam String[] titles, String[] rows,
			HttpServletResponse response) {

		response.setContentType("application/ostet-stream");
		String fileName = String.valueOf(System.currentTimeMillis() + ".xls");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		InputStream inputStream = null;
		OutputStream os = null;
		File file = null;
		try {
			file = new File(ConfigUtils.get("TEMP_FILE_PATH") + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			excelImportTemplateService.exportExcel(titles, rows, file);
			inputStream = new FileInputStream(file);
			os = response.getOutputStream();
			StreamUtils.copyStream(inputStream, os);

		} catch (FileNotFoundException e) {
			logger.info("FileNotFoundException", e);
		} catch (IOException e) {
			logger.info("IOException", e);
		} catch (WriteException e) {
			logger.info("WriteException", e);
		} catch (ParseException e) {
			logger.info("ParseException", e);
		} finally {
			StreamUtils.close(os);
			StreamUtils.close(inputStream);
			if (file != null && file.exists()) {
				file.delete();
			}
		}
	}

	/**
	 * excel导出DailyBalanceAccount
	 * 
	 * @param titles
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/exportDailyBalanceAccount")
	public void exportDailyBalanceAccount(@RequestParam String[] titles, String[] rows, String sum, String date,
			String tradeAccount, String merchantName, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/ostet-stream");
		String[] merchantNames = merchantName.split(",");
		String header = "";

		for (int i = 0; i < merchantNames.length; i++) {
			header += merchantNames[i];
		}
		String fileName = null;
		String fileNameString = null;
		if (StringUtils.isEmpty(merchantName)) {
			fileName = String.valueOf("商户对账日报" + date + ".xls");
			fileNameString = String.valueOf("商户对账日报" + date + ".xls");
		} else {
			fileName = String.valueOf("商户对账日报" + "【" + header + "】" + date + ".xls");
			fileNameString = String.valueOf("商户对账日报" + "【" + header + "】" + date + ".xls");
		}
		try {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		InputStream inputStream = null;
		OutputStream os = null;
		File file = null;
		try {
			file = new File(ConfigUtils.get("TEMP_FILE_PATH") + fileNameString);
			if (!file.exists()) {
				file.createNewFile();
			}
			excelImportTemplateService.exportDailyBalanceAccount(titles, rows, sum, merchantName, date, tradeAccount,
					file);
			inputStream = new FileInputStream(file);
			os = response.getOutputStream();
			StreamUtils.copyStream(inputStream, os);
		} catch (FileNotFoundException e) {
			logger.info("FileNotFoundException", e);
		} catch (IOException e) {
			logger.info("IOException", e);
		} catch (WriteException e) {
			logger.info("WriteException", e);
		} catch (ParseException e) {
			logger.info("ParseException", e);
		} finally {
			StreamUtils.close(os);
			StreamUtils.close(inputStream);
			if (file != null && file.exists()) {
				file.delete();
			}
		}
	}

	/**
	 * excel导出exportDailyCardConsumeSummary
	 * 
	 * @param titles
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/exportDailyCardConsumeSummary")
	public void exportDailyCardConsumeSummary(@RequestParam String[] titles, String[] rows, String sum,
			String tradeAccount, String tradeBatch, String merchantName, String date, String merchantNameInfo,
			HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/ostet-stream");
		String[] merchantNames = merchantName.split(",");
		String header = "";
		for (int i = 0; i < merchantNames.length; i++) {
			header += merchantNames[i];
		}
		// 为文件名赋值
		String fileName = null;
		String fileNameString = null;
		if (StringUtils.isEmpty(merchantName)) {
			fileName = String.valueOf("卡片消费汇总表" + date + ".xls");
			fileNameString = String.valueOf("卡片消费汇总表" + date + ".xls");
		} else {
			fileName = String.valueOf("卡片消费汇总表" + "【" + header + "】" + ".xls");
			fileNameString = String.valueOf("卡片消费汇总表" + "【" + header + "】" + ".xls");
		}
		try {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		InputStream inputStream = null;
		OutputStream os = null;
		File file = null;
		try {
			file = new File(ConfigUtils.get("TEMP_FILE_PATH") + fileNameString);
			if (!file.exists()) {
				file.createNewFile();
			}
			excelImportTemplateService.exportDailyCardConsumeSummary(titles, rows, sum, tradeAccount, tradeBatch,
					merchantNameInfo, date, file);
			inputStream = new FileInputStream(file);
			os = response.getOutputStream();
			StreamUtils.copyStream(inputStream, os);
		} catch (FileNotFoundException e) {
			logger.info("FileNotFoundException", e);
		} catch (IOException e) {
			logger.info("IOException", e);
		} catch (WriteException e) {
			logger.info("WriteException", e);
		} catch (ParseException e) {
			logger.info("ParseException", e);
		} finally {
			StreamUtils.close(os);
			StreamUtils.close(inputStream);
			if (file != null && file.exists()) {
				file.delete();
			}
		}
	}

	/**
	 * excel导出exportDailyCardPaySummary
	 * 
	 * @param titles
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/exportDailyCardPaySummary")
	public void exportDailyCardPaySummary(@RequestParam String[] titles, String[] rows, String sum,String sumOriginal, String tradeAccount,
			String tradeBatch, String account, String dateStart, String dateEnd, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/ostet-stream");
		String fileName = String.valueOf("一卡通充值&售卡汇总表" + ".xls");
		String fileNameString = String.valueOf("一卡通充值&售卡汇总表" + ".xls");
		try {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		InputStream inputStream = null;
		OutputStream os = null;
		File file = null;
		try {
			file = new File(ConfigUtils.get("TEMP_FILE_PATH") + fileNameString);
			if (!file.exists()) {
				file.createNewFile();
			}
			excelImportTemplateService.exportDailyCardPaySummary(titles, rows, sum, sumOriginal,tradeAccount, tradeBatch, account, dateStart, dateEnd, file);
			inputStream = new FileInputStream(file);
			os = response.getOutputStream();
			StreamUtils.copyStream(inputStream, os);
		} catch (FileNotFoundException e) {
			logger.info("FileNotFoundException", e);
		} catch (IOException e) {
			logger.info("IOException", e);
		} catch (WriteException e) {
			logger.info("WriteException", e);
		} catch (ParseException e) {
			logger.info("ParseException", e);
		} finally {
			StreamUtils.close(os);
			StreamUtils.close(inputStream);
			if (file != null && file.exists()) {
				file.delete();
			}
		}
	}

	private static SecureRandom random = new SecureRandom();

	public static String randomString(int length) {
		String str = new BigInteger(130, random).toString(32);
		return str.substring(0, length);
	}

	/**
	 * Excel模板下载
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/download")
	public void download(String id, String templateName, HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {

		response.setContentType("application/ostet-stream");

		// chrome mozilla/5.0 (windows nt 6.1; win64; x64) applewebkit/537.36
		// (khtml, like gecko) chrome/54.0.2840.71 safari/537.36
		// IE11 mozilla/5.0 (windows nt 6.1; wow64; trident/7.0; rv:11.0) like
		// gecko
		// IE10 mozilla/5.0 (compatible; msie 10.0; windows nt 6.1; wow64;
		// trident/6.0)
		// firefox mozilla/5.0 (windows nt 6.1; wow64; rv:49.0) gecko/20100101
		// firefox/49.0

		String agent = request.getHeader("User-Agent").toLowerCase();
		// if (-1 != agent.indexOf("firefox")) {
		// templateName = "=?UTF-8?B?" + (new
		// String(org.apache.commons.codec.binary.Base64.encodeBase64(templateName.getBytes("UTF-8"))))+
		// "?=";
		// }else if (-1 != agent.indexOf("chrome")) {
		// templateName = new String(templateName.getBytes(), "ISO8859-1");
		// } else {//IE7+
		// templateName = URLEncoder.encode(templateName, "UTF-8");// IE浏览器
		// }

		// if (request.getHeader("User-Agent").toLowerCase().indexOf("msie") >
		// -1) {
		// templateName = URLEncoder.encode(templateName, "UTF-8");// IE浏览器
		// } else {
		// // 先去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,
		// templateName = new String(templateName.replaceAll(" ",
		// "").getBytes("UTF-8"), "ISO8859-1");
		// }

		if (agent.contains("msie") || agent.contains("trident")) {
			templateName = URLEncoder.encode(templateName, "UTF-8");
		} else {
			// 非IE浏览器的处理：
			templateName = new String(templateName.getBytes("UTF-8"), "ISO-8859-1");
		}

		response.addHeader("Content-Disposition", "attachment;filename=" + templateName + ".xls");
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setCharacterEncoding("UTF-8");

		InputStream inputStream = null;
		OutputStream os = null;
		File file = null;
		try {
			file = new File(ConfigUtils.get("TEMP_FILE_PATH") + templateName + ".xls");
			if (!file.exists()) {
				file.createNewFile();
			}
			excelImportTemplateService.createExcelTemplate(id, file);
			inputStream = new FileInputStream(file);
			os = response.getOutputStream();

			StreamUtils.copyStream(inputStream, os);

		} catch (FileNotFoundException e) {
			logger.info("FileNotFoundException", e);
		} catch (IOException e) {
			logger.info("IOException", e);
		} finally {
			StreamUtils.close(os);
			StreamUtils.close(inputStream);
			if (file != null && file.exists()) {
				file.delete();
			}
		}
	}

	/**
	 * 删除模板
	 * 
	 * @param roleIds
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String deleteTemplate(String ids) {
		String[] id = ids.split(",");
		excelImportTemplateService.deleteTemplate(id);
		return "success";
	}

	/**
	 * 下拉框
	 * 
	 * @param departmentVO
	 * @return
	 */
	@RequestMapping(value = "/getcombobox", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getCombobox(Model model) {

		return JSONObject.toJSONString(excelImportTemplateService.getCombobox());
	}

	/**
	 * excel导出日汇总
	 * 
	 * @param titles
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/exportDailyExcel")
	public void exportDailyExcel(@RequestParam String[] titles, String[] rows, int merchantcount, int amount,
			double sum, HttpServletResponse response) {

		response.setContentType("application/ostet-stream");
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = "一卡通商户结算日报表" + formate.format(new Date()) + ".xls";
		String fileNameString = "一卡通商户结算日报表" + formate.format(new Date()) + ".xls";
		try {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.info(e.getMessage());
		}
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		InputStream inputStream = null;
		OutputStream os = null;
		File file = null;
		try {
			file = new File(ConfigUtils.get("TEMP_FILE_PATH") + fileNameString);
			if (!file.exists()) {
				file.createNewFile();
			}
			excelImportTemplateService.exportDailyExcel(titles, rows, file, merchantcount, amount, sum);
			inputStream = new FileInputStream(file);
			os = response.getOutputStream();
			StreamUtils.copyStream(inputStream, os);

		} catch (FileNotFoundException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		} catch (WriteException e) {
			logger.info(e.getMessage());
		} catch (ParseException e) {
			logger.info(e.getMessage());
		} finally {
			StreamUtils.close(os);
			StreamUtils.close(inputStream);
			if (file != null && file.exists()) {
				file.delete();
			}
		}
	}

	/**
	 * excel导出一卡通储值消费月报表
	 * 
	 * @param titles
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/exportMonthlyExcel")
	public void exportMonthlyExcel(@RequestParam String[] titles, String[] rows, String[] foot,
			HttpServletResponse response) {

		response.setContentType("application/ostet-stream");
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = "一卡通储值消费月结算报表" + formate.format(new Date()) + ".xls";
		String fileNameString = "一卡通储值消费月结算报表" + formate.format(new Date()) + ".xls";
		try {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.info(e.getMessage());
		}
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		InputStream inputStream = null;
		OutputStream os = null;
		File file = null;
		try {
			file = new File(ConfigUtils.get("TEMP_FILE_PATH") + fileNameString);
			if (!file.exists()) {
				file.createNewFile();
			}
			excelImportTemplateService.exportMonthlyExcel(titles, rows, foot, file);
			inputStream = new FileInputStream(file);
			os = response.getOutputStream();
			StreamUtils.copyStream(inputStream, os);

		} catch (FileNotFoundException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		} catch (WriteException e) {
			logger.info(e.getMessage());
		} catch (ParseException e) {
			logger.info(e.getMessage());
		} finally {
			StreamUtils.close(os);
			StreamUtils.close(inputStream);
			if (file != null && file.exists()) {
				file.delete();
			}
		}
	}

	/**
	 * 实体卡确认导出excel
	 * 
	 * @param titles
	 * @param rows
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/exportConfirmExcel")
	public void exportConfirmExcel(@RequestParam String[] titles, String[] rows, HttpServletResponse response) {
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
		response.setContentType("application/ostet-stream");
		String fileName = "实体卡确认清单" + formate.format(new Date()) + ".xls";
		String fileNameString = "实体卡确认清单" + formate.format(new Date()) + ".xls";
		try {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.info(e.getMessage());
		}
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		InputStream inputStream = null;
		OutputStream os = null;
		File file = null;
		try {
			file = new File(ConfigUtils.get("TEMP_FILE_PATH") + fileNameString);
			if (!file.exists()) {
				file.createNewFile();
			}
			excelImportTemplateService.exportConfirmExcel(titles, rows, file);
			inputStream = new FileInputStream(file);
			os = response.getOutputStream();
			StreamUtils.copyStream(inputStream, os);

		} catch (FileNotFoundException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		} catch (WriteException e) {
			logger.info(e.getMessage());
		} catch (ParseException e) {
			logger.info(e.getMessage());
		} finally {
			StreamUtils.close(os);
			StreamUtils.close(inputStream);
			if (file != null && file.exists()) {
				file.delete();
			}
		}
	}

}
