package com.example.demo.res;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true,jsr250Enabled = true) 
//it should be enabled if we want to use @secured or @rolesallowed annotation on class or method
public class SecurityConfigurer extends WebSecurityConfigurerAdapter{

	PasswordEncoder encoder;
	@Autowired
	 public SecurityConfigurer(PasswordEncoder encoder) {
		 this.encoder=encoder;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		//.csrf().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.POST,"/api/**").hasAnyAuthority(Permissions.RESOURCE_WRITE.getPermission())
		.antMatchers(HttpMethod.PUT,"/api/**").hasAnyAuthority(Permissions.RESOURCE_WRITE.getPermission())
		.antMatchers(HttpMethod.DELETE,"/api/**").hasAnyAuthority(Permissions.RESOURCE_WRITE.getPermission())
		.antMatchers(HttpMethod.GET,"/api/**").hasAnyRole(Roles.ADMIN.name(),Roles.USER.name())
		.anyRequest()
		.authenticated()
		.and()
		.httpBasic();
	}
	@Override
	@Bean
	protected UserDetailsService userDetailsService() {
		UserDetails james=User.builder().username("james").password(encoder.encode("james"))
				//.roles(Roles.ADMIN.name())
				.authorities(Roles.ADMIN.authorities())
				.build();
		UserDetails anna=User.builder().username("anna").password(encoder.encode("anna"))
				//.roles(Roles.USER.name())
				.authorities(Roles.USER.authorities())
				.build();
		return new InMemoryUserDetailsManager(james,anna);
	}
}
