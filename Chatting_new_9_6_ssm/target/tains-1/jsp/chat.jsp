<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>MyContact</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" href="../css/bootstrap.css" type="text/css"></link>
<link rel="stylesheet" href="../css/chat.css" type="text/css"></link>
<script type="text/javascript" src="../js/jquery-2.1.4.js"></script>
<script type="text/javascript">
	var userId = '${u.userId}';
	var userNickName = '${u.userNickName}';
	var groupMemberMap = '${groupMemberMap}';
	var toId;
	var ws;
	var target = "ws://localhost:8080/tains-1/chat?userId=" + userId;
	var tog;	//群id
	$(function() {
		if ('WebSocket' in window) {
			ws = new WebSocket(target);
		} else if ('MozWebSocket' in window) {
			ws = new MozWebSocket(target);
		} else {
			alert("浏览器不支持");
			return;
		}
		var friendreqId;	//好友申请者的Id
		var groupreqId; 	//入群申请者的Id
		ws.onmessage = function(event) {
			eval("var result = " + event.data);
			//var result = eval("("+event.data+")"); 
			if(result.type==1){		//单聊
				to=result.clickId;
				if($(".contact").length > 0){		
					$(".contact").each(function(index, item) {
						if($(this).is(":visible")==true){		//取得用户已经打开的聊天区
							var ts = $(this).attr("id").split("tent");
							to=ts[1];		//取得用户点击的好友Id
						}
					});
				}
				if (to == result.msgFromUserId) { //判断用户是否打开了与发送者对应的聊天区(接收方点击的好友ID与消息来源的Id是否相等)
					if (undefined != result.msgFromUserId
							&& undefined != result.acptime
							&& undefined != result.msgContent) {
						$("#content" + to).append(
								"<div>来自userId :" + result.msgFromUserId + ",接收时间："
										+ result.acptime + "<br/>消息内容："
										+ result.msgContent + "</div>");
					};
				}
				if(""==to||null==to||to!=result.from){	//如果点击的好友Id和消息来源不同，则加上消息条数
					//if($("pre").is(":visible")==false){
					if($("#"+result.from+">span").length==0){
						$("#"+result.from).append("<span class='badge'>"+result.row+"</span>");
					}else{
						$("#"+result.from+" span").text(result.row);
					}
					if(result.msgTypeId==3){	//好友请求部分
						friendreqId=result.msgFromUserId;//请求发送方的Id
						if(!result.hasOwnProperty("resp")){ //判断result中是否包含"resp"
							$("#friendreq").append("<button id='btnreq"+friendreqId+"' disabled='disabled'><span class='label label-info'><small>好友申请</small>"+result.fromnickname+"</span></button>");
							$("#friendreq").append("<pre class='prettyprint linenums'><small>请求内容：</small>"+result.msgContent+"</pre>");
							$("#friendreq").append("<li class='list-unstyled'><a id='yes"+friendreqId+"' class='btn btn-default'>接受</a><a id='no"+friendreqId+"' class='btn btn-default'>拒绝</a></li>");
						}else{
							if(result.resp==1){
								$("#prompt").empty();
								$("#prompt").append("<span id='pro"+friendreqId+"'>--"+result.fromnickname+"--接受了您请求</span>");
								if($("#contacts").find("#"+result.msgToUserId).length == 0){//	注意 站在请求发送方的角度 添加对方的Id	
								var to = result.msgToUserId;
								$("#contacts").append("<button id='"+to+"' type='button' class='btn btn-info btn-block'>"+result.fromnickname+"</button>");
									//动态生成的按钮进行事件委托，绑定函数
									$("#contacts").on('click','#'+to,function(){friclick(to);}); 
								}
							}else{
								$("#prompt").empty();
								$("#prompt").append("<span id='pro"+friendreqId+"' >--"+result.fromnickname+"--拒绝了您请求</span>");
							}
						}
						$("#friendreq").on('click','#yes'+friendreqId,function(){
							 	//$("#prompt").load("../friend/add",{"friendUserId":friendreqId,"myUserId":userId,"typeId":1});
								$("#prompt").append("<span>您接受了--"+result.fromnickname+"--的请求</span>");
							 	$("#btnreq"+friendreqId).remove();
							 	$("#friendreq>pre").remove();
							 	$(this).hide();
								$("#no"+friendreqId).remove();
								if($("#contacts").find("#"+friendreqId).length == 0){
									$("#contacts").append("<button id='"+friendreqId+"' type='button' class='btn btn-info btn-block'>"+result.fromnickname+"</button>");
									$("#contacts").on('click','#'+friendreqId,function(){friclick(friendreqId);});//注意 站在请求接收方的角度 添加请求发送方的Id					
								}
								var resp = {
									frontindex : 4,
		            				msgToUserId : userId,
		            				msgFromUserId : friendreqId,		//站在请求发送者的角度 判断发送者的消息是否已被我读取
		            				fromnickname : userNickName,
		            				msgContent : "1",
		            				msgTypeId : 3
								};
								var msgContent = JSON.stringify(resp);
								ws.send(msgContent);
							});
							$("#friendreq").on('click','#no'+friendreqId,function(){
								//$.post("../friend/add",{"friendUserId":friendreqId,"myUserId":userId,"typeId":0});
								$("#prompt").append("<span>您拒绝了--"+result.fromnickname+"--的请求</span>");
								$("#friendreq>pre").remove();
								$(this).hide();
								$("#yes"+friendreqId).remove();
								$("#prompt").hide();
								$("#btnreq"+friendreqId).remove();
								var resp = {
									frontindex : 4,
		            				msgToUserId : userId,
		            				msgFromUserId : friendreqId,		//站在请求发送者的角度
		            				fromnickname : userNickName,
		            				msgContent : "2",
		            				msgTypeId : 3		//	消息类型为好友申请
								};
								var msgContent = JSON.stringify(resp);
								ws.send(msgContent);
							}); 
					}
				}
			}else if(result.type==0){	//群聊
				//要判断所有聊天区哪一个是显示的，而不是判断哪一个存在
				$("pre").each(function(){//通过each来遍历
			    	if($(this).css("display")=='block'){ //通过$(this).css("css名") 来获取当前遍历元素的display值
			    		if($(this).attr("id").indexOf("cg")!=-1){
			    			var tg = $(this).attr("id").split("g");
							tog=tg[1];		//已打开聊天区对应的群Id
							
							if (result.gtype==0) {
								if (tog == result.getmsgid) { //判断用户是否打开了与发送者对应的聊天区(接收方点击的好友ID与消息来源的Id是否相等)																						
									if (undefined != result.sendName
											&& undefined != result.acptime
											&& undefined != result.msgContent) {
										$("#cg" + tog).append(
												"<div>来自user :" + result.sendName + ",接收时间："
														+ result.acptime + "<br/>消息内容："
														+ result.msgContent + "</div>");
									};
									if(result.aj=="1"){
										/* $.get("/Chatting_new_9_1/chat/gmsgread", { "getmsgid" : tog,
												"myId" : userId }); */
										 $.ajax({
											type: "POST",
											url : "/tains-1/chat/gmsgread",
											data : {
												getmsgid : tog,
												myId : userId
											} 
										}); 
									}
									alert("a");
									if (result.addnewmember=="1") {
										$("#gms"+tog).append("<center><button id='gm"+result.newmember+"' class='btn btn-info btn-small'>"+result.newname+"</button></br></center>");
									}									
								}else{
									if (result.row!=0) {
										//如果点击的好友Id和消息来源不同，则加上消息条数
										if($("#g"+result.getmsgid+">span").length==0){
											$("#g"+result.getmsgid).append("<span class='badge'>"+result.row+"</span>");
										}else{
											$("#g"+result.getmsgid+">span").text(result.row);
										}
									}
								}
							}							
			    		}else if($(this).attr("id").indexOf("tent")!=-1){//
						if (result.row!=0) {
							
							//如果点击的好友Id和消息来源不同，则加上消息条数
							if($("#g"+result.getmsgid+">span").length==0){
								$("#g"+result.getmsgid).append("<span class='badge'>"+result.row+"</span>");
							}else{
								$("#g"+result.getmsgid+">span").text(result.row);
							}
						}
					}
			    	}
			    });
			    var prel = $("pre").length;
			    if(prel <= 0){	
				    if (result.row!=0&&undefined != result.row) {							
						//如果点击的好友Id和消息来源不同，则加上消息条数
						if($("#g"+result.getmsgid+">span").length==0){
							$("#g"+result.getmsgid).append("<span class='badge'>"+result.row+"</span>");
						}else{
							$("#g"+result.getmsgid+">span").text(result.row);
						}
					}
			    }
			    /* if(result.newtype=="0"){
					alert("newtype");//此处行不通，用户没打开相应的聊天区时，再打开聊天区会加载不到新成员
						if(result.addnewmember=="1"){			//需考虑到新建群的时候（此时群内可能无成员，需新建成功时马上将群主加入右下方的群成员列表中）
							alert("b");
							$.ajax({
								type: "get",
				            	url: "../group/newadd",
				            	data:{"gid":result.getmsgid},
				            	success:function(result){
				            		eval("var map = " + result);
				            		for(m in map){
					            		$("#gms"+tog).append("<center><button id='gm"+m+"' class='btn btn-info btn-small'>"+map[m]+"</button></br></center>");
					            		
				            		}
				            	}
							});
						}
				} */
			    if(result.gtype=="-1"){
					if (result.row!=0) {
										
						//如果点击的好友Id和消息来源不同，则加上消息条数
						if($("#g"+result.getmsgid+">span").length==0){
							$("#g"+result.getmsgid).append("<span class='badge'>"+result.row+"</span>");
						}else{
							$("#g"+result.getmsgid+">span").text(result.row);
						}
					}			    	
			    }
				if(result.gtype=="2"){
					groupreqId=result.sendUserId;//请求发送方的Id(???)
					if(!result.hasOwnProperty("gresp")){ //判断result中是否包含"resp"
						$("#groupreq").append("<button id='btngreq"+groupreqId+"' disabled='disabled'><span class='label label-info'><small>入群申请</small>"+result.fromnickname+"</span></button>");
						$("#groupreq").append("<pre class='prettyprint linenums'><small>请求内容：</small>"+result.msgContent+"</pre>");
						$("#groupreq").append("<li class='list-unstyled'><a id='yes"+groupreqId+"' class='btn btn-default'>接受</a><a id='no"+groupreqId+"' class='btn btn-default'>拒绝</a></li>");
					}else{
						if(result.gresp=="1"){
								$("#groupprompt").empty();
								$("#groupprompt").append("<span id='gpro"+groupreqId+"'>--"+result.gname+"的群主-"+result.fromnickname+"--接受了您请求</span>");
								if($("#groups").find("#g"+result.getmsgid).length == 0){//	注意 站在请求发送方的角度 添加对方的Id	
									var tog = result.getmsgid;
									$("#groups").append("<button id='g"+tog+"' type='button' class='btn btn-info btn-block'>"+result.gname+"</button>");
									
									//动态生成的按钮进行事件委托，绑定函数
									$("#groups").on('click','#g'+tog,function(){groclick(tog);}); 
								}
							}else{
								$("#groupprompt").empty();
								$("#groupprompt").append("<span id='gpro"+friendreqId+"' >--"+result.gname+"的群主-"+result.fromnickname+"--拒绝了您请求</span>");
							}
					}
					
					$("#groupreq").on('click','#yes'+groupreqId,function(){
					 	//$("#prompt").load("../friend/add",{"friendUserId":friendreqId,"myUserId":userId,"typeId":1});
						$("#groupprompt").append("<span>您接受了--"+result.fromnickname+"--的请求</span>");
					 	$("#btngreq"+groupreqId).remove();
					 	$("#groupreq>pre").remove();
					 	$(this).hide();
						$("#no"+groupreqId).remove();
						
						if(tog==result.getmsgid){	//是否打开了对应的群窗口
							if($("#gms"+tog).find("#gm"+groupreqId).length == 0){
								$("#gms"+tog).append("<center><button id='gm"+groupreqId+"' class='btn btn-info btn-small'>"+result.fromnickname+"</button></br></center>");
								/* $("#gms"+tog).on('click','#gm'+groupreqId,function(){groclick(groupreqId);});//注意 站在请求接收方的角度 添加请求发送方的Id */					
							}
						}
						
						var resp = {
	            						frontindex : 8,		//请求发送方
	            						gmId :result.gmId,
	            						getmsgid : result.getmsgid,
	            						toId : groupreqId,
	            						fromId : userId,
	            						fromnickname : userNickName,
	            						msgContent : "1",	//表示接受
	            						msgType : 3		//入群请求（待修改）
	            					};
						var gmsgContent = JSON.stringify(resp);
						ws.send(gmsgContent);
					});
				  $("#groupreq").on('click','#no'+groupreqId,function(){
						//$.post("../friend/add",{"friendUserId":friendreqId,"myUserId":userId,"typeId":0});
						$("#groupprompt").append("<span>您拒绝了--"+result.fromnickname+"--的请求</span>");
						$("#groupreq>pre").remove();
						$(this).hide();
						$("#yes"+groupreqId).remove();
						$("#groupprompt").hide();
						$("#btngreq"+groupreqId).remove();
						var resp = {
	            						frontindex : 8,		//请求发送方
	            						gmId :result.gmId,
	            						getmsgid : result.getmsgid,
	            						toId : groupreqId,
	            						fromId : userId,
	            						fromnickname : userNickName,
	            						msgContent : "0",	//表示拒绝
	            						msgType : 3		//入群请求（待修改）
	            					};
						var msgContent = JSON.stringify(resp);
						ws.send(msgContent);
					});
					
			    }


			    }
			};
				
			
			$("#search").click(function() {
				var lid=$("#addfriendLoginId").val();
				if(lid!=null&lid!=""){
					//$("#friendadd").load("../friend/add",{"lid":lid});
					$("#prompt").empty();
					$.ajax({
			            type: "get",
            			url: "../friend/loadadd",
            			data:{"lid":lid},
            			success:function(m){
            				$("#friendadd").html(m);
            					$("#req").click(function(){
            						var adds = $("#addiv>button").attr("id").split("f");	//用"f"分割<button id="af..">的id
            						var req = $("#reqinfo").val();
	            					 obj = {
	            						frontindex : 3,
	            						msgToUserId :adds[1],
	            						msgFromUserId : userId,
	            						fromnickname : userNickName,
	            						msgContent : req,
	            						msgTypeId : 3
	            					};
	            					var addmsg=JSON.stringify(obj);
	            					ws.send(addmsg);   			
            					});
                			
            			}
		            });	
		           };				
			});
			
			$("#send").click(function(){
			
				$("pre").each(function(){//通过each来遍历
			    if($(this).css("display")=='block'){ //通过$(this).css("css名") 来获取当前遍历元素的display值
			    	if($(this).attr("id").indexOf("tent")!=-1){
			    		var ts = $(this).attr("id").split("tent");
						to=ts[1];
						
						var obj = {
							frontindex : 2,
							msgContent : $("#msg").val(),
							msgTypeId : 1,
							msgFromUserId : userId,
							msgToUserId : to, //
						};
						if (to != null) {
							var msgContent = JSON.stringify(obj); //从一个对象解析出字符串
							ws.send(msgContent);
							var date = new Date();
							var now = date.toLocaleTimeString();
							$("#content" + to).append("<div>当前用户：" + userNickName + "，当前时间：" + now + "<br/>消息内容：" + obj.msgContent + "</div>"); //自己发送的消息显示
							$("#msg").val("");
						}
						
			    	}else if($(this).attr("id").indexOf("cg")!=-1){
			    		var tg = $(this).attr("id").split("g");
						tog=tg[1];		//取得用户点击的群Id(已打开聊天区对应的群Id)
						
						var date = new Date();
						var now = date.toLocaleTimeString();
						var obj = {
							frontindex : 6,
							msgContent : $("#msg").val(),
							//msgTime : now,
							getmsgid : tog,
							sendUserId : userId,
							readedUserId : userId,
							sendName : userNickName
						};
						if (tog != null) {
							var groupmsgContent = JSON.stringify(obj); //从一个对象解析出字符串
							ws.send(groupmsgContent);
							$("#cg" + tog).append("<div>当前用户：" + userNickName + "，当前时间：" + now + "<br/>消息内容：" + obj.msgContent + "</div>"); //自己发送的消息显示
							$("#msg").val("");
						}
			    	}
			    }
			});
			
			
			});		
	});
	function closed() {
		//console.log(e);
		ws.close(); //关闭TCP连接
		location = "../jsp/login.jsp";
	};
	//var to ;
	function friclick(to){
		if ($("#content" + to).length <= 0) {
			$("#c").append("<pre id='content"+to+"' class='contact'></pre>");
		}
		$("pre").hide();	//	注意群聊的聊天区和单聊聊天区的显示是否有问题
		$(".contact").each(function(index, item) {
			if ($(this).attr("id") == "content" + to) {
				$("#content" + to).show();
			}
		});
		var obj = {
			frontindex : 1,
			msgContent : null,
			msgTypeId : 1,	//	测试消息			待修改
			msgFromUserId : userId,
			msgToUserId : to, 	//
		};
		var msgContent = JSON.stringify(obj); //从一个对象解析出字符串
		if($("#"+to+">span").length!=0){	//判断是否有未读消息
			$($("#"+to).find("span").remove());
		} 
		ws.send(msgContent);
	}
	
	
	function groclick(tog){
		if ($("#cg" + tog).length <= 0) {
			$("#c").append("<pre id='cg"+tog+"' class='contactg'></pre>");
		}
		$("pre").hide();	//如果群聊聊天区不是“pre”，则需要考虑是否有样式问题（还有id问题）
		$(".gmembers").remove();	//先把所有群用户面板移除，以防止重叠
		$(".contactg").each(function(index, item) {
			if ($(this).attr("id") == "cg" + tog) {
				$("#cg" + tog).show();
				$("#cg" + tog).append("<div id='gms"+tog+"' class='gmembers'></div>");
				//$("#gms"+ tog).show();
			}
		});
		var groupMemberMap = '${groupMemberMap}';	//得到群id和群用户的map
		eval("var result = " + groupMemberMap);//map里的键是gid，值是List集合
		for(i in result){	//循环遍历map集合中的user对象并根据需求取值
			if(i == tog){
				for(var j =0;j<result[i].length;j++){
						$("#gms"+tog).append("<center><button id='gm"+result[i][j].userId+"' class='btn btn-info btn-small'>"+result[i][j].userNickName+"</button></br></center>");					
				} 
			}
		}
		if($("#gms"+tog).find("center").length == 0){			//需考虑到新建群的时候（此时群内可能无成员，需新建成功时马上将群主加入右下方的群成员列表中）
			$.ajax({
				type: "get",
            	url: "../group/newmember",
            	data:{"gid":tog},
            	success:function(result){
            		eval("var map = " + result);
            		for(m in map){
	            		$("#gms"+tog).append("<center><button id='gm"+m+"' class='btn btn-info btn-small'>"+map[m]+"</button></br></center>");
	            		
            		}
            	}
			});
		}
		var obj = {
			frontindex : 5,
			getmsgid : tog,
			sendUserId : userId
		};
		var gmsgContent = JSON.stringify(obj); //从一个对象解析出字符串
		if($("#g"+tog+">span").length!=0){	//判断是否有未读消息
			$($("#g"+tog).find("span").remove());
		} 
		
		ws.send(gmsgContent);
	}
	
</script>
<script type="text/javascript">
$(function(){
	$("#gsearch").click(function() {
				var gid=$("#addgroupId").val();
				if(gid!=null&gid!=""){
					//$("#friendadd").load("../friend/add",{"lid":lid});
					$("#prompt").empty();
					$.ajax({
			            type: "get",
            			url: "../group/loadadd",
            			data:{"gid":gid},
            			success:function(m){
            				$("#groupadd").html(m);
            					$("#req").click(function(){
            						var adds = $("#addiv>button").attr("id").split("g");	//用"f"分割<button id="ag..">的id
            						var req = $("#reqinfo").val();
	            					 obj = {
	            						frontindex : 7,		//请求发送方
	            						getmsgid :adds[1],
	            						sendUserId : userId,
	            						fromnickname : userNickName,
	            						msgContent : req,
	            						msgType : 3		//入群请求（待修改）
	            					};
	            					var addgroupmsg=JSON.stringify(obj);
	            					ws.send(addgroupmsg);   			
            					});
                			
            			}
		            });	
		           };				
			});
			//界面处理和表单验证
			$(".userInfo").hide();
			$("#head").mouseover(function(){
				$(".userInfo").show();
			});
			$("#head").mouseout(function(){
				$(".userInfo").hide();
			});
			$("#chatmenu").height($("#main").height());
			
			
});
</script>
</head>

<body>
	<div class="container">
		<!-- <div style="position:absolute;width:100%;height:100%;z-index:-1;left:0;top:0;">
			<img alt="" src="../images/bg.jpg" height="100%" width="100%">
		</div> -->
		<div id="menu" class="row">
			<div class="col-md-12">
				<h1 class="text-center">
					${u.userNickName }'s Contact<small> (test)</small>
					<button style="float: right" class="btn btn-warning"
						onclick="closed();">Exit</button>
					<button class="btn btn-primary btn-lg btn-block" type="button">
						<span class="glyphicon glyphicon-home"></span>
					</button>
				</h1>
			</div>
		</div>
		<div class="row">
			<div class="col-md-3" id="chatmenu">
				<div id="head">
					<img alt="140x140" src="../images/1U45430O-1.jpg" width="100%"/>
					<div class="myinfo">
						<div class="userInfo">
							
							<form action="">
							   <center><h1>用户个人信息</h1></center>
							   <div id="div2" class="div2">
							   <div id="imgPreview2"> <img class="img-circle" alt="140x140"  src="../images/1U45430O-1.jpg" width="120px" height="120px"></div>
								   <input type="file" name="userHeadPortrait" id="pic" onchange="PreviewImage(this)" accept="image/gif, image/jpeg, image/jpg" /><br/>
							       	昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：&nbsp;&nbsp;<input type="text" name="userNickName" value="${u.userNickName}" placeholder="昵称">  <br/>
					              	 <%-- 密码：&nbsp;&nbsp;<input type="text" name="userPassWord" value="${u.}" placeholder="密码"><br/> --%>
								  	 个性签名：<input type="textarea" name="userSignature" value="${u.userSignature}" placeholder="我是一只大花猫"><br/>
								   	性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别：&nbsp;&nbsp;<input type="text" name="sex" value="${u.userSex}" placeholder="男或女"><br/>
			                      	 生&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日：&nbsp;&nbsp;<input type="date" name="userBirthday" value="${u.userBirthday}" /><br/>
									 <button type="submit" name="change" id="changeInfo">修改</button>
							   </div>
						   </form>						   							
						</div>
					</div>
				</div>
				<div>
					<div class="row"><!-- height:522px -->
						<!-- 选项卡组件（菜单项nav-tabs）-->
						<ul id="myTab" class="nav nav-tabs" role="tablist">
							<li class="active"><a href="#contacts" role="tab" data-toggle="tab">联系人</a>
							</li>
							<li><a href="#groups" role="tab" data-toggle="tab">群组</a>
							</li>
							<li><a href="#addfriend" role="tab" data-toggle="tab">好友添加</a>
							</li>
							<li><a href="#addgroup" role="tab" data-toggle="tab">群添加</a>
							</li>
						</ul>
						<!-- 选项卡面板 -->
						<div id="myTabContent" class="tab-content">
							<div class="tab-pane active" id="contacts"><!-- 联系人 -->
								<c:forEach items="${f}" var="fu" varStatus="vs">
									<button id="${fu.userId}" type="button"
										class="btn btn-info btn-block" onclick="friclick(${fu.userId});">${fu.userNickName}</button>
								</c:forEach>
							</div>
							<div class="tab-pane" id="groups"><!-- 群组 -->
								<c:forEach items="${groups}" var="g" varStatus="vs">
									<button id="g${g.gid}" type="button" class="btn btn-info btn-block" onclick="groclick(${g.gid});">${g.gname}</button>
									<%-- <a href='#' id='out' class='btn' rel='popover' title='Using Popover' data-content='Just add content to the data-content attribute.' onclick="gclick(${g.gid})">Click Me!</a> --%>
								</c:forEach>
							</div>
							<div class="tab-pane" id="addfriend"><!-- 好友添加 -->
							<form role="form" class="form-inline">
							  <div class="form-group ">
							    <input type="text" id="addfriendLoginId" class="form-control" placeholder="Enter UserLoginId" style="width:205px"><!-- 325 -->
								<button id="search" class="btn btn-primary" type="button" style="float:right">Search</button>
							  </div>
							  <div id="friendadd">
							  
							  </div>
							  	<div id="friendreq">
							  	
							  	</div>
							  	<div id="prompt" class="alert alert-success">
							  	
							  	</div>
							</form>   
							</div>
							<div class="tab-pane" id="addgroup"><!-- 群添加 -->
								<!-- <button type="button" class="btn btn-primary" data-loading-text="Loading...">TRY BA!</button> -->
								<form role="form" class="form-inline">
							    <div class="form-group " style="width:100%">
							      <input type="text" id="addgroupId" class="form-control" placeholder="Enter groupId" style="width:100%"><!-- 325 -->
								  <button id="gsearch" class="btn btn-primary btn-block" type="button" style="float:right">Search</button>
							    </div>
							    <div id="groupadd">
							  
							    </div>
							  	<div id="groupreq">
							  	
							  	</div>
							  	<div id="groupprompt" class="alert alert-success">
							  	
							  	</div>
							</form>
							</div>
						</div>
						<!-- <div class="col-md-4">
							<button type="button" class="btn btn-primary btn-block"><span class="glyphicon glyphicon-font"></span></button>
						</div>
						<div class="col-md-4">
							<button type="button" class="btn btn-primary btn-block"><span class="glyphicon glyphicon-bold"></span></button>
						</div>
						<div class="col-md-4">
							<button type="button" class="btn btn-primary btn-block"><span class="glyphicon glyphicon-italic"></span></button>
						</div> -->
					</div>
				</div>
			</div>
			<div class="col-md-9" id="main">
				聊天区
				<div id="c" style="height:500px">
					<!-- <pre id="test" style="height:500px;"></pre> -->
					
				</div>
				<br />
				<center>
					<textarea id="msg" class="form-control" style="width:90%" rows="8"></textarea>
				</center>
				<br /> <a id="send" type="button" class="btn btn-info btn-block">Send</a>
				<br /> <br />
			</div>
		</div>
		
		<!-- <div id="slidershow" class="carousel slide" data-pause="hover" data-ride="carousel" data-wrap="true" data-interval="5000">
		 设置图片轮播的顺序
		 <ol class="carousel-indicators">
			 <li class="active" data-target="#slidershow" data-slide-to="0"></li>
			 <li data-target="#slidershow" data-slide-to="1"></li>
			 <li data-target="#slidershow" data-slide-to="2"></li>
			 <li data-target="#slidershow" data-slide-to="3"></li>
		 </ol>
		 设置背景轮播图片
		 <div class="carousel-inner">
		 	<div class="item active">
				 <a href="##"><img class="imag" src="/Chatting_new_9_6_ssm/images/bg.jpg"></a>
				 <div class="carousel-caption">
					 <h3>图片标题1</h3>
					 <p>描述内容1...</p>
				 </div>
			 </div>
			 <div class="item">
				 <a href="##"><img class="imag" src="/Chatting_new_9_6_ssm/images/register.jpg"></a>
				 <div class="carousel-caption">
					 <h3>图片标题1</h3>
					 <p>描述内容1...</p>
				 </div>
			 </div>
			 <div class="item">
				 <a href="##"><img class="imag" src="/Chatting_new_9_6_ssm/images/login1.jpg"></a>
				 <div class="carousel-caption">
					 <h3>图片标题2</h3>
					 <p>描述内容2...</p>
				 </div>
			 </div>
			 <div class="item">
				 <a href="##"><img class="imag" src="/Chatting_new_9_6_ssm/images/login2.jpg"></a>
				 <div class="carousel-caption">
					 <h3>图片标题3</h3>
					 <p>描述内容3...</p>
				 </div>
			 </div>
		 </div>
		 设置轮播图片控制器
		 <a class="left carousel-control" href="#slidershow" role="button" data-slide="prev">
		 	<span class="glyphicon glyphicon-chevron-left"></span>
		 </a>
		 <a class="right carousel-control" href="#slidershow" role="button" data-slide="next">
		 	<span class="glyphicon glyphicon-chevron-right"></span>
		 </a>
	</div> -->
	</div>
	<script type="text/javascript" src="../js/jquery-2.1.4.js"></script>
	<script type="text/javascript" src="../js/bootstrap.js"></script>
	<script type="text/javascript" src="../js/jquery.validate-1.13.1.js"></script>
</body>
</html>
