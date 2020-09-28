package com.style.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


/**
 * @author leon
 * @date 2020-09-21 13:45:17
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/oauth/**").permitAll()
			.anyRequest().authenticated();
		;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		//内存模式下创建的用户
		//权限列表
		UserDetails admin = User.withUsername("admin")
				.password(new BCryptPasswordEncoder().encode("123456"))
				//角色
				.roles("ADMIN", "DEVELOPER")
				//权限
				.authorities("get","update")
				.build();

		//开发者
		UserDetails developer = User.withUsername("developer")
				.password(new BCryptPasswordEncoder().encode("123456"))
				//角色
				.roles("DEVELOPER")
				//权限
				.authorities("get")
				.build();


		InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
		userDetailsManager.createUser(admin);
		userDetailsManager.createUser(developer);
		return userDetailsManager;
	}

}
