
<!DOCTYPE html>
<html>
	<head>
	    <title>excel导入模板</title>
	    <#include "../../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/component/exportExcel.js"></script>
	</head>
	<body style="overflow-y: hidden"  scroll="no">
		
  		
  		<div id="dialog" class="easyui-dialog" closed="true" data-options="modal:true">
			<form id="f1" method="post" action="${webRoot}/component/excelImportTemplate/upload" enctype="multipart/form-data">
				<br>
				<input class="easyui-filebox" name="file" id="file"/>
				<a href="#" class="easyui-linkbutton" iconcls="icon-ok" plain="true" onclick="upload()">上传</a>
  		</form>
		
		</div>
	
	         <table id="codeTable" class="easyui-datagrid" title="excel模板信息" pageSize="20" style="padding:30px;" fit=true
	            data-options="singleSelect:false,collapsible:false,
				url:'${webRoot}/component/excelImportTemplate/queryTemplate',
				pagination:true,
				toolbar:'#tb',
				idField:'id',
				method:'post',
				remoteSort:false,
				multiSort:true,
				rownumbers:true, 
	            ">
			<div id="tb" style="padding:4px;height:30px;">
				<a href="#" class="easyui-linkbutton" iconcls="icon-redo" plain="true" id = "downExcel" onclick="downloadTemplate()">模板下载</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-add" plain="true" onclick="addTemplate()">添加模板</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-remove" plain="true" onclick="deleteTemplate()">删除</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-redo" plain="true" id = "exportExcel" onclick="exportTest()">导出为excel</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-redo" plain="true" id = "uploadTest" onclick="uploadTest()">上传excel测试</a>
			</div>
			
			<thead>
				<tr>
					<th data-options="field:'excelImportTemplateId',width:80,align:'left',checkbox:true">ID</th>
					<th data-options="field:'templateName',width:200,align:'center',title:'模板名称',formatter:formateTemplateEdit"></th>
					<th data-options="field:'tableName',width:140,align:'left'">对应表名</th>
					<th data-options="field:'createTime',width:140,align:'center',formatter:formatTimeYYYYMMDDHHMMSS">创建时间</th>
					<th data-options="field:'rule',width:140,align:'center'">校验规则</th>
					<th data-options="field:'excelKey',width:250,align:'center'">唯一标识</th>
				</tr>
			</thead>
		</table>
		<script>
		$(function(){ $('#codeTable').datagrid({ onClickCell:function(rowIndex, field, value){ if(field == "templateName"){ $(this).datagrid('clearSelections'); $(this).datagrid('onUnselectAll'); } } }); });
		
		//生成
		function downloadTemplate() {
			var row = $('#codeTable').datagrid('getChecked');
			if(!row||row.length==0) {
				$.messager.alert('提示','请选择需要的下载的模板!','info');
				return;
			}
			var url = "${webRoot}/component/excelImportTemplate/download?templateName="+row[0].templateName+"&id="+row[0].excelKey
			$("#downExcel").attr("href",url); 
		}
		
		function exportTest(){
			var id = "codeTable";
			var exportExcel = "exportExcel";
			exportToExcel(id,exportExcel);
		}
		
		function uploadTest(){
			$("#dialog").dialog({
		        title: "excel上传测试",
		        width: 300,
		        height: 100,
		        top: $(window).height() * 0.3,
		        left: $(window).width() * 0.4
		       });
		
		
			$("#dialog").dialog("open");
		}
		
		//上传
		function　upload(){
			$("#f1").form({
				onSubmit:function(){
					var name = $("#file").filebox('getText');
					if(endWith(name,".xls")){
						return true;
					}else{
						$.messager.alert('提示', '请选择excel文件', 'info');
						return false;
					}
				},
				success:function(data){
					if(data=="{}"){
						$('#dialog').dialog('close');
					}else{
						$.messager.alert('提示', data, 'info');
					}
				}
			});
			$("#f1").submit();
		}
		
		
		function endWith(str1, str2){
			if(str1 == null || str2 == null){
		 		return false;
		 	}
		 	if(str1.length < str2.length){
		  		return false;
		 	}else if(str1 == str2){
		  		return true;
		 	}else if(str1.substring(str1.length - str2.length) == str2){
		  		return true;
		 	}
		 		return false;
		}
		
		
		
		// 修改
		function formateTemplateEdit(value,row,index){
				var s = '<a href="#" onclick="editTemplate(\''+row.excelImportTemplateId+'\')">'+row.templateName+'</a>';
				return s;
		}
		
		// 修改模板
		
		function editTemplate(id){
			var url = "${webRoot}/component/excelImportTemplate/edit/"+id;
			parent.$("#dialog").dialog({
				title:"修改模板",
				content:createFrame(url),
				width:380,    
				height:400,
				top:($(window).height()-310)*0.5,
				left:($(window).width()-450)*0.5
			});
			parent.$("#dialog").dialog("open");
		}
		
		//删除模板
		function deleteTemplate() {
			var ids = "";
			var row = $('#codeTable').datagrid('getChecked');
			if(!row||row.length==0) {
				$.messager.alert('提示','请选择需要的编码!','info');
				return;
			}
			for(var i = 0 ; i<row.length ; i ++) {
				ids += row[i].excelImportTemplateId + ",";
			}
    		
    		$.messager.confirm('提示','确认删除选择的模板吗？',function(r){
				if(r) {
					$.ajax({
		    			type:"post",
		    			url:"${webRoot}/component/excelImportTemplate/delete",
		    			data:{
		    				"ids":ids
		    			},
		    			async:false,
		    			success:function(data) {
		    				parent.reloadData("codeTable");
		    				$('#codeTable').datagrid("unselectAll");
		    			}
    				});
				}
			});
    		
		}

	    // 添加模板
		function addTemplate() {
			var url = "${webRoot}/component/excelImportTemplate/add";
			parent.$("#dialog").dialog({
				title:"添加模板",
				content:createFrame(url),
				width:380,    
				height:400,
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