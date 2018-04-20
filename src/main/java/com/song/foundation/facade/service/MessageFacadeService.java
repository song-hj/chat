package com.song.foundation.facade.service;

import com.song.base.facade.service.BaseFacadeService;
import com.song.foundation.entity.Message;

public interface MessageFacadeService extends BaseFacadeService<Message, Long> {

	/**
	 * 验证是否聊过天
	 * @param fid
	 * @param uid
	 * @return
	 */
	public Message checkMessage(Long fid, Long uid);
}
