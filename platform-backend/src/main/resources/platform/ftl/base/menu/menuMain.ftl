<!DOCTYPE html>
<html>
	<head>
	    <title>菜单管理</title>
	    <#include "../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/component/treegrid-dnd.js"></script>
	</head>
	<body class="easyui-layout"  style="overflow-y: hidden"  scroll="yes">
		 <div id="panel" region="center" style="background: #eee; overflow-y:hidden">
	    <table  id="menuEdit" style="padding:30px;"  fit=true title="菜单列表"
			url="${webRoot}/menu/menuData"
			rownumbers="true" toolbar="#tb"
			idField="menuId" treeField="menuName">
			<div id="tb" style="padding:4px;height:30px;">
				<a href="#" class="easyui-linkbutton" iconcls="icon-add" plain="true" onclick="editMenu(1,'添加同级目录')">添加同级目录</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-add" plain="true" onclick="editMenu(2,'添加下级目录')">添加下级目录</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-add" plain="true" onclick="editMenu(3,'添加菜单')">添加菜单</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-edit" plain="true" onclick="editMenu(4,'修改')">修改</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-remove" plain="true" onclick="deleteMenu()">删除</a>
			</div>
			<thead>
				<tr>
					<th data-options="field:'menuName',width:200,align:'left'">菜单名</th>
					<th data-options="field:'isDir',width:80,align:'center',formatter:formatIsDir">菜单类型</th>
					<th data-options="field:'url',width:200,align:'left'">菜单URL</th>
					<th data-options="field:'updateTime',width:140,align:'center',formatter:formatTimeYYYYMMDDHHMMSS">更新时间</th>
					<th data-options="field:'_operate',width:200,align:'left',formatter:formateOperation">操作</th>
				</tr>
			</thead>
		</table>
		</div>
		
		<script>
			// 拖拽排序
			$("#menuEdit").treegrid({
				onLoadSuccess: function(row){
					$(this).treegrid('enableDnd', row?row.id:null);
				},
				
				onDrop:function(targetRow,sourceRow,point){
				
					if(targetRow.isDir==0&&targetRow.children){
						$("#menuEdit").treegrid('reload',sourceRow.pMenuId);
						parent.$.messager.alert('提示','菜单下不能有子节点!','info');
						return false;
					}
					parent.$.messager.confirm('提示','确认移动该菜单？',function(r){
						if(r) {
							$.ajax({
				    			type:"post",
				    			url:"${webRoot}/menu/sort?parentId="+sourceRow._parentId+"&dropMenuId="+sourceRow.menuId+"&targetMenuId="+targetRow.menuId+"&point="+point,
				    			async:false
			    			});
						}else{
							$("#menuEdit").treegrid('reload',sourceRow.pMenuId);
						}
					});
				}
			});
		
			//格式化菜单类型
			function formatIsDir(value,row,index) {
				switch(value) {
					case 0:
						return "菜单";
					case 1:
						return "目录";
				}
			}
			
			//编辑菜单
			function editMenu(type,title) {
				var row = $('#menuEdit').datagrid('getSelected');
				if(!row) {
					parent.$.messager.alert('提示','请选择菜单节点!','info');
					return;
				}
				//目录下才可以新增下级目录
				if(type==2 && row.isDir != 1) {
					parent.$.messager.alert('提示','目录下才可新增下级目录，请选择目录!','info');
					return;
				}
				var url = "${webRoot}/menu/edit/"+row.menuId+"/"+type;
				parent.$("#dialog").dialog({
					title:title,
					width:340,
					height:330,
					top : $(window).height() * 0.3,
					left : $(window).width() * 0.4,
					content:createFrame(url),
					modal:true
				});
				parent.$("#dialog").dialog("open");
				
			}
			
			//删除菜单
			function deleteMenu() {
				var flag = true;
				var row = $('#menuEdit').datagrid('getSelected');
				if(!row) {
					parent.$.messager.alert('提示','请选择需要删除的菜单!','info');
					return;
				}
				//删除目录需要判断是否有子节点
				if(row.isDir == 1) {
					$.ajax({
		    			type:"post",
		    			url:"${webRoot}/menu/countSubMenus/"+row.menuId,
		    			async:false,
		    			error:function(request) {
		    			},
		    			success:function(count) {
							if(count > 0) {
								parent.$.messager.alert('提示','目录下有子节点，不能删除!','info');
								flag = false;
							}	    				
		    			}
		    		});
				}
				if(!flag) {
					return;
				}
				parent.$.messager.confirm('提示','确认删除该菜单？',function(r){
					if(r) {
						$.ajax({
			    			type:"post",
			    			url:"${webRoot}/menu/delete/"+row.menuId,
			    			async:false,
			    			error:function(request) {
			    			},
			    			success:function(count) {
								$('#menuEdit').datagrid('deleteRow',row.menuId);	
			    			}
			    		});
					}
				});

				
			}
			
			//打开一个弹窗
			function createFrame(url) {
				var s = '<iframe id="menuEditFrame" name="menuEditFrame" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
				return s;
			}
			
			//格式化功能
			function formateOperation(value,row,index) {
				if(row.isDir != 1) {//叶子节点
					return '<a href="#" onclick="listOperation(\''+row.menuId+'\')">功能列表</a>';
				} else {
					return "";
				}
			}
			
			//操作列表
			function listOperation(menuId) {
				var url = "${webRoot}/operation/init/"+menuId;
				parent.$("#dialog").dialog({
					title:'功能列表',
					width:$(window).height(),
					height:$(window).height(),
					top : $(window).height() * 0.3,
					left : $(window).width() * 0.4,
					content:createFrame(url),
					modal:true
				});
				parent.$("#dialog").dialog("open");
			}
		</script>
	</body>
</html>