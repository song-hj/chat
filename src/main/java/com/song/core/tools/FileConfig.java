package com.song.core.tools;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;

import com.song.core.security.support.SpringUtils;

public class FileConfig implements InitializingBean {
	private static Environment env = null;
	
	private String fileLocation = null;
	
	private static FileConfig fileConfig = null;
	
	private FileConfig(){
		
	}
	
	/**
	 * 存放本地图片路径
	 */
	public static final String local_upload_path="local_upload_path";
	
	
	/**
	 * 图片服务器路径
	 */
	public static final String image_server_path = "image_server_path";
	
	/**
	 * 是否生产环境
	 */
	public static final String production_server = "production_server";

	public static String getValue(String name) {
		return env.getProperty(name);
	}
	
	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		env = SpringUtils.getEnvironment(fileLocation);
	}

	public static FileConfig getFileConfig() {
		if(fileConfig==null) {
			fileConfig= new FileConfig();
		}
		return fileConfig;
	}
	
	
	
	
}
