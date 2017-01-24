package com.oiice.vo;
// default package

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

/**
 * Group entity. @author MyEclipse Persistence Tools
 */
@Component
public class Group implements java.io.Serializable {

	// Fields

	private Integer gid;
	private String gname;
	private String members;
	private Date createtime;
	private List<Groupmsg> groupmsgs = new ArrayList<Groupmsg>(); 

	// Constructors

	/** default constructor */
	public Group() {
	}

	/** full constructor */
	
	public Group(String gname, String members, Date createtime,
			List<Groupmsg> groupmsgs) {
		super();
		this.gname = gname;
		this.members = members;
		this.createtime = createtime;
		this.groupmsgs = groupmsgs;
	}


	// Property accessors

	public Integer getGid() {
		return this.gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public String getGname() {
		return this.gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getMembers() {
		return this.members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public List<Groupmsg> getGroupmsgs() {
		return groupmsgs;
	}

	public void setGroupmsgs(List<Groupmsg> groupmsgs) {
		this.groupmsgs = groupmsgs;
	}

	

}