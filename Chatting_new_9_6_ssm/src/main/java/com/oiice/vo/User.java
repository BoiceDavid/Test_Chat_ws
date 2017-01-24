package com.oiice.vo;

import java.sql.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;


/**
 * 用户类
 * @author acer
 *
 */
@Component
public class User {
	private Integer userId;
	private Integer userLoginId;
	private String userNickName;
	private String userPassWord;
	private String userSignature;
	private Integer userSex;
	@DateTimeFormat(pattern="yyyy-mm-dd")
	private Date userBirthday;
	private String userHeadPortrait;
	private Integer policyId;
	private Integer userStatus;
	private String userFriendPolicyQuestion;
	private String userFriendPolicyAnswer;
	private String userGids;
	private List<Friends> friendsesForMyUserId;
	private List<Friends> friendsesForFriendUserId;
	private List<Messages> messagesesForMsgFromUserId;
	private List<Messages> messagesesForMsgToUserId;
	//private List<Friendgroups> friendgroupses;

	public User(){}
	
	public User(String userNickName, String userPassWord, String userSignature,
			Integer userSex, Date userBirthday, String userHeadPortrait,
			Integer policyId, Integer userState,
			String userFriendPolicyQuestion, String userFriendPolicyAnswer) {
		super();
		this.userNickName = userNickName;
		this.userPassWord = userPassWord;
		this.userSignature = userSignature;
		this.userSex = userSex;
		this.userBirthday = userBirthday;
		this.userHeadPortrait = userHeadPortrait;
		this.policyId = policyId;
		this.userStatus = userState;
		this.userFriendPolicyQuestion = userFriendPolicyQuestion;
		this.userFriendPolicyAnswer = userFriendPolicyAnswer;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(Integer userLoginId) {
		this.userLoginId = userLoginId;
	}
	public String getUserNickName() {
		return userNickName;
	}
	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}
	public String getUserPassWord() {
		return userPassWord;
	}
	public void setUserPassWord(String userPassWord) {
		this.userPassWord = userPassWord;
	}
	public String getUserSignature() {
		return userSignature;
	}
	public void setUserSignature(String userSignature) {
		this.userSignature = userSignature;
	}
	public Integer getUserSex() {
		return userSex;
	}
	public void setUserSex(Integer userSex) {
		this.userSex = userSex;
	}
	public Date getUserBirthday() {
		return userBirthday;
	}
	public void setUserBirthday(Date userBirthday) {
		this.userBirthday = userBirthday;
	}
	public String getUserHeadPortrait() {
		return userHeadPortrait;
	}
	public void setUserHeadPortrait(String userHeadPortrait) {
		this.userHeadPortrait = userHeadPortrait;
	}
	public Integer getPolicyId() {
		return policyId;
	}
	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}
	public String getUserFriendPolicyQuestion() {
		return userFriendPolicyQuestion;
	}
	public void setUserFriendPolicyQuestion(String userFriendPolicyQuestion) {
		this.userFriendPolicyQuestion = userFriendPolicyQuestion;
	}
	public String getUserFriendPolicyAnswer() {
		return userFriendPolicyAnswer;
	}
	public void setUserFriendPolicyAnswer(String userFriendPolicyAnswer) {
		this.userFriendPolicyAnswer = userFriendPolicyAnswer;
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public List<Friends> getFriendsesForMyUserId() {
		return friendsesForMyUserId;
	}

	public void setFriendsesForMyUserId(List<Friends> friendsesForMyUserId) {
		this.friendsesForMyUserId = friendsesForMyUserId;
	}

	public List<Friends> getFriendsesForFriendUserId() {
		return friendsesForFriendUserId;
	}

	public void setFriendsesForFriendUserId(List<Friends> friendsesForFriendUserId) {
		this.friendsesForFriendUserId = friendsesForFriendUserId;
	}

	public List<Messages> getMessagesesForMsgFromUserId() {
		return messagesesForMsgFromUserId;
	}

	public void setMessagesesForMsgFromUserId(
			List<Messages> messagesesForMsgFromUserId) {
		this.messagesesForMsgFromUserId = messagesesForMsgFromUserId;
	}

	public List<Messages> getMessagesesForMsgToUserId() {
		return messagesesForMsgToUserId;
	}

	public void setMessagesesForMsgToUserId(List<Messages> messagesesForMsgToUserId) {
		this.messagesesForMsgToUserId = messagesesForMsgToUserId;
	}

/*	public List<Friendgroups> getFriendgroupses() {
		return friendgroupses;
	}

	public void setFriendgroupses(List<Friendgroups> friendgroupses) {
		this.friendgroupses = friendgroupses;
	}*/

	public String getUserGids() {
		return userGids;
	}

	public void setUserGids(String userGids) {
		this.userGids = userGids;
	}
	
	
}
