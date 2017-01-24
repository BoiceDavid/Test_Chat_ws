<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'login.jsp' starting page</title>

  <link rel="stylesheet" href="../css/bootstrap.css" type="text/css"></link>
  <link rel="stylesheet" href="../css/login.css" type="text/css"></link>
  </head>
  
  <body>
    <div class="wrapper">
	    <div class="container">
	     
	     <h1 id="welcome">欢迎登陆即时聊天</h1>
	     
		 <form id="loginForm" class="form" action="../users/login" method="post">
		    <input type="text" name="userLoginId" placeholder="账号" value="${user.userLoginId}">
			<input type="password" name="userPassWord" placeholder="密码">
			<button type="submit" name="login" id="login-button" class="btn btn-primary">登录</button>
			<a href="register.jsp" name="register" class="btn btn-primary">注册</a>
		 <form>
	   </div>

	   <ul class="bg-bubbles">
	     <li><img src="../images/2012111181020854.jpg" width=38px height=38px></li>
			<li><img src="../images/1U45430O-1.jpg" width=80px height=80px></li>
			<li><img src="../images/1_120505144733_10.jpg" ></li>
			<li><img src="../images/1G5293960-0.jpg" width=60px height=60px></li>
			<li><img src="../images/500fe382bc063.jpg" ></li>
			<li><img src="../images/13323TW3S0-13962F.jpg" width=120px height=120px></li>
			<li><img src="../images/20129307343782.jpg" width=160px height=160px></li>
			<li><img src="../images/201291893228996.jpg" width=20px height=20px></li>
			<li><img src="../images/2012050316201581940.jpg" width=20px height=20px></li>
			<li><img src="../images/2012100413195471481.jpg" width=160px height=160px></li>
	   </ul>
   </div>
  </body>
  <script src="../js/jquery-2.1.4.js" type="text/javascript"></script>
  <script type="text/javascript" src="../js/jquery.validate-1.13.1.js"></script>
	<script>
	var uname = "${user.userNickName}";
	$(function(){
		if(uname!=null&""!=uname){
			$("#welcome").before("<h1>恭喜您，"+uname+"注册成功,账号为${user.userLoginId}</h1>");
		}
		$("#loginForm").validate({
				rules:{
					userLoginId:{
						required:true,
						rangelength:[6,9]
						//minlength:6,
						//maxlength:10						
					},
					userPassWord:{
						required:true,
						rangelength:[6,16]
					}
				},
				messages:{
					userLoginId:{
						required:"必须填写账号",
						rangelength:"账号必须为6~9位"					
					},
					userPassWord:{
						required:"必须填写密码",
						rangelength:"用户名必须为6~16位"
					}
				}
			});
		
	});
	
	/* $('#login-button').click(function (event) {
		event.preventDefault();
		$('form').fadeOut(300);
		$('.wrapper').addClass('form-success');
		setTimeout(function(){
		  location.href="./commpage/404.html";
		},800);
	}); */
	</script>
</html>
