package com.oiice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oiice.dao.GroupsmsgMapper;
import com.oiice.service.GroupmsgService;
import com.oiice.vo.Groupmsg;
@Service("groupmsgService")
public class GroupmsgServiceImpl implements GroupmsgService {
	@Autowired
	private GroupsmsgMapper groupsmsgMapper;

	@Override
	public Integer changeGMReaded(Groupmsg groupmsg) {
		Integer gmid=null;
		try {
			gmid = groupsmsgMapper.updateGroupmsgReaded(groupmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gmid;
	}

	@Override
	public Integer addGroupmsg(Groupmsg groupmsg) {
		Integer gmid=null;
		try {
			groupsmsgMapper.insertGroupmsg(groupmsg);
			gmid = groupmsg.getGmId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gmid;
	}

	@Override
	public Groupmsg getGroupmsgById(Integer gmid) {
		Groupmsg groupmsg=null;
		try {
			groupmsg = groupsmsgMapper.selectGroupmsgById(gmid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupmsg;
	}

}
