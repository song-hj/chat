package com.song.view.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.song.core.tools.CommUtil;
import com.song.core.tools.InterfaceVo;
import com.song.core.tools.LogUtils;
import com.song.core.tools.InterfaceVo.Code;
import com.song.foundation.entity.User;
import com.song.foundation.facade.service.UserFacadeService;
import com.song.foundation.service.UserLoginLogService;


@Controller
public class LoginViewController {
	
	
	@Autowired
	private UserFacadeService userFacadeService;
	
	@Autowired
	private UserLoginLogService userLoginLogService;
	
	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping("/system/login")  
    @ResponseBody  
    public InterfaceVo login(HttpServletRequest request,HttpServletResponse response,
    		String userName,String password) {
		InterfaceVo vo = new InterfaceVo();
		if(!CommUtil.isNotNull(userName)){
			vo.setCode(-1);
			vo.setMessage("用户名不能为空");
			return vo;
		}
		if(!CommUtil.isNotNull(password)){
			vo.setCode(-1);
			vo.setMessage("密码不能为空");
			return vo;
		}
		User user = userFacadeService.login(userName, password);
		if(user != null){
			request.getSession().setAttribute("user", user);
			vo.setCodeEnum(Code.SUCCESS);
			userLoginLogService.savaUserLog(request, user, 1);
			LogUtils.userLog(user.getUserName(), "", "用户登录成功", request, 
					CommUtil.getInstance().getRequestInfo(request));
		}else{
			vo.setCodeEnum(Code.ERROR);
		}
        return vo;
	}
	
	
	/**
	 * 退出登录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/system/logout")  
    @ResponseBody  
    public InterfaceVo loginOut(HttpSession session) {
		InterfaceVo vo = new InterfaceVo();
		session.invalidate();
		vo.setCode(1);
		vo.setMessage("退出成功");
		return vo;
	}
}
