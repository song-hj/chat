package com.song.view.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.song.core.security.support.SecurityUserHolder;
import com.song.core.tools.CommUtil;
import com.song.core.tools.FileConfig;
import com.song.core.tools.InterfaceVo;
import com.song.foundation.entity.Accessory;
import com.song.foundation.entity.User;
import com.song.foundation.facade.service.AccessoryFacadeService;


@Controller
public class IndexViewController {


	/**
	 * 主页
	 * @param request
	 * @param response
	 * @param domain
	 * @return
	 */
	@RequestMapping(value = { "/" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ModelAndView indexDomain(HttpServletRequest request, HttpServletResponse response,String domain){
		ModelAndView view = new ModelAndView();
		view.setViewName("/message/index");
		return view;
	}


	/**
	 * 服务器错误
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/500" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Map<String,Object> error500(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map = Maps.newHashMap();
		map.put("error", 500);
		return map;
	}

	/**
	 * 找不到页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/404" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Map<String,Object> error404(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map = Maps.newHashMap();
		map.put("error", 404);
		return map;
	}
}
