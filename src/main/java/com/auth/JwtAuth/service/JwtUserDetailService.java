package com.auth.JwtAuth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth.JwtAuth.model.UserEntity;
import com.auth.JwtAuth.repository.UserRepository;

@Service
public class JwtUserDetailService implements UserDetailsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtUserDetailService.class);
	
	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByUsername(name);
		LOGGER.info("User-> {}  ", user);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + name);
		}
		String userRole = user.getRole();
		return new User(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(userRole));
	}
}
