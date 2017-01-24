package com.oiice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.oiice.dao.UserMapper;
import com.oiice.service.UserService;
import com.oiice.vo.User;
@Service("userService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private User user;
	
	@Override
	public User getUserById(Integer userId) {
		User user=null;
		try {
			user = userMapper.queryUserById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User userLoginService(Integer userLoginId,String userPassWord) {
		//User user = new ClassPathXmlApplicationContext("applicationContext.xml").getBean("user",User.class);
		user.setUserLoginId(userLoginId);
		user.setUserPassWord(userPassWord);
		try {
			user=userMapper.queryUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User userRegisterService(String userNickName, String userPassWord, Integer userSex,java.sql.Date userBirthday, String userHeadPortrait) {
		Integer userLoginId =(int)(100000+Math.random()*999900000);
		user.setUserLoginId(userLoginId);
		user.setUserNickName(userNickName);
		user.setUserPassWord(userPassWord);
		user.setUserSex(userSex);
		user.setUserBirthday(userBirthday);
		user.setUserHeadPortrait(userHeadPortrait);
		Integer userId=null;
		try {
			userMapper.insertUser(user);
			
		} catch (Exception e) {
			e.printStackTrace();
		}//插入数据
		//user = this.getUserById(userId);
		return user;
	}

	@Override
	public List<User> getFriendsById(Integer userId) {
		List<User> friends=null;
		try {
			friends = userMapper.getFriendsByMyId(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return friends;
	}

	@Override
	public User getUserByLoginId(Integer loginId) {
		User user=null;
		try {
			user = userMapper.selectUserByLoginId(loginId);
		} catch (Exception e) {
			e.printStackTrace();
		}
			return user;			
	}

	@Override
	public Integer changeUserGidsById(User user) {
		Integer uid=null;
		try {
			uid = userMapper.updateUserGidsById(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uid;
	}

	@Override
	public User getUserByNickName(String userNickName) {
		User user=null;
		try {
			user = userMapper.queryUserByNickName(userNickName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

}
