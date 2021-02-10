package com.auth.JwtAuth.securityConfig;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth.JwtAuth.service.JwtUserDetailService;

/**
 * Processes a HTTP request's BASIC authorization headers, putting the result
 * into the SecurityContextHolder.
 *
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private JwtUserDetailService jwtUserDetailService;

	public JwtAuthorizationFilter(AuthenticationManager authManager,
			AuthenticationEntryPoint authenticationEntryPoint) {
		super(authManager, authenticationEntryPoint);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = jwtTokenProvider.resolveToken(request);
		try {
			if (token != null && jwtTokenProvider.validateToken(token)) {
				Authentication auth = getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (AuthenticationException ex) {
			SecurityContextHolder.clearContext();
			this.getAuthenticationEntryPoint().commence(request, response, ex);
			return;
		}

		chain.doFilter(request, response);
	}

	private Authentication getAuthentication(String token) throws AuthenticationException {
		String userName = jwtTokenProvider.getUsername(token);
		UserDetails userDetails = jwtUserDetailService.loadUserByUsername(userName);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

	}

}
