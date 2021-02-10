package com.auth.JwtAuth.model;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_USER;

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return name();
	}
}
