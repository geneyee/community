package com.dev.community.web.dto.user;

import com.dev.community.domain.user.Users;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserResponseDTO {
	
	private Long id;
	private String username;
	
	public UserResponseDTO(Long id, String username) {
		this.id = id;
		this.username = username;
	}
	
	public static UserResponseDTO of(Users user) {
		return new UserResponseDTO(user.getId(), user.getUsername());
	}

}
