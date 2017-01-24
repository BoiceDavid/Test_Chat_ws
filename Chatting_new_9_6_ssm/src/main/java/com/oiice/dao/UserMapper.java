package com.oiice.dao;

import java.util.List;
import com.oiice.vo.User;

/**
 * 用户表映射器类（Mapper Instances）
 * @author acer
 *
 */

public interface UserMapper {
	public User queryUserById(Integer userId) throws Exception;
	public User queryUserByNickName(String userNickName) throws Exception;
	public User queryUser(User user) throws Exception;
	public Integer insertUser(User user) throws Exception;
	public List<User> getFriendsByMyId(Integer userId) throws Exception;
	public Integer updateUstatusById(User user) throws Exception;
	public User selectUserByLoginId(Integer loginId) throws Exception;
	public Integer updateUserGidsById(User user) throws Exception;
}
