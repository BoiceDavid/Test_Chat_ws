package com.oiice.service;

import com.oiice.vo.Groupmsg;

public interface GroupmsgService {
	public Integer changeGMReaded(Groupmsg groupmsg);
	public Integer addGroupmsg(Groupmsg groupmsg);
	public Groupmsg getGroupmsgById(Integer gmid);
}
