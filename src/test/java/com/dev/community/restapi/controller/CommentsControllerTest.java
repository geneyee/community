package com.dev.community.restapi.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.dev.community.domain.comment.CommentRepository;
import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.posts.PostsRepository;
import com.dev.community.domain.user.UserRepository;
import com.dev.community.domain.user.Users;
import com.dev.community.web.dto.comment.request.CommentCreateRequestDTO;
import com.dev.community.web.dto.comment.request.CommentUpdateRequestDTO;
import com.dev.community.web.dto.comment.response.CommentResponseDTO;
import com.dev.community.web.dto.user.UserResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@WebMvcTest(CommentsController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CommentsControllerTest {
	
	private String username = "test";
	
	private Principal principal = new Principal() {
		
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return username;
		}
	};
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostsRepository postsRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@DisplayName("글 347번에 댓글 저장")
	@WithMockUser(username = "test")
	@Test
	void save() throws JsonProcessingException, Exception {
		// given
		Integer postsId = 347;
		// user 임의로 
		Users user = this.commentRepository.findByAuthorId(postsId);
		Optional<Posts> posts = this.postsRepository.findById(postsId);
		Posts post = posts.get();
		String content = "controller save test content";
		
		CommentCreateRequestDTO requestDTO = CommentCreateRequestDTO.builder()
				.content(content)
				.author(user)
				.createdDate(LocalDateTime.now())
				.postsId(post.getId())
				.build();
		
		
		// when
		mockMvc.perform(MockMvcRequestBuilders.post("/posts/347/comments")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestDTO)))
		.andExpect(status().isOk())
		.andDo(print());
	}
	
	
	@DisplayName("347번 글의 전체 댓글 목록 조회")
	@Test
	@WithMockUser(username = "test")
	void commentList() throws JsonProcessingException, Exception {
		
		// given
		Integer postsId = 347;
		// user 임의로 
		Users user = this.commentRepository.findByAuthorId(postsId);
		log.info("user => {}", user.toString());
//		UserResponseDTO userResponseDTO = UserResponseDTO.of(user);
		UserResponseDTO userResponseDTO = new UserResponseDTO(user.getId(), user.getUsername());
		log.info("userdto", userResponseDTO.getId());
		
		List<CommentResponseDTO> responseDTO = List.of(
				CommentResponseDTO.builder()
				.id(200)
				.postsId(347)
				.content("comment controller test1")
				.userResponseDTO(userResponseDTO)
				.createdDate(LocalDateTime.now())
				.build(), 
				CommentResponseDTO.builder()
				.id(201)
				.postsId(347)
				.content("comment controller test2")
				.userResponseDTO(userResponseDTO)
				.createdDate(LocalDateTime.now())
				.build());
		
		// when 
		mockMvc.perform(get("/posts/347/comments")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(responseDTO)))
//		.andExpect(jsonPath("$.data[0].content").value(equalTo("comment controller test1")))
//		.andExpect(jsonPath("$.data[1].content").value(equalTo("comment controller test2")))
		.andExpect(status().isOk())
		.andDo(print());
	}
	
	@DisplayName("47번 댓글 수정")
	@Test
	@WithMockUser(username = "test")
	void update() throws JsonProcessingException, Exception {
		//given
		Integer postsId = 347;
		// user 임의로 
		Users user = Users.builder()
			.username(username)
			.build();

		Optional<Posts> posts = this.postsRepository.findById(postsId);
		Posts post = posts.get();
		
//		CommentResponseDTO savedComment = CommentResponseDTO.builder()
//				.id(100)
//				.postsId(postsId)
//				.content("comment controller test1")
//				.userResponseDTO(getUserDTO(id, username))
//				.createdDate(LocalDateTime.now())
//				.build();
//		
//		Integer updateId = savedComment.getId();
//		log.info("updateId => {}", updateId);
		String expectedContent = "update test2";
		
		CommentUpdateRequestDTO requestDTO = CommentUpdateRequestDTO.builder()
				.id(47)
				.postsId(postsId)
				.content(expectedContent)
				.modifiedDate(LocalDateTime.now())
				.build();
		
		mockMvc.perform(patch("/comment/47")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestDTO)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content").value(equalTo(requestDTO.getContent())))
		.andDo(print());
		
	}
	
	// CommentResponseDTO에서 User대신 dto사용 
	private UserResponseDTO getUserDTO(Long id, String username) {
		return new UserResponseDTO(id, username);
	}

}
