package com.hdos.platform.base.config.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hdos.platform.base.config.model.ConfigVO;
import com.hdos.platform.base.config.service.ConfigService;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.util.SqlUtils;
import com.hdos.platform.common.vo.TreeVO;

/**
 * 配置管理
 *
 * @author chenyang
 */
@Controller
@RequestMapping("/config")
public class ConfigController {

    /**
     * 配置管理
     */
    @Autowired
    private ConfigService configService;

    /**
     * 首页
     *
     * @return view
     */
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public String init() {
        return "base/config/configMain";
    }

    /**
     * 编辑页面 - 新增
     *
     * @return view
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editNew(@RequestParam(required = false) String parentId, Model model) {
        ConfigVO parent = configService.findById(parentId);
        if (null == parent) {
            parent = new ConfigVO();
            parent.setId(TreeVO.DEFAULT_ROOT_NODE_ID);
            parent.setName("根节点");
        }
        model.addAttribute("parent", parent);
        model.addAttribute("data", new ConfigVO());
        return "base/config/configEdit";
    }

    /**
     * 编辑页面 - 修改
     *
     * @return view
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editCurrent(@PathVariable String id, Model model) {
        ConfigVO current = configService.findById(id);
        if (null != current) {
            model.addAttribute("data", current);
            model.addAttribute("parent", configService.findById(current.getParentId()));
        }
        return "base/config/configEdit";
    }

    /**
     * 保存
     *
     * @param vo    配置管理
     * @param model 数据模型
     * @return 配置管理
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Object save(ConfigVO vo, Model model) {
        Assert.notNull(vo.getConfigKey(), "提交的配置数据不正确！");

        if (!vo.getId().isEmpty()) {
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("id", vo.getId());
            condition.put("configKey", vo.getConfigKey());
            List<ConfigVO> existed = configService.find(condition);
            if (existed.size() > 0) {
                return "duplicated";

            }
        }else {
            ConfigVO configVO = configService.findByKey(vo.getConfigKey());
            if (null!=configVO){
                return "duplicated";
            }
        }
        configService.save(vo);
        return JSONObject.toJSONString(vo);
    }

    /**
     * 删除配置管理
     *
     * @param id    主键
     * @param model 数据模型
     * @return 配置管理
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object delete(@PathVariable String id, Model model) {
        configService.deleteCascadeById(id);
        return "success";
    }

    /**
     * 查询子节点
     *
     * @param parentId 父节点ID
     * @param model    数据模型
     * @return 子节点列表
     */
    @RequestMapping(value = "/tree/data")
    @ResponseBody
    public List<TreeVO> getChildren(@RequestParam String parentId, Model model) {
        Map<String, Object> condition = Maps.newHashMap();
        condition.put("parentId", parentId);
        List<ConfigVO> configs = configService.find(condition);
        List<TreeVO> treeNodes = Lists.newArrayList();
        if (configs != null) {
            for (ConfigVO config : configs) {
                treeNodes.add(config.getTreeNode());
            }
        }
        return treeNodes;
    }

    /**
     * 查询列表
     *
     * @param keyword    查询关键词
     * @param parentId   父节点ID
     * @param pageNumber 分页对象
     * @param pageSize   分页对象
     * @param model      数据模型
     * @return 列表
     */
    @RequestMapping(value = "/list/data")
    @ResponseBody
    public Object getList(@RequestParam(required = false) String keyword, @RequestParam(required = false) String parentId,
                          @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                          @RequestParam(value = "rows", defaultValue = "20") int pageSize, Model model) {
        // 分页查询
        List<ConfigVO> resultList = Lists.newArrayList(); // 同时包含父节点和子节点
        Map<String, Object> condition = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            condition.put("name", SqlUtils.likeClause(keyword));
        }
        if (StringUtils.isNotBlank(parentId)) {
            condition.put("parentId", parentId);
            ConfigVO parent = configService.findById(parentId);
            if (null != parent && (keyword == null || (keyword != null && parent.getName().contains(keyword)))) {
                resultList.add(parent);
            }
        }
        Page<ConfigVO> pageData = configService.findPage(condition, pageNumber, pageSize);
        resultList.addAll(pageData.getContent());
        // 拼装符合 datagrid 格式的数据
        JSONObject rst = new JSONObject();
        rst.put("total", pageData.getTotalElements());
        rst.put("rows", resultList);
        return rst.toJSONString();
    }

    /**
     * 根据配置项键格式化名称
     *
     * @return
     */
    @RequestMapping(value = "/format")
    @ResponseBody
    public Object getNameByConfigKey(@RequestParam String configKey) {

        if (configKey != null || !"".equals(configKey)) {
            List<ConfigVO> list = configService.getSubByKey(configKey);
            return JSONObject.toJSON(list);
        } else {
            return null;
        }

    }


}
