package com.song.base.repository.support;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.QueryLookupStrategy;

import com.song.base.repository.BaseRepository;

/**
 * 建一个自定义的FactoryBean去替代默认的工厂类 
 * @author songhj
 *
 * @param <R>
 * @param <M>
 * @param <ID>
 */
public class SimpleBaseRepositoryFactoryBean<R extends JpaRepository<M, ID>,M, ID extends Serializable> extends JpaRepositoryFactoryBean<R, M, ID> {

	public SimpleBaseRepositoryFactoryBean() {
		
    }
	
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new SimpleBaseRepositoryFactory<M, ID>(entityManager);
	}
	
	
	private static class SimpleBaseRepositoryFactory<M, ID extends Serializable> extends JpaRepositoryFactory {

		private EntityManager entityManager;

		public SimpleBaseRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}
		
		@SuppressWarnings("unchecked")
		protected Object getTargetRepository(RepositoryInformation metadata) {
	        Class<?> repositoryInterface = metadata.getRepositoryInterface();
	        if (isBaseRepository(repositoryInterface)) {
	            JpaEntityInformation<M, ID> entityInformation = getEntityInformation((Class<M>) metadata.getDomainType());
	            SimpleBaseRepository<M, ID> repository = new SimpleBaseRepository<M, ID>(entityInformation, entityManager);
	            
	        }
	        return super.getTargetRepository(metadata);
	    }  

		
		
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
	        if (isBaseRepository(metadata.getRepositoryInterface())) {
	            return SimpleBaseRepository.class;
	        }
	        return super.getRepositoryBaseClass(metadata);
	    }

	    private boolean isBaseRepository(Class<?> repositoryInterface) {
	        return BaseRepository.class.isAssignableFrom(repositoryInterface);
	    }
	    
	    @Override
	    protected QueryLookupStrategy getQueryLookupStrategy(QueryLookupStrategy.Key key) {
	        return super.getQueryLookupStrategy(key);
	    }
	}
}



