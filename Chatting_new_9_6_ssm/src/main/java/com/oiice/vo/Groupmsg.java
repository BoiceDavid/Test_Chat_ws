package com.oiice.vo;
// default package

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

/**
 * Groupmsg entity. @author MyEclipse Persistence Tools
 */
@Component
public class Groupmsg implements java.io.Serializable {

	// Fields

	private Integer gmId;
	private User user;
	private Group group;
	private String msgContent;
	private Timestamp msgTime;
	private String readedUserId;
	private Integer msgType;
	private Integer getmsgid;
	private Integer sendUserId;

	// Constructors

	/** default constructor */
	public Groupmsg() {
	}

	/** full constructor */
	public Groupmsg(User user, Group group, String msgContent,
			Timestamp msgTime, String readedUserId, Integer msgType) {
		this.user = user;
		this.group = group;
		this.msgContent = msgContent;
		this.msgTime = msgTime;
		this.readedUserId = readedUserId;
		this.msgType = msgType;
	}

	// Property accessors

	public Integer getGmId() {
		return this.gmId;
	}

	public void setGmId(Integer gmId) {
		this.gmId = gmId;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return this.group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getMsgContent() {
		return this.msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public Timestamp getMsgTime() {
		return this.msgTime;
	}

	public void setMsgTime(Timestamp msgTime) {
		this.msgTime = msgTime;
	}

	public String getReadedUserId() {
		return this.readedUserId;
	}

	public void setReadedUserId(String readedUserId) {
		this.readedUserId = readedUserId;
	}

	public Integer getMsgType() {
		return this.msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public Integer getGetmsgid() {
		return getmsgid;
	}

	public void setGetmsgid(Integer getmsgid) {
		this.getmsgid = getmsgid;
	}

	public Integer getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(Integer sendUserId) {
		this.sendUserId = sendUserId;
	}
	
	
}