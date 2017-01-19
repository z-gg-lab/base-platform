<!DOCTYPE html>
<html>
<head>
    <title>用户管理</title>
<#include "../../include.ftl">
    <script type="text/javascript" src="${webRoot}/statics/base/js/jquery.edatagrid.js"></script>
    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
<div region="west" split="false" style="width:200px;">
    <ul id="userTree" class="easyui-tree"/>
</div>
<div id="toolbar">
    <div style="margin:5px">
        <label>帐号：</label>
        <input class="easyui-textbox" type="text" id="userAccountQuery" data-options=" " style="width:100px"
               onkeyup="searchenter(event);"/>
        <label>&nbsp用户编码：&nbsp</label>
        <input class="easyui-textbox" type="text" id="userCodeQuery" data-options=" " style="width:100px"
               onkeyup="searchenter(event);"/>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true"
           onclick="queryDataAccount()"></a>
    </div>
</div>
<div id="mainConfigPanel" region="center" style="background: #eee; overflow-y:hidden">
    <table id="userTable" class="easyui-datagrid" title="用户列表" style="padding:30px;"
           toolbar="#toolbar" idField="id"
           rownumbers="false" fit="true">
        <thead>
        <tr>
            <th data-options="field:'userId',checkbox:true"></th>
            <th data-options="field:'userAccount',width:90,align:'left'">帐号</th>
            <th data-options="field:'nickName',width:90,align:'left'">昵称</th>
            <th data-options="field:'userName',width:90,align:'left'">姓名</th>
            <th data-options="field:'departmentName',width:90,align:'left'">所属部门</th>
            <th data-options="field:'userCode',width:90,align:'left'">用户编码</th>
        </tr>
        </thead>
    </table>
    <div id="toolbar">
        <div style="margin:5px">
            选中部门：<label id='selected-node-name'></label>
        </div>
        <div style="margin-bottom:5px">
        </div>
    </div>
    <script type="text/javascript">
    
        //获取变量
        var userIds = "${(userIds)}";
        var singleSelect = "${(isSingle)}";
        var type = "${(type)}";
        var methodOfType = "${(methodOfType)}";
        var methodOfUser = "${(methodOfUser)}";
        var backIds = "";
        var userArray = new Array();
        var initName = "";
        var isMerchant = "${(isMerchant)}";
        var typeDepartment = null;
        var isMerchantForDepartment = "false";
        $.ajax({
            type: "post",
            url: "${webRoot}/department/getFirstName",
            success: function (data) {
                var datanew = $.parseJSON(data);
                $('#selected-node-name').html(datanew.departmentName);
            }
        })
        //单选或者多选判断
        $(function () {
            if (singleSelect == "true") {
                $("#userTable").datagrid({singleSelect: true});
            }
            if (singleSelect == "false") {
                $("#userTable").datagrid({singleSelect: false});
            }
        })


        //修改性别显示
        function sex(value) {
            switch (value) {
                case 0:
                    return '<span >女</span>';
                case 1:
                    return '<span >男</span>';
            }
        }

        // 初始化配置项树
        $("#userTree").tree({
            checkbox: false,
            url: "${webRoot}/component/departmentselect/tree/data?parentId=-1&type=" + typeDepartment + "&flag=" + isMerchant,
            animate: false,
            line: false,
            onClick: function (node) {
                selectedNode = node;
                $('#selected-node-name').html(selectedNode.text);
                var row = $('#userTable').datagrid('getChecked');
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
                            isMerchantForDepartment = "false";
                        } else {
                            isMerchantForDepartment = "true";
                        }
                    }
                });
                loadList("", "",selectedNode.id, userIds, type, methodOfType, methodOfUser, isMerchantForDepartment);
            },
            onBeforeExpand: function (node, param) {
                $("#userTree").tree("options").url = "${webRoot}/component/departmentselect/tree/data?parentId=" + node.id + "&type=" + typeDepartment + "&flag=" + isMerchant;
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
        }
        ;

		Array.prototype.remove = function(b) { 
		var a = this.indexOf(b); 
		if (a >= 0) { 
		this.splice(a, 1); 
		return true; 
		} 
		return false; 
		}; 

        $('#userTable').datagrid({
            onCheck: function (index, row) {
                userIds += row.userId + ",";
                backIds += row.userId + ",";
                userArray.push(row);
            },
            onUncheck: function (index, row) {
                tmp = row.userId + ",";
                userIds = userIds.replace(tmp, "");
                backIds = backIds.replace(tmp, "");
                for (var i = 0; i < userArray.length; i++) {
                    if (userArray[i].userId == row.userId) {
                        userArray.remove(userArray[i]);
                    }
                }
            },
            onUnselect: function (index, row) {
                tmp = row.userId + ",";
                userIds = userIds.replace(tmp, "");
                backIds = backIds.replace(tmp, "");
                for (var i = 0; i < userArray.length; i++) {
                    if (userArray[i].userId == row.userId) {
                        userArray.remove(userArray[i]);
                    }
                }
                 
            },
            onSelectAll: function (rows) {
                for (var i = 0; i < rows.length; i++) {
                    userIds += rows[i].userId + ",";
                    backIds += rows[i].userId + ",";
                    userArray.push(rows[i]);
                }
            },
        })

        //查询功能2017年1月3日17:09:12
        function queryDataAccount() {
            $(loadList($("#userAccountQuery").textbox('getValue'),$("#userCodeQuery").textbox('getValue'), "-2", userIds, type, methodOfType, methodOfUser, isMerchantForDepartment));
        }

        // 初始化datagrid的时候根据传入的useIds反向加载列表
        $("#userTable").datagrid({url: "${webRoot}/user/listTree4Component"});
        function loadList(keywordAccount,keywordCode, parentId, userIds, type, methodOfType, methodOfUser, isMerchantForDepartment) {
            var _userIds = userIds || "";
            var _keywordAccount = keywordAccount || "";
            var _keywordCode = keywordCode || "";
            var _parentId = parentId || "";
            var _type = type || "";
            var _methodOfType = methodOfType || "";
            var _methodOfUser = methodOfUser || "";
            var _isMerchantForDepartment = isMerchantForDepartment || "";
            $('#userTable').datagrid({
                queryParams: {
                    "keywordAccount": _keywordAccount,
                    "keywordCode": _keywordCode,
                    "parentId": _parentId,
                    "userIds": _userIds,
                    "type": _type,
                    "methodOfType": _methodOfType,
                    "methodOfUser": _methodOfUser,
                    "isMerchantForDepartment": _isMerchantForDepartment,
                },
            });
            $('#userTable').datagrid('unselectAll');
        }
        ;
        //初始化加载，默认为部门类型
        $(loadList("","", "", userIds, type, methodOfType, methodOfUser, isMerchantForDepartment));
    </script>
</body>
</html>