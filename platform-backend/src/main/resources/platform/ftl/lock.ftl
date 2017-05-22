<!DOCTYPE html>
<html>
<head>
    <title>锁定</title>
	<#include "include.ftl">
    <link rel="stylesheet" type="text/css" href="${webRoot}/statics/base/css/index.css"/>
</head>
<body style="overflow:hidden;background:#fff" scroll="no">
	<form id="unlockForm" class="easyui-form" method="post">
		<div style="margin:10px 0px 5px 10px;">
			<img src="${webRoot}/statics/base/images/unlock.png" width="32" height="32" style="float:left"/><p style="float:left;margin-top:8px">请输入当前用户密码解锁</p>
			<p style="clear:left">
				<input class="easyui-textbox" style="width:180px" id="password" name="password" type="password"/>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-point" onclick="unlock()">解锁</a>
			</p>
		</div>
		<div style="margin-left:10px"><span class="tips" style="color:red"></span></div>
	</form>
	<script type="text/javascript" src="${webRoot}/statics/base/js/md5.js"></script>
	<script type="text/javascript">
		$(function(){
			$('#password').textbox().next('span').find('input').focus();
		});
		function unlock(){
			var password = hex_md5($('#password').textbox('getText'));
			$.ajax({						
				type : "post",
				url : "${webRoot}/profile/unlock",
				data : {
							"password":password,
							"userId":"${userId}",
							"globalTimeout":"${globalTimeout}"
						},
				success:function(data) {
					if(data == "success"){
						parent.$("#dialog").dialog("close");
					}else{
						$(".tips").text(data);
						$('#password').textbox('setValue','');
						$('#password').textbox().next('span').find('input').focus();
					}
				}
			});
		}
	</script>
</body>
</html>