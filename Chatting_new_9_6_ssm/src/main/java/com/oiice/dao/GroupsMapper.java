package com.oiice.dao;
import com.oiice.vo.Group;
public interface GroupsMapper {
	public Group selectGroupByGid(Integer gid) throws Exception;
	public Group selectGroupandmsgByGid(Integer gid) throws Exception;
	public Integer updateGroupmember(Group group) throws Exception;
	public Integer insertGroup(Group group) throws Exception;
	public Group selectGroupByGname(String gname) throws Exception;
}
