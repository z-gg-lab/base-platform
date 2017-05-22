<!DOCTYPE html>
<html>
	<head>
	    <title>导入</title>
	    <#include "../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/jquery-ui-jqLoding.js"></script>
	</head>
	<body style="overflow-y: hidden" scroll="yes">
				<form id="excelForm" method="post" enctype="multipart/form-data">
					<table class="table-list">
						<tr>
						<td>
							已选择文件：
						</td>
						<td>
						<input class="easyui-filebox" name="file" id="file" style="width:200px;"/>
						</td>
						</tr>
						<tr>
						<td>
							请选择上传文件：
						</td>
						<td>
						<a href="#" class="easyui-linkbutton" iconcls="icon-ok" plain="true" onclick="upload()">Excel上传</a>
						</td>
						</tr>
						<tr>
						<td>
							请下载Excel样例
						</td>
						<td>
						<a class="easyui-linkbutton" id="downDevice" plain="true" onclick="uploadDevice()">Excel样例下载</a>
						</td>
						</tr>
					</table>
	  			</form>	
	    <script type="text/javascript">
	    	//上传
			function　upload(){
				$("#excelForm").form({					
					onSubmit:function(){
						var name = $("#file").filebox('getText');
						$("#fileName").val(name);
						if(endWith(name,".xls")){
							$(this).jqLoading();
							return true;
						}else{
							$.messager.alert('提示', '请选择xls格式的文件', 'info');
							return false;
						};					
					},
					url:"${webRoot}/excelimp/userExcel",
					success:function(data){
						$(this).jqLoading("destroy");
						if(data != '' && data != null){			
							var url = "${webRoot}/user/userImpErr?data="+data;
							parent.$("#departmentSelect").dialog({
								title:"错误提示",
								content:createFrameImpErr(url),
								width:400,
								height:300,
								top:$(window).height()*0.5,
			    				left:$(window).width()*0.5,
			    				onClose:function(){
			    				}				
							});
							parent.$("#departmentSelect").dialog("open");						
						}
						else{
							$.messager.alert('提示',"导入成功",'info',function(){
								parent.$("#dialog").dialog("close");
							});
							$('#userTable').datagrid('load');
						}				
    					$('#userTable').datagrid('reload');
    					$('#userTable').datagrid('unselectAll');
					}
				});
				$("#excelForm").submit();
				}
		
			function createFrameImpErr(url) {
				var s = '<iframe name="createFrameImpErr" id="createFrameImpErr" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
				return s;
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
			//状态格式化
			function formateStatus(value,row,index){
				if(value==1){
			        return "待导入";
			    }else if(value==2){
			        return "正在导入";
			    }else if(value==3){
			    	return "导入成功";
			    }else{
			    	return "导入失败";
			    }
			} 
			
			//格式化错误原因
			function formateError(value,row,index) {
				if(row.status == 4){
					return '<a href="#" onclick="showDetails(\''+row.taskId+'\')">查看详情</a>';
				}
			} 
			
			function showDetails(taskId){
				var row = $('#taskImpTable').datagrid('getSelected');
				if(!row&&!taskId) {
					$.messager.alert('提示','请选择需要查看的错误信息!','info');
					return;
				}
				if(!taskId) {
					taskId = row.taskId;
				}
				var url = "${webRoot}/taskimp/errorDetails/"+taskId;
				parent.$("#edittwo").dialog({
					title:"失败详情",
					content:createFrame(url),
					width:450,
					height:350,
					top:100,
    				left:400				
				});
				parent.$("#edittwo").dialog("open");
			} 
			
			function uploadDevice(){
				var templateName = "用户信息导入";
				var excelKey = "USER123";
				var url = "${webRoot}/component/excelImportTemplate/download?templateName="+templateName+"&id="+excelKey;
				$("#downDevice").attr("href",url);	
			}
			
			//打开一个弹窗
			function createFrame(url) {
				var s = '<iframe name="errorDetailsFrame" scrolling="yes" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
				return s;
			}    	
	    </script>
	</body>
</html>