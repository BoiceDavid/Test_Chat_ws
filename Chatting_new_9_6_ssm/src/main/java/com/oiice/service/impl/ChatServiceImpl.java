package com.oiice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oiice.dao.MessagesMapper;
import com.oiice.service.ChatService;
import com.oiice.vo.Messages;
@Service("chatService")
public class ChatServiceImpl implements ChatService {
	@Autowired
	private MessagesMapper messagesMapper;
	
	@Autowired
	private Messages messages;
	
	@Override
	public Integer addSendMsg(Messages messages) {
		messages.setMsgStatus(0);
		Integer msgId=null;
		try {
			msgId = messagesMapper.insertMessages(messages);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgId;
	}

	@Override
	public Messages getMessagesById(Integer msgId) {
		try {
			messages = messagesMapper.selectMessagesById(msgId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messages;
	}

	@Override
	public Integer changeMsgStatus(Messages messages) {
		Integer msgId=null;
		try {
			msgId = messagesMapper.updateMessages(messages);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgId;
	}

	@Override
	public List<Messages> getUnreadByToUserId(Integer toUserId) {
		List<Messages> mList=null;
		try {
			mList = messagesMapper.selectUnreadByFromUserId(toUserId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mList;
	}

	@Override
	public List<Messages> getMessagesByToUserId(Integer toUserId) {
		
		List<Messages> mList = null;
		try {
			mList = messagesMapper.selectMessagesByMsgToUserId(toUserId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mList;
	}

	@Override
	public List<Messages> getUnread(Messages messages) {
		List<Messages> mList = null;
		try {
			mList = messagesMapper.selectUnread(messages);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mList;
	}

}
