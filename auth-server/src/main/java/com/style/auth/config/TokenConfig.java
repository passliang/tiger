package com.style.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * token 配置
 *
 * @author leon
 * @date 2020-09-21 13:35:51
 */
@Configuration
public class TokenConfig {

	private static final String SIGN_KEY = "MyStyle";

	/**
	 * jwt token 存储
	 *
	 * @return TokenStore
	 */
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	/**
	 * jwt tokenConvert
	 *
	 * @return JwtAccessTokenConverter
	 */
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey(SIGN_KEY);
		return jwtAccessTokenConverter;
	}

}
