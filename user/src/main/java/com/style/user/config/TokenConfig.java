package com.style.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * token相关配置
 *
 * @author leon
 * @date 2020-09-25 10:22:47
 */
@Configuration
public class TokenConfig {

	private static final String VERIFY_KEY = "MyStyle";

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setVerifierKey(VERIFY_KEY);
		jwtAccessTokenConverter.setSigningKey(VERIFY_KEY);
		return jwtAccessTokenConverter;
	}
}
