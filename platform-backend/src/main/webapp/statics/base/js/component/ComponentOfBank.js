//银行选择控件 <input style="easyui-combobox" value="请选择" id="bank" name="bankId" />  可以参考applyEdit.ftl
	$("#bank").combobox({
		url:window.webRoot+"/component/bank/bankSelect",
		valueField:'bankId',
		textField:'bankName',
		panelHeight:'auto',
		editable:false,
	})
