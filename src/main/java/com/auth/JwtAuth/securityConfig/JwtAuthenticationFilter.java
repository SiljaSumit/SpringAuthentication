package com.auth.JwtAuth.securityConfig;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth.JwtAuth.dto.RequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			RequestDto loginRequest = new ObjectMapper().readValue(request.getInputStream(), RequestDto.class);
			if(loginRequest.getUsername()!=null && loginRequest.getPassword()!=null) {
				return this.getAuthenticationManager().authenticate(
						new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			} else {
				throw new AuthenticationServiceException("Username or password missing");
			}
		} catch (IOException e) {
	        throw new AuthenticationServiceException("Input mapping failed");
		}
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		super.unsuccessfulAuthentication(request, response, failed);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		UserDetails userDetails = (UserDetails) authResult.getPrincipal();
		String token = jwtTokenProvider.generateToken(userDetails);
		SecurityContextHolder.getContext().setAuthentication(authResult);
		response.addHeader(JwtTokenProvider.HEADER_STRING, token);
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
