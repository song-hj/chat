package com.song.foundation.facade.service;


import com.song.base.facade.service.BaseFacadeService;
import com.song.core.tools.InterfaceVo;
import com.song.foundation.entity.Friends;

public interface FriendsFacadeService extends BaseFacadeService<Friends, Long> {
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public InterfaceVo friendsList(int pageNo, int pageSize, String friendName);
	
	
	/**
	 * 查找朋友
	 * @param value
	 * @param id
	 * @return
	 */
	public InterfaceVo findFriend(String value,Long fid, Long uid, int status);

	/**
	 * 添加朋友
	 * @param fid
	 * @param id
	 * @return
	 */
	public InterfaceVo addFriend(Long fid, Long uid);


	/**
	 * 新的好友列表
	 * @param null2String
	 * @param type
	 * @param id
	 * @return
	 */
	public InterfaceVo friendList(Long uid);
	
	
	/**
	 * 好友列表
	 * @param fName
	 * @param uid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public InterfaceVo friendList(String fName, Long uid, int pageNo, int pageSize);


	/**
	 * 同意或拒绝好友
	 * @param id
	 * @param agree
	 * @return
	 */
	public InterfaceVo agreeFriend(Long id, int agree, Long uid);


	/**
	 * 修改昵称
	 * @param fid
	 * @param id
	 * @return
	 */
	public InterfaceVo updateFrienddName(String fName,Long fid, Long uid);

}
