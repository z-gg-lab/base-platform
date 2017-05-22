<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>${systemName!''}</title>
		<link href="${webRoot}/statics/base/css/login.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${webRoot}/statics/base/js/jquery.min.js"></script>
		<script type="text/javascript">
			$(function(){
				if (window != top){
					top.location.href = "login.ftl";
				}
			});
		</script>
	</head>
	
	<body>
		<form id="loginForm" action="${webRoot}/login" method="post">
			<div class="login-banner">
			  <div class="login-box">
			    <div class="login-form">
			      <div class="title"> <span> 帐号登录</span> </div>
			      <div class="user">
			        <label></label>
			        <input id="username" name="username" type="text" placeholder="请输入用户名" autofocus value="${username!}" />
			      </div>
			      <div class="password">
			        <label></label>
			        <input id="password" name="password" type="password" placeholder="请输入密码" />
			      </div>
			      <div class="remember">
			        <label>
			          <input type="checkbox" name="rememberMe" />
			          记住密码</label>
			     </div>
			    <#if Request.isCaptchaNeeded>
			      <div class="yzm">
			        <input type="text" name="captcha" />
			        <div class="pic"><img id="captchaImg" src="${webRoot}/captchaServlet" /></div>
			        <div class="refresh"><a onclick="changeImg();">看不清？换一个</a></div>
			      </div>
			    </#if>
			      <div class="tips"><span>${errorMsg!}&nbsp;</span></div>
			      <div class="login-btn"><a onclick="doLogin();">登&nbsp;录</a></div>
			    </div>
			  </div>
			</div>
			<div class="footer-box">
			  <p>${copyrightInfo!''}</p>
			</div>
		</form>
		<script>
			//登录
			function doLogin() {
				if (!$("#username").val()) {
					$("#username").focus();
					$(".tips").html("<span>用户名不能为空！ </span>");
					return;
				}
				if (!$("#password").val()) {
					$("#password").focus();
					$(".tips").html("<span>密码不能为空！ </span>");
					return;
				}
				$("#loginForm").submit();
			}
			$("#loginForm").keydown(function(e){
				var e = e || event,
				keycode = e.which || e.keyCode;
				if (keycode == 13) {
					doLogin();
				}
			});
			
			function changeImg() {
				document.getElementById("captchaImg").src = "${webRoot}/captchaServlet?"+Math.random();
			}
		</script>
	</body>
</html>
