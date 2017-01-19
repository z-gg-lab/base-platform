<!DOCTYPE html>
<html>
	<head>
	    <title>配置项编辑</title>
	    <#include "../../include.ftl">
	</head>
	<body style="overflow-y: hidden" scroll="no">
	    <form id="configEditForm" action="${webRoot}/config/save" method="post">
	    	<table class="table-list">
	    		<tr>
	    			<td class="t-title" width="30%">上级配置项：</td>
	    			<td>
	    				${(parent.name)!''}
	    				<input type="hidden" id="configId" name="id" value="${(data.id)!''}"/>
	    				<input type="hidden" name="parentId" value="${(parent.id)!''}"/>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">配置项名：<font style="color:red">*</font></td>
	    			<td><input class="easyui-textbox" data-options="prompt:'请输入配置项目名',required:false" maxlength="50" id="_name" name="name" value="${(data.name)!''}"/></td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">配置项键：<font style="color:red">*</font></td>
	    			<td><input class="easyui-textbox" data-options="prompt:'请输入配置项键',required:false" iconAlign='right' maxlength="50"  id="_configKey" name="configKey" value="${(data.configKey)!''}"/></td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">配置项值：</td>
	    			<td><input class="easyui-textbox"maxlength="100" name="configValue" value="${(data.configValue)!''}" /></td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">配置项描述：</td>
	    		<td><input name="description" class="easyui-textbox" data-options="multiline:true" style="height:50px;" value="${(data.description)!''}"/></td>
	    		</tr>
	    		<#if data.id??>
	    		<tr>
	    			<td class="t-title" width="30%">排序：</td>
	    			<td><input class="easyui-textbox" type="text" maxlength="100" id="sortNo" name="sortNo" value="${(data.sortNo)!''}" /></td>
	    		</tr>
	    		</#if>
	    		<tr>
	    			<td colspan="2">
	    				<span style="size:7px;">注：标<font style="color:red">*</font>的为必填项！</span>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td colspan="2" align="center">
	    				<a href="#" class="easyui-linkbutton" iconcls="icon-save" onclick="saveConfig()">保存</a>
	    				<a href="#" class="easyui-linkbutton" iconcls="icon-cancel" onclick="closeWindow()">取消</a>
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	    <script>
	    
	    var sortNo = $('#sortNo').val();
	    
	    function saveConfig() {
	    
	    	$('#_name').textbox({required:true});
	    	$('#_configKey').textbox({required:true});
	    
	       $("#configEditForm").form({
			url: "${webRoot}/config/save",
			onSubmit: function(){
				return $(this).form('validate');
              },
			success: function(data) {
				if(data == 'duplicated') {
					// 保存失败
					$.messager.alert('错误', '配置项键已存在！', 'error');
					return;
				} else {
					
					parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#configTable').datagrid('load');
					var node = parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#configTree').tree('getSelected');
					
					if(node){
						var flag = parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#configTree').tree('isLeaf',node.target);
						if (sortNo){
							if(flag){
								node.text = $('#name').val();
								parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#configTree').tree('update',node);
							}
							if(node.id==$('#configId').val()){
								node.text = $('#name').val();
								parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#configTree').tree('update',node);
							}else{
								parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#configTree').tree('reload',node.target);
							}
							parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#selected-node-name').html(node.text);
							
						}else{ // 添加操作
							parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#configTree').tree('append', { 
                        						parent : node.target, 
                       							data :data 
                   	 							}); 
							parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#configTree').tree('reload',node.target);
						}
						
					}else{ // 没有选中节点
						parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#configTree').tree('reload');
					}
					//parent.reloadTreeNode($('#configId').val(), $('#name').val());
				}
				closeWindow();
			}
		});
		$("#configEditForm").submit();
	}
	    	
	//取消
	function closeWindow() {
		parent.$("#dialog").dialog("close");
	}
	    </script>
	</body>
</html>