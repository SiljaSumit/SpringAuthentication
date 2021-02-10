package com.auth.JwtAuth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth.JwtAuth.exceptions.CustomException;
import com.auth.JwtAuth.model.UserEntity;
import com.auth.JwtAuth.repository.UserRepository;
import com.auth.JwtAuth.securityConfig.JwtTokenProvider;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwdEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private JwtUserDetailService userDetailsService;


	@Override
	@Transactional
	public String signUp(String username, String password) throws CustomException {
		if (!userRepository.existsByUsername(username)) {
			String encodedPassword = passwdEncoder.encode(password);
			UserEntity user = new UserEntity(username, encodedPassword);
			userRepository.save(user);
		} else {
			throw new CustomException("Username already existed");
		}
		/* generate user token */
		UserDetails userDeails = userDetailsService.loadUserByUsername(username);
		return jwtTokenProvider.generateToken(userDeails);
	}
}
