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
 * 好友表
 * @author songhj
 */
@Entity
@Table(name = "friends")
public class Friends extends BaseEntity<Long> implements Serializable {
	
	private static final long serialVersionUID = -7777171553392523423L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "friends")
	@TableGenerator(name = "friends", allocationSize = 1)
	private Long id;
	
	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;
	
	/**
	 * 好友ID
	 */
	@Column(name = "friend_id")
	private Long friendId;
	
	/**
	 * 好友备注
	 */
	@Column(name = "friend_name")
	private String friendName;
	
	
	/**
	 * 好友备注
	 */
	@Column(name = "user_name")
	private String userName;
	
	/**
	 * 是否同意
	 * 0:初始化
	 * 1：同意
	 * 2：拒绝
	 */
	@Column(name = "agree")
	private int agree;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getFriendId() {
		return friendId;
	}

	public void setFriendId(Long friendId) {
		this.friendId = friendId;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public int getAgree() {
		return agree;
	}

	public void setAgree(int agree) {
		this.agree = agree;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}

