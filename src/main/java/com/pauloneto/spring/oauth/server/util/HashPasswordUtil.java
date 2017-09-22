package com.pauloneto.spring.oauth.server.util;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

public abstract class HashPasswordUtil {

	private static Object salt;

	public static String generateHash(String password) {
		MessageDigestPasswordEncoder digestPasswordEncoder = getInstanceMessageDisterPassword();
		String encodePassword = digestPasswordEncoder.encodePassword(password, salt);
		return encodePassword;
	}

	private static MessageDigestPasswordEncoder getInstanceMessageDisterPassword() {
		MessageDigestPasswordEncoder digestPasswordEncoder = new MessageDigestPasswordEncoder("SHA-256");
		return digestPasswordEncoder;

	}

	public static boolean isPasswordValid(String password, String hashPassword) {
		MessageDigestPasswordEncoder digestPasswordEncoder = getInstanceMessageDisterPassword();
		return digestPasswordEncoder.isPasswordValid(hashPassword, password, salt);

	}

}
