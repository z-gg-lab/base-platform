package com.hdos.platform.base.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.component.model.ExcelImportTemplateVO;
import com.hdos.platform.base.component.service.ExcelImportTemplateService;
import com.hdos.platform.base.config.model.ConfigVO;
import com.hdos.platform.base.department.mapper.DepartmentMapper;
import com.hdos.platform.base.department.model.DepartmentVO;
import com.hdos.platform.base.department.service.DepartmentService;
import com.hdos.platform.base.filter.LoginContext;
import com.hdos.platform.base.filter.LoginUserInfo;
import com.hdos.platform.base.role.model.RoleVO;
import com.hdos.platform.base.user.model.AccountInfoVO;
import com.hdos.platform.base.user.model.UserVO;
import com.hdos.platform.base.user.service.AccountInfoService;
import com.hdos.platform.base.user.service.AccountService;
import com.hdos.platform.base.user.service.UserService;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.util.ConfigUtils;

	@Controller
	@RequestMapping("/user")
	public class UserController {
		@Autowired
		private UserService userService;
		@Autowired
		private AccountService accountService;
		@Autowired
		private AccountInfoService accountInfoService;		
		@Autowired
		private DepartmentService departmentService;
		@Autowired
		private DepartmentMapper departmentMapper;
		@Autowired
		private ExcelImportTemplateService excelImportTemplateService;
		
		private static String USER_KEY = "USER123";
		
		/**
		 * 树形结构查找用户， 传入参数keyword表示查询用户账号；parentId表示所选择的树形节点。
		 */
		@RequestMapping(value = "/listTree")
		@ResponseBody
		public Object getList(@RequestParam(required = false) String keyword, @RequestParam(required = false) String parentId, @RequestParam(required = false) String isMerchant,
						@RequestParam(value = "page", defaultValue = "1") int pageNumber,
						@RequestParam(value = "rows", defaultValue = "10") int pageSize, Model model) {
			if(StringUtils.isEmpty(parentId))//当为根节点的时候
			{
				parentId = departmentService.searchFirstDepartment();
			}
			if("-1".equals(parentId))//当为根节点的时候
			{
				parentId = departmentService.searchFirstDepartment();
			}
			if (!StringUtils.isEmpty(keyword)) {//当传入了查询条件的时候
				return list(keyword, pageNumber, pageSize,isMerchant,parentId);
			}
			JSONObject rst = new JSONObject();
			
//			String fullCode = departmentMapper.getDepartmentFullCode(parentId);
//			List<DepartmentVO> list = departmentMapper.queryAllByParentId(fullCode);
//			String departmentIds = ""; // 设置为空，直接添加。
//			for (DepartmentVO temp : list) {
//				departmentIds += temp.getDepartmentId() + ",";
//			}
//			String[] ids = departmentIds.split(",");
			Page<AccountInfoVO> userPage = accountInfoService.findPageByDepartmentId(parentId, pageNumber, pageSize,isMerchant);
			rst.put("total", userPage.getTotalElements());
			rst.put("rows", userPage.getContent()); // 查询到在departmentId下的所有用户。
			return rst.toJSONString();
		}
	/**
	 * 分页查询用户数据
	 * @param userAccount
	 * @param page
	 * @param rows
	 * @param isMerchant
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public String list(String userAccount,int page,int rows, String isMerchant, String parentId) {
		//2016年11月10日11:14:07加入对商户的查询
		if("-1".equals(parentId))//当为根节点的时候
		{
			parentId = departmentService.searchFirstDepartment();
		}
		Map<String, Object> queryCondition = new HashMap<String,Object>();
		JSONObject rst = new JSONObject();	
		queryCondition.put("userAccount", userAccount);
		queryCondition.put("parentId", parentId);
		;
		Page<AccountInfoVO> userPage = accountInfoService.findPage(queryCondition, page, rows, isMerchant);	
		rst.put("total", userPage.getTotalElements());
		rst.put("rows", userPage.getContent());
		return rst.toJSONString();
	}
	
	/**
	 * 用户管理主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/init")
	public String init() {
		return "base/user/userMain";
	}
	
	/**
	 * 用户信息导入错误页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/userImpErr")
	public String userImpErr(String data,Model model) {
		model.addAttribute("data",data);
		return "base/user/userImpErr";
	}
	
	/**
	 * 新增用户
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String addUser() {
		return "base/user/userAdd";
	}
	
	/**
	 * 保存用户
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public String saveUser(AccountInfoVO accountInfoVO, String isMerchant){
		String res = userService.saveUser(accountInfoVO,isMerchant);
		if(res.equals("NoDepartment"))
			return "NoDepartment";
		if(res.equals("true"))
			return "success";
		if(res.equals("false"))
			return "false";
		if(res.equals("falseUserCode"))
			return "falseUserCode";
		return "true";
	}
	
	/**
	 * 保存用户角色
	 * @return
	 */
	@RequestMapping(value="/saveUserRole")
	@ResponseBody
	public String saveUserRole(@RequestParam String[] userIds,String roleId) {
		
		userService.saveUserRole(userIds,roleId);
		return "success";
	}
	@RequestMapping(value="/saveUserRoleSecond")
	@ResponseBody
	public String saveUserRoleSecond(@RequestParam String userIds,String roleId) {
		if(StringUtils.isEmpty(roleId)){
			return "false";
		}
		String[] users = userIds.split(","); 
		userService.saveUserRoleSecond(users,roleId);
		return "success";
	}
	
	/**
	 * 用户类型
	 * @return
	 */
	@RequestMapping(value = "/type")
	@ResponseBody
	public String getType(){
		List<ConfigVO> tmp = ConfigUtils.getList("USER_TYPE");
		return JSONObject.toJSONString(tmp);
	}
	
	/**
	 * 删除用户
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String deleteUser(String userIds) {
		String[] ids = userIds.split(",");
		userService.deleteUsers(ids);
		return "success";
	}
	
	/**
	 * 修改用户
	 */
	@RequestMapping(value = "/edit/{userId}")
	public String editUser(@PathVariable("userId") String userId,Model model) {
		AccountInfoVO accountInfoVO = accountInfoService.readAccountInfoById(userId);
		model.addAttribute("accountInfoVO", accountInfoVO);
		return "base/user/userEdit";
	}
	
	/**
	 * 查找用户
	 */
	@RequestMapping(value = "/select")
	@ResponseBody
	public String searchSelectedUser(String userIds) {
		String[] ids = userIds.split(",");
		List<UserVO> userVO = userService.serchByUserId(ids);
		return JSONObject.toJSONString(userVO);
	}
	
	/**
	 * 查询所有用户
	 * @return
	 */
	@RequestMapping(value = "/listUserSelect")
	@ResponseBody
	public String listUserSelect(@RequestParam()String roleId) {
		
		List<UserVO> list = userService.findAllUsers(roleId);
		
		return JSONObject.toJSONString(list);
		
	}
	
	/**
	 * 查询特定用户的departmentName
	 */
	@RequestMapping(value = "/queryDepartmentName", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	@ResponseBody
	public String queryDepartmentName(String userId){
		 return  userService.queryDepartmentName(userId);
	}
	
	/**
	 * 查询相对于所选角色的所有被分配的用户
	 */
	@RequestMapping(value = "/listTreeOfRole4UserId")
	@ResponseBody
	public String listTreeOfRole4UserId(String roleId)  {
			List<UserVO> list = userService.listTreeOfRole(roleId);
			if(list == null){
				return null;
			}
			String userIds = "";
			for(UserVO tmp :list){
				userIds += tmp.getUserId() + ",";
			}
		  return  userIds;
	}
	
	@RequestMapping(value = "/listTreeOfRole")
	@ResponseBody
	public String listTreeOfRole(String roleId)  {
			List<UserVO> list = userService.listTreeOfRole(roleId);
		  return  JSONObject.toJSONString(list);
	}
	/**
	 * 角色管理模块分配用户管理&人员选择插件，通过传入的userIds，控制人员选择。
	 * @param keyword
	 * @param parentId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/listTree4Role")
	@ResponseBody
	public Object getList4Role(@RequestParam(required = false) String keyword, @RequestParam(required = false) String parentId, @RequestParam(required = false) String userIds) {
		if(StringUtils.isEmpty(parentId)){
			return list4Role(userIds);
		}
		String fullCode = departmentMapper.getDepartmentFullCode(parentId);
		List<DepartmentVO> list = departmentMapper.queryAllByParentId(fullCode);
		String departmentIds = ""; // 设置为空，直接添加。
		for (DepartmentVO temp : list) {
			departmentIds += temp.getDepartmentId() + ",";
		}
		String[] ids = departmentIds.split(",");
		List<AccountInfoVO> List = accountInfoService.findByDepartmentId4Role(ids); 
		String[] users = userIds.split(",");
		for(int y = 0; y < List.size(); y++){
			for(int i = 0; i <users.length; i++){
				if(List.get(y).getUserId().equals(users[i])){
					List.remove(y);
					y--;//直接控制y--才好。然后break语句。 y-- 是因为for结束会进行y++
					//i = -1;//这个很关键，因为remove（y）后List会自动移位 这里一开始i为0 但是之后会自动变成1，所以必须将i置为0
					break;
				}
			}
		}
		return  JSONObject.toJSONString(List);
	}
	
	/**
	 * 不同与上面函数，这里面加上了对type的判断。
	 * @param keyword
	 * @param parentId
	 * @param userIds
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/listTree4Component")
	@ResponseBody
	public Object listTree4Component(@RequestParam(required = false) String keywordAccount,@RequestParam(required = false) String keywordCode, @RequestParam(required = false) String parentId, @RequestParam(required = false) String userIds,
			@RequestParam(required = false) String type, @RequestParam(required = false) String methodOfType, @RequestParam(required = false) String methodOfUser, @RequestParam(required = false) String isMerchantForDepartment, Model model) {
		//easyUi datagrid初次加载时候会传递全为空的字段。这里直接返回空
		//查询条件标准：keyword不为空，且parentId == -2or-3
		if(keywordAccount == null && keywordCode == null &&parentId ==null && userIds == null && type == null && methodOfType == null && methodOfUser == null){
			return null;
		}
		//账号查询
		if(("-2".equals(parentId))){
			Map<String, Object> condition = new HashMap<String,Object>();
			JSONObject rst = new JSONObject();	
			if("false".equals(methodOfUser)){
				
				String[] users = userIds.split(",");
				String[] types = type.split(",");
				condition.put("keywordAccount", keywordAccount);
				condition.put("keywordCode", keywordCode);
				condition.put("methodOfType", methodOfType);
				//oracle下，传入id为空时，置为-1；--》not in('')
				if(StringUtils.isEmpty(userIds)){
					condition.put("userIds", "-1");
				}else{
					condition.put("userIds", users);
				}
				
				condition.put("type", types);
				List<AccountInfoVO> content = accountInfoService.findByConditionKeywordAccount(condition, isMerchantForDepartment);
				rst.put("total", content.size());
				rst.put("rows", content);
				return rst.toJSONString();
			}
			if("true".equals(methodOfUser)){
				String[] users = userIds.split(",");
				String[] types = type.split(",");
				condition.put("parentId", parentId);
				condition.put("methodOfType", methodOfType);
				condition.put("userIds", users);
				condition.put("type", types);
				List<AccountInfoVO> content = accountInfoService.findByConditionOfSelectdUserIds(condition, isMerchantForDepartment);
				rst.put("total", content.size()); 
				rst.put("rows", content);
				return  JSONObject.toJSONString(content);
			}
		}
		Map<String, Object> condition = new HashMap<String,Object>();
		JSONObject rst = new JSONObject();	
		if(StringUtils.isEmpty(parentId)){
			parentId = departmentService.searchFirstDepartment();
		}
		if("false".equals(methodOfUser)){
			
			String[] users = userIds.split(",");
			String[] types = type.split(",");
			condition.put("parentId", parentId);
			condition.put("methodOfType", methodOfType);
			//oracle下，传入id为空时，置为-1；--》not in('')
			if(StringUtils.isEmpty(userIds)){
				condition.put("userIds", "-1");
			}else{
				condition.put("userIds", users);
			}
			
			condition.put("type", types);
			List<AccountInfoVO> content = accountInfoService.findByCondition(condition, isMerchantForDepartment);
			rst.put("total", content.size());
			rst.put("rows", content);
			return rst.toJSONString();
		}
		if("true".equals(methodOfUser)){
			String[] users = userIds.split(",");
			String[] types = type.split(",");
			condition.put("parentId", parentId);
			condition.put("methodOfType", methodOfType);
			condition.put("userIds", users);
			condition.put("type", types);
			List<AccountInfoVO> content = accountInfoService.findByConditionOfSelectdUserIds(condition, isMerchantForDepartment);
			rst.put("total", content.size()); 
			rst.put("rows", content);
			return  JSONObject.toJSONString(content);
		}
		return null;
	}
	
	/**
	 * 剔除掉userIds中包含的用户，userIds存储方式为+= ','
	 * @param userIds
	 * @return
	 */
	public String list4Role(String userIds) {
			List<AccountInfoVO> List = accountInfoService.findAll();	
			if(!StringUtils.isEmpty(userIds)){//判断是否为空
				String[] users = userIds.split(",");
				for(int y = 0; y < List.size(); y++){
					for(int i = 0; i <users.length; i++){
						if(List.get(y).getUserId().equals(users[i])){
							List.remove(y);
							y--;  //当y=0时，会出现问题。所以一定要加上break
							break;
						}
					}
				}
			}
			return  JSONObject.toJSONString(List);
	}
	
	/**
	 * 剔除掉useIds和type中包含的用户
	 * @param userIds
	 * @param type
	 * @return
	 */
	public String list4Component(String userIds, String type) {
		List<AccountInfoVO> List = accountInfoService.findAll();	
		if(!StringUtils.isEmpty(userIds)){//判断是否为空
			String[] users = userIds.split(",");
			for(int y = 0; y < List.size(); y++){
				for(int i = 0; i <users.length; i++){
					if(List.get(y).getUserId().equals(users[i])){
						List.remove(y);
						y--;
						break;
					}
				}
			}
		}
		List = list4ComponentOfType(List,type);
		return  JSONObject.toJSONString(List);
	}
	
	public List<AccountInfoVO> list4ComponentOfType(List<AccountInfoVO> accountInfoVO, String type){
		if(!StringUtils.isEmpty(type)){
			for(int y = 0; y < accountInfoVO.size(); y++){
				if(accountInfoVO.get(y).getType().equals(type)){
					accountInfoVO.remove(y);
					y--;
					break;
				}
			}
		}
		return accountInfoVO;
	}
	
	/**
	 * 返回登陆用户信息
	 * @return
	 */
	@RequestMapping(value = "/currentUserInfo")
	@ResponseBody
	public AccountInfoVO currentUserInfo(){
		LoginUserInfo loginUserInfo = LoginContext.getCurrentUser();
		AccountInfoVO accountInfoVO = accountInfoService.getUserInfo(loginUserInfo.getUserId());
		return accountInfoVO;
	}
	
	/**
	 * 返回角色选择页面
	 * @return
	 */
	@RequestMapping(value = "/userrole")
	public String userRole(String userId,String excludeUser, Model model){
		model.addAttribute("userId", userId);
		model.addAttribute("excludeUser",excludeUser);
		return "base/user/userRole";
	}
	
	/**
	 * 返回角色选择页面
	 * @return
	 */
	@RequestMapping(value = "/userHasRole", produces = "text/htm;charset=UTF-8")
	@ResponseBody
	public String userHasRole(String userId, String random){
		List<RoleVO> userHasRole = userService.userHasRole(userId);
		
		return JSONObject.toJSONString(userHasRole);
	}


	/**
	 * 导入内容的定义
	 * @param model
	 * @param request
	 * @return String
	 */
	@RequestMapping("/userImp")
	public String userImp(Model model) {
		return "base/taskimp/excelImp";
	}
	
	/**
	 * 导入
	 * @return String
	 */
	@RequestMapping(value = "/userImps")
	public String userImps(Model model) {
		ExcelImportTemplateVO excelImportTemplateVO  = excelImportTemplateService.getByKey(USER_KEY);
		model.addAttribute("excelImportTemplateVO",excelImportTemplateVO);
		return "base/taskimp/excelImp";
	}
}
//	不再需要查询部门之下所有人员
//	String fullCode = departmentMapper.getDepartmentFullCode(parentId);
//	List<DepartmentVO> list = departmentMapper.queryAllByParentId(fullCode);
//	String departmentIds = ""; // 设置为空，直接添加。
//	for (DepartmentVO temp : list) {
//		departmentIds += temp.getDepartmentId() + ",";
//	}
//	String[] ids = departmentIds.split(",");
//	List<AccountInfoVO> List = accountInfoService.findByDepartmentId4Role(ids);
//	for(int y = 0; y < List.size(); y++){
//		for(int i = 0; i <users.length; i++){
//			if(List.get(y).getUserId().equals(users[i])){
//				List.remove(y);
//				y--;//直接控制y--才好。然后break语句。 y-- 是因为for结束会进行y++
//					//i = -1;//这个很关键，因为remove（y）后List会自动移位 这里一开始i为0 但是之后会自动变成1，所以必须将i置为0
//				break;
//			}
//		}
//	}