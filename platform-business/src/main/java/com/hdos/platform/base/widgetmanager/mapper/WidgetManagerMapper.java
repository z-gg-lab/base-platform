package com.hdos.platform.base.widgetmanager.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hdos.platform.base.widgetmanager.model.WidgetManagerVO;
import com.hdos.platform.core.base.BaseMapper;
@Repository
public interface WidgetManagerMapper extends BaseMapper<WidgetManagerVO> {

	/**
	 * 获取最新的版本
	 * @param widgetVersion
	 * @return
	 */
	//String getWidgetType(String widgetVersion);

	/**
	 * 判断是否重名
	 * @param widgetName
	 * @param widgetVersion
	 * @return
	 */
	int validWidgetNameAndVersion(String widgetVersion,String widgetType);

	/**
	 * 获取最新控件的类型
	 * @return
	 */
	//String getLastedWidgetTypeValue();

	/**
	 * 获取所有最新控件
	 * @return
	 */
	List<WidgetManagerVO> getLastedWidgetManagerVOs();

 

	//int getWidgetCount();

	WidgetManagerVO getLastedOcxWidget(String widgetType);

	//WidgetManagerVO getLastedCommonWidget(String widgetType);


	//List<String> getWidgetTypes();

	int getCountOfFileName(String widgetName);

}
