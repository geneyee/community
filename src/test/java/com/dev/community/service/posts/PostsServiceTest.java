package com.dev.community.service.posts;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.posts.PostsRepository;
import com.dev.community.domain.user.Users;
import com.dev.community.web.dto.posts.request.PostCreateRequestDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class PostsServiceTest {

	@Autowired
	PostsService postsService;
	
	Users user;
	
	@Autowired
	PostsRepository postsRepository;
	
	@Test
	@WithMockUser
	void save() {
		// given
		String title = "test";
		String content = "content";
		Integer viewCount=0;
		
		Posts post = Posts.builder()
				.title(title)
				.author(user)
				.content(content)
				.build();
		
		/// 이러면 user가 null.....
	
		PostCreateRequestDTO dto = new PostCreateRequestDTO(title, content, post.getAuthor(), viewCount);
		
		Posts entity = dto.toEntity();
		
		Posts given = postsRepository.save(entity);
		
		// when
		Integer getId = postsService.save(dto, user);
		
		// then
		assertThat(given.getId()).isEqualTo(getId);
		
	}
}
