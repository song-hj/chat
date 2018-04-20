package com.song.foundation.facade.service;


import com.song.base.facade.service.BaseFacadeService;
import com.song.core.tools.InterfaceVo;
import com.song.foundation.entity.User;

public interface UserFacadeService extends BaseFacadeService<User, Long> {
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public InterfaceVo saveUser(User user);
	
	/**
	 * 用户登录
	 */
	public User login(String userName,String password);
	
	/**
	 * 修改资料
	 * @param name
	 * @param value
	 * @param userId
	 * @return
	 */
	public InterfaceVo updateUser(String name, String value, Long userId);
	

}
