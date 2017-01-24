package com.oiice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oiice.vo.User;

public interface UserService {
	public User getUserById(Integer userId);
	public User getUserByNickName(String userNickName);
	public User userLoginService(Integer userLoginId,String userPassWord);
	public User userRegisterService(String userNickName,String userPassWord,Integer userSex,java.sql.Date userBirthday,String userHeadPortrait);
	public List<User> getFriendsById(Integer userId);
	public User getUserByLoginId(Integer loginId);
	public Integer changeUserGidsById(User user);
}
