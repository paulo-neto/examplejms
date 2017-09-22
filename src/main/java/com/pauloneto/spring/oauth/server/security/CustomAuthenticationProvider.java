package com.pauloneto.spring.oauth.server.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.pauloneto.spring.oauth.server.mesages.KeyMesages;
import com.pauloneto.spring.oauth.server.mesages.MesagesProperties;
import com.pauloneto.spring.oauth.server.models.Usuario;
import com.pauloneto.spring.oauth.server.repository.impl.UsuarioRepository;
import com.pauloneto.spring.oauth.server.util.AssertUtils;
import com.pauloneto.spring.oauth.server.util.HashPasswordUtil;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        password = HashPasswordUtil.generateHash(password);
        Usuario usuEncontrado = usuarioRepository.buscarPorLoginSenha(name, password);
        if (!AssertUtils.isEmpty(usuEncontrado)) {
        	Collection<? extends GrantedAuthority> authorities = usuEncontrado.getPerfis();
            return new UsernamePasswordAuthenticationToken(name, password, authorities);
        }else {
			throw new UsernameNotFoundException(MesagesProperties.getInstancia().getMesage(KeyMesages.BAD_CREDENCIALS));
        }
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
