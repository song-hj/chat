package com.song.core.tools;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * 模板引擎
 * @author songhj
 *
 */
@Component
public class TemplateEngine {

	@Autowired
	private VelocityEngine velocityEngine;

	
	public String generateWithTemplate(String templateName, Map<String,Object> map) {
		try {
			return VelocityEngineUtils.mergeTemplateIntoString(
					this.velocityEngine, templateName, "UTF-8", map);
		} catch (VelocityException e) {
			e.printStackTrace();
		}
		return "";
	}

	@SuppressWarnings("deprecation")
	public String generateWithString(String content, Map<String,Object> map) {
		try {
			StringWriter writer = new StringWriter();
			VelocityEngineUtils.mergeTemplate(this.velocityEngine, content,
					map, writer);
			return writer.toString();
		} catch (VelocityException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * content:  您的验证码是${code}
	 * map:      {"core":1234}
	 * @param content
	 * @param map
	 * @return
	 */
	public String getTemplateString(String content, Map<String,Object> map) {
		VelocityContext context = new VelocityContext();
		if(map != null){
			for(String key : map.keySet()){
				context.put(key,map.get(key));
			}
		}
		StringWriter stringWriter = new StringWriter();
		Velocity.init();
		Velocity.evaluate(context, stringWriter, "template.log",content);
		
		return stringWriter.toString().trim();
	}
}