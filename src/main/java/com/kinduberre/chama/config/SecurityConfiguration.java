package com.kinduberre.chama.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.kinduberre.chama.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private CustomLoginSuccessHandler successHandler;

	
	@Bean
	LogoutSuccessHandler logoutSuccessHandler() {
	    return new CustomLogoutSuccessHandler();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//	    http
//        	.csrf(AbstractHttpConfigurer::disable)
//	        .authorizeHttpRequests((auth) -> auth
//			.requestMatchers("/login").permitAll()
//			.requestMatchers("/register").permitAll()
//			.requestMatchers("/recover-password").permitAll()
//			.requestMatchers("/reset-password").permitAll()
//			.requestMatchers("/home/**").hasAnyRole("SUPER_USER", "ADMIN_USER", "SITE_USER")
//			.requestMatchers("/admin/**").hasAnyRole("SUPER_USER","ADMIN_USER")
//			.anyRequest().authenticated()
//			)
//	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//			// form login
//			.formLogin((form) -> form
//			.loginPage("/login")
//			.failureUrl("/login?error=true")
//			.successHandler(successHandler)
//			.usernameParameter("email")
//			.passwordParameter("password")
//			)
//			// logout
//			.logout((logout) -> 
//					logout
//					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//					.logoutSuccessHandler(logoutSuccessHandler()))
//			.exceptionHandling().accessDeniedPage("/access-denied");
//	    
//	    // http....;
		
		http.authorizeRequests()
        .requestMatchers("/anonymous*")
        .anonymous()
        .requestMatchers("/login*").permitAll()
        .requestMatchers("/register").permitAll()
		.requestMatchers("/recover-password").permitAll()
		.requestMatchers("/reset-password").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .loginProcessingUrl("/login")
        .usernameParameter("email")
		.passwordParameter("password")
        .successHandler(successHandler)
        .failureUrl("/login?error=true")
        .and()
        .logout()
        .deleteCookies("JSESSIONID")
        .and()
        .rememberMe()
        .key("uniqueAndSecret")
        .tokenValiditySeconds(86400)
        .and()
        .csrf()
        .disable()
        .logout((logout) -> 
		logout
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessHandler(logoutSuccessHandler()))
        .exceptionHandling().accessDeniedPage("/access-denied");;

	    
	    return http.build();
	  }

	 @Bean
	 WebSecurityCustomizer webSecurityCustomizer() {
		 return (web) -> web.ignoring().requestMatchers("/resources/**","/assets/**", "/static/**", "/css/**", "/js/**", "/img/**"
				 ); 
	 }
	 
	 @Autowired
	 public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	    auth
	            .userDetailsService(userDetailsService)
	            .passwordEncoder(bCryptPasswordEncoder);
	 }

}
