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
 * 信息明细表
 * @author songhj
 *
 */
@Entity
@Table(name = "message_info")
public class MessageInfo extends BaseEntity<Long> implements Serializable {
	
	private static final long serialVersionUID = 658077706867712030L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "message_info")
	@TableGenerator(name = "message_info", allocationSize = 1)
	private Long id;
	
	/**
	 * 消息
	 */
	@Column(name = "content")
	private String content;
	
	
	/**
	 * 消息状态
	 * 0：初始化状态
	 * 1：自己删除了记录
	 * 2：朋友删除了记录
	 */
	@Column(name = "message_status")
	private int messageStatus;
	
	/**
	 * 是否已读取信息
	 */
	@Column(name = "read_status")
	private boolean readStatus;
	
	/**
	 * 好友ID
	 */
	@Column(name = "message_id")
	private Long messageId;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public int getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(int messageStatus) {
		this.messageStatus = messageStatus;
	}

	public boolean isReadStatus() {
		return readStatus;
	}

	public void setReadStatus(boolean readStatus) {
		this.readStatus = readStatus;
	}

}
