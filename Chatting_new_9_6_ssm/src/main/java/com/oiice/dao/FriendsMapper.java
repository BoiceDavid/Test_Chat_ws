package com.oiice.dao;

import com.oiice.vo.Friends;

public interface FriendsMapper {
	public Integer insertFriend(Friends friends) throws Exception;
	public Integer selectMyFriend(Friends friends) throws Exception;
}
