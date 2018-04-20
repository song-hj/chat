package com.song.foundation.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.song.base.service.BaseService;
import com.song.core.tools.CommUtil;
import com.song.core.tools.InterfaceVo;
import com.song.core.tools.Md5Encrypt;
import com.song.core.tools.InterfaceVo.Code;
import com.song.foundation.entity.User;
import com.song.foundation.facade.service.UserFacadeService;
import com.song.foundation.service.UserService;

@Service
@Transactional
public class UserService extends BaseService<User,Long> implements UserFacadeService {
	

	@Override
	public InterfaceVo saveUser(User user) {
		// TODO Auto-generated method stub
		InterfaceVo vo = new InterfaceVo();
		if(!CommUtil.isNotNull(user.getUserName())){
			vo.setCode(-1);
			vo.setMessage("用户名不能为空");
			return vo;
		}
		User chUser = this.findBy("userName", user.getUserName());
		if(chUser != null){
			vo.setCode(-1);
			vo.setMessage("用户名已被注册");
			return vo;
		}
		if(!CommUtil.isNotNull(user.getPassword())){
			vo.setCode(-1);
			vo.setMessage("密码不能为空");
			return vo;
		}
		if(!CommUtil.isNotNull(user.getMobile())){
			vo.setCode(-1);
			vo.setMessage("手机号不能为空");
			return vo;
		}
		user.setSex("未知");
		user.setNickName(user.getUserName());
		user.setPassword(Md5Encrypt.md51(user.getPassword()));
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		user.setDeleteStatus(false);
		/** 保存用户 */
		this.save(user);
		vo.setCodeEnum(Code.SUCCESS);
		return vo;
	}

	@Override
	public User login(String userName,String password) {
		// TODO Auto-generated method stub
		User user = this.findBy("userName", userName);
		if(user == null){
			user = this.findBy("mobile", userName);
		}
		if(user == null || !user.getPassword().equals(Md5Encrypt.md51(password))){
			return null;
		}
		return user;
	}

	@Override
	public InterfaceVo updateUser(String name, String value, Long userId) {
		// TODO Auto-generated method stub
		InterfaceVo vo = new InterfaceVo();
		if(!CommUtil.isNotNull(name)){
			vo.setCode(-1);
			vo.setMessage("参数错误");
			return vo;
		}
		if(!CommUtil.isNotNull(value)){
			vo.setCode(-1);
			vo.setMessage("参数错误");
			return vo;
		}
		User user = this.findOne(userId);
		if("nickName".equals(name)){
			user.setNickName(value);
		}else if("mobile".equals(name)){
			user.setMobile(value);
		}else if("sex".equals(name)){
			user.setSex(value);
		}else if("address".equals(name)){
			user.setAddress(value);
		}else if("description".equals(name)){
			user.setDescription(value);
		}
		this.update(user);
		vo.setCodeEnum(Code.SUCCESS);
		return vo;
	}

	

}
