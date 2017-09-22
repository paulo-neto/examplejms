package com.pauloneto.spring.oauth.server.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Repository;

import com.pauloneto.spring.oauth.server.models.Perfil;

@Repository
public class PerfilRepository extends GenericRepositoryImpl<Perfil> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<Perfil> findAll() {
		List<Perfil> retorno = new ArrayList<>();
		StringBuilder consulta = new StringBuilder("select p from Perfil p");
		try{
			TypedQuery<Perfil> query = entityManager.createQuery(consulta.toString(), Perfil.class);
			retorno = query.getResultList();
		}catch (Exception e) {
			logger.error(ExceptionUtils.getRootCauseMessage(e),e);
		}
		return retorno;
	}

	public List<Perfil> consultarPerfisDoUsuario(Long codigoUsuario) {
		List<Perfil> retorno = new ArrayList<>();
		StringBuilder consulta = new StringBuilder("select u.perfis from Usuario u where u.id = :cdUsuario");
		try{
			Query query = entityManager.createQuery(consulta.toString());
			query.setParameter("cdUsuario", codigoUsuario);
			retorno = query.getResultList();
		}catch (Exception e) {
			logger.error(ExceptionUtils.getRootCauseMessage(e),e);
		}
		return retorno;
	}
}
