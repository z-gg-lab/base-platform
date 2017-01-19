package com.hdos.platform.base.component.service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hdos.platform.base.component.mapper.ExcelImportTemplateMapper;
import com.hdos.platform.base.component.model.ExcelImportColumnVO;
import com.hdos.platform.base.component.model.ExcelImportTemplateVO;
import com.hdos.platform.base.component.model.TableVO;
import com.hdos.platform.base.config.model.ConfigVO;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.page.PageImpl;
import com.hdos.platform.common.util.PrimaryKeyUtils;
import com.hdos.platform.core.base.BaseService;
import jxl.Cell;
import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;

@Transactional
public abstract class BaseExcelImportTemplateService extends BaseService<ExcelImportTemplateVO> {

    @Autowired
    private ExcelImportTemplateMapper excelImportTemplateMapper;

    private static final Logger logger = LoggerFactory.getLogger(BaseExcelImportTemplateService.class);

    /**
     * COLUMN类型
     */
    private static final String COLUMN_TYPE_1 = "字符串";
    private static final String COLUMN_TYPE_2 = "数字";
    private static final String COLUMN_TYPE_3 = "时间";

    /**
     * 校验规则：系统校验
     */
    private static final String SYSTEM_CHECK = "1";

    /**
     * 校验规则：自定义校验
     */
    private static final String CUSTOM_CHECK = "2";

    /**
     * 生成excel模板
     *
     * @param excelKey
     * @param file
     */
    public void createExcelTemplate(String excelKey, File file) {

        WritableWorkbook workbook = null;
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("excelKey", excelKey);
        List<ExcelImportTemplateVO> listTemplate = excelImportTemplateMapper.list(condition);
        if (listTemplate.size() != 0) {
            List<ExcelImportColumnVO> listColumn = excelImportTemplateMapper
                    .listDetail(listTemplate.get(0).getExcelImportTemplateId());
            try {
                // 根据路径生成excel文件
                workbook = Workbook.createWorkbook(file);
                WritableSheet sheet = getExcelSheet(workbook, listTemplate.get(0).getTemplateName(), listColumn.size());
                WritableCellFormat wcf = new WritableCellFormat(NumberFormats.TEXT);
                Label tempLabel = null;
                for (int i = 0; i < listColumn.size(); i++) {
                    tempLabel = new Label(i, 0, listColumn.get(i).getFieldName(), getHeaderCellStyle());
                    // sheet.setColumnView(i,
                    // listColumn.get(i).getFieldName().length() + 8);
                    sheet.setColumnView(i, listColumn.get(i).getFieldName().length() + 8, wcf);
                    sheet.addCell(tempLabel);
                }
                workbook.write();
                workbook.close();
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
    }

    /**
     * 保存模板
     *
     * @param excelImportTemplateVO
     */
    public void saveTemplate(ExcelImportTemplateVO excelImportTemplateVO) {

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("templateName", excelImportTemplateVO.getTemplateName());
        excelImportTemplateVO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        if (excelImportTemplateVO.getExcelImportTemplateId() != null
                && !StringUtils.isEmpty(excelImportTemplateVO.getExcelImportTemplateId())) {
            // 修改
            condition.put("excelImportTemplateId", excelImportTemplateVO.getExcelImportTemplateId());
            ExcelImportTemplateVO vo = excelImportTemplateMapper
                    .getById(excelImportTemplateVO.getExcelImportTemplateId());

            if (excelImportTemplateMapper.count(condition) > 0) {
                throw new RuntimeException("该模板已经存在！");
            }

            if (excelImportTemplateMapper.countKey(excelImportTemplateVO) > 0) {
                throw new RuntimeException("该模板已经存在！");
            }

            // 模板的内容改变或者表名改变
            if (!excelImportTemplateVO.getConfig().equals("null")
                    || !excelImportTemplateVO.getTableName().equals(vo.getTableName())) {
                excelImportTemplateMapper.deleteInBulkDetail(excelImportTemplateVO.getExcelImportTemplateId());
            }
            excelImportTemplateMapper.update(excelImportTemplateVO);
        } else {
            // 新增
            if (excelImportTemplateMapper.count(condition) > 0) {
                throw new RuntimeException("该模板Key已经存在！");
            }
            excelImportTemplateVO.setExcelImportTemplateId("null");
            if (excelImportTemplateMapper.countKey(excelImportTemplateVO) > 0) {
                throw new RuntimeException("该模板Key已经存在！");
            }

            excelImportTemplateVO.setExcelImportTemplateId(generateKey(excelImportTemplateVO));
            // 插入主表
            excelImportTemplateMapper.insert(excelImportTemplateVO);
        }

        if (!excelImportTemplateVO.getConfig().equals("null")) {

            if ("[]".equals(excelImportTemplateVO.getConfig())) {
                // 说明删除了配置
                excelImportTemplateMapper.deleteInBulkDetail(excelImportTemplateVO.getExcelImportTemplateId());
            } else {

                JSONArray jsonArray = JSON.parseArray(excelImportTemplateVO.getConfig());
                StringBuilder columnName = new StringBuilder();
                StringBuilder fieldName = new StringBuilder();
                StringBuilder type = new StringBuilder();
                // 关联字段？
                StringBuilder relationFieldName = new StringBuilder();
                StringBuilder length = new StringBuilder();
                StringBuilder sortNo = new StringBuilder();
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        columnName.append(jsonArray.getJSONObject(i).getString("columnName")).append(",");
                        fieldName.append(jsonArray.getJSONObject(i).getString("fieldName")).append(",");
                        type.append(jsonArray.getJSONObject(i).getString("type")).append(",");
                        relationFieldName.append(jsonArray.getJSONObject(i).getString("relationFieldName")).append(",");
                        length.append(jsonArray.getJSONObject(i).getString("length")).append(",");
                        sortNo.append(jsonArray.getJSONObject(i).getString("sortNo")).append(",");
                    }
                }
                // 存放详细信息
                List<ExcelImportColumnVO> listDetail = new ArrayList<ExcelImportColumnVO>();

                String[] columnNames = columnName.toString().split(",");
                String[] fieldNames = fieldName.toString().split(",");
                // 如果前台不填怎么处理？---> 必须填
                String[] types = type.toString().split(",");
                String[] relationFieldNames = relationFieldName.toString().split(",");
                String[] lengths = length.toString().split(",");
                String[] sortNos = sortNo.toString().split(",");

                for (int i = 0; i < fieldNames.length; i++) {
                    // 生成明细信息
                    ExcelImportColumnVO excelImportColumnVO = new ExcelImportColumnVO();
                    excelImportColumnVO.setExcelImportColumnId(PrimaryKeyUtils.generate(excelImportColumnVO));
                    excelImportColumnVO.setExcelImportTemplateId(excelImportTemplateVO.getExcelImportTemplateId());
                    excelImportColumnVO.setColumnName(columnNames[i]);
                    excelImportColumnVO.setFieldName(fieldNames[i]);
                    excelImportColumnVO.setRelationFieldName(relationFieldNames[i]);
                    excelImportColumnVO.setType(types[i]);
                    excelImportColumnVO.setLength(lengths[i]);
                    excelImportColumnVO.setSortNo(Integer.valueOf(sortNos[i]));
                    listDetail.add(excelImportColumnVO);
                }

                // 获得对应表的字段
                List<TableVO> listTable = excelImportTemplateMapper
                        .getColumnByTableName(excelImportTemplateVO.getTableName());

                // 20161118按数据库字段排序
                // for (int i = 0; i < listTable.size(); i++) {
                // for (int j = 0; j < listDetail.size(); j++) {
                // if
                // (listTable.get(i).getColumnName().equals(listDetail.get(j).getColumnName()))
                // {
                // listDetail.get(j).setSortNo(i);
                // }
                // }
                // }

                // 20170104增加手动排序

                // 批量插入明细表
                excelImportTemplateMapper.insertDetailBatch(listDetail);
            }
        }

    }

    /**
     * 读取Excel
     *
     * @param file
     * @throws Exception
     */
    public Map<String, Object> readExcelData(File file, ZipEntry ze, String excelKey, List<String> listName)
            throws Exception {

        Map<String, Object> condition = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int count = 0;

        Workbook workbook = Workbook.getWorkbook(file);
        Sheet sheet = workbook.getSheet(0);

        if (excelKey == null || "".equals(excelKey)) {
            resultMap.put("msg", "excel对应的模板不存在！");
            return resultMap;
        }

        // if (ze == null || ("").equals(ze)) {
        // // condition.put("templateName",file.getName().replace(".xls", ""));
        condition.put("excelKey", excelKey);
        // } else {
        // String[] arr = ze.getName().split("/");
        // condition.put("templateName", arr[arr.length - 1].replace(".xls",
        // ""));
        // }
        List<ExcelImportTemplateVO> listTemplate = excelImportTemplateMapper.list(condition);

        if (listTemplate.size() != 0) {
            // 模板名唯一
            String tableName = listTemplate.get(0).getTableName();
            // 获得模板对应的字段名和列名(不包含ID)
            List<ExcelImportColumnVO> listColumn = excelImportTemplateMapper
                    .listDetail(listTemplate.get(0).getExcelImportTemplateId());
            // 根据表名获取primaryKey
            List<TableVO> lisPri = excelImportTemplateMapper.getColumnByTablePRI(tableName);

            if (lisPri.size() != 0) {

                // 组装字段名
                StringBuilder columnBuilder = new StringBuilder();
                columnBuilder.append(tableName).append("(").append(lisPri.get(0).getColumnName()).append(",");
                if (listColumn.size() != 0) {
                    for (int i = 0; i < listColumn.size(); i++) {
                        columnBuilder.append(listColumn.get(i).getColumnName()).append(",");
                    }
                }

                List<String[]> listExcel = new ArrayList<String[]>();
                Cell cell = null;
                if (sheet.getRows() <= 1) {
                    resultMap.put("msg", "excel为空！");
                    return resultMap;
                }

                // 获取excel
                for (int i = 0; i < sheet.getRows(); i++) {
                    String[] str = new String[sheet.getColumns()];
                    for (int j = 0; j < sheet.getColumns(); j++) {

                        if (sheet.getCell(1, 0).equals("")) {
                            resultMap.put("msg", "excel为空！");
                            return resultMap;
                        }

                        // 获取第i行，第j列的值
                        cell = sheet.getCell(j, i);
                        str[j] = cell.getContents();
                        // if (i >= 1) {
                        // sBuilder.append("\"").append(cell.getContents()).append("\",");
                        // }
                    }
                    listExcel.add(str);
                }

                String[] head = listExcel.get(0);

                if (listColumn.size() != 0) {

                    if (head.length != listColumn.size()) {
                        resultMap.put("msg", "上传的excel与配置的模板列数不一致，请检查！");
                        return resultMap;
                    }

                    // 20161118 校验excel头是否与配置的模板相同
                    for (int i = 0; i < listColumn.size(); i++) {
                        if (listColumn.get(i).getFieldName().equals(head[i])) {
                            count++;
                        }
                    }

                    if (count != head.length) {
                        resultMap.put("msg", "上传的excel的列名与配置的模板列名不一致，请检查！");
                        return resultMap;
                    }

                    // 根据校验规则校验
                    // 默认校验
                    for (int i = 0; i < listColumn.size(); i++) {
                        for (int j = 0; j < head.length; j++) {
                            if (listColumn.get(i).getFieldName().equals(head[j])) {
                                for (int k = 1; k < listExcel.size(); k++) {
                                    // 内容过长
                                    if (Integer.valueOf(listColumn.get(i).getLength())
                                            - listExcel.get(k)[j].length() < 0) {
                                        resultMap.put("msg", "第" + (k + 1) + "行" +
                                                "第" + (i + 1) + "列：" + listColumn.get(i).getFieldName() + "过长");
                                        return resultMap;
                                    }
                                    // column类型验证
                                    if (listColumn.get(i).getType().equals(COLUMN_TYPE_2)) {
                                        if (!listExcel.get(k)[j].matches("[0-9]+")) {
                                            resultMap.put("msg", "第" + (i + 1) + "列：" + listColumn.get(i).getFieldName()
                                                    + "，第" + (k + 1) + "行：" + listExcel.get(k)[j] + "不是数字类型");
                                            return resultMap;
                                        }
                                    } else if (listColumn.get(i).getType().equals(COLUMN_TYPE_3)) {

                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        df.setLenient(false);
                                        try {
                                            df.parse(listExcel.get(k)[j]);
                                        } catch (Exception e) {
                                            logger.info(e.getMessage());
                                            try {
                                                sdf.parse(listExcel.get(k)[j]);
                                            } catch (Exception e2) {
                                                logger.info(e2.getMessage());
                                                resultMap.put("msg",
                                                        "第" + (i + 1) + "列：" + listColumn.get(i).getFieldName() + "，第"
                                                                + (k + 1) + "行：" + listExcel.get(k)[j]
                                                                + "不是 yyyy-MM-dd HH:mm:ss或 yyyy-MM-dd的时间类型");
                                                return resultMap;

                                            }

                                        }

                                    }
                                }
                            }
                        }
                    }

                    String str = null;
                    StringBuilder sBuilder = null;
                    for (int i = 1; i < listExcel.size(); i++) {
                        sBuilder = new StringBuilder();
                        sBuilder.append("(\'")
                                .append(PrimaryKeyUtils.generate(getVO(listTemplate.get(0).getTableName())))
                                .append("\',");
                        for (int j = 0; j < listExcel.get(i).length; j++) {
                            if (!"".equals(listExcel.get(i)[j])) {
                                sBuilder.append("\'").append(listExcel.get(i)[j]).append("\',");
                            } else {
                                sBuilder.append("\"").append("null").append("\",");
                            }
                        }


                    }

                    if (listTemplate.get(0).getRule().equals(CUSTOM_CHECK)) {
                        // 预留接口自行验证
                        resultMap = hook(listExcel, excelKey, listName);
                        if (resultMap.get("msg") == null || ("").equals(resultMap.get("msg"))) {
                            // throw new RuntimeException("自定义验证不通过");
                            insertDatabase(listExcel, resultMap.get("merchantVO"));
                        } else {
                            return resultMap;
                        }
                    } else {
                        str = sBuilder.toString().substring(0, sBuilder.toString().lastIndexOf(",")) + ")";
                        String string = columnBuilder.toString().substring(0, columnBuilder.toString().lastIndexOf(","))
                                + ")";
                        // 拼接参数
                        condition.put("value", str);
                        condition.put("column", string);
                        excelImportTemplateMapper.insertByExcel(condition);
                        condition.remove("value");
                        condition.remove("column");
                    }
                }

            } else {
                resultMap.put("msg", "该模板对应的表不存在！");
                return resultMap;
            }
        } else {
            resultMap.put("msg", "数据库没有对应的模板！");
            return resultMap;
        }
        return resultMap;
    }

    /**
     * 自定义验证的抽象方法，自己实现
     */
    public abstract Map<String, Object> hook(List<String[]> listExcel, String excelKey, List<String> listName)
            throws Exception;

    public abstract void insertDatabase(List<String[]> listExcel, Object Object) throws Exception;

    /**
     * 设置表头样式
     *
     * @return
     */
    private WritableCellFormat getHeaderCellStyle() {
        WritableFont font = new WritableFont(WritableFont.createFont("微软雅黑"), 10, WritableFont.BOLD, false,
                UnderlineStyle.NO_UNDERLINE);

        WritableCellFormat headerFormat = new WritableCellFormat(NumberFormats.TEXT);

        try {
            // 添加字体设置
            headerFormat.setFont(font);
            // 设置单元格背景色
            headerFormat.setBackground(jxl.format.Colour.IVORY);// 象牙白
            // 设置表头表格边框样式
            headerFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框
            // 表头内容水平居中显示
            headerFormat.setAlignment(Alignment.CENTRE);
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
    private WritableSheet getExcelSheet(WritableWorkbook book, String excelName, int length)
            throws IOException, RowsExceededException {
        WritableSheet sheet = book.createSheet(excelName, 0);
        // 显示整个sheet中的网格线
        sheet.getSettings().setShowGridLines(true);
        CellView navCellView = new CellView();
        // navCellView.setAutosize(true); // 设置自动大小
        WritableCellFormat wcf = new WritableCellFormat(NumberFormats.TEXT);
        navCellView.setFormat(wcf);
        // 设置表头的高度
        sheet.setRowView(0, 500, false);
        // 设置每一列的单元格格式
        for (int i = 0; i < length; i++) {
            sheet.setColumnView(i, navCellView);
        }

        return sheet;

    }

    /**
     * 导出excel
     *
     * @param titles
     * @param rows
     * @throws IOException
     * @throws WriteException
     * @throws ParseException
     */
    public void exportExcel(String[] titles, String[] rows, File file)
            throws IOException, WriteException, ParseException {
        WritableWorkbook book = Workbook.createWorkbook(file);
        WritableSheet sheet = getExcelSheet(book, file.getName(), titles.length);
        int count = 0;
        for (int i = 0; i < titles.length; i++) {
            sheet.addCell(new Label(i, 0, titles[i], getHeaderCellStyle()));

            if (titles[i].contains("时间")) {
                count = i;
            }
        }
        int row = 1;
        int column = 0;
        int rowsLength = rows.length;
        for (int i = 0; i < rows.length; i++) {

            // 该列为时间
            if (i >= count && count <= rows.length && count % i == 0) {
                count = count + titles.length;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Long time = Long.valueOf(rows[i]);
                String d = sdf.format(time);
                Date date = sdf.parse(d);
                rows[i] = sdf.format(date);
            }

            sheet.addCell(new Label(column++, row, rows[i]));
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

    /**
     * 转换表名
     *
     * @param tableName
     * @return
     */

    private static String getString(String tableName) {

        if ((tableName.isEmpty()) || ("".equals(tableName))) {
            return "";
        } else {

            int _postion = -1;
            tableName = tableName.toLowerCase();
            while (tableName.indexOf("_") >= 1) {
                _postion = tableName.indexOf("_");
                if (_postion < tableName.length() - 1) {
                    tableName = tableName.substring(0, _postion)
                            + tableName.substring(_postion + 1, _postion + 2).toUpperCase()
                            + tableName.substring(_postion + 2, tableName.length());
                } else {
                    tableName = tableName.replace("_", "");
                }
            }

        }

        return tableName;

    }

    /**
     * 获得类首字母大写
     *
     * @param className
     * @return
     */
    public static String getVO(String className) {
        int co = className.indexOf("_");
        if (co < 0) {
            return null;
        }
        return getString(className).substring(co) + "VO";
    }

    /**
     * 根据表名查询字段名和备注
     *
     * @param tableName
     * @return
     */
    public List<TableVO> getTable(String tableName, String[] columnNames, String[] sortNos) {

        Map<String, Object> condition = new HashMap<String, Object>();

        condition.put("tableName", tableName);
        List<String> listColumn = new ArrayList<String>();
        // 排除已选择的字段
        if (columnNames != null) {
            for (int i = 0; i < columnNames.length; i++) {
                listColumn.add(columnNames[i]);
                condition.put("columnNames", listColumn);
            }
        } else {
            condition.put("columnNames", null);
        }
        List<TableVO> listTable = excelImportTemplateMapper.getColumnByTable(condition);
        System.out.print(sortNos.length);
        for (int i = 0; i < listTable.size(); i++) {
            if (sortNos.length != 0) {
                listTable.get(i).setSortNo(Integer.valueOf(sortNos[sortNos.length - 1]) + i + 1);
            } else {
                listTable.get(i).setSortNo(i + 1);
            }

        }

        return listTable;
    }

    /**
     * 分页查询
     *
     * @param condition  条件
     * @param pageNumber 页码，从 1 开始
     * @param pageSize   每页条数
     * @return
     */
    public Page<ExcelImportTemplateVO> findPage(Map<String, Object> condition, int pageNumber, int pageSize) {
        int total = excelImportTemplateMapper.count(condition);
        RowBounds rowBounds = new RowBounds((pageNumber - 1) * pageSize, pageSize);
        List<ExcelImportTemplateVO> content = total > 0 ? excelImportTemplateMapper.list(condition, rowBounds)
                : new ArrayList<ExcelImportTemplateVO>(0);
        List<ConfigVO> list = this.getCombobox();
        for (int i = 0; i < content.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (String.valueOf(content.get(i).getRule()).equals(list.get(j).getConfigValue())) {
                    content.get(i).setRule(list.get(j).getName());
                }
            }

        }

        return new PageImpl<ExcelImportTemplateVO>(content, pageNumber, pageSize, total);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    public void deleteTemplate(String... ids) {
        excelImportTemplateMapper.deleteInBulk(ids);
        // 删除明细
        excelImportTemplateMapper.deleteInBulkDetail(ids);
    }

    /**
     * @param id
     * @return
     */
    public ExcelImportTemplateVO getById(String id) {
        return excelImportTemplateMapper.getById(id);

    }

    /**
     * 根据模板名找对应的对象
     *
     * @param key
     * @return ExcelImportTemplateVO
     */
    public ExcelImportTemplateVO getByKey(String key) {
        return excelImportTemplateMapper.getByKey(key);
    }

    /**
     * 查询明细表信息
     *
     * @param id
     * @return
     */
    public List<ExcelImportColumnVO> query(String id) {

        return excelImportTemplateMapper.listDetail(id);

    }

    /**
     * 构造下拉框
     *
     * @return
     */

    public List<ConfigVO> getCombobox() {

        Map<String, String> map = new HashMap<String, String>();
        map.put("系统校验 ", SYSTEM_CHECK);
        map.put("自定义校验 ", CUSTOM_CHECK);

        List<ConfigVO> list = new ArrayList<ConfigVO>();

        for (Entry<String, String> entry : map.entrySet()) {
            ConfigVO vo = new ConfigVO();
            vo.setName(entry.getKey());
            vo.setConfigValue(String.valueOf(entry.getValue()));
            list.add(vo);
        }

        return list;

    }

}
