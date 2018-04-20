package com.song.core.tools;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

public class InterfaceVo implements Serializable {
	
	private static final long serialVersionUID = 7084486896006737258L;

	/**
	 * 成功／失败代码
	 */
	private int code;
	
	/**
	 * 成功／失败代码说明
	 */
	private String message;
	
	
	private Map outData = Maps.newHashMap();
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	public int getCode() {
		return code;
	}
	

	public Map getOutData() {
		return outData;
	}

	public void setOutData(Map outData) {
		this.outData = outData;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public void setCodeEnum(Code code) {
		this.code = code.code;
		this.message = code.message;
		
	}

	public void setCodeEnum(int code,String message) {
		this.code = code;
		this.message = message;
	}

	
	public enum Code {
		ERROR(-1,"自定义问题"), 
		USER_NO_LOGIN(0,"用户未登录"),
		SUCCESS(1,"成功"),
		USERNAME_ERROR(2,"用户名未注册"),
		USER_EXISTS(3,"用户已存在"),
		PASSWORD_ERROR(4,"密码错误"),
		MOBILE_IDENTIFYING_ERROR(5,"手机验证码错误"),
		IMG_IDENTIFYING_ERROR(6,"图片验证码错误"),
		
		JSON_ERROR(7,"接口数据转换异常,格式不对"),
		UPLOAD_FILE_ERROR(8,"文件上传出错"),
		WEIXING_TOKET_INVALID(9,"微信验证失效"),
		BIND_MOBILE(10,"须要绑写手机号"),
		USER_EXISTS_BIND_MOBILE(11,"手机号已被绑定"),
		
		SAVE_WAIT_INSURE(102,"保存成功,待投保"),
		WAITE_RESULT(100,"等待结果返回");
	    
	    private int code;
	    
	    private String message;
	    
	    private Code(int code,String message){
	    	this.code=code;
	    	this.message = message;
	    }
	    
	    public int getCode(){
	    	return code;
	    }
	    
	}
	
}
