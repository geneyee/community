package com.dev.community.service.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import com.dev.community.domain.user.Role;
import com.dev.community.domain.user.UserRepository;
import com.dev.community.domain.user.Users;
import com.dev.community.security.dto.UsersSecurityDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService{
	
	private final UserRepository userRepository;

	// loadUserByUsername 메소드는 username으로 비밀번호를 조회하여 리턴하는 메서드이다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO 로그인을 한다.
		
		// 1. username을 통해 DB에서 사용자를 조회한다.
		Optional<Users> entity = this.userRepository.findByUsername(username);
		
		// 2. 사용자가 없다면 예외를 발생시킨다.
		if(entity.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		} 
		// 3. 사용자가 있다면 사용자를 리턴한다.
		Users user = entity.get();
		
		// 4. 사용자의 권한을 저장할 리스트를 생성한다.
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		// 5. username이 admin이면 Role.ADMIN을 저장하고, 아니면 Role.USER를 저장한다.
		if(username.equals("admin")) {
			authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
		}

		// 6. 스프링 시큐리티의 User 객체를 리턴한다. 
		return new User(user.getUsername(), user.getPassword(), authorities);
	}

}


//	스프링 시큐리티는 loadUserByUsername 메서드를 통해 리턴된 User 객체를 통해 로그인을 처리한다.
//	로그인을 처리하는 과정은 다음과 같다.
// 1. 사용자가 입력한 username을 통해 DB에서 사용자 정보를 조회한다.
// 2. 사용자 정보가 있다면 사용자의 권한을 조회한다.
// 3. 사용자 정보와 사용자의 권한을 통해 스프링 시큐리티의 User 객체를 생성한다.
// 4. 사용자가 입력한 비밀번호와 DB에서 조회한 비밀번호를 비교한다.
// 5. 비밀번호가 일치하면 로그인을 처리한다.
// 6. 비밀번호가 일치하지 않으면 예외를 발생시킨다.
