package com.song.foundation.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.song.base.entity.BaseEntity;

/**
 * 信息表
 * @author songhj
 *
 */
@Entity
@Table(name = "message")
public class Message extends BaseEntity<Long> implements Serializable {
	
	private static final long serialVersionUID = 1339650767197091650L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "message")
	@TableGenerator(name = "message", allocationSize = 1)
	private Long id;
	
	/**
	 * 对话唯一标志
	 */
	@Column(name = "mark")
	private String mark;
	
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
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
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
	
	
}
