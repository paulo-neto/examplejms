/**
 * 
 */
package com.pauloneto.spring.oauth.server.repository.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.pauloneto.spring.oauth.server.mesages.KeyMesages;
import com.pauloneto.spring.oauth.server.models.TO;
import com.pauloneto.spring.oauth.server.repository.IGenericRepository;
import com.pauloneto.spring.oauth.server.repository.RepositoryException;

/**
 * Classe que implementa os métodos de mínimos de manipulação com o Banco de
 * Dados.
 * 
 * @author Paulo Antonio
 * @since quinta-feira 03 11 2016
 */
public abstract class GenericRepositoryImpl<T> implements IGenericRepository<T>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6163696283184654122L;
	
	@PersistenceContext(unitName = "persistenceUnit")
	protected EntityManager entityManager;

	@Inject
	protected Logger logger;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.correios.ppi.repository.interfaces.IPPIGenericDao#save(java.lang.
	 * Object)
	 */
	@Override
	public T save(T entity) throws RepositoryException {
		try {
			entityManager.persist(entity);
			return entity;
		} catch (Exception e) {
			throw new RepositoryException(KeyMesages.ERRO_SALVAR, ExceptionUtils.getRootCauseMessage(e));
		}
	}
	
	@Override
	public List<T> saveList(List<T> lista)throws RepositoryException{
		int contador = 1;
		List<T> persistedList = new ArrayList<T>();
		for(T t: lista){
			entityManager.persist(t);
			//a cada 50 Entidades, sincroniza e limpa o cache
			if (contador % 50 == 0) {
				entityManager.flush();
				entityManager.clear();
	        }
			contador++;
			persistedList.add(t);
		}
		return persistedList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.correios.ppi.repository.interfaces.IPPIGenericDao#delete(java.lang
	 * .Object)
	 */
	@Override
	public void delete(T entity) throws RepositoryException {
		try {
			entityManager.remove(entity);
		} catch (Exception e) {
			throw new RepositoryException(KeyMesages.ERRO_DELETAR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.correios.ppi.repository.interfaces.IPPIGenericDao#update(java.lang
	 * .Object)
	 */
	@Override
	public T update(T entity) throws RepositoryException {
		try {
			entity = entityManager.merge(entity);
			return entity;
		} catch (Exception e) {
			throw new RepositoryException(KeyMesages.ERRO_ATUAIZAR, ExceptionUtils.getRootCauseMessage(e));
		}
	}
	
	public List<T> updateList(List<T> lista)throws RepositoryException{
		int contador = 1;
		List<T> mergedList = new ArrayList<T>();
		for(T t: lista){
			entityManager.merge(t);
			//a cada 50 Entidades, sincroniza e limpa o cache
			if (contador % 50 == 0) {
				entityManager.flush();
				entityManager.clear();
	        }
			contador++;
			mergedList.add(t);
		}
		return mergedList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.correios.ppi.repository.interfaces.IPPIGenericDao#findId(java.lang
	 * .Class, Long)
	 */
	@Override
	public T findId(Class<T> clazz, Long entityID){
		return entityManager.find(clazz, entityID);
	}
	
	private TO obter(T entity)throws Exception{
		Long sequencial = getId(entity);
		if (sequencial != null) {
		    return (TO) entityManager.find(entity.getClass(), 
			    sequencial);
		}
		return null;
	}
	
	/**
	 * Objetivo:
     * Dada uma entidade, obter o valor contido no atributo marcado 
     * como ID (@Id).
     * 
     * Funcionamento:
     * Através reflection obtém a entidade marcada com a annotation @Id e o 
     * seu respectivo valor
     *
     * @author Paulo Antonio
	 * @param entidade
	 * @return
	 * @throws Exception
	 */
	protected Long getId(T entidade) throws Exception {
		Field[] fields = entidade.getClass().getDeclaredFields();
		for (Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				field.setAccessible(true);
				return field.getLong(entidade);
			}
		}
		return null;
	}
	
	public T getRefencia(Class<T> clazz, Long entityID){
		return this.entityManager.getReference(clazz, entityID);
	}
	
	public EntityManager geEntityManager(){
		return this.entityManager;
	}
}
