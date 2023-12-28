package com.dev.community.service.user;

import java.util.Optional;

import org.springframework.boot.context.config.ConfigDataLocationNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dev.community.domain.user.UserRepository;
import com.dev.community.domain.user.Users;
import com.dev.community.exception.DataNotFoundException;
import com.dev.community.web.dto.user.UserCreateDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public Users create(String username, String email, String password) {

		Users user = Users.builder()
				.username(username)
				.email(email)
				.password(passwordEncoder.encode(password))
				.build();
		
		this.userRepository.save(user);
		
		return user;
	}

	public Users create(UserCreateDTO userCreateDTO) {
		// TODO 회원가입을 한다. DTO로 넘겨받음
		Users user = Users.builder()
				.username(userCreateDTO.getUsername())
				.password(passwordEncoder.encode(userCreateDTO.getPassword1()))
				.email(userCreateDTO.getEmail())
				.build();
		
		Users entity = this.userRepository.save(user);
		
		return entity;
	}
	
	public Users getUser(String username) {
		// TODO user 정보 조회
		Optional<Users> user = this.userRepository.findByUsername(username);
		
		if(user.isPresent()) {
			return user.get();
		} else {
			throw new DataNotFoundException("User not found");
		}
	}

}
