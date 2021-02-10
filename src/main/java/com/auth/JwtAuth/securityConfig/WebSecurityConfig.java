package com.auth.JwtAuth.securityConfig;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.auth.JwtAuth.exceptions.ExceptionResponse;
import com.auth.JwtAuth.service.JwtUserDetailService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

	@Autowired
	private JwtUserDetailService jwtUserDetailService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(jwtUserDetailService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public AuthenticationEntryPoint jwtAuthenticationEntryPoint() throws Exception {
		return (request, response, authException) -> {
			SecurityContextHolder.clearContext();
			LOGGER.error("Authorization exception: {}", authException.getMessage());
			
			String errorResponse = new ExceptionResponse.ExceptionResponseBuilder().withStatus(UNAUTHORIZED)
					.withDetail("authorization failed").withMessage(authException.getMessage())
					.withError_code(UNAUTHORIZED.value()).atTime(LocalDateTime.now(ZoneOffset.UTC)).build().toJson();
			 response.setStatus(UNAUTHORIZED.value());
			 response.setContentType(APPLICATION_JSON_VALUE);
			 response.getWriter().write(errorResponse);
		};
	}

	@Bean
	public SimpleUrlAuthenticationFailureHandler jwtAuthenticationFailureHandler() throws Exception {
		return new SimpleUrlAuthenticationFailureHandler() {
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				SecurityContextHolder.clearContext();
				LOGGER.error("Authentication exception: {}", authException.getMessage());
				
				String errorResponse = new ExceptionResponse.ExceptionResponseBuilder().withStatus(UNAUTHORIZED)
						.withDetail("authentication failed").withMessage(authException.getMessage())
						.withError_code(UNAUTHORIZED.value()).atTime(LocalDateTime.now(ZoneOffset.UTC)).build().toJson();
				 response.setStatus(UNAUTHORIZED.value());
				 response.setContentType(APPLICATION_JSON_VALUE);
				 response.getWriter().write(errorResponse);
			}
		};
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
		jwtAuthenticationFilter
				.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/users/signin", "POST"));
		jwtAuthenticationFilter.setAuthenticationFailureHandler(jwtAuthenticationFailureHandler());
		jwtAuthenticationFilter.setAuthenticationManager(authenticationManager());
		return jwtAuthenticationFilter;
	}

	@Bean
	public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
		return new JwtAuthorizationFilter(authenticationManager(), jwtAuthenticationEntryPoint());
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// Disable CSRF (cross site request forgery)
		httpSecurity.csrf().disable();

		// No session will be created or used by spring security
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Entry points
		httpSecurity.authorizeRequests()//
				.antMatchers(HttpMethod.POST, "/users/signup").permitAll()
				.anyRequest().authenticated()
				.and()
				.addFilter(jwtAuthenticationFilter())
				.addFilter(jwtAuthorizationFilter());
	}

}
