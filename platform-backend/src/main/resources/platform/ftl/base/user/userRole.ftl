<!DOCTYPE html>
<html>
	<head>
	    <#include "../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/component/ComponentOfDepartment.js"></script>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/component/ComponentOfMenu.js"></script>
	</head>
	<body style="overflow-y: hidden"  scroll="no">
	   	   <table id="roleTable" class="easyui-datagrid"  style="padding:30px;" fit=true
	            data-options="singleSelect:false,collapsible:false,
				toolbar:'#toolbar',
				idField:'roleId',
				method:'post',
				remoteSort:false,
				multiSort:true,
	            ">
	        <thead>
	            <tr>
	            	<th data-options="field:'roleId',checkbox:true"></th>
	                <th data-options="field:'roleName',width:100,align:'left',sortable:true">角色名</th>
	                <th data-options="field:'remark',width:200,align:'left'">角色描述</th>
	                <th data-options="field:'updateTime',width:150,align:'center',formatter:formatTimeYYYYMMDDHHMMSS">修改时间</th>
	            </tr>
	        </thead>
	    </table>
	    <div id="toolbar">
			<a href="#" class="easyui-linkbutton" onclick="addRole()" data-options="iconCls:'icon-ok',plain:true">确认</a>
			<a href="#" class="easyui-linkbutton" onclick="closeWindow()" data-options="iconCls:'icon-cancel',plain:true">取消</a>
		</div>
	    <script type="text/javascript">
	    	//初始化参数
	    	var roleName = ""; 
	    	var userId = "${(userId)}";
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
    		
    		    		//显示未分配的角色
    //		$("#roleTable").datagrid({
    //			url:'${webRoot}/role/listExcludeUser?userId='+'${(userId)}'
    //		})
    		//显示所有角色
    		$("#roleTable").datagrid({
    			url:'${webRoot}/role/listAll',
    			onLoadSuccess:function(data){
    				var roleIds =data.rows; 
    				$.ajax({
    				async: false,
    				type: "post",
    				url: '${webRoot}/role/listUserNoPage?userId='+'${(userId)}',
    				success: function(data){
    				//不再根据这个显示，而是根据 传入的显示
    					var temp = $.parseJSON(data);
    					var excludeUser = "${(excludeUser)}";
    					var excludeUsers = excludeUser.split(",");
    					for(var i = 0; i < roleIds.length; i++){
    						for(var j =0; j < excludeUsers.length - 1; j++){
	    						if(excludeUsers[j] == roleIds[i].roleId){
	    							$("#roleTable").datagrid('selectRow',i)
	    						}
	    					}
    					}
    					//$("#roleTable").datagrid('unselectRow',)
    				}
    			})
    			}
    		})
    		
			//返回
			function addRole(){
				var roleId = "";
				var temp = $('#roleTable').datagrid('getSelections');
				for(var i = 0; i < temp.length; i++){
					roleName = roleName + temp[i].roleName + ",";
				}
				for(var i = 0; i < temp.length; i++){
					roleId = roleId + temp[i].roleId + ",";
				}
				
				if(parent.window.frames["userEditFrame"] != null){
					parent.window.frames["userEditFrame"].$("#userRoleString").textbox('setValue',roleName);
					parent.window.frames["userEditFrame"].$("#userRole").val(roleId);
				}
				if(parent.window.frames["userEditFrameAdd"] != null){
					parent.window.frames["userEditFrameAdd"].$("#userRoleString").textbox('setValue',roleName);
					parent.window.frames["userEditFrameAdd"].$("#userRole").val(roleId);
				}
				parent.$("#userRole").dialog("close");
			}
			
			//取消
			function closeWindow(){
				roleName = ""; 
				parent.$("#userRole").dialog("close");
			}
	    </script>
	</body>
</html>
