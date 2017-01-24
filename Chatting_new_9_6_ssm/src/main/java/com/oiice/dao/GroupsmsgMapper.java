package com.oiice.dao;
import com.oiice.vo.Groupmsg;

public interface GroupsmsgMapper {
	public Integer updateGroupmsgReaded(Groupmsg groupmsg) throws Exception;
	public Integer insertGroupmsg(Groupmsg groupmsg) throws Exception;
	public Groupmsg selectGroupmsgById(Integer gmid) throws Exception;
}
