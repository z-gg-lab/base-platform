package com.hdos.platform.base.widgetmanager.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.base.fileuploader.mapper.FileUploaderMapper;
import com.hdos.platform.base.fileuploader.model.FileVO;
import com.hdos.platform.base.widgetmanager.mapper.WidgetManagerMapper;
import com.hdos.platform.base.widgetmanager.model.WidgetManagerVO;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.page.PageImpl;
import com.hdos.platform.common.util.ConfigUtils;
import com.hdos.platform.core.base.BaseService;

@Service
@Transactional
public class WidgetManagerService extends BaseService<WidgetManagerVO> {
    private static final Logger logger = LoggerFactory.getLogger(WidgetManagerService.class);
    @Autowired
    private WidgetManagerMapper widgetManagerMapper;
    
    @Autowired
    private FileUploaderMapper fileUploaderMapper;

    /**
     * 分页查询
     *
     * @param condition  条件
     * @param pageNumber 页码，从 1 开始
     * @param pageSize   每页条数
     * @return
     */
    public Page<WidgetManagerVO> findPage(Map<String, Object> condition, int pageNumber, int pageSize) {
        int total = widgetManagerMapper.count(condition);
        RowBounds rowBounds = new RowBounds((pageNumber - 1) * pageSize, pageSize);
        List<WidgetManagerVO> content = total > 0 ? widgetManagerMapper.list(condition, rowBounds) : new ArrayList<WidgetManagerVO>(0);
        return new PageImpl<WidgetManagerVO>(content, pageNumber, pageSize, total);
    }

    /**
     * 按id查询
     *
     * @return
     */
    public WidgetManagerVO readWidgetById(String widgetId) {
        return widgetManagerMapper.getById(widgetId);
    }

    public int checkOverrideWidget(String widgetVersion, String widgetType) {
        return widgetManagerMapper.validWidgetNameAndVersion(widgetVersion, widgetType);
    }

    /**
     * @param widgetVersion
     * @param widgetType
     * @param targetFile
     * @param widgetName
     * @param widgetPath
     * @param operator
     * @param operatorId
     * @return
     */
    public String saveUpLoad(String widgetVersion, String widgetType, String widgetName, String widgetPath, String operator, String operatorId) {

        WidgetManagerVO widgetManagerVO = new WidgetManagerVO();
        widgetManagerVO.setWidgetName(widgetName);
        widgetManagerVO.setWidgetType(widgetType);
        widgetManagerVO.setWidgetVersion(widgetVersion);
        widgetManagerVO.setOperator(operator);
        widgetManagerVO.setOperatorId(operatorId);
        widgetManagerVO.setWidgetPath(widgetPath);
        widgetManagerVO.setWidgetId(generateKey(widgetManagerVO));
        widgetManagerVO.setCreateTime(new Timestamp(new Date().getTime()));
        widgetManagerMapper.insert(widgetManagerVO);

        return "success";
    }

    /**
     * 判断是否有最新的widget控件
     *
     * @param versions
     * @return
     */
    public String hasLastedVersionWidget(String widgetVersions) {
        String isHasLasted = "false";
        if(widgetManagerMapper.getLastedWidgetManagerVOs().isEmpty()){
        	return "false";
        } 
      
        	if("".equals(widgetVersions) || widgetVersions == null){
        		return "true";
        	}
            String[] versionAndType = widgetVersions.split(";");
            int size = widgetManagerMapper.getLastedWidgetManagerVOs().size();
            if (size > versionAndType.length) {
                return "true";
            }
            for (int i = 0; i < versionAndType.length; i++) {
                String oldWidgetVersion = versionAndType[i].split(",")[0];
                String widgetType = versionAndType[i].split(",")[1];
                WidgetManagerVO widgetManagerVO = widgetManagerMapper.getLastedOcxWidget(widgetType);
                if(widgetManagerVO != null){
                	  String widgetVersion = widgetManagerVO.getWidgetVersion();
                	  if (!oldWidgetVersion.equalsIgnoreCase(widgetVersion)) {
                          isHasLasted = "true";
                      } 
                }else{
                	isHasLasted = "true";
                }
              //  String widgetVersion = widgetManagerMapper.getLastedOcxWidget(widgetType).getWidgetVersion();
            }
       // }
        return isHasLasted;
    }

    public Page<WidgetManagerVO> findNewWidget() {
        List<WidgetManagerVO> WidgetManagerVOList = widgetManagerMapper.getLastedWidgetManagerVOs();
        for (WidgetManagerVO widgetManagerVO : WidgetManagerVOList) {
            //if (ConfigUtils.get("DEVICE_OCX").equals(widgetManagerVO.getWidgetType())) {
               // widgetManagerVO.setWidgetExplain("OCX控件未注册或者IE未配置(IE浏览器)");
            //} else {
                widgetManagerVO.setWidgetExplain("控件未注册或未配置");
           // }
        }
        return new PageImpl<WidgetManagerVO>(WidgetManagerVOList);
    }


    public Page<WidgetManagerVO> findLastedWidget(String widgetVersions) {
        List<WidgetManagerVO> widgetManagerVOList = widgetManagerMapper.getLastedWidgetManagerVOs();
        String versionAndType[] = widgetVersions.split(";");
        //待查询的控件
        List<WidgetManagerVO> widgetList = new ArrayList<WidgetManagerVO>();
        for (int i = 0; i < versionAndType.length; i++) {
            String widgetType = versionAndType[i].split(",")[1];
            String widgetVersion = versionAndType[i].split(",")[0];
            WidgetManagerVO WidgetManagerVO = new WidgetManagerVO();
            WidgetManagerVO.setWidgetVersion(widgetVersion);
            WidgetManagerVO.setWidgetType(widgetType);
            widgetList.add(i, WidgetManagerVO);
        }
        if (widgetList.size() != 0 && widgetManagerVOList.size() != 0) {
            for (int i = 0; i < widgetList.size(); i++) {
                for (int j = 0; j < widgetManagerVOList.size(); j++) {
                    if (widgetManagerVOList.get(j).getWidgetType().equals(widgetList.get(i).getWidgetType())) {
                        if (widgetManagerVOList.get(j).getWidgetVersion().equalsIgnoreCase(widgetList.get(i).getWidgetVersion())) {
                            widgetManagerVOList.get(j).setWidgetVersion("-1");       
                        } else {
                            widgetManagerVOList.get(j).setOldWidgetVersion(widgetList.get(i).getWidgetVersion());
                        }
                    }
                }
            }
            int j = 0;
            while (j < widgetManagerVOList.size()) {
                if (("-1").equals(widgetManagerVOList.get(j).getWidgetVersion())) {
                    widgetManagerVOList.remove(j);
                } else {
                    j++;
                }
            }
        }

        return new PageImpl<WidgetManagerVO>(widgetManagerVOList);
    }


    public String deleteWidget(String widgetId) {
    	WidgetManagerVO widgetManagerVO = widgetManagerMapper.getById(widgetId);
    	if(widgetManagerVO == null){
    		return "控件已经不存在";
    	}else{
    		widgetManagerMapper.delete(widgetId);
    		return "success";
    	}
    	
    }

    public int checkOverrideWidgetFileName(String originalFilename) {
        return widgetManagerMapper.getCountOfFileName(originalFilename);
    }

 
	public String saveWidget(String widgetVersion, String widgetType, String fileId,String userName, String userId) {
		   //判断控件版本是否重复
			int count = widgetManagerMapper.validWidgetNameAndVersion(widgetVersion, widgetType);
			if (count > 0) {
				return "同一控件类型下版本不允许重复";
			}
			FileVO fileVO = fileUploaderMapper.getById(fileId);
			WidgetManagerVO widgetManagerVO = new WidgetManagerVO();
		    widgetManagerVO.setFileId(fileId);
	        widgetManagerVO.setWidgetName(fileVO.getFileName());
	        widgetManagerVO.setWidgetType(widgetType);
	        widgetManagerVO.setWidgetVersion(widgetVersion);
	        widgetManagerVO.setOperator(userName);
	        widgetManagerVO.setOperatorId(userId);
	        widgetManagerVO.setWidgetPath(fileVO.getDiscPath());
	        widgetManagerVO.setWidgetId(generateKey(widgetManagerVO));
	        widgetManagerVO.setCreateTime(new Timestamp(new Date().getTime()));
	        widgetManagerMapper.insert(widgetManagerVO);

	        return "success";
	}

}
