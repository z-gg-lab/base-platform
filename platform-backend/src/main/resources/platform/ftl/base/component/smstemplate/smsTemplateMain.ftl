<!DOCTYPE html>
<html>
	<head>
	    <title>短信模板配置</title>
	    <#include "../../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
	</head>
	<body style="overflow-y: hidden"  scroll="no">
	         <table id="codeTable" class="easyui-datagrid" title="短信模板列表" pageSize="20" style="padding:30px;" fit=true
	            data-options="singleSelect:false,collapsible:false,
				url:'${webRoot}/component/smsTemplate/querySmsTemplate',
				pagination:true,
				toolbar:'#tb',
				idField:'id',
				method:'post',
				remoteSort:false,
				multiSort:true,
				rownumbers:true, 
	            ">
		            
			<div id="tb" style="padding:4px;height:30px;">
				<a href="#" class="easyui-linkbutton" iconcls="icon-add" plain="true" onclick="addSmsTemplate()">添加短信模板</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-remove" plain="true" onclick="deleteCode()">删除</a>
			</div>
			
			<thead>
				<tr>
					<th data-options="field:'smsTemplateId',width:80,align:'left',checkbox:true">smsTemplateId</th>
					<th data-options="field:'templateId',width:200,align:'center',formatter:formatecodeEdit">模板ID</th>
					<th data-options="field:'templateContent',width:200,align:'left'">模板内容</th>
					<th data-options="field:'smsSignature',width:140,align:'center'">短信签名</th>
					<th data-options="field:'createTime',width:140,align:'center',formatter:formatTimeYYYYMMDDHHMMSS">创建时间</th>
				</tr>
			</thead>
		</table>
		<script>
		
		$(function(){ $('#codeTable').datagrid({ onClickCell:function(rowIndex, field, value){ if(field == "templateId"){ $(this).datagrid('clearSelections'); $(this).datagrid('onUnselectAll'); } } }); });
		
		// 修改
		function formatecodeEdit(value,row,index){
				var s = '<a href="#" onclick="editCode(\''+row.smsTemplateId+'\')">'+row.templateId+'</a>';
				return s;
		}
		
		// 修改模板 
		
		function editCode(id){
		
			var url = "${webRoot}/component/smsTemplate/edit/"+id;
			parent.$("#dialog").dialog({
				title:"修改短信模板",
				content:createFrame(url),
				width:350,    
				height:300,
				top:($(window).height()-310)*0.5,
				left:($(window).width()-450)*0.5
			});
			parent.$("#dialog").dialog("open");
		}
		
		//删除模板
		function deleteCode() {
			var ids = "";
			var row = $('#codeTable').datagrid('getChecked');
			if(!row||row.length==0) {
				$.messager.alert('提示','请选择需要删除的短信模板 !','info');
				return;
			}
			for(var i = 0 ; i<row.length ; i ++) {
				ids += row[i].smsTemplateId + ",";
			}
			$.messager.confirm('提示','确认删除选择的模板？',function(r){
				if(r) {
					$.ajax({
	    				type:"get",
	    				url:"${webRoot}/component/smsTemplate/delete",
	    				data:{
	    					"ids":ids
	    				},
	    				async:false,
	    				success:function(data) {
	    					parent.reloadData("codeTable");
	    					$("#codeTable").datagrid('clearChecked');
	    				}
	    			});
				}
			});
		}

	    // 添加短信模板
		function addSmsTemplate() {
			var url = "${webRoot}/component/smsTemplate/add";
			parent.$("#dialog").dialog({
				title:"添加短信模板",
				content:createFrame(url),
				width:350,    
				height:300,
				top:($(window).height()-310)*0.5,
				left:($(window).width()-450)*0.5
			});
			parent.$("#dialog").dialog("open"); 
		}

		function createFrame(url) {
			var s = '<iframe id="userSelect4Role" name="userSelect4Role" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
			return s;
		}



		</script>
	</body>
</html>