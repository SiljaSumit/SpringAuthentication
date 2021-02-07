package com.auth.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.dto.RequestDto;
import com.auth.dto.ResponseDto;
import com.auth.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/hello")
	public ResponseEntity<String> sayHi( ) {
		return ResponseEntity.ok("Hello");
	}
	
	@PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDto> signUp(@Valid @RequestBody RequestDto loginRequest) throws Exception {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		
		String token = userService.signUp(username, password);
		return ResponseEntity.ok(new ResponseDto(username,token));
	}
}


