package com.dev.community.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.dev.community.domain.user.UserRepository;
import com.dev.community.domain.user.Users;
import com.dev.community.web.dto.user.UserCreateDTO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
public class UserServiceTest {
	
	@Autowired
	UserRepository userRepository;
	
//	private UserCreateDTO createDTO;
	
	@Autowired
	UserService userService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

//	private Users user;

//	@Test
//	@DisplayName("회원가입")
//	void save() {
//		//given
//		UserCreateDTO dto = UserCreateDTO.builder()
//				.email("email!!@email.com")
//				.username("testname")
//				.password1("1234")
//				.password2("1234")
//				.build();
//		
//		System.out.println(dto.getUsername());
//		
//		Users entity = Users.builder()
//				.username(dto.getUsername())
//				.password(passwordEncoder.encode(dto.getPassword1()))
//				.email(dto.getEmail())
//				.build();
//		
//		Users user = userRepository.save(entity);
//		System.out.println("given : "+user.getUsername());
//		
//		// when
//		Users result = userService.create(dto);
//		System.out.println("result : "+result.getUsername());
//		
//		// then
//		assertThat(result.getUsername()).isEqualTo(user.getUsername());
//		
//		
//		
//		
//	}

}
