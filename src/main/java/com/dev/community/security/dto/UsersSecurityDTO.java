package com.dev.community.security.dto;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UsersSecurityDTO extends User{
	
	private Integer id;
	private String username;
	private String email;
	private String password;
	private boolean social;
	
	public UsersSecurityDTO(String username, String password, String email, boolean social,
			List<GrantedAuthority> authorities) {
		super(username, password, authorities);

	}

}
