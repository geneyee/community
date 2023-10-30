package com.dev.community.web.controller.user;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import com.dev.community.domain.user.Users;
import com.dev.community.service.user.UserService;

@WebMvcTest(UserController.class)
@WithUserDetails
public class UserControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private BindingResult bindingResult;
	
	@MockBean
	private Users user;
	
	@Test
	@DisplayName("get_회원가입_화면")
	void signupForm() throws Exception {
		mvc.perform(get("/user/signup"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(view().name("user/signup_form"));
	}
	
	@DisplayName("post_회원가입_성공")
	@Test
	void signup() throws Exception {
		mvc.perform(post("/user/signup")
				.param("username", "jake")
				.param("password1", "12345")
				.param("password2", "12345")
				.param("email", "test4@test.com")
				.with(csrf()))
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/posts/list"));
	}
	
	@DisplayName("post_회원가입_실패_비밀번호_불일치")
	@Test
	void signupFailPw() throws Exception {
		mvc.perform(post("/user/signup")
				.param("username", "jackson")
				.param("password1", "12345")
				.param("password2", "54321")
				.param("email", "test4@test.com")
				.with(csrf()))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(model().errorCount(1))
		.andExpect(view().name("user/signup_form"));
	}
	
	
//	@DisplayName("post_회원가입_성공")
//	@Test
//	void signup() throws Exception {
//		
//		String username = "jake";
//		String password1 = "12345";
//		String password2 = "12345";
////		String email = "test@test.com";
//		String email = "test4@test.com";
//		
//		UserCreateDTO dto= UserCreateDTO.builder()
//									.username(username)
//									.password1(password1)
//									.password2(password2)
//									.email(email)
//									.build();
//		
//		given(this.userService.create(any())).willReturn(user);
//				
//		mvc.perform(post("/user/signup").with(csrf()))
//		.andExpect(status().isOk())
//		.andExpect(view().name("redirect:/posts/list"));
//	}
	

//	@DisplayName("post_회원가입_실패_비밀번호_불일치")
//	@Test
//	public void signupFail() throws Exception {
//		
//		UserCreateDTO dto = UserCreateDTO.builder()
//									.username("tom")
//									.password1("1234")
//									.password2("1235")
//									.email("test3@test.com")
//									.build();
//		
////		given(this.userService.create(any())).willReturn(user);
//		
//		doReturn(user).when(this.userService).create(dto);
//									
//		ResultActions result =  mvc.perform(post("/user/singup")
//									.with(csrf()))
//									.andDo(print());
//		
//		result.andExpect(status().isOk())
//		.andExpect(view().name("user/signup_form"));
//	}
	
	
	

}
