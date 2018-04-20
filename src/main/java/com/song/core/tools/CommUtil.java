package com.song.core.tools;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.song.core.security.support.SpringUtils;
import com.song.core.tools.InterfaceVo.Code;
import com.song.foundation.entity.Accessory;

public class CommUtil {
	
	/**
	 * 获取当前日期前N天或者后N天的日期
	 * @param num
	 * @return
	 */
	public static Date getDate(int num){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, num);
		return cal.getTime(); 
	}
	
	public static Date formatShortDate(String s) {
		Date d = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = format.parse(s);
		} catch (Exception localException) {
		}
		return d;
	}
	
	public static String formatShortDate(Date date) {
		String d = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = format.format(date);
		} catch (Exception localException) {
		}
		return d;
	}
	
	
	public static Date formatLongDate(String s) {
		Date d = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			d = format.parse(s);
		} catch (Exception localException) {
		}
		return d;
	}
	
	public static String formatLongDate(Date date) {
		String d = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			d = format.format(date);
		} catch (Exception localException) {
		}
		return d;
	}
	
	public static String formatPrepaidRecharge(Object v) {
		if (v == null)
			return null;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(v);
	}
	
	public static String formatChinaDate(Object v) {
		if (v == null)
			return null;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(v);
	}
	
	public static String formatChinaTime(Object v) {
		if (v == null)
			return null;
		SimpleDateFormat df = new SimpleDateFormat("HHmmss");
		return df.format(v);
	}
	
	/**
	 * 非空判断
	 * @param obj
	 * @return
	 */
	public static boolean isNotNull(Object obj) {
		if ((obj != null) && (!obj.toString().equals(""))) {
			return true;
		}
		return false;
	}
	
	
	public static int null2Int(Object s) {
		int v = 0;
		if (s != null)
			try {
				v = Integer.parseInt(s.toString());
			} catch (Exception localException) {
			}
		return v;
	}

	public static float null2Float(Object s) {
		float v = 0.0F;
		if (s != null)
			try {
				v = Float.parseFloat(s.toString());
			} catch (Exception localException) {
			}
		return v;
	}

	public static double null2Double(Object s) {
		double v = 0.0D;
		if (s != null)
			try {
				v = Double.parseDouble(null2String(s));
			} catch (Exception localException) {
			}
		return v;
	}

	public static boolean null2Boolean(Object s) {
		boolean v = false;
		if (s != null)
			try {
				v = Boolean.parseBoolean(s.toString());
			} catch (Exception localException) {
			}
		return v;
	}

	public static String null2String(Object s) {
		return s == null ? "" : s.toString().trim();
	}

	public static Long null2Long(Object s) {
		Long v = Long.valueOf(-1L);
		if (s != null)
			try {
				v = Long.valueOf(Long.parseLong(s.toString()));
			} catch (Exception localException) {
			}
		return v;
	}
	
	/**
	 * 随机数
	 * @param length
	 * @return
	 */
	public static final String randomString(int length) {
		char[] numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				.toCharArray();
		if (length < 1) {
			return "";
		}
		Random randGen = new Random();
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	/**
	 * 随机数
	 * @param length
	 * @return
	 */
	public static final String randomInt(int length) {
		if (length < 1) {
			return null;
		}
		Random randGen = new Random();
		char[] numbersAndLetters = "0123456789".toCharArray();

		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(10)];
		}
		return new String(randBuffer);
	}
	
	
	/**
	 * 获取IP地址
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("x-real-ip");
		}
		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
    private static CommUtil instance = new CommUtil();
	
	public static CommUtil getInstance(){
		return instance;
	}
	
	/**
	 * 获取当前所在城市
	 * @param request
	 * @return
	 */
	public String getCity(HttpServletRequest request){
		String current_city = "";
        String current_ip = CommUtil.getInstance().getIp(request);
		try{
			current_city = getCityDetail(current_ip);
		}catch(Exception e){
		}
		return current_city;
	}
	
	
	/**
	 * 获取IP地址
	 * @param request
	 * @return
	 */
	public String getIp(HttpServletRequest request) {
        try {
        	if (request != null) {
	        	return CommUtil.getIpAddr(request);
        	}
        } catch (Exception e) {
            //ignore  如unit test
        }

        return "unknown";
    }
	
	public static String getCityDetail(String ip) {
		if("localhost".equals(ip) || ip.startsWith("127.0.0.1") || "0:0:0:0:0:0:0:1".equals(ip) || ip.startsWith("192.168")){
			return "深圳市";
		}
//		NamedCache blockingCache = CacheFactory.getCache("getCityDetail");
		if(ip.indexOf(",") > -1){
			ip = ip.split(",")[0];
		}
		String content = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();  
        String html = "";  
        try {  
            HttpGet httpget = null;  
            //创建HttpGet对象  
            httpget = new HttpGet("http://api.map.baidu.com/location/ip?ak=Q4haDC6GHi6dkX2SOIIwwqGb&ip="+ip+"&coor=bd09ll");  
            //使用execute方法发送HTTPGET请求，并返回HttpResponse对象  
            HttpResponse response = httpclient.execute(httpget);  
            //使用getEntity方法获得返回结果  
            HttpEntity entity = response.getEntity();  
            //读取response响应内容  
            html = EntityUtils.toString(entity,"GB2312");  
            //关闭底层流  
            EntityUtils.consume(entity);  
        } catch (IOException e) {  
            e.printStackTrace();
        } finally {  
            try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }  
        /** 
         * 利用Parser解析HTML，将标签<li>下的内容存储到nodeList里，并获取第一个<li>下的内容，用split分割后获取最终的结果是 日本 
         */  
        JSONObject jobj = JSON.parseObject(html);
        int status = jobj.getInteger("status");
        if(status == 0){
        	String address = jobj.getJSONObject("content").getJSONObject("address_detail").getString("city");  
        	return address;  
        }
        return "unknow";
	}
	
	
	public String getRequestInfo(HttpServletRequest request){
		StringBuffer info = new StringBuffer();
		String accept = request.getHeader("accept");
		String userAgent = request.getHeader("User-Agent");
		String url = request.getRequestURI();
		String params = LogUtils.getParams(request);
		String headers = LogUtils.getHeaders(request);
		info.append(LogUtils.getBlock(userAgent));
		info.append(LogUtils.getBlock(url));
		info.append(LogUtils.getBlock(accept));
		info.append(LogUtils.getBlock(params));
		info.append(LogUtils.getBlock(headers));
		return info.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/** 图片上传 */
	public static boolean isImg(String extend) {
		boolean ret = false;
		List<String> list = Lists.newArrayList();
		list.add("jpg");
		list.add("jpeg");
		list.add("bmp");
		list.add("gif");
		list.add("png");
		list.add("tif");
		for (String s : list) {
			if (s.equals(extend))
				ret = true;
		}
		return ret;
	}
	
	public static boolean deleteFile(String path) {
		boolean flag = false;
		File file = new File(path);

		if ((file.isFile()) && (file.exists())) {
			file.delete();
			flag = true;
		}
		return flag;
	}
	
	public static InterfaceVo saveImage(HttpServletRequest request,Long userId) {
		// TODO Auto-generated method stub
		InterfaceVo vo = new InterfaceVo();
		Accessory accessory = new Accessory();
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		    CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
		    
		    String fileName = file.getFileItem().getName();
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			float size = file.getSize();
			BufferedImage bis = ImageIO.read(file.getInputStream());
			int width = bis.getWidth();
			int height = bis.getHeight();
			String path = "/picture/" + userId + "/";
			if(userId.intValue() == -1){
				path = "/picture/other/";
			}
		    
		    String production_server = FileConfig.getValue(FileConfig.production_server);
		    //本地服务
		    if("true".equals(production_server)){
				String newName = CommUtil.randomString(18);
		        String filePath = FileConfig.getValue(FileConfig.image_server_path) + path;
				//验证路径是否存在，不存在创建
				File chekFile = new File(filePath);
				if (!chekFile.exists()){       
					chekFile.mkdirs();
				}
				//生成输出地址URL  
		        String relPath = filePath + newName + "." + ext;
		        while(true){
		        	chekFile = new File(relPath);
		        	if(chekFile.exists()){
		        		newName = CommUtil.randomString(18);
		        		relPath = filePath + newName + "." + ext;
		        	}else{
		        		break; 
		        	}
		        }
		        
		        BufferedImage image = ImageIO.read(file.getInputStream());
		        ImageIO.write(image, ext, new File(relPath));
		        //图片名称
		        accessory.setName(newName);
		    }else{
		    	InputStream input = file.getInputStream();
		    	int flen = (int) file.getSize();
	    		byte[] strBuffer = new byte[flen];
	    		input.read(strBuffer, 0, flen);
	    		String picture = Base64.encodeBase64String(strBuffer);
	    		
	    		Map<String,String> params = new HashMap<String,String>();
	    		params.put("path", path);
	    		params.put("picture", picture);
	    		params.put("ext", ext);
	    		String resultStr = HttpUtil.post(request.getScheme() + "://" + request.getServerName() + "/file/uploadImageFile", params);
	    		JSONObject json = JSONObject.parseObject(resultStr);
	    		if(json.getInteger("code") == 1){
	    			accessory.setName(json.getString("name"));
	    		}
		    }
		    //保存图片信息
	        accessory.setPath(path);
	        accessory.setExt("." + ext);
	        accessory.setSize(size);
	        accessory.setWidth(width);
	        accessory.setHeight(height);
	        accessory.setCreateTime(new Date());
	        
	        vo.setCodeEnum(Code.SUCCESS);
	        vo.getOutData().put("obj", accessory);
		} catch (Exception e) {
			// TODO: handle exception
			vo.setCodeEnum(-1, e.getMessage());
		}
		return vo;
	}
	
	/**
	 * 微信上传图片
	 * @param request
	 * @param userId
	 * @return
	 */
	public static InterfaceVo saveImage2(HttpServletRequest request,Long userId) {
		// TODO Auto-generated method stub
		InterfaceVo vo = new InterfaceVo();
		Accessory accessory = new Accessory();
		try {
			String fileName = UUID.randomUUID().toString() + request.getParameter("name");
			String filePath =  request.getSession().getServletContext().getRealPath("/") + FileConfig.getValue(FileConfig.local_upload_path);
			request.setCharacterEncoding("UTF8");
			File file = new File(filePath + "/" + fileName);
			FileOutputStream fos = new FileOutputStream(file);
			try {
				FileCopyUtils.copy(request.getInputStream(),  new FileOutputStream(file));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				fos.close();
			}
			DiskFileItem item =  new DiskFileItem("file","",false,null,Integer.parseInt(String.valueOf(file.length())),new File(filePath));
			FileCopyUtils.copy(new FileInputStream(file),item.getOutputStream());
			
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			String path = "/picture/" + userId + "/";
			if(userId.intValue() == -1){
				path = "/picture/other/";
			}
		    
			String newName = CommUtil.randomString(18);
			filePath = FileConfig.getValue(FileConfig.image_server_path) + path;
			//验证路径是否存在，不存在创建
			File chekFile = new File(filePath);
			if (!chekFile.exists()){       
				chekFile.mkdirs();
			}
			//生成输出地址URL  
			String relPath = filePath + newName + "." + ext;
			while(true){
				chekFile = new File(relPath);
				if(chekFile.exists()){
					newName = CommUtil.randomString(18);
					relPath = filePath + newName + "." + ext;
				}else{
					break; 
				}
			}
			//写入图片
			item.write(new File(relPath));
			
			item.delete();
			CommUtil.deleteFile(file.getPath());
			
			//图片名称
			accessory.setName(newName);
		    //保存图片信息
	        accessory.setPath(path);
	        accessory.setExt("." + ext);
	        accessory.setSize(file.length());
	        accessory.setCreateTime(new Date());
	        
	        vo.setCodeEnum(Code.SUCCESS);
	        vo.getOutData().put("obj", accessory);
		} catch (Exception e) {
			// TODO: handle exception
			vo.setCodeEnum(-1, e.getMessage());
		}
		return vo;
	}
	
	
	public static Map<String,Object> saveImage3(String path,String picture,String ext) {
		Map<String,Object> map = Maps.newHashMap();
		try {
			String filePath = FileConfig.getValue(FileConfig.image_server_path) + path;
			//验证路径是否存在，不存在创建
			File chekFile = new File(filePath);
			if (!chekFile.exists()){       
				chekFile.mkdirs();
			}
			//验证名字是否存在
			String fileName = CommUtil.randomString(18);
			String relPath = filePath + "/" + fileName + "." + ext;
			while(true){
	        	chekFile = new File(relPath);
	        	if(chekFile.exists()){
	        		fileName = CommUtil.randomString(18);
	        		relPath = filePath + "/" + fileName + "." + ext;
	        	}else{
	        		break; 
	        	}
	        }
			byte[] bytes = Base64.decodeBase64(picture);
    		InputStream inputStream = new ByteArrayInputStream(bytes);
    		BufferedImage image = ImageIO.read(inputStream);
    		ImageIO.write(image, ext, new File(relPath));
    		
    		map.put("code",1);
    		map.put("name",fileName);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code",-1);
    		map.put("message",e.getMessage());
		}
		return map;
	}
	
}
