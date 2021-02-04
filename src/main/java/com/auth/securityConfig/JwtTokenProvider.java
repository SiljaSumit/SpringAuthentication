package com.auth.securityConfig;

import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final long JWT_TOKEN_VALIDITY = 86400000;

	@Value("${jwt.secret}")
	private String secretKey;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String generateToken(UserDetails userDetails) {
	
		String username = userDetails.getUsername();
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("auth", userDetails.getAuthorities().stream().map(s -> new SimpleGrantedAuthority(s.getAuthority()))
				.filter(Objects::nonNull).collect(Collectors.toList()));
		return doGenerateToken(claims, userDetails.getUsername());
	
	}

	public String resolveToken(HttpServletRequest req)  {
		String bearerToken = req.getHeader(HEADER_STRING);
		if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
			return bearerToken.substring(7);
		}
		LOGGER.warn("token is not given");
		return null;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException ex) {
			throw new AuthenticationServiceException("Expired or invalid JWT token", ex);
		}
	}

	public String getUsername(String token) {
		try {
			return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
		} catch (JwtException ex) {
			throw new AuthenticationServiceException("Problem occured while parsing the token to get subject",ex);
		}
	}

	private String doGenerateToken(Claims claims, String subject) {
		Date now = new Date(System.currentTimeMillis());
		Date validity = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY);
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(now).setExpiration(validity)
				.signWith(SignatureAlgorithm.HS512, secretKey).compact();

	}
}
