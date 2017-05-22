<!DOCTYPE html>
<html>
	<head>
	 	<title>编辑</title>
	    <#include "../../include.ftl">
		<script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
		<link rel="stylesheet" type="text/css" href="${webRoot}/statics/base/css/uploader/webuploader.css">
		<script type="text/javascript" src="${webRoot}/statics/base/js/uploader/webuploader.min.js"></script>
		<script type="text/javascript" src="${webRoot}/statics/base/js/uploader/fileUpload.js"></script>		
	</head>
	<body style="overflow-y: hidden" scroll="no">
	
		<div id="attachment" style="margin-left:10px"></div>
	    <form id="widgetEditForm" method="post" action="${webRoot}/widgetmanager/upload">
	    	<table class="table-list">
	    		<tr>
	    			<td class="t-title" width="30%">控件名称：<font style="color:red">*</font></td>

	    			<td>
	    			<input name="widgetName" type="text" disabled="true " class="easyui-textbox" id="widgetName" style="width:180px;" />
	    			</td>
    			    			
	    	     </tr>
	    	
	    		<tr>
	    			<td class="t-title" width="30%">控件版本号：<font style="color:red">*</font></td>

	    			<td>
	    				<input id="fileId" type="hidden"  name="fileId"/>
	    				<input id="widgetVersion" class="easyui-textbox"   name="widgetVersion" style="width:180px;" value=""/>
	    			</td>
    			    			
	    	     </tr>
	    	     <tr>
	    			<td class="t-title" width="30%">控件类型：<font style="color:red">*</font></td>

	    			<td>
	    				<input id="widgetType" class="easyui-combobox" name="widgetType" style="width:180px;" data-options="required:true,novalidate:true,editable:false"/>
	    			</td>
    			    			
	    	     </tr>
	    		 <tr>
			    	<td colspan="2" align="center">
			    			<font style="color:red"><span id="descript">*完善信息后才可以保存<span></font>
			    	</td>
			    </tr>
	    		<tr>
	    			<td colspan="2" align="center">
	    				<a href="#" class="easyui-linkbutton" id="save"  iconcls="icon-save" onclick="saveWidget()">保存</a>
	    				<a href="#" class="easyui-linkbutton" iconcls="icon-cancel" onclick="closeWindow()">取消</a>
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	    <script>
	    //0表示未上传控件 1表示已上传
	    var isUpload = 0;
	    
	    $(function(){
			$.ajax({ 
				url: '${webRoot}/widgetmanager/widgetType',
				dataType: 'json', 
				success: function(data){   
					// 修改ajax返回的值
					data.unshift({'configValue':'','name':'-请选择-'});   //unshift方法添加到第一行，push方法添加到末尾
					$('#widgetType').combobox({ 
						data: data,   
						valueField:'configValue',    
						textField:'name',
						editable:false,
						panelHeight:100,
					});   
				}
			});
		});
	    	//保存
	    	function saveWidget() { 
	    	     if(isUpload == 0){
	    	     	parent.$.messager.alert('提示', '请先上传控件，再保存控件信息', 'info');
	    	     	return;
	    	     }
	    		  $("#widgetEditForm").form({
				  onSubmit:function(){
				 
				  if($('#widgetVersion').textbox('getText') == ''){
				  	 $("#descript").text("请输入控件控件版本号");
				  	 return false;
				  }
				   if($('#widgetType').combobox('getText') == '-请选择-'){
	    			    $("#descript").text("请选择控件控件的类型");
	    			      return false;
	    			  }
				   return $(this).form('validate');
				},
				success:function(data){
					if(data=="success"){
						isUpload = 0;
					   parent.reloadData("widgetTable");//调用父页面（首页）的方法
						closeWindow();
					}else{
						parent.$.messager.alert('提示', data, 'info');
					}
				}
			});
			$("#widgetEditForm").submit();
	    		 
		    }
		    
			//文件上传
	    	$("#attachment").powerWebUpload({
	    		auto:false,
    			fileNumLimit:1,
    			fileSingleSizeLimit: 2*1024*1024,
    			accept:{
    				title:'zip',
                	extensions:'zip',
                	mimeTypes:'application/x-zip-compressed'
    			},   			
    			innerOptions:{
	    			onComplete:function(event){
	    				var attachmentId = $("#attachment").GetFilesAddress();
	    				var fileName = $(".webuploadinfo").html();			
	    				//var fileId = attachmentId[0];
	    				$(".webuploadDelbtn").click(); //文件上传成功后自动清除队列中的文件
	    				$('#fileId').val(attachmentId);
	    				isUpload = 1;
	    				$('#widgetName').textbox('setValue',fileName);
	    			}
    			}
	    	});
				
				
				
		    
		    
		    
		  
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
	    	//取消
	    	function closeWindow() {
	    		parent.$("#dialog").dialog("close");
	    	}
	    </script>
	</body>
</html>