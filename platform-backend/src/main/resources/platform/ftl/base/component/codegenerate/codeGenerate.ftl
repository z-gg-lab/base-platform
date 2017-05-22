<!DOCTYPE html>
<html>
<head>
    <title>编码管理</title>
    <#include "../../../include.ftl">
</head>
<body style="overflow-y: hidden" scroll="no">
	<form id="codeForm" class="easyui-form" method="post">
    	<table class="table-list" >
			
			<tr>
				<td class="t-title" width="30%">业务名称：<font style="color:red">*</font></td>
				<td>
					<input id="id" name="id" type="hidden" value="${(codeGenerateVO.id)!''}"/>
					<input id="businessName" name="businessName" class="easyui-textbox" prompt="请输入业务名称" data-options="required:false,validType:['specialCharacters','lengthCharacter[100,200]']"  value="${(codeGenerateVO.businessName)!''}"/>
				</td>
			</tr>
			<tr>
				<td class="t-title" width="30%">业务类型：<font style="color:red">*</font></td>
				<td>
					<input id="businessKey" name="businessKey" class="easyui-textbox" prompt="请输入业务类型" data-options="required:false,validType:['english','specialCharacters','lengthCharacter[18,36]']"  value="${(codeGenerateVO.businessKey)!''}"/>
				</td>
			</tr>
			
			<tr>
				<td class="t-title" width="30%">生成策略：<font style="color:red">*</font></td>
				<td>
						<input  class="easyui-combobox" id="generateType" name="generateType" required="true" value="${(codeGenerateVO.generateType)!''}"/>
			</td>
			</tr>
			
			<tr>
				<td class="t-title" width="30%">序号长度：<font style="color:red">*</font></td>
				<td>
					<input id="length" name="length" type="text" class="easyui-numberbox"  prompt="请输入序号长度" validType="length[0,5]"  data-options="required:false,min:0,max:99999" value="${(codeGenerateVO.length)!''}"/>
				</td>
			</tr>			    	
			
			<tr>
				<td class="t-title" width="30%">序号规则：<font style="color:red">*</font></td>
				<td>
					<input id="rule" name="rule" type="text"  class="easyui-textbox" prompt='{"prefix":"aaa","formatter":"yyyyMMdd","suffix":"0000"}' style="height:60px;" data-options="multiline:true,required:false,validType:['noChinese','length[0,100]']"/>
				</td>
			</tr>	
			
			<tr>
				<td class="t-title" width="30%">当前序号：<font style="color:red">*</font></td>
				<td>
					<input id="no" name="no" class="easyui-numberbox"  type="text"  prompt="请输入当前序号"  validType="length[0,10]"  data-options="required:false,min:0" value="${(codeGenerateVO.no)!'0'}"/>
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
	// 赋值
	$("#rule").attr("value",'${(codeGenerateVO.rule)!""}');
	
	// 生成策略下拉框
	$(function(){
		$.ajax({ 
			url: '${webRoot}/component/codegenerate/getcombobox',
			dataType: 'json', 
			success: function(data){   
				// 修改ajax返回的值
				data.unshift({'configValue':'','name':'-请选择-'});   //unshift方法添加到第一行，push方法添加到末尾
				$('#generateType').combobox({ 
					data: data,   
					valueField:'configValue',    
					textField:'name',
					editable:false,
					panelHeight:100,
				});   
			}
		});
	});
	
	// 保存编码
	function saveCode() {
		$("#businessName").textbox({required:true});
		$("#businessKey").textbox({required:true});
		$("#length").textbox({required:true});
		$("#rule").textbox({required:true});
		$("#no").textbox({required:true});
	
	
	   	$("#codeForm").form({
			url:"${webRoot}/component/codegenerate/save",
			onSubmit: function(){
				if($('#generateType').combobox('getValue')==""){
					$('#generateType').combobox('clear');
					return $(this).form('validate');
				}
					return $(this).form('validate');
	        },
		 	success:function(data) {
			 	if(data=="succ"){
			 		cancle();
				}else {
					$.messager.alert('提示','该业务类型或业务名称已经注册过编码','info');
				}
		 	}
		});
		
		$("#codeForm").submit();
	}
	
	function cancle(){
		closeDialog("dialog");
		parent.reloadData("codeTable");
	}
	

</script>
</html>