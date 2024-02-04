package com.springrest.springrest.basic;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class BasicAuthSecurityConfiguartion {

	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
		
		http.sessionManagement(
				session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);
		
		//http.formLogin(withDefaults());
		http.httpBasic();
		http.csrf().disable();
		return http.build();
		
	}
}
