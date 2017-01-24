package com.oiice.service;

import java.util.List;

import com.oiice.vo.Messages;

public interface ChatService {
	public Messages getMessagesById(Integer msgId);
	public Integer addSendMsg(Messages messages);
	public Integer changeMsgStatus(Messages messages);
	public List<Messages> getUnreadByToUserId(Integer toUserId);
	public List<Messages> getMessagesByToUserId(Integer toUserId);
	public List<Messages> getUnread(Messages messages);
}
