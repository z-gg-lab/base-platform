package com.hdos.platform.base.department.service;

import com.hdos.platform.base.department.mapper.DepartmentMapper;
import com.hdos.platform.base.department.model.DepartmentVO;
import com.hdos.platform.base.user.model.AccountInfoVO;
import com.hdos.platform.common.util.CodeGenerateUtils;
import com.hdos.platform.common.vo.TreeVO;
import com.hdos.platform.core.base.BaseService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机构管理
 *
 * @author tail
 */
@Service
@Transactional
public class DepartmentService extends BaseService<DepartmentVO> {

    /**
     * 菜单编辑类型：新增
     */
    private static final int EDIT_TYPE_ADD = 0;

    /**
     * 菜单编辑类型：添加同级机构
     */
    private static final int EDIT_TYPE_ADD_SAMELEVEL_DEPARTMENT = 1;

    /**
     * 菜单编辑类型：添加下级机构
     */
    private static final int EDIT_TYPE_ADD_LOWLEVEL_DEPARTMENT = 2;

    /**
     * 菜单编辑类型：添加机构
     */
    private static final int EDIT_TYPE_ADD_DEPARTMENT = 3;

    /**
     * 菜单编辑类型：修改
     */
    private static final int EDIT_TYPE_UPDATE = 4;

    @Autowired
    private DepartmentMapper mapper;

    private final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    /**
     * 根据父节点id获取机构
     *
     * @param parentId
     * @return
     */
    public List<TreeVO> queryTreeByParentId(String parentId) {

        List<DepartmentVO> list = mapper.queryByParentId(parentId);

        return departmentListToTreeList(list);
    }

    /**
     * 根据父节点id获取所有机构
     *
     * @param parentId
     * @return
     */
    public List<TreeVO> queryAllTreeByParentId(String parentId) {

        List<DepartmentVO> list = mapper.queryAllByParentId(parentId);
        return departmentListToTreeList(list);
    }

    /**
     * 根据编辑类型，组装departmentVO
     *
     * @param departmentId
     * @param editType
     * @return
     */
    public DepartmentVO buildDepartmentVOByEditType(String departmentId, int editType) {

        // 读取当前参考的机构VO
        DepartmentVO departmentVOSelected = mapper.getById(departmentId);
        DepartmentVO initDepartmentVO = new DepartmentVO();

        if (null == departmentVOSelected && EDIT_TYPE_ADD_DEPARTMENT == editType) {
            initDepartmentVO.setParentId("-1");
            initDepartmentVO.setParentName(null);
            // 2016.08.10 防止前台得不到type而报错
            initDepartmentVO.setType(BigDecimal.ZERO);
        }
        // 新增同级机构
        if (EDIT_TYPE_ADD_SAMELEVEL_DEPARTMENT == editType) {
            // 获取父级id
            initDepartmentVO.setParentId(departmentVOSelected.getParentId());
            initDepartmentVO.setParentName(mapper.getDepartmentName(departmentVOSelected.getParentId()));
            initDepartmentVO.setType(BigDecimal.ZERO);
        }

        // 新增下级机构
        if (EDIT_TYPE_ADD_LOWLEVEL_DEPARTMENT == editType) {
            // 设置父级id
            initDepartmentVO.setParentId(departmentVOSelected.getDepartmentId());
            initDepartmentVO.setParentName(mapper.getDepartmentName(departmentVOSelected.getDepartmentId()));
            initDepartmentVO.setType(BigDecimal.ZERO);
        }

        // 修改
        if (EDIT_TYPE_UPDATE == editType) {
            initDepartmentVO = departmentVOSelected;
            initDepartmentVO.setParentName(mapper.getDepartmentName(departmentVOSelected.getParentId()));
        }
        return initDepartmentVO;
    }

    /**
     * 保存机构
     *
     * @param departmentVO
     */
    public Map<String, Object> saveDepartment(DepartmentVO departmentVO) {
    	Map<String, Object> result = new HashMap<String, Object>();
        int editType;
        if (null == departmentVO) {
            result.put("msg","保存机构失败");
            return result;
        }

        // 2016-10-25同级机构不同名
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("departmenId", departmentVO.getDepartmentId() == "" ? "null" : departmentVO.getDepartmentId());
        condition.put("parentId", departmentVO.getParentId());
        condition.put("departmentName", departmentVO.getDepartmentName());

        if (mapper.count(condition) > 0) {
        	result.put("msg","该机构名称已存在");
        	return result;
//        	throw new RuntimeException("该机构名称已存在");
        }
        departmentVO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        departmentVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        if (StringUtils.isNotEmpty(departmentVO.getDepartmentId())) {

            // 更新
            if (!verifyDepartmentCodeExceptSelf(departmentVO.getDepartmentCode(), departmentVO.getDepartmentId())) {
            	result.put("msg","该机构编码已存在");
            	return result;
            }
            mapper.update(departmentVO);
            if (!"-1".equals(departmentVO.getParentId())) {
                mapper.updateFname(departmentVO);
                departmentVO.setFullName(
                        departmentVO.getFullName().substring(0, departmentVO.getFullName().lastIndexOf(">") + 1)
                                + departmentVO.getDepartmentName());
            } else {
                departmentVO.setFullName(departmentVO.getDepartmentName());
            }

            // 更新下级机构的fullName
            if (mapper.countSubDepartments(departmentVO.getDepartmentId()) > 0) {
                this.updateSubFullname(departmentVO);
            }

            editType = EDIT_TYPE_UPDATE;
        } else {
            // 新增
            departmentVO.setDepartmentId(((generateKey(departmentVO))));
            // 20161117 增加机构编码
            //可手动输入机构编码2016年12月29日
            if (StringUtils.isEmpty(departmentVO.getDepartmentCode())) {
                departmentVO.setDepartmentCode(CodeGenerateUtils.getCodeGenerator("departmentCode", null));
                //自动生成如果与手工填写重复，则添加标识
                if (!verifyDepartmentCode(departmentVO.getDepartmentCode())) {
                	departmentVO.setDepartmentCode(departmentVO.getDepartmentCode()+"_2");
                }
            } else {
                //校验用户编码是否唯一
                if (!verifyDepartmentCode(departmentVO.getDepartmentCode())) {
                	result.put("msg","该机构编码已存在");
                	return result;
                }
            }
            if ("-1".equals(departmentVO.getParentId())) {
                departmentVO.setFullName(departmentVO.getDepartmentName());
            }
            mapper.insert(departmentVO);

            if (!"-1".equals(departmentVO.getParentId())) {
                mapper.updateFname(departmentVO);
            }
            editType = EDIT_TYPE_ADD;
        }
        departmentVO.setEditType(editType);
        result.put("msg","success");
        return result;
    }

    /**
     * 校验用户编码是否唯一，排除自身
     *
     * @param departmentCode
     * @param departmentId
     * @return
     */
    private boolean verifyDepartmentCodeExceptSelf(String departmentCode, String departmentId) {
        return mapper.verifyDepartmentCodeExceptSelf(departmentCode, departmentId) >= 1 ? false : true;
    }

    /**
     * 校验用户编码是否唯一
     *
     * @param departmentCode
     */
    private boolean verifyDepartmentCode(String departmentCode) {
        return mapper.verifyDepartmentCode(departmentCode) >= 1 ? false : true;
    }


    /**
     * 修改子机构的fullName
     *
     * @param departmentVO
     */
    private void updateSubFullname(DepartmentVO departmentVO) {
        List<DepartmentVO> listSub = mapper.queryByParentId(departmentVO.getDepartmentId());
        if (listSub.size() != 0) {
            for (DepartmentVO vo : listSub) {
                vo.setFullName(departmentVO.getFullName() + ">" + vo.getDepartmentName());
                mapper.updateFullName(vo);
                this.updateSubFullname(vo);
            }
        }
    }

    /**
     * 根据parentId获取机构
     *
     * @param parentId
     * @return
     */
    public List<DepartmentVO> queryByParentId(String parentId) {

        List<DepartmentVO> list = mapper.queryByParentId(parentId);

        for (DepartmentVO departmentVO : list) {
            if (mapper.countSubDepartments(departmentVO.getDepartmentId()) == 0) {
                departmentVO.setState("open");
            }
        }

        return list;
    }

    /**
     * 删除机构
     *
     * @param departmentId
     * @return
     * @throws Exception
     */
    public void delete(String departmentId) throws Exception {

        int count = mapper.verifyUser(departmentId);

        if (count > 0) {
            throw new RuntimeException("该机构下有用户");
        }
        mapper.deleteByDepartmentId(departmentId);
    }

    /**
     * 根据departmentId 查询其下子机构的个数
     *
     * @param departmentId
     * @return
     */
    public int countSubDepartments(String departmentId) {
        return mapper.countSubDepartments(departmentId);
    }

    /**
     * departmentVO转treeVO
     *
     * @param departmentVO
     * @return
     */
    private TreeVO departmentVOToTreeVO(DepartmentVO departmentVO) {

        TreeVO treeVO = new TreeVO();
        treeVO.setId(departmentVO.getDepartmentId());
        treeVO.setText(departmentVO.getDepartmentName());
        // 控制机构树节点的状态
        if ("-1".equals(departmentVO.getParentId())) {
            treeVO.setState(TreeVO.STATE_CLOSED);
        } else {
            treeVO.setState(TreeVO.STATE_OPEN);
        }
        return treeVO;
    }

    /**
     * departmentList转treeList
     *
     * @param list
     * @return
     */
    private List<TreeVO> departmentListToTreeList(List<DepartmentVO> list) {

        List<TreeVO> treeList = new ArrayList<TreeVO>();
        for (DepartmentVO departmentVO : list) {
            TreeVO treeVO = departmentVOToTreeVO(departmentVO);
            treeList.add(treeVO);
        }
        return treeList;
    }

    /**
     * 获取第一个部门的名字和id
     *
     * @return
     */
    public String searchFirstDepartment() {

        return mapper.searchFirstDepartment();
    }

    /**
     * 获取第一个名字
     *
     * @return
     */
    public String getFirstName() {

        return mapper.getFirstName();
    }

    /**
     * 获取部门信息
     *
     * @param departmentId
     * @return
     */
    public DepartmentVO getById(String departmentId) {
        return mapper.getById(departmentId);
    }

    /**
     * 获取商户信息
     *
     * @param merchantId
     * @return
     */
    public AccountInfoVO getByMerchantId(String merchantId) {
        return mapper.getByMerchantId(merchantId);
    }

    /**
     * 获取部门id
     *
     * @param fullName
     * @return
     */
    public String getIdByFullName(String fullName) {
        return mapper.getIdByFullName(fullName);
    }

    /**
     * 获取部门id
     *
     * @param departmentCode
     * @return
     */
    public String getIdByDepartmentCode(String departmentCode) {
        return mapper.getIdByDepartmentCode(departmentCode);
    }
}
