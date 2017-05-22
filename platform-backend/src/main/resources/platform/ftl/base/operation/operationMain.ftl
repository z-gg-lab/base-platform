<!DOCTYPE html>
<html>
	<head>
	    <title>功能管理</title>
	    <#include "../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
	</head>
	<body class="easyui-layout"  style="overflow-y: hidden"  scroll="no">
	    <div id="panel" region="center" style="background: #eee; overflow-y:hidden">
	    <table id="operationTable" class="easyui-datagrid" style="padding:30px;" fit=true
	            toolbar="#toolbar" idField="roleId"
	           	url="${webRoot}/operation/list/${menuId!''}"
	            pagination="true"
	            rownumbers="true" singleSelect="false">
	        <thead>
	            <tr>
	            	<th data-options="field:'operationId',checkbox:true"></th>
	                <th data-options="field:'operationName',width:140,align:'left',formatter:formateOperation">功能名称</th>
	                <th data-options="field:'operationCode',width:60,align:'left'">功能编码</th>
	                <th data-options="field:'remark',width:150,align:'left'">功能说明</th>
	                <th data-options="field:'updateTime',width:150,align:'center',formatter:formatTimeYYYYMMDD">修改时间</th>
	            </tr>
	        </thead>
	    </table>
	    </div>
	    <div id="toolbar" style="padding:5px;height:auto">
	    	<div style="margin-bottom:5px">
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addOperation();">添加</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delOperation()">删除</a>
	        </div>
	    </div>
    	<div id="editOperation" class="easyui-dialog" closed="true" style="width:340px;height:310px;"></div>
	    <script type="text/javascript">
	    
	        //添加功能
			function addOperation() {
				var url = "${webRoot}/operation/add/${menuId!''}";
				$("#editOperation").dialog({
					title:"添加功能",
					content:createFrame(url),
					modal:true
				});
				$("#editOperation").dialog("open");
			}
			
			//打开一个弹窗
			function createFrame(url) {
				var s = '<iframe name="menuEditFrame" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
				return s;
			}
			
			//格式化功能
			function formateOperation(value,row,index) {
				var a = "<a href='#' onclick='editOperation(\""+row.operationId+"\")'>"+row.operationName+"</a>";
				return  a;
			}
			
			//修改功能
			function editOperation(operationId) {
				var row = $('#operationTable').datagrid('getSelected');
				if(!row&&!operationId) {
					$.messager.alert('提示','请选择需要修改的功能!','info');
					return;
				}
				if(!operationId) {
					operationId = row.operationId;
				}
				var url = "${webRoot}/operation/edit/"+operationId;
				$("#editOperation").dialog({
					title:"修改功能 ",
					content:createFrame(url),
					modal:true
				});
				$("#editOperation").dialog("open");
			}
			
			//删除功能
			function delOperation() {
				var operationIds = "";
				var row = $('#operationTable').datagrid('getChecked');
				if(!row||row.length==0) {
					$.messager.alert('提示','请选择需要删除的功能!','info');
					return;
				}
				for(var i = 0 ; i<row.length ; i ++) {
					operationIds += row[i].operationId + ",";
				}
				$.messager.confirm('提示','确认删除选择的功能？',function(r){
					if(r) {
						$.ajax({
			    			type:"post",
			    			url:"${webRoot}/operation/delete",
			    			data:{
			    				"operationIds":operationIds
			    			},
			    			async:false,
			    			error:function(request) {
			    			},
			    			success:function(data) {
			    				$('#operationTable').datagrid('load');
                                $("#operationTable").datagrid("uncheckAll");
			    			}
			    		});
					}
				});
			}
	    </script>
	</body>
</html>