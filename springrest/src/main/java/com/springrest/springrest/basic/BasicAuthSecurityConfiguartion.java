package com.springrest.springrest.basic;

import static org.springframework.security.config.Customizer.withDefaults;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class BasicAuthSecurityConfiguartion {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
		
		http.sessionManagement(
				session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);
		
		//http.formLogin(withDefaults());
		http.httpBasic();
		http.csrf().disable();
		
		
		return http.build();
		
	}
	
//	@Bean
//	UserDetailsService userDetailsService() {
//		
//		var user= User.withUsername("saroj")
//				.password("{noop}dummy")
//				.roles("USER")
//				.build();
//		
//		var admin= User.withUsername("admin")
//				.password("{noop}dummy")
//				.roles("ADMIN")
//				.build();
//		return new InMemoryUserDetailsManager(user, admin);
//	}
	
//	@Bean
//	DataSource dataSource() {
//		
//		return new EmbeddedDatabaseBuilder()
//				.setType(EmbeddedDatabaseType.H2)
//				.addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
//				.build();
//	}
	
	  @Bean
	    public BCryptPasswordEncoder bCryptPasswordEncoder() {
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
	        		.passwordEncoder(str-> bCryptPasswordEncoder().encode(str))
					.roles("USER")
					.build();
			
			var admin= User.withUsername("admin1")
					//.password("{noop}dummy")
					.password("dummy")
	        		.passwordEncoder(str-> bCryptPasswordEncoder().encode(str))
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
}
