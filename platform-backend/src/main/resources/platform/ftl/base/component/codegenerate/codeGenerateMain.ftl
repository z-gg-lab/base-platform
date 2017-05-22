<!DOCTYPE html>
<html>
	<head>
	    <title>编码组件</title>
	    <#include "../../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
	</head>
	<body style="overflow-y: hidden"  scroll="no">
	         <table id="codeTable" class="easyui-datagrid" title="编码信息列表" pageSize="20" style="padding:30px;" fit=true
	            data-options="singleSelect:false,collapsible:false,
				url:'${webRoot}/component/codegenerate/querycode',
				pagination:true,
				toolbar:'#tb',
				idField:'id',
				method:'post',
				remoteSort:false,
				multiSort:true,
				rownumbers:true, 
	            ">
		            
			<div id="tb" style="padding:4px;height:30px;">
				<a href="#" class="easyui-linkbutton" iconcls="icon-add" plain="true" onclick="addCode()">添加编码</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-remove" plain="true" onclick="deleteCode()">删除</a>
			</div>
			
			<thead>
				<tr>
					<th data-options="field:'id',width:80,align:'left',checkbox:true">ID</th>
					<th data-options="field:'businessKey',width:200,align:'center',formatter:formatecodeEdit">业务类型</th>
					<th data-options="field:'businessName',width:200,align:'center'">业务名称</th>
					<th data-options="field:'type',width:140,align:'left'">生成策略</th>
					<th data-options="field:'lastGenerateTime',width:140,align:'center',formatter:formatTimeYYYYMMDDHHMMSS">最后编码生成时间</th>
					<th data-options="field:'rule',width:140,align:'center'">生成规则</th>
					<th data-options="field:'length',width:140,align:'center'">序号长度</th>
					<th data-options="field:'no',width:140,align:'center'">当前序号</th>
				</tr>
			</thead>
		</table>
		<script>
		
		$(function(){ $('#codeTable').datagrid({ onClickCell:function(rowIndex, field, value){ if(field == "businessKey"){ $(this).datagrid('clearSelections'); $(this).datagrid('onUnselectAll'); } } }); });
		
		
		
		// 修改
		function formatecodeEdit(value,row,index){
				var s = '<a href="#" onclick="editCode(\''+row.id+'\')">'+row.businessKey+'</a>';
				return s;
		}
		
		// 修改编码
		
		function editCode(id){
			
			var url = "${webRoot}/component/codegenerate/edit/"+id;
			parent.$("#dialog").dialog({
				title:"修改编码",
				content:createFrame(url),
				width:380,    
				height:430,
				top:($(window).height()-310)*0.5,
				left:($(window).width()-450)*0.5
			});
			
			parent.$("#dialog").dialog("open");
		}
		
		//删除编码
		function deleteCode() {
			var ids = "";
			var row = $('#codeTable').datagrid('getChecked');
			if(!row||row.length==0) {
				$.messager.alert('提示','请选择需要的编码!','info');
				return;
			}
			for(var i = 0 ; i<row.length ; i ++) {
				ids += row[i].id + ",";
			}
			$.messager.confirm('提示','确认删除选择的编码？',function(r){
				if(r) {
					$.ajax({
	    				type:"get",
	    				url:"${webRoot}/component/codegenerate/delete",
	    				data:{
	    					"ids":ids
	    				},
	    				async:false,
	    				success:function(data) {
	    					if(data=="success"){
	    						parent.reloadData("codeTable");
	    					}else{
	    						$.messager.alert('提示','需要删除的编码已经被使用过，无法删除!','info');
	    					}
	    				}
	    			});
				}
			});
		}

	    // 添加编码
		function addCode() {
			var url = "${webRoot}/component/codegenerate/add";
			parent.$("#dialog").dialog({
				title:"添加编码",
				content:createFrame(url),
				width:380,    
				height:430,
				top:($(window).height()-310)*0.5,
				left:($(window).width()-450)*0.5
			});
			parent.$("#dialog").dialog("open"); 
		}

		function createFrame(url) {
			var s = '<iframe id="userSelect4Role" name="userSelect4Role" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
			return s;
		}


		function formatter(){
			var key = 'DEPARTMENT_TYPE';
		
			 $.ajax({
		        type: "post",
		        url: window.webRoot + "/config/format?configKey="+key,
		        async: false,
		        dataType: "json",
		        success: function (data) {
		        
		        }
		    });
			
		}




		</script>
	</body>
</html>