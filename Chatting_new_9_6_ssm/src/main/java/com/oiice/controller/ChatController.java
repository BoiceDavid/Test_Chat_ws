package com.oiice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oiice.service.ChatService;
import com.oiice.service.GroupService;
import com.oiice.service.GroupmsgService;
import com.oiice.service.UserService;
import com.oiice.vo.Group;
import com.oiice.vo.Groupmsg;
import com.oiice.vo.User;

@Controller
@RequestMapping("/chat")
public class ChatController {
	//private static Gson gson=new Gson();
	@Autowired
	private UserService userService;
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private GroupService groupService;
	
	
	@Autowired
	private GroupmsgService groupmsgService;
	
	@Autowired 
	private Groupmsg groupmsg;
	
	
	@RequestMapping("/friends")
	public String loadFriends(@RequestParam("fromId")String fromIdString,@RequestParam("toId")String toIdString,Model model) {
		Integer fromId = Integer.valueOf(fromIdString);
		User from = userService.getUserById(fromId);
		Integer toId = Integer.valueOf(toIdString);
		User to = userService.getUserById(toId);
		model.addAttribute("fromuser",from);
		model.addAttribute("touser", to);
		return "mainchat";
	}
	
	@RequestMapping("/gmsgread")
	@ResponseBody
	synchronized
	public String receive(@RequestParam("getmsgid")String getmsgid,@RequestParam("myId")String myId,Model model) {
		System.out.println("ajax请求");
		Integer id = Integer.valueOf(getmsgid);
		//Integer sendid = Integer.valueOf(sendUserId);
		Group group = groupService.getGroupandmsgById(id);
		List<Groupmsg> groupmsgs = group.getGroupmsgs();
		for (Groupmsg groupmsg : groupmsgs) {
			String readed = groupmsg.getReadedUserId();
			if (readed.indexOf(myId)==-1) {
				groupmsg.setReadedUserId(readed + "," + myId);
				System.out.println("gmId:"+groupmsg.getGmId());
				System.out.println("readedId:"+groupmsg.getReadedUserId());
				groupmsgService.changeGMReaded(groupmsg);
			}			
		}
		//String[] reader = readed.split(",");
		//groupmsg.setReadedUserId()
		return "{1:1}";
	}
}
