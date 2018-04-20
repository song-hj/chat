package com.song.foundation.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.song.base.entity.BaseEntity;


/**
 * 用户表
 * @author songhj
 */
@Entity
@Table(name = "user")
public class User extends BaseEntity<Long> implements Serializable {
	
	private static final long serialVersionUID = -7777171553392523423L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "user")
	@TableGenerator(name = "user", allocationSize = 1)
	private Long id;
	
	/**
	 * 用户名
	 */
	@Column(name = "user_name")
	private String userName;
	
	/**
	 * 密码
	 */
	@Column(name = "password")
	private String password;
	
	/**
	 * 昵称
	 */
	@Column(name = "nick_name")
	private String nickName;
	
	
	/**
	 * 手机
	 */
	@Column(name = "mobile")
	private String mobile;
	
	/**
	 * 性别
	 */
	@Column(name = "sex")
	private String sex;
	
	/**
	 * 头像
	 */
	@Column(name = "head_id")
	private Long headId;
	
	/**
	 * 地址
	 */
	@Column(name = "address")
	private String address;
	
	/**
	 * 个人签名
	 */
	@Column(name = "description")
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getHeadId() {
		return headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}

