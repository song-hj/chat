package com.song.core.tools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.song.core.security.support.SecurityUserHolder;
import com.song.foundation.entity.User;

public class LogUtils {

	/** 异常日志记录  */
	public static final Logger ERROR_LOG = LoggerFactory.getLogger("error");
	/** 访问日志记录  */
    public static final Logger ACCESS_LOG = LoggerFactory.getLogger("access");
    /** 用户登录退出日志记录  */
    public static final Logger SYS_USER = LoggerFactory.getLogger("sys-user");

    /**
     * 记录访问日志
     * [username][jsessionid][ip][accept][UserAgent][url][params][Referer]
     *
     * @param request
     */
    public static String logAccess(HttpServletRequest request) {
        String username = "Anonymous"; //匿名
        User user = SecurityUserHolder.getCurrentUser(request);
        if(user != null){
        	username = user.getUserName();
        }
        String jsessionId = request.getRequestedSessionId();
        String accept = request.getHeader("accept");
        String userAgent = request.getHeader("User-Agent");
        String url = request.getRequestURI();
        String params = getParams(request);
        String headers = getHeaders(request);

        StringBuilder s = new StringBuilder();
        s.append(getBlock(username));
        s.append(getBlock(CommUtil.getInstance().getIp(request)));
        s.append(getBlock(CommUtil.getInstance().getCity(request)));
        s.append(getBlock(jsessionId));
        s.append(getBlock(url));
        s.append(getBlock(params));
        s.append(getBlock(headers));
        s.append(getBlock(request.getHeader("Referer")));
        s.append(getBlock(accept));
        s.append(getBlock(userAgent));
        ACCESS_LOG.info(s.toString());
        return s.toString();
    }

    /**
     * 记录异常错误
     * 格式 [exception]
     *
     * @param message
     * @param e
     */
    public static void logError(String message, Throwable e,HttpServletRequest request) {
    	String username = "Anonymous"; //匿名
        User user = SecurityUserHolder.getCurrentUser(request);
        if(user != null){
        	username = user.getUserName();
        }
        StringBuilder s = new StringBuilder();
        s.append(getBlock("exception"));
        s.append(getBlock(username));
        s.append(getBlock(CommUtil.getInstance().getIp(request)));
        s.append(getBlock(CommUtil.getInstance().getCity(request)));
    	String jsessionId = request.getRequestedSessionId();
        String accept = request.getHeader("accept");
        String userAgent = request.getHeader("User-Agent");
        String url = request.getRequestURI();
        String params = getParams(request);
        String headers = getHeaders(request);
        s.append(getBlock(jsessionId));
        s.append(getBlock(url));
        s.append(getBlock(params));
        s.append(getBlock(headers));
        s.append(getBlock(request.getHeader("Referer")));
        s.append(getBlock(accept));
        s.append(getBlock(userAgent));
        s.append(getBlock(message));
        ERROR_LOG.error(s.toString(), e);
    }

    /**
     * 记录页面错误
     * 错误日志记录 [page/eception][username][statusCode][errorMessage][servletName][uri][exceptionName][ip][exception]
     *
     * @param request
     */
    public static void logPageError(HttpServletRequest request) {
    	String username = "Anonymous"; //匿名
        User user = SecurityUserHolder.getCurrentUser(request);
        if(user != null){
        	username = user.getUserName();
        }
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String message = (String) request.getAttribute("javax.servlet.error.message");
        String uri = (String) request.getAttribute("javax.servlet.error.request_uri");
        Throwable t = (Throwable) request.getAttribute("javax.servlet.error.exception");


        if (statusCode == null) {
            statusCode = 0;
        }

        StringBuilder s = new StringBuilder();
        s.append(getBlock(t == null ? "page" : "exception"));
        s.append(getBlock(username));
        s.append(getBlock(statusCode));
        s.append(getBlock(message));
        s.append(getBlock(CommUtil.getInstance().getIp(request)));

        s.append(getBlock(uri));
        s.append(getBlock(request.getHeader("Referer")));
        StringWriter sw = new StringWriter();

        while (t != null) {
            t.printStackTrace(new PrintWriter(sw));
            t = t.getCause();
        }
        s.append(getBlock(sw.toString()));
        ERROR_LOG.error(s.toString());

    }


    public static String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }



    protected static String getParams(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        return JSON.toJSONString(params);
    }


    public static String getHeaders(HttpServletRequest request) {
        Map<String, List<String>> headers = Maps.newHashMap();
        Enumeration<String> namesEnumeration = request.getHeaderNames();
        while(namesEnumeration.hasMoreElements()) {
            String name = namesEnumeration.nextElement();
            Enumeration<String> valueEnumeration = request.getHeaders(name);
            List<String> values = Lists.newArrayList();
            while(valueEnumeration.hasMoreElements()) {
                values.add(valueEnumeration.nextElement());
            }
            headers.put(name, values);
        }
        return JSON.toJSONString(headers);
    }



    /**
     * 记录格式 [ip][用户名][操作][错误消息]
     * <p/>
     * 注意操作如下：
     * loginError 登录失败
     * loginSuccess 登录成功
     * passwordError 密码错误
     * changePassword 修改密码
     * changeStatus 修改状态
     *
     * @param username
     * @param op
     * @param msg
     * @param args
     */
    public static void userLog(String username, String op, String msg,HttpServletRequest request, Object... args) {
        StringBuilder s = new StringBuilder();
        if(request != null){
        	s.append(LogUtils.getBlock(CommUtil.getInstance().getIp(request)));
        	s.append(LogUtils.getBlock(CommUtil.getInstance().getCity(request)));
        }
        s.append(LogUtils.getBlock(username));
        s.append(LogUtils.getBlock(op));
        s.append(LogUtils.getBlock(msg));

        SYS_USER.info(s.toString(), args);
    }
    
    public static void userLog(String username, String op, String msg,String ip,String area, Object... args) {
    	StringBuilder s = new StringBuilder();
		s.append(LogUtils.getBlock(ip));
		s.append(LogUtils.getBlock(area));
    	s.append(LogUtils.getBlock(username));
    	s.append(LogUtils.getBlock(op));
    	s.append(LogUtils.getBlock(msg));
    	
    	SYS_USER.info(s.toString(), args);
    }
    
    public static void userLog(String username, String op, String msg) {
    	StringBuilder s = new StringBuilder();
    	s.append(LogUtils.getBlock(username));
    	s.append(LogUtils.getBlock(op));
    	s.append(LogUtils.getBlock(msg));
    	
    	SYS_USER.info(s.toString());
    }
    
    
    
}
