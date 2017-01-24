package com.oiice.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.oiice.service.GroupService;
import com.oiice.service.UserService;
import com.oiice.vo.Group;
import com.oiice.vo.User;

@Controller
@RequestMapping("/users")
public class UserController {
		
	@Autowired
	private UserService userService;
	
	@Autowired
	private GroupService groupService;
	
	/*public void setUserService(UserService userService) {
		this.userService = userService;
	}*/
	//restful风格 /{userLoginId}/{userPassWord}
	@RequestMapping(value="/login",method=RequestMethod.POST,produces="text/html;charset=UTF-8")
	public String userlogin(@RequestParam("userLoginId") Integer userLoginId,@RequestParam("userPassWord") String userPassWord,Model model){
		String result = null;
		User user = userService.userLoginService(userLoginId, userPassWord);
		if (user==null) {
			result="error";
		}else {
			List<User> friends = userService.getFriendsById(user.getUserId());
			String groupIds = user.getUserGids();
			if (groupIds!=null&!"".equals(groupIds)) {
				String[] gids = groupIds.split(",");
				List<Group> groups=new ArrayList<Group>();
				Map<String, List<User>> groupMemberMap = new HashMap<String, List<User>>();	//存储用户所在每个群对应的群成员的昵称 
				for (String gid : gids) {
					List<User> membersList = new ArrayList<User>();	//注意 每个群必须是不同的list对象
					Integer g = Integer.valueOf(gid);
					Group group = groupService.getGroupandmsgById(g);
					
					String memberString = group.getMembers();	//得到所有成员Id
					String[] members =  memberString.split(",");
					for (String mstring : members) {
						Integer mid = Integer.valueOf(mstring);
						User mu = userService.getUserById(mid);//得到成员
						membersList.add(mu);
					}
					groupMemberMap.put(gid, membersList);
					
					groups.add(group);
				}
				System.out.println(com.alibaba.fastjson.JSONArray.toJSONString(groupMemberMap));//
				model.addAttribute("groups",groups);
				model.addAttribute("groupMemberMap",com.alibaba.fastjson.JSONArray.toJSONString(groupMemberMap,SerializerFeature.DisableCircularReferenceDetect));				
			}
			
			model.addAttribute("u",user);
			model.addAttribute("f",friends);
			result="chat";
		}
		
		return result;
	}
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String userRegister(HttpServletRequest request,HttpServletResponse response) {
		Map<String, String> remap= new HashMap<String, String>();
		Map<String, String[]> map = request.getParameterMap();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String result=null;
		Iterator<String> iterator = (Iterator)map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = map.get(key)[0];
			//System.out.println(key+"\t"+value);
			remap.put(key, value);
		}
		String userNickName=remap.get("userNickName");
		String userPassWord=remap.get("userPassWord");
		Integer userSex=Integer.valueOf(remap.get("userSex"));
		java.sql.Date userBirthday = null;
		try {
			userBirthday = new java.sql.Date(dateFormat.parse(remap.get("userBirthday")).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String userHeadPortrait=remap.get("userHeadPortrait");
		User user = null;
		//此处待完善
		if (userService.getUserByNickName(userNickName)==null) {
			user = userService.userRegisterService(userNickName, userPassWord, userSex, userBirthday, userHeadPortrait);
		}
		if (user!=null) {
			request.setAttribute("user", user);
			result="login";
		}else {
			result="register";
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/check",method=RequestMethod.POST)
	public String checkNickName(@RequestParam("userNickName") String userNickName){
		String flag="true";
		User testUser = userService.getUserByNickName(userNickName);
		if (testUser!=null) {
			flag="false";
		}
		return flag;
	}
	
}
