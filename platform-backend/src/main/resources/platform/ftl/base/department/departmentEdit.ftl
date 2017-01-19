<!DOCTYPE html>
<html>
	<head>
	    <title>机构管理</title>
	    <#include "../../include.ftl">
	</head>
	<body style="overflow-y: hidden" scroll="yes">
	    <form id="departmentForm" action="${webRoot}/department/save" method="post">
	    	<table class="table-list">
	    		<tr>
	    			<!--<td class="t-title" width="30%">父级ID：</td>-->
	    			
	    				<input type="hidden" maxlength="100" name="departmentId" value="${(departmentVO.departmentId)!''}"/>
	    				<input type="hidden" maxlength="100" name="parentId" value="${(departmentVO.parentId)!''}"/>
	    			
	    		</tr>
	    		
	    		<tr>
	    			<td class="t-title" width="30%">父级名称：</td>
	    			<td>
	    				${(departmentVO.parentName)!''}
	    			</td>
	    		</tr>
	    		
	    		<tr>
	    			<td class="t-title" width="30%">机构名称：<font style="color:red">*</font></font></td>
	    			<td><input class="easyui-textbox" data-options="prompt:'请输入机构名称',required:false,validType:['specialCharacters','lengthCharacter[50,100]']" id="_departmentName" name="departmentName" value="${(departmentVO.departmentName)!''}"/></td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">机构编码：</td>
	    			<td><input class="easyui-textbox" data-options="prompt:'请输入机构编码',required:false,validType:['noChinese','nospace','specialCharacters','length[0,50]']"  id="departmentCode" name="departmentCode" value="${(departmentVO.departmentCode)!''}"/></td>
	    		</tr>
	    	</table>
	    		
	    	<table class="table-list" id="_table">
	    		
	    		<tr>
	    			<td class="t-title" width="30%">机构全名：</td>
	    			<td>
	    				${(departmentVO.fullName)!''}
	    				<input type="hidden" name="fullName" value="${(departmentVO.fullName)!''}"/>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">机构CODE：</td>
	    			<td>${(departmentVO.code)!''}
	    		</tr>
	    		
	    		<tr>
	    			<td class="t-title" width="30%">机构全CODE：</td>
	    			<td>${(departmentVO.fullCode)!''}
	    		</tr>
	    		
	    		<tr>
	    			<td class="t-title" width="30%">层级：</td>
	    			<td>${(departmentVO.departmentLevel)!''}
	    		</tr>
	    		
	    	</table>
	    	<table class="table-list">
	    		<tr>
	    			<td class="t-title" width="30%">机构类型：<font style="color:red">*</font></td>
	    			<td>
					<!--高度自适应 data-options="panelHeight:'auto'"-->
					<input id="_type" name="type" class="easyui-combobox" required="true" value="${(departmentVO.type)!''}"/>
					
	    			</td>
	    		</tr>
	    		
	    		<tr>
	    			<td class="t-title" width="30%">备注：</td>
	    			<td><input name="remark" class="easyui-textbox" data-options="multiline:true,validType:['specialCharacters','lengthCharacter[125,250]']" style="height:50px;" value="${(departmentVO.remark)!''}"/></td>
	    		</tr>
	    		
	    		<tr>
	    			<td colspan="2">
	    				<span style="size:7px;">注：标<font style="color:red">*</font>的为必填项！</span>
	    			</td>
	    		</tr>
	    		
	    		<tr>
	    			<td colspan="2" align="center">
	    				<a href="#" class="easyui-linkbutton" iconcls="icon-save" onclick="saveDepartment()">保存</a>
	    				<a href="#" class="easyui-linkbutton" iconcls="icon-cancel" onclick="closeWindow()">取消</a>
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	<script>
			
    	$(function(){
			var type = ${departmentVO.type};
			if(type!=0){
				$.ajax({ 
					url: '${webRoot}/department/getCombobox',
					dataType: 'json', 
					success: function(data){   
						// 修改ajax返回的值
						data.unshift({'configValue':'','name':'-请选择-'});   //unshift方法添加到第一行，push方法添加到末尾
						$('#_type').combobox({ 
							data: data,   
							valueField:'configValue',    
							textField:'name',
							editable:false,
							panelHeight:100,
						});   
					}
				});
				parent.$('#dialog').css("height",getHeight('departmentForm')+50);
			}else{
				$.ajax({ 
					url: '${webRoot}/department/getCombobox',
					dataType: 'json', 
					success: function(data){   
						// 修改ajax返回的值
						data.unshift({'configValue':'','name':'-请选择-'});   //unshift方法添加到第一行，push方法添加到末尾
						$('#_type').combobox({ 
							data: data,   
							valueField:'configValue',    
							textField:'name',
							editable:false,
							panelHeight:100,
							onLoadSuccess: function () { //加载完成后,设置选中第一项  
                                var val = $(this).combobox('getData');  
                                for (var item in val[0]) {  
                                    if (item == 'configValue') {  
                                        $(this).combobox('select', val[0][item]);  
                                    }  
                                }  
                            }  
						});   
					}
					
				});
				$('#_table').hide();
				parent.$('#dialog').css("height",getHeight('departmentForm')+50);
			}
		});
			
			function getHeight(id){
				var height = 0;
				var div = document.getElementById(id).getBoundingClientRect();
				if(div.height){
					height = div.height;
				}else{
					height = div.bottom - div.top;
				}
				return height;
			}
			
			
			
			function saveDepartment() {
				$('#_departmentName').textbox({required:true});
			
	    	       $("#departmentForm").form({
	    			url:"${webRoot}/department/save",
	    			onSubmit: function(){
	    				
	    				if($('#_type').combobox('getValue')==""){
	    					$('#_type').combobox('clear');
	    					return $(this).form('validate');
	    				}
	    				return $(this).form('validate');
		              },
	    			 success:function(data) {
	    			 	if($.parseJSON(data).msg!="success"){
	    			 		$.messager.alert('提示', $.parseJSON(data).msg,'info');
	    			 	}else{
							var json = JSON.parse(data).departmentVO;
		    			 	var parentId = json.parentId;
		    				var pDepartment = parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#departmentEdit').treegrid('find',json.parentId);
		    				var departmentId = json.departmentId;
							var nodeArray = new Array();
							var row = parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#departmentEdit').treegrid("getSelected");
							nodeArray.push(json);
	
		    				if(json.editType == 0) {
		    					//新增
		    					if(pDepartment){
			    					parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#departmentEdit').treegrid('append',{parent:row.departmentId,data:json});
									parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#departmentEdit').treegrid('reload',pDepartment.departmentId); 
		    					}else{
		    						parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#departmentEdit').treegrid('reload');
		    					}
		    				} else {
		    					parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#departmentEdit').treegrid('updateRow',{index:departmentId,row:json});
		    				}
		    			    closeWindow();
		    			}
	    			}
	    		});
	    		$("#departmentForm").submit();
	    	}
						
	    	//取消
	    	function closeWindow() {
	    		parent.$("#dialog").dialog("close");
	    	}
	    </script>
	</body>
</html>