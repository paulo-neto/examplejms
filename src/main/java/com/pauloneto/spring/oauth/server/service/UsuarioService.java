package com.pauloneto.spring.oauth.server.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pauloneto.spring.oauth.server.mesages.KeyMesages;
import com.pauloneto.spring.oauth.server.models.Perfil;
import com.pauloneto.spring.oauth.server.models.Usuario;
import com.pauloneto.spring.oauth.server.repository.RepositoryException;
import com.pauloneto.spring.oauth.server.repository.impl.PerfilRepository;
import com.pauloneto.spring.oauth.server.repository.impl.UsuarioRepository;
import com.pauloneto.spring.oauth.server.util.AssertUtils;
import com.pauloneto.spring.oauth.server.util.HashPasswordUtil;

@Service 
public class UsuarioService {

	@Inject
	private Logger logger;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PerfilRepository perfilRepository;
	
	public List<Usuario> listar(){
		return usuarioRepository.findAll();
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Usuario salvar(Usuario usuario) throws ServiceException{
		String hashedPwd = HashPasswordUtil.generateHash(usuario.getSenha());
		usuario.setSenha(hashedPwd);
		try{
			return usuarioRepository.save(usuario);
		}catch (RepositoryException e) {
			logger.error(ExceptionUtils.getRootCause(e), e);
			throw new ServiceException(e);
		}
	}


	public Usuario obterPorId(Long codigo) {
		return usuarioRepository.findId(Usuario.class, codigo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void remover(Long codigo) throws ServiceException {
		Usuario u = usuarioRepository.findId(Usuario.class, codigo);
		try {
			usuarioRepository.delete(u);
		} catch (RepositoryException e) {
			logger.error(ExceptionUtils.getRootCauseMessage(e),e);
			throw new ServiceException(KeyMesages.ERRO_GENERICO,ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Usuario atualizar(Long codigo, Usuario usuario) throws ServiceException {
		Usuario usuarioEncontrado = usuarioRepository.findId(Usuario.class, codigo);
		if (AssertUtils.isEmpty(usuarioEncontrado)) {
			String arg[] = {codigo.toString()};
			throw new ServiceException(KeyMesages.USUARIO_NAO_ENCONTRADO,arg);
		}
		try {
			BeanUtils.copyProperties(usuarioEncontrado, usuario);
			usuarioEncontrado.setId(codigo);
			return usuarioRepository.update(usuarioEncontrado);
		} catch (IllegalAccessException e) {
			logger.error(ExceptionUtils.getRootCauseMessage(e),e);
			throw new ServiceException(KeyMesages.ERRO_GENERICO,ExceptionUtils.getRootCauseMessage(e));
		} catch (InvocationTargetException e) {
			logger.error(ExceptionUtils.getRootCauseMessage(e),e);
			throw new ServiceException(KeyMesages.ERRO_GENERICO,ExceptionUtils.getRootCauseMessage(e));
		} catch (RepositoryException e) {
			logger.error(ExceptionUtils.getRootCauseMessage(e),e);
			throw new ServiceException(KeyMesages.ERRO_GENERICO,ExceptionUtils.getRootCauseMessage(e));
		}
	}


	@Transactional(propagation = Propagation.REQUIRED)
	public void adicionaPerfilaUsuario(Long codigoPerfil, Long codigoUsuario) throws ServiceException {
		Perfil perfilEncontrado = perfilRepository.findId(Perfil.class, codigoPerfil);
		if(AssertUtils.isEmpty(perfilEncontrado)){
			String arg[] = {codigoPerfil.toString()};
			throw new ServiceException(KeyMesages.PERFIL_NAO_ENCONTRADO,arg);
		}
		Usuario usuarioEncontrado = usuarioRepository.findId(Usuario.class, codigoUsuario);
		if(AssertUtils.isEmpty(usuarioEncontrado)){
			String arg[] = {codigoUsuario.toString()};
			throw new ServiceException(KeyMesages.USUARIO_NAO_ENCONTRADO,arg);
		}
		usuarioEncontrado.getPerfis().add(perfilEncontrado);
		try {
			usuarioRepository.update(usuarioEncontrado);
		} catch (RepositoryException e) {
			throw new ServiceException(KeyMesages.ERRO_GENERICO,ExceptionUtils.getRootCauseMessage(e));
		}
	}


	public List<Perfil> consultarPerfisDoUsuario(Long codigoUsuario){
		return perfilRepository.consultarPerfisDoUsuario(codigoUsuario);
	}
}
