package com.oiice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oiice.service.ChatService;
import com.oiice.service.FriendsService;
import com.oiice.service.UserService;
import com.oiice.vo.Friends;
import com.oiice.vo.Messages;
import com.oiice.vo.User;

@Controller
@RequestMapping("/friend")
public class FriendsController {
	@Autowired
	private FriendsService friendsService;
	@Autowired
	private ChatService chatService;
	@Autowired
	private UserService userService;
	@Autowired
	Friends friends;
	@Autowired
	Messages messages;
	@RequestMapping("/loadadd")
	public String addFriend(@RequestParam("lid")String lid,Model model) {
		Integer addFriendLoginId = Integer.valueOf(lid);
		User user = userService.getUserByLoginId(addFriendLoginId);
		model.addAttribute("f", user);
		//查找要添加好友...
		return "addfriend";
	}
	//此处已被遗弃
/*	@RequestMapping(value="/add")
	public String add(@RequestParam("friendUserId")Integer friendUserId,@RequestParam("myUserId")Integer myUserId,@RequestParam("typeId")Integer typeId,Model model) {
		messages.setMsgfromuserid(friendUserId);	
		messages.setMsgtouserid(myUserId);
		messages.setMsgtypeid(3);
		messages.setMsgcontent("");
		messages.setMsgstatus(1);		//更新好友请求消息状态为已读
		chatService.changeMsgStatus(messages);
		if (typeId==1) {
			friends.setMyUserId(friendUserId);
			friends.setFriendUserId(myUserId);
			friends.setTypeId(typeId);
			friendsService.addFriend(friends);
			friends.setMyUserId(myUserId);
			friends.setFriendUserId(friendUserId);
			friends.setTypeId(typeId);
			friendsService.addFriend(friends);		//双方的好友关系添加
			//System.out.println("success");
			User friend = userService.getUserById(friendUserId);
			model.addAttribute("f", friend);
		}
		//添加好友...
		return "addFriendSuccess";
	}*/
}
