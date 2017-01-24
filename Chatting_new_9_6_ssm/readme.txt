整合ssm 
	群聊未读消息有问题	12/30 9:56	已解决


client：	
	//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，
    // 防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        var is = confirm("确定关闭窗口？");
        if (is){
            websocket.close();
        }
    };
    
server：
	//configurator = SpringConfigurator.class是为了使该类可以通过Spring注入。
	@ServerEndpoint(value = "/websocket",configurator = SpringConfigurator.class)
	
12/31	
已在云服务器上部署，mysql数据库未部署，
现可访问http://www.oiice.com/tains-1/jsp/login.jsp

好友和群名 乱码（中文乱码） mysql数据库编码问题 已解决

手机上显示有问题（好友添加），需优化布局；

2017/1/4
群新成员添加，每个客户端需得到最新的群成员信息		此问题急需解决!!
备注，上传头像，建群，聊天记录，好友搜索（跳到好友面板）
