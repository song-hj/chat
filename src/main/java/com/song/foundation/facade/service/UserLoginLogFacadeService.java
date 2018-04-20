package com.song.foundation.facade.service;

import javax.servlet.http.HttpServletRequest;

import com.song.base.facade.service.BaseFacadeService;
import com.song.foundation.entity.User;
import com.song.foundation.entity.UserLoginLog;

public interface UserLoginLogFacadeService extends BaseFacadeService<UserLoginLog, Long> {
	
	public void savaUserLog(HttpServletRequest request, User user, int type);
}
