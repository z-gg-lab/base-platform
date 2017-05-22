<!DOCTYPE html>
<html>
	<head>
	    <title>角色管理</title>
	    <#include "../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/component/ComponentOfDepartment.js"></script>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/component/ComponentOfMenu.js"></script>
	</head>
	<body style="overflow-y: hidden"  scroll="no">
	    <table id="roleTable" class="easyui-datagrid" title="角色信息列表" pageSize="20" style="padding:30px;" fit=true
	            data-options="singleSelect:false,collapsible:false,
				url:'${webRoot}/role/list',
				pagination:true,
				toolbar:'#toolbar',
				idField:'roleId',
				method:'post',
				remoteSort:false,
				multiSort:true,
				rownumbers:true, 
	            ">
	        <thead>
	            <tr>
	            	<th data-options="field:'roleId',checkbox:true"></th>
	                <th data-options="field:'roleName',width:100,align:'left',sortable:true,formatter:formateRoleEdit">角色名</th>
	                <th data-options="field:'remark',width:200,align:'left'">角色描述</th>
	                <th data-options="field:'updateTime',width:200,align:'center',formatter:formatTimeYYYYMMDDHHMMSS">修改时间</th>
	                <th data-options="field:'_operate',width:300,align:'center',formatter:formateRoleName">操作</th>
	            </tr>
	        </thead>
	    </table>
	    <div id="toolbar" style="padding:5px;height:auto">
	    	<div style="margin-bottom:5px">
				角色名:<input type="text" id="roleName" name="roleName" class="easyui-textbox" maxlength="100" onkeyup="searchenter1(event);"/>  
          		<a onclick="queryCondition(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">查询</a> 
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addRole();">添加</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delRole()">删除</a>
			</div>
	    </div>
		<div id="departmentSelect4RoleAdd" class="easyui-dialog" closed="true"> </div>
	    <script type="text/javascript">
	    $(function(){ $('#roleTable').datagrid({ onClickCell:function(rowIndex, field, value){ if(field == "roleName"){ $(this).datagrid('clearSelections'); $(this).datagrid('onUnselectAll'); } } }); });
	    
	    	//初始化参数
	    	var userIds="";
		    //回车进入模糊查询
		    
	    	function searchenter1(event) {
        		event = event || window.event;
        		if (event.keyCode == 13) {          
					var roleName = $("#roleName").val();
					$('#roleTable').datagrid({
						queryParams: {
							"roleName": roleName
						}
					});
        		}
    		}
		   
		    var noproblem = window.webRoot;
	        //添加角色
			function addRole() {
				var url = "${webRoot}/role/add";
				parent.$("#dialog").dialog({
					title:"添加角色",
					content:createFrame(url),
					width:450,    
    				height:310,
    				top:($(window).height()-310)*0.5,
    				left:($(window).width()-450)*0.5
				});
				parent.$("#dialog").dialog("open"); 
			}
			
			//修改角色
			function editRole(roleId) {	
				var row = $('#roleTable').datagrid('getChecked');
				if(!row&&!roleId) {
					$.messager.alert('提示','请选择需要修改的角色!','info');
					return;
				}
				if(!roleId) {
					roleId = row.roleId;
				}
				var url = "${webRoot}/role/edit/"+roleId;
				parent.$("#dialog").dialog({
					title:"修改角色",
					content:createFrame(url),
					width:450,    
    				height:310,
    				top:($(window).height()-310)*0.5,
    				left:($(window).width()-450)*0.5
				});
				parent.$("#dialog").dialog("open");
			}

			var json = {"singleSelect":"false"};
			
			//格式化角色名
			function formateRoleName(value,row,index) {
				return '<a href="#" id = "_a" onclick="openMenuSelect(\''+row.roleId+'\',allocateRight,json)">分配菜单功能</a>&nbsp&nbsp<a href="#"  onclick="userSelectOfRole(\''+row.roleId+'\',getRoleId)">分配人员</a>';
			}
			
			//分配人员
			function createFrame(url,roleId) {
				var s = '<iframe id="userSelect4Role" name="userSelect4Role" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
				return s;
			}
			
			function userSelectOfRole(roleId){
				var urlForUser = window.webRoot+"/role/roleUserList?roleId="+roleId;
				var s1 = createFrame(urlForUser,roleId);
				parent.$("#dg").dialog(
					{
						title : "分配人员",
						modal:true,
						width: $(window).width()*0.5,
			        	height: $(window).height()*0.7,
    					top : $(window).width()*0.1,
						left : $(window).height()*0.1,
						content : s1,
					})
				$('#roleTable').datagrid({ onClickCell:function(rowIndex, field, value){ 
      			} }); 
				parent.$("#dg").dialog('open');
			}
				//父页面设置取的当前选择行roleId的函数，供子页面使用。
				 function getRoleId(){
			    	var tmp =  $("#roleTable").datagrid("getSelected").roleId;
			    	$("#roleTable").datagrid("clearSelections");
			    	return tmp;
			    }
		    
			//格式化修改用户
			function formateRoleEdit(value,row,index){
				var s = '<a href="#" onclick="editRole(\''+row.roleId+'\')">'+row.roleName+'</a>';
				return s;
			}

			// 分配菜单功能
			function allocateRight(roleId,data){
				var menuIds = new Array();
				var operationIds = new Array();
				for(var i in data){
					menuIds.push(data[i].id);
				}
				// 处理功能列表
				for(var i in data){
					if(data[i].attributes.flag){
						for(var j in data[i].children){
							if(data[i].children[j].checked){
								for(var k in menuIds){
									if(menuIds[k] == data[i].children[j].id){
										operationIds.push(menuIds[k]);
										menuIds.splice(k,1);
									}
								}
							}
						}
					}
				}
				
				//var roleId = $('#table').datagrid('getSelected').roleId;
					$.ajax({
		    			method:"post",
		    			url: "${webRoot}/menu/saveMenuRole?menuIds="+menuIds+"&roleId="+roleId,
		    			async:false,
		    			error: function(request) {
		    				$('#roleTable').datagrid('clearChecked');
		    				$.messager.alert('错误', '菜单分配失败！', 'error');
		    			},
		    			success: function(data) {
							parent.$("#win").dialog("close");
		    				$('#roleTable').datagrid('clearChecked');
		    			}
		    		});
		    		
		    		$.ajax({
		    			method:"post",
		    			url: "${webRoot}/operation/saveOperation_Role?operationIds="+operationIds+"&roleId="+roleId,
		    			async:false,
		    			error: function(request) {
		    				$.messager.alert('错误', '功能分配失败！', 'error');
		    				$('#roleTable').datagrid('clearChecked');
		    			},
		    			success: function(data) {
							parent.$("#win").dialog("close");
							$('#roleTable').datagrid('clearChecked');
		    			}
		    		});
			}
			
			//设置查询条件
			function queryCondition() {
				var roleName = $("#roleName").val();
				$('#roleTable').datagrid({
					queryParams: {
						"roleName": roleName
				  }
				});
			}
			
			//删除角色
			function delRole() {
				var roldIds = "";
				var row = $('#roleTable').datagrid('getChecked');
				if(!row||row.length==0) {
					$.messager.alert('提示','请选择需要删除的角色!','info');
					return;
				}
				for(var i = 0 ; i<row.length ; i ++) {
					roldIds += row[i].roleId + ",";
				}
				$.messager.confirm('提示','确认删除选择的角色？',function(r){
					if(r) {
						$.ajax({
			    			type:"post",
			    			url:"${webRoot}/role/delete",
			    			data:{
			    				"roleIds":roldIds
			    			},
			    			async:false,
			    			error:function(request) {
			    			},
			    			success:function(data) {
			    				if(data == "false"){
			    					$.messager.alert('提示','所选角色被分配了用户，请勿删除！','info');
			    				}
			    				$('#roleTable').datagrid('reload');
			    			}
			    		});
					}
				});
			}
			//获得子页面userSelect4Role里面的userIds
			function getUserIds(){
				return  window.frames["userSelect4Role"].userIds ;
			}
			
			//获得子页面所选定的roleId
			function getUserRoleId(){
				return window.frames["userSelect4Role"].roleId ;
			}
			//获得子页面datagrid元素
			function getDatagrid(){
				return window.frames["userSelect4Role"].$("#userDisplay");
			}
	    </script>
	</body>
</html>
