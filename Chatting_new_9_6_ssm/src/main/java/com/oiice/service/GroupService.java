package com.oiice.service;

import com.oiice.vo.Group;

public interface GroupService {
	public Group getGroupById(Integer gid);
	public Group getGroupandmsgById(Integer gid);
	public Integer changGroupmember(Group group);
	public Integer addGroup(Group group);
	public Group getGroupByName(String gname);
}
