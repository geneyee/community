package com.dev.community.domain.user;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ToString
@NoArgsConstructor
@Getter
@Entity
public class Users {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String username;
	
	private String password;
	
	@Column(unique = true)
	private String email;
	
//	@Enumerated(EnumType.STRING)
//	@Column(nullable = false)
//	private Role role;
	
	@Builder
	public Users(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

}
