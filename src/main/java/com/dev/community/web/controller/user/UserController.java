package com.dev.community.web.controller.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.community.service.user.UserService;
import com.dev.community.web.dto.user.UserCreateDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {

	private final UserService userService;

	// 회원가입 화면
	@GetMapping("/signup")
	public String signup(UserCreateDTO userCreateDTO) {
		return "user/signup_form";
	}

	// 회원가입
	@PostMapping("/signup")
	public String signup(@Valid UserCreateDTO userCreateDTO, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "user/signup_form";
		}
		
		if(!userCreateDTO.getPassword1().equals(userCreateDTO.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
			return "user/signup_form";
		}
		try {
			userService.create(userCreateDTO);
			//userService.create(userCreateDTO.getUsername(), userCreateDTO.getPassword1(), userCreateDTO.getEmail());
		}catch (DataIntegrityViolationException e){
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
			return "user/signup_form";
		} catch(Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "user/signup_form";
		}
		
		
		return "redirect:/posts/list";
	}
	
	// 로그인
	@GetMapping("/login")
	public String login(String error, String logout) {
		log.info("login get------------------");
		log.info("logout => {}", logout);
		return "user/login_form";
	}
	

}
