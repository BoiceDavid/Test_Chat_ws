package com.oiice.dao;

import java.util.List;
import com.oiice.vo.Messages;

public interface MessagesMapper {
	public Messages selectMessagesById(Integer msgId) throws Exception;
	public Integer insertMessages(Messages messages) throws Exception;
	public Integer updateMessages(Messages messages) throws Exception;
	public List<Messages> selectUnreadByFromUserId(Integer msgToUserId) throws Exception;
	public List<Messages> selectMessagesByMsgToUserId(Integer msgToUserId) throws Exception;
	public List<Messages> selectUnread(Messages messages) throws Exception;
	public Messages selectUnreadReq(Messages messages) throws Exception;
}
