<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  <link rel="stylesheet" href="../css/bootstrap.css" type="text/css"></link>
  </head>
  
  <body>
    

    
	    <div id="addiv" class="dropdown">
		    <button id="ag${g.gid}" data-toggle="dropdown" type="button" class="btn btn-primary dropdown-toggle btn-block">${g.gname}<span class="caret"></span></button>
		    <ul class="dropdown-menu" style="width:276.5px">
		        <li><input class="" type="text" id="reqinfo" style="width:276.5px"	placeholder="Enter request Info"></li>
		        <li><button type="button" id="req" class="btn btn-info btn-block">请求</button></li>
		    </ul>
		</div>
	<script type="text/javascript" src="../js/jquery-2.1.4.js"></script>
	<script type="text/javascript" src="../js/bootstrap.js"></script>
  </body>
</html>
