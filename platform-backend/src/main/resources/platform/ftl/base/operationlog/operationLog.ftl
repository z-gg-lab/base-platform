<!DOCTYPE html>
<html>
	<head>
	    <title>操作日志配置管理</title>
	    <#include "../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/jquery.edatagrid.js"></script>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
	</head>
	<body style="overflow-y: hidden"  scroll="yes">
		<div id="tb" class="easyui-panel" style="padding:5px;border-bottom:0px;">
			<span>操作类型:</span><input type="text" class="easyui-textbox" id="logOperParam" size=10 />
			<span>操作用户:</span><input type="text" class="easyui-textbox" id="logUserParam" size=10 />
			<span>发生时间:</span><input class="easyui-datebox" id="logTimeParam1" style="width:150px;"size=10 />
			&nbsp;-&nbsp;
			<input class="easyui-datebox" id="logTimeParam2" style="width:150px;"size=10 />
			<span>IP地址:</span><input type="text" class="easyui-textbox" id="logAddress" size=10 />
			<span>操作结果:</span>
						<select id="logStatus" class = "easyui-combobox">
	    					<option value="0" selected="selected">成功</option>
	    					<option value="1">失败</option>
	    				</select> 
          	<a onclick="queryCondition()" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">查询</a> 
        </div>
		<table id="operationLogConfig" class="easyui-datagrid" pageSize="20" style="padding:30px;" fit=true
	            data-options="singleSelect:false,collapsible:false,
				url:'${webRoot}/operationlog/queryLogByCondition',
				pagination:true,
				idField:'logId',
				toolbar:'#tb',
				remoteSort:false,
				multiSort:true,
				rownumbers:true,
				selectOnCheck: true,
				checkOnSelect:true 
	            ">
	        <thead>
	            <tr>
	                <th data-options="field:'logUser',width:100,align:'left'">操作用户</th>
	                <th data-options="field:'logAddress',width:100,align:'left'">IP地址</th>
	                <th data-options="field:'logTime',width:150,align:'left',formatter:formatTimeYYYYMMDD">发生时间</th>
	                <th data-options="field:'logOper',width:200,align:'left'">操作类型</th>
	                <th data-options="field:'logContent',width:250,align:'left'">操作内容信息</th>
	                <th data-options="field:'logStatus',width:80,align:'center',formatter:formateStatus"">操作结果</th>
	            </tr>
	        </thead>
	    </table>
	    <script type="text/javascript">
			
			function formateStatus(value,row,index) {
				var state = "";
				if(value == 0) {
					state = "成功";
				} else {
					state = "失败";
				}
				return state;
			}
			
			//设置查询条件
			function queryCondition() {
				$('#operationLogConfig').datagrid({
					queryParams: {
						"logOper": $("#logOperParam").val(),
						"logUser": $("#logUserParam").val(),
						"logTime1": $('#logTimeParam1').datebox('getValue'),
						"logTime2": $('#logTimeParam2').datebox('getValue'),
						"logAddress": $("#logAddress").val(),
						"logStatus": $("#logStatus").val()
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
