package com.oiice.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.oiice.service.GroupService;
import com.oiice.service.UserService;
import com.oiice.vo.Group;
import com.oiice.vo.User;

@Controller
@RequestMapping("/group")
public class GroupController {
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Group group;
	
	@Autowired
	private User user;
	
	@RequestMapping("/loadadd")
	public String addGroup(@RequestParam("gid")String gid,Model model) {
		Integer addGroupId = Integer.valueOf(gid);
		Group group = groupService.getGroupById(addGroupId);
		//Integer g = groupService.changGroupmember(group);
		model.addAttribute("g", group);
		//查找要添加好友...
		return "addgroup";
	}
	
	@RequestMapping("/newmember")
	public @ResponseBody String newmember(@RequestParam("gid")String gid,Model model) {
		Integer addGroupId = Integer.valueOf(gid);
		Group group = groupService.getGroupById(addGroupId);
		//Integer g = groupService.changGroupmember(group);
		String gmembers = group.getMembers();
		Map<String, String> memberMap=new HashMap<String, String>();//拿到群成员id与name对应的集合
		String[] gmembersStrings = gmembers.split(",");
		for (String string : gmembersStrings) {
			Integer member = Integer.valueOf(string);
			User user2 = userService.getUserById(member);
			Integer uid = user2.getUserId();
			String uname = user2.getUserNickName();
			memberMap.put(Integer.toString(uid), uname);
		}
		//model.addAttribute(memberMap);
		return com.alibaba.fastjson.JSONArray.toJSONString(memberMap);
	}
	
	@RequestMapping(value="/newadd")
	public String newmember1(@RequestParam("gid")String gid,Model model){
		System.out.println("intoto~~~");
		Integer addGroupId = Integer.valueOf(gid);
		Group group = groupService.getGroupById(addGroupId);
		//Integer g = groupService.changGroupmember(group);
		String gmembers = group.getMembers();
		Map<String, String> memberMap=new HashMap<String, String>();//拿到群成员id与name对应的集合
		String[] gmembersStrings = gmembers.split(",");
		for (String string : gmembersStrings) {
			Integer member = Integer.valueOf(string);
			User user2 = userService.getUserById(member);
			Integer uid = user2.getUserId();
			String uname = user2.getUserNickName();
			memberMap.put(Integer.toString(uid), uname);
		}
		model.addAttribute("groupMemberMap",memberMap);
//		request.getSession().setAttribute("groupMemberMap", memberMap);

		return "chat";
	}
	
	@ResponseBody
	@RequestMapping(value="/create")
	public Map<String, Group> create(@RequestParam("creategname")String creategname,@RequestParam("uid")String uid){
		if (groupService.getGroupByName(creategname)==null) {		
			group.setGname(creategname);
			group.setMembers(uid);
			//System.out.println(uid);
			Timestamp now = new Timestamp(System.currentTimeMillis());//Timestamp类型的当前时间,因为json封装对象时timestamp转换异常
			group.setCreatetime(now);
			//Group表中插入新组
			Integer gid = groupService.addGroup(group);
			//User表中添加userGids列新组
			user = userService.getUserById(Integer.valueOf(uid));
			String gidString = user.getUserGids();
			//System.out.println(gidString);
			String gids=gidString+","+gid;
			//System.out.println(gids);
			user.setUserGids(gids);
			userService.changeUserGidsById(user);
			//System.out.println(gid);
			/*if (gid>0) {
				model.addAttribute("mygroup", groupService.getGroupById(gid));
			}*/
			 Map<String, Group> map = new HashMap<String, Group>();
			 map.put("mygroup", groupService.getGroupById(gid));
			return map;
		}else{
			return null;
		}
	}
}
