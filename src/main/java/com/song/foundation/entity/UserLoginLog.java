package com.song.foundation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.song.base.entity.BaseEntity;


/**
 * 用户登录和退出日志
 * @author songhj
 *
 */
@Entity
@Table(name = "user_login_log")
public class UserLoginLog extends BaseEntity<Long> {

	private static final long serialVersionUID = -2593081292701133960L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "user_login_log")
	@TableGenerator(name = "user_login_log", allocationSize = 1)
	private Long id;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * IP地址
	 */
	@Column(name = "ip")
	private String ip;
	
	/**
	 * 地理位置
	 */
	@Column(name = "address")
	private String address;
	
	/**
	 * -1退出、1登录
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 请求的会话ID
	 */
	@Column(name = "session_id")
	private String sessionId;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 代理
	 */
	@Lob
	@Column(name = "agent", columnDefinition = "LongText")
	private String agent;
	
	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	

}