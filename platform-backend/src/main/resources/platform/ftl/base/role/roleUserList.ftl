<!DOCTYPE html>
<html>
<head>
    <title>用户管理</title>
<#include "../../include.ftl">
    <script type="text/javascript" src="${webRoot}/statics/base/js/jquery.edatagrid.js"></script>
    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
    <script type="text/javascript" src="${webRoot}/statics/base/js/component/userSelect.js"></script>
</head>
<body>

<table id="userDisplay" class="easyui-datagrid" style="padding:30px;"
       toolbar="#toolbar" idField="id"
       pagination="false"
       rownumbers="true" fit="true">
    <thead>
    <tr>
        <th data-options="field:'userId',checkbox:true"></th>
        <th data-options="field:'userAccount',width:90,align:'left'">帐号</th>
        <th data-options="field:'nickName',width:90,align:'left'">昵称</th>
        <th data-options="field:'userName',width:90,align:'left'">姓名</th>
        <th data-options="field:'userCode',width:150,align:'left'">用户编码</th>
    </tr>
    </thead>
</table>
<div id="toolbar" style="padding:5px;height:auto">
    <div style="margin-bottom:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="selectUsers()">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
           onclick="delRole()">删除</a>
    </div>
</div>
</body>
<script type="text/javascript">
    var roleId = "${(roleId)}";
    //调用控件
    function selectUsers() {
        var userIds = "";
        $.ajax({
            type: "post",
            url: "${webRoot}/user/listTreeOfRole4UserId?roleId=" + roleId,
            data: roleId,
            async: false,
            success: function (data) {
                userIds = data;
            },
        });
        userSelect({"isSingle": "false", "excludeUserIds": userIds}, addUsers);
    }

    //添加用户callback函数
    function addUsers(userIds) {
        if (userIds == "") {
            return;
        }
        else {
            $.ajax({
                type: "post",
                url: "${webRoot}/user/saveUserRoleSecond?userIds=" + userIds + "&roleId=" + roleId,
                async: false,
                error: function (request) {
                },
                success: function (data) {
                    if (data == "false") {
                        $.messager.alert('警告', '操作有误请回到初始界面重新来过', 'info');
                    }
                    parent.$("#userSelect4Component").dialog("close");
                }
            });
            $("#userDisplay").datagrid('load');
        }
    }
    //设置显示的隶属于选定角色的用户

    $("#userDisplay").datagrid({
        url: "${webRoot}/user/listTreeOfRole?roleId=" + roleId,
    })

    function load() {
        $("#userDisplay").datagrid('load');
    }

    //添加角色
    var urlForUser = window.webRoot + "/component/userSelect4RoleAdd";
    function createFrameNow(url) {
        var s = '<iframe name="userEditFrame" scrolling="no" frameborder="0"  src="' + url + '" style="width:100%;height:98%;"></iframe>';
        return s;
    }


    //删除角色
    function delRole() {
        var users = "";
        var row = $('#userDisplay').datagrid('getChecked');
        if (!row || row.length == 0) {
            $.messager.alert('提示', '请选择需要取消分配的人员!', 'info');
            return;
        }
        for (var i = 0; i < row.length; i++) {
            users += row[i].userId + ",";
        }
        $.messager.confirm('提示', '确认取消分配选中的人员吗？', function (r) {
            if (r) {
                $.ajax({
                    type: "get",
                    url: "${webRoot}/role/deleteUsers",
                    data: {
                        "roleId": roleId,
                        "users": users
                    },
                    async: false,
                    error: function (request) {
                    },
                    success: function (data) {
                        $('#userDisplay').datagrid('load');
                        $('#userDisplay').datagrid('unselectAll');  //异步操作，delRole已经远离上下文。
                        $.ajax({
                            type: "post",
                            url: "${webRoot}/user/listTreeOfRole4UserId?roleId=" + roleId,
                            data: roleId,
                            async: false,
                            success: trans,
                        });
                    }
                });
            }
        });
        //delUsers = row;
    }

    function trans() {

    }

    //修改性别显示
    function sex(value) {
        switch (value) {
            case 0:
                return '<span >女</span>';
            case 1:
                return '<span >男</span>';
        }
    }

</script>
</body>
</html>
