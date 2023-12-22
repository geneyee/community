package com.dev.community.web.controller.posts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.security.Principal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.user.Users;
import com.dev.community.service.posts.PostsService;
import com.dev.community.service.user.UserService;
import com.dev.community.web.dto.posts.request.PostCreateRequestDTO;
import com.dev.community.web.dto.posts.request.PostsUpdateRequestDTO;
import com.dev.community.web.dto.posts.response.PostsResponseDTO;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(PostsController.class)
public class PostsControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PostsService postsService;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private Principal principal;
	
	@MockBean
	private BindingResult bindingResult;
	
	
	
	@Test
	@DisplayName("글쓰기 post")
	@WithMockUser(username = "test", roles = "USER")
	void createPost() throws Exception {
		// given
		given(userService.getUser(anyString())).willReturn(new Users());
		given(postsService.save(any(), any())).willReturn(Posts.builder()
				.id(1)
				.title("test title")
				.content("test content")
				.author(new Users())
				.build());

		// when, then
		mockMvc.perform(post("/posts/create")
				.param("title", "test title")
				.param("content", "test content")
				.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/posts/list"));
	}
	
	@Test
	@DisplayName("글쓰기 post validation error")
	@WithMockUser(username = "test", roles = "USER")
	void createPostInValid() throws Exception {

		mockMvc.perform(post("/posts/create")
				.param("title", "")
				.param("content", "test content")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(view().name("post/posts_form"));

		verify(postsService, never()).save(any(PostCreateRequestDTO.class), any(Users.class));
	}
	
	@Test
	@DisplayName("글 수정 get 권한없음")
	@WithMockUser(username = "test")
	void modify400() throws Exception {
		// given
		Integer postId = 1;
		PostsResponseDTO responseDTO = new PostsResponseDTO();
		responseDTO.setAuthor(Users.builder()
				.username("error")
				.build());
		given(postsService.findById(postId)).willReturn(responseDTO);
		
		// when, then
		mockMvc.perform(get("/posts/modify/{id}", postId)
				.with(user("test")))
		.andExpect(status().isBadRequest())
		.andExpect(result -> assertEquals("400 BAD_REQUEST \"수정권한이 없습니다.\"", result.getResolvedException().getMessage()));
	}
	
	@Test
	@DisplayName("글 수정 post")
	@WithMockUser(username = "test", roles = "USER")
	void modifyPost() throws Exception {
		Integer postId = 1;
		
		given(postsService.modify(anyInt(), any(), any())).willReturn(PostsUpdateRequestDTO
																	.builder()
																	.id(postId)
																	.title("test title")
																	.content("test content")
																	.build());
														
		mockMvc.perform(post("/posts/modify/{id}", postId)
				.param("title", "test title")
				.param("content", "test content")
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/posts/"+postId));
		
	}
	


}
