package com.kinduberre.chama.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private CustomLoginSuccessHandler successHandler;

	@Autowired
	private DataSource dataSource;

	@Value("${spring.queries.users-query}")
	private String usersQuery;

	@Value("${spring.queries.roles-query}")
	private String rolesQuery;

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().usersByUsernameQuery(usersQuery).authoritiesByUsernameQuery(rolesQuery)
				.dataSource(dataSource).passwordEncoder(bCryptPasswordEncoder);
	}
	
	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
	    return new CustomLogoutSuccessHandler();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll()
			.requestMatchers("/login").permitAll()
			.requestMatchers("/register").permitAll()
			.requestMatchers("/recover-password").permitAll()
			.requestMatchers("/reset-password").permitAll()
			.requestMatchers("/getSTKPushResult").permitAll()
			.requestMatchers("/home/**").hasAnyAuthority("SUPER_USER", "ADMIN_USER", "SITE_USER")
			.requestMatchers("/admin/**").hasAnyAuthority("SUPER_USER","ADMIN_USER")
			.anyRequest().authenticated()
			)
			// form login
			.formLogin((form) -> form
			.loginPage("/login")
			.failureUrl("/login?error=true")
			.successHandler(successHandler)
			.usernameParameter("email")
			.passwordParameter("password")
			)
			// logout
			.logout((logout) -> {
				try {
					logout
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.logoutSuccessHandler(logoutSuccessHandler())
					.and()
					.exceptionHandling()
					.accessDeniedPage("/access-denied");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
	    
	    // http....;
	    
	    return http.build();
	  }

	 @Bean
	 public WebSecurityCustomizer webSecurityCustomizer() {
		 return (web) -> web.ignoring().requestMatchers("/resources/**","/assets/**", "/static/**", "/css/**", "/js/**", "/img/**"); 
	 }


}
