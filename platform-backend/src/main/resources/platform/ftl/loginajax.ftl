<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<#include "../../include.ftl">
		<link href="${webRoot}/statics/base/css/loginajax.css" rel="stylesheet" type="text/css" />
	</head>
	
	<body onload="getMouse()">
		<form id="loginForm">
			<div class="login-box">
				<div class="login-form">
					<div class="title"> <span> 帐号登录</span></div>
				    <div class="user">
				        <label></label>
				        <input id="username" name="username" type="text" placeholder="请输入用户名" value="${username!''}" />
				    </div>
				    <div class="password">
				    	<label></label>
				        <input id="password" name="password" type="password" placeholder="请输入密码" />
				    </div>
			      <div class="yzm" id="yzm">
			        <input type="text" name="captcha" />
			        <div class="pic"><img id="captchaImg" src="${webRoot}/captchaServlet"/></div>
			        <div class="refresh"><a onclick="changeImg();">看不清？换一个</a></div>
			      </div>
			      	<br>
				    <div class="login-btn"><a onclick="doLogin();">登&nbsp;录</a></div>
				</div>
			</div>
		</form>
		<script>
		
			$('#yzm').hide();
			// IE10
			$('#yzm').css('visibility','hidden');

			var count = 0;
	
			function getMouse(){
			 	document.getElementById("password").focus();
			}
		
			function changeImg() {
				document.getElementById("captchaImg").src = "${webRoot}/captchaServlet?"+Math.random();
			}
		
			$("#loginForm").keydown(function(e){
				var e = e || event,
				keycode = e.which || e.keyCode;
				if (keycode == 13) {
					doLogin();
				}
			});
		
			//登录
			function doLogin() {
				if (!$("#username").val()) {
					$("#username").focus();
					$(".tips").html("<span>用户名不能为空！</span>");
					return;
				}
				if (!$("#password").val()) {
					$("#password").focus();
					$(".tips").html("<span>密码不能为空！</span>");
					return;
				}
		
			$.ajax({
					method:"post",
					url:"${webRoot}/doLoginajax",
					data:$('#loginForm').serialize(),
					async:false,
					success:function(data){
						if(data=="succ"){
							window.close();
						}
						if(data=="cookie"){
							$.messager.alert('错误', '请输入当前用户的用户名', 'error');
						}
						if(data=="fail"){
							count++;
							$.messager.alert('错误', '用户名或密码错误！', 'error');
							if(count>=5){
								$('#yzm').show();
								$('#yzm').css('visibility','visible');
							}
						}
						if(data=="yzm"){
							$.messager.alert('错误', '验证码错误！', 'error');
						}
						
					}
				}); 
				
			}
		</script>
	</body>
</html>
