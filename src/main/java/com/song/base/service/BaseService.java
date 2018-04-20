package com.song.base.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.song.base.entity.BaseEntity;
import com.song.base.repository.BaseRepository;
import com.song.base.search.PageRequest;

@Transactional
public abstract class BaseService<M extends BaseEntity<ID>, ID extends Serializable> {

	protected boolean hasInit = false;
	protected boolean isFullCached = false;
	
    public boolean isFullCached() {
		return isFullCached;
	}

	public void setFullCached(boolean isFullCached) {
		this.isFullCached = isFullCached;
	}

	private BaseRepository<M, ID> baseRepository;
    
    @Autowired
    public void setBaseRepository(BaseRepository<M, ID> baseRepository) {
        this.baseRepository = baseRepository;
    }
    
    public BaseRepository<M, ID> getBaseRepository(){
    	return this.baseRepository;
    }
    
	//初始化依赖注入
    public void init() {
		this.hasInit = true;
	}


	
	
	
	
	
	
	
	
	
	
	/** 添加  */
	public void save(M entity){
		baseRepository.save(entity);
	}

	public M saveObj(M entity){
		return baseRepository.save(entity);
	}

	public M saveAndFlush(M entity){
		return baseRepository.saveAndFlush(entity);
	}

	public List<M> save(List<M> entitys){
		return baseRepository.save(entitys);
	}





	/** 删除 */
	public void delete(ID id){
		baseRepository.delete(id);
	}

	public void delete(M entity){
		baseRepository.delete(entity);
	}

	public void delete(List<M> entitys){
		baseRepository.delete(entitys);
	}

	public void deleteAllInBatch(List<M> entitys){
		baseRepository.deleteInBatch(entitys);
	}

	public void deleteAllInBatch(){
		baseRepository.deleteAllInBatch();
	}

	public void deleteAll(){

		baseRepository.deleteAll();
	}


	/** 修改  */
	public boolean update(M entity){
		M m1 = baseRepository.save(entity);
		if(m1.equals(entity)){
			return false;
		}
		return true;
	}


	/** 刷新 */
	public void flush(){
		baseRepository.flush();
	}


	/** 查询  */
	public Page<M> findAll(Pageable pageable){
		return baseRepository.findAll(pageable);
	}

	public List<M> findAll(Sort sort){
		Iterable<M> elements = baseRepository.findAll(sort);
		if(elements == null){
			return Lists.newArrayList();
		}
		return Lists.newArrayList(elements); 
	}

	public M findBy(String propertyName, Object value){
		Map<String,Object> params = Maps.newHashMap();
		params.put(propertyName, value);
		List<M> list = findAll(null, params);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	public M findOne(ID id){
		return baseRepository.findOne(id);
	}

	public List<M> findAll(ID[] ids){
		Iterable<M> elements = baseRepository.findAll(Arrays.asList(ids));
		if(elements == null){
			return Lists.newArrayList();
		}
		return Lists.newArrayList(elements);
	}

	public List<M> findAll(){
		Iterable<M> elements = baseRepository.findAll();
		if(elements == null){
			return Lists.newArrayList();
		}
		return Lists.newArrayList(elements);
	}
	
	public List<M> findAll(Map<String,Object> params){
		return baseRepository.findAll(null, params);
	}

	/** 分页查询  */
	public List<M> findAll(String hql, PageRequest page, Object... params){
		return baseRepository.findAll(hql, page, params);
	}
	
	public List<Object> findAllS(String hql, PageRequest page, Object... params){
		return baseRepository.findAllS(hql, page, params);
	}
	
	public List<M> findAll(PageRequest page, Map<String,Object> params){
		return baseRepository.findAll(page, params);
	}
	


	/** 统计实体总数  */
	public long count(){
		return baseRepository.count();
	}

	public long count(String hql, Object... params){
		return baseRepository.count(hql, params);
	}





	/** 校验 */
	public boolean exists(ID id){
		return baseRepository.exists(id);
	}

	

}
