package com.hdos.platform.base.user.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.hdos.platform.base.component.mapper.ExcelImportTemplateMapper;
import com.hdos.platform.base.component.model.ExcelImportColumnVO;
import com.hdos.platform.base.component.service.BaseExcelImportTemplateService;
import com.hdos.platform.base.config.model.ConfigVO;
import com.hdos.platform.base.department.mapper.DepartmentMapper;
import com.hdos.platform.base.user.mapper.AccountMapper;
import com.hdos.platform.base.user.mapper.UserMapper;
import com.hdos.platform.base.user.model.AccountInfoVO;
import com.hdos.platform.base.user.model.AccountVO;
import com.hdos.platform.base.user.model.UserVO;
import com.hdos.platform.common.util.CodeGenerateUtils;
import com.hdos.platform.common.util.ConfigUtils;
import com.hdos.platform.common.util.PrimaryKeyUtils;


@Service
@Transactional
public class FileImpService extends BaseExcelImportTemplateService{

	@Autowired
	private ExcelImportTemplateMapper excelImportTemplateMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private DepartmentMapper departmentMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private AccountMapper accountMapper;
	
	private final static String USERTYPE_OPERATOR = "1001";
	private final static int ERR_MESSAGE = 30;
	
	@Override
	public Map<String, Object> hook(List<String[]> listExcel,String excelKey,List<String> listName) throws Exception {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String returnInfo = null;
		List<ExcelImportColumnVO> listColumn = excelImportTemplateMapper.listFieldKey(excelKey);
		//初始校验,flag用来判断某行数据是否有问题，有问题的话则会返回错误信息，同时加上“，”作为区分
		//已弃用
		boolean flag = false;
		//统计报错数量
		int count = 0;
		for (int i = 1; i < listExcel.size(); i++) {
			flag = false;
			String[] str = listExcel.get(i);
			for (int k = 0; k < str.length; k++) {
				if (str[0].trim() == null || "".equals(str[0].trim())) {
					if(count <= ERR_MESSAGE){
						sb.append("第" + (i + 1) + "行" + (k + 1) + "列" + "【"+listColumn.get(0).getFieldName()+"】" + "数据为空；,");
						flag = true;
					}
					count++;
				} 
				if(StringUtils.isEmpty(str[k].trim()) && k == 1){
					if(count <= ERR_MESSAGE){
					sb.append("第" + (i + 1) + "行" + (k + 1) + "列" + "【"+listColumn.get(k).getFieldName()+"】"
										+ "数据为必填项；,");
					flag = true;
					}
					count++;
				}
				if(StringUtils.isEmpty(str[k].trim()) && k == 2){
					if(count <= ERR_MESSAGE){
					sb.append("第" + (i + 1) + "行" + (k + 1) + "列" +"【"+ listColumn.get(k).getFieldName()+"】"
										+ "数据为必填项；,");
					flag = true;
					}
					count++;
				}
				if(StringUtils.isEmpty(str[k].trim()) && k == 3){
					if(count <= ERR_MESSAGE){
					sb.append("第" + (i + 1) + "行" + (k + 1) + "列" + "【"+listColumn.get(k).getFieldName()+"】"
										+ "数据为必填项；,");
					flag = true;
					}
					count++;
				}
				if(StringUtils.isEmpty(str[k].trim()) && k == 5){
					if(count <= ERR_MESSAGE){
					sb.append("第" + (i + 1) + "行" + (k + 1) + "列" + "【"+listColumn.get(k).getFieldName()+"】"
										+ "数据为必填项；,");
					flag = true;
					}
					count++;
				}
				// 数据的字节长度应该小于数据库给的字段长度
				if (Integer.valueOf(listColumn.get(k).getLength()) - listExcel.get(i)[k].getBytes("GBK").length < 0) {
					if(count <= ERR_MESSAGE){
					sb.append("第" + (i + 1) + "行" + (k + 1) + "列" +"【"+ listColumn.get(k).getFieldName()+"】"
							+ "数据内容过长；,");
					flag = true;
					}
					count++;
				}

				// 数据中有无特殊符号
				switch(k){
				case 0:
				case 1:
				case 2:
					if (!"".equals(listExcel.get(i)[k]) && !isCorrect(listExcel.get(i)[k].trim())) {
						if(count <= ERR_MESSAGE){
						sb.append("第" + (i + 1) + "行" + (k + 1) + "列" +"【"+ listColumn.get(k).getFieldName()+"】"
								+ "不能输入特殊字符，只能输入中文、英文、数字和下划线；,");
						flag = true;
						}
						count++;
					}
					break;
				case 9:
					if (!"".equals(listExcel.get(i)[k]) && !isValidate(listExcel.get(i)[k].trim())) {
						if(count <= ERR_MESSAGE){
						sb.append("第" + (i + 1) + "行" + (k + 1) + "列" +"【"+ listColumn.get(k).getFieldName()+"】"
								+ "不能输入特殊字符，只能输入英文、数字和下划线；,");
						flag = true;
						}
						count++;
					}
					break;
				case 4:
					if (!"".equals(listExcel.get(i)[k]) && !isSex(listExcel.get(i)[k].trim())) {
						if(count <= ERR_MESSAGE){
						sb.append("第" + (i + 1) + "行" + (k + 1) + "列" +"【"+ listColumn.get(k).getFieldName()+"】"
								+ "格式错误；,");
						flag = true;
						}
						count++;
						}
					break;
				case 5:
					if (!"".equals(listExcel.get(i)[k]) && !isDepartment(listExcel.get(i)[k].trim())) {
						if(count <= ERR_MESSAGE){
						sb.append("第" + (i + 1) + "行" + (k + 1) + "列" + "【"+listColumn.get(k).getFieldName()+"】"
								+ "不存在；,");
						flag = true;
						}
						count++;
					}
					break;
				case 3:
					if (!"".equals(listExcel.get(i)[k]) && "fail".equals(formatUserType(listExcel.get(i)[k].trim()))) {
						if(count <= ERR_MESSAGE){
						sb.append("第" + (i + 1) + "行" + (k + 1) + "列" + "【"+listColumn.get(k).getFieldName()+"】"
								+ "不存在；,");
						flag = true;
						}
						count++;
					}
					break;
				case 6:
					if (!"".equals(listExcel.get(i)[k]) && !isPhone(listExcel.get(i)[k].trim())) {
						if(count <= ERR_MESSAGE){
						sb.append("第" + (i + 1) + "行" + (k + 1) + "列" +"【"+ listColumn.get(k).getFieldName()+"】"
								+ "格式错误；,");
						flag = true;
						}
						count++;
					}
					break;
				case 7:
					if (!"".equals(listExcel.get(i)[k]) && !isEmail(listExcel.get(i)[k].trim())) {
						if(count <= ERR_MESSAGE){
						sb.append("第" + (i + 1) + "行" + (k + 1) + "列" +"【"+ listColumn.get(k).getFieldName()+"】"
								+ "格式错误；,");
						flag = true;
						}
						count++;
					}
					break;
				}
			}
			if(flag){
			}
		}
		
		// 校验数据的重复性
		flag = false;
			Map<String, Object> readMap = new HashMap<String, Object>();
			Map<String, Object> posMap = new HashMap<String, Object>();
			Map<String, Object> imeiMap = new HashMap<String, Object>();
			AccountInfoVO accountInfoVO = new AccountInfoVO();
			for (int n = 1; n < listExcel.size(); n++) {
				flag = false;
				if (posMap.get(listExcel.get(n)[0].trim()) == null) {
					posMap.put(listExcel.get(n)[0].trim(), 1);
				} else {
					if(count <= ERR_MESSAGE){
					sb.append("第" + (n + 1) + "行1列" + "【"+listColumn.get(0).getFieldName()+"】" + "数据在导入的excel中有重复；,");
					flag = true;
					}
					count++;
				}
				accountInfoVO.setUserAccount(listExcel.get(n)[0].trim());
				if (userService.verifyRepeat(accountInfoVO) ) {
					if(count <= ERR_MESSAGE){
					sb.append("第" + (n + 1) + "行1列" + "【"+listColumn.get(0).getFieldName()+"】" + "数据在系统中已存在；,");
					flag = true;
					}
					count++;
				}
				if(flag){
				}
			}
			for (int n = 1; n < listExcel.size(); n++) {
				flag = false;
				if(listExcel.get(n)[9].trim() == null  || StringUtils.isEmpty(listExcel.get(n)[9].trim())){
					continue;
				}
				if (readMap.get(listExcel.get(n)[9].trim()) == null) {
					readMap.put(listExcel.get(n)[9].trim(), 1);
				} else {
					if(count <= ERR_MESSAGE){
					sb.append("第" + (n + 1) + "行10列" + "【"+listColumn.get(9).getFieldName()+"】" + "数据在导入的excel中有重复；,");
					flag = true;
					}
					count++;
				}
				accountInfoVO.setUserAccount(listExcel.get(n)[0].trim());
				if (!userService.verifyUserCode(listExcel.get(n)[9].trim()) ) {
					if(count <= ERR_MESSAGE){
					sb.append("第" + (n + 1) + "行10列" +"【"+ listColumn.get(9).getFieldName() +"】"+ "数据在系统中已存在；,");
					flag = true;
					}
					count++;
				}
				if(flag){
				}
			}
		if(count >= 32){
			sb.append("......共"+count+"条错误信息；"+",");
		}
		returnInfo = sb.toString();
		resultMap.put("msg", returnInfo);
		return resultMap;
	}

	private boolean isDepartment(String trim) {
		//校验部门的存在
		String departmentId = departmentMapper.getIdByFullCode(trim);
		if(StringUtils.isEmpty(departmentId)){
			return false;
		}
		return true;
	}

	/**
	 * 验证邮箱格式
	 * @param trim
	 * @return
	 */
	private boolean isEmail(String trim) {
		String regex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(trim);
		return matcher.matches();
	}

	/**
	 * 验证电话格式
	 * @param trim
	 * @return
	 */
	private boolean isPhone(String trim) {
		//String regex="1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}";
		String regex = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(trim);
		return matcher.matches();
	}

	/**
	 * 判别输入是否为男女
	 * @param trim
	 * @return
	 */
	private boolean isSex(String trim) {
		boolean flag = "男".equals(trim)||"女".equals(trim);
		return flag;
	}

	/**
	 * 验证通过后将数据插到数据库中
	 */
	@Override
	public void insertDatabase(List<String[]> listExcel, Object object) throws Exception {
		AccountInfoVO accountInfoVO = null;
		UserVO userVO = null;
		AccountVO accountVO = null;
		List<AccountInfoVO> list = new ArrayList<AccountInfoVO>();
		for (int i = 1; i < listExcel.size(); i++) {
			accountInfoVO = new AccountInfoVO();
			String[] str = listExcel.get(i);
			//部门ID
			String departmentId = departmentMapper.getIdByFullCode(str[5].trim());
			accountInfoVO.setDepartmentId(departmentId);
			//性别
			if("男".equals(str[4].trim())){
				accountInfoVO.setGender(1);
			}
			if("女".equals(str[4].trim())){
				accountInfoVO.setGender(0);
			}
			//其他
			accountInfoVO.setUserAccount(str[0].trim());
			accountInfoVO.setNickName(str[1].trim());
			accountInfoVO.setUserName(str[2].trim());
			accountInfoVO.setPhone(str[6].trim());
			accountInfoVO.setEmail(str[7].trim());
			accountInfoVO.setDescription(str[8].trim());
			String tmp = generateKey(accountInfoVO);
			accountInfoVO.setUserId(tmp);
			tmp = DigestUtils.md5Hex(ConfigUtils.get("USER_IMP_PWD"));
			accountInfoVO.setPwd(tmp);
			if(StringUtils.isEmpty(str[9].trim())){
				accountInfoVO.setUserCode(CodeGenerateUtils.getCodeGenerator("userCode", null));
			}else{
				accountInfoVO.setUserCode(str[9].trim());
			}
			//格式化用户类型
			String userType = formatUserType(str[3].trim());
			accountInfoVO.setType(userType);
//			accountInfoVO.setType(str[3].trim());
			userVO = AccountInfoVO.accountInfo2UserVO(accountInfoVO);
			accountVO = AccountInfoVO.accountInfoVO2AccountVO(accountInfoVO);
			userVO.setCreateTime(new Timestamp(System.currentTimeMillis()));
			userVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			accountVO.setCreateTime(new Timestamp(System.currentTimeMillis()));
			accountVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			userMapper.insert(userVO);
			accountMapper.insert(accountVO);
		}
	}
	
	/**
	 * 格式化用户类型
	 * @param trim
	 * @return
	 */
	private String formatUserType(String trim) {
		List<ConfigVO> configVO = ConfigUtils.getList("USER_TYPE");
		for(ConfigVO i :configVO){
			if(trim.equals(i.getConfigValue()))
				return trim;
			if(trim.equals(i.getName())){
				return i.getConfigValue();
			}
		}
		return "fail";
	}

	/**
	 * 生成key
	 * @param vo
	 * @return
	 */
	protected String generateKey(AccountInfoVO vo) {
		return PrimaryKeyUtils.generate(vo);
	}
	/**
	 * 只能含有英文、数字、下划线、中线和竖线
	 * @param str
	 * @return boolean
	 */
	private static boolean isValidate(String str){
		String reg = "[0-9a-zA-Z\\-\\_\\|]+";
		return str.matches(reg);
	}
	
	/**
	 *只能含有中英文、数字和下划线 
	 * @param str
	 * @return boolean
	 */
	private static boolean isCorrect(String str) {
		String reg = "[0-9a-zA-Z\\u4e00-\\u9fa5\\_]+";
		return str.matches(reg);
	}
}
