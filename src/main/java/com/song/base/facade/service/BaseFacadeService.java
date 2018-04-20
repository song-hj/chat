package com.song.base.facade.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.song.base.entity.BaseEntity;
import com.song.base.search.PageRequest;

/**
 * @author songhj
 *
 * @param <M>
 * @param <ID>
 */
public interface BaseFacadeService<M extends BaseEntity<ID>, ID extends Serializable> {

	/** 添加  */
	public void save(M entity);

	public M saveObj(M entity);
	
	public M saveAndFlush(M entity);
	
	public List<M> save(List<M> entitys);
	
	

	/** 删除 */
	public void delete(ID id);
	
	public void delete(M entity);
	
	public void delete(List<M> entitys);
	
	public void deleteAllInBatch(List<M> entitys);
	
	public void deleteAllInBatch();
	
	public void deleteAll();
	
	
	/** 修改  */
	public boolean update(M entity);
	
	
	/** 刷新 */
	public void flush();
	
	
	
	/** 查询  */
	public Page<M> findAll(Pageable pageable);
	
	public List<M> findAll(Sort sort);
	
	public List<M> findAll(Map<String,Object> params);
	
	public M findBy(String propertyName, Object value);
	
	public M findOne(ID id);
	
	public List<M> findAll(ID[] ids);
	
	public List<M> findAll();

	/** 分页查询  */
	public List<M> findAll(String hql, PageRequest page, Object... params);
	
	public List<M> findAll(PageRequest page, Map<String,Object> params);

	
	
	/** 统计实体总数  */
	public long count();
	
	public long count(String hql, Object... params);
	
	
	
	/** 校验 */
	public boolean exists(ID id);

}
