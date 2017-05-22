<!DOCTYPE html>
<html>
	<head>
	    <title>用户管理</title>
	    <#include "../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/jquery.edatagrid.js"></script>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/component/ComponentOfDepartment.js"></script>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/component/ConfigUtils.js"></script>
	    
	</head>
	<body class="easyui-layout" style="overflow-y: hidden" scroll="no">

		<div region="west" split="false" style="width:200px;">
				<div class="easyui-panel" title="机构列表" fit="true" >
			<ul id="userTree" class="easyui-tree" />
			</div>
		</div>
		
	    <div id="mainConfigPanel" region="center" style="background: #eee; overflow-y:hidden">
	    	
		    <table id="userTable" class="easyui-datagrid" title="用户列表" style="padding:30px;"
		            toolbar="#toolbar" idField="id"
		            url="${webRoot}/user/listTree"
		            pagination="true"
		            pageSize="20"
		            method="post"
		            rownumbers="true" fit="true" singleSelect="false">
		        <thead>
		           <tr>
	            	<th data-options="field:'userId',checkbox:true"></th>
	                <th data-options="field:'userAccount',width:90,align:'left',formatter:formateUserName">帐号</th>
	                <th data-options="field:'nickName',width:90,align:'left'">昵称</th>
	                <th data-options="field:'type',width:90,align:'left',formatter:formatType">类型</th>
	                <th data-options="field:'userName',width:90,align:'left'">姓名</th>
	                <th data-options="field:'userCode',width:90,align:'left'">用户编码</th>
	                <th data-options="field:'gender',width:90,align:'left',formatter:sex">性别</th>
	                <th data-options="field:'phone',width:90,align:'left'">电话</th>
	                <th data-options="field:'email',width:100,align:'left'">邮箱</th>
	                <th data-options="field:'description',width:90,align:'left'">描述</th>
	            </tr>
		        </thead>
		    </table>
		    	<div id="toolbar">
		    	<div style="margin:5px">
			    	选中名称：<label id='selected-node-name'></label>
		    	</div>
		    	<div style="margin:5px">
		    	<label>帐号查询：</label>
			    	<input class="easyui-textbox" type="text" id="userAccount"  data-options=" prompt:'请输入用户帐号'" style="width:150px" onkeyup="searchenter(event);" />
			    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="queryData()">查询</a>
			        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addUser()">添加</a>
		       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delUser()">删除</a>
		        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="impUser();">用户信息导入</a>     
		    	</div>
		    </div>
	    </div>
	
	    <script type="text/javascript">	    	
		    //首列修改功能。
		    // $("#userTable").datagrid({
			//	onClickCell: function(index,field,value){
			//					var rows = $("#userTable").datagrid('getRows');
			//					editUser(rows[index].userId);
			//				 }
			//})
			var type = null;
			var isMerchant = "false";
			$.ajax({						
					type : "post",
					url : "${webRoot}/department/getFirstName",
					success:function(data) {
						 datanew = $.parseJSON(data);
						 $('#selected-node-name').html(datanew.departmentName);
					}
			})
			
		     //回车进入查询
			    	function searchenter(event) {
		        		event = event || window.event;
		        		if (event.keyCode == 13) {          
							queryData();
		        	}
		    	}
	   		
	   		//用户导入
	   		function impUser(){
	   			var url = "${webRoot}/user/userImp";
				parent.$("#dialog").dialog({
					title:"导入用户信息",
					content:createFrameImp(url),
					width:400,
					height:200,
					top:($(window).height()-420)*0.5,
    				left:($(window).width()-500)*0.5,
    				onClose:function(){
    					$('#userTable').datagrid('reload');
    					$('#userTable').datagrid('unselectAll');
    				}				
				});
				parent.$("#dialog").dialog("open");
	   		}
	   		
		    //修改性别显示
		    	function sex(value) {
		    		switch(value) {
						case 0:
							return '<span >女</span>';
						case 1:
							return '<span >男</span>';
					}	    			
				}
	    	var selectedNode = {id: '-1'};
	    	// 初始化配置项树
			$("#userTree").tree({
				checkbox: false,
				url: "${webRoot}/component/departmentselect/tree/data?parentId=-1&type=" + type + "&flag=" + "true",
				animate: false,
				line: false,
				onClick: function(node) {
					selectedNode = node;
					$('#selected-node-name').html(selectedNode.text);
					$('input.easyui-searchbox').val('');
					$.ajax({
                        type: "post",
                        url: "${webRoot}/department/countmerchant",
                        data: {
                            "merchantId": node.id
                        },
                        async: false,
                        dataType: 'json',
                        success: function (data) {
                            if (data == false) {
                                isMerchant = "false";
                        	}else{
                        		isMerchant = "true";
                        	}
                        }
                    });
					loadList("",selectedNode.id);
					
				},
				onBeforeExpand: function(node, param) {
					$("#userTree").tree("options").url = "${webRoot}/component/departmentselect/tree/data?parentId=" + node.id + "&type=" + type + "&flag=" + "true";
				}
			});
			
			// 重新加载树节点
			function reloadTreeNode(currentNodeId, newNodeText) {
				var pnode = null;
				if (selectedNode && selectedNode.target) {
					pnode = $('#userTree').tree('getParent', selectedNode.target);
					if (currentNodeId && currentNodeId == selectedNode.id) {
						$('#selected-node-name').html(newNodeText);
					}
				} else {
					pnode = $('#userTree').tree('getRoot');
				}
				if (pnode) {
					$('#userTree').tree('reload', pnode.target);
				} else {
					$('#userTree').tree('reload');
				}
			};
	    	
	    	// 查询操作
	    	function queryData() {
	    		var keyword = $("#userAccount").val();
	    		loadList(keyword, selectedNode.id);
	    	}
	    	
	    	// 加载列表
	    	function loadList(keyword, parentId) {
	    		var _keyword = keyword || "";
	    		var _parentId = parentId || "";
				$('#userTable').datagrid({
					queryParams: {
						"keyword": _keyword,
						"parentId": _parentId,
						"isMerchant": isMerchant,
					}
				});
				$('#userTable').datagrid('unselectAll');
	    	};
	    	$(loadList("",""));
	    	
	        //添加用户
			function addUser() {
				var url = "${webRoot}/user/add";
				parent.$("#dialog").dialog({
					title:"添加用户",
					content:createFrameAdd(url),
					modal:true,
					width:570,
					height:450,
					top:$(window).height()*0.3,
					left:$(window).width()*0.3,
				});
				parent.$("#dialog").dialog("open");
			}
			
			//修改用户
			function editUser(userId) {
				var row = $('#userTable').datagrid('getSelected');
				if(!row&&!userId) {
					$.messager.alert('提示','请选择需要修改的用户!','info');
					return;
				}
				if(!userId) {
					userId = row.userId;
				}
				var url = "${webRoot}/user/edit/"+userId;
				parent.$("#dg").dialog({
					title:"修改用户",
					content:createFrame(url),
					modal:true,
					width:570,
					height:450,
					top:$(window).height()*0.3,
					left:$(window).width()*0.3,
				});
				parent.$("#dg").dialog("open");
			}
			
			
			//打开一个弹窗
			function createFrame(url) {
				var s = '<iframe name="userEditFrame" id="userEditFrame" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
				return s;
			}
			function createFrameImp(url) {
				var s = '<iframe name="createFrameImp" id="createFrameImp" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
				return s;
			}
			function createFrameAdd(url) {
				var s = '<iframe name="userEditFrameAdd" id="userEditFrameAdd" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
				return s;
			}
			
			//格式化用户名
			function formateUserName(value,row,index) {
				var s = '<a href="#" onclick="editUser(\''+row.userId+'\')">'+row.userAccount+'</a>';
				return s;				
			}
			
			//格式化用户类型
			function formatType(value,row, index){
				return format("USER_TYPE",value);
			}
			
			//删除用户
			function delUser() {
				var userIds = "";
				var row = $('#userTable').datagrid('getChecked');
				if(!row||row.length==0) {
					$.messager.alert('提示','请选择需要删除的用户!','info');
					return;
				}
				for(var i = 0 ; i<row.length ; i ++) {
					userIds += row[i].userId + ",";
				}
				$.messager.confirm('提示','确认删除选择的用户？',function(r){
					if(r) {
						$.ajax({
			    			type:"post",
			    			url:"${webRoot}/user/delete",
			    			data:{
			    				"userIds":userIds
			    			},
			    			async:false,
			    			error:function(request) {
			    			},
			    			success:function(data) {
			    				$('#userTable').datagrid('load');
			    			}
			    		});
					}
				});
				$('#userTable').datagrid('unselectAll');
			}
			
		//晶晶的去除高亮
		$(function(){ 
			$('#userTable').datagrid({ 
				onClickCell:function(rowIndex, field, value){ 
					if(field == "userAccount"){ 
						$(this).datagrid('clearSelections');
						$(this).datagrid('onUnselectAll'); 
					} 
				} 
			}); 
		});
	    </script>
	</body>
</html>