package com.oiice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.oiice.dao.FriendsMapper;
import com.oiice.dao.MessagesMapper;
import com.oiice.service.FriendsService;
import com.oiice.vo.Friends;
import com.oiice.vo.Messages;

@Service("friendsService")
public class FriendsServiceImpl implements FriendsService {
	@Autowired
	private FriendsMapper friendsMapper;
	@Autowired
	private MessagesMapper messagesMapper;
	@Autowired
	private Friends friends;
	@Override
	public Integer addFriend(Friends friends) {
		Integer friendsId=null;
		try {
			friendsId = friendsMapper.insertFriend(friends);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return friendsId;
	}
	@Override
	public Integer foundMyFriend(Friends friends) {
		Integer row = null;
		try {
			row = friendsMapper.selectMyFriend(friends);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return row;
	}
	@Override
	public Messages getUnreadReq(Messages messages) {
		Messages msg = null;
		try {
			msg = messagesMapper.selectUnreadReq(messages);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

}
