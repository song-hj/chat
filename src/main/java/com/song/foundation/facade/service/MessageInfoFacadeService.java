package com.song.foundation.facade.service;

import com.song.base.facade.service.BaseFacadeService;
import com.song.core.tools.InterfaceVo;
import com.song.foundation.entity.MessageInfo;

public interface MessageInfoFacadeService extends BaseFacadeService<MessageInfo, Long>{

	
	/**
	 * 保存信息
	 * @param fid
	 * @param content
	 * @param uid
	 * @return
	 */
	public InterfaceVo saveMessage(Long fid, String content, Long uid);

	/**
	 * 消息列表
	 * @param uid
	 * @return
	 */
	public InterfaceVo messageList(Long uid);

	/**
	 * 获取聊天信息
	 * @param fid
	 * @param id
	 * @return
	 */
	public InterfaceVo getReadMessage(boolean readStatus,Long fid, Long uid, int pageNo, int pageSize);

	/**
	 * 未读信息数量
	 * @param uid
	 * @return
	 */
	public InterfaceVo getNoReadMessageNum(Long uid);
}
