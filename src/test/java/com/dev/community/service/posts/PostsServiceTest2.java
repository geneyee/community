package com.dev.community.service.posts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;

import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.posts.PostsRepository;
import com.dev.community.domain.user.Users;
import com.dev.community.web.dto.posts.request.PostCreateRequestDTO;
import com.dev.community.web.dto.posts.request.PostsUpdateRequestDTO;
import com.dev.community.web.dto.posts.response.PostsResponseDTO;

@ExtendWith(MockitoExtension.class)
public class PostsServiceTest2 {
	
	@Mock
	private PostsRepository postsRepository;
	
	@Mock
	private Principal principal;
	
	@InjectMocks
	private PostsService postsService;
	
	@Test
	@DisplayName("게시글 생성 성공 테스트")
	void save() {
		
		// given
		PostCreateRequestDTO dto = PostCreateRequestDTO.builder()
				.title("test title")
				.content("test content")
				.build();
		
		Users user = Users.builder()
				.username("testUser")
				.password("test")
				.email("testUser@test.com")
				.build();
		
		Posts post = Posts.builder()
				.title(dto.getTitle())
				.content(dto.getContent())
				.author(user)
				.createdDate(LocalDateTime.now())
				.viewCount(0)
				.build();
		
		when(postsRepository.save(any())).thenReturn(post);
		
		// when
		Posts savedPost = postsService.save(dto, user);
		
		// then
		assertEquals(savedPost.getTitle(), dto.getTitle());
		assertEquals(savedPost.getContent(), dto.getContent());
		assertEquals(savedPost.getAuthor(), user);	
		
	}
	
	@Test
	@DisplayName("게시글 조회 성공 테스트")
	void findById() {
        
        // given
		Integer postId = 1;
		
        Users user = Users.builder()
                .username("testUser")
                .password("test")
                .email("testUser@test.com")
				.build();
        
        Posts post = Posts.builder()
                .id(postId)
                .title("test title")
                .content("test content")
                .author(user)
                .createdDate(LocalDateTime.now())
                .viewCount(0)
                .build();
        
        when(postsRepository.findById(postId)).thenReturn(Optional.of(post));
        
        // when
        PostsResponseDTO responseDTO = postsService.findById(postId);
        
        // then
        assertNotNull(responseDTO);
        assertEquals(post.getId(), responseDTO.getId());
        assertEquals("test title", responseDTO.getTitle());
        assertEquals(user, responseDTO.getAuthor());
	}
	
	@Test
	@DisplayName("게시글 조회 실패 테스트")
	void findByIdFail() {
		
		// given
        Integer postId = 1;
        when(postsRepository.findById(postId)).thenReturn(Optional.empty());
	
        // when, then
        assertThrows(NoSuchElementException.class, () -> postsService.findById(postId));
        
        /*
        // when
        boolean thrown = false;
		try {
			postsService.findById(postId);
		} catch (NoSuchElementException e) {
			thrown = true;
		}
		// then
		assertEquals(true, thrown);
		*/
	}
	
	@Test
	@DisplayName("게시글 수정 성공 테스트")
	@WithMockUser(username = "testUser")
	void modify() {
		// given
		Integer postId = 1;
		String username = "testUser";
		when(principal.getName()).thenReturn(username);
		
		Users user = Users.builder()
				.username(username)
                .password("test")
                .email("testUser@test.com")
                .build();
		
		Posts post = Posts.builder()
				.id(postId)
				.title("test title")
				.content("test content")
				.author(user)
				.createdDate(LocalDateTime.now())
				.viewCount(0)
				.build();
		
		when(postsRepository.findById(postId)).thenReturn(Optional.of(post));
		when(postsRepository.save(any())).thenReturn(post);
		
		PostsUpdateRequestDTO dto = PostsUpdateRequestDTO.builder()
				.title("test title update")
				.content("test content update")
				.build();
		
		// when
		PostsUpdateRequestDTO updatedDTO = postsService.modify(postId, dto, principal);
		
		// then
		assertNotNull(updatedDTO);
		assertEquals(dto.getTitle(), updatedDTO.getTitle());
		assertEquals(dto.getContent(), updatedDTO.getContent());
		assertEquals(user, updatedDTO.getAuthor());
		
	}
	
	@Test
	@DisplayName("게시글 수정 실패 테스트_권한 없음")
	@WithMockUser(username = "testUser")
	void modifyFail() {
		
		// given
		Integer postId = 1;
		when(principal.getName()).thenReturn("testUser");
		
		Users user = Users.builder()
                .username("anotherUser")
                .password("test")
                .email("testUser@test.com")
                .build();
		
		Posts post = Posts.builder()
				.id(postId)
				.title("test title")
				.content("test content")
				.author(user)
				.createdDate(LocalDateTime.now())
				.viewCount(0)
				.build();
		
		when(postsRepository.findById(postId)).thenReturn(Optional.of(post));
	
	    PostsUpdateRequestDTO dto = PostsUpdateRequestDTO.builder()
                .title("test title update")
                .content("test content update")
                .build();
	    
	    // when, then
	    ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
	    		() -> postsService.modify(postId, dto, principal));
	    
	    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
	    assertEquals("400 BAD_REQUEST \"수정권한이 없습니다.\"", exception.getMessage());
	}
	
	@Test
	@DisplayName("게시글 삭제 성공 테스트")
	void delete() {
		// given
		Integer postId = 1;
		
		Users user = Users.builder()
                .username("testUser")
                .password("test")
                .email("testUser@test.com")
                .build();
		
		Posts post = Posts.builder()
				.id(postId)
				.author(user)
				.build();
		
		PostsResponseDTO responseDTO = new PostsResponseDTO(post);
		
        // When
        Boolean result = postsService.delete(responseDTO);

        // Then
        assertTrue(result);
        verify(postsRepository, times(1)).delete(any(Posts.class));
	}
	
	@Test
	@DisplayName("게시글 전체 목록")
	void findAll() {
		// given
		int page = 0;
		String keyword = "";
		
		List<Posts> postsList = new ArrayList<>();
		
		for(int i = 0; i < 20; i++) {
			Users user = Users.builder()
					.username("testUser" + i)
					.password("test")
					.email("testUser" + i + "@test.com")
					.build();
			
			Posts post = Posts.builder()
					.id(i)
					.title("test title" + i)
					.content("test content" + i)
					.author(user)
					.createdDate(LocalDateTime.now())
					.viewCount(0)
					.build();
			
			postsList.add(post);
		}
		
		Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("id")));
		
		Page<Posts> expectedPage = new PageImpl<>(postsList, pageable, postsList.size());

		when(postsRepository.findAllByKeyword(anyString(), any(Pageable.class))).thenReturn(expectedPage);
		
		// when
		Page<Posts> resultPage = postsService.findAllDesc(page, keyword);
		
		// then
		assertNotNull(resultPage);
		assertEquals(expectedPage, resultPage);
		verify(postsRepository, times(1)).findAllByKeyword(eq(keyword), any(Pageable.class));
	}
	
}
