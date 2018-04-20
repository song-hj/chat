package com.song.base.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;


/**
 * 抽象实体基类，提供统一的ID，和相关的基本功能方法 如果是oracle请参考{@link BaseOracleEntity}
 * @author songhj
 */
@MappedSuperclass
public abstract class BaseEntity<ID extends Serializable> extends AbstractEntity<ID> implements Serializable {

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_time")
	private Date updateTime;
	
	@Column(name="create_user_id")
	private Long createUserId;
	
	@Column(name="update_user_id")
	private Long updateUserId;

	@Column(name="delete_status")
	private boolean deleteStatus;

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public boolean isDeleteStatus() {
		return this.deleteStatus;
	}
	
	public boolean getDeleteStatus() {
		return this.deleteStatus;
	}

	public void setDeleteStatus(boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}


	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public void setCreateInfo(Long userId,Date time){
		this.createUserId = userId;
		this.updateUserId = userId;
		this.updateTime = time;
		this.createTime = time;
	}
	
	public void setUpdateInfo(Long userId,Date time){
		this.updateUserId = userId;
		this.updateTime = time;
	}
}
