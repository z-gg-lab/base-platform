<!DOCTYPE html>
<html>
	<head>
	    <title>个人信息编辑</title>
	    <#include "../../include.ftl">
	</head>
	<body style="overflow-y: hidden" scroll="no">
		<div id="cc" class="easyui-layout" fit="true">   
    	<div data-options="region:'north',border:false,split:true" fit="true"> 
		    <form id="profileEditForm" action="${webRoot}/profile/save" method="post">
		    	<table class="table-list">
		    		<tr>
		    			<input type="hidden" name="userId" value="${(accountInfoVO.userId)!''}"/>
		    			<td class="t-title">帐号：<font style="color:red">*</font></td>
		    			<td>	    				
		    				<input class="easyui-textbox" type="text" maxlength="100" name="userAccount" value="${(accountInfoVO.userAccount)!''}" disabled="true "/>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td class="t-title">昵称：</td>
		    			<td>
		    				<input class="easyui-textbox" name="nickName" value="${(accountInfoVO.nickName)!''}" data-options="validType:'length[0,50]'"/>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td class="t-title">所属机构：<font style="color:red">*</font></td>
						<td>
							<input name="departmentId" type="text" disabled="true" class="easyui-textbox" id="userTree"  />
						</td>
		    		</tr>
		    		<tr>
			    		<td class="t-title" width="30%">类型：<font style="color:red">*</font></td>
	    			<td><input class="easyui-combobox" value="${(accountInfoVO.type)!''}" name="type" disabled="true " data-options="url:'${webRoot}/user/type',valueField:'configValue',textField:'name',panelHeight:100"></td>
		    		</tr>
		    		<tr>	
		    		<td class="t-title" width="30%">旧密码：<font style="color:red">*</font></td>
		    			<td>
		    				<input data-options="novalidate:true" id="Oldpassword" name="Oldpwd" maxlength="100" type="password" data-options="required:true" 
		    				class="easyui-textbox"/>
		    			</td>
		    		</tr>
		    		<tr>	
		    		<td class="t-title" width="30%">新密码：</td>
		    			<td>
		    				<input data-options="novalidate:true,validType:['noChinese','nospace','specialCharactersComma','length[0,100]']" id="password" name="pwd" maxlength="100" type="password" value="" 
		    				class="easyui-textbox"/>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td class="t-title" width="30%">确认密码：</td>
		    			<td>
		    				<input data-options="novalidate:true" id="repassword" maxlength="100" maxlength="100" name="repassword" type="password" value="" 
		    				 class="easyui-textbox" validType="equalTo['#password']"  />
		    			</td>
		    		</tr>
		    		<tr>
		    			<td class="t-title" width="100px">姓名：<font style="color:red">*</font></td>
		    			<td>
		    				<input class="easyui-textbox" name="userName" value="${accountInfoVO.userName!''}" disabled="true " />
		    			</td>
		    		</tr>
		    		<tr>
			    		<td class="t-title" width="30%">用户编码：</td>
		    			<td>
		    				<input class="easyui-textbox" id="userCode" name="userCode" value="${accountInfoVO.userCode!''}" disabled="true " />
		    			</td>
	    			</tr>
		    		<tr>
		    			<td class="t-title">电话：</td>
		    			<td>
		    			<input id="phone" maxlength="100" maxlength="100" name="phone" value="${(accountInfoVO.phone)!''}" 
		    				class="easyui-textbox" data-options="novalidate:true,validType:'phoneRex'" title="请输入正确的手机号码！~"/>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td class="t-title">邮箱：</td>
		    			<td>
		    				<input id="email" maxlength="100" name="email" value="${(accountInfoVO.email)!''}" 
		    				 class="easyui-textbox" data-options="novalidate:true,validType:['email','length[0,25]']" title="邮箱格式不对"/>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td class="t-title" align="center" width="30%">性别： </td>
		    			<td>
		  	    			<input type="hidden" id="gender" />
		    				<input type="radio" id="men" name="gender" value="1" />男
		    				<input type="radio" id="women" name="gender" value="0" />女
		    			</td>
		    		</tr>
		    		<tr>
		    			<td class="t-title">描述：</td>
		    			<td>
		    				<input class="easyui-textbox" maxlength="100" validType="length[0,250]" data-options="novalidate:true,multiline:true" style="height:50px;" name="description" value="${(accountInfoVO.description)!''}"/>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td colspan="2">
		    				<span style="size:7px;">注：标<font style="color:red">*</font>的为必填项！</span>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td colspan="2">
		    				<a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-save" onclick="saveProfile()" style="margin-left:200px;">保存</a>
		    			</td>
		    		</tr>
		    		
		    	</table>
		    </form>
	    </div>
	    </div>
	    <script type="text/javascript" src="${webRoot}/statics/base/js/md5.js"></script>
	    <script>
	    
	    // 传递部门ID
  		 $(function(){
			$.ajax({						
				type : "post",
				url : "${webRoot}/user/queryDepartmentName",
				data : {"userId":"${accountInfoVO.userId}"},
				success:function(data) {
					$('#userTree').textbox('setText',data);
				}
			})
			
    		if(${(accountInfoVO.gender)!''} == 1){
    			$("#men").attr('checked', 'checked');
			}
    		else if(${(accountInfoVO.gender)!''} == 0){
    			$("#women").attr('checked', 'checked');
			}
		})
			
	     $.extend($.fn.validatebox.defaults.rules, {  
	   		equalTo: {
	        validator:function(value,param){
	            return $(param[0]).val() == value;
	        	},
	        message:'两次密码输入不匹配'
	    		}
	        });
	    
	    $.extend($.fn.validatebox.defaults.rules, {  
	   		phoneRex: {
	        validator:function(value){
	        //	var rex=/^1[3-8]+\d{9}$/;
	        var rex=/^1([38]\d|4[57]|5[0-35-9]|7[06-8]|8[89])\d{8}$/; 
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
	    	
    	// 保存
    	function saveProfile() {
    		var OldPwd = $('#Oldpassword').textbox('getText');
    		var newPwd = $('#password').textbox('getText');
    		if(newPwd == ""){
				$("#profileEditForm").form({
					url:"${webRoot}/profile/save",
    				onSubmit:function(){	  
	    				var isValid = $(this).form('validate');
						return isValid;	
    				},
					success:function(data) {
						$.messager.alert('提示','保存成功!','info');
					}
    			});
    			$('#profileEditForm').submit();
    		}else{
    			var hash = hex_md5(OldPwd);
	    		if(hash == '${accountInfoVO.pwd}'){
	    			$("#profileEditForm").form({
		    			url:"${webRoot}/profile/save",
			    				onSubmit:function(){	  
				    				var isValid = $(this).form('validate');
									return isValid;	
			    				},
		    			success:function(data) {
		    					$.messager.alert('提示','保存成功!','info');
		    			}
	    			})
	    			$('#profileEditForm').submit();
	    		}else{
	    			$.messager.alert('提示','输入的旧密码错误，修改操作失败!','info');
	    		}
    		} 			   		
    	}
	    </script>
	</body>
</html>