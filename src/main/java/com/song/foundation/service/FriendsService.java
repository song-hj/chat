package com.song.foundation.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.song.base.search.PageRequest;
import com.song.base.service.BaseService;
import com.song.core.tools.CommUtil;
import com.song.core.tools.FileConfig;
import com.song.core.tools.InterfaceVo;
import com.song.core.tools.InterfaceVo.Code;
import com.song.foundation.entity.Accessory;
import com.song.foundation.entity.Friends;
import com.song.foundation.entity.User;
import com.song.foundation.facade.service.FriendsFacadeService;

@Service
@Transactional
public class FriendsService extends BaseService<Friends,Long> implements FriendsFacadeService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AccessoryService accessoryService;
	
	
	@Override
	public InterfaceVo friendsList(int pageNo, int pageSize, String friendName) {
		// TODO Auto-generated method stub
		InterfaceVo vo = new InterfaceVo();
		Map<String,Object> params = Maps.newHashMap();
		params.put("friendName", friendName);
		List<Friends> lstFds = this.findAll(new PageRequest(pageNo, pageSize), params);
		List<Object> data = Lists.newArrayList(); 
		for (Friends friends : lstFds) {
			
		}
		vo.setCodeEnum(Code.SUCCESS);
//		vo.setTotalPage(totalPage);
//		vo.setTotal(total);
		return vo;
	}
	
	@Override
	public InterfaceVo findFriend(String value, Long fid, Long uid, int status) {
		// TODO Auto-generated method stub
		InterfaceVo vo = new InterfaceVo();
		Map<String,Object> map = Maps.newHashMap();
		if(status == 0){
			if(!CommUtil.isNotNull(value)){
				vo.setMessage("查询条件不能为空");
				vo.setCode(-1);
				return vo;
			}
			User user = userService.findBy("userName", value);
			if(user == null){
				user = userService.findBy("mobile", value);
			}
			if(user != null){
				List<Object> params = Lists.newArrayList();
				params.add(uid);
				params.add(user.getId());
				params.add(uid);
				params.add(user.getId());
				String hql = "select count(1) from Friends x where ((x.userId = ?1 and x.friendId = ?2) ";
				hql += " or (x.friendId = ?3 and x.userId = ?4)) and x.agree = 1 ";
				long count = this.count(hql, params.toArray());
				map.put("nickName", user.getNickName());
				map.put("uid", user.getId());
				if(user.getHeadId() != null){
					Accessory acc = accessoryService.findOne(user.getHeadId());
					map.put("head", acc.getPath() + acc.getName() + acc.getExt());
				}
				map.put("status", false);
				if(user.getId().equals(uid) || count > 0){
					map.put("status", true);
				}
				vo.setOutData(map);
				vo.setCodeEnum(Code.SUCCESS);
				return vo;
			}
		}else if(status == 1){
			List<Object> params = Lists.newArrayList();
			StringBuffer hql = new StringBuffer(" select x from Friends x");
			hql.append(" where ((x.userId = ?1 and x.friendId = ?2) or (x.friendId = ?3 and x.userId = ?4)) ");
			hql.append(" and x.agree = 1 ");
			params.add(uid);
			params.add(fid);
			params.add(uid);
			params.add(fid);
			List<Friends> list = this.findAll(hql.toString(), null, params.toArray());
			if(list.size() == 1){
				Friends fd = list.get(0);
				User user = userService.findOne(fid);
				if(fd.getUserId().equals(uid)){
					map.put("fName", fd.getFriendName());
				}else{
					map.put("fName", fd.getUserName());
				}
				map.put("userName",user.getUserName());
				map.put("nickName",user.getNickName());
				map.put("fid",user.getId());
				map.put("sex",user.getSex());
				map.put("address",user.getAddress());
				map.put("description", user.getDescription());
				List<String> photos = Lists.newArrayList();
				photos.add("/picture/1/LRm9YA2c2sKyo72Ntu.jpg");
				photos.add("/picture/6/13oWPhpQStuGFokXoH.jpg");
				photos.add("/picture/5/68899xeIYkLAbWiXUY.jpg");
				map.put("photos",photos);
				if(user.getHeadId() != null){
					Accessory acc = accessoryService.findOne(user.getHeadId());
					map.put("head", acc.getPath() + acc.getName() + acc.getExt());
				}
				vo.setOutData(map);
				vo.setCodeEnum(Code.SUCCESS);
				return vo;
			}
		}
		vo.setCodeEnum(Code.ERROR);
		return vo;
	}

	@Override
	public InterfaceVo addFriend(Long fid, Long uid) {
		// TODO Auto-generated method stub
		InterfaceVo vo = new InterfaceVo();
		if(fid.equals(uid)){
			vo.setCode(-1);
			vo.setMessage("不能添加自己为好友");
			return vo;
		}
		User fuser = userService.findOne(fid);
		if(fuser == null){
			vo.setCode(-1);
			vo.setMessage("该用户不存在");
			return vo;
		}
		List<Object> params = Lists.newArrayList();
		params.add(uid);
		params.add(fid);
		params.add(uid);
		params.add(fid);
		String hql = "select x from Friends x where ((x.userId = ?1 and x.friendId = ?2) ";
		hql += " or (x.friendId = ?3 and x.userId = ?4)) ";
		List<Friends> friends = this.findAll(hql, null, params.toArray());
		if(friends.size() > 0){
			if(friends.get(0).getAgree() == 1){
				vo.setCode(-1);
				vo.setMessage("你已添加过");
				return vo;
			}else{
				Friends fd = friends.get(0);
				this.delete(fd);
			}
		}
		User user = userService.findOne(uid);
		Friends friend = new Friends();
		friend.setCreateInfo(uid, new Date());
		friend.setDeleteStatus(false);
		friend.setFriendId(fid);
		friend.setUserId(uid);
		friend.setAgree(0);
		this.save(friend);
		vo.setCodeEnum(Code.SUCCESS);
		
		return vo;
	}

	@Override
	public InterfaceVo friendList(Long uid) {
		// TODO Auto-generated method stub
		InterfaceVo vo = new InterfaceVo();
		Map<String,Object> map = Maps.newHashMap();
		Map<String,Object> params = Maps.newHashMap();
		params.put("deleteStatus", false);
		params.put("friendId", uid);
		List<Friends> list = this.findAll(params);
		List<Object> data = Lists.newArrayList();
		for (Friends friend : list) {
			Map<String, Object> mapObj = Maps.newHashMap();
			User user = userService.findOne(friend.getUserId());
			mapObj.put("id", friend.getId());
			mapObj.put("fName", user.getNickName());
			mapObj.put("agree", friend.getAgree());
			if(user.getHeadId() != null){
				Accessory acc = accessoryService.findOne(user.getHeadId());
				mapObj.put("head", acc.getPath() + acc.getName() + acc.getExt());
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
	public InterfaceVo agreeFriend(Long id, int agree, Long uid) {
		// TODO Auto-generated method stub
		InterfaceVo vo = new InterfaceVo();
		Friends friends = this.findOne(id);
		if(friends == null || !friends.getFriendId().equals(uid)){
			vo.setCode(-1);
			vo.setMessage("操作失败");
			return vo;
		}
		if(agree == 1 || agree == 2){
			friends.setAgree(agree);
			friends.setUpdateTime(new Date());
			this.update(friends);
			vo.setCodeEnum(Code.SUCCESS);
			return vo;
		}
		vo.setCode(-1);
		vo.setMessage("操作失败");
		return vo;
	}

	@Override
	public InterfaceVo friendList(String fName, Long uid, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		InterfaceVo vo = new InterfaceVo();
		Map<String,Object> map = Maps.newHashMap();
		List<Object> params = Lists.newArrayList();
		StringBuffer hql = new StringBuffer(" select x from Friends x");
		params.add(uid);
		params.add(uid);
		hql.append(" where (x.userId = ?1 or x.friendId = ?2) and x.agree = 1 ");
		if(CommUtil.isNotNull(fName)){
			params.add("%"+fName+"%");
			params.add("%"+fName+"%");
			hql.append(" and (x.friendName like ?3 or x.userName like ?4) ");
		}
		hql.append(" order by x.createTime ");
		List<Friends> list = this.findAll(hql.toString(), new PageRequest(pageNo, pageSize), params.toArray());
		List<Object> data = Lists.newArrayList();
		for (Friends friend : list) {
			Map<String, Object> mapObj = Maps.newHashMap();
			User user = null;
			if(friend.getUserId().equals(uid)){
				user = userService.findOne(friend.getFriendId());
				mapObj.put("name", friend.getFriendName() == null?user.getNickName():friend.getFriendName());
			}else{
				user = userService.findOne(friend.getUserId());
				mapObj.put("name", friend.getUserName() == null?user.getNickName():friend.getUserName());
			}
			mapObj.put("id", user.getId());
			if(user.getHeadId() != null){
				Accessory acc = accessoryService.findOne(user.getHeadId());
				mapObj.put("head", acc.getPath() + acc.getName() + acc.getExt());
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
	public InterfaceVo updateFrienddName(String fName, Long fid, Long uid) {
		// TODO Auto-generated method stub
		InterfaceVo vo = new InterfaceVo();
		List<Object> params = Lists.newArrayList();
		params.add(uid);
		params.add(fid);
		params.add(uid);
		params.add(fid);
		String hql = "select x from Friends x where ((x.userId = ?1 and x.friendId = ?2) ";
		hql += " or (x.friendId = ?3 and x.userId = ?4)) ";
		List<Friends> friends = this.findAll(hql, null, params.toArray());
		if(friends.size() > 0){
			Friends fd = friends.get(0);
			if(fd.getUserId().equals(uid)){
				fd.setFriendName(fName);
			}else {
				fd.setUserName(fName);
			}
			this.update(fd);
			vo.setCodeEnum(Code.SUCCESS);
		}else{
			vo.setCode(-1);
			vo.setMessage("修改失败");
		}
		return vo;
	}

}
