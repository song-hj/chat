package com.song.foundation.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.song.base.service.BaseService;
import com.song.core.tools.CommUtil;
import com.song.foundation.entity.User;
import com.song.foundation.entity.UserLoginLog;
import com.song.foundation.facade.service.UserLoginLogFacadeService;

@Service
@Transactional
public class UserLoginLogService extends BaseService<UserLoginLog,Long> implements UserLoginLogFacadeService {

	/**
	 * 登录或退出日志记录
	 */
	@Override
	public void savaUserLog(HttpServletRequest request, User user, int type) {
		// TODO Auto-generated method stub
		UserLoginLog userLoginLog = new UserLoginLog();
		userLoginLog.setIp(CommUtil.getInstance().getIp(request));
		userLoginLog.setSessionId(request.getSession().getId());
		userLoginLog.setStatus(type);
		userLoginLog.setAgent(CommUtil.getInstance().getRequestInfo(request));
		userLoginLog.setUserId(user.getId());
		userLoginLog.setAddress(CommUtil.getInstance().getCity(request));
		userLoginLog.setDeleteStatus(false);
		userLoginLog.setCreateInfo(user.getId(), new Date());
		this.save(userLoginLog);
	}

}
