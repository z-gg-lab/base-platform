<!DOCTYPE html>
<html>
	<head>
	    <title>配置管理</title>
	    <#include "../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/jquery.edatagrid.js"></script>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
	</head>
	<body class="easyui-layout" style="overflow-y: hidden" scroll="yes">
		
		<div region="west" split="false" style="width:200px;">
			<ul id="configTree" class="easyui-tree" />
		</div>
		
	    <div id="mainConfigPanel" region="center" style="background: #eee; overflow-y:hidden">
	    	
		    <table id="configTable" class="easyui-datagrid" title="配置项列表" style="padding:30px;" fit=true
		            toolbar="#toolbar" idField="id" pageSize = 20
		            url="${webRoot}/config/list/data"
		            pagination="true"
		            rownumbers="true" singleSelect="true">
		        <thead>
		            <tr>
		                <th data-options="field:'name',width:150,align:'left',formatter:formateOperation">配置项名</th>
		                <th data-options="field:'description',width:250,align:'left'">配置项描述</th>
		                <th data-options="field:'configKey',width:150,align:'left'">配置项键</th>
		                <th data-options="field:'configValue',width:150,align:'left'">配置项值</th>
		                <th data-options="field:'sortNo',width:50,align:'center'">排序</th>
		                <th data-options="field:'updateTime',width:150,align:'center',formatter:formatTimeYYYYMMDDHHMMSS">修改时间</th>
		            </tr>
		        </thead>
		    </table>
		    <div id="toolbar">
		    	<div style="margin:5px">
			    	选中树节点：<label id='selected-node-name'>根节点</label>
		    	</div>
		    	<div>
			    	<span>&nbsp配置项名：</span	><input id="_name" type="text" class="easyui-textbox" data-options="prompt:'请输入配置项名'" style="width:200px",/>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="queryData()">查询</a>
			        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addConfig();">添加</a>
			        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delConfig()">删除</a>
		    	</div>
		    </div>
	    </div>
	
	    <script type="text/javascript">
	    
	    $(function(){ $('#configTable').datagrid({ onClickCell:function(rowIndex, field, value){ if(field == "name"){ $(this).datagrid('clearSelections'); $(this).datagrid('onUnselectAll'); } } }); });
	    
	    	//格式化功能
			function formateOperation(value,row,index) {
				return '<a href="#" onclick="editConfig(\''+row.id+'\')">'+row.name+'</a>';
			}
			
	    	var selectedNode = {id: '-1'};
	    	// 初始化配置项树
			$("#configTree").tree({
				checkbox: false,
				url: "${webRoot}/config/tree/data?parentId=-1",
				animate: false,
				line: false,
				onClick: function(node) {
					selectedNode = node;
					$('#selected-node-name').html(selectedNode.text);
					$('input.easyui-searchbox').val('');
					loadList("", selectedNode.id);
				},
				onBeforeExpand: function(node, param) {
					$("#configTree").tree("options").url = "${webRoot}/config/tree/data?parentId=" + node.id;
				}
			});
			
			// 重新加载树节点
			function reloadTreeNode(currentNodeId, newNodeText) {
				var pnode = null;
				if (selectedNode && selectedNode.target) {
					pnode = $('#configTree').tree('getParent', selectedNode.target);
					if (currentNodeId && currentNodeId == selectedNode.id) {
						$('#selected-node-name').html(newNodeText);
					}
				} else {
					pnode = $('#configTree').tree('getRoot');
				}
				if (pnode) {
					$('#configTree').tree('reload', pnode.target);
				} else {
					$('#configTree').tree('reload');
				}
			};
	    	
	    	// 查询操作
	    	function queryData() {
	    		var keyword = $("#_name").val();
	    		loadList(keyword, selectedNode.id);
	    	}
	    	
	    	// 加载列表
	    	function loadList(keyword, parentId) {
	    		var _keyword = keyword || "";
	    		var _parentId = parentId || "";
				$('#configTable').datagrid({
					queryParams: {
						"keyword": _keyword,
						"parentId": _parentId
					}
				});
	    	};
	    	
	        //添加
			function addConfig() {
				var url = "${webRoot}/config/edit?parentId=" + selectedNode.id;
				
				parent.$("#dialog").dialog({
					title:"添加配置项",
					width:350,
					height:400,
					top : $(window).height() * 0.3,
					left : $(window).width() * 0.4,
					content:createFrame(url),
					modal:true
				});
				parent.$("#dialog").dialog("open");
			};
			
			//修改
			function editConfig(id) {
				var url = "${webRoot}/config/edit/"+id;
				parent.$("#dialog").dialog({
					title:"修改配置项",
					width:350,
					height:400,
					top : $(window).height() * 0.3,
					left : $(window).width() * 0.4,
					content:createFrame(url),
					modal:true
				});
				parent.$("#dialog").dialog("open");
			};
			
			// 删除
			function delConfig() {
				var row = $('#configTable').datagrid('getSelected');
				if(!row) {
					parent.$.messager.alert('提示','请选择需要删除的行!','info');
					return;
				}
				parent.$.messager.confirm('确认','确定要删除这条数据吗？', function(r){
					if (r){
						$.ajax({
			    			type:"post",
			    			url: "${webRoot}/config/delete/" + row.id,
			    			async: false,
			    			error: function(request) {
			    			},
			    			success: function(data) {
			    				// 返回主键
			    				if(data = "success") {
		    						$('#configTable').datagrid('load');
		    						reloadTreeNode();
		    					} else {
		    						parent.$.messager.alert('警告','删除失败!','error');
		    					}
			    			}
			    		});
					}
				});
			};
			
			//打开一个弹窗
			function createFrame(url) {
				var s = '<iframe name="configEditFrame" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
				return s;
			}
	    </script>
	</body>
</html>