/**
 * 部门选择控件 -- 弹框版
 * 备注
 * 	json : {"singleSelect":"false","type":"1,2","showMerchant":"true","selectDepartment":"true"}
 */
function openDepartmentSelect(callback,json,nodeId) {
	//nodeId为数组
//	var nodeId = '402882e8-587014a9-0158-7015d8e6-000a';
	
	var type = null;
	// 防止重复弹框
	var count = 1;
	
    if (json.type != null) {
        type = json.type;
    }
    // 根据参数设置是否可以多选

    if (json.singleSelect == "true") {
        checkbox = false;
    } else {
        checkbox = true;
    }

    // 是否显示商户
    if(json.showMerchant=="true"){
    
    	// 是否可以选择机构
    	if (json.selectDepartment == "true") {
    		//checkbox = true;
    		selectDepartment = true;
    	} else {
    		//checkbox = false;
    		selectDepartment = false;
    	}
    }else{
    	selectDepartment = true;
    	
    }
    	
    
    var root = window.webRoot;
    parent.$("#win").dialog({
        title: "部门选择",
        width: 300,
        height: 300,
        top: $(window).height() * 0.3,
        left: $(window).width() * 0.4,
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
                    callback(data);
                } else {
                    closeWindow();
                    callback(json);
                }
            }
        }, {
            iconCls: 'icon-no',
            text: '取消',
            handler: closeWindow
        }],
        onOpen: function () {
            parent.$("#win").tree({
                url: root + "/component/departmentselect/tree/data?parentId=-1&type=" + type + "&flag=" + json.showMerchant,
                animate: true,
                lines: true,
                checkbox: checkbox,
                cascadeCheck:false,
                onLoadSuccess:function(node,data){
                	if(nodeId!=null){
                		for(var z in nodeId){
                			for(var i in data){
                    			if(data[i].id==nodeId[z]){
                    				var no = parent.$("#win").tree('find',nodeId[z]);
                    				if(checkbox){
                    					parent.$("#win").tree('check',no.target);
                    				}else{
                    					parent.$("#win").tree('select',no.target);
                    				}
                    			}
                    		}
                		}
                		
                	}
                },
                onClick:function(node){
                	// 如果不能选择机构，则进行验证
                    if (!selectDepartment) {
                        $.ajax({
                            type: "post",
                            url: window.webRoot + "/department/countmerchant?merchantId="+node.id,
                            async: false,
                            dataType: 'json',
                            success: function (data) {
                            	if (data == false) {
                            		parent.$.messager.alert('提示', '当前选择的是机构，请选择商户!', 'info');
                                    parent.$('#win').tree('uncheck',node.target);
                                    parent.$("#win").find('.tree-node-selected').removeClass('tree-node-selected');
                            	}else{
                            		parent.$('#win').tree('check', node.target);
                            	}
                            }
                        });
                    }
                	
                },
                onCheck:function(node, checked){
                	if (!selectDepartment) {
                        $.ajax({
                            type: "post",
                            url: window.webRoot + "/department/countmerchant",
                            data: {
                                "merchantId": node.id
                            },
                            async: false,
                            dataType: 'json',
                            success: function (data) {
                                    if (data == false&&count==1) {
                                    	parent.$.messager.alert('提示', '当前选择的是机构，请选择商户!', 'info');
                                    	count = 2;
                                    	parent.$('#win').tree('uncheck', node.target);
                                    }else{
                                    	count = 1;
                                    }
                            }
                        });
                    }
                },
                onBeforeExpand: function (node, param) {
                	parent.$("#win").tree("options").url = root + "/component/departmentselect/tree/data?parentId=" + node.id + "&type=" + type + "&flag=" + json.showMerchant;
                }
            });
        }
    });
    parent.$("#win").dialog("open");
}


// 关闭部门选择dialog
function closeWindow() {
    parent.$("#win").dialog("close");
    $("#win").dialog("close");
    $('#table').datagrid('clearChecked');
}