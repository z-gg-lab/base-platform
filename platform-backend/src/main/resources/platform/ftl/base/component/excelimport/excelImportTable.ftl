<!DOCTYPE html>
<html>
<head>
    <title>模板配置</title>
<#include "../../../include.ftl">
</head>
<body style="overflow-y: hidden" scroll="no">
<table id="t1" pageSize="20" style="padding:30px;" fit=true
       data-options="singleSelect:false,collapsible:false,
				pagination:true,
				toolbar:'#tb',
				idField:'columnName',
				method:'post',
				remoteSort:false,
				multiSort:true,
				rownumbers:true,
	            ">

    <div id="tb" style="padding:4px;height:30px;">
        <a href="#" class="easyui-linkbutton" iconcls="icon-save" plain="true" onclick="saveField()">保存配置</a>
    </div>

    <thead>
    <tr>
        <th data-options="field:'id',checkbox:'true'"></th>
        <th data-options="field:'columnName'">字段名</th>
        <th data-options="field:'columnComment'">列名</th>
        <th data-options="field:'dataType'">字段类型</th>
        <th data-options="field:'dataLength'">字段长度</th>
        <th data-options="field:'sortNo'">排序</th>
    </tr>
    </thead>
</table>
<script>

    $('#t1').datagrid({url:'${webRoot}/component/excelImportTemplate/loadtable?tableName='+ parent.tableName+'&&columnNames='+ parent.columnNames+'&&sortNos='+parent.sortNos});

    function saveField(){
        var row = $("#t1").datagrid('getChecked');

        for(var i in row){
            var len = row[i].dataLength;
            if(len==null){
                len = 50;
            }

            parent.$('#configTable').datagrid('appendRow',{
                columnName:row[i].columnName,
                fieldName: row[i].columnComment,
                type:'字符串',
                relationFieldName:'null',
                length:len,
                sortNo:row[i].sortNo
            });
        }
        parent.columnNames = [];
        parent.sortNos = [];
        parent.$('#dg').dialog('close');

    }


</script>
</body>
</html>