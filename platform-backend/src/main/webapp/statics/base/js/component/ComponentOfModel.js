//设备类型选择控件 <input class="easyui-combobox" value="请选择" id="model" name="deviceName" />
	$(function(){
		$("#model").combobox({
			url:window.webRoot+"/model/modelSelect",
			valueField:'modelCode',
			textField:'modelName',
			panelMaxHeight:185, 
			panelHeight:'auto',
		})
	});
