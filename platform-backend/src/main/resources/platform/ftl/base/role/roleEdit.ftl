<!DOCTYPE html>
<html>
	<head>
	    <title>角色编辑</title>
	    <#include "../../include.ftl">
	</head>
	<body style="overflow-y: hidden" scroll="no">
	    <form id="roleEditForm" action="${webRoot}/menu/save" method="post">
	    	<table class="table-list">
	    		<tr>
	    			<td class="t-title" width="30%">角色名：<font style="color:red">*</font></td>
	    			<td>
	    				<input type="hidden" maxlength="100" name="roleId" value="${(roleVO.roleId)!''}"/>
	    				<input class="easyui-textbox" id="roleName" data-options="required:false,validType:['specialCharacters','lengthCharacter[50,100]']" title="该输入项为必输项" style="height:20px;width:150px" name="roleName" value="${(roleVO.roleName)!''}"/>
	    			</td>
	    		<tr>
	    			<td class="t-title"  width="30%"><p >角色描述：<p></td>
	    			<td>
	    				<textarea class="easyui-textbox" data-options="multiline:true,validType:['specialCharacters','lengthCharacter[125,250]']" style="width:200px; height:100px" name="remark">${(roleVO.remark)!''}</textarea>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td colspan="2">
	    				<span style="size:7px;">注：标<font style="color:red">*</font>的为必填项！</span>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td colspan="2" align="center">
	    				<a href="#" class="easyui-linkbutton" id="save"  iconcls="icon-save" onclick="saveRole()">保存</a>
	    				<a href="#" class="easyui-linkbutton" iconcls="icon-cancel" onclick="closeWindow()">取消</a>
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	    <script>
	    	//保存角色
	    	function saveRole() { 
	    		$('#roleName').textbox({required:true});		
	    		$("#roleEditForm").form({
	    			url:"${webRoot}/role/save",
	    			success:function(data) {
	    				var roleId = data.roleId;
	    				if(data == "false"){
	    					$.messager.alert('提示','该角色已存在!','info');
	    					addRole();
	    					
	    				}
	    				if(roleId != '' && roleId != null) {
    						parent.loadData("roleTable");//调用父页面（首页）的方法
    					} else {
    						parent.reloadData("roleTable");//调用父页面（首页）的方法
    					}
	    				closeWindow();
	    			}
	    		});
	    		$("#roleEditForm").submit();
		    	}
		  
	    	//取消
	    	function closeWindow() {
	    		parent.$("#dialog").dialog("close");
	    	}
	    </script>
	</body>
</html>