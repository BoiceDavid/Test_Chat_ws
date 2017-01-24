package com.oiice.vo;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

/**
 * Messages entity. @author MyEclipse Persistence Tools
 */
@Component
public class Messages implements java.io.Serializable {

	// Fields

	private Integer msgId;
	//private Messagestype messagestype;
	private User usersByMsgFromUserId;
	private User usersByMsgToUserId;
	private String msgContent;
	private Integer msgStatus;
	private Timestamp msgTime;
	private Integer msgFromUserId;
	private Integer msgToUserId;
	private Integer msgTypeId;
	// Constructors

	/** default constructor */
	public Messages() {
	}

	/** full constructor */
	public Messages(User usersByMsgFromUserId,
			User usersByMsgToUserId, String msgContent, Integer msgStatus,
			Timestamp msgTime) {
		//this.messagestype = messagestype;
		this.usersByMsgFromUserId = usersByMsgFromUserId;
		this.usersByMsgToUserId = usersByMsgToUserId;
		this.msgContent = msgContent;
		this.msgStatus = msgStatus;
		this.msgTime = msgTime;
	}

	// Property accessors
	public Integer getMsgId() {
		return msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

/*	public Messagestype getMessagestype() {
		return messagestype;
	}

	public void setMessagestype(Messagestype messagestype) {
		this.messagestype = messagestype;
	}*/

	public User getUsersByMsgFromUserId() {
		return usersByMsgFromUserId;
	}

	public void setUsersByMsgFromUserId(User usersByMsgFromUserId) {
		this.usersByMsgFromUserId = usersByMsgFromUserId;
	}

	public User getUsersByMsgToUserId() {
		return usersByMsgToUserId;
	}

	public void setUsersByMsgToUserId(User usersByMsgToUserId) {
		this.usersByMsgToUserId = usersByMsgToUserId;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public Integer getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(Integer msgStatus) {
		this.msgStatus = msgStatus;
	}

	public Timestamp getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(Timestamp msgTime) {
		this.msgTime = msgTime;
	}

	public Integer getMsgFromUserId() {
		return msgFromUserId;
	}

	public void setMsgFromUserId(Integer msgFromUserId) {
		this.msgFromUserId = msgFromUserId;
	}

	public Integer getMsgToUserId() {
		return msgToUserId;
	}

	public void setMsgToUserId(Integer msgToUserId) {
		this.msgToUserId = msgToUserId;
	}

	public Integer getMsgTypeId() {
		return msgTypeId;
	}

	public void setMsgTypeId(Integer msgTypeId) {
		this.msgTypeId = msgTypeId;
	}
	
}