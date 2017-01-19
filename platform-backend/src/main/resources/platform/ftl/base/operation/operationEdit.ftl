<!DOCTYPE html>
<html>
	<head>
	    <title>功能编辑</title>
	    <#include "../../include.ftl">
	</head>
	<body style="overflow-y: hidden" scroll="no">
	    <form id="operationEditForm" method="post">
	    	<table class="table-list">
	    		<tr>
	    			<td class="t-title" width="30%">功能名称：<font style="color:red">*</font></td>
	    			<td>
	    				<input type="hidden" maxlength="100" name="menuId" value="${(operationVO.menuId)!''}"/>
	    				<input type="hidden" maxlength="100" name="operationId" value="${(operationVO.operationId)!''}"/>
	    				<input class="easyui-textbox" data-options="prompt:'请输入功能名称',required:false" maxlength="100" id="_operationName" name="operationName" value="${(operationVO.operationName)!''}"/></td>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">功能编码：<font style="color:red">*</font></td>
	    			<td>
	    				<input class="easyui-textbox" data-options="prompt:'请输入功能编码',required:false" maxlength="100" id="_operationCode" name="operationCode" value="${(operationVO.operationCode)!''}"/></td>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">功能说明：</td>
	    			<td><input class="easyui-textbox" name="remark" data-options="multiline:true" style="height:50px;" value="${(operationVO.remark)!''}"/></td>
	    		</tr>
	    		<tr>
	    			<td colspan="2">
	    				<span style="size:7px;">注：标<font style="color:red">*</font>的为必填项！</span>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td colspan="2" align="center">
	    				<a href="#" class="easyui-linkbutton" iconcls="icon-save" onclick="saveOperation()">保存</a>
	    				<a href="#" class="easyui-linkbutton" iconcls="icon-cancel" onclick="closeWindow()">取消</a>
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	    <script>
	    
	    	// 保存
	    	
	    	function saveOperation() {
	    	
	    		$('#_operationName').textbox({required:true});
	    		$('#_operationCode').textbox({required:true});
	    		
	    	    $("#operationEditForm").form({
	    			url:"${webRoot}/operation/save",
	    			onSubmit: function(){
	    				return $(this).form('validate');
		              },
	    			 success:function(data) {
						if(data == '0' && data != null) {
    						parent.$('#operationTable').datagrid('load');
    					} else {
    						parent.$('#operationTable').datagrid('reload');
    					}
	    				closeWindow();
	    			}
	    		});
	    		$("#operationEditForm").submit();
	    	}
	    
	    	//取消
	    	function closeWindow() {
	    		parent.$("#operationTable").datagrid("uncheckAll");
	    		parent.$("#editOperation").dialog("close");
	    	}
	    </script>
	</body>
</html>