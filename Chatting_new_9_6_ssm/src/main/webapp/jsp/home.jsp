<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'MyHome.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" href="../css/bootstrap.css" type="text/css"></link>
  </head>
  
  <body>
    <dl>
    	<dt>ID：${user.userLoginId}</dt>
    	<dd></dd>
    	<dt>姓名：${user.userNickName }</dt>
    	<dd></dd>
    	<dt>头像：${user.userHeadPortrait }</dt>
    	<dd></dd>
    </dl>
    	<div>
    	<span class="glyphicon glyphicon-heart"></span>
		<span class="glyphicon glyphicon-cloud"></span>
		<span class="glyphicon glyphicon-search"></span>
		<button class="btn btn-primary"><span class="glyphicon glyphicon-home"></span></button>
	</div>
  </body>
</html>
