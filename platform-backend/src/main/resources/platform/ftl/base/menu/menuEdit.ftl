<!DOCTYPE html>
<html>
	<head>
	    <title>菜单编辑</title>
	    <#include "../../include.ftl">
	</head>
	<body style="overflow-y: hidden" scroll="yes">
	    <form id="menuEditForm" action="${webRoot}/menu/save" method="post">
	    	<table class="table-list">
	    		<tr>
	    			<td class="t-title" width="30%">上级目录：</td>
	    			<td>
	    				${(menuVO.pMenuName)!''}
	    				<input type="hidden" maxlength="100" id="menuId" name="menuId" value="${(menuVO.menuId)!''}"/>
	    				<input type="hidden" maxlength="100" name="pMenuId" id="pMenuId" value="${(menuVO.pMenuId)!''}"/>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">菜单名：<font style="color:red">*</font></td>
	    			<td>
	    			<input class="easyui-textbox" data-options="required:false,validType:['specialCharacters','lengthCharacter[50,100]']"
						   prompt="请输入菜单名称" id="menuName" name="menuName" value="${(menuVO.menuName)!''}"/></td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">菜单类型：</td>
	    			<td>
	    				<select name="isDirSelect" disabled>
	    					<option <#if menuVO.isDir==1>selected</#if>>目录</option>
	    					<option <#if menuVO.isDir==0>selected</#if>>菜单</option>
	    				</select>
	    				<input type="hidden" name="isDir" id="isDir" value="${(menuVO.isDir)!''}"/>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">菜单URL：</td>
	    			<td><input class="easyui-textbox" type="text" data-options="required:false,validType:['lengthCharacter[125,250]']"id="url" name="url" value="${(menuVO.url)!''}" <#if menuVO.isDir==1>disabled="disabled"</#if>/></td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">图标：</td>
	    			<td>
	    				<input id="icon" name="icon" type="hidden"  value="${(menuVO.iconCls)!''}"/>
	    				<img id="_img" name="_img" src="${webRoot}/statics/base/icons/${(menuVO.iconCls)!'icon-tux'}.png"/>
	    				<a href="#" onclick="listIcon()">选择图标</a>
	    			</td>
	    		</tr>
	    		
	    		<tr>
	    			<td colspan="2">
	    				<span style="size:7px;">注：标<font style="color:red">*</font>的为必填项！</span>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td colspan="2" align="center">
	    				<a href="#" class="easyui-linkbutton" iconcls="icon-save" onclick="saveMenu()">保存</a>
	    				<a href="#" class="easyui-linkbutton" iconcls="icon-cancel" onclick="closeWindow()">取消</a>
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	    <script>
	    // 保存菜单
	    function saveMenu() {
	    	if(parent.testExcel!=undefined||parent.testExcel!=null){
		    	$("#icon").val(parent.testExcel);
		    	parent.testExcel = null;
	    	}
	    
			if($('#menuName').val()==""){
				$('#menuName').textbox({required:true});
				return false;
			}
			
			var menu = {
            	menuId:$("#menuId").val(),
            	pMenuId:$("#pMenuId").val(),
            	menuName:$("#menuName").val(),
            	isDir:$("#isDir").val(),
            	url:$("#url").val(),
            	iconCls:$("#icon").val(),
        	};
			
	    	$.ajax({
	    			type:"post",
	    			url:"${webRoot}/menu/save",
	    			data:menu,
	    			async:false,
	    			success:function(json) {
					 	var data = JSON.parse(json);
						//var pMenuId = data.pMenuId;
						var pMenuId = $("#pMenuId").val();
						var pMenu = parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#menuEdit').treegrid('find',pMenuId);
						var nodeArray = new Array();
						var row = parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#menuEdit').treegrid("getSelected");
						nodeArray.push(data);
						if(data.editType == 0) {
							//新增
							if(pMenu) {
								//ie下这地方有问题
								parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#menuEdit').treegrid('reload',pMenu.menuId);
								parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#menuEdit').treegrid('append',{parent:row.menuId,data:data});
							}else {
								parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#menuEdit').treegrid('append',{data:nodeArray});
							}
						} else {
							parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#menuEdit').treegrid('updateRow',{index:data.menuId,row:data});
						}
						closeWindow();
					}
		    });
	    
	    }
	    
	    //取消
	    function closeWindow() {
	    	parent.$("#dialog").dialog("close");
	    }
	    
    	function listIcon() {
			var url = "${webRoot}/menu/listIcons";
			parent.$("#iconFrame").dialog({
				title:"图标选择",
				content:createFrame(url),
				width:300,    
				height:350,
				top:270,
				left:640
			});
			parent.$("#iconFrame").dialog("open");
		}

		function createFrame(url) {
			var s = '<iframe id="conf"  scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
			return s;
		}
	    
	    
	    </script>
	</body>
</html>