package com.example.demo.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource ds;

	@Bean
	public BCryptPasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(ds)
				.usersByUsernameQuery("select username, password, 'true' as enabled from users where username = ?")
				.authoritiesByUsernameQuery("select username, authority from roles where username = ?")
				.passwordEncoder(getEncoder());
		;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// Spring Security 4 automatically prefixes any role with ROLE_.
		http.authorizeRequests().antMatchers("/").permitAll().anyRequest().hasAnyRole("ADMIN", "USER").anyRequest()
				.authenticated().and().httpBasic();

	}

}