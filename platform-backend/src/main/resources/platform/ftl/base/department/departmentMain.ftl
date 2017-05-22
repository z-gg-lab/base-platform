<!DOCTYPE html>
<html>
	<head>
	    <title>机构管理</title>
	    <#include "../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/component/ConfigUtils.js"></script>
	</head>
	<body class="easyui-layout"  style="overflow-y: hidden"  scroll="yes">
		<div id="panel" region="center" style="background: #eee; overflow-y:hidden">
	    <table id="departmentEdit" class="easyui-treegrid" style="padding:30px;" fit=true title="机构列表"
			url="${webRoot}/department/departmentData"
			rownumbers="true" toolbar="#tb"
			idField="departmentId" treeField="departmentName">
			<div id="tb" style="padding:4px;height:30px;">
				<a href="#" class="easyui-linkbutton" iconcls="icon-add" plain="true" onclick="editDepartment(1,'添加同级机构')">添加同级机构</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-add" plain="true" onclick="editDepartment(2,'添加下级机构')">添加下级机构</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-add" plain="true" onclick="editDepartment(3,'添加机构')">添加机构</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-edit" plain="true" onclick="editDepartment(4,'修改')">修改</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-remove" plain="true" onclick="deleteDepartment()">删除</a>
			</div>
			<thead>
				<tr>
					<th data-options="field:'departmentId',width:80,align:'left',hidden:'true'">机构ID</th>
					<th data-options="field:'parentId',width:80,align:'center',hidden:'true'">父级ID</th>
					<th data-options="field:'departmentName',width:200,align:'left'">机构名称</th>
					<th data-options="field:'departmentCode',width:140,align:'left'">机构编码</th>
					<th data-options="field:'code',width:140,align:'center'">机构CODE</th>
					<th data-options="field:'fullCode',width:140,align:'center'">机构全CODE</th>
					<th data-options="field:'departmentLevel',width:140,align:'center'">层级</th>
					<th data-options="field:'type',width:140,align:'center',formatter:formatterType">机构类型</th>
					<th data-options="field:'remark',width:140,align:'center'">备注</th>
				</tr>
			</thead>
		</table>
		</div>
		
		<script>
			//格式化机构类型
			function formatterType(value){
				return format('DEPARTMENT_TYPE',value);
			}
		
			//修改机构信息
			function editDepartment(type,title) {
				var row = $('#departmentEdit').datagrid('getSelected');
				if(!row && type!=3) {
					parent.$.messager.alert('提示','请选择机构!','info');
					return;
				}
				
				if(type==3) {
					departmentId = -1;
					$('#departmentEdit').treegrid('clearChecked');
				}else{
					departmentId = row.departmentId;
				}			
				var url = "${webRoot}/department/edit/"+departmentId+"/"+type;
				parent.$("#dialog").dialog({
					title:title,
					width:330,
					top : $(window).height() * 0.3,
					left: $(window).width() * 0.4,
					content:createFrame(url),
					modal:true
				});
				parent.$("#dialog").dialog("open");
			}
			
			//删除机构
			function deleteDepartment() {
				var flag = true;
				var row = $('#departmentEdit').datagrid('getSelected');
				if(!row) {
					parent.$.messager.alert('提示','请选择需要删除的机构!','info');
					return;
				}
				//判断是否有子节点
					$.ajax({
		    			type:"post",
		    			url:"${webRoot}/department/countSubDepartments/"+row.departmentId,
		    			async:false,
		    			error:function(request) {
		    			},
		    			success:function(count) {
							if(count > 0) {
								parent.$.messager.alert('提示','该机构下有子机构，不能删除!','info');
								flag = false;
							}	    				
		    			}
		    		});
				if(!flag) {
					return;
				}
				parent.$.messager.confirm('提示','确认删除该机构？',function(r){
					if(r) {
						$.ajax({
			    			type:"post",
			    			url:"${webRoot}/department/delete/"+row.departmentId,
			    			async:false,
			    			error:function(request) {
			    			},
			    			success:function(data) {
			    				if(data=="success"){
									$('#departmentEdit').datagrid('deleteRow',row.departmentId);	
			    				}else{
			    					parent.$.messager.alert('提示','该机构下有用户，不能删除!','info');
			    				}
			    			}
			    		});
					}
				});

				
			}
			
			//打开一个弹窗
			function createFrame(url) {
				var s = '<iframe name="departmentEditFrame" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
				return s;
			}
		</script>
	</body>
</html>