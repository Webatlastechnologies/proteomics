package com.vkhatri.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.vkhatri.service.UserDetailService;

@Configuration
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailService userService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http
	      .csrf().disable()
	      .authorizeRequests()
	       .antMatchers("/api/**").authenticated()
	       .antMatchers( "/login/**").permitAll()
	       .anyRequest().authenticated()
	        .and()
	      .formLogin().loginPage("/login").defaultSuccessUrl("/greeting",true).usernameParameter("username").passwordParameter("password").successForwardUrl("/greeting")
	        .and()
	      .logout().logoutSuccessUrl("/login?logout")
	        .and()
	      .httpBasic();
	      //.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//working code
		/*auth.inMemoryAuthentication().
		withUser("vishal1").password("vishal1").roles("USER").and().
		withUser("vishal2").password("vishal2").roles("USER", "ADMIN");*/

		auth.userDetailsService(this.userService);
	}
}
