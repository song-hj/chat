package com.song.core.security.support;

import javax.servlet.http.HttpServletRequest;


import com.song.foundation.entity.User;

/**
 * 获取当前用户
 * @author songhj
 *
 */
public class SecurityUserHolder {
	
	public static User getCurrentUser(HttpServletRequest request) {
		User user = null;
		if(request.getSession(false) != null){
			user = request.getSession(false).getAttribute("user") != null ? (User) request
					.getSession(false).getAttribute("user") : null;
		}else if(request.getSession(true) != null){
			user = request.getSession(false).getAttribute("user") != null ? (User) request
					.getSession(false).getAttribute("user") : null;
		}
		return user;
	}
}
