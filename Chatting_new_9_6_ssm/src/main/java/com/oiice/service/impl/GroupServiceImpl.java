package com.oiice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.oiice.dao.GroupsMapper;
import com.oiice.service.GroupService;
import com.oiice.vo.Group;
@Service("groupService")
public class GroupServiceImpl implements GroupService {
	@Autowired
	private GroupsMapper groupsMapper;

	@Override
	public Group getGroupById(Integer gid) {
		Group group=null;
		try {
			group = groupsMapper.selectGroupByGid(gid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return group;
	}

	@Override
	public Group getGroupandmsgById(Integer gid) {
		Group group=null;
		try {
			group = groupsMapper.selectGroupandmsgByGid(gid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return group;
	}

	@Override
	public Integer changGroupmember(Group group) {
		Integer gid=null;
		try {
			gid = groupsMapper.updateGroupmember(group);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gid;
	}

	@Override
	public Integer addGroup(Group group) {
		Integer gid=null;
		try {
			groupsMapper.insertGroup(group);
			gid=group.getGid();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gid;
	}

	@Override
	public Group getGroupByName(String gname) {
		Group group=null;
		try {
			group = groupsMapper.selectGroupByGname(gname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return group;
	}

}
