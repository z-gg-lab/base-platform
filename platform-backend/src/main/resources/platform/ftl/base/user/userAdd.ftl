<!DOCTYPE html>
<html>
	<head>
	    <title>用户编辑</title>
	    <#include "../../include.ftl">
	</head>
	<style type="text/css">
	.t-title {
	width:40px;
	}
	.inputStyle{
	width:100px
	}
	.titleStyle{color:"4F4F4F"; align:"center" ;font-family:"楷体"}
	</style>
	<script>
	    $.extend($.fn.validatebox.defaults.rules, {  
	   		equalTo: {
	        validator:function(value,param){
	            return $(param[0]).val() == value;
	        	},
	        message:'字段不匹配'
	    		}
	        });
        $.extend($.fn.validatebox.defaults.rules, {  
	   		phoneRex: {
	        validator:function(value){
	        	var rex=/^1[3-8]+\d{9}$/;
	       		var rex2=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
			    if(rex.test(value)||rex2.test(value))
			    {
			      return true;
			    }else
			    {
			       return false;
			    }
				},
	        message:'电话号码格式错误'
			}
        });
	    </script>
	<body style="overflow-y: hidden" scroll="no">
	    <form id="userAddForm" action="${webRoot}/menu/save" method="post">
	    	<table class="table-list">
    			<tr>
    			<td   class="t-title">帐号：<font style="color:red">*</font></td>
    			<td >
    				<input type="hidden" maxlength="100" name="userId" value="${(accountInfoVO.userId)!''}"/>
    				<input id="userAccount"  data-options="required:true,novalidate:true,validType:['specialCharacters','nospace','lengthCharacter[50,100]']" title="该输入项为必输项" type="text" class="easyui-textbox" width="50px" name="userAccount" prompt="请输入登录帐号" value="${(accountInfoVO.userAccount)!''}"
    				 />	    				 
    			</td>
    			<td   class="t-title" >昵称：<font style="color:red">*</font></td>
    			<td>
    				<input name="nickName"  data-options="required:true,novalidate:true,validType:['lengthCharacter[25,50]','specialCharacters']" value="${(accountInfoVO.nickName)!''}"
    				class="easyui-textbox" />
    			</td>
    			<tr>
    			<td   class="t-title" width="30%">密码：<font style="color:red">*</font></td>
    			<td>
    				<input data-options="required:true,novalidate:true,validType:['noChinese','nospace','specialCharactersComma','length[0,100]']" title="该输入项为必输项" id="pwd" name="pwd" maxlength="100"  type="password" value="${(accountInfoVO.pwd)!''}" 
    				class="easyui-textbox"  />
    			</td>
    			<td   class="t-title" width="30%">确认密码：<font style="color:red">*</font></td>
    			<td>
    				<input data-options="required:true,novalidate:true" title="该输入项为必输项" id="repassword" maxlength="100"  name="repassword" type="password" value="${(accountInfoVO.pwd)!''}" 
    				 class="easyui-textbox" validType="equalTo['#pwd']" invalidMessage="两次密码输入不匹配" />
    			</td>
	    		</tr>
				<tr>
					<td class="t-title"  >所属机构：<font style="color:red">*</font></td>
					<td> 
						<input type="hidden"   name="departmentId"    id="normalUserTree"  />
						<input class="easyui-textbox"  disabled="true"  id = "formalUserTree"/>
					</td>
	    			<td   class="t-title" width="30%">类型：<font style="color:red">*</font></td>
	    			<td>
	    				<input type="hidden"   name="type"    id="type"  />
	    				<input id="userType" class="easyui-textbox"   data-options="disabled:'true'" >
	    			</td>
	    		</tr>
	    		<tr>
	    			<td   class="t-title" width="30%">姓名：<font style="color:red">*</font></td>
	    			<td>
	    				<input  name="userName"  data-options="required:true,novalidate:true,validType:['specialCharacters','nospace','lengthCharacter[25,50]']" value="${(accountInfoVO.userName)!''}"
	    				 class="easyui-textbox" />
	    			</td>
	    			<td class="t-title" width="30%">用户编码：</td>
	    			<td>
	    				<input id="userCode" name="userCode" data-options="required:false,novalidate:true,validType:['noChinese','specialCharacters','nospace','length[0,36]']" class="easyui-textbox"   value = "" />
	    			</td>
	    		</tr>
	    		<tr>
				<td class="t-title" width="30%">电话：</td>
	    			<td>
	    				<input id="phone" maxlength="100" maxlength="100" name="phone" value="${(accountInfoVO.phone)!''}" 
	    				class="easyui-textbox" data-options="novalidate:true,validType:'phoneRex'" title="请输入正确的手机号码！~"/>
	    			</td>
	    			<td class="t-title" width="30%">邮箱：</td>
	    			<td>
	    				<input id="email" maxlength="100" maxlength="100" name="email" value="${(accountInfoVO.email)!''}" 
	    				class="easyui-textbox" data-options="novalidate:true,validType:['email','length[0,25]']" title="邮箱格式不对"/>
	    			</td>
	    		</tr>
	    		<tr>
				<td class="t-title" width="30%">角色：</td>
	    			<td>
	    				<input id="userRoleString" name="userRoleString" class="easyui-textbox" readonly="readonly"  value = "" />
	    				<input id="userRole"  type="hidden" name="userRole"  value = ""/>
	    				<a href="#" onClick = 'userRoleDiff()'>角色选择</a>
	    			</td>
	    			<td   class="t-title" align="center" width="30%">性别： </td>
	    			<td>
	  	    			<input type="hidden" id="gender" />
	    				<input type="radio" id="men" name="gender" value="1" checked="true"/>男
	    				<input type="radio" id="women" name="gender" value="0" />女
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="t-title" width="30%">描述：</td>
	    			<td>
	    				<input class="easyui-textbox" data-options="novalidate:true,multiline:true,validType:['specialCharacters','nospace','lengthCharacter[125,250]']" style="height:50px;" name="description" value="${(accountInfoVO.description)!''}"/>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td style=" text-align: center" colspan="4">
	    				<span style="size:7px;">注：标<font style="color:red">*</font>的为必填项！</span>
	    			</td>
	    		</tr>
	    		<tr>
	    		
	    		<tr>
	    			<td colspan="4" align="center">&nbsp&nbsp&nbsp
	    				<a href="#" class="easyui-linkbutton" iconcls="icon-save" onclick="saveUser()">保存</a>
	    				<a href="#" class="easyui-linkbutton" iconcls="icon-cancel" onclick="closeWindow()">取消</a>
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	    
	    <script>
	    	 //初始化
	    	 // 传递部门ID
	  		 $(function(){
	  		 	if(typeof(parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.selectedNode.text) == "undefined"){
	  		 		$.ajax({						
					type : "post",
					url : "${webRoot}/department/getFirstName",
					success:function(data) {
						var datanew = $.parseJSON(data);
						$('#formalUserTree').textbox('setText',datanew.departmentName);
					}
					})
	  		 		parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.selectedNode.id = "";
					}
				else{
					$('#formalUserTree').textbox('setText',parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.selectedNode.text);
				}
				//当为商户用户时
				if(parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.isMerchant == 'true'){
					$('#userType').textbox('setValue', '商户用户');
				}
				if(parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.isMerchant == 'false'){
					$('#userType').textbox('setValue', '操作员用户');
				}
			})	    	
			
			
			
	    	//保存用户
	    	function saveUser() {	   
	    			$('#normalUserTree').val(parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.selectedNode.id);
	    			var stroage = null;
	    			var typeConfig = null;
	    			var configKey = "USER_TYPE";
	    			var value = $('#userType').val();
	    			$.ajax({
						type : "post",
						url : window.webRoot + "/config/format?configKey=" + configKey,
						async : false,
						dataType : "json",
						success : function(data) {
							storage = data;
						}
					});
					for ( var i in storage) {
						if (storage[i].name == value) {
							typeConfig =  storage[i].configValue;
						}
					}
	    			$('#type').val(typeConfig);
	    			//当是商户用户时，会保存对应的商户用户
	    			$("#userAddForm").form({
	    				url:"${webRoot}/user/save?isMerchant="+parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.isMerchant,
	    				onSubmit:function(){	
		    				var isValid = $(this).form('validate');
							return isValid;	
	    				},
	    				success:function(data) {
		    				var userId = data.userId;
		    				if(data == 'false'){		    				
		    					$.messager.alert('提示','该用户已存在！','info');
								return;
		    				}
		    				if(data == 'NoDepartment'){
		    					$.messager.alert('提示','请先选择机构！','info');
								return;
		    				}
		    				if(data == 'falseUserCode'){
		    					$.messager.alert('提示','该用户编码已存在！','info');
								return;
		    				}

		    				if(userId != '' && userId != null) {
	    						parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#userTable').datagrid('load');
	    					} else {
	    						parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#userTable').datagrid('load');
	    					}
		    				closeWindow();
		    			}
	    			})
	    			$("#userAddForm").submit();
	    	}
	    	//取消
	    	function closeWindow() {
	    		parent.$("#dialog").dialog("close");
	    		parent.$('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#userTable').datagrid('unselectAll');
	    	}
	    	
	    	function createFrame(url) {
				var s = '<iframe name="userRoleFrame" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
				return s;
			}
	    	//角色控件
	    	function userRoleDiff() {
				var url = "${webRoot}/user/userrole?userId="+""+"&excludeUser="+$("#userRole").val();
				parent.$("#userRole").dialog({
					title:"选择角色",
					content:createFrame(url),
					modal:true,
					width:570,
					height:380,
					top:$(window).height()*0.3,
					left:$(window).width()*0.3,
				});
				parent.$("#userRole").dialog("open");
			}
	    </script>
	</body>
</html>
