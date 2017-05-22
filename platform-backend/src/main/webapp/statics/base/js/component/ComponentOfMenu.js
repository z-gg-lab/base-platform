/**
 * 角色分配菜单
 * 	call : 方法名 ———> 定义该方法时需指定一个data参数，用来接收返回的json数据
 * 	json : {"singleSelect":"true/false",....}
 */

function openMenuSelect(roleId, callback, json) {
    if (json.singleSelect == true) {
        checkbox = false;
    } else {
        checkbox = true;
    }

    if (!roleId) {
        roleId = null;
    }

    var root = window.webRoot;
    parent.$("#win").dialog({
        title: "菜单选择",
        width: 300,
        height: 350,
        top: $(window).height() * 0.3,
        left: $(window).width() * 0.5,
        toolbar: [{
            iconCls: 'icon-ok',
            text: '确定',
            handler: function () {

                var nodes = parent.$('#win').tree('getChecked');
                $.each(nodes, function (key, value) {
                    delete value.target;
                });
                var jsonStr = JSON.stringify(nodes);
                var json = JSON.parse(jsonStr);

                if (json == "") {
                    var data = parent.$('#win').tree('getSelected');
                    closeWindow();
                    callback(roleId, data);
                } else {
                    closeWindow();
                    callback(roleId, json);
                }

            }
        }, {
            iconCls: 'icon-no',
            text: '关闭',
            handler: closeWindow
        }],

        onOpen: function () {
            // 解决父子级联问题
            var flag;
            parent.$("#win").tree({
                checkbox: checkbox,
                cascadeCheck: false,
                url: root + "/menu/getTreeDataByRoleSelected?pMenuId=-1&roleId=" + roleId,
                animate: false,
                line: false,
                onClick: function (node) {
                        var parentNode = parent.$('#win').tree('getParent', node.target);
                    },
                    onBeforeLoad: function (node, param) {
                        flag = false;
                    },
                    onCheck: function (node, checked) {
                        if (flag) {
                            flag = false; //关
                            var childNode = parent.$('#win').tree('getChildren', node.target);
                            var parentNode = parent.$('#win').tree('getParent', node.target);
                            if (parentNode != null) {
                                // 父节点的父节点
                                var pNode = parent.$('#win').tree('getParent', parentNode.target);

                                if (pNode != null) {
                                    var ppNode = parent.$('#win').tree('getParent', pNode.target);
                                }
                            }

                            if (checked) {

                                if (ppNode != null) { // 如果是父节点上面还有父节点
                                    parent.$('#win').tree('check', ppNode.target); //父节点的父节点的父节点勾选
                                }

                                if (pNode != null) { // 如果是父节点上面还有父节点
                                    parent.$('#win').tree('check', pNode.target); //父节点的父节点勾选
                                }

                                if (parentNode != null) { //如果不是根节点
                                    parent.$('#win').tree('check', parentNode.target); //父节点勾选
                                }
                                flag = true; //开，父节点走完，子节点无限递归
                                if (childNode.length > 0) {
                                    for (var i = 0; i < childNode.length; i++) {
                                        parent.$('#win').tree('check', childNode[i].target); //子节点勾选
                                    }
                                }

                            } else {
                                if (parentNode != null) { //如果不是根节点
                                    //父节点取消勾选
                                    //parent.$('#win').tree('uncheck', parentNode.target);
                                }
                                flag = true; //开，父节点走完，子节点无限递归
                                if (childNode.length > 0) {
                                    for (var i = 0; i < childNode.length; i++) {
                                        parent.$('#win').tree('uncheck', childNode[i].target); //子节点取消勾选
                                    }
                                }

                            }

                        }
                    },
                    onLoadSuccess: function (node, data) {
                        //加载完成，正常开启onCheck事件
                        flag = true;

                        // 展开勾选的节点
                        //							var nodes = parent.$("#win").tree('getChecked');
                        //							for(var i in nodes){
                        //								parent.$("#win").tree('expand',nodes[i].target);
                        //							}

                        parent.$("#win").tree('expandAll');

                    },
                    onBeforeExpand: function (node, param) {
                        if (node.attributes.flag == true) {
                            parent.$("#win").tree("options").url =
                                root + "/operation/getTreeData?menuId=" + node.id + "&roleId=" + roleId;
                            //parent.$('#win').tree('select', node.target);

                        } else {
                            parent.$("#win").tree("options").url =
                                root + "/menu/getTreeDataByRoleSelected?pMenuId=" + node.id + "&roleId=" + roleId;
                            //parent.$('#win').tree('select', node.target);
                        }

                    }
            });
        }
    });
    parent.$("#win").dialog("open");

}

//关闭部门选择dialog
function closeWindow() {
    parent.$("#win").dialog("close");
    $("#win").dialog("close");
    $('#table').datagrid('clearChecked');
}