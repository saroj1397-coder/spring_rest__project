package com.springrest.springrest.jwt;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class JWTSecurityConfiguartion {

	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
		
		http.sessionManagement(
				session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);
		
		//http.formLogin(withDefaults());
		http.httpBasic();
		http.csrf().disable();
		
		http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
		
		return http.build();
		
	}
	
	  @Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }


	  @Bean
	    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
	        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//	        userDetailsManager.setPasswordEncoder(passwordEncoder); // Use setPasswordEncoder instead of setPasswordEncoder

	        // Add users and authorities as needed
	        
	        var user= User.withUsername("saroj1")
					//.password("{noop}dummy")
	        		.password("dummy")
	        		.passwordEncoder(str->passwordEncoder().encode(str))
					.roles("USER")
					.build();
			
			var admin= User.withUsername("admin1")
					//.password("{noop}dummy")
					.password("dummy")
	        		.passwordEncoder(str->passwordEncoder().encode(str))
					.roles("ADMIN")
					.build();

			
			jdbcUserDetailsManager.createUser(user);
			jdbcUserDetailsManager.createUser(admin);
	        return jdbcUserDetailsManager;
	    }
	  
		 @Value("${spring.datasource.driver-class-name}")
		    private String driverClassName;

		    @Value("${spring.datasource.url}")
		    private String url;

		    @Value("${spring.datasource.username}")
		    private String username;

		    @Value("${spring.datasource.password}")
		    private String password;
		    
		    @Value("${spring.jpa.hibernate.ddl-auto}")
		    private String hibernateddlAuto;
	
	 @Bean
	    public DataSource dataSource() throws ScriptException, SQLException {
	        DriverManagerDataSource dataSource = new DriverManagerDataSource();
	        dataSource.setDriverClassName(driverClassName);
	        dataSource.setUrl(url);
	        dataSource.setUsername(username);
	        dataSource.setPassword(password);
	        
	        
	        // Initialize database with SQL scripts
	        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
	        databasePopulator.addScript(new ClassPathResource("sql/mysql-schema.sql"));
	       // databasePopulator.addScript(new ClassPathResource("sql/mysql-data.sql"));
	        databasePopulator.populate(dataSource.getConnection());
	        
	        return dataSource;
	    }
	 
	 @Bean
	 public KeyPair keyPair() {
	KeyPairGenerator keyPairGenerator;
	try {
		keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		return keyPairGenerator.generateKeyPair();
	} catch (Exception e) {
		throw new RuntimeException(e);
			}
	
	 }
	 
	 @Bean
	 public RSAKey rsaKey(KeyPair keyPair) {
		return new RSAKey
				.Builder((RSAPublicKey)keyPair.getPublic())
				 .privateKey(keyPair.getPrivate())
				 .keyID(UUID.randomUUID().toString())
				 .build();
				 
		 
	 }
	 
	 @Bean
	 public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
		var jwkSet= new JWKSet(rsaKey);
		
		return (jwkSelector,context)-> jwkSelector.select(jwkSet); 
	 }
	 
	 @Bean
	 JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
		 return NimbusJwtDecoder
				 .withPublicKey(rsaKey.toRSAPublicKey())
				 .build();
	 }
	 
	 @Bean
	 public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		 return new NimbusJwtEncoder(jwkSource);
	 }
}
