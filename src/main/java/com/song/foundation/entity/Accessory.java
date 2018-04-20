package com.song.foundation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.song.base.entity.BaseEntity;

/**
 * 图片
 * @author songhj
 *
 */
@Entity
@Table(name = "accessory")
public class Accessory extends BaseEntity<Long> {

	private static final long serialVersionUID = 7789890191041214842L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "accessory")
	@TableGenerator(name = "accessory", allocationSize = 1)
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
	 * 附件名称
	 */
	private String name;
	/**
	 * 附件路径
	 */
	private String path;
	/**
	 * 附件大小
	 */
	private float size;
	/**
	 * 图片宽度
	 */
	private int width;
	/**
	 * 图片高度
	 */
	private int height;
	/**
	 * 附件扩展名
	 */
	private String ext;

	/**
	 * 备注
	 */
	private String info;

	/**
	 * 用户id
	 */
	@Transient
	private User user;

	/**
	 * 是否获取后缀
	 */
	@Transient
	private Boolean derive;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getDerive() {
		return derive;
	}

	public void setDerive(Boolean derive) {
		this.derive = derive;
	}
}
