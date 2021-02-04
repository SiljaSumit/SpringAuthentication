package com.auth.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RequestDto implements Serializable {
	private static final long serialVersionUID = -43797581924569273L;
	@NotNull(message = "username is missing")
	@Size(min = 4, max = 255, message = "Minimum username length: 4 characters")
	private String username;

	@NotNull(message = "password is missing")
	@Size(min = 8, message = "Minimum password length: 8 characters")
	private String password;

	public RequestDto(String username, String password) {
		this.username = username;
		this.password = password;
		System.out.println("user from dto: "+username);
	}

}
