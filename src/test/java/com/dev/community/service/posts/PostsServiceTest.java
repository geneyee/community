package com.dev.community.service.posts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.posts.PostsRepository;
import com.dev.community.domain.user.UserRepository;
import com.dev.community.domain.user.Users;
import com.dev.community.service.user.UserSecurityService;
import com.dev.community.web.dto.posts.request.PostCreateRequestDTO;
import com.dev.community.web.dto.posts.request.PostsUpdateRequestDTO;
import com.dev.community.web.dto.posts.response.PostsResponseDTO;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@Transactional
public class PostsServiceTest {

	@Autowired
	private PostsService postsService;
	
	@Autowired
	private PostsRepository postsRepository;
	
	@Autowired
	private UserRepository userRepository;

	
	Principal principal = new Principal() {
		
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return username;
		}
	};
	
	String username = "test";
	
//	void setup() {
//		Optional<Posts> post = this.postsRepository.findById(1);
//		Users user = post.get().getAuthor();
//	}
	
	@DisplayName("글 저장")
	@Test
	void save() {
		// given
//		Users user = post.get().getAuthor();
		Optional<Users> users = this.userRepository.findByUsername(username);
		assertTrue(users.isPresent());
		Users user = users.get();
		log.info("user => {}", user.getUsername());
		
		PostCreateRequestDTO requestDTO = createDTO();
		Posts entity = requestDTO.toEntity();
		Posts target = this.postsRepository.save(entity);
		
		// when
		Posts expected = this.postsService.save(requestDTO, user);
		
		// then
		assertThat(expected.getContent()).isEqualTo(target.getContent());
	}
	
	@DisplayName("특정 게시글 id로 해당 글 조회하기")
	@Test
	void findById() {
		Integer id = 347;
		Optional<Posts> post = this.postsRepository.findById(id);
		assertTrue(post.isPresent());
		Posts target = post.get();
		
		PostsResponseDTO expected = this.postsService.findById(id);
		
		assertThat(expected.getId()).isEqualTo(target.getId());
	}
	
	@DisplayName("특정 게시글 id로 해당 글 제목, 내용 수정하기")
	@Test
	@WithMockUser(username = "test")
	@Transactional
	void modify() {
		// given
		//postId
		Integer id = 347;
		Optional<Posts> posts = this.postsRepository.findById(id);
		assertTrue(posts.isPresent());
		Posts post = posts.get();
		
		PostsUpdateRequestDTO reqeustDTO = updateDTO();
		Posts entity = reqeustDTO.toEntity();
		log.info("viewCount => {}", entity.getViewCount());
		Posts target = this.postsRepository.save(entity);
		
		log.info("principal.getName() => {}", principal.getName());
		
		// when
		PostsUpdateRequestDTO expected = this.postsService.modify(post.getId(), reqeustDTO, principal);
	
		// then
		assertThat(expected.getTitle()).isEqualTo(target.getTitle());
	}
	
	@DisplayName("특정 게시글 id로 해당 글 삭제하기")
	@Test
	void delete() {
		Integer id = 1;
		PostsResponseDTO responseDTO = this.postsService.findById(id);
		
		Boolean result = this.postsService.delete(responseDTO);
		
		assertThat(result).isTrue();
	}
	
	
	// create dto
	private PostCreateRequestDTO createDTO() {
		// 저장할 글 
		Optional<Posts> post = this.postsRepository.findById(1);
		
		// user
//		Users user = post.get().getAuthor();
		Optional<Users> users = this.userRepository.findByUsername(username);
		Users user = users.get();
		
		return PostCreateRequestDTO.builder()
				.title("글 저장 테스트 중 입니다.")
				.content("내용 : 글 저장 테스트 중 입니다.")
				.author(user)
				.viewCount(0)
				.build();
	}
	
	// update dto
	private PostsUpdateRequestDTO updateDTO() {
		PostCreateRequestDTO requestDTO = createDTO();

		PostsUpdateRequestDTO update = PostsUpdateRequestDTO.builder()
				.id(347)
				.title("글 수정 테스트 중 입니다.")
				.content("내용 : 글 수정 테스트 중 입니다.")
				.author(requestDTO.getAuthor())
				.modifiedDate(LocalDateTime.now())
				.viewCount(requestDTO.getViewCount())
				.build();
		return update;
	}
	
}
