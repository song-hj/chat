package com.song.core.filter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.song.core.security.support.SecurityUserHolder;
import com.song.core.tools.CommUtil;
import com.song.core.tools.LogUtils;
import com.song.foundation.entity.User;
import com.song.foundation.entity.UserLoginLog;
import com.song.foundation.facade.service.UserLoginLogFacadeService;

/**
 * 日志过滤器
 * @author songhj
 */
@Component
public class LogFilter implements Filter {
	
	@Autowired
	private UserLoginLogFacadeService userLoginLogFacadeService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) request;
		//防止中文乱码
		req.setCharacterEncoding("utf-8");
		//访问日志输出
		LogUtils.logAccess(req);
		
		String url = req.getRequestURI();
		
		if(url.indexOf("/system/logout") != -1){
			//退出日志记录
			User user = SecurityUserHolder.getCurrentUser(req);
			if(user!=null) {
				//保存日志
				userLoginLogFacadeService.savaUserLog(req, user, -1);
				LogUtils.userLog(user.getUserName(), CommUtil.getInstance().getRequestInfo(req), "用户退出成功",
						CommUtil.getInstance().getIp(req), CommUtil.getInstance().getCity(req), "");
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}