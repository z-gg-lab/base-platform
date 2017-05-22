<!DOCTYPE html>
<html>
	<head>
	    <title>操作日志配置管理</title>
	    <#include "../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/jquery.edatagrid.js"></script>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
	</head>
	<body style="overflow-y: hidden"  scroll="no">
		<div class="easyui-panel" style="padding:5px;border-bottom:0px;">
			<span>操作类型:</span><input type="text" id="logcfgOperParam" value="" size=10 />  
			<span>模板标识:</span><input type="text" id="logcfgMarkParam" value="" size=10 />  
			<span>模板状态:</span>
						<select id="logcfgStatusParam">
	    					<option value="0" selected="selected">启用</option>
	    					<option value="1">停用</option>
	    				</select> 
          	<a onclick="queryCondition()" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">查询</a> 
        </div>
	    <table id="operationLogConfig" class="easyui-datagrid" style="padding:30px;"
				toolbar="#toolbar" idField="logcfgId"
	            url="${webRoot}/operationlogconfig/queryLogManageByCondition"
	            pagination="true"
	            rownumbers="true" singleSelect="false"
	            selectOnCheck: true,
				checkOnSelect: true>
	        <thead>
	            <tr>
	                <th data-options="field:'logcfgId',width:200,align:'left', checkbox:true"></th>
	                <th data-options="field:'logcfgOper',width:200,align:'left'">操作类型</th>
	                <th data-options="field:'logcfgMark',width:450,align:'left'">模板标识</th>
	                <th data-options="field:'logcfgStatus',width:80,align:'center',formatter:formateStatus"">模板状态</th>
	                <th data-options="field:'updateTime',width:150,align:'center',formatter:formatTimeYYYYMMDD">修改时间</th>
	                <th data-options="field:'_operate',width:80,align:'center',formatter:formateName">操作</th>
	            </tr>
	        </thead>
	    </table>
	    <div id="toolbar" style="padding:5px;height:auto">
	    	<div style="margin-bottom:5px">
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add();">添加</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="del()">删除</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="" plain="true" onclick="start()">启用</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="" plain="true" onclick="stop()">停用</a>
	        </div>
	    </div>
	    <div id="editOperationConfig" class="easyui-dialog" closed="true" style="width:540px;height:470px;"></div>
	    <script type="text/javascript">
	    
	    	//格式化角色名
			function formateName(value,row,index) {
				return '<a href="#" onclick="edit(\''+row.logcfgId+'\')">修改</a>';
			}
			
			function formateStatus(value,row,index) {
				var state = "";
				if(value == 0) {
					state = "启用";
				} else {
					state = "停用";
				}
				return state;
			}
			
			//设置查询条件
			function queryCondition() {
				$('#operationLogConfig').datagrid({
					queryParams: {
						"logcfgOper": $("#logcfgOperParam").val(),
						"logcfgMark": $("#logcfgMarkParam").val(),
						"logcfgStatus": $("#logcfgStatusParam").val()
						
				  }
				});
			}
			
			function add() {
				var url = "${webRoot}/operationlogconfig/edit/add";
				$("#editOperationConfig").dialog({
					title:'新增',
					content:createFrame(url),
					modal:true
				});
				$("#editOperationConfig").dialog("open");
			}
	    
	    
	        //添加角色
			function edit(id) {
                var url = "${webRoot}/operationlogconfig/edit/" + id;
				$("#editOperationConfig").dialog({
					title:'修改',
					content:createFrame(url),
					modal:true
				});
				$("#editOperationConfig").dialog("open");
			}
			
			function del() {
				var row = $('#operationLogConfig').datagrid('getSelections');
				if(!row) {
					$.messager.alert('提示','请选择需要删除的配置!','info');
					return;
				}
				var id = "";
				for(var i = 0; i < row.length; i++) {
					id += row[i].logcfgId + ",";
				}
				$.messager.confirm('提示','确认删除配置？',function(r){
					if(r) {
						$.ajax({
			    			type:"post",
			    			url:"${webRoot}/operationlogconfig/delete/"+id,
			    			async:false,
			    			error:function(request) {
			    			},
			    			success:function(count) {
								$('#operationLogConfig').datagrid('reload');	
			    			}
			    		});
					}
				});
			}
			
			function start() {
				var row = $('#operationLogConfig').datagrid('getSelections');
				if(!row) {
					$.messager.alert('提示','请选择需要启用的配置!','info');
					return;
				}
				var id = "";
				for(var i = 0; i < row.length; i++) {
					id += row[i].logcfgId + ",";
				}
				$.messager.confirm('提示','确认启用配置？',function(r){
					if(r) {
						$.ajax({
			    			type:"post",
			    			url:"${webRoot}/operationlogconfig/enableLogManage/"+id,
			    			async:false,
			    			error:function(request) {
			    			},
			    			success:function(count) {
								$('#operationLogConfig').datagrid('reload');	
			    			}
			    		});
					}
				}); 
			}
			
			
			function stop() {
				var row = $('#operationLogConfig').datagrid('getSelections');
				if(!row) {
					$.messager.alert('提示','请选择需要停用的配置!','info');
					return;
				}
				var id = "";
				for(var i = 0; i < row.length; i++) {
					id += row[i].logcfgId + ",";
				}
				$.messager.confirm('提示','确认停用配置？',function(r){
					if(r) {
						$.ajax({
			    			type:"post",
			    			url:"${webRoot}/operationlogconfig/disableLogManage/"+id,
			    			async:false,
			    			error:function(request) {
			    			},
			    			success:function(count) {
								$('#operationLogConfig').datagrid('reload');	
			    			}
			    		});
					}
				});
			}
			
			//打开一个弹窗
			function createFrame(url) {
				var s = '<iframe name="roleEditFrame" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
				return s;
			}
	    </script>
	</body>
</html>