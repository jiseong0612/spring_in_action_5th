package tacos.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	
	/**
	 * Http 보안을 구성 한다
	 */
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests()
		.antMatchers("/design", "/orders")
		.access("hasRole('ROLE_USER')")
		.antMatchers("/", "/**").access("permitAll")
		.and()
		.httpBasic();
	}
	
	/**
	 * 사용자 인증 정보를 구성한다.
	 */
	public void configure(AuthenticationManagerBuilder auth)throws Exception{
		auth
		.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery(
		    "select username, password, enabled from users where username=?")
		.authoritiesByUsernameQuery(
			"select username, authority from authorities where username=?")
		.passwordEncoder(new NoEncodingPasswordencoder());//비밀번호 암호화
		/*
		 * auth.inMemoryAuthentication() .withUser("user1") .password("{noop}password1")
		 * .authorities("ROLE_USER") .and() .withUser("user2")
		 * .password("{noop}password2") .authorities("ROLE_USER");
		 */
	}
}
