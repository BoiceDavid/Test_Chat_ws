package com.oiice.service;

import java.util.List;

import com.oiice.vo.Friends;
import com.oiice.vo.Messages;

public interface FriendsService {
	public Integer addFriend(Friends friends);
	public Integer foundMyFriend(Friends friends);
	public Messages getUnreadReq(Messages messages);
}
