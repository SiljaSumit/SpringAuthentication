package com.auth.JwtAuth.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ResponseDto implements Serializable {

	private static final long serialVersionUID = -6702796537970938832L;
	private String userName;
	private String token;
}
