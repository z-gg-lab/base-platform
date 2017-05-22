<!DOCTYPE html>
<html>
	<head>
	    <title>操作日志配置编辑</title>
	    <#include "../../include.ftl">
	</head>
	<body style="overflow-y: hidden" scroll="no">
	    <form id="operationConfigForm" action="${webRoot}/operationlogconfig/save" method="post">
	    	<table class="table-list">
	    		<tr>
	    			<td class="t-title" width="30%">操作类型：<font style="color:red">*</font></td>
	    			<td>
	    				<input type="text" maxlength="100" name="logcfgOper" value="${(userLogConfigVO.logcfgOper )!''}"/>
	    				<input type="hidden" name="logcfgId" value="${(userLogConfigVO.logcfgId)!''}"/>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">模板标识：<font style="color:red">*</font></td>
	    			<td><input type="text" maxlength="2000" name="logcfgMark" value="${(userLogConfigVO.logcfgMark)!''}"/></td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">定义描述：</td>
	    			<td>
	    				<textarea maxlength="250" rows="3" name="logcfgDesc" value="${(userLogConfigVO.logcfgDesc)!''}"></textarea>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">成功内容模板：</td>
	    			<td><textarea maxlength="2000" rows="3" name="logcfgSuccess" >${(userLogConfigVO.logcfgSuccess)!''}</textarea></td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">失败内容模板：</td>
	    			<td><textarea maxlength="2000" rows="3" name="logcfgFailed" >${(userLogConfigVO.logcfgFailed)!''}</textarea></td>
	    		</tr>
	    		<tr>
	    			<td colspan="2">
	    				<span style="size:7px;">注：标<font style="color:red">*</font>的为必填项！</span>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td colspan="2" align="center">
	    				<a href="#" class="easyui-linkbutton" iconcls="icon-save" onclick="saveOperationLogConfig()">保存</a>
	    				<a href="#" class="easyui-linkbutton" iconcls="icon-cancel" onclick="closeWindow()">取消</a>
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	    <script>
	    	//保存菜单
	    	function saveOperationLogConfig() {
	    	
	    		$.ajax({
	    			type:"post",
	    			url:"${webRoot}/operationlogconfig/save",
	    			data:$("#operationConfigForm").serialize(),
	    			async:false,
	    			error:function(request) {
	    			},
	    			success:function(data) {
	    				parent.$('#operationLogConfig').datagrid('reload');
	    				closeWindow();
	    			}
	    		});
	    	}
	    
	    	//取消
	    	function closeWindow() {
	    	debugger;
	    		parent.$("#editOperationConfig").dialog("close");
	    	}
	    </script>
	</body>
</html>