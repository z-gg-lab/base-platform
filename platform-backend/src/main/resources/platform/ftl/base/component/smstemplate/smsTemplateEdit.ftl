<!DOCTYPE html>
<html>
<head>
    <title>短信模板</title>
    <#include "../../../include.ftl">
</head>
<body style="overflow-y: hidden" scroll="no">
	<form id="smsTemplateForm" class="easyui-form" method="post">
    	<table class="table-list" >
			
			<tr>
				<td class="t-title" width="30%">模板ID：<font style="color:red">*</font></td>
				<td>
					<input id="SmsTemplateId" name="smsTemplateId" type="hidden" value="${(smsTemplateVO.smsTemplateId)!''}"/>
					<input id="templateId" name="templateId" class="easyui-textbox"  prompt="请输入模板ID" data-options="required:false,validType:['specialCharacters','noChinese','lengthCharacter[18,36]']"  value="${(smsTemplateVO.templateId)!''}"/>
				</td>
			</tr>
			
			<tr>
				<td class="t-title" width="30%">模板内容：<font style="color:red">*</font></td>
				<td>
					<input id="templateContent" name="templateContent" class="easyui-textbox" prompt="请输入模板内容"  style="height:60px;" data-options="multiline:true,required:false,validType:['specialCharacters','lengthCharacter[50,100]']" value="${(smsTemplateVO.templateContent)!''}"/>
				</td>
			</tr>			    	
			
			<tr>
				<td class="t-title" width="30%">短信签名：<font style="color:red">*</font></td>
				<td>
					<input id="smsSignature" name="smsSignature"  class="easyui-textbox" prompt='请输入短信签名' data-options="required:false,validType:['specialCharacters','lengthCharacter[25,50]']" value="${(smsTemplateVO.smsSignature)!''}"/>
				</td>
			</tr>	
			
	    	<tr>
				<td colspan="2">
					<span style="size:7px;">注：标<font style="color:red">*</font>的为必填项！</span>
				</td>
			</tr>
			
			<tr>
				<td colspan="2" align="center">
					<a href="#" class="easyui-linkbutton" iconcls="icon-save" onclick="saveSmsTemplate()">保存</a>
					<a href="#" class="easyui-linkbutton" iconcls="icon-cancel" onclick="cancle()">取消</a>
				</td>
			</tr>
		</table>
    </form>
</body>
<script>
	
	// 保存编码
	function saveSmsTemplate() {
		$("#templateId").textbox({required:true});
		$("#templateContent").textbox({required:true});
		$("#smsSignature").textbox({required:true});
	
	   	$("#smsTemplateForm").form({
			url:"${webRoot}/component/smsTemplate/save",
			onSubmit: function(){
				return $(this).form('validate');
	        },
		 	success:function(data) {
			 	if(data=="succ"){
			 	
			 		cancle();
				}else {
					$.messager.alert('提示','该模板已存在','info');
				}
		 	}
		});
		
		$("#smsTemplateForm").submit();
	}
	
	function cancle(){
		closeDialog("dialog");
		parent.reloadData("codeTable");
		parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$("#codeTable").datagrid("uncheckAll");
	}


</script>
</html>