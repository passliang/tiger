package com.style.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Collections;


/**
 * 认证配置
 *
 * @author leon
 * @date 2020-09-21 13:39:31
 *
                           _
                           \"-._ _.--"~~"--._
                            \   "            ^.    ___
                            /                  \.-~_.-~
                     .-----'     /\/"\ /~-._      /
                    /  __      _/\-.__\L_.-/\     "-.
                   /.-"  \    ( ` \_o>"<o_/  \  .--._\
                  /'      \    \:     "     :/_/     "`
                          /  /\ "\    ~    /~"
                          \ I  \/]"-._ _.-"[
                       ___ \|___/ ./    l   \___   ___
                  .--v~   "v` ( `-.__   __.-' ) ~v"   ~v--.
               .-{   |     :   \_    "~"    _/   :     |   }-.
              /   \  |           ~-.,___,.-~           |  /   \
             ]     \ |                                 | /     [
             /\     \|     :                     :     |/     /\
            /  ^._  _K.___,^                     ^.___,K_  _.^  \
           /   /  "~/  "\                           /"  \~"  \   \
          /   /    /     \ _          :          _ /     \    \   \
        .^--./    /       Y___________l___________Y       \    \.--^.
        [    \   /        |        [/    ]        |        \   /    ]
        |     "v"         l________[____/]________j  -Row   }r"     /
        }------t          /                       \       /`-.     /
        |      |         Y                         Y     /    "-._/
        }-----v'         |         :               |     7-.     /
        |   |_|          |         l               |    / . "-._/
        l  .[_]          :          \              :  r[]/_.  /
         \_____]                     "--.             "-.____/
                                            "MyStyle"
                                                        ---Row

        **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private ClientDetailsService clientDetailsService;
	@Autowired
	private TokenStore tokenStore;
	@Autowired
	private JwtAccessTokenConverter jwtAccessTokenConverter;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints
				//允许token请求调用的请求类型
				.allowedTokenEndpointRequestMethods(HttpMethod.POST, HttpMethod.GET)
				//认证管理器
				.authenticationManager(authenticationManager)
				//token
				.tokenServices(tokenServices())
				//认证码
				.authorizationCodeServices(authorizationCodeServices())
		;
	}

	@Bean
	public AuthorizationServerTokenServices tokenServices() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		//认证管理器
		tokenServices.setAuthenticationManager(authenticationManager);
		//客户端信息
		tokenServices.setClientDetailsService(clientDetailsService);
		//jwt token存储
		tokenServices.setTokenStore(tokenStore);
		//token增强
		TokenEnhancerChain tokenEnhancer = new TokenEnhancerChain();
		tokenEnhancer.setTokenEnhancers(Collections.singletonList(jwtAccessTokenConverter));
		tokenServices.setTokenEnhancer(tokenEnhancer);
		//支持刷新token
		tokenServices.setSupportRefreshToken(true);
		//token 有效期 2小时
		tokenServices.setAccessTokenValiditySeconds(2 * 60 * 60);
		// 刷新token有效期 7天
		tokenServices.setRefreshTokenValiditySeconds(7 * 24 * 60 * 60);
		return tokenServices;
	}

	/**
	 * 授权码 这里使用内存存储
	 *
	 * @return AuthorizationCodeServices
	 */
	@Bean
	public AuthorizationCodeServices authorizationCodeServices() {
		return new InMemoryAuthorizationCodeServices();
	}

	/**
	 * 自定义 加密器
	 *
	 * @return PasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 客户端的信息
	 *
	 * @param clients clients
	 * @throws  Exception
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//配置内存中的 client
		clients.inMemory()
				.withClient("admin")
				.secret(new BCryptPasswordEncoder().encode("MyStyle"))
				//客户端允许的授权模式
				.authorities("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
				//允许的授权范围
				.scopes("all","read","write")
				//验证回调地址
				.redirectUris("https://www.baidu.com")
				.and()
				.withClient("user")
				.secret(new BCryptPasswordEncoder().encode("MyStyle"))
				//客户端允许的授权模式
				.authorities("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
				//允许的授权范围
				.scopes("read")
				//验证回调地址
				.redirectUris("https://www.baidu.com")
				.and()
				.build();
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) {
		security
				//oauth/token_key是公开
				.tokenKeyAccess("permitAll()")
				//oauth/check_token公开
				.checkTokenAccess("permitAll()")
				//表单认证（申请令牌）
				.allowFormAuthenticationForClients()
		;
	}


}
