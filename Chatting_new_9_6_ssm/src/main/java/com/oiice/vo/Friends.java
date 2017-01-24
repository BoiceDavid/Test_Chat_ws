package com.oiice.vo;

import org.springframework.stereotype.Component;

/**
 * Friends entity. @author MyEclipse Persistence Tools
 */
@Component
public class Friends implements java.io.Serializable {

	// Fields

	private Integer friendsId;
	private String friendName;
	private User usersByFriendUserId;
	private User usersByMyUserId;
	//private Friendgroups friendgroups;
	//private Friendtype friendtype;
	private Integer friendUserId;
	private Integer myUserId;
	private Integer groupId;
	private Integer typeId;

	// Constructors

	/** default constructor */
	public Friends() {
	}

	public Friends(String friendName,
			User usersByFriendUserId, User usersByMyUserId,
			Integer friendUserId, Integer myUserId, Integer groupId,
			Integer typeId) {
		super();
		this.friendName = friendName;
		this.usersByFriendUserId = usersByFriendUserId;
		this.usersByMyUserId = usersByMyUserId;
		//this.friendgroups = friendgroups;
		//this.friendtype = friendtype;
		this.friendUserId = friendUserId;
		this.myUserId = myUserId;
		this.groupId = groupId;
		this.typeId = typeId;
	}

	// Property accessors

	public Integer getFriendsId() {
		return this.friendsId;
	}

	public void setFriendsId(Integer friendsId) {
		this.friendsId = friendsId;
	}

	public User getUsersByMyUserId() {
		return this.usersByMyUserId;
	}

	public void setUsersByMyUserId(User usersByMyUserId) {
		this.usersByMyUserId = usersByMyUserId;
	}

/*	public Friendgroups getFriendgroups() {
		return this.friendgroups;
	}

	public void setFriendgroups(Friendgroups friendgroups) {
		this.friendgroups = friendgroups;
	}

	public Friendtype getFriendtype() {
		return this.friendtype;
	}

	public void setFriendtype(Friendtype friendtype) {
		this.friendtype = friendtype;
	}*/

	public User getUsersByFriendUserId() {
		return this.usersByFriendUserId;
	}

	public void setUsersByFriendUserId(User usersByFriendUserId) {
		this.usersByFriendUserId = usersByFriendUserId;
	}

	public String getFriendName() {
		return this.friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public Integer getFriendUserId() {
		return friendUserId;
	}

	public void setFriendUserId(Integer friendUserId) {
		this.friendUserId = friendUserId;
	}

	public Integer getMyUserId() {
		return myUserId;
	}

	public void setMyUserId(Integer myUserId) {
		this.myUserId = myUserId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

}