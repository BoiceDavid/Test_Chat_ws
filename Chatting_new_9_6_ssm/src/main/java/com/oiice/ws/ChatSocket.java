package com.oiice.ws;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import net.sf.json.JSONObject;

import org.springframework.web.context.ContextLoader;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.oiice.dao.UserMapper;
import com.oiice.service.ChatService;
import com.oiice.service.FriendsService;
import com.oiice.service.GroupService;
import com.oiice.service.GroupmsgService;
import com.oiice.service.UserService;
import com.oiice.vo.Friends;
import com.oiice.vo.Group;
import com.oiice.vo.Groupmsg;
import com.oiice.vo.Messages;
import com.oiice.vo.User;

//@EnableWebMvc	启用对springmvc的支持
@ServerEndpoint("/chat")	//可在连接时指定服务端地址
public class ChatSocket implements Serializable{
	//private static Gson gson=new Gson();
	public User myuser = null;
	private static List<Session> sessions = new ArrayList<Session>();
	private static Map<Integer, Session> map = new HashMap<Integer, Session>(); //用于将 userId 和 session 对应起来,存入上线用户
	@OnOpen
	public void open(Session session) {
		String queryString = session.getQueryString();//getQueryString这里是拿到"?"后面的所有内容
		String userId = queryString.substring(queryString.indexOf("=")+1);
		UserService userService = (UserService) ContextLoader.getCurrentWebApplicationContext().getBean("userService");
		Integer uid = Integer.valueOf(userId);
		myuser = userService.getUserById(uid);
		map.put(uid, session);
		
		ChatService chatService = (ChatService)ContextLoader.getCurrentWebApplicationContext().getBean("chatService");
		List<Messages> mList = chatService.getUnreadByToUserId(uid);	//所有未读消息
		List<Integer> fromIds = new ArrayList<Integer>();		//用于放置消息发送者的Id
		List<Messages> frireqs = new ArrayList<Messages>();		//好友请求信息
		for (Messages m : mList) {
			if(m.getMsgTypeId()!=3){		//不查询msgTypeId为3的信息
			Integer fId = m.getMsgFromUserId();
			fromIds.add(fId);}else{
				frireqs.add(m);		//
			}
		}
		//Set<Integer> froms = new HashSet<Integer>(fromIds);		//将List转换为Set
		for (Integer fromId : fromIds) {	
			Integer row=Collections.frequency(fromIds, fromId);	//查询集合中每个元素相等的次数（即本用户来自每个用户的未读消息条数）
			try {
				JSONObject jsonObject = new JSONObject();
				//jsonObject.put("backindex", 1);		
				jsonObject.put("from",fromId);
				jsonObject.put("row",row);
				jsonObject.put("msgFromUserId", 0);
				jsonObject.put("type", 1);
				//System.out.println(jsonObject.toString());
				session.getBasicRemote().sendText(jsonObject.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for (Messages frireq : frireqs) {
			if (frireq.getMsgToUserId().equals(uid)) {	//表明好友请求发给本用户的
				try {
					JSONObject jsonObject =JSONObject.fromObject(frireq);
					Integer reqfrom = frireq.getMsgFromUserId();
					String fromname = userService.getUserById(reqfrom).getUserNickName();//请求方的昵称
					jsonObject.put("fromnickname", fromname);
					jsonObject.put("type", 1);
					session.getBasicRemote().sendText(jsonObject.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String gidString = myuser.getUserGids();
		if(gidString!=null){
			String[] gids = gidString.split(",");
			GroupService groupService = (GroupService)ContextLoader.getCurrentWebApplicationContext().getBean("groupService");
			for (String string : gids) {
				Integer gid = Integer.valueOf(string);
				Group group = groupService.getGroupandmsgById(gid);
				List<Groupmsg> groupmsgs = group.getGroupmsgs();
				int row=0;
				for (Groupmsg groupmsg : groupmsgs) {
					if (groupmsg.getReadedUserId().indexOf(userId)==-1) {
						row++;
					}
				}
				
				try {
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("row", row);
					jsonObject.put("getmsgid", gid);
					jsonObject.put("type", 0);
					jsonObject.put("gtype", "-1");
					session.getBasicRemote().sendText(jsonObject.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	Integer i=0;
	@OnMessage
	public void message(Session session,String ms) {
		
		JSONObject jsonObject = new JSONObject();
		JSONObject msgObj = JSONObject.fromObject(ms);		//json字符串--->jsonObject类型
		Integer frontindex = (Integer) msgObj.get("frontindex");
		msgObj.remove("frontindex");
		//String msg = msgObj.toString();				//jsonObject类型--->json字符串
		
		Messages messages = new Messages();
		Groupmsg groupmsg = new Groupmsg();
		
		ChatService chatService = (ChatService)ContextLoader.getCurrentWebApplicationContext().getBean("chatService");
		GroupService groupService = (GroupService)ContextLoader.getCurrentWebApplicationContext().getBean("groupService");
		
		if (frontindex==1) {
			messages = (Messages) JSONObject.toBean(msgObj, Messages.class);
			//messages = gson.fromJson(msg, Messages.class);			
			Integer fromId = messages.getMsgFromUserId();//此时用户已选中一个好友，fromId表示本用户
			List<Messages> mList = chatService.getUnreadByToUserId(fromId);//本用户的未读消息
			Integer toId = messages.getMsgToUserId();//此时用户选中的好友Id
			for (Messages m : mList) {
				Integer fromUserId = m.getMsgFromUserId();
				String acptime = m.getMsgTime().toString(); //将数据库中的发送时间转为字符串
				if (fromUserId.equals(toId)) {		//Messages表中的消息发送方即用户选中的好友
					try {
						
						//String mString = JSON.toJSONString(m);
						jsonObject = JSONObject.fromObject(m);
						jsonObject.put("type", 1);
						jsonObject.put("clickId", toId);
						jsonObject.put("acptime", acptime);
						session.getBasicRemote().sendText(jsonObject.toString());
						
						//session.getBasicRemote().sendText(gson.toJson(m));
						m.setMsgStatus(1);
						chatService.changeMsgStatus(m);//改变消息状态为已读**
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else if (frontindex==2){
			//messages = gson.fromJson(msg, Messages.class);
			messages = (Messages) JSONObject.toBean(msgObj, Messages.class);
			
			Integer flag = chatService.addSendMsg(messages);	//将消息内容添加到数据库中
			Integer msgId = messages.getMsgId();
			messages = chatService.getMessagesById(msgId);
			Integer toId =  messages.getMsgToUserId();
				Session to_session = this.map.get(toId);
				if (to_session!=null) {
						try {
							
							Integer fromId = messages.getMsgFromUserId();	//本用户Id
							Messages messages2 = new Messages();
							messages2.setMsgFromUserId(fromId);
							messages2.setMsgToUserId(toId);
							List<Messages> mList = chatService.getUnread(messages);	//根据发送方和接收方查询指定未读信息的条数
							Integer row =  mList.size();
							
							String acptime = messages.getMsgTime().toString(); //将数据库中的发送时间转为字符串
							jsonObject = JSONObject.fromObject(messages);
							jsonObject.put("from",fromId);
							jsonObject.put("row",row);
							jsonObject.put("type", 1);
							jsonObject.put("acptime", acptime);
							//jsonObject.put("backindex", 1);							
							to_session.getBasicRemote().sendText(jsonObject.toString());
							//to_session.getBasicRemote().sendText(gson.toJson(messages));
						} catch (IOException e) {
							e.printStackTrace();
						}
				}			
		}else if(frontindex==3){
			//JSONObject addObj = JSONObject.parseObject(msg);		//json字符串--->jsonObject类型
			String nickname = (String) msgObj.get("fromnickname");
			msgObj.remove("fromnickname");
			//String fm = msgObj.toString();				//jsonObject类型--->json字符串
			//messages = gson.fromJson(fm, Messages.class);
			messages = (Messages) JSONObject.toBean(msgObj, Messages.class);
			Friends friends = (Friends)ContextLoader.getCurrentWebApplicationContext().getBean("friends");
			friends.setMyUserId(messages.getMsgToUserId());
			friends.setFriendUserId(messages.getMsgFromUserId());
			FriendsService friendsService = (FriendsService)ContextLoader.getCurrentWebApplicationContext().getBean("friendsService");
			if(friendsService.foundMyFriend(friends)<=0){//判断是否已经是好友	还需要处理
				if(friendsService.getUnreadReq(messages) == null){		//注意此处判断	关于好友请求
					Integer flag = chatService.addSendMsg(messages);	//将消息内容添加到数据库中
					/*Integer msgId = messages.getMsgId();
					messages = chatService.getMessagesById(msgId);*/
					Integer toId =  messages.getMsgToUserId();		
					//Integer toId = messages.getToUserId();
						Session to_session = this.map.get(toId);
						if (to_session!=null) {
								try {
									jsonObject=JSONObject.fromObject(messages);
									jsonObject.put("fromnickname", nickname);
									jsonObject.put("type", 1);
									System.out.println("请求者："+nickname);
									//添加申请者的nickname
									to_session.getBasicRemote().sendText(jsonObject.toString());
								} catch (IOException e) {
									e.printStackTrace();
								}
						}
					}
				}			
		}else if(frontindex==4){
			FriendsService friendsService = (FriendsService)ContextLoader.getCurrentWebApplicationContext().getBean("friendsService");
			String nickname = (String) msgObj.get("fromnickname");
			msgObj.remove("fromnickname");
			messages = (Messages) JSONObject.toBean(msgObj, Messages.class);
			//从数据库中查找对应的messages
			Messages mm = friendsService.getUnreadReq(messages);		//关于好友请求消息状态的改变
			if (mm!=null) {
				mm.setMsgStatus(1);
				chatService.changeMsgStatus(mm);				
			}
			Integer toId =  messages.getMsgFromUserId();	//响应给请求者
			Session to_session=this.map.get(toId);
			if (messages.getMsgContent().equals("1")) {
				Friends friends = (Friends)ContextLoader.getCurrentWebApplicationContext().getBean("friends");
				friends.setMyUserId(messages.getMsgFromUserId());
				friends.setFriendUserId(messages.getMsgToUserId());
				friends.setTypeId(1);			//双方好友关联		此处应该用事务
				if (friendsService.foundMyFriend(friends)<=0) {
					friendsService.addFriend(friends);
					friends.setMyUserId(messages.getMsgToUserId());
					friends.setFriendUserId(messages.getMsgFromUserId());
					friends.setTypeId(1);
					friendsService.addFriend(friends);					
				}
				if (to_session!=null) {
						try {
							jsonObject=JSONObject.fromObject(messages);
							jsonObject.put("fromnickname", nickname);	//添加申请者的nickname
							jsonObject.put("resp", 1);		//表示接受申请
							jsonObject.put("type", 1);
							to_session.getBasicRemote().sendText(jsonObject.toString());
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}else if(messages.getMsgContent().equals("2")){
				if (to_session!=null) {
						try {
							jsonObject=JSONObject.fromObject(messages);
							jsonObject.put("fromnickname", nickname);	//添加申请者的nickname
							jsonObject.put("resp", 0);	//表示拒绝申请
							jsonObject.put("type", 1);
							to_session.getBasicRemote().sendText(jsonObject.toString());
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		}else if (frontindex==5) {	//用户未读群消息接收
			Integer gid = msgObj.getInt("getmsgid");		//用户点击的群Id
			String myIdString = (String)msgObj.get("sendUserId");
			Group group = groupService.getGroupandmsgById(gid);		//根据gid得到group（关联查询）
			List<Groupmsg> groupmsgs = group.getGroupmsgs();	//得到此群的所有消息	
			List<Groupmsg> unReadGroupmsgs=new ArrayList<Groupmsg>();	//未读消息集合
			for (Groupmsg gm : groupmsgs) {
				String readed = gm.getReadedUserId();	//得到Groupmsg中的已读用户Id
				if (readed.indexOf(myIdString)==-1) {	//已读用户字段与用户Id匹配，结果为-1时表示用户未读此条信息
					unReadGroupmsgs.add(gm);
				}				
			}
			UserService userService = (UserService)ContextLoader.getCurrentWebApplicationContext().getBean("userService");
			GroupmsgService groupmsgService = (GroupmsgService)ContextLoader.getCurrentWebApplicationContext().getBean("groupmsgService");
			try {
				for (Groupmsg unreadGM : unReadGroupmsgs) {
					User user = userService.getUserById(unreadGM.getSendUserId());
					String sendName = user.getUserNickName();
					String acptime = unreadGM.getMsgTime().toString(); //将数据库中的发送时间转为字符串
					jsonObject = JSONObject.fromObject(unreadGM);
					jsonObject.put("type", 0);
					jsonObject.put("gtype", 0);
					jsonObject.put("aj", 1);
					jsonObject.put("sendName", sendName);
					jsonObject.put("acptime", acptime);
					/*String readed = unreadGM.getReadedUserId();
					if (readed.indexOf(myIdString)==-1) {
						unreadGM.setReadedUserId(readed+","+myIdString);						
					}
					groupmsgService.changeGMReaded(unreadGM);		//更新已读用户Id字段
*/					//jsonObject.put("gclick", gid);	//防止前台“tog”为undefined
					//System.out.println(jsonObject.toString());
					session.getBasicRemote().sendText(jsonObject.toString());
				}					
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if (frontindex==6) {	//群聊消息 
			String sendName = (String)msgObj.get("sendName");
			msgObj.remove("sendName");
			GroupmsgService groupmsgService = (GroupmsgService)ContextLoader.getCurrentWebApplicationContext().getBean("groupmsgService");
			
			Timestamp now = new Timestamp(System.currentTimeMillis());//Timestamp类型的当前时间,因为json封装对象时timestamp转换异常
			//now.put("msgTime", now);
			
			Groupmsg gm = (Groupmsg)JSONObject.toBean(msgObj,Groupmsg.class);
			gm.setMsgTime(now);
			
			Integer gmId = groupmsgService.addGroupmsg(gm);	//将发送的信息添加到数据库
			System.out.println("插入群消息。"+gmId);
			Integer agid = gm.getGetmsgid();//拿到接收消息的群Id
			
			Group group = groupService.getGroupandmsgById(agid);	//关联查询
			List<Groupmsg> groupmsgs = group.getGroupmsgs();//[]
			
			String mString = group.getMembers();
			String[] members = mString.split(",");
			try {
				for (String m : members) {										
					Integer mid = Integer.valueOf(m);
					if (!mid.equals(gm.getSendUserId())) {		//判断不对
						List<Groupmsg> unReadGroupmsgs=new ArrayList<Groupmsg>();	//未读消息集合
						for (Groupmsg gmsg : groupmsgs) {
							String readed = gmsg.getReadedUserId();	//得到Groupmsg中的已读用户Id
							if (readed.indexOf(m)==-1) {	//已读用户字段与用户Id匹配，结果为-1时表示用户未读此条信息
								unReadGroupmsgs.add(gmsg);
							}				
						}
						
						msgObj.put("row", unReadGroupmsgs.size());
						String acptime = gm.getMsgTime().toString(); //将数据库中的发送时间转为字符串
						msgObj.put("acptime", acptime);
						msgObj.put("type", 0);
						msgObj.put("gtype", 0);	
						msgObj.put("aj", 1);
						msgObj.put("sendName", sendName);
						msgObj.put("gmId", gmId);
						
						Session to_session = this.map.get(mid);
						if (to_session!=null) {
							to_session.getBasicRemote().sendText(msgObj.toString());						
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//User user = userService.getUserById(gm.getSendUserId());	//根据用户的Id得到User对象
			
			
		}else if(frontindex==7){//入群申请
			Integer myId = msgObj.getInt("sendUserId");
			String fromnickname = (String)msgObj.get("fromnickname");
			msgObj.remove("fromnickname");
			
			Integer gid = Integer.valueOf((String)msgObj.get("getmsgid"));
			UserService userService = (UserService)ContextLoader.getCurrentWebApplicationContext().getBean("userService");
			User user = userService.getUserById(myId);
			String userGids = user.getUserGids();
			Integer createuid=null;	//群主Id
			if (userGids.indexOf(Integer.toString(gid))==-1) {		//防止重复添加群成员						
				Group group = groupService.getGroupById(gid);
				String memberString = group.getMembers();
				if (memberString.length()>1) {				
					String[] members = memberString.split(",");
					createuid = Integer.valueOf(members[0]);	//取群成员字符串的第一个成员作为群主
					String temp = memberString.substring(2, memberString.length());
					System.out.println("temp:"+temp);
					msgObj.put("readedUserId", temp+","+myId);	//将此字段存入数据库是为了处理未读消息，只有群主需要收到此消息，其他成员都默认已读
					
				}else {
					createuid = Integer.valueOf(memberString);
					msgObj.put("readedUserId", memberString);
				}
				Groupmsg gmsg = (Groupmsg)JSONObject.toBean(msgObj, Groupmsg.class);
				Timestamp now = new Timestamp(System.currentTimeMillis());//Timestamp类型的当前时间,因为json封装对象时timestamp转换异常
				gmsg.setMsgTime(now);
				GroupmsgService groupmsgService = (GroupmsgService)ContextLoader.getCurrentWebApplicationContext().getBean("groupmsgService");
				Integer amId = groupmsgService.addGroupmsg(gmsg);		//将这条入群请求添加到数据库中
				System.out.println("添加成功的群消息Id"+amId);
				msgObj.put("type", 0);
				msgObj.put("gtype", 2);
				msgObj.put("fromnickname", fromnickname);
				msgObj.put("gmId", amId);
	
				Session toSession = this.map.get(createuid);
				try {
					if(toSession!=null){
						toSession.getBasicRemote().sendText(msgObj.toString());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else if (frontindex==8) {
			String fromnickname = msgObj.getString("fromnickname");
//			msgObj.remove("fromnickname");
			Integer getmsgid = msgObj.getInt("getmsgid");	//响应的群Id
			Group group = groupService.getGroupById(getmsgid);	//响应的群
			String gmembers = group.getMembers();	//取得群成员
			String createId = msgObj.getString("fromId");	//群主Id
			Integer gmId = msgObj.getInt("gmId");	//群消息Id
			Integer toId = msgObj.getInt("toId");	//响应给谁
			Session toSession = this.map.get(toId);
			GroupmsgService groupmsgService = (GroupmsgService)ContextLoader.getCurrentWebApplicationContext().getBean("groupmsgService");
			if (msgObj.get("msgContent").equals("1")&&toSession!=null) {
				groupmsg = groupmsgService.getGroupmsgById(gmId);//修改此条群消息中的已读用户字段
				String readed = groupmsg.getReadedUserId();
				if(readed.indexOf(createId)==-1){
					groupmsg.setReadedUserId(readed+","+createId+","+toId);	//将群主Id加入已读用户字段中
				}else {
					groupmsg.setReadedUserId(readed+","+toId);	//将群主Id加入已读用户字段中
				}
				groupmsgService.changeGMReaded(groupmsg);
				
				group.setMembers(gmembers+","+toId);
				groupService.changGroupmember(group);//将用户添加到群成员中
				UserService userService = (UserService)ContextLoader.getCurrentWebApplicationContext().getBean("userService");
				User user = userService.getUserById(toId);
				String userGids = user.getUserGids();
				user.setUserGids(userGids+","+getmsgid);
				userService.changeUserGidsById(user);//修改用户的userGids字段
				
				msgObj.put("gresp", "1");
				msgObj.put("type", "0");
				msgObj.put("gtype", "2");
				msgObj.put("gname", group.getGname());
				msgObj.put("getmsgid", getmsgid);
				msgObj.put("sendUserId", toId);
				//msgObj.put("map", memberMap);
				try {
					toSession.getBasicRemote().sendText(msgObj.toString());
					System.out.println("同意");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				User newmember = userService.getUserById(toId);		
				String newname = newmember.getUserNickName();		//取到新成员的userid和name发给每个成员
				//Map<String, String> memberMap=new HashMap<String, String>();//拿到群成员id与name对应的集合
				String[] gmembersStrings = gmembers.split(",");
				try {
					for (String string : gmembersStrings) {
						Integer member = Integer.valueOf(string);
						jsonObject.put("newmember", toId);
						jsonObject.put("newname", newname);
						jsonObject.put("type", 0);
						jsonObject.put("gtype", "0");
						jsonObject.put("addnewmember", 1);
						jsonObject.put("getmsgid", getmsgid);
						//jsonObject.put("newtype", "0");
						Session toSession2 = this.map.get(member);
						if (toSession2!=null) {
							toSession2.getBasicRemote().sendText(jsonObject.toString());							
						}
					}					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}else if (msgObj.get("msgContent").equals("0")&&toSession!=null) {
				msgObj.put("gresp", "0");
				msgObj.put("type", "0");
				msgObj.put("gtype", "2");
				msgObj.put("gname", group.getGname());
				msgObj.put("getmsgid", getmsgid);
				try {
					toSession.getBasicRemote().sendText(msgObj.toString());
					System.out.println("拒绝");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	@SuppressWarnings("static-access")
	@OnClose
	public void close(Session session) {
		UserMapper userMapper = null;
		try {
			userMapper = ContextLoader.getCurrentWebApplicationContext().getBean(UserMapper.class);
			myuser.setUserStatus(0);
			if(this.map.containsKey(myuser.getUserId())){
				this.map.remove(myuser.getUserId());
			}
			userMapper.updateUstatusById(myuser);
			System.gc();
			System.out.println("gc test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}