/**
 * 
 */
package com.pauloneto.spring.oauth.server.repository;

import java.util.List;

/**
 * Interface que define os métodos de mínimos de manipulação com o
 * Banco de Dados. 
 * @author Paulo Antonio
 */
public interface IGenericRepository<T> {

	/**
	 * Salva uma Entidade.
	 * @param entity
	 * @return
	 * @throws PPIException
	 */
	public T save(T entity) throws RepositoryException;
	
	/**
	 * Salva uma Lista de Entidade.
	 * @param entity
	 * @return
	 * @throws PPIException
	 */
	public List<T> saveList(List<T> lista)throws RepositoryException;

	/**
	 * Deleta uma Entidade.
	 * @param entity
	 * @return
	 * @throws PPIException
	 */
	public void delete(T entity)throws RepositoryException;

	/**
	 * Altera uma Entidade.
	 * @param entity
	 * @return
	 * @throws PPIException
	 */
	public T update(T entity)throws RepositoryException;

	/**
	 * Consulta uma Entidade pelo id.
	 * @param entity
	 * @return
	 * @throws PPIException
	 */
	public T findId(Class<T> clazz,Long entityID);
}
