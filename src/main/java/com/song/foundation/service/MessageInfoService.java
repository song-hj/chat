package com.song.foundation.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.song.activemq.mq.producer.queue.ProducerService;
import com.song.base.search.PageRequest;
import com.song.base.service.BaseService;
import com.song.core.tools.CommUtil;
import com.song.core.tools.FileConfig;
import com.song.core.tools.InterfaceVo;
import com.song.core.tools.InterfaceVo.Code;
import com.song.foundation.entity.Accessory;
import com.song.foundation.entity.Friends;
import com.song.foundation.entity.Message;
import com.song.foundation.entity.MessageInfo;
import com.song.foundation.entity.User;
import com.song.foundation.facade.service.MessageInfoFacadeService;

@Service
@Transactional
public class MessageInfoService extends BaseService<MessageInfo,Long> implements MessageInfoFacadeService {

	@Autowired
	private FriendsService friendsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AccessoryService accessoryService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ProducerService producerService;
	
	@Override
	public InterfaceVo saveMessage(Long fid, String content, Long uid) {
		// TODO Auto-generated method stub
		InterfaceVo vo = new InterfaceVo();
		if(!CommUtil.isNotNull(content)){
			vo.setMessage("发送信息不能为空");
			vo.setCode(-1);
			return vo;
		}
		//验证是否为好友
		List<Object> params = Lists.newArrayList();
		params.add(uid);
		params.add(fid);
		params.add(uid);
		params.add(fid);
		String hql = "select count(1) from Friends x where ((x.userId = ?1 and x.friendId = ?2) ";
		hql += " or (x.friendId = ?3 and x.userId = ?4))";
		long count = friendsService.count(hql, params.toArray());
		if(count == 0){
			vo.setMessage("请先添加为好友");
			vo.setCode(-1);
			return vo;
		}
		
		//验证是否聊过天
		Message message = messageService.checkMessage(fid, uid);
		
		Date date = new Date();
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setCreateInfo(uid, date);
		messageInfo.setDeleteStatus(false);
		
		messageInfo.setReadStatus(false);
		messageInfo.setMessageStatus(0);
		messageInfo.setMessageId(message.getId());
		messageInfo.setContent(content);
		this.save(messageInfo);
		
		//修改时间
		message.setUpdateTime(date);
		messageService.update(message);
		
		User user = userService.findOne(uid);
		Map<String, Object> outData = Maps.newHashMap();
		if(user.getHeadId() != null){
			Accessory acc = accessoryService.findOne(user.getHeadId());
			String head = acc.getPath() + acc.getName() + acc.getExt();
			outData.put("head", head);
		}
		
		//消息队列
		producerService.sendMessage("message.queue-" + uid + "-" + fid, content);
		
		vo.setOutData(outData);
		vo.setCodeEnum(Code.SUCCESS);
		return vo;
	}

	@Override
	public InterfaceVo messageList(Long uid) {
		// TODO Auto-generated method stub
		InterfaceVo vo = new InterfaceVo();
		Map<String,Object> map = Maps.newHashMap();
		List<Object> params = Lists.newArrayList();
		params.add(uid);
		params.add(uid);
		String hql = "select x from Friends x,Message y where (x.userId = ?1 or x.friendId = ?2) and x.agree = 1 and y.deleteStatus = false ";
		hql += " and ((y.userId = x.userId and y.friendId = x.friendId) or (y.userId = x.friendId and y.friendId = x.userId)) ";
		hql += " order by y.updateTime desc ";
		List<Friends> lstFriends = friendsService.findAll(hql, null, params.toArray());
		
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
		List<Object> data = Lists.newArrayList();
		for (Friends friend : lstFriends) {
			Map<String,Object> mapObj = Maps.newHashMap();
			User fuser = null;
			if(friend.getUserId().equals(uid)){
				fuser = userService.findOne(friend.getFriendId());
				mapObj.put("fname", friend.getFriendName() == null?fuser.getNickName():friend.getFriendName());
			}else{
				fuser = userService.findOne(friend.getUserId());
				mapObj.put("fname", friend.getUserName() == null?fuser.getNickName():friend.getUserName());
			}
			Message message = messageService.checkMessage(friend.getFriendId(), friend.getUserId());
			if(fuser.getHeadId() != null){
				Accessory acc = accessoryService.findOne(fuser.getHeadId());
				mapObj.put("head", acc.getPath() + acc.getName() + acc.getExt());
			}
			mapObj.put("fid", fuser.getId());
			String m = "凌晨";
			if(message.getUpdateTime().getHours() >= 6 && message.getUpdateTime().getHours() < 12){
				m = "上午";
			}else if(message.getUpdateTime().getHours() >= 12 && message.getUpdateTime().getHours() < 18){
				m = "下午";
			}else if(message.getUpdateTime().getHours() >= 18){
				m = "晚上";
			}
			mapObj.put("time",m+sdf.format(message.getUpdateTime()));
			mapObj.put("msgId",message.getId());
			
			params.clear();
			params.add(message.getId());
			params.add(uid);
			hql = "select x from MessageInfo x where x.deleteStatus = false and x.messageId = ?1 ";
			hql += " and x.messageStatus != ?2 order by x.createTime desc";
			MessageInfo messageInfo = this.findAll(hql, new PageRequest(0, 1), params.toArray()).get(0);
			String content = messageInfo.getContent().length() > 15?messageInfo.getContent().substring(0, 15)+"..." : messageInfo.getContent();
			mapObj.put("content",content);
			data.add(mapObj);
		}
		if(data.size() > 0){
			map.put("data", data);
		}
		vo.setOutData(map);
		vo.setCodeEnum(Code.SUCCESS);
		return vo;
	}

	@Override
	public InterfaceVo getReadMessage(boolean readStatus,Long fid, Long uid, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		Map<String,Object> map = Maps.newHashMap();
		InterfaceVo vo = new InterfaceVo();
		String mark = null;
		if(fid.intValue() > uid.intValue()){
			mark = uid.toString() + "-" + fid.toString();
		}else{
			mark = fid.toString() + "-" + uid.toString();
		}
		Message message = messageService.findBy("mark", mark);
		if(message == null){
			vo.setCode(-1);
			vo.setMessage("参数错误");
			return vo;
		}
		
		List<Object> params = Lists.newArrayList();
		params.add(message.getId());
		params.add(uid.intValue());
		String hql = " from MessageInfo x where x.deleteStatus = false and x.messageId = ?1 and x.messageStatus != ?2 ";
		if(readStatus == false){
			params.add(fid);
			params.add(readStatus);
			hql += " and x.createUserId = ?3  and x.readStatus = ?4 ";
		}
		hql += " order by x.createTime desc ";
		
		List<MessageInfo> list = this.findAll("select x " + hql, new PageRequest(pageNo, pageSize), params.toArray());
		
		if(readStatus){
			long count = this.count("select count(1) " + hql, params.toArray());
			map.put("totalPage", count%pageSize == 0? count/pageSize : count/pageSize + 1);
		}
		
		User fuser = userService.findOne(fid);
		User user = userService.findOne(uid);
		String fhead = null;
		String uhead = null;
		if(fuser.getHeadId() != null){
			Accessory acc = accessoryService.findOne(fuser.getHeadId());
			fhead = acc.getPath() + acc.getName() + acc.getExt();
		}
		if(user.getHeadId() != null){
			Accessory acc = accessoryService.findOne(user.getHeadId());
			uhead = acc.getPath() + acc.getName() + acc.getExt();
		}
		
		List<Object> data = Lists.newArrayList();
		for (MessageInfo messageInfo : list) {
			Map<String,Object> mapObj = Maps.newHashMap();
			mapObj.put("msgId", messageInfo.getId());
			mapObj.put("content", messageInfo.getContent());
			if(messageInfo.getCreateUserId().equals(uid)){
				mapObj.put("status", "right");
				mapObj.put("head", uhead);
			}else{
				mapObj.put("status", "left");
				mapObj.put("head", fhead);
			}
			//修改成已读
			if(messageInfo.isReadStatus() == false && !messageInfo.getCreateUserId().equals(uid)){
				messageInfo.setReadStatus(true);
				messageInfo.setUpdateTime(new Date());
				this.update(messageInfo);
			}
			data.add(mapObj);
		}
		if(data.size() > 0){
			map.put("data", data);
		}
		vo.setOutData(map);
		vo.setCodeEnum(Code.SUCCESS);
		return vo;
	}

	@Override
	public InterfaceVo getNoReadMessageNum(Long uid) {
		// TODO Auto-generated method stub
		InterfaceVo vo = new InterfaceVo();
		String hql = "select y.createUserId,count(1) from Message x,MessageInfo y "
				+ " where x.deleteStatus = false and y.deleteStatus = false "
				+ " and x.id = y.messageId and (x.userId = "+ uid +" or x.friendId = "+ uid +") "
			    + " and y.readStatus = false and y.createUserId != "+ uid +" "
				+ " group by y.createUserId";
		
		List<Object> list = this.findAllS(hql, null, null);
		
		List<Object> data = Lists.newArrayList();
		for (Object obj : list) {
			Object[] objs = (Object[])obj;
			Map<String,Object> mapObj = Maps.newHashMap();
			mapObj.put("fid", objs[0]);
			mapObj.put("msgNum", objs[1]);
			data.add(mapObj);
		}
		if(data.size() > 0){
			Map<String,Object> outData = Maps.newHashMap();
			outData.put("data", data);
			vo.setOutData(outData);
		}
		vo.setCodeEnum(Code.SUCCESS);
		return vo;
	}

}
