package com.song.foundation.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.song.base.service.BaseService;
import com.song.foundation.entity.Message;
import com.song.foundation.facade.service.MessageFacadeService;

@Service
@Transactional
public class MessageService extends BaseService<Message,Long> implements MessageFacadeService {

	@Override
	public Message checkMessage(Long fid, Long uid) {
		// TODO Auto-generated method stub
		String mark = null;
		if(fid.intValue() > uid.intValue()){
			mark = uid.toString() + "-" + fid.toString();
		}else{
			mark = fid.toString() + "-" + uid.toString();
		}
		Message message = this.findBy("mark", mark);
		if(message == null){
			Message newMessage = new Message();
			newMessage.setCreateInfo(uid, new Date());
			newMessage.setDeleteStatus(false);
			
			newMessage.setUserId(uid);
			newMessage.setFriendId(fid);
			newMessage.setMark(mark);
			message = this.saveObj(newMessage);
		}
		return message;
	}

}
