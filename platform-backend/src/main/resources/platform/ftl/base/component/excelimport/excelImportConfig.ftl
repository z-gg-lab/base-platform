
<!DOCTYPE html>
<html>
	<head>
	    <title>模板配置</title>
	    <#include "../../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/component/jquery.edatagrid.js"></script>
	</head>
	<body style="overflow-y: hidden"  scroll="no">
	         <table id="configTable" pageSize="20" style="padding:30px;" fit=true
	            data-options="singleSelect:false,collapsible:false,
				pagination:true,
				toolbar:'#tb',
				idField:'id',
				method:'post',
				remoteSort:false,
				multiSort:true,
				rownumbers:true
	            ">

			<div id="tb" style="padding:4px;height:30px;">
				<a href="#" class="easyui-linkbutton" iconcls="icon-add" plain="true" onclick="loadTable()">添加字段</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-save" plain="true" onclick="saveConfig()">确认</a>
				<a href="#" class="easyui-linkbutton" iconcls="icon-remove" plain="true" onclick="deleteConfig()">删除</a>
			</div>

			<thead>
				<tr>
                    <th data-options="field:'id',checkbox:'true'"></th>
					<th data-options="field:'columnName',width:90,editor:'text'">字段名</th>
					<th data-options="field:'fieldName',width:90,editor:'text'">列名</th>
					<th data-options="field: 'type', title: '类型', width: 80, editor:{type:'combobox',options:{valueField:'name',textField:'name',data:products,required:true}}">类型</th>
					<th data-options="field:'relationFieldName',width:80,editor:'text'">关联字段名</th>
					<th data-options="field:'length',width:50,editor:'number'">长度</th>
					<th data-options="field:'sortNo',width:30,editor:'text'">排序</th>
				</tr>
			</thead>
		</table>
		<script>

	   var products = [
	        {value:'1',name:'字符串'},
	        {value:'2',name:'数字'},
	        {value:'3',name:'时间'}
		];

		var tableName = parent.testExcel;
		var id = parent.excelId;
		var columnNames = new Array();
		var sortNos = new Array();
		if(id){
			$("#configTable").datagrid({
				url:'${webRoot}/component/excelImportTemplate/query/'+id
			});
		}

		function loadTable(){

			var rows = $("#configTable").datagrid("getRows");
			for(var i in rows){
				columnNames.push(rows[i].columnName);
			}

            for(var i in rows){
                sortNos.push(rows[i].sortNo);
            }

			console.log(sortNos)
			var url = '${webRoot}/component/excelImportTemplate/table';
			$("#dg").dialog({
		     	title: "字段选择",
		     	content:createFrame(url),
		        width: 400,
		        height: 400,
		        top:($(window).height()-400)*0.5,
				left:($(window).width()-300)*0.5
			});
			$("#dg").dialog('open');
		}

		function createFrame(url) {
			var s = '<iframe id="columnSelect" name="userSelect4Role" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
			return s;
		}

		// 控制编辑
		$("#configTable").edatagrid({

		});

		//删除配置
		function deleteConfig() {
			var row = $("#configTable").datagrid('getChecked');
			var delRows = [];
			if(row){
        		$.messager.confirm('提示','确认删除选择的字段？',function(r){
					if(r) {
						// 删除行会调用刷新，index会发生改变，先赋值再删除可以解决
						for(var i in row){
							delRows.push(row[i]);
						}
						for(var i in delRows){
	        				$('#configTable').datagrid('deleteRow',$('#configTable').datagrid('getRowIndex', delRows[i]));
						}
	        		}
				});
			}
		}

		function saveConfig() {
			var rows = 	$("#configTable").edatagrid('getRows');
			// 结束所有的编辑，确保存储的是最新值
			for(var i in rows){
				$("#configTable").datagrid("endEdit",i);
			}
			parent.testExcel = rows;
			cancle();
		}

		function cancle(){
			closeDialog("win");
		}

		</script>
		<div id="dg" class="easyui-dialog" closed="true" data-options="modal:true"></div>
	</body>
</html>