package com.song.foundation.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.song.base.service.BaseService;
import com.song.foundation.entity.Accessory;
import com.song.foundation.entity.User;
import com.song.foundation.facade.service.AccessoryFacadeService;

/**
 * 图片
 * @author songhj
 *
 */
@Service
@Transactional
public class AccessoryService extends BaseService<Accessory, Long> implements AccessoryFacadeService {

	@Autowired
	private UserService userService;
	
	@Override
	public Accessory savePhoto(Accessory acc) {
		Date date = new Date();
		if(acc.getId()==null) {
			acc.setCreateTime(date);
			acc.setCreateUserId(acc.getCreateUserId());
		}
		acc.setUpdateUserId(acc.getUpdateUserId());
		acc.setUpdateTime(date);
		Accessory accessory = this.saveObj(acc);
		//保存头像
		User user = userService.findOne(acc.getCreateUserId());
		user.setHeadId(accessory.getId());
		userService.update(user);
		return accessory;
	}
}
