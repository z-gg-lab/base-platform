<!DOCTYPE html>
<html>
<head>
    <title>编码管理</title>
    <#include "../../../include.ftl">
</head>
<body style="overflow-y: hidden" scroll="no">
	<form id="templateForm" class="easyui-form" method="post">
    	<table class="table-list" >
			
			<tr>
				<td class="t-title" width="30%">模板名称：<font style="color:red">*</font></td>
				<td>
					<input id="excelImportTemplateId" name="excelImportTemplateId" type="hidden" value="${(excelImportTemplateVO.excelImportTemplateId)!''}"/>
					<input id="templateName" name="templateName" class="easyui-textbox" prompt="请输入模板名称" data-options="required:false,validType:['specialCharactersComma','lengthCharacter[25,50]']" value="${(excelImportTemplateVO.templateName)!''}"/>
				</td>
			</tr>
			
			<tr>
				<td class="t-title" width="30%">模板Key：<font style="color:red">*</font></td>
				<td>
					<input id="excelKey" name="excelKey" class="easyui-textbox" prompt="请输入模板Key" data-options="required:false,validType:['noChinese','specialCharactersComma','lengthCharacter[25,50]']" value="${(excelImportTemplateVO.excelKey)!''}"/>
				</td>
			</tr>
			
			
			
			<tr>
				<td class="t-title" width="30%">对应表名：<font style="color:red">*</font></td>
				<td>
					<input  class="easyui-textbox" id="tableName" name="tableName" data-options="required:false,validType:['specialCharactersComma','lengthCharacter[25,50]']" prompt="请输入对应表名"  value="${(excelImportTemplateVO.tableName)!''}"/>
				</td>
			</tr>
			
			<tr>
				<td class="t-title" width="30%">校验规则：<font style="color:red">*</font></td>
				<td>
					<input  class="easyui-combobox" id="rule" name="rule" data-options="required:true" validType="length[0,100]" value="${(excelImportTemplateVO.rule)!''}"/>
				</td>
			</tr>
			
			<tr>
				<td class="t-title" width="30%">模板配置：<font style="color:red">*</font></td>
				<td>
					<input id="config" name="config" type="hidden"/>
					<a href="#"  onclick="addTemplateConfig()">添加配置</a>
				</td>
			</tr>
			
	    
	    	<tr>
				<td colspan="2">
					<span style="size:7px;">注：标<font style="color:red">*</font>的为必填项！</span>
				</td>
			</tr>
			
			<tr>
				<td colspan="2" align="center">
					<a href="#" class="easyui-linkbutton" iconcls="icon-save" onclick="saveCode()">保存</a>
					<a href="#" class="easyui-linkbutton" iconcls="icon-cancel" onclick="cancle()">取消</a>
				</td>
			</tr>
		</table>
    </form>
    
</body>
<script>
		parent.testExcel = null;
		parent.excelId = "${(excelImportTemplateVO.excelImportTemplateId)!''}";
        var tableName;

		// 校验规则下拉框	
		$(function(){
			$.ajax({ 
				url: '${webRoot}/component/excelImportTemplate/getcombobox',
				dataType: 'json', 
				success: function(data){   
					// 修改ajax返回的值
					data.unshift({'configValue':'','name':'-请选择-'});   //unshift方法添加到第一行，push方法添加到末尾
					$('#rule').combobox({ 
						data: data,   
						valueField:'configValue',    
						textField:'name',
						editable:false,
						panelHeight:100,
					});   
				}
			});
		});
		
		// 添加配置
		function addTemplateConfig() {
			tableName = $("#tableName").textbox('getValue');
			if(!tableName){
				$.messager.alert('提示', '请输入表名！', 'info');
				return;
			}
			parent.testExcel = tableName;
			var url = "${webRoot}/component/excelImportTemplate/config";
			parent.$("#win").dialog({
				title:"添加模板配置",
				content:createFrame(url),
				width:550,    
				height:500,
				top:270,
				left:640,
                onClose:function () {

                }
			});
			parent.$("#win").dialog("open"); 
		}


		function createFrame(url) {
			var s = '<iframe id="conf"  scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
			return s;
		}
		
		//保存模板
		function saveCode() {
			$("#templateName").textbox({required:true});
			$("#tableName").textbox({required:true});
			$("#excelKey").textbox({required:true});
			if(tableName==parent.testExcel){
                $("#config").val(JSON.stringify(null));
			}else {
                $("#config").val(JSON.stringify(parent.testExcel));
			}
		   	$("#templateForm").form({
				url:"${webRoot}/component/excelImportTemplate/createtemplate",
				onSubmit: function(){
					if($('#rule').combobox('getValue')==""){
						$('#rule').combobox('clear');
						return $(this).form('validate');
					}
					return $(this).form('validate');
		        },
			 	success:function(data) {
			 		if(data=="fail"){
			 			$.messager.alert('提示', '该模板已经存在', 'info');
			 		}else{
				 		cancle();
			 		}
			 	}
			});
			
			$("#templateForm").submit();
		}
		
		function cancle(){
			closeDialog("dialog");
			parent.reloadData("codeTable");
			parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$("#codeTable").datagrid("uncheckAll");
		}

</script>
</html>