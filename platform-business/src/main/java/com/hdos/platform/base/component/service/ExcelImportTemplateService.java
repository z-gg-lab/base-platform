package com.hdos.platform.base.component.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.hdos.platform.base.config.model.ConfigVO;
import com.hdos.platform.common.util.ConfigUtils;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

@Service
@Transactional
public class ExcelImportTemplateService extends BaseExcelImportTemplateService {

	private static final Logger logger = LoggerFactory.getLogger(ExcelImportTemplateService.class);
	
	private SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
	
	public void exportDailyBalanceAccount(String[] titles, String[] rows, String sum, String merchantName, String date,
			String tradeAccount, File file) throws IOException, WriteException, ParseException {
		WritableWorkbook book = Workbook.createWorkbook(file);
		// 生成第一页
		WritableSheet sheet = book.createSheet(file.getName(), 0);
		// 显示整个sheet中的网格线
		sheet.getSettings().setShowGridLines(true);
		CellView navCellView = new CellView();
		WritableCellFormat wcf = new WritableCellFormat(NumberFormats.TEXT);
		navCellView.setFormat(wcf);
		// 设置表头的高度
		sheet.setRowView(0, 600, false);
		// 设置每一列的单元格格式
		sheet.setColumnView(0, 8);
		sheet.setColumnView(1, 30);
		sheet.setColumnView(2, 30);
		sheet.setColumnView(3, 22);
		sheet.setColumnView(4, 18);
		sheet.setColumnView(5, 20);
		sheet.addCell(new Label(0, 0, "", getHeaderCellStyle()));
		WritableFont wf = new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE);
		WritableCellFormat wcfTitle = new WritableCellFormat(NumberFormats.TEXT); // 单元格定义
		wcfTitle.setFont(wf);
		wcfTitle.setAlignment(jxl.format.Alignment.CENTRE); // 设置对齐方式
		wcfTitle.setVerticalAlignment(jxl.format.VerticalAlignment.BOTTOM);
		wcfTitle.setBackground(jxl.format.Colour.LIME);
		wcfTitle.setWrap(true);
		wcfTitle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		if(StringUtils.isEmpty(merchantName)){
			sheet.addCell(new Label(1, 0, date +"商户日对账表", wcfTitle));
		}else{
			sheet.addCell(new Label(1, 0, "【" + merchantName + "】" + date + "日对账表", wcfTitle));
		}
		sheet.addCell(new Label(2, 0, "", getHeaderCellStyle()));
		sheet.addCell(new Label(3, 0, "", getHeaderCellStyle()));
		sheet.addCell(new Label(4, 0, "", getHeaderCellStyle()));
		sheet.addCell(new Label(5, 0, "", getHeaderCellStyle()));
		sheet.mergeCells(1, 0, 5, 0); // 合并单元格
		WritableFont font = new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE);
		WritableCellFormat headerFormat = new WritableCellFormat(NumberFormats.TEXT);
		WritableCellFormat amountFormat = new WritableCellFormat(NumberFormats.TEXT);
		amountFormat.setAlignment(jxl.format.Alignment.RIGHT);
		headerFormat.setFont(font);
		for (int i = 0; i < titles.length; i++) {
			sheet.addCell(new Label(i, 1, titles[i], headerFormat));
		}
		int row = 2;
		int column = 0;
		int count = 0;
		int rowsLength = rows.length;
		for (int i = 0; i < rows.length; i++) {
			count++;
			if(count == 6){
				sheet.addCell(new Label(column++, row, rows[i],amountFormat));
				count = 0;
			}else{
				sheet.addCell(new Label(column++, row, rows[i]));
			}
			rowsLength--;
			
			// 换行
			if (rowsLength % titles.length == 0) {
				column = 0;
				row++;
			}
		}
		sheet.addCell(new Label(0, row, "总计", getHeaderCellStyle()));
		sheet.addCell(new Label(1, row, "/", getHeaderCellStyle()));
		sheet.addCell(new Label(2, row, "/", getHeaderCellStyle()));
		sheet.addCell(new Label(3, row, "/", getHeaderCellStyle()));
		sheet.addCell(new Label(4, row, tradeAccount, getHeaderCellStyle()));
		WritableCellFormat amountSum = getHeaderCellStyle();
		amountSum.setAlignment(jxl.format.Alignment.RIGHT);
		sheet.addCell(new Label(5, row, sum, amountSum));
		row += 2;
		WritableFont wfend = new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.RED);
		WritableCellFormat wcfEnd = new WritableCellFormat(NumberFormats.TEXT); // 单元格定义
		sheet.setRowView(row, 1000, false);// 设置表尾高度
		wcfEnd.setFont(wfend);
		wcfEnd.setAlignment(jxl.format.Alignment.LEFT); // 设置对齐方式
		wcfEnd.setVerticalAlignment(jxl.format.VerticalAlignment.BOTTOM);
		wcfEnd.setWrap(true);
		wcfEnd.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		sheet.addCell(new Label(0, row, "说明：1.该统计表为商户当日所有交易明细列表；2.数据行按照交易流水顺序进行排序；3.总计栏：卡号栏为交易笔数计数，交易金额为各笔交易的金额总计求和。",
				wcfEnd));
		sheet.mergeCells(0, row, 5, row); // 合并单元格
		book.write();
		book.close();
	}

	/**
	 * 设置表头样式
	 * 
	 * @return
	 */
	private static WritableCellFormat getHeaderCellStyle() {
		WritableFont font = new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE);

		WritableCellFormat headerFormat = new WritableCellFormat(NumberFormats.TEXT);

		try {
			// 添加字体设置
			headerFormat.setFont(font);
			// 设置单元格背景色
			headerFormat.setBackground(jxl.format.Colour.LIME);// 翡翠绿
			// 设置表头表格边框样式
			headerFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框
		} catch (WriteException e) {
			logger.info(e.getMessage());
		}
		return headerFormat;
	}

	/**
	 * 抽离出WritableSheet方法
	 * 
	 * @param excelName
	 * @return
	 * @throws IOException
	 * @throws RowsExceededException
	 */
	/*private WritableSheet getExcelSheet(WritableWorkbook book, String excelName, int length)
			throws IOException, RowsExceededException {
		WritableSheet sheet = book.createSheet(excelName, 0);
		// 显示整个sheet中的网格线
		sheet.getSettings().setShowGridLines(true);
		CellView navCellView = new CellView();
		navCellView.setAutosize(true); // 设置自动大小
		navCellView.setSize(30);
		WritableCellFormat wcf = new WritableCellFormat(NumberFormats.TEXT);
		navCellView.setFormat(wcf);
		// 设置表头的高度
		sheet.setRowView(0, 500, false);
		// 设置每一列的单元格格式
		for (int i = 0; i < length; i++) {
			sheet.setColumnView(i, navCellView);
		}

		return sheet;

	}*/

	/**
	 * 生成一卡通售卡&充值日统计表
	 * 
	 * @param titles
	 * @param rows
	 * @param sum
	 * @param tradeAccount
	 * @param tradeBatch
	 * @param account
	 * @param date
	 * @param file
	 */
	public void exportDailyCardPaySummary(String[] titles, String[] rows,String sum, String sumOriginal, String tradeAccount,
			String tradeBatch, String account, String dateStart,String dateEnd, File file)
			throws IOException, WriteException, ParseException {
		WritableWorkbook book = Workbook.createWorkbook(file);
		// 生成第一页
		WritableSheet sheet = book.createSheet(file.getName(), 0);
		// 显示整个sheet中的网格线
		sheet.getSettings().setShowGridLines(true);
		CellView navCellView = new CellView();
		WritableCellFormat wcf = new WritableCellFormat(NumberFormats.TEXT);
		navCellView.setFormat(wcf);
		// 设置表头的高度
		sheet.setRowView(0, 600, false);
		// 设置每一列的单元格格式
		sheet.setColumnView(0, 8);
		sheet.setColumnView(1, 22);
		sheet.setColumnView(2, 30);
		sheet.setColumnView(3, 19);
		sheet.setColumnView(4, 19);
		sheet.setColumnView(5, 15);
		sheet.setColumnView(6, 20);
		sheet.setColumnView(7, 15);
		sheet.setColumnView(8, 10);
		sheet.setColumnView(9, 15);
		sheet.setColumnView(10, 15);
		WritableFont wf = new WritableFont(WritableFont.createFont("微软雅黑"), 20, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE);
		WritableCellFormat wcfTitle = new WritableCellFormat(NumberFormats.TEXT); // 单元格定义
		wcfTitle.setFont(wf);
		wcfTitle.setAlignment(jxl.format.Alignment.CENTRE); // 设置对齐方式
		wcfTitle.setVerticalAlignment(jxl.format.VerticalAlignment.BOTTOM);
		wcfTitle.setWrap(true);
		wcfTitle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		sheet.addCell(new Label(0, 0, "一卡通充值&售卡汇总表", wcfTitle));
		sheet.addCell(new Label(1, 0, "", wcfTitle));
		sheet.addCell(new Label(2, 0, "", wcfTitle));
		sheet.addCell(new Label(3, 0, "", wcfTitle));
		sheet.addCell(new Label(4, 0, "", wcfTitle));
		sheet.addCell(new Label(5, 0, "", wcfTitle));
		sheet.addCell(new Label(6, 0, "", wcfTitle));
		sheet.addCell(new Label(7, 0, "", wcfTitle));
		sheet.mergeCells(0, 0, 7, 0); // 合并单元格
		sheet.addCell(new Label(8, 0, dateStart+"/"+dateEnd, getBodyCellStyle()));
		sheet.addCell(new Label(9, 0, "", wcfTitle));
		sheet.addCell(new Label(10, 0, "", wcfTitle));
		sheet.mergeCells(8, 0, 10, 0); // 合并单元格

		WritableFont font = new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE);
		WritableCellFormat headerFormat = new WritableCellFormat(NumberFormats.TEXT);
		WritableCellFormat amountFormat = new WritableCellFormat(NumberFormats.TEXT);
		amountFormat.setAlignment(jxl.format.Alignment.RIGHT);
		headerFormat.setFont(font);
		for (int i = 0; i < titles.length; i++) {
			sheet.addCell(new Label(i, 1, titles[i], headerFormat));
		}
		int row = 2;
		int column = 0;
		int count = 0;
		int rowsLength = rows.length;
		for (int i = 0; i < rows.length; i++) {
			count++;
			if(count == 10 || count == 8 || count == 9){
				sheet.addCell(new Label(column++, row, rows[i],amountFormat));
			}else{
				sheet.addCell(new Label(column++, row, rows[i]));
			}
			rowsLength--;
			// 换行
			if (rowsLength % titles.length == 0) {
				column = 0;
				row++;
				count = 0;
			}
		}
		sheet.addCell(new Label(0, row, "总计", getBodyCellStyle()));
		sheet.addCell(new Label(1, row, "/", getBodyCellStyle()));
		sheet.addCell(new Label(2, row, tradeAccount, getBodyCellStyle()));
		sheet.addCell(new Label(3, row, tradeBatch, getBodyCellStyle()));
		sheet.addCell(new Label(4, row, "/", getBodyCellStyle()));
		sheet.addCell(new Label(5, row, "/", getBodyCellStyle()));
		WritableCellFormat amountSum = getHeaderCellStyle();
		amountSum.setAlignment(jxl.format.Alignment.RIGHT);
		sheet.addCell(new Label(6, row, account, getBodyCellStyle()));
		sheet.addCell(new Label(7, row, sumOriginal, amountSum));
		sheet.addCell(new Label(8, row, "/", getBodyCellStyle()));
		sheet.addCell(new Label(9, row, sum, amountSum));
		sheet.addCell(new Label(10, row, "/", getBodyCellStyle()));
		row += 2;
		// 设置结尾格式
		WritableFont wfend = new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.RED);
		WritableCellFormat wcfEnd = new WritableCellFormat(NumberFormats.TEXT); // 单元格定义
		sheet.setRowView(row, 1000, false);// 设置表尾高度
		wcfEnd.setFont(wfend);
		wcfEnd.setAlignment(jxl.format.Alignment.LEFT); // 设置对齐方式
		wcfEnd.setVerticalAlignment(jxl.format.VerticalAlignment.BOTTOM);
		wcfEnd.setWrap(true);
		wcfEnd.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		sheet.addCell(new Label(0, row,
				"合计栏说明：1.卡号列：统计当日有充值操作的卡片数量，同一张卡多次充值只记一次；2.交易流水号：统计总交易笔数；3.充值账号：统计当日有充值操作的账号，同一账号多次充值只记一次；4.实际金额：所有充值交易的实际金额总和；5.充值金额：所有充值交易的充值金额总和；",
				wcfEnd));
		sheet.mergeCells(0, row, 10, row); // 合并单元格
		book.write();
		book.close();

	}

	/**
	 * 设置普表样式
	 * 
	 * @return
	 */
	private static WritableCellFormat getBodyCellStyle() {
		WritableFont font = new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE);

		WritableCellFormat headerFormat = new WritableCellFormat(NumberFormats.TEXT);

		try {
			// 添加字体设置
			headerFormat.setFont(font);
			// 设置表头表格边框样式
			headerFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框
		} catch (WriteException e) {
			logger.info(e.getMessage());
		}
		return headerFormat;
	}

	/**
	 * 生成卡片消费日汇总表excel文件
	 * 
	 * @param titles
	 * @param rows
	 * @param sum
	 * @param tradeAccount
	 * @param tradeBatch
	 * @param merchant
	 * @param date
	 * @param file
	 */

	public void exportDailyCardConsumeSummary(String[] titles, String[] rows, String sum, String tradeAccount,
			String tradeBatch, String merchantName, String date, File file)
			throws IOException, WriteException, ParseException {
		WritableWorkbook book = Workbook.createWorkbook(file);
		// 生成第一页
		WritableSheet sheet = book.createSheet(file.getName(), 0);
		// 显示整个sheet中的网格线
		sheet.getSettings().setShowGridLines(true);
		CellView navCellView = new CellView();
		WritableCellFormat wcf = new WritableCellFormat(NumberFormats.TEXT);
		navCellView.setFormat(wcf);
		// 设置表头的高度
		sheet.setRowView(0, 600, false);
		// 设置每一列的单元格格式
		sheet.setColumnView(0, 8);
		sheet.setColumnView(1, 22);
		sheet.setColumnView(2, 30);
		sheet.setColumnView(3, 19);
		sheet.setColumnView(4, 20);
		sheet.setColumnView(5, 15);
		sheet.setColumnView(6, 15);
		WritableFont wf = new WritableFont(WritableFont.createFont("微软雅黑"), 20, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE);
		WritableCellFormat wcfTitle = new WritableCellFormat(NumberFormats.TEXT); // 单元格定义
		wcfTitle.setFont(wf);
		wcfTitle.setAlignment(jxl.format.Alignment.CENTRE); // 设置对齐方式
		wcfTitle.setVerticalAlignment(jxl.format.VerticalAlignment.BOTTOM);
		wcfTitle.setWrap(true);
		wcfTitle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		sheet.addCell(new Label(0, 0, "加油一卡通消费日报表", wcfTitle));
		sheet.addCell(new Label(1, 0, "", wcfTitle));
		sheet.addCell(new Label(2, 0, "", wcfTitle));
		sheet.addCell(new Label(3, 0, "", wcfTitle));
		sheet.addCell(new Label(4, 0, "", wcfTitle));
		sheet.mergeCells(0, 0, 4, 0); // 合并单元格
		sheet.addCell(new Label(5, 0, date, getBodyCellStyle()));
		sheet.addCell(new Label(6, 0, "", wcfTitle));
		sheet.mergeCells(5, 0, 6, 0); // 合并单元格

		WritableFont font = new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE);
		WritableCellFormat headerFormat = new WritableCellFormat(NumberFormats.TEXT);
		WritableCellFormat amountFormat = new WritableCellFormat(NumberFormats.TEXT);
		amountFormat.setAlignment(jxl.format.Alignment.RIGHT);
		headerFormat.setFont(font);
		for (int i = 0; i < titles.length; i++) {
			sheet.addCell(new Label(i, 1, titles[i], headerFormat));
		}
		int row = 2;
		int column = 0;
		int count = 0;
		int rowsLength = rows.length;
		for (int i = 0; i < rows.length; i++) {
			count++;
			if(count == 6){
				sheet.addCell(new Label(column++, row, rows[i],amountFormat));
				count = -1;
			}else{
				sheet.addCell(new Label(column++, row, rows[i]));
			}
			rowsLength--;
			// 换行
			if (rowsLength % titles.length == 0) {
				column = 0;
				row++;
			}
		}
		sheet.addCell(new Label(0, row, "总计", getBodyCellStyle()));
		sheet.addCell(new Label(1, row, tradeAccount, getBodyCellStyle()));
		sheet.addCell(new Label(2, row, tradeBatch, getBodyCellStyle()));
		sheet.addCell(new Label(3, row, "/", getBodyCellStyle()));
		sheet.addCell(new Label(4, row, merchantName, getBodyCellStyle()));
		WritableCellFormat amountSum = getHeaderCellStyle();
		amountSum.setAlignment(jxl.format.Alignment.RIGHT);
		sheet.addCell(new Label(5, row, sum, amountSum));
		sheet.addCell(new Label(6, row, "/", getBodyCellStyle()));
		row += 2;
		// 设置结尾格式
		WritableFont wfend = new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.RED);
		WritableCellFormat wcfEnd = new WritableCellFormat(NumberFormats.TEXT); // 单元格定义
		sheet.setRowView(row, 1000, false);// 设置表尾高度
		wcfEnd.setFont(wfend);
		wcfEnd.setAlignment(jxl.format.Alignment.LEFT); // 设置对齐方式
		wcfEnd.setVerticalAlignment(jxl.format.VerticalAlignment.BOTTOM);
		wcfEnd.setWrap(true);
		wcfEnd.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		sheet.addCell(new Label(0, row,
				"合计栏说明：1.卡号列：统计当日有消费交易的卡片数量，同一张卡多次消费本列只记一次；2.交易流水号：统计总交易笔数；3.商户名称：统计当日有消费交易的商户数量，同一商户多次消费只记一次；4.金额：所有消费交易的金额总和；",
				wcfEnd));
		sheet.mergeCells(0, row, 6, row); // 合并单元格
		book.write();
		book.close();

	}

	public void exportMonthlyExcel(String[] titles, String[] rows,String[] foot, File file) throws IOException, WriteException, ParseException {
		WritableWorkbook book = Workbook.createWorkbook(file);
		// 生成第一页
		WritableSheet sheet = book.createSheet(file.getName(), 0);
		// 显示整个sheet中的网格线
		sheet.getSettings().setShowGridLines(true);
		CellView navCellView = new CellView();
		WritableCellFormat wcf = new WritableCellFormat(NumberFormats.TEXT);
		navCellView.setFormat(wcf);
		// 设置表头的高度
		sheet.setRowView(0, 600, false);
		// 设置每一列的单元格格式
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		sheet.setColumnView(2, 20);
		sheet.setColumnView(3, 20);
		sheet.setColumnView(4, 20);
		sheet.setColumnView(5, 20);
		sheet.setColumnView(6, 20);
		sheet.setColumnView(7, 20);
		for (int i = 0; i < titles.length; i++) {
			sheet.addCell(new Label(i, 0, "", getDailyHeaderCellStyle(18,0, 2,2)));
		}

		Date data = new Date();
		
		String currentTime = formate.format(data);
		sheet.mergeCells(0, 0, 5, 0); // 合并单元格
		sheet.addCell(new Label(0, 0, "一卡通储值消费月结算报表", getDailyHeaderCellStyle(18,0, 2,2)));
		sheet.mergeCells(6, 0, 7, 0); // 合并单元格
		sheet.addCell(new Label(6, 0, "日期：" + currentTime, getDailyHeaderCellStyle(10,0, 1,2)));

		for (int i = 0; i < titles.length; i++) {
			if (i == 0 || i == 1 || i == 6 || i == 7) {
				sheet.mergeCells(i, 1, i, 2);
				if(i ==1 || i==6){
					sheet.addCell(new Label(i, 1, titles[i], getDailyHeaderCellStyle(12,0, 2,3)));
				}else{
					sheet.addCell(new Label(i, 1, titles[i], getDailyHeaderCellStyle(12, 0,2,2)));
				}
			} else {
				if (i == 2) {
					sheet.mergeCells(i, 1, i + 1, 1);
					sheet.addCell(new Label(i, 1, "储值卡售卡/充值", getDailyHeaderCellStyle(12,0, 2,2)));
					sheet.addCell(new Label(i, 2, titles[i], getDailyHeaderCellStyle(12, 0,2,3)));
				}
				if (i == 3 || i == 5) {
					sheet.addCell(new Label(i, 2, titles[i], getDailyHeaderCellStyle(12,0, 2,3)));
				}
				if (i == 4) {
					sheet.mergeCells(i, 1, i + 1, 1);
					sheet.addCell(new Label(i, 1, "储值卡消费", getDailyHeaderCellStyle(12,0, 2,2)));
					sheet.addCell(new Label(i, 2, titles[i], getDailyHeaderCellStyle(12,0, 2,3)));
				}
			}
		}
		int row = 3;
		int column = 0;
		int rowsLength = rows.length;
		for (int i = 0; i < rows.length; i++) {
			if("0".equals(rows[i])){
				rows[i] ="";
			}
			if(i%8 ==0 || i%8 ==7){
				sheet.addCell(new Label(column++, row, rows[i], getDailyHeaderCellStyle(10,0, 1,2)));
			}else{
				sheet.addCell(new Label(column++, row, rows[i], getDailyHeaderCellStyle(10,0, 1,3)));
			}
			rowsLength--;
			// 换行
			if (rowsLength % titles.length == 0) {
				column = 0;
				row++;
			}
		}
		for(int i=0;i<foot.length;i++){
			if("-1".equals(foot[i])){
				foot[i] ="0";
			}
		}
		sheet.addCell(new Label(0, row, "总计", getDailyHeaderCellStyle(10,0, 2,2)));
		sheet.addCell(new Label(1, row, foot[1], getDailyHeaderCellStyle(10, 0,2,3)));
		sheet.addCell(new Label(2, row, foot[2], getDailyHeaderCellStyle(10, 0,2,3)));
		sheet.addCell(new Label(3, row, foot[3], getDailyHeaderCellStyle(10,0, 2,3)));
		sheet.addCell(new Label(4, row, foot[4], getDailyHeaderCellStyle(10,0, 2,3)));
		sheet.addCell(new Label(5, row, foot[5], getDailyHeaderCellStyle(10,0, 2,3)));
		sheet.addCell(new Label(6, row, foot[6], getDailyHeaderCellStyle(10,0, 2,3)));
		row++;
		WritableFont wfend = new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.RED);
		WritableCellFormat wcfEnd = new WritableCellFormat(NumberFormats.TEXT); // 单元格定义
		sheet.setRowView(row, 1000, false);// 设置表尾高度
		wcfEnd.setFont(wfend);
		wcfEnd.setAlignment(jxl.format.Alignment.LEFT); // 设置对齐方式
		wcfEnd.setVerticalAlignment(jxl.format.VerticalAlignment.BOTTOM);
		wcfEnd.setWrap(true);
		wcfEnd.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		sheet.addCell(new Label(0, row,
				"说明：1.期初余额=前一自然日的【期末余额】2.期末余额=期初余额+售卡充值金额-消费金额3.汇总栏：期初余额=本月第一日期初余额；期末余额=本月最后一日期末余额；充值数量，金额，消费数量，金额为本月所有自然日的累计值。若汇总栏：期末余额≠期初余额+充值金额汇总-消费金额汇总，则说明资金存在问题",
				wcfEnd));
		sheet.mergeCells(0, row, 7, row); // 合并单元格
		
		book.write();
		book.close();
	}

	/**
	 * 
	 * @param titles
	 * @param rows
	 * @param file
	 * @throws IOException
	 * @throws WriteException
	 * @throws ParseException
	 */
	public void exportDailyExcel(String[] titles, String[] rows, File file, int merchantcount, int amount, double sum)
			throws IOException, WriteException, ParseException {

		WritableWorkbook book = Workbook.createWorkbook(file);
		// 生成第一页
		WritableSheet sheet = book.createSheet(file.getName(), 0);
		// 显示整个sheet中的网格线
		sheet.getSettings().setShowGridLines(true);
		CellView navCellView = new CellView();
		WritableCellFormat wcf = new WritableCellFormat(NumberFormats.TEXT);
		navCellView.setFormat(wcf);
		// 设置表头的高度
		sheet.setRowView(0, 600, false);
		// 设置每一列的单元格格式
		sheet.setColumnView(0, 8);
		sheet.setColumnView(1, 30);
		sheet.setColumnView(2, 22);
		sheet.setColumnView(3, 18);
		sheet.setColumnView(4, 20);
		sheet.setColumnView(5, 20);
		sheet.setColumnView(6, 20);
		sheet.setColumnView(7, 20);
		sheet.setColumnView(8, 10);
		for (int i = 0; i < titles.length; i++) {
			if(i==7 || i==8){
				sheet.addCell(new Label(i, 0, "", getDailyHeaderCellStyle(18, 0,2,3)));
			}else{
				sheet.addCell(new Label(i, 0, "", getDailyHeaderCellStyle(18,0, 2,2)));
			}
		}

		Date data = new Date();
		String currentTime = formate.format(data);
		sheet.mergeCells(0, 0, 6, 0); // 合并单元格
		sheet.addCell(new Label(0, 0, "加油一卡通消费结算日报表", getDailyHeaderCellStyle(18,0, 2,2)));
		sheet.mergeCells(7, 0, 8, 0); // 合并单元格
		sheet.addCell(new Label(7, 0, "日期：" + currentTime, getDailyHeaderCellStyle(10, 0,1,2)));

		for (int i = 0; i < titles.length; i++) {
			 
			if (i == 0 || i == 1 || i == 2) {
				sheet.mergeCells(i, 1, i, 2);
				sheet.addCell(new Label(i, 1, titles[i], getDailyHeaderCellStyle(12, 0,2,2)));
			} else {
				if (i == 3) {
					sheet.mergeCells(i, 1, i + 3, 1);
					sheet.addCell(new Label(i, 1, "对公账户", getDailyHeaderCellStyle(12,0, 2,2)));
				}
				if (i == 7) {
					sheet.mergeCells(i, 1, i + 1, 1);
					sheet.addCell(new Label(i, 1, "消费结算", getDailyHeaderCellStyle(12,0, 2,2)));
				}
				if(i==7 || i==8){
					sheet.addCell(new Label(i, 2, titles[i], getDailyHeaderCellStyle(12,0, 2,3)));
				}else{
					sheet.addCell(new Label(i, 2, titles[i], getDailyHeaderCellStyle(12,0, 2,2)));
				}
				
			}
		}
		int row = 3;
		int column = 0;
		int rowsLength = rows.length;
		for (int i = 0; i < rows.length; i++) {
			if(i% 9 ==7 || i%9 ==8 ){
				sheet.addCell(new Label(column++, row, rows[i], getDailyHeaderCellStyle(10,0, 1,3)));
			}else{
				sheet.addCell(new Label(column++, row, rows[i], getDailyHeaderCellStyle(10, 0,1,2)));
			}
			rowsLength--;
			// 换行
			if (rowsLength % titles.length == 0) {
				column = 0;
				row++;
			}
		}

		sheet.addCell(new Label(0, row, "总计", getDailyHeaderCellStyle(10,0, 2,2)));
		sheet.addCell(new Label(1, row, String.valueOf(merchantcount), getDailyHeaderCellStyle(10,0, 2,2)));
		sheet.addCell(new Label(2, row, "/", getDailyHeaderCellStyle(10,0, 2,2)));
		sheet.addCell(new Label(3, row, "/", getDailyHeaderCellStyle(10,0, 2,2)));
		sheet.addCell(new Label(4, row, "/", getDailyHeaderCellStyle(10,0, 2,2)));
		sheet.addCell(new Label(5, row, "/", getDailyHeaderCellStyle(10,0, 2,2)));
		sheet.addCell(new Label(6, row, "/", getDailyHeaderCellStyle(10,0, 2,2)));
		sheet.addCell(new Label(7, row, String.valueOf(amount), getDailyHeaderCellStyle(10, 0,2,3)));
		sheet.addCell(new Label(8, row, String.valueOf(sum), getDailyHeaderCellStyle(10, 0,2,3)));
		row++;
		WritableFont wfend = new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.RED);
		WritableCellFormat wcfEnd = new WritableCellFormat(NumberFormats.TEXT); // 单元格定义
		sheet.setRowView(row, 1000, false);// 设置表尾高度
		wcfEnd.setFont(wfend);
		wcfEnd.setAlignment(jxl.format.Alignment.LEFT); // 设置对齐方式
		wcfEnd.setVerticalAlignment(jxl.format.VerticalAlignment.BOTTOM);
		wcfEnd.setWrap(true);
		wcfEnd.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		sheet.addCell(new Label(0, row,
				"说明：1.该统计表只统计当日有一卡通交易的商户，无一卡通交易的不列入结算表；2.商户对公账户：取自商户信息注册登记时填写的账户；3.合计栏：商户名称栏统计有交易的商户数量；消费数量为表中商户的交易数量求和；金额为各商户的交易金额求和。4.该统计表为平台与商户的日结算表，为平台申请批量付款或财务付款的最终数据，需进行审核和签字核对。",
				wcfEnd));
		sheet.mergeCells(0, row, 8, row); // 合并单元格
		book.write();
		book.close();
	}

	private static WritableCellFormat getDailyHeaderCellStyle(int size,int color, int type,int align) {
		// 2表示粗体 1表示细体
		WritableFont font;
		if (type == 1) {
			font = new WritableFont(WritableFont.createFont("微软雅黑"), size, WritableFont.NO_BOLD, false,
					UnderlineStyle.NO_UNDERLINE);
		} else {
			font = new WritableFont(WritableFont.createFont("微软雅黑"), size, WritableFont.BOLD, false,
					UnderlineStyle.NO_UNDERLINE);
		}
		WritableCellFormat headerFormat = new WritableCellFormat(NumberFormats.TEXT);
		
		try {
			if(color !=0){
				font.setColour(Colour.RED);
			}
			// 添加字体设置
			headerFormat.setFont(font);
		 
			if(align == 1){
				headerFormat.setAlignment(Alignment.LEFT);
			}else if(align ==2){
				headerFormat.setAlignment(Alignment.CENTRE);
			}else{
				headerFormat.setAlignment(Alignment.RIGHT);
			}
			
		} catch (WriteException e) {
			logger.info(e.getMessage());
		}
		return headerFormat;
	}


	 
	public void exportConfirmExcel(String[] titles, String[] rows, File file) throws IOException, WriteException, ParseException {
		WritableWorkbook book = Workbook.createWorkbook(file);
		// 生成第一页
		WritableSheet sheet = book.createSheet(file.getName(), 0);
		// 显示整个sheet中的网格线
		sheet.getSettings().setShowGridLines(true);
		CellView navCellView = new CellView();
		WritableCellFormat wcf = new WritableCellFormat(NumberFormats.TEXT);
		navCellView.setFormat(wcf);
		// 设置表头的高度
		sheet.setRowView(0, 600, false);
		// 设置每一列的单元格格式
		sheet.setColumnView(0, 30);
		sheet.setColumnView(1, 10);
		sheet.setColumnView(2, 10);
		sheet.setColumnView(3, 20);
		sheet.setColumnView(4, 15);
		sheet.setColumnView(5, 15);
		sheet.setColumnView(6, 40);
		for (int i = 0; i < titles.length; i++) {
			sheet.addCell(new Label(i, 0, "", getDailyHeaderCellStyle(18,0, 2,2)));
		}

		Date data = new Date();
		String currentTime = formate.format(data);
		sheet.mergeCells(0, 0, 5, 0); // 合并单元格
		sheet.addCell(new Label(0, 0, "实体卡确认清单", getDailyHeaderCellStyle(18,0, 2,2)));
		sheet.mergeCells(6, 0, 6, 0); // 合并单元格
		sheet.addCell(new Label(6, 0, "日期：" + currentTime, getDailyHeaderCellStyle(10,0, 1,2)));

		for (int i = 0; i < titles.length; i++) {
			sheet.addCell(new Label(i, 1, titles[i], getDailyHeaderCellStyle(12,0, 2,2)));
		}
		int row = 2;
		int column = 0;
		int rowsLength = rows.length;
		/*for (int i = 0; i < rows.length; i++) {
			if(i%7 == 6){
				if("检测成功".equals(rows[i])){
					sheet.addCell(new Label(column++, row, rows[i], getDailyHeaderCellStyle(10,0, 1,2)));
				}else{
					sheet.addCell(new Label(column++, row, rows[i], getDailyHeaderCellStyle(10,1, 1,2)));
				}
			}else{
				if(i%7 == 1){
					if("10".equals(rows[i])){
						rows[i] ="记名卡";
					}else if("01".equals(rows[i])){
						rows[i] ="不记名卡";
					}else{
						rows[i] = "";
					}
				}
			 
				if(i%7== 3){
					List<ConfigVO> configList = ConfigUtils.getList("MANUFACTURER");
					for(ConfigVO configVO : configList){
						if(configVO.getConfigValue().equals(rows[i])){
							rows[i] = configVO.getName();
						}
					}
				}
				if(i%7 ==4 || i%7 ==5){
					 if(!("".equals(rows[i]) || rows[i]==null)){
						 rows[i] = formate.format(formate.parse(rows[i]));
					 }
				}
			
				sheet.addCell(new Label(column++, row, rows[i], getDailyHeaderCellStyle(10,0, 1,2)));
			}
			rowsLength--;
			// 换行
			if (rowsLength % titles.length == 0) {
				column = 0;
				row++;
			}
		}*/
		
		for (int i = 0; i < rows.length; i++) {
			if(i% titles.length == 1){
				if("10".equals(rows[i])){
					rows[i] ="记名卡";
				}else if("01".equals(rows[i])){
					rows[i] ="不记名卡";
				}else{
					rows[i] = "";
				}
				sheet.addCell(new Label(column++, row, rows[i], getDailyHeaderCellStyle(10,0, 1,2)));
			}else if(i% titles.length == 3){
				rows[i] = getManufacturerName(rows[i]);
				sheet.addCell(new Label(column++, row, rows[i], getDailyHeaderCellStyle(10,0, 1,2)));
			}else if(i%titles.length ==4 || i%titles.length ==5){
				 if(!("".equals(rows[i]) || rows[i]==null)){
					 rows[i] = formate.format(formate.parse(rows[i]));
				 }
				 sheet.addCell(new Label(column++, row, rows[i], getDailyHeaderCellStyle(10,0, 1,2)));
			}else if(i%titles.length == 6){
				if("检测成功".equals(rows[i])){
					sheet.addCell(new Label(column++, row, rows[i], getDailyHeaderCellStyle(10,0, 1,2)));
				}else{
					sheet.addCell(new Label(column++, row, rows[i], getDailyHeaderCellStyle(10,1, 1,2)));
				}
			}else{
				sheet.addCell(new Label(column++, row, rows[i], getDailyHeaderCellStyle(10,0, 1,2)));
			}
			
			rowsLength--;
			// 换行
			if (rowsLength % titles.length == 0) {
				column = 0;
				row++;
			}
		}
		book.write();
		book.close();
	}

	
	public String getManufacturerName(String name){
		List<ConfigVO> configList = ConfigUtils.getList("MANUFACTURER");
		for(ConfigVO configVO : configList){
			if(configVO.getConfigValue().equals(name)){
				name = configVO.getName();
			}
		}
		return name;
	}
	@Override
	public Map<String, Object> hook(List<String[]> listExcel,String excelKey,List<String> listName) throws Exception {
		return null;
	}

	@Override
	public void insertDatabase(List<String[]> listExcel, Object Object) throws Exception {
		
	}

}
