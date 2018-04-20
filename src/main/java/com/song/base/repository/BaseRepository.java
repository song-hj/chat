package com.song.base.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.song.base.search.PageRequest;

/**
 * 自定义方法
 * @author songhj
 *
 * @param <M>
 * @param <ID>
 */
@NoRepositoryBean
public interface BaseRepository<M, ID extends Serializable> extends JpaRepository<M, ID> {
	
	public void delete(ID[] ids);
	
	public List<M> findAll(String hql, PageRequest page, Object... params);
	
	public List<Object> findAllS(String hql, PageRequest page, Object... params);
	
	public List<M> findAll(PageRequest page, Map<String,Object> params);
	
	public long count(String hql, Object... params);
	
}
