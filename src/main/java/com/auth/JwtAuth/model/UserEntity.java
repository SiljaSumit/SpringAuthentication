package com.auth.JwtAuth.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name", unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(name = "role_id")
	private int roleId;

	public UserEntity(String username, String password) {
		this.username = username;
		this.password = password;
		this.roleId = 2;
	}

	public String getRole() {
		if (roleId == 1) {
			return Roles.ROLE_ADMIN.getAuthority();
		}
		return Roles.ROLE_USER.getAuthority();
	}

	@Override
	public String toString() {
		return "User -> " + username;
	}

}