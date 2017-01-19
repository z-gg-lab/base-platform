package com.hdos.platform.base.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.component.service.DepartmentSelectService;
import com.hdos.platform.common.vo.TreeVO;

/**
 * 机构商户控件控制器
 * @author zhuw
 *
 */
@Controller
@RequestMapping("/component/departmentselect")
public class DepartmentSelectController {
	
	private final Logger logger = LoggerFactory.getLogger(DepartmentSelectController.class);
	@Autowired
	private DepartmentSelectService departmentSelectService;
	
	/**
	 * 初始化主页面
	 * @return
	 */
	@RequestMapping("/init")
	public String init() {
		if(logger.isInfoEnabled()) {
			logger.info("[机构管理页面初始化成功]");
		}
		return "base/component/test";
	}
	
	/**
	 * 查询子节点
	 * @param parentId
	 * @param model
	 * @return 子节点列表
	 */
	@RequestMapping(value = "/tree/data")
	@ResponseBody
	public String getChildren(@RequestParam String parentId, String[] type,String flag,Model model) {
		Map<String, Object> condition = new HashMap<String, Object>();
		
		condition.put("parentId", parentId);
		condition.put("flag",flag);
		List<String> list = new ArrayList<String>();
		if(type!=null){
			for (int i = 0; i < type.length; i++) {
				if(!type[i].equals("null")){
					list.add(type[i]);
					condition.put("type", list);
				}else {
					condition.put("type", null);
				}
			}
		}else {
			condition.put("type", null);
		}
		
		List<TreeVO> treeList = departmentSelectService.find(condition);
		return JSONObject.toJSONString(treeList);
	}

	
}
