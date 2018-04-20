package com.song.base.repository.support;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.song.base.search.PageRequest;
import com.song.core.tools.CommUtil;
import com.song.base.repository.BaseRepository;

/**
 * 自定义repository的方法接口实现类,该类主要提供自定义的公用方法
 * @author songhj
 *
 * @param <M>
 * @param <ID>
 */
public class SimpleBaseRepository <M, ID extends Serializable> extends SimpleJpaRepository<M, ID> implements BaseRepository<M, ID> {

	@SuppressWarnings("unused")  
	private Logger logger = LoggerFactory.getLogger(SimpleBaseRepository.class); 

	public static final String LOGIC_DELETE_ALL_QUERY_STRING = "update %s x set x.deleteStatus = true where x in (?1)";
	public static final String DELETE_ALL_QUERY_STRING = "delete from %s x where x in (?1)";
	public static final String FIND_QUERY_STRING = "select x from %s x ";
	public static final String COUNT_QUERY_STRING = "select count(x) from %s x ";


	private EntityManager em;	private JpaEntityInformation<M, ID> entityInformation;

	private Class<M> entityClass;
	private String entityName;
	private String idName;



	public SimpleBaseRepository(JpaEntityInformation<M, ID> entityInformation, EntityManager entityManager) {
		// TODO Auto-generated constructor stub
		super(entityInformation, entityManager);
		this.entityInformation = entityInformation;
		this.entityClass = entityInformation.getJavaType();
		this.entityName = entityInformation.getEntityName();
		this.idName = entityInformation.getIdAttributeNames().iterator().next();

		this.em = entityManager;
	}

	/**************************** 自定义方法实现        ***********************************/
	@Override
	public void delete(ID[] ids) {
		// TODO Auto-generated method stub
		if (ArrayUtils.isEmpty(ids)) {
			return;
		}
		List<M> models = Lists.newArrayList();
		for (ID id : ids) {
			M model = null;
			try {
				model = entityClass.newInstance();;
			} catch (Exception e) {
				throw new RuntimeException("batch delete " + entityClass + " error", e);
			}
			try {
				BeanUtils.setProperty(model, idName, id);
			} catch (Exception e) {
				throw new RuntimeException("batch delete " + entityClass + " error, can not set id", e);
			}
			models.add(model);
		}
		deleteInBatch(models);
	}


	/***	 * @param hql	 * @param pageable null表示不分页	 * @param params	 * @param <M>	 * @return	 */
	@Override
	public List<M> findAll(final String hql, final PageRequest page, final Object... params) {
		// TODO Auto-generated method stub
		Query query = em.createQuery(hql);		setParameters(query, params);		if (page != null) {			query.setFirstResult(page.getPageNo() * page.getPageSize());			query.setMaxResults(page.getPageSize());		}
		@SuppressWarnings("unchecked")
		List<M> result = query.getResultList();
		return result;
	}
	
	
	@Override
	public List<Object> findAllS(final String hql, final PageRequest page, final Object... params) {
		// TODO Auto-generated method stub
		Query query = em.createQuery(hql);
		setParameters(query, params);
		if (page != null) {
			query.setFirstResult(page.getPageNo() * page.getPageSize());
			query.setMaxResults(page.getPageSize());
		}
		@SuppressWarnings("unchecked")
		List<Object> result = query.getResultList();
		return result;
	}

	@Override
	public List<M> findAll(PageRequest page, Map<String, Object> params) {
		// TODO Auto-generated method stub
		StringBuffer hql = new StringBuffer(String.format(FIND_QUERY_STRING, entityName));
		List<Object> objs = setParameters(hql, params);
		Query query = em.createQuery(hql.toString());
		if(objs != null){
			setParameters(query, objs.toArray());
		}
		if(page != null){
			query.setFirstResult(page.getPageNo());
			query.setMaxResults(page.getPageSize());
		}
		List<M> list = query.getResultList();
		return list;
	}


	@Override
	public long count(String hql, Object... params) {
		// TODO Auto-generated method stub
		Query query = em.createQuery(hql);		setParameters(query, params);
		return (long)query.getSingleResult();
	}


	/**	 * 按顺序设置Query参数	 * @param query	 * @param params	 */
	@Transactional	private List<Object> setParameters(StringBuffer hql, Map<String, Object> params) {
		List<Object> objs = null;		if (params != null) {
			objs = Lists.newArrayList();
			int i = 0;			for (String key : params.keySet()) {
				i ++;
				if(i == 1){
					hql.append(" where x." + key + " = ?" + i);
				}else{
					hql.append(" and x." + key + " = ?" + i);
				}
				objs.add(params.get(key));
			}		}
		return objs;	}
	
	@Transactional
	private void setParameters(Query query, Object[] params) {
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i+1, params[i]);
			}
		}
	}

}