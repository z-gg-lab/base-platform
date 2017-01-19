<!DOCTYPE html>
<html>
	<head>
	    <title>控件管理</title>
	    <#include "../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/component/ConfigUtils.js"></script>
		<script type="text/javascript" src="${webRoot}/statics/base/js/uploader/webuploader.min.js"></script>
		<script type="text/javascript" src="${webRoot}/statics/base/js/uploader/fileUpload.js"></script>		
	</head>
	<body style="overflow-y: hidden"  scroll="no">
	    <table id="widgetTable" class="easyui-datagrid" title="控件管理" pageSize="20"  fit=true
	            data-options="singleSelect:true,collapsible:false,
				url:'${webRoot}/widgetmanager/list',
				pagination:true,
				toolbar:'#toolbar',
				idField:'widgetId',
				method:'post',
				remoteSort:false,
				multiSort:true,
				rownumbers:true 
	            ">
	        <thead>
	        <tr>  
	        	<th data-options="field:'widgetId',hidden:true"></th>
	        	<th data-options="field:'fileId',hidden:true"></th>
	    	 	<th data-options="field:'widgetName',width:100,align:'center'">控件名称</th>
	    	 	<th data-options="field:'widgetPath',width:260,align:'center',formatter:formatePathEdit">控件下载路径</th>
	    	 	<th data-options="field:'operator',width:100,align:'center'">创建人</th>
	    	 	<th data-options="field:'widgetVersion',width:100,align:'center'">控件版本号</th>
	    	 	<th data-options="field:'widgetType',width:100,align:'center',formatter:formateWidgetType" >类型</th>
	    	 	<th data-options="field:'createTime',width:100,align:'center',formatter:formatTimeYYYYMMDDHHMMSS" >上传时间</th>
            </tr>
	        </thead>
	    </table>
	    <div id="toolbar" style="padding:5px;height:auto">
	    	<div style="margin-bottom:5px">
	    		控件名称：<input class="easyui-textbox" id = "widgetName" style="width:160px;"/>
	    		<a onclick="statisticsSearch()" class="easyui-linkbutton" iconCls="icon-search" plain="true">查询</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="uploadWidget();">上传控件压缩包</a>
			    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delWidget();">删除</a>
			</div>
	    </div>
	    <script type="text/javascript">
	       function statisticsSearch(){
	       var widgetName =$("#widgetName").textbox("getValue");
	       		$('#widgetTable').datagrid({
					queryParams: {
						"widgetName": widgetName,
				  }
				});
	    
	       }
	    	//
	    	function formateWidgetType(val,row){
	    	   return format('WIDGET_TYPE',val);
	    	}
	       //上传控件
	       function uploadWidget(){
	       		var url = "${webRoot}/widgetmanager/add";
				parent.$("#dialog").dialog({
					title:"上传控件zip压缩包",
					content:createFrame(url),
					width:360,    
    				height:320,
    				top:($(window).height()-310)*0.5,
    				left:($(window).width()-450)*0.5
				});
				parent.$("#dialog").dialog("open"); 
	       }
			//创建视图
			function createFrame(url,widgetId) {
             var s = '<iframe name="widgetEditFrame" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
				return s;
			}
			
			//
			function formatePathEdit(value,row,index){
				var s = '<a href="#"  onclick="downLoadWidget(\''+row.fileId+'\')">'+row.widgetPath+'</a>';
				return s;
			}
			
			function downLoadWidget(fileId){
			 var link="${webRoot}/file/filedownload?downloadfiles="+fileId;  
            window.open(link);  
            return false;  
			}
			
			//删除角色（注意id的名字和数据库中的肯能不一样）
			function delWidget() {
			    var row = $('#widgetTable').datagrid('getSelected');
				if(!row||row.length==0) {
					$.messager.alert('提示','请选择需要删除的!','info');
					return;
				}
				$.messager.confirm('提示','确认删除选择的控件压缩包？',function(r){
					if(r) {
						$.ajax({
			    			type:"post",
			    			url:"${webRoot}/widgetmanager/delete",
			    			data:{
			    				"widgetId":row.widgetId
			    			},
			    			async:false,
			    			error:function(request) {
			    			},
			    			success:function(data) {
			    				if(data == "success"){
			    					$('#widgetTable').datagrid('load');
			    				}else{
			    					$.messager.alert('提示',data,'info');
			    				}
			    				
			    			}
			    		});
					}
				});
			}
	    </script>
	</body>
</html>
