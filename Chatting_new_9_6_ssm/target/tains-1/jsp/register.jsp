<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'register.jsp' starting page</title>
    <link rel="stylesheet" href="../css/register.css" type="text/css"/>
	<style>
	
		.gender{
		    display:inline;
			border:1px #CCCCCC solid;
			padding:1px 12px;
			margin-left:10px;
			margin-right:20px;
		}
	
	</style>
  </head>
  
  <body>
       <div class="wrapper">
	   <div class="container">
	     
	     <h1>欢迎注册即时聊天</h1>
	     
		 <form id="regForm" class="form" action="../users/register" method="POST">
		    <input type="text" name="userNickName" placeholder="昵称"><br/>
			<input id="pwd" type="password" name="userPassWord" placeholder="密码"><br/>
			<input type="password" name="confirm-userpassword" placeholder="确认密码"><br/>
			<input type="radio" class="gender" value="1" name="userSex">男 <input type="radio" class="gender" value="0" name="userSex">女<br/>
			<input type="date" name="userBirthday" value="2016-11-13" /><br/>
			<input type="file" name="userHeadPortrait" id="pic" accept="image/gif, image/jpeg, image/jpg" /><br/>
			<button type="submit" name="register" id="reg">立即注册</button>
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
  	<script type="text/javascript" src="../js/jquery-2.1.4.js"></script>
  	<script type="text/javascript" src="../js/jquery.validate-1.13.1.js"></script>
	<script>
		$(function(){
			$("#regForm").validate({
				rules:{
					userNickName:{
						required:true,
						rangelength:[2,10],
						remote:{
							url:"../users/check",
							type:"POST"
						}
						//minlength:6,
						//maxlength:10						
					},
					userPassWord:{
						required:true,
						rangelength:[6,16]
					},
					"confirm-userpassword":{
						equalTo:"#pwd"
					},
					userBirthday:{
						dateISO:true
					}
				},
				messages:{
					userNickName:{
						required:"必须填写用户名",
						rangelength:"用户名必须为2~10位"					
					},
					userPassWord:{
						required:"必须填写密码",
						rangelength:"用户名必须为6~16位"
					},
					"confirm-userpassword":{
						equalTo:"两次输入的密码不一致"
					},
					userBirthday:{
						dateISO:"请输入ISO格式日期（如：2016/09/09）"
					}
				}
			});
		});
		/* $('#reg').click(function (event) {
			if($("#regForm").valid()){
				var userNickName=$()
				event.preventDefault();
				$('form').fadeOut(300);
				$('.wrapper').addClass('form-success');
				setTimeout(function(){
				  location.href="login.jsp?userNickName="+userNickName+"&userPassWord="+userPassWord+"&userPassWord"+userPassWord+"&userSex="+userSex+"&userBirthday="+userBirthday+"&userHeadPortrait="+userHeadPortrait;
				},800);
			}
		}); */
	</script>
	
</html>
